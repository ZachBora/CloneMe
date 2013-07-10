package com.worldcretornica.cloneme;

import java.util.logging.Logger;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.entity.EntityHumanNPC.PlayerNPC;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


/*
import ch.spacebase.npccreatures.npcs.NPCManager;
import ch.spacebase.npccreatures.npcs.NPCType;
import ch.spacebase.npccreatures.npcs.entity.HumanNPC;
*/

import com.worldcretornica.cloneme.compat.INPC;
import com.worldcretornica.cloneme.utils.CMUtils;

public class Clone {

	public final Logger logger = Logger.getLogger("Minecraft"); // TODO

	public enum Direction 
	{
		NONE, EAST, WEST, SOUTH, NORTH,
		/*
		 * NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST
		 */
	}

	private String owner;
	public long xtrans;
	public long ytrans;
	public long ztrans;
	public int rotation;
	public Direction mirror;
	private String name;
	private Location startingLocation;
	//private HumanNPC npc;
	//private org.bukkit.entity.HumanEntity npc;
	private NPC npc;
	private ClonedPlayer clonedplayer;
	private Location currentLocation;
	private INPC inpc = null;
	//private IBlockCompat blockcompat = null;
	
	
	public Clone(String owner, long xtrans, long ytrans, long ztrans, int rotation, Direction mirror, 
			Location startingLocation, World world, /*NPCManager manager,*/ String name, ClonedPlayer cp) throws IllegalArgumentException 
	{
		this.owner = owner;
		this.xtrans = xtrans;
		this.ytrans = ytrans;
		this.ztrans = ztrans;
		this.rotation = rotation;
		this.startingLocation = startingLocation;
		this.currentLocation = startingLocation;
		
		if (mirror == Direction.WEST) {
			this.mirror = Direction.EAST;
		} else if (mirror == Direction.SOUTH) {
			this.mirror = Direction.NORTH;
		} else {
			this.mirror = mirror;
		}

		this.name = name;
		this.clonedplayer = cp;

		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, this.name);
		
		npc.spawn(getNewLocation(this.currentLocation.clone()));
		
		if (npc.getBukkitEntity() instanceof PlayerNPC) {
			((PlayerNPC) npc.getBukkitEntity()).setGravityEnabled(false);
		}
		
		((Player) npc.getBukkitEntity()).setGameMode(Bukkit.getPlayer(owner).getGameMode());
		
		copyEquipment();
		
		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		String cbversion = packageName.substring(packageName.lastIndexOf('.') + 1);
		if (cbversion.equals("craftbukkit")) 
		{
			cbversion = "pre";
		}
				
		try 
		{
			final Class<?> clazz = Class.forName("com.worldcretornica.cloneme.compat." + cbversion + ".NPC");
			if (INPC.class.isAssignableFrom(clazz)) 
			{
				inpc = (INPC) clazz.getConstructor().newInstance();
			}
		}
		catch (final Exception e) 
		{
			logger.warning("CloneMe did not find support for this CraftBukkit version. Check for an update.");
			logger.info("Update hopefully available at http://dev.bukkit.org/server-mods/cloneme");
			e.printStackTrace();
        }
		
