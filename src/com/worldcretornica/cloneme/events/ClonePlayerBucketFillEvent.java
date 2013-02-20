package com.worldcretornica.cloneme.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

public class ClonePlayerBucketFillEvent extends PlayerBucketFillEvent {

	public ClonePlayerBucketFillEvent(Player who, Block blockClicked, BlockFace blockFace, Material bucket, ItemStack itemInHand) {
		super(who, blockClicked, blockFace, bucket, itemInHand);
	}

}
