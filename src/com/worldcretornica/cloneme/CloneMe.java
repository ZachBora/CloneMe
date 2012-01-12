package com.worldcretornica.cloneme;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet29DestroyEntity;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import tk.npccreatures.NPCCreatures;
import tk.npccreatures.npcs.NPCManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.worldcretornica.cloneme.Clone.Direction;

public class CloneMe extends JavaPlugin {

    public final ClonePlayerListener cloneplayerlistener = new ClonePlayerListener(
	    this);
    public final CloneBlockListener cloneblocklistener = new CloneBlockListener(
	    this);

    public HashMap<String, Set<Clone>> clonelist;

    public String pdfdescription;
    private String pdfversion;

    public final Logger logger = Logger.getLogger("Minecraft");

    // Permissions
    public PermissionHandler permissions;
    public PermissionManager permpex;
    boolean permissions3;

    // NPC!
    public NPCManager npcManager;

    public boolean usingNPC;

    @Override
    public void onDisable() {

	for (Set<Clone> clones : clonelist.values()) {
	    for (Clone clone : clones) {
		if (npcManager != null) {
		    Packet29DestroyEntity p29 = new Packet29DestroyEntity(
			    clone.getNPC().getEntityId());

		    for (Player p : this.getServer().getOnlinePlayers()) {
			((CraftPlayer) p).getHandle().netServerHandler
				.sendPacket(p29);
		    }
		}

		clone.remove();
		clone = null;
	    }
	}

	clonelist.clear();

	this.logger.info(pdfdescription + " disabled.");
    }

    @Override
    public void onEnable() {
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

	PluginDescriptionFile pdfFile = this.getDescription();
	pdfdescription = pdfFile.getName();
	pdfversion = pdfFile.getVersion();

	setupPermissions();

	clonelist = new HashMap<String, Set<Clone>>();

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

	    this.logger.info("[" + pdfdescription + "] NPCCreatures v"
		    + npccreature.getDescription().getVersion() + " found!");

	    npccreature = null;
	} else {
	    npcManager = null;
	    this.logger
		    .info("["
			    + pdfdescription
			    + "] Could not find NPCCreatures, clones will not be visible!");
	}

