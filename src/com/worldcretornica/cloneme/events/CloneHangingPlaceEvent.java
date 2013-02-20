package com.worldcretornica.cloneme.events;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingPlaceEvent;

public class CloneHangingPlaceEvent extends HangingPlaceEvent {

	public CloneHangingPlaceEvent(Hanging hanging, Player player, Block block, BlockFace blockFace) {
		super(hanging, player, block, blockFace);
	}

}
