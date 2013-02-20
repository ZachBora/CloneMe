package com.worldcretornica.cloneme.compat.v1_4_5;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

import com.worldcretornica.cloneme.compat.IBlockCompat;

public class BlockCompat implements IBlockCompat
{
	public boolean interact(Block block, Player player)
	{
		if (block != null && block.getType() != Material.AIR) 
		{
			net.minecraft.server.v1_4_5.World myworld = ((org.bukkit.craftbukkit.v1_4_5.CraftWorld) block.getWorld()).getHandle();
			net.minecraft.server.v1_4_5.EntityHuman human = ((org.bukkit.craftbukkit.v1_4_5.entity.CraftPlayer) player).getHandle();

			if (myworld != null && human != null) 
			{
				return net.minecraft.server.v1_4_5.Block.byId[block.getTypeId()].interact(myworld, block.getX(), block.getY(), block.getZ(), human, 0, 0, 0, 0);
			}
		}
		
		return false;
	}
	
	public Hanging placehanging(Hanging entity, World world, int x, int y, int z, BlockFace blockface) 
	{
		int dir;
		switch (blockface) 
        {
	        case SOUTH:
	        default: dir = 0; break;
	        case WEST: dir = 1; break;
	        case NORTH: dir = 2; break;
	        case EAST: dir = 3; break;
        }
		
        net.minecraft.server.v1_4_5.WorldServer w = ((org.bukkit.craftbukkit.v1_4_5.CraftWorld) world).getHandle();
        net.minecraft.server.v1_4_5.Entity newentity = null;
        
        if (org.bukkit.entity.Painting.class.isAssignableFrom(entity.getClass())) 
        {
        	newentity = new net.minecraft.server.v1_4_5.EntityPainting(w, (int) x, (int) y, (int) z, dir);
        	Painting newpaint = (Painting) newentity.getBukkitEntity();
        	newpaint.setArt(((Painting) entity).getArt());
        } 
        else if (org.bukkit.entity.ItemFrame.class.isAssignableFrom(entity.getClass())) 
        {
        	newentity = new net.minecraft.server.v1_4_5.EntityItemFrame(w, (int) x, (int) y, (int) z, dir);
        }

        if (newentity != null && !((net.minecraft.server.v1_4_5.EntityHanging) newentity).survives()) 
        {
        	newentity = null;
        }
        
		w.addEntity(newentity);
		
		return (Hanging) newentity.getBukkitEntity();
	}
}
