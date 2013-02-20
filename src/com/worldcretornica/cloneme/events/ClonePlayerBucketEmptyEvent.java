package com.worldcretornica.cloneme.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

public class ClonePlayerBucketEmptyEvent extends PlayerBucketEmptyEvent {

	public ClonePlayerBucketEmptyEvent(Player who, Block blockClicked, BlockFace blockFace, Material bucket, ItemStack itemInHand) {
		super(who, blockClicked, blockFace, bucket, itemInHand);
	}

}
