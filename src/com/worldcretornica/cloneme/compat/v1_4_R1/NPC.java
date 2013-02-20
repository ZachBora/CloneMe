package com.worldcretornica.cloneme.compat.v1_4_R1;

import net.minecraft.server.v1_4_R1.*;
import org.bukkit.craftbukkit.v1_4_R1.entity.*;

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
	
	/*@Override
	public HumanEntity createNPC(String name, Location location) 
	{
		WorldServer ws = ((CraftWorld) location.getWorld()).getHandle();
		
		EntityPlayer entity = new EntityPlayer(ws.getMinecraftServer(), ws, name, new PlayerInteractManager(ws));
		ws.addEntity(entity);
		CraftPlayer cp = entity.getBukkitEntity();
		//cp.teleport(location);
		cp.setSleepingIgnored(true);
		
		return (HumanEntity) cp;
	}

	@Override
	public void renameNPC(String Name, HumanEntity humanentity) 
	{
		CraftPlayer cp = (CraftPlayer) humanentity;
		EntityPlayer entity = cp.getHandle();
		entity.name = Name;
	}

	@Override
	public void removeNPC(HumanEntity entity) 
	{
		entity.remove();
	}
	
	@Override
	public void moveNPC(HumanEntity entity, Location location)
	{
		CraftPlayer cp = (CraftPlayer) entity;
		
		cp.teleport(location);
		
		//EntityPlayer ep = cp.getHandle();
		//ep.teleportTo(location, true);
	}*/
}
