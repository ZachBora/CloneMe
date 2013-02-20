package com.worldcretornica.cloneme.compat;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;

public interface IBlockCompat 
{
	public boolean interact(Block block, Player player);
	
	public Hanging placehanging(Hanging entity, World world, int x, int y, int z, BlockFace blockface);
}
