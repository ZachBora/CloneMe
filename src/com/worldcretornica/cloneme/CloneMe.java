package com.worldcretornica.cloneme;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import tk.npccreatures.NPCCreatures;
import tk.npccreatures.npcs.NPCManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.worldcretornica.cloneme.commands.CMCommand;
import com.worldcretornica.cloneme.listeners.CloneBlockListener;
import com.worldcretornica.cloneme.listeners.ClonePlayerListener;

public class CloneMe extends JavaPlugin {

    // FINAL
    public static final String NAME = "CloneMe";
    public static final String PREFIX = "[CloneMe]";
    public static final double VERSION = 0.1;
    public static final int SUBVERSION = 1;

    private final ClonePlayerListener cloneplayerlistener = new ClonePlayerListener(
	    this);
    private final CloneBlockListener cloneblocklistener = new CloneBlockListener(
	    this);

    public final Logger logger = Logger.getLogger("Minecraft"); // TODO

    // Permissions
    private PermissionHandler permissions;
    private PermissionManager permpex;

    // NPC!
    private NPCManager npcManager;

    private CloneManager cloneManager;

    public boolean usingNPC;

    @Override
    public void onDisable() {
	cloneManager.removeAllClones();

	this.logger.info(PREFIX + " disabled.");
    }

    @Override
    public void onEnable() {

	cloneManager = new CloneManager(this);

	PluginManager pm = getServer().getPluginManager();
	pm.registerEvent(Event.Type.PLAYER_QUIT, this.cloneplayerlistener,
		Event.Priority.Normal, this);
	pm.registerEvent(Event.Type.PLAYER_JOIN, this.cloneplayerlistener,
		Event.Priority.Normal, this);
	pm.registerEvent(Event.Type.PLAYER_INTERACT, this.cloneplayerlistener,
		Event.Priority.Low, this);
	pm.registerEvent(Event.Type.BLOCK_DAMAGE, this.cloneblocklistener,
		Event.Priority.Normal, this);
	pm.registerEvent(Event.Type.BLOCK_PLACE, this.cloneblocklistener,
		Event.Priority.Low, this);
	pm.registerEvent(Event.Type.BLOCK_BREAK, this.cloneblocklistener,
		Event.Priority.Low, this);

	setupPermissions();

	if (getServer().getPluginManager().isPluginEnabled("NPCCreatures")) {
	    pm.registerEvent(Event.Type.PLAYER_MOVE, this.cloneplayerlistener,
		    Event.Priority.Normal, this);
	    pm.registerEvent(Event.Type.PLAYER_TOGGLE_SNEAK,
		    this.cloneplayerlistener, Event.Priority.Normal, this);
	    pm.registerEvent(Event.Type.PLAYER_ITEM_HELD,
		    this.cloneplayerlistener, Event.Priority.Normal, this);
	    pm.registerEvent(Event.Type.PLAYER_ANIMATION,
		    this.cloneplayerlistener, Event.Priority.Normal, this);

	    Plugin npccreature = getServer().getPluginManager().getPlugin(
		    "NPCCreatures");

	    npcManager = ((NPCCreatures) npccreature).getNPCManager();

	    this.logger.info(PREFIX + " NPCCreatures v"
		    + npccreature.getDescription().getVersion() + " found!");

	    npccreature = null;
	} else {
	    npcManager = null;
	    this.logger
		    .info(PREFIX
			    + "Could not find NPCCreatures, clones will not be visible!");
	}

	getCommand("cloneme").setExecutor(new CMCommand(this));

	this.logger.info(PREFIX + " version " + VERSION + "_" + SUBVERSION
		+ " is enabled!");
    }

    public NPCManager getNPCManager() {
	return npcManager;
    }

    public CloneManager getCloneManager() {
	return cloneManager;
    }

    public void schedule(Runnable runnable, long delay) {
	getServer().getScheduler().scheduleSyncDelayedTask(this, runnable,
		delay);
    }

    /*
     * public direction getPlayerFaceDirection(Player p) { Location playerloc =
     * p.getLocation();
     * 
     * //Calculate the player direction double rot = (playerloc.getYaw() - 90) %
     * 360; if (rot < 0) rot += 360.0;
     * 
     * //Find direction /*if (0 <= rot && rot < 22.5 || 337.5 <= rot && rot <
     * 360){ return direction.north; }else if (22.5 <= rot && rot < 67.5){
     * return direction.northeast; }else if (67.5 <= rot && rot < 112.5) {
     * return direction.east; } else if (112.5 <= rot && rot < 157.5) { return
     * direction.southeast; } else if (157.5 <= rot && rot < 202.5) { return
     * direction.south; } else if (202.5 <= rot && rot < 247.5) { return
     * direction.southwest; } else if (247.5 <= rot && rot < 292.5) { return
     * direction.west; } else if (292.5 <= rot && rot < 337.5) { return
     * direction.northwest; }
     * 
     * if (0 <= rot && rot < 45 || 315 <= rot && rot < 360){ return
     * direction.north; }else if (45 <= rot && rot < 135) { return
     * direction.east; } else if (135 <= rot && rot < 225) { return
     * direction.south; } else if (225 <= rot && rot < 315) { return
     * direction.west; }
     * 
     * return direction.none; }
     */

    private void setupPermissions() {
	if (permissions != null)
	    return;

	Plugin permTest = this.getServer().getPluginManager()
		.getPlugin("Permissions");
	Plugin pexTest = this.getServer().getPluginManager()
		.getPlugin("PermissionsEx");

	// Check to see if Permissions exists
	if (pexTest != null) {
	    // We're using Permissions
	    permpex = PermissionsEx.getPermissionManager();
	    // Check for Permissions 3
	    logger.info(PREFIX + " PermissionsEx "
		    + pexTest.getDescription().getVersion() + " found");
	    return;
	} else if (permTest == null) {
	    logger.info(PREFIX + " Permissions not found, using SuperPerms");
	    return;
	}
	// Check if it's a bridge
	if (permTest.getDescription().getVersion().startsWith("2.7.7")) {
	    logger.info(PREFIX + " Found Permissions Bridge. Using SuperPerms");
	    return;
	}

	// We're using Permissions
	permissions = ((Permissions) permTest).getHandler();
	// Check for Permissions 3
	logger.info(PREFIX + " Permissions "
		+ permTest.getDescription().getVersion() + " found");
    }

    public boolean checkPermissions(Player player, String node) {
	// Permissions
	if (this.permissions != null) {
	    if (this.permissions.has(player, node))
		return true;
	    // Pex
	} else if (this.permpex != null) {
	    if (this.permpex.has(player, node))
		return true;
	    // SuperPerms
	} else if (player.hasPermission(node)
		|| player.hasPermission(NAME + ".*")
		|| player.hasPermission("*")) {
	    return true;
	} else if (player.isOp()) {
	    return true;
	}
	return false;
    }
}
