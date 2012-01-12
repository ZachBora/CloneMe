package com.worldcretornica.cloneme.listeners;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import com.worldcretornica.cloneme.Clone;
import com.worldcretornica.cloneme.CloneMe;
import com.worldcretornica.cloneme.ScheduledBlockChange;
import com.worldcretornica.cloneme.ScheduledBlockChange.ChangeType;
import com.worldcretornica.cloneme.events.ClonePlayerInteractEvent;

public class ClonePlayerListener extends PlayerListener {

    private CloneMe plugin;

    public ClonePlayerListener(CloneMe plugin) {
	this.plugin = plugin;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
	if (event.isCancelled())
	    return;

	if (!(event instanceof ClonePlayerInteractEvent)) {
	    Player p = event.getPlayer();

	    Set<Clone> clones = plugin.getCloneManager().getClones(p);
	    if (clones != null && clones.size() != 0) {
		for (Clone clone : clones) {
		    plugin.schedule(
			    new ScheduledBlockChange(clone, plugin, p, event
				    .getClickedBlock(), ChangeType.INTERACT,
				    event), 1);
		}
	    }
	}
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
	if (event.isCancelled())
	    return;

	Location loc = event.getTo();
	Player p = event.getPlayer();

	Set<Clone> clones = plugin.getCloneManager().getClones(p);
	if (clones != null && loc != null && clones.size() != 0) {
	    for (Clone clone : clones) {
		clone.move(loc);
	    }
	}
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

    }

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
	if (event.isCancelled())
	    return;

	Player p = event.getPlayer();
	boolean sneaking = event.isSneaking();

	Set<Clone> clones = plugin.getCloneManager().getClones(p);
	if (clones != null && clones.size() != 0) {
	    for (Clone clone : clones) {
		clone.setSneaking(sneaking);
	    }
	}
    }

    @Override
    public void onPlayerAnimation(PlayerAnimationEvent event) {
	if (event.isCancelled())
	    return;

	if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
	    Player p = event.getPlayer();

	    Set<Clone> clones = plugin.getCloneManager().getClones(p);
	    if (clones != null && clones.size() != 0) {
		for (Clone clone : clones) {
		    clone.doArmSwing();
		}
	    }
	}
    }

    @Override
    public void onItemHeldChange(PlayerItemHeldEvent event) {

	Player p = event.getPlayer();
	ItemStack is = p.getInventory().getItem(event.getNewSlot());

	Set<Clone> clones = plugin.getCloneManager().getClones(p);
	if (clones != null && clones.size() != 0) {
	    for (Clone clone : clones) {
		clone.setItemInHand(is);
	    }
	}
    }

}
