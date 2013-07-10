package com.worldcretornica.cloneme.compat.v1_6_R1;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.ItemFrame;

import com.worldcretornica.cloneme.CloneMe;
import com.worldcretornica.cloneme.compat.IBlockCompat;

import fr.neatmonster.nocheatplus.checks.CheckType;

import net.minecraft.server.v1_6_R1.*;
import org.bukkit.craftbukkit.v1_6_R1.*;
import org.bukkit.craftbukkit.v1_6_R1.entity.*;

public class BlockCompat implements IBlockCompat
{
	public boolean interact(org.bukkit.block.Block block, Player player)
	{		
		if (block != null && block.getType() != Material.AIR) 
		{					
			World myworld = ((CraftWorld) block.getWorld()).getHandle();
			EntityHuman human = ((CraftPlayer) player).getHandle();

			if (myworld != null && human != null) 
			{
				boolean result;
				boolean alreadyexempt = false;
				
				if(CloneMe.usingNCP)
				{
					if(CloneMe.isncpExempt(player, CheckType.ALL))
						alreadyexempt = true;
					else
						CloneMe.ncpExempt(player, CheckType.ALL);
				}
				
				result = Block.byId[block.getTypeId()].interact(myworld, block.getX(), block.getY(), block.getZ(), human, 0, 0, 0, 0);
				
				if(CloneMe.usingNCP)
				{
					if(!alreadyexempt)
						{}//CloneMe.ncpUnexempt(player, CheckType.ALL);
					else
						alreadyexempt = false;
						
				}
				
				return result;
			}
		}
		
		return false;
	}
	
	public Hanging placehanging(Hanging entity, org.bukkit.World world, int x, int y, int z, BlockFace blockface) 
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
		
        WorldServer w = ((CraftWorld) world).getHandle();
        Entity newentity = null;
        
        if (Painting.class.isAssignableFrom(entity.getClass())) 
        {
        	newentity = new EntityPainting(w, (int) x, (int) y, (int) z, dir);
        	Painting newpaint = (Painting) newentity.getBukkitEntity();
        	newpaint.setArt(((Painting) entity).getArt());
        } 
        else if (ItemFrame.class.isAssignableFrom(entity.getClass())) 
        {
        	newentity = new EntityItemFrame(w, (int) x, (int) y, (int) z, dir);
        }

        if (newentity != null && !((EntityHanging) newentity).survives()) 
        {
        	newentity = null;
        }
        
		w.addEntity(newentity);
		
		return (Hanging) newentity.getBukkitEntity();
	}

	public void setYaw(LivingEntity entity, Location loc) 
	{
		EntityLiving el = ((CraftLivingEntity) entity).getHandle();
		
		//el.yaw = (float)(loc.getYaw());
		el.yaw = (float)(loc.getYaw());
		el.lastYaw = el.yaw;
		el.pitch = (float)(loc.getPitch());
		el.lastPitch = el.pitch;
	}
}
