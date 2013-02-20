package com.worldcretornica.cloneme.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ClonePlayerInteractEntityEvent extends PlayerInteractEntityEvent {

	public ClonePlayerInteractEntityEvent(Player who, Entity clickedEntity) {
		super(who, clickedEntity);
	}

}
