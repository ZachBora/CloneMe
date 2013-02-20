package com.worldcretornica.cloneme.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.worldcretornica.cloneme.Clone;
import com.worldcretornica.cloneme.CloneMe;
import com.worldcretornica.cloneme.ScheduledBlockChange;
import com.worldcretornica.cloneme.ScheduledCloneRemove;
import com.worldcretornica.cloneme.events.ClonePlayerBucketEmptyEvent;
import com.worldcretornica.cloneme.events.ClonePlayerBucketFillEvent;
import com.worldcretornica.cloneme.events.ClonePlayerInteractEntityEvent;

public class ClonePlayerListener implements Listener 
{
	private CloneMe plugin;

	public ClonePlayerListener(CloneMe plugin) 
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent event) 
	{		
		Player p = event.getPlayer();

		List<Clone> clones = plugin.getCloneManager().getClones(p);
		if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
			for (Clone clone : clones) {
				plugin.schedule(new ScheduledBlockChange(clone, plugin, p, event.getClickedBlock(), ScheduledBlockChange.ChangeType.INTERACT, event), 1);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event)
	{
		if (!(event instanceof ClonePlayerBucketEmptyEvent)) {
			Player p = event.getPlayer();
	
			List<Clone> clones = plugin.getCloneManager().getClones(p);
			if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
				for (Clone clone : clones) {
					plugin.schedule(new ScheduledBlockChange(clone, plugin, p, event.getBlockClicked(), ScheduledBlockChange.ChangeType.BUCKET_EMPTY, event), 1);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerBucketFill(final PlayerBucketFillEvent event)
	{
		if (!(event instanceof ClonePlayerBucketFillEvent)) {
			Player p = event.getPlayer();
	
			List<Clone> clones = plugin.getCloneManager().getClones(p);
			if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
				for (Clone clone : clones) {
					plugin.schedule(new ScheduledBlockChange(clone, plugin, p, event.getBlockClicked(), ScheduledBlockChange.ChangeType.BUCKET_FILL, event), 1);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
	{
		if (!(event instanceof ClonePlayerInteractEntityEvent)) {
			Player p = event.getPlayer();
	
			List<Clone> clones = plugin.getCloneManager().getClones(p);
			if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
				for (Clone clone : clones) {
					plugin.schedule(new ScheduledBlockChange(clone, plugin, p, ScheduledBlockChange.ChangeType.INTERACT_ENTITY, event), 1);
				}
			}
		}
	}
	

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(final PlayerMoveEvent event) 
	{			
		Location loc = event.getTo().clone();
		Player p = event.getPlayer();

		List<Clone> clones = plugin.getCloneManager().getClones(p);
				
		if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && loc != null && clones.size() != 0) {
			for (Clone clone : clones) 
			{
				try
				{
					clone.move(loc);
					clone.setFlying(p.isFlying());
				}
				catch(IllegalArgumentException e)
				{
					p.sendMessage(e.getMessage());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerToggleSneak(final PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();
		boolean sneaking = event.isSneaking();

		List<Clone> clones = plugin.getCloneManager().getClones(p);
		if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
			for (Clone clone : clones) {
				clone.setSneaking(sneaking);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerAnimation(final PlayerAnimationEvent event) {

		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
			Player p = event.getPlayer();

			List<Clone> clones = plugin.getCloneManager().getClones(p);
			if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
				for (Clone clone : clones) {
					clone.doArmSwing();
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemHeldChange(final PlayerItemHeldEvent event) {

		Player p = event.getPlayer();
		ItemStack is = p.getInventory().getItem(event.getNewSlot());

		List<Clone> clones = plugin.getCloneManager().getClones(p);
		if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
			for (Clone clone : clones) {
				clone.setItemInHand(is);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerToggleFlight(final PlayerToggleFlightEvent event) {

		Player p = event.getPlayer();
		boolean isflying = event.isFlying();

		List<Clone> clones = plugin.getCloneManager().getClones(p);
		if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
			for (Clone clone : clones) {
				clone.setFlying(isflying);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerPlayerQuit(final PlayerQuitEvent event)
	{
		String name = event.getPlayer().getName();
		
		if(plugin.getCloneManager().hasClones(name))
		{
			BukkitTask bt = Bukkit.getScheduler().runTaskLater(plugin, new ScheduledCloneRemove(plugin, name), 20 * 60 * 15);
			plugin.toBeRemoved.put(name, bt.getTaskId());
			plugin.logger.info(CloneMe.PREFIX + " " + name + "'s clones will be auto removed in 15 minutes");
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		String name = event.getPlayer().getName();
		
		if(plugin.toBeRemoved.containsKey(name))
		{
			int taskid = plugin.toBeRemoved.get(name);
			Bukkit.getScheduler().cancelTask(taskid);
			plugin.logger.info(CloneMe.PREFIX + " " + name + "'s will no longer be auto removed.");
		}
	}
}