	this.logger.info("[" + pdfdescription + "] version " + pdfversion
		+ " is enabled!");
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
	if (l.equalsIgnoreCase("cloneme")) {
	    if (!(s instanceof Player)
		    || this.checkPermissions((Player) s, "CloneMe.use")) {
		if (a.length == 0) {
		    s.sendMessage(ChatColor.BLUE + pdfdescription + " v"
			    + pdfversion + ChatColor.WHITE
			    + " [] means optional, <> means obligated");
		    if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme add "
				+ ChatColor.WHITE + "Adds a new clone.");
		    if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme modify "
				+ ChatColor.GREEN + "<id> " + ChatColor.WHITE
				+ "Modify a clone. Use /list to get ids.");
		    s.sendMessage(ChatColor.RED + "/cloneme remove "
			    + ChatColor.GREEN + "<id> " + ChatColor.WHITE
			    + "Removes a clone. Use /list to get ids.");
		    if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme ready "
				+ ChatColor.WHITE + "Starts your clones.");
		    if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme pause "
				+ ChatColor.WHITE + "Pause your clones.");
		    s.sendMessage(ChatColor.RED + "/cloneme list "
			    + ChatColor.GREEN + "[name] " + ChatColor.WHITE
			    + "Lists clones.");
		    s.sendMessage(ChatColor.RED + "/cloneme users "
			    + ChatColor.WHITE + "Lists clone users.");
		    if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme save "
				+ ChatColor.GREEN + "<name> " + ChatColor.WHITE
				+ "Save your clones in a template.");
		    if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme load "
				+ ChatColor.GREEN + "<name> " + ChatColor.WHITE
				+ "Load clones from a template.");
		    s.sendMessage(ChatColor.RED + "/cloneme stop "
			    + ChatColor.GREEN + "[name] " + ChatColor.WHITE
			    + "Removes the clones.");
		    s.sendMessage(ChatColor.RED + "/cloneme stopall "
			    + ChatColor.WHITE + "Stop all the clones.");
		    s.sendMessage(ChatColor.RED + "/cloneme reload "
			    + ChatColor.WHITE
			    + "Reloads the plugin. Keeps clones.");
		} else if (a[0].toString().equalsIgnoreCase("save")) {
		    if (!(s instanceof Player)) {
			s.sendMessage("[" + pdfdescription
				+ "] Only players can save templates!");
			return true;
		    }

		    // NPC bob = npcManager.getNPCs().get(0);
		    // bob.walkTo(((Player) s).getLocation());

		    // TODO
		    s.sendMessage(ChatColor.RED + "Not yet implemented!");
		} else if (a[0].toString().equalsIgnoreCase("load")) {
		    if (!(s instanceof Player)) {
			s.sendMessage("[" + pdfdescription
				+ "] Only players can load templates!");
			return true;
		    }

		    // TODO
		    s.sendMessage(ChatColor.RED + "Not yet implemented!");
		} else if (a[0].toString().equalsIgnoreCase("remove")) {
		    // TODO
		    s.sendMessage(ChatColor.RED + "Not yet implemented!");
		} else if (a[0].toString().equalsIgnoreCase("ready")) {
		    if (!(s instanceof Player)) {
			s.sendMessage("[" + pdfdescription
				+ "] Only players can be ready!");
			return true;
		    }

		    // TODO
		    s.sendMessage(ChatColor.RED + "Not yet implemented!");
		} else if (a[0].toString().equalsIgnoreCase("pause")) {
		    if (!(s instanceof Player)) {
			s.sendMessage("[" + pdfdescription
				+ "] Only players can pause!");
			return true;
		    }

		    // TODO
		    s.sendMessage(ChatColor.RED + "Not yet implemented!");
		} else if (a[0].toString().equalsIgnoreCase("list")) {
		    // TODO
		    s.sendMessage(ChatColor.RED + "Not yet implemented!");
		} else if (a[0].toString().equalsIgnoreCase("add")) {
		    if (!(s instanceof Player)) {
			s.sendMessage("[" + pdfdescription
				+ "] Only players can have clones!");
			return true;
		    }

		    if (a.length == 1) {
			s.sendMessage(ChatColor.BLUE + pdfdescription + " v"
				+ pdfversion + " " + ChatColor.RED
				+ "/cloneme add [params]");
			s.sendMessage("Possible parameters :");
			s.sendMessage(ChatColor.RED + "[r:0/90/180/270] "
				+ ChatColor.WHITE + "Clone will be rotated.");
			s.sendMessage(ChatColor.RED + "[m:north/west] "
				+ ChatColor.WHITE + "Clone will be mirrored.");
			s.sendMessage(ChatColor.RED + "[x:0] [z:0] [y:0] "
				+ ChatColor.WHITE
				+ "Clone will be transitioned.");
			s.sendMessage(ChatColor.RED + "[n:name] "
				+ ChatColor.WHITE
				+ "Sets the overhead name of the clone.");
			s.sendMessage("Example: " + ChatColor.GREEN
				+ "/cloneme add r:90 x:-4 z:3 n:Boblennon");
		    } else {

			int nbparam = 0;

			long xpos = 0;
			long ypos = 0;
			long zpos = 0;
			String name = s.getName();

			int rotation = 0;
			Direction dir = Direction.NONE;

			for (String arg : a) {
			    if (arg.toLowerCase().contains("x:")) {
				try {
				    xpos = Long.parseLong(arg.substring(2));
				    nbparam += 1;
				} catch (NumberFormatException ex) {
				    s.sendMessage(ChatColor.RED
					    + "["
					    + pdfdescription
					    + "] Error : Argument 'x:' need to be followed by a whole number! Ex: 'x:5'");
				    return true;
				}
			    } else if (arg.toLowerCase().contains("n:")) {
				name = arg.substring(2);
				if (name.length() == 0) {
				    s.sendMessage(ChatColor.RED
					    + "["
					    + pdfdescription
					    + "] Error : Argument 'n:' need to be followed by a string! Ex: 'n:Bob'");
				}
			    } else if (arg.toLowerCase().contains("y:")) {
				try {
				    ypos = Integer.parseInt(arg.substring(2));
				    nbparam += 1;
				} catch (NumberFormatException ex) {
				    s.sendMessage(ChatColor.RED
					    + "["
					    + pdfdescription
					    + "] Error : Argument 'y:' need to be followed by a whole number! Ex: 'y:5'");
				    return true;
				}
			    } else if (arg.toLowerCase().contains("z:")) {
				try {
				    zpos = Integer.parseInt(arg.substring(2));
				    nbparam += 1;
				} catch (NumberFormatException ex) {
				    s.sendMessage(ChatColor.RED
					    + "["
					    + pdfdescription
					    + "] Error : Argument 'z:' need to be followed by a whole number! Ex: 'z:5'");
				    return true;
				}
			    } else if (arg.toLowerCase().contains("m:")) {
				try {
				    dir = Direction.valueOf(arg.substring(2));
				    nbparam += 1;
				} catch (Exception ex) {
				    s.sendMessage(ChatColor.RED
					    + "["
					    + pdfdescription
					    + "] Error : Argument 'mirror:' need to be a valid direction! Ex: 'mirror:north'");
				    return true;
				}
			    } else if (arg.toLowerCase().contains("r:")) {
				try {
				    rotation = Integer.parseInt(arg
					    .substring(2));
				    if (rotation % 90 != 0)
					throw new NumberFormatException();
				    nbparam += 1;
				} catch (NumberFormatException ex) {
				    s.sendMessage(ChatColor.RED
					    + "["
					    + pdfdescription
					    + "] Error : Argument 'r:' need to be followed by a multiple of 90! Ex: 'r:180'");
				    return true;
				}
			    }
			}

			if (nbparam > 0) {
			    AddClone((Player) s, xpos, ypos, zpos, rotation,
				    dir, name);

			    s.sendMessage("Clone added!");
			} else {
			    s.sendMessage(ChatColor.RED
				    + "["
				    + pdfdescription
				    + "] Error : You must at least put one of these: r: x: y: z: m: ");
			    return true;
			}
		    }
		} else if (a[0].toString().equalsIgnoreCase("modify")) {
		    if (!(s instanceof Player)) {
			s.sendMessage("[" + pdfdescription
				+ "] Only players can modify clones!");
			return true;
		    }

		    if (a.length == 1) {
			s.sendMessage(ChatColor.BLUE + pdfdescription + " v"
				+ pdfversion + " " + ChatColor.RED
				+ "/cloneme modify <id> [params]");
			s.sendMessage("Use /list to get id");
			s.sendMessage("Possible parameters :");
			s.sendMessage(ChatColor.RED + "[r:0/90/180/270] "
				+ ChatColor.WHITE + "Clone will be rotated.");
			s.sendMessage(ChatColor.RED + "[mirror:north/west] "
				+ ChatColor.WHITE + "Clone will be mirrored.");
			s.sendMessage(ChatColor.RED + "[x:0] [z:0] [y:0] "
				+ ChatColor.WHITE
				+ "Clone will be transitioned.");
			s.sendMessage(ChatColor.RED + "[n:name] "
				+ ChatColor.WHITE
				+ "Sets the overhead name of the clone.");
		    } else {
			// TODO
			s.sendMessage(ChatColor.RED + "Not yet implemented!");
			// s.sendMessage("Clone modified!");
		    }
		} else if (a[0].toString().equalsIgnoreCase("stop")) {
		    String cloner = s.getName();

		    if (a.length == 2 && a[1].toString() != "") {
			cloner = a[1].toString();
		    }

		    Player pl = getServer().getPlayer(cloner);
		    OfflinePlayer opl = null;

		    if (pl == null)
			opl = getServer().getOfflinePlayer(cloner);

		    if (pl == null && opl == null) {
			s.sendMessage("Player " + cloner + " not found.");
		    } else {
			if (clonelist.containsKey(cloner)) {
			    if (npcManager != null) {
				for (Clone clone : getClones(cloner)) {
				    Packet20NamedEntitySpawn p20 = clone
					    .makeNamedEntitySpawnPacket(ChatColor.GREEN
						    + clone.getName());
				    Packet29DestroyEntity p29 = new Packet29DestroyEntity(
					    clone.getNPC().getEntityId());

				    for (Player p : this.getServer()
					    .getOnlinePlayers()) {
					((CraftPlayer) p).getHandle().netServerHandler
						.sendPacket(p29);
					((CraftPlayer) p).getHandle().netServerHandler
						.sendPacket(p20);
				    }

				    clone.remove();
				    clone = null;
				}
			    }

			    clonelist.remove(cloner);

			    if (s instanceof Player
				    && (pl == null || ((Player) s).equals(pl))) {
				s.sendMessage(cloner
					+ "'s clones have been removed!");
			    } else {
				if (!(s instanceof Player)) {
				    s.sendMessage("Clones removed!");
				}

				if (pl != null) {
				    pl.sendMessage(ChatColor.RED + s.getName()
					    + " removed your clones!");
				}
			    }
			} else {
			    s.sendMessage("Did not have any clones!");
			}
		    }
		} else if (a[0].toString().equalsIgnoreCase("stopall")) {
		    for (Set<Clone> clones : clonelist.values()) {
			for (Clone clone : clones) {
			    if (npcManager != null) {
				Packet20NamedEntitySpawn p20 = clone
					.makeNamedEntitySpawnPacket(ChatColor.GREEN
						+ clone.getName());
				Packet29DestroyEntity p29 = new Packet29DestroyEntity(
					clone.getNPC().getEntityId());

				for (Player p : this.getServer()
					.getOnlinePlayers()) {
				    ((CraftPlayer) p).getHandle().netServerHandler
					    .sendPacket(p29);
				    ((CraftPlayer) p).getHandle().netServerHandler
					    .sendPacket(p20);
				}
			    }

			    clone.remove();
			    clone = null;
			}
		    }

		    clonelist.clear();

		    s.sendMessage("Everyone's clones removed!");
		}
	    }
	    return true;
	} else {
	    return false;
	}
    }

    public void schedule(Runnable runnable, long delay) {
	getServer().getScheduler().scheduleSyncDelayedTask(this, runnable,
		delay);
    }

    public void AddClone(Player s, long xpos, long ypos, long zpos,
	    int rotation, Direction dir, String name) {
	Location start = s.getLocation();

	Clone clone = new Clone(s.getName(), xpos, ypos, zpos, rotation, dir,
		start, s.getWorld(), npcManager,
		name);

	clone.setItemInHand(s.getItemInHand());
	clone.setSneaking(s.isSneaking());

	if (!clonelist.containsKey(s.getName())) {
	    clonelist.put(s.getName(), new HashSet<Clone>());
	}
	clonelist.get(s.getName()).add(clone);

	if (npcManager != null) {
	    Packet20NamedEntitySpawn p20 = clone.makeNamedEntitySpawnPacket();
	    Packet29DestroyEntity p29 = new Packet29DestroyEntity(
		    clone.getNPC().getEntityId());

	    for (Player p : this.getServer().getOnlinePlayers()) {
		((CraftPlayer) p).getHandle().netServerHandler.sendPacket(p29);
		((CraftPlayer) p).getHandle().netServerHandler.sendPacket(p20);
	    }
	}
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

    public Set<Clone> getClones(String name) {
	return clonelist.get(name);
    }

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
	    permissions3 = false;
	    logger.info("[" + pdfdescription + "] PermissionsEx "
		    + pexTest.getDescription().getVersion() + " found");
	    return;
	} else if (permTest == null) {
	    logger.info("[" + pdfdescription
		    + "] Permissions not found, using SuperPerms");
	    return;
	}
	// Check if it's a bridge
	if (permTest.getDescription().getVersion().startsWith("2.7.7")) {
	    logger.info("[" + pdfdescription
		    + "] Found Permissions Bridge. Using SuperPerms");
	    return;
	}

	// We're using Permissions
	permissions = ((Permissions) permTest).getHandler();
	// Check for Permissions 3
	permissions3 = permTest.getDescription().getVersion().startsWith("3");
	logger.info("[" + pdfdescription + "] Permissions "
		+ permTest.getDescription().getVersion() + " found");
    }

    public Boolean checkPermissions(Player player, String node) {
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
		|| player.hasPermission(pdfdescription + ".*")
		|| player.hasPermission("*")) {
	    return true;
	} else if (player.isOp()) {
	    return true;
	}
	return false;
    }
}
