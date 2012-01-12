package com.worldcretornica.cloneme.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.worldcretornica.cloneme.Clone.Direction;
import com.worldcretornica.cloneme.CloneMe;

public class CMCommand implements CommandExecutor {

	private CloneMe plugin;

	public CMCommand(CloneMe plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("cloneme")) {
			if (!(s instanceof Player) || plugin.checkPermissions((Player) s, "CloneMe.use")) {
				if (a.length == 0) {
					s.sendMessage(ChatColor.BLUE + CloneMe.NAME + " v" + CloneMe.VERSION + ChatColor.WHITE + " [] means optional, <> means obligated");
					if (s instanceof Player)
						s.sendMessage(ChatColor.RED + "/cloneme add " + ChatColor.WHITE + "Adds a new clone.");
					if (s instanceof Player)
						s.sendMessage(ChatColor.RED + "/cloneme modify " + ChatColor.GREEN + "<id> " + ChatColor.WHITE + "Modify a clone. Use /list to get ids.");
					s.sendMessage(ChatColor.RED + "/cloneme remove " + ChatColor.GREEN + "<id> " + ChatColor.WHITE + "Removes a clone. Use /list to get ids.");
					if (s instanceof Player)
						s.sendMessage(ChatColor.RED + "/cloneme ready " + ChatColor.WHITE + "Starts your clones.");
					if (s instanceof Player)
						s.sendMessage(ChatColor.RED + "/cloneme pause " + ChatColor.WHITE + "Pause your clones.");
					s.sendMessage(ChatColor.RED + "/cloneme list " + ChatColor.GREEN + "[name] " + ChatColor.WHITE + "Lists clones.");
					s.sendMessage(ChatColor.RED + "/cloneme users " + ChatColor.WHITE + "Lists clone users.");
					if (s instanceof Player)
						s.sendMessage(ChatColor.RED + "/cloneme save " + ChatColor.GREEN + "<name> " + ChatColor.WHITE + "Save your clones in a template.");
					if (s instanceof Player)
						s.sendMessage(ChatColor.RED + "/cloneme load " + ChatColor.GREEN + "<name> " + ChatColor.WHITE + "Load clones from a template.");
					s.sendMessage(ChatColor.RED + "/cloneme stop " + ChatColor.GREEN + "[name] " + ChatColor.WHITE + "Removes the clones.");
					s.sendMessage(ChatColor.RED + "/cloneme stopall " + ChatColor.WHITE + "Stop all the clones.");
					s.sendMessage(ChatColor.RED + "/cloneme reload " + ChatColor.WHITE + "Reloads the plugin. Keeps clones.");
				} else if (a[0].toString().equalsIgnoreCase("save")) {
					if (!(s instanceof Player)) {
						s.sendMessage(CloneMe.NAME + " Only players can save templates!");
						return true;
					}

					// NPC bob = npcManager.getNPCs().get(0);
					// bob.walkTo(((Player) s).getLocation());

					// TODO
					s.sendMessage(ChatColor.RED + "Not yet implemented!");
				} else if (a[0].toString().equalsIgnoreCase("load")) {
					if (!(s instanceof Player)) {
						s.sendMessage(CloneMe.PREFIX + " Only players can load templates!");
						return true;
					}

					// TODO
					s.sendMessage(ChatColor.RED + "Not yet implemented!");
				} else if (a[0].toString().equalsIgnoreCase("remove")) {
					// TODO
					s.sendMessage(ChatColor.RED + "Not yet implemented!");
				} else if (a[0].toString().equalsIgnoreCase("ready")) {
					if (!(s instanceof Player)) {
						s.sendMessage(CloneMe.PREFIX + " Only players can be ready!");
						return true;
					}

					// TODO
					s.sendMessage(ChatColor.RED + "Not yet implemented!");
				} else if (a[0].toString().equalsIgnoreCase("pause")) {
					if (!(s instanceof Player)) {
						s.sendMessage(CloneMe.PREFIX + " Only players can pause!");
						return true;
					}

					// TODO
					s.sendMessage(ChatColor.RED + "Not yet implemented!");
				} else if (a[0].toString().equalsIgnoreCase("list")) {
					// TODO
					s.sendMessage(ChatColor.RED + "Not yet implemented!");
				} else if (a[0].toString().equalsIgnoreCase("add")) {
					if (!(s instanceof Player)) {
						s.sendMessage(CloneMe.PREFIX + " Only players can have clones!");
						return true;
					}

					if (a.length == 1) {
						s.sendMessage(ChatColor.BLUE + CloneMe.NAME + " v" + CloneMe.VERSION + "_" + CloneMe.VERSION + " " + ChatColor.RED + "/cloneme add [params]");
						s.sendMessage("Possible parameters :");
						s.sendMessage(ChatColor.RED + "[r:0/90/180/270] " + ChatColor.WHITE + "Clone will be rotated.");
						s.sendMessage(ChatColor.RED + "[m:north/west] " + ChatColor.WHITE + "Clone will be mirrored.");
						s.sendMessage(ChatColor.RED + "[x:0] [z:0] [y:0] " + ChatColor.WHITE + "Clone will be transitioned.");
						s.sendMessage(ChatColor.RED + "[n:name] " + ChatColor.WHITE + "Sets the overhead name of the clone.");
						s.sendMessage("Example: " + ChatColor.GREEN + "/cloneme add r:90 x:-4 z:3 n:Boblennon");
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
									s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'x:' need to be followed by a whole number! Ex: 'x:5'");
									return true;
								}
							} else if (arg.toLowerCase().contains("n:")) {
								name = arg.substring(2);
								if (name.length() == 0) {
									s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'n:' need to be followed by a string! Ex: 'n:Bob'");
								}
							} else if (arg.toLowerCase().contains("y:")) {
								try {
									ypos = Integer.parseInt(arg.substring(2));
									nbparam += 1;
								} catch (NumberFormatException ex) {
									s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'y:' need to be followed by a whole number! Ex: 'y:5'");
									return true;
								}
							} else if (arg.toLowerCase().contains("z:")) {
								try {
									zpos = Integer.parseInt(arg.substring(2));
									nbparam += 1;
								} catch (NumberFormatException ex) {
									s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'z:' need to be followed by a whole number! Ex: 'z:5'");
									return true;
								}
							} else if (arg.toLowerCase().contains("m:")) {
								try {
									dir = Direction.valueOf(arg.substring(2).toUpperCase());
									nbparam += 1;
								} catch (Exception ex) {
									s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'm:' need to be a valid direction! Ex: 'm:north'");
									return true;
								}
							} else if (arg.toLowerCase().contains("r:")) {
								try {
									rotation = Integer.parseInt(arg.substring(2));
									if (rotation % 90 != 0)
										throw new NumberFormatException();
									nbparam += 1;
								} catch (NumberFormatException ex) {
									s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'r:' need to be followed by a multiple of 90! Ex: 'r:180'");
									return true;
								}
							}
						}

						if (nbparam > 0) {
							plugin.getCloneManager().spawnClone((Player) s, xpos, ypos, zpos, rotation, dir, name);

							s.sendMessage("Clone added!");
						} else {
							s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : You must at least put one of these: r: x: y: z: m: ");
							return true;
						}
					}
				} else if (a[0].toString().equalsIgnoreCase("modify")) {
					if (!(s instanceof Player)) {
						s.sendMessage(CloneMe.PREFIX + " Only players can modify clones!");
						return true;
					}

					if (a.length == 1) {
						s.sendMessage(ChatColor.BLUE + CloneMe.NAME + " v" + CloneMe.VERSION + " " + ChatColor.RED + "/cloneme modify <id> [params]");
						s.sendMessage("Use /list to get id");
						s.sendMessage("Possible parameters :");
						s.sendMessage(ChatColor.RED + "[r:0/90/180/270] " + ChatColor.WHITE + "Clone will be rotated.");
						s.sendMessage(ChatColor.RED + "[mirror:north/west] " + ChatColor.WHITE + "Clone will be mirrored.");
						s.sendMessage(ChatColor.RED + "[x:0] [z:0] [y:0] " + ChatColor.WHITE + "Clone will be transitioned.");
						s.sendMessage(ChatColor.RED + "[n:name] " + ChatColor.WHITE + "Sets the overhead name of the clone.");
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

					Player pl = plugin.getServer().getPlayer(cloner);
					OfflinePlayer opl = null;

					if (pl == null)
						opl = plugin.getServer().getOfflinePlayer(cloner);

					if (pl == null && opl == null) {
						s.sendMessage("Player " + cloner + " not found.");
					} else {

						plugin.getCloneManager().removeClones(cloner);

						if (s instanceof Player && (pl == null || ((Player) s).equals(pl))) {
							s.sendMessage(cloner + "'s clones have been removed!");
						} else {
							if (!(s instanceof Player)) {
								s.sendMessage("Clones removed!");
							}

							if (pl != null) {
								pl.sendMessage(ChatColor.RED + s.getName() + " removed your clones!");
							}
						}
					}
				} else if (a[0].toString().equalsIgnoreCase("stopall")) {
					plugin.getCloneManager().removeAllClones();

					s.sendMessage("Everyone's clones removed!");
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
