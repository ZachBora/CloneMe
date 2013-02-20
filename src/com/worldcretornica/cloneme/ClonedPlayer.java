package com.worldcretornica.cloneme;

import java.util.ArrayList;
import java.util.List;

public class ClonedPlayer {

	public List<Clone> clones;
	public boolean paused;
	public int limit = 100;
	
	public ClonedPlayer()
	{
		clones = new ArrayList<Clone>();
		paused = false;
		limit = 100;
	}
	
}
