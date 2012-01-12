package com.worldcretornica.cloneme.listeners;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.worldcretornica.cloneme.Clone;
import com.worldcretornica.cloneme.CloneMe;
import com.worldcretornica.cloneme.ScheduledBlockChange;
import com.worldcretornica.cloneme.ScheduledBlockChange.ChangeType;
import com.worldcretornica.cloneme.events.CloneBlockBreakEvent;
import com.worldcretornica.cloneme.events.CloneBlockPlaceEvent;

public class CloneBlockListener extends BlockListener {

	private CloneMe plugin;

	public CloneBlockListener(CloneMe plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		if (!(event instanceof CloneBlockBreakEvent)) {
			Player p = event.getPlayer();

			Set<Clone> clones = plugin.getCloneManager().getClones(p);
			if (clones != null && clones.size() != 0) {
				for (Clone clone : clones) {
					plugin.schedule(new ScheduledBlockChange(clone, plugin, p, event.getBlock(), ChangeType.BLOCK_BREAK, event), 1);
				}
			}
		}
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled() || !event.canBuild())
			return;

		if (!(event instanceof CloneBlockPlaceEvent)) {
			Player p = event.getPlayer();

			Set<Clone> clones = plugin.getCloneManager().getClones(p);
			if (clones != null && clones.size() != 0) {
				for (Clone clone : clones) {
					plugin.schedule(new ScheduledBlockChange(clone, plugin, p, event.getBlock(), ChangeType.BLOCK_PLACE, event), 1);
				}
			}
		}
	}
}
