package com.worldcretornica.cloneme.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class CloneHangingBreakByEntityEvent extends HangingBreakByEntityEvent {

	public CloneHangingBreakByEntityEvent(Hanging hanging, Entity remover) {
		super(hanging, remover);
	}

}
