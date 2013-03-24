package com.worldcretornica.cloneme;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import com.worldcretornica.cloneme.compat.IBlockCompat;
import com.worldcretornica.cloneme.events.CloneBlockBreakEvent;
import com.worldcretornica.cloneme.events.CloneBlockPlaceEvent;
import com.worldcretornica.cloneme.events.CloneHangingBreakByEntityEvent;
import com.worldcretornica.cloneme.events.CloneHangingPlaceEvent;
import com.worldcretornica.cloneme.events.ClonePlayerBucketEmptyEvent;
import com.worldcretornica.cloneme.events.ClonePlayerBucketFillEvent;
import com.worldcretornica.cloneme.events.ClonePlayerInteractEntityEvent;

import fr.neatmonster.nocheatplus.checks.CheckType;

public class ScheduledBlockChange implements Runnable {

	private Clone clone;
	private CloneMe plugin;
	private Block block;
	private Player player;
	private ChangeType change;
	private Event event;
	
	public enum ChangeType {
		BLOCK_BREAK, 
		BLOCK_PLACE, 
		INTERACT, 
		BUCKET_FILL, 
		BUCKET_EMPTY, 
		HANGING_PLACE,
		HANGING_BREAK,
		INTERACT_ENTITY
	}
	
	private IBlockCompat blockcompat;

	public ScheduledBlockChange(Clone clone, CloneMe plugin, Player player, Block block, ChangeType changes, Event event) {
		this.clone = clone;
		this.plugin = plugin;
		this.player = player;
		this.block = block;
		this.change = changes;
		this.event = event;
	}
	
	public ScheduledBlockChange(Clone clone, CloneMe plugin, Player player, ChangeType changes, Event event) {
		this.clone = clone;
		this.plugin = plugin;
		this.player = player;
		this.change = changes;
		this.event = event;
	}

