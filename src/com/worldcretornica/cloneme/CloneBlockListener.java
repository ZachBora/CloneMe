package com.worldcretornica.cloneme;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.worldcretornica.cloneme.ScheduledBlockChange.changetype;
import com.worldcretornica.cloneme.events.CloneBlockBreakEvent;
import com.worldcretornica.cloneme.events.CloneBlockPlaceEvent;

public class CloneBlockListener extends BlockListener {

    public static CloneMe plugin;

    public CloneBlockListener(CloneMe instance) {
	plugin = instance;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
	if (event.isCancelled())
	    return;

	if (!(event instanceof CloneBlockBreakEvent)) {
	    Player p = event.getPlayer();

	    Set<Clone> clones = plugin.getClones(p.getName());
	    if (clones != null && clones.size() != 0) {
		for (Clone clone : clones) {
		    plugin.schedule(new ScheduledBlockChange(clone, plugin, p,
			    event.getBlock(), changetype.blockbreak, event), 1);
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

	    Set<Clone> clones = plugin.getClones(p.getName());
	    if (clones != null && clones.size() != 0) {
		for (Clone clone : clones) {
		    plugin.schedule(new ScheduledBlockChange(clone, plugin, p,
			    event.getBlock(), changetype.blockplace, event), 1);
		}
	    }
	}
    }
}