		/*if (manager != null) {
			try{
				npc = (HumanNPC) manager.spawnNPC(owner, getNewLocation(new Location(world, startingLocation.getX(), startingLocation.getY(), startingLocation.getZ())), NPCType.HUMAN);
			}catch(IllegalArgumentException e)
			{
				this.remove();
				throw new IllegalArgumentException(e.getMessage());
			}
			npc.setName(name);
			((net.minecraft.server.EntityHuman) npc.getHandle()).name = name;
		}*/
	}
	
	public void copyEquipment()
	{
		Player powner = Bukkit.getPlayerExact(owner);
		Player pclone = (Player) npc.getBukkitEntity();
		
		ItemStack is = null;
		
		if(powner.getInventory() != null)
		{
			if(powner.getInventory().getBoots() != null)
			{
				is = powner.getInventory().getBoots().clone();
				if(is != null)
				{
					is.setItemMeta(powner.getInventory().getBoots().getItemMeta().clone());
					pclone.getInventory().setBoots(is);
				}
			}
			
			if(powner.getInventory().getChestplate() != null)
			{
				is = powner.getInventory().getChestplate().clone();
				if(is != null)
				{
					is.setItemMeta(powner.getInventory().getChestplate().getItemMeta().clone());
					pclone.getInventory().setChestplate(is);
				}
			}
	
			if(powner.getInventory().getHelmet() != null)
			{
				is = powner.getInventory().getHelmet().clone();
				if(is != null)
				{
					is.setItemMeta(powner.getInventory().getHelmet().getItemMeta().clone());
					pclone.getInventory().setHelmet(is);
				}
			}
	
			if(powner.getInventory().getLeggings() != null)
			{
				is = powner.getInventory().getLeggings().clone();
				if(is != null)
				{
					is.setItemMeta(powner.getInventory().getLeggings().getItemMeta().clone());
					pclone.getInventory().setLeggings(is);
				}
			}
		}
	}
	
	public void refresh()
	{
		npc.despawn();
		npc.spawn(getNewLocation(Bukkit.getPlayer(owner).getLocation()));
		
		if (npc.getBukkitEntity() instanceof PlayerNPC) {
			((PlayerNPC) npc.getBukkitEntity()).setGravityEnabled(false);
		}
		
		((Player) npc.getBukkitEntity()).setGameMode(Bukkit.getPlayer(owner).getGameMode());
		
		copyEquipment();
	}

	public String getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
		npc.setName(name);
	}

	/*public HumanNPC getNPC() {
		return npc;
	}*/

	public Location getStartingLocation() {
		return startingLocation;
	}

	public void remove() {
		/*if (npc != null) {
			npc.remove();
		}*/
		owner = "";
		xtrans = 0;
		ytrans = 0;
		ztrans = 0;
		rotation = 0;
		mirror = Direction.NONE;
		//removePhysicalClone();
		//inpc.removeNPC(npc);
		npc.despawn();
		npc.destroy();
		npc = null;
	}

	public void move(Location playerlocation) throws IllegalArgumentException
	{
		/*if (npc != null) {
			Location newloc = getNewLocation(playerlocation);
			
			npc.teleport(newloc);
			
			//Temporary fix
			((net.minecraft.server.EntityPlayer) npc.getHandle()).aX = (float)(newloc.getYaw());
		} */
		
		//logger.info(playerlocation.getBlockX() + "," + playerlocation.getBlockY() + "," + playerlocation.getBlockZ());
		
		//logger.info(currentLocation.getBlockX() + "," + currentLocation.getBlockY() + "," + currentLocation.getBlockZ());
		//npc.teleport(currentLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
		//inpc.moveNPC(npc, currentLocation);
		
		//Location npcloc = npc.getLocation();
		//npc.despawn();
		//npc.spawn(currentLocation);
		
		currentLocation = getNewLocation(playerlocation);
		npc.getBukkitEntity().teleport(currentLocation);

		inpc.setYaw((HumanEntity) npc.getBukkitEntity(), currentLocation);
		
		//if(((Player) npc.getBukkitEntity()).isFlying())
		//{
		//	npc.getTrait(net.citizensnpcs.trait.Gravity.class).gravitate(false);
		//} else {
		//	npc.getTrait(net.citizensnpcs.trait.Gravity.class).gravitate(true);
			/*Vector v = npc.getBukkitEntity().getVelocity();
			v.setY(0);
			npc.getBukkitEntity().setVelocity(v);*/
		//}
		
		//logger.info(npc.getBukkitEntity().getLocation().getBlockX() + "," + npc.getBukkitEntity().getLocation().getBlockY() + "," + npc.getBukkitEntity().getLocation().getBlockZ());
		
		//logger.info(npcloc.getBlockX() + "," + npcloc.getBlockY() + "," + npcloc.getBlockZ());
		/*if(newlocation.getBlock().getLocation().distance(currentLocation.getBlock().getLocation()) >= 1)
		{
			removePhysicalClone();
			currentLocation = getNewLocation(playerlocation);
			placePhysicalClone();
		}*/
	}
	
	/*public void removePhysicalClone()
	{
		/*Block block = currentLocation.getBlock();
		/*for(Player player : Bukkit.getOnlinePlayers())
		{			
			player.sendBlockChange(currentLocation, block.getType(), block.getData());
			block.getState().update(true);
		}
		block.getState().update(true);
	}*/
	
	/*public void placePhysicalClone()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.getName().equalsIgnoreCase(owner))
			{
				if(!player.getLocation().getBlock().getLocation().equals(currentLocation.getBlock().getLocation()))
					player.sendBlockChange(currentLocation, Material.YELLOW_FLOWER, (byte) 0);
			}else{
				if(currentLocation.getBlock().getType() == Material.AIR)
				{
					player.sendBlockChange(currentLocation, Material.RED_ROSE, (byte) 0);
				}
			}
		}
	}*/

	public Location getNewLocation(Location oldlocation) throws IllegalArgumentException {
				
		Location location = oldlocation.clone();
		
		location.add(xtrans, ytrans, ztrans);

		double x = location.getX() - startingLocation.getX();
		double z = location.getZ() - startingLocation.getZ();
		
		if (rotation == 90) {
			location.setX(-z);
			location.setZ(x);
		} else if (rotation == 180) {
			location.setX(-x);
			location.setZ(-z);
		} else if (rotation == 270) {
			location.setX(z);
			location.setZ(-x);
		} else {
			location.setX(x);
			location.setZ(z);
		}

		if (mirror == Direction.EAST) {
			location.setX(-location.getX());
			location.setYaw(360 - (oldlocation.getYaw() + rotation));
		} else if (mirror == Direction.NORTH) {
			location.setZ(-location.getZ());
			location.setYaw(180 - (oldlocation.getYaw() + rotation));
		} else {
			location.setYaw(oldlocation.getYaw() + rotation);
		}
	
		location.add(startingLocation.getX(), 0, startingLocation.getZ());
	
		if(startingLocation.distance(location) > clonedplayer.limit)
		{
			throw new IllegalArgumentException("Above limit. Use /cloneme limit <x> to increase it.");
		}
		
		return location;
	}
	
	public Location getNewEntityLocation(Location oldlocation) {
		
		Location location = oldlocation.clone();
		location.add(xtrans, ytrans, ztrans);

		double x = location.getX() - startingLocation.getBlockX();
		double z = location.getZ() - startingLocation.getBlockZ();
		location.setY(oldlocation.getY());
		
		if (rotation == 90) {
			location.setX(-z);
			location.setZ(x);
		} else if (rotation == 180) {
			location.setX(-x);
			location.setZ(-z);
		} else if (rotation == 270) {
			location.setX(z);
			location.setZ(-x);
		} else {
			location.setX(x);
			location.setZ(z);
		}

		if (mirror == Direction.EAST) {
			location.setX(-location.getX());
		} else if (mirror == Direction.NORTH) {
			location.setZ(-location.getZ());
		}

		location.add(startingLocation.getBlockX(), 0, startingLocation.getBlockZ());
		
		return location;
	}

	public Block setNewBlock(Block oldblock, boolean settoair) 
	{
		Block newblock = getNewBlockLocation(oldblock);
		
		if(!newblock.getLocation().equals(oldblock.getLocation()))
		{
			Byte bottomdata = getNewBlockData(oldblock);
			
			Material mat = oldblock.getType();
			
			if (settoair) 
			{
				newblock.setType(Material.AIR);
			} 
			else 
			{	
				
				
				if (mat == Material.WOODEN_DOOR || mat == Material.IRON_DOOR_BLOCK) 
				{
					Byte upperdata = oldblock.getLocation().add(0, 1, 0).getBlock().getData();
					
					bottomdata = getNewDoorData(oldblock, oldblock.getLocation().add(0, 1, 0).getBlock(), false);
					
					newblock.setTypeIdAndData(mat.getId(), bottomdata, false);
					
					upperdata = getNewDoorData(oldblock, oldblock.getLocation().add(0, 1, 0).getBlock(), true);
	
					newblock.getWorld().getBlockAt(newblock.getLocation().clone().add(0, 1, 0)).setTypeIdAndData(mat.getId(), (byte) upperdata, false);
				}
				else if(oldblock.getState() instanceof Skull)
				{
					newblock.setTypeIdAndData(mat.getId(), bottomdata, true);
					newblock.setData(bottomdata);
					
					Skull oldskull = (Skull) oldblock.getState();
					Skull newskull = (Skull) newblock.getState();
									
					newskull.setRotation(getNewBlockFace(oldskull.getRotation()));
					newskull.setSkullType(oldskull.getSkullType());
					
					if(newskull.getSkullType() == SkullType.PLAYER)
						newskull.setOwner(oldskull.getOwner());
					
					newskull.update(true);
				}
				else if(mat == Material.BED_BLOCK)
				{
					byte oldblock1data = oldblock.getData();
					
					Block oldblock2 = null;
					Block newblock2 = null;
					
					switch(oldblock1data)
					{
					case 0:
					case 4:
					case 16:
						oldblock2 = oldblock.getLocation().clone().add(0, 0, 1).getBlock();
						newblock2 = getNewBlockLocation(oldblock2);
						break;
					case 1:
					case 5:
						oldblock2 = oldblock.getLocation().clone().add(-1, 0, 0).getBlock();
						newblock2 = getNewBlockLocation(oldblock2);
						break;
					case 2:
					case 6:
						oldblock2 = oldblock.getLocation().clone().add(0, 0, -1).getBlock();
						newblock2 = getNewBlockLocation(oldblock2);
						break;
					case 3:
					case 7:
						oldblock2 = oldblock.getLocation().clone().add(1, 0, 0).getBlock();
						newblock2 = getNewBlockLocation(oldblock2);
						break;
					}
					
					newblock.setTypeIdAndData(mat.getId(), getNewBlockData(oldblock), false);
					newblock2.setTypeIdAndData(mat.getId(), getNewBlockData(oldblock2), false);
				}
				else
				{
					newblock.setTypeIdAndData(mat.getId(), bottomdata, true);
					newblock.setData(bottomdata);
				}
			}
		}
		
		return newblock;
	}

	public Block getNewBlockLocation(Block oldblock) {
		Location location = oldblock.getLocation().clone();

		location.add(xtrans, ytrans, ztrans);

		double x = location.getBlockX() - startingLocation.getBlockX();
		double z = location.getBlockZ() - startingLocation.getBlockZ();

		if (rotation == 90) {
			location.setX(-z);
			location.setZ(x);
		} else if (rotation == 180) {
			location.setX(-x);
			location.setZ(-z);
		} else if (rotation == 270) {
			location.setX(z);
			location.setZ(-x);
		} else {
			location.setX(x);
			location.setZ(z);
		}

		if (mirror == Direction.EAST) {
			location.setX(-location.getBlockX());
		} else if (mirror == Direction.NORTH) {
			location.setZ(-location.getBlockZ());
		}

		location.add(startingLocation.getBlockX(), 0, startingLocation.getBlockZ());

		return location.getBlock();
	}
	
	public BlockFace getNewBlockFace(BlockFace blockface) 
	{		
		if (rotation == 90) {
			blockface = CMUtils.getNewRotatedFace(blockface, 1);
		} else if (rotation == 180) {
			blockface = CMUtils.getNewRotatedFace(blockface, 2);
		} else if (rotation == 270) {
			blockface = CMUtils.getNewRotatedFace(blockface, 3);
		}
		
		blockface = CMUtils.getNewMirrorFace(blockface, mirror);

		return blockface;
	}
	
	public byte getNewBlockData(Block oldblock) {
		byte data = oldblock.getData();
		Material mat = oldblock.getType();
		
		if (rotation == 90) 
		{
			data = CMUtils.rotateData(1, mat, data);
		} else if (rotation == 180) {
			data = CMUtils.rotateData(2, mat, data);
		} else if (rotation == 270) {
			data = CMUtils.rotateData(3, mat, data);
		}

		data = CMUtils.mirrorData(mat, data, mirror);
		
		return data;
	}
	
	public byte getNewDoorData(Block oldlowerblock, Block oldupperblock, boolean Upper) {
		byte lowerdata = oldlowerblock.getData();
		byte upperdata = oldupperblock.getData();

		if (rotation == 90) {
			if(Upper)
				upperdata = CMUtils.rotateDoorData(1, lowerdata, upperdata, Upper);
			else
				lowerdata = CMUtils.rotateDoorData(1, lowerdata, upperdata, Upper);
		} else if (rotation == 180) {
			if(Upper)
				upperdata = CMUtils.rotateDoorData(2, lowerdata, upperdata, Upper);
			else
				lowerdata = CMUtils.rotateDoorData(2, lowerdata, upperdata, Upper);
		} else if (rotation == 270) {
			if(Upper)
				upperdata = CMUtils.rotateDoorData(3, lowerdata, upperdata, Upper);
			else
				lowerdata = CMUtils.rotateDoorData(3, lowerdata, upperdata, Upper);
		}

		if(Upper)
			upperdata = CMUtils.mirrorDoorData(lowerdata, upperdata, mirror, Upper);
		else
			lowerdata = CMUtils.mirrorDoorData(lowerdata, upperdata, mirror, Upper);
		
		if (Upper)
			return upperdata;
		else
			return lowerdata;
	}


	public boolean breakBlock(Block breakblock, Player p) {
		Block block = getNewBlockLocation(breakblock);

		if (p.getGameMode() == GameMode.SURVIVAL) {
			if (block.getType() != Material.AIR) {
				p.getInventory().addItem(new ItemStack(block.getType(), 1, block.getData()));
			}
		}

		setNewBlock(breakblock, true);

		return true;
	}

	public boolean placeBlock(Block placeblock, Player p) {
		if (p.getGameMode() == GameMode.SURVIVAL) {
			boolean found = false;

			for (ItemStack is : p.getInventory().getContents()) {
				if (is != null && is.getData() != null) {
					if (is.getData().getItemType() == placeblock.getType() && is.getData().getData() == placeblock.getData()) {
						if (is.getAmount() == 1) {
							p.getInventory().remove(is);
						} else {
							is.setAmount(is.getAmount() - 1);
						}
						setNewBlock(placeblock, false);
						return true;
					}
				}
			}

			if (!found) {
				return false;
			}
		}else{
			setNewBlock(placeblock, false);
		}
		
		return true;
	}

	public void setItemInHand(ItemStack is) {
		
		HumanEntity entity = (HumanEntity) npc.getBukkitEntity();
		
		if(is == null)
		{
			entity.setItemInHand(null);
		}
		else if(is.getAmount() == 0)
		{
			entity.setItemInHand(null);
		}
		else
		{
			entity.setItemInHand(is.clone());
		}
		/*if (npc != null) {
			if(is == null)
			{
				((CraftHumanEntity) npc).setItemInHand(null);
			}else{
				if (is.getAmount() == 0) {
					//npc.setItemInHand(null);
					((CraftHumanEntity) npc).setItemInHand(null);
				} else {
					//npc.setItemInHand(is.clone());
					((HumanNPC) npc).setItemInHand(is.clone());
				}
			}
		}*/
	}

	public void setSneaking(boolean sneak) {
		/*if (npc != null) {
			npc.setSneaking(sneak);
		}*/
		inpc.setSneak((HumanEntity) npc.getBukkitEntity(), sneak);
	}
	
	public void setFlying(boolean flying)
	{
		Player p = ((Player) npc.getBukkitEntity());
		p.setFlying(flying);
	}

	public void doArmSwing() 
	{
		/*if (npc != null) {
			npc.animateArmSwing();
		}*/
		inpc.armSwing((HumanEntity) npc.getBukkitEntity());
	}

	/*public Packet20NamedEntitySpawn makeNamedEntitySpawnPacket() {
		return makeNamedEntitySpawnPacket(name);
	}*/

	// Copied from ModDisguise by desmin88
	// See:
	// https://github.com/desmin88/MobDisguise/blob/master/src/me/desmin88/mobdisguise/utils/PacketUtils.java
	/*public Packet20NamedEntitySpawn makeNamedEntitySpawnPacket(String Name) {
		Location loc = npc.getLocation();
		Packet20NamedEntitySpawn packet = new Packet20NamedEntitySpawn();
		packet.a = npc.getEntityId();
		packet.b = name; // Set the name of the player to the name they want.
		packet.c = (int) loc.getX();
		packet.c = MathHelper.floor(loc.getX() * 32.0D);
		packet.d = MathHelper.floor(loc.getY() * 32.0D);
		packet.e = MathHelper.floor(loc.getZ() * 32.0D);
		packet.f = (byte) ((int) loc.getYaw() * 256.0F / 360.0F);
		packet.g = (byte) ((int) (loc.getPitch() * 256.0F / 360.0F));
		//NPCCREATURE CRASHES >.>
		packet.h = npc.getItemInHand().getTypeId();
		return packet;
	}*/
}
