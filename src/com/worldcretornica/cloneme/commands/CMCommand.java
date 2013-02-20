package com.worldcretornica.cloneme.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.worldcretornica.cloneme.Clone;
import com.worldcretornica.cloneme.Clone.Direction;
import com.worldcretornica.cloneme.CloneMe;
import com.worldcretornica.cloneme.ClonedPlayer;

import java.util.List;
import java.util.Map;

public class CMCommand implements CommandExecutor {

	private CloneMe plugin;

	public CMCommand(CloneMe plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) 
	{
		if (l.equalsIgnoreCase("cloneme") || l.equalsIgnoreCase("clone") || l.equalsIgnoreCase("c")) 
		{
			if (!(s instanceof Player) || plugin.checkPermissions((Player) s, "CloneMe.use")) 
			{
				if (a.length == 0) 
				{
					return Help(s);
				}
				else
				{
					String a0 = a[0].toString();
					if (a0.equalsIgnoreCase("help")) {return Help(s);}
					else if (a0.equalsIgnoreCase("save")) {return Save(s, a);}
					else if (a0.equalsIgnoreCase("load")) {return Load(s, a);} 
					else if (a0.equalsIgnoreCase("remove")) {return Remove(s, a);} 
					else if (a0.equalsIgnoreCase("ready")) {return Ready(s, a);} 
					else if (a0.equalsIgnoreCase("limit")) {return Limit(s, a);} 
					else if (a0.equalsIgnoreCase("pause")) {return Pause(s, a);} 
					else if (a0.equalsIgnoreCase("list")) {return List(s, a);}
					else if (a0.equalsIgnoreCase("players")) {return Players(s, a);} 
					else if (a0.equalsIgnoreCase("add")) {return Add(s, a);} 
					else if (a0.equalsIgnoreCase("modify")) {return Modify(s, a);} 
					else if (a0.equalsIgnoreCase("stop")) {return Stop(s, a);} 
					else if (a0.equalsIgnoreCase("stopall")) {return StopAll(s, a);}
				}
			}
		}
		return false;
	}

	private boolean StopAll(CommandSender s, String[] a) 
	{
		plugin.getCloneManager().removeAllClones();

		s.sendMessage(ChatColor.BLUE + CloneMe.PREFIX + " Everyone's clones have been removed!");
		return true;
	}

	private boolean Stop(CommandSender s, String[] a) 
	{
		String cloner = s.getName();

		if (a.length == 2 && a[1].toString() != "") {
			cloner = a[1].toString();
		}

		Player pl = plugin.getServer().getPlayer(cloner);
		OfflinePlayer opl = null;

		if (pl == null)
			opl = plugin.getServer().getOfflinePlayer(cloner);

		if (pl == null && opl == null) 
		{
			s.sendMessage("Player " + cloner + " not found.");
		} 
		else 
		{
			if(pl != null)
				plugin.getCloneManager().removeClones(pl);
			
			if(opl != null)
				plugin.getCloneManager().removeClones(opl);

			if (s instanceof Player && (pl != null || ((Player) s).equals(pl))) 
			{
				s.sendMessage(pl.getName() + "'s clones have been removed!");
			} 
			else 
			{
				if (!(s instanceof Player)) {
					s.sendMessage("Clones removed!");
				}

				if (pl != null) {
					s.sendMessage(pl.getName() + "'s clones have been removed!");
					pl.sendMessage(ChatColor.RED + s.getName() + " removed your clones!");
				}
			}
		}
		return true;
	}

	private boolean Modify(CommandSender s, String[] a) 
	{
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
		} 
		else 
		{
			Clone clone = null;
			int i = -1;
			
			try
			{
				i = Integer.parseInt(a[1]);
			}
			catch(NumberFormatException e)
			{
				s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " <id> needs to be a number");
				return true;
			}
			
