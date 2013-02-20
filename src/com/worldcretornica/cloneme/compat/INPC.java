package com.worldcretornica.cloneme.compat;

import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;

public interface INPC 
{
	public void setYaw(HumanEntity entity, Location loc);
	
	public void armSwing(HumanEntity entity);
	
	public void setSneak(HumanEntity entity, boolean sneak);
}