	@Override
	public void run() {

		Block newblock = null;
		boolean alreadyexempt = false;
		
		if(block != null)
			newblock = clone.getNewBlockLocation(block);

		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		String cbversion = packageName.substring(packageName.lastIndexOf('.') + 1);
		if (cbversion.equals("craftbukkit")) 
		{
			cbversion = "pre";
		}
		
		switch (change) {
		case INTERACT:
						
			//if its the same block, don't interact twice
			if(!block.getLocation().equals(newblock.getLocation()))
			{
				//so we don't open tons of chests at the same time
				if(!(newblock.getState() instanceof InventoryHolder) && 
						newblock.getType() != Material.ANVIL &&
						newblock.getType() != Material.WORKBENCH&&
						newblock.getType() != Material.BEACON)
				{
					boolean interacted = false;
					
					try 
					{
						final Class<?> clazz = Class.forName("com.worldcretornica.cloneme.compat." + cbversion + ".BlockCompat");
						if (IBlockCompat.class.isAssignableFrom(clazz)) 
						{
							blockcompat = (IBlockCompat) clazz.getConstructor().newInstance();
							interacted = blockcompat.interact(newblock, player);
						}
					}
					catch (final Exception e) 
					{
						plugin.getLogger().warning("Clone block interactions not find support for this CraftBukkit version. Check for an update.");
						plugin.getLogger().info("Update hopefully available at http://dev.bukkit.org/server-mods/cloneme");
						e.printStackTrace();
		            }
					
					if(!interacted)
					{
						PlayerInteractEvent pievent = (PlayerInteractEvent) event;
						Block oldblock = pievent.getClickedBlock();
						Block seedblock = oldblock.getRelative(pievent.getBlockFace());
						
						if(seedblock.getType() == Material.COCOA ||
								seedblock.getType() == Material.SEEDS ||
								seedblock.getType() == Material.CARROT ||
								seedblock.getType() == Material.POTATO ||
								seedblock.getType() == Material.NETHER_WARTS ||
								seedblock.getType() == Material.MELON_SEEDS ||
								seedblock.getType() == Material.PUMPKIN_SEEDS)
						{
							newblock = clone.getNewBlockLocation(oldblock);
							Block newplacedblock = newblock.getRelative(pievent.getBlockFace());
							
							CloneBlockPlaceEvent placeevent = new CloneBlockPlaceEvent(newplacedblock, newplacedblock.getState(), newblock, player.getItemInHand(), player, true);
	
							if(CloneMe.usingNCP)
							{
								if(CloneMe.isncpExempt(player, CheckType.ALL))
									alreadyexempt = true;
								else
									CloneMe.ncpExempt(player, CheckType.ALL);
							}
							
							plugin.getServer().getPluginManager().callEvent(placeevent);
							
							if(CloneMe.usingNCP)
							{
								if(!alreadyexempt)
								{}//CloneMe.ncpUnexempt(player, CheckType.ALL);
								else
									alreadyexempt = false;
									
							}
							
							
							if(placeevent.isCancelled() || !clone.placeBlock(seedblock, player)) {
								player.sendMessage(ChatColor.RED + "Clone could not place block");
							}
						}
					}
				}
			}
			
			break;
			
		case INTERACT_ENTITY:
			
			PlayerInteractEntityEvent pieevent = (PlayerInteractEntityEvent) event;
			
			Location newentityloc = null;
			
			try
			{
				newentityloc = clone.getNewLocation(pieevent.getRightClicked().getLocation());
			}
			catch(IllegalArgumentException e)
			{
				pieevent.getPlayer().sendMessage(e.getMessage());
				return;
			}
			
			Hanging foundentity = null;
			
			for(Entity entity : newentityloc.getBlock().getChunk().getEntities())
			{
				Location foundentityloc = entity.getLocation().clone();

				if(entity instanceof Hanging && foundentityloc.distance(newentityloc) < 0.4 &&
						entity.getType().equals(pieevent.getRightClicked().getType()))
				{
					foundentity = (Hanging) entity;
				}
			}
			
			if(foundentity != null)
			{
				ClonePlayerInteractEntityEvent playerintereactentityevent = new ClonePlayerInteractEntityEvent(pieevent.getPlayer(), foundentity);
				
				if(CloneMe.usingNCP)
				{
					if(CloneMe.isncpExempt(player, CheckType.ALL))
						alreadyexempt = true;
					else
						CloneMe.ncpExempt(player, CheckType.ALL);
				}
				
				plugin.getServer().getPluginManager().callEvent(playerintereactentityevent);
	
                if(CloneMe.usingNCP)
				{
					if(!alreadyexempt)
						{}//CloneMe.ncpUnexempt(player, CheckType.ALL);
					else
						alreadyexempt = false;
						
				}
				
				if (playerintereactentityevent.isCancelled()) {
					player.sendMessage(ChatColor.RED + "Clone could not interact with entity");
				}
				else
				{
					if(foundentity instanceof ItemFrame)
					{
						ItemFrame itemframe = (ItemFrame) foundentity;
						ItemFrame olditemframe = (ItemFrame) pieevent.getRightClicked();
						itemframe.setItem(olditemframe.getItem().clone());
						itemframe.setRotation(olditemframe.getRotation());
					}
				}
			}
			
			break;
			
		case BLOCK_BREAK:

			CloneBlockBreakEvent breakevent = new CloneBlockBreakEvent(newblock, player);

			if(CloneMe.usingNCP)
			{
				if(CloneMe.isncpExempt(player, CheckType.ALL))
					alreadyexempt = true;
				else
					CloneMe.ncpExempt(player, CheckType.ALL);
			}
			
			plugin.getServer().getPluginManager().callEvent(breakevent);
			
			if(CloneMe.usingNCP)
			{
				if(!alreadyexempt)
					{}//CloneMe.ncpUnexempt(player, CheckType.ALL);
				else
					alreadyexempt = false;
					
			}

			if (breakevent.isCancelled() || !clone.breakBlock(block, player)) {
				player.sendMessage(ChatColor.RED + "Clone could not break block");
			}

			break;
		case BLOCK_PLACE:

			BlockPlaceEvent pevent = (BlockPlaceEvent) event;
			Block placedagainstblock = null;
			if (pevent.getBlockAgainst() != null)
				placedagainstblock = pevent.getBlockAgainst();
			Block newplacedagainstblock = null;

			if (placedagainstblock != null)
				newplacedagainstblock = clone.getNewBlockLocation(pevent.getBlockAgainst());

			CloneBlockPlaceEvent placeevent = new CloneBlockPlaceEvent(newblock, newblock.getState(), newplacedagainstblock, pevent.getItemInHand(), player, true);

			if(CloneMe.usingNCP)
			{
				if(CloneMe.isncpExempt(player, CheckType.BLOCKPLACE))
					alreadyexempt = true;
				else
					CloneMe.ncpExempt(player, CheckType.ALL);
			}
			
			plugin.getServer().getPluginManager().callEvent(placeevent);

			if(CloneMe.usingNCP)
			{
				if(!alreadyexempt)
					{}//CloneMe.ncpUnexempt(player, CheckType.ALL);
				else
					alreadyexempt = false;
					
			}
			
			if (!placeevent.canBuild() || placeevent.isCancelled() || !clone.placeBlock(block, player)) {
				player.sendMessage(ChatColor.RED + "Clone could not place block");
				player.sendMessage(" exempt? " + CloneMe.isncpExempt(player, CheckType.BLOCKPLACE));
			}

			break;
			
		case HANGING_PLACE:

			HangingPlaceEvent hpevent = (HangingPlaceEvent) event;

			Block newhangingblock = clone.getNewBlockLocation(hpevent.getBlock());
			Hanging newentity = null;

			//newentity = (Hanging) newhangingblock.getWorld().spawn(newloc.clone().add(0, 0, 0.5), hpevent.getEntity().getType().getEntityClass());
						
			try 
			{
				final Class<?> clazz = Class.forName("com.worldcretornica.cloneme.compat." + cbversion + ".BlockCompat");
				if (IBlockCompat.class.isAssignableFrom(clazz)) 
				{
					blockcompat = (IBlockCompat) clazz.getConstructor().newInstance();
					newentity = blockcompat.placehanging(hpevent.getEntity(), hpevent.getEntity().getWorld(), newhangingblock.getX(), newhangingblock.getY(), newhangingblock.getZ(), clone.getNewBlockFace(hpevent.getBlockFace()));
				}
			}
			catch (final Exception e) 
			{
				plugin.getLogger().warning("Clone block interactions not find support for this CraftBukkit version. Check for an update.");
				plugin.getLogger().info("Update hopefully available at http://dev.bukkit.org/server-mods/cloneme");
            }
			
			CloneHangingPlaceEvent hangingevent = new CloneHangingPlaceEvent(newentity, player, newhangingblock, clone.getNewBlockFace(hpevent.getBlockFace()));
			
			if(CloneMe.usingNCP)
			{
				if(CloneMe.isncpExempt(player, CheckType.ALL))
					alreadyexempt = true;
				else
					CloneMe.ncpExempt(player, CheckType.ALL);
			}
			
			plugin.getServer().getPluginManager().callEvent(hangingevent);

			if(CloneMe.usingNCP)
			{
				if(!alreadyexempt)
					{}//CloneMe.ncpUnexempt(player, CheckType.ALL);
				else
					alreadyexempt = false;
					
			}
			
			if (hangingevent.isCancelled()) 
			{
				if (newentity != null)
					newentity.remove();
				player.sendMessage(ChatColor.RED + "Clone could not place hanging entity");
			}

			break;
		
		case HANGING_BREAK:
			
			HangingBreakByEntityEvent hbbeevent = (HangingBreakByEntityEvent) event;
			
			Location newhangingentityloc = null;
			
			try
			{
				newhangingentityloc = clone.getNewLocation(hbbeevent.getEntity().getLocation());
			}
			catch(IllegalArgumentException e)
			{
				Entity entity = hbbeevent.getRemover();
				if(entity instanceof Player)
					((Player) entity).sendMessage(e.getMessage());
				return;
			}
			
			Hanging foundhangingentity = null;
			
			for(Entity entity : newhangingentityloc.getBlock().getChunk().getEntities())
			{
				Location foundentityloc = entity.getLocation().clone();

				if(entity instanceof Hanging && foundentityloc.distance(newhangingentityloc) < 0.4 &&
						entity.getType().equals(hbbeevent.getEntity().getType()))
				{
					foundhangingentity = (Hanging) entity;
				}
			}
			
			if(foundhangingentity != null)
			{
				CloneHangingBreakByEntityEvent breakhangingevent = new CloneHangingBreakByEntityEvent(foundhangingentity, hbbeevent.getRemover());
				
				if(CloneMe.usingNCP)
				{
					if(CloneMe.isncpExempt(player, CheckType.ALL))
						alreadyexempt = true;
					else
						CloneMe.ncpExempt(player, CheckType.ALL);
				}
				
				plugin.getServer().getPluginManager().callEvent(breakhangingevent);
				
				if(CloneMe.usingNCP)
				{
					if(!alreadyexempt)
						{}//CloneMe.ncpUnexempt(player, CheckType.ALL);
					else
						alreadyexempt = false;
				}
	
				if (breakhangingevent.isCancelled()) {
					player.sendMessage(ChatColor.RED + "Clone could not break block");
				}
				else
				{
					foundhangingentity.remove();
				}
			}
			
			break;
			
		case BUCKET_EMPTY:
			
			PlayerBucketEmptyEvent pbeevent = (PlayerBucketEmptyEvent) event;
			
			Block emptiedonblock = null;
			if(pbeevent.getBlockClicked() != null)
				emptiedonblock = pbeevent.getBlockClicked();
			Block newemptiedonblock = null;
			
			if(emptiedonblock != null)
				newemptiedonblock = clone.getNewBlockLocation(pbeevent.getBlockClicked());
			
			BlockFace newblockface = clone.getNewBlockFace(pbeevent.getBlockFace());
			
			ClonePlayerBucketEmptyEvent bucketemptyevent = new ClonePlayerBucketEmptyEvent(player, newemptiedonblock, newblockface, pbeevent.getBucket(), pbeevent.getItemStack().clone());
			
			if(CloneMe.usingNCP)
			{
				if(CloneMe.isncpExempt(player, CheckType.ALL))
					alreadyexempt = true;
				else
					CloneMe.ncpExempt(player, CheckType.ALL);
			}
			
			plugin.getServer().getPluginManager().callEvent(bucketemptyevent);
			
			if(CloneMe.usingNCP)
			{
				if(!alreadyexempt)
					{}//CloneMe.ncpUnexempt(player, CheckType.ALL);
				else
					alreadyexempt = false;
					
			}
			
			block = emptiedonblock.getRelative(pbeevent.getBlockFace());
						
			if(bucketemptyevent.isCancelled() || !clone.placeBlock(block, player))
			{
				player.sendMessage(ChatColor.RED + "Clone could not empty bucket");
			}
			
			break;
			
		case BUCKET_FILL:
			
			PlayerBucketFillEvent bfevent = (PlayerBucketFillEvent) event;
			
			Block fillingblock = null;
			if(bfevent.getBlockClicked() != null)
				fillingblock = bfevent.getBlockClicked();
			Block newfillingblock = null;
			
			if(fillingblock != null)
				newfillingblock = clone.getNewBlockLocation(bfevent.getBlockClicked());
			
			BlockFace newfilledblockface = clone.getNewBlockFace(bfevent.getBlockFace());
			
			ClonePlayerBucketFillEvent bucketfillevent = new ClonePlayerBucketFillEvent(player, newfillingblock, newfilledblockface, bfevent.getBucket(), bfevent.getItemStack().clone());
			
			if(CloneMe.usingNCP)
			{
				if(CloneMe.isncpExempt(player, CheckType.ALL))
					alreadyexempt = true;
				else
					CloneMe.ncpExempt(player, CheckType.ALL);
			}
			
			plugin.getServer().getPluginManager().callEvent(bucketfillevent);
			
			if(CloneMe.usingNCP)
			{
				if(!alreadyexempt)
					{}//CloneMe.ncpUnexempt(player, CheckType.ALL);
				else
					alreadyexempt = false;
					
			}
			
			block = fillingblock.getRelative(bfevent.getBlockFace());
						
			if(bucketfillevent.isCancelled() || !clone.breakBlock(block, player))
			{
				player.sendMessage(ChatColor.RED + "Clone could not fill bucket");
			}
			
			break;
		}
	}

}
