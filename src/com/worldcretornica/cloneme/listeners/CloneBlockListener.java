package com.worldcretornica.cloneme.listeners;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

import com.worldcretornica.cloneme.Clone;
import com.worldcretornica.cloneme.CloneMe;
import com.worldcretornica.cloneme.ScheduledBlockChange;
import com.worldcretornica.cloneme.events.CloneBlockBreakEvent;
import com.worldcretornica.cloneme.events.CloneBlockPlaceEvent;
import com.worldcretornica.cloneme.events.CloneHangingBreakByEntityEvent;
import com.worldcretornica.cloneme.events.CloneHangingPlaceEvent;

public class CloneBlockListener implements Listener {

	private CloneMe plugin;
	
	public CloneBlockListener(CloneMe plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event) 
	{
		if (!(event instanceof CloneBlockBreakEvent)) {
			Player p = event.getPlayer();

			List<Clone> clones = plugin.getCloneManager().getClones(p);
			if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
				
				for (Clone clone : clones) 
				{
					plugin.schedule(new ScheduledBlockChange(clone, plugin, p, event.getBlock(), ScheduledBlockChange.ChangeType.BLOCK_BREAK, event), 1);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event) {
		if (!event.canBuild())
			return;

		if (!(event instanceof CloneBlockPlaceEvent)) {
			Player p = event.getPlayer();

			List<Clone> clones = plugin.getCloneManager().getClones(p);
			if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
				for (Clone clone : clones) 
				{
					plugin.schedule(new ScheduledBlockChange(clone, plugin, p, event.getBlock(), ScheduledBlockChange.ChangeType.BLOCK_PLACE, event), 1);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onHangingPlace(final HangingPlaceEvent event)
	{	
		if (!(event instanceof CloneHangingPlaceEvent)) {
			Player p = event.getPlayer();

			List<Clone> clones = plugin.getCloneManager().getClones(p);
			if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
				for (Clone clone : clones) {
					plugin.schedule(new ScheduledBlockChange(clone, plugin, p, event.getBlock(), ScheduledBlockChange.ChangeType.HANGING_PLACE, event), 1);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onHangingBreak(final HangingBreakByEntityEvent event)
	{
		if (!(event instanceof CloneHangingBreakByEntityEvent)) {
			Entity remover = event.getRemover();

			if(remover instanceof Player)
			{
				Player p = (Player) remover;
				
				List<Clone> clones = plugin.getCloneManager().getClones(p);
				if (clones != null && !plugin.getCloneManager().IsPaused(p.getName()) && clones.size() != 0) {
					for (Clone clone : clones) {
						plugin.schedule(new ScheduledBlockChange(clone, plugin, p, ScheduledBlockChange.ChangeType.HANGING_BREAK, event), 1);
					}
				}
			}
		}
	}
	
}