			if(i > -1)
			{
				List<Clone> clones = plugin.getCloneManager().getClones((Player) s);
				
				if(i > clones.size() || i <= 0)
				{
					s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : You only have " + clones.size() + " clones.");
					return true;
				}
				
				clone = clones.get(i-1);
				
				if(clone != null)
				{
					int nbparam = 0;
	
					for (String arg : a) {
						if (arg.toLowerCase().contains("x:")) {
							try {
								clone.xtrans = Long.parseLong(arg.substring(2));
								nbparam += 1;
							} catch (NumberFormatException ex) {
								s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'x:' need to be followed by a whole number! Ex: 'x:5'");
								return true;
							}
						} else if (arg.toLowerCase().contains("n:")) {
							if (arg.substring(2).length() == 0) {
								s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'n:' need to be followed by a string! Ex: 'n:Bob'");
								return true;
							}
							nbparam += 1;
							clone.setName(arg.substring(2));
						} else if (arg.toLowerCase().contains("y:")) {
							try {
								clone.ytrans = Integer.parseInt(arg.substring(2));
								nbparam += 1;
							} catch (NumberFormatException ex) {
								s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'y:' need to be followed by a whole number! Ex: 'y:5'");
								return true;
							}
						} else if (arg.toLowerCase().contains("z:")) {
							try {
								clone.ztrans = Integer.parseInt(arg.substring(2));
								nbparam += 1;
							} catch (NumberFormatException ex) {
								s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'z:' need to be followed by a whole number! Ex: 'z:5'");
								return true;
							}
						} else if (arg.toLowerCase().contains("m:")) {
							try {
								Direction dir = Direction.valueOf(arg.substring(2).toUpperCase().toString());
								
								if (dir == Direction.WEST || dir == Direction.EAST) 
								{
									clone.mirror = Direction.EAST;
									nbparam += 1;
								} 
								else if (dir == Direction.SOUTH || dir == Direction.NORTH) 
								{
									clone.mirror = Direction.NORTH;
									nbparam += 1;
								}
								
							} catch (Exception ex) {
								s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'm:' need to be a valid direction! Ex: 'm:north'");
								return true;
							}
						} else if (arg.toLowerCase().contains("r:")) {
							try {
								int rotation = Integer.parseInt(arg.substring(2));
								if (rotation % 90 != 0)
									throw new NumberFormatException();
								clone.rotation = rotation;
								nbparam += 1;
							} catch (NumberFormatException ex) {
								s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Argument 'r:' need to be followed by a multiple of 90! Ex: 'r:180'");
								return true;
							}
						}
					}
	
					if (nbparam > 0) 
					{
						clone.refresh();
						s.sendMessage("Clone modified!");
					} 
					else 
					{
						s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : You must at least put one of these: r: x: y: z: m: n:");
					}
				}
				else 
				{
					s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : Clone not found");
				}
			}
		}
		return true;
	}

	private boolean Add(CommandSender s, String[] a) 
	{
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Only players can have clones!");
			return true;
		}

		if (a.length == 1) {
			s.sendMessage(ChatColor.BLUE + CloneMe.NAME + " v" + CloneMe.VERSION + " " + ChatColor.RED + "/cloneme add [params]");
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
						dir = Direction.valueOf(arg.substring(2).toUpperCase().toString());
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
		return true;
	}

	private boolean List(CommandSender s, String[] a) 
	{
		String name = s.getName();
		
		if(a.length > 1)
		{
			name = a[1];
		}
		
		List<Clone> clones = plugin.getCloneManager().getClones(name);
		
		if(clones != null && clones.size() > 0)
		{
			s.sendMessage("Clones of " + name + " :");
			for(int i = 0; i < clones.size(); i++)
			{
				Clone clone = clones.get(i);
				
				String message = " " + (i+1) + "> " + clone.getName();
				if(clone.xtrans != 0)
					message += " X:" + clone.xtrans;
				
				if(clone.ytrans != 0)
					message += " Y:" + clone.ytrans;
				
				if(clone.ztrans != 0)
					message += " Z:" + clone.ztrans;
				
				if(clone.mirror != Direction.NONE)
					message += " M:" + clone.mirror.name();
				
				if(clone.rotation != 0)
					message += " R:" + clone.rotation;
				
				//;" " + clone.getName() + " X:" + clone.xtrans + ",Y:" + clone.ytrans + ",Z:" + clone.ztrans + " ");
				s.sendMessage(message);
			}
		}
		else
		{
			if(name.equalsIgnoreCase(s.getName()))
			{
				s.sendMessage(CloneMe.PREFIX + " You do not have clones.");
			}
			else
			{
				s.sendMessage(CloneMe.PREFIX + " " + name + " does not have clones.");
			}
		}
		
		return true;
	}
	
	private boolean Players(CommandSender s, String[] a) 
	{		
		Map<String, ClonedPlayer> players = plugin.getCloneManager().getClones();
		
		if(players != null && players.size() > 0)
		{
			s.sendMessage("Players using clones: ");
			for(String name : players.keySet())
			{
				s.sendMessage(" > " + name);
			}
		}
		else
		{
			s.sendMessage(CloneMe.PREFIX + " Noone has clones.");
		}
		
		return true;
	}

	private boolean Pause(CommandSender s, String[] a) 
	{
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Only players can pause!");
			return true;
		}

		plugin.getCloneManager().PauseClones(s.getName());
		s.sendMessage(ChatColor.BLUE + CloneMe.PREFIX + "Clones paused!");
		return true;
	}

	private boolean Limit(CommandSender s, String[] a) 
	{
		if (a.length == 1) {
			s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Usage: " + ChatColor.GREEN + "/cloneme limit 100");
		}else{
			int limit = 100;
			try {
				limit = Integer.parseInt(a[1]);
			} catch (NumberFormatException ex) {
				s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : limit needs to be followed by a whole number! Ex: '100'");
				return true;
			}
			plugin.getCloneManager().SetLimit(s.getName(), limit);
			s.sendMessage(ChatColor.BLUE + CloneMe.PREFIX + " Limit set to " + limit);
		}
		return true;
	}

	private boolean Ready(CommandSender s, String[] a) 
	{
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Only players can be ready!");
			return true;
		}

		plugin.getCloneManager().UnpauseClones(s.getName());
		s.sendMessage("Clones unpaused!");
		return true;
	}

	private boolean Remove(CommandSender s, String[] a) 
	{
		if (!(s instanceof Player)) {
			s.sendMessage(CloneMe.PREFIX + " Only players can remove clones!");
			return true;
		}

		Clone clone = null;
		int i = -1;
		
		try
		{
			i = Integer.parseInt(a[1]);
		}
		catch(NumberFormatException e)
		{
			s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " <id> needs to be a number");
			return true;
		}
		
		if(i > -1)
		{
			List<Clone> clones = plugin.getCloneManager().getClones((Player) s);
			
			if(i > clones.size() || i <= 0)
			{
				s.sendMessage(ChatColor.RED + CloneMe.PREFIX + " Error : You only have " + clones.size() + " clones.");
				return true;
			}
			
			clone = clones.get(i-1);
			clone.remove();
			clones.remove(i-1);
		}
		
		return true;
	}

	private boolean Load(CommandSender s, String[] a) 
	{
		if (!(s instanceof Player)) {
			s.sendMessage(CloneMe.PREFIX + " Only players can load templates!");
			return true;
		}

		// TODO
		s.sendMessage(ChatColor.RED + CloneMe.PREFIX + "Not yet implemented!");
		return true;
	}

	private boolean Save(CommandSender s, String[] a) 
	{
		if (!(s instanceof Player)) 
		{
			s.sendMessage(CloneMe.NAME + " Only players can save templates!");
			return true;
		}
		

		// NPC bob = npcManager.getNPCs().get(0);
		// bob.walkTo(((Player) s).getLocation());

		// TODO
		s.sendMessage(ChatColor.RED + CloneMe.PREFIX + "Not yet implemented!");
		return true;
	}

	private boolean Help(CommandSender s) 
	{
		s.sendMessage(ChatColor.BLUE + CloneMe.NAME + " v" + CloneMe.VERSION + ChatColor.WHITE + " [] means optional, <> means obligated");
		if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme add " + ChatColor.WHITE + "Adds a new clone.");
		if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme modify " + ChatColor.GREEN + "<id> " + ChatColor.WHITE + "Modify a clone. Use /list to get ids.");
		if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme remove " + ChatColor.GREEN + "<id> " + ChatColor.WHITE + "Removes a clone. Use /list to get ids.");
		if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme ready " + ChatColor.WHITE + "Starts your clones.");
		if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme pause " + ChatColor.WHITE + "Pause your clones.");
		s.sendMessage(ChatColor.RED + "/cloneme players " + ChatColor.WHITE + "Lists players using clones.");
		s.sendMessage(ChatColor.RED + "/cloneme list " + ChatColor.GREEN + "[name] " + ChatColor.WHITE + "Lists clones.");
		s.sendMessage(ChatColor.RED + "/cloneme users " + ChatColor.WHITE + "Lists clone users.");
		if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme save " + ChatColor.GREEN + "<name> " + ChatColor.WHITE + "Save your clones in a template.");
		if (s instanceof Player)
			s.sendMessage(ChatColor.RED + "/cloneme load " + ChatColor.GREEN + "<name> " + ChatColor.WHITE + "Load clones from a template.");
		s.sendMessage(ChatColor.RED + "/cloneme stop " + ChatColor.GREEN + "[name] " + ChatColor.WHITE + "Removes the clones.");
		s.sendMessage(ChatColor.RED + "/cloneme stopall " + ChatColor.WHITE + "Stop all the clones.");
		s.sendMessage(ChatColor.RED + "/cloneme reload " + ChatColor.WHITE + "Reloads the plugin. Keeps clones.");
		
		return true;
	}
}
