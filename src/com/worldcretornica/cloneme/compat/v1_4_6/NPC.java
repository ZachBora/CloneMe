package com.worldcretornica.cloneme.compat.v1_4_6;

import net.minecraft.server.v1_4_6.*;
import org.bukkit.craftbukkit.v1_4_6.entity.*;

import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;

import com.worldcretornica.cloneme.compat.INPC;

public class NPC implements INPC 
{
	@Override
	public void setYaw(HumanEntity entity, Location loc) 
	{
		((CraftHumanEntity) entity).getHandle().az = loc.getYaw();
	}

	@Override
	public void armSwing(HumanEntity entity) 
	{
		EntityHuman eh = ((CraftHumanEntity) entity).getHandle();
		((WorldServer) eh.world).tracker.a(eh, new Packet18ArmAnimation(eh, 1));
	}

	@Override
	public void setSneak(HumanEntity entity, boolean sneak) 
	{
		((CraftHumanEntity) entity).getHandle().setSneaking(sneak);
	}
}
