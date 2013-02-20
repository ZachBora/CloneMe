package com.worldcretornica.cloneme.events;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class CloneBlockPlaceEvent extends BlockPlaceEvent {

	public CloneBlockPlaceEvent(Block placedBlock, BlockState replacedBlockState, Block placedAgainst, ItemStack itemInHand, Player thePlayer, boolean canBuild) {
		super(placedBlock, replacedBlockState, placedAgainst, itemInHand, thePlayer, canBuild);
	}

}
