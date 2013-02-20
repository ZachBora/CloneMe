package com.worldcretornica.cloneme;

public class ScheduledCloneRemove implements Runnable 
{

	String name = "";
	CloneMe plugin = null;
	
	public ScheduledCloneRemove(CloneMe instance, String name)
	{
		this.name = name;
		this.plugin = instance;
	}
	
	@Override
	public void run() 
	{
		plugin.getCloneManager().removeClones(name);
	}

}
