package com.worldcretornica.cloneme.events;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClonePlayerInteractEvent extends PlayerInteractEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3868388333255631567L;

	public ClonePlayerInteractEvent(Player who, Action action, ItemStack item, Block clickedBlock, BlockFace clickedFace) {
		super(who, action, item, clickedBlock, clickedFace);
	}
}
