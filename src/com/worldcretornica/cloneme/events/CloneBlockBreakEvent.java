package com.worldcretornica.cloneme.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class CloneBlockBreakEvent extends BlockBreakEvent {

	public CloneBlockBreakEvent(Block theBlock, Player player) {
		super(theBlock, player);
	}

}
