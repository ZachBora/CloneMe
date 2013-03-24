package com.worldcretornica.cloneme;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.worldcretornica.cloneme.commands.CMCommand;
import com.worldcretornica.cloneme.listeners.CloneBlockListener;
import com.worldcretornica.cloneme.listeners.ClonePlayerListener;
import com.worldcretornica.cloneme.listeners.NPCClonePlayerListener;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;

public class CloneMe extends JavaPlugin {

	// FINAL
	public static String NAME;
	public static String PREFIX;
	public static String VERSION;

	public final Logger logger = Logger.getLogger("Minecraft"); // TODO
	
	// NPC!
	//private NPCManager npcManager;

	private CloneManager cloneManager;

	public boolean usingNPC;
	
	public static boolean usingNCP = false;
	
	public Map<String, Integer> toBeRemoved = null;
		
	@Override
	public void onDisable() {
		cloneManager.removeAllClones();
	}

	@Override
	public void onEnable() {

		PluginDescriptionFile pdfFile = this.getDescription();
		NAME = pdfFile.getName();
		PREFIX = "[" + NAME + "]";
		VERSION = pdfFile.getVersion();
		
		cloneManager = new CloneManager(this);
		getServer().getPluginManager().registerEvents(new CloneBlockListener(this), this);
		getServer().getPluginManager().registerEvents(new ClonePlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new NPCClonePlayerListener(this), this);
		
		
		if(getServer().getPluginManager().isPluginEnabled("NoCheatPlus"))
		{
			logger.info(PREFIX + " NoCheatPlus detected, hooking");
			usingNCP = true;
		}
		
		/*if (getServer().getPluginManager().isPluginEnabled("NPCCreatures")) {
			
			Plugin npccreature = getServer().getPluginManager().getPlugin("NPCCreatures");

			npcManager = ((NPCCreatures) npccreature).getNPCManager();

			this.logger.info(PREFIX + " NPCCreatures v" + npccreature.getDescription().getVersion() + " found");

			npccreature = null;
		} else {
			npcManager = null;
			this.logger.info(PREFIX + "Could not find NPCCreatures, clones will not be visible!");
		}*/
					
		getCommand("cloneme").setExecutor(new CMCommand(this));
		
		toBeRemoved = new HashMap<String, Integer>();
	}

	/*public NPCManager getNPCManager() {
		return npcManager;
	}*/

	public CloneManager getCloneManager() {
		return cloneManager;
	}

	public void schedule(Runnable runnable, long delay) {
		getServer().getScheduler().scheduleSyncDelayedTask(this, runnable, delay);
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


	public boolean checkPermissions(Player player, String node) {
		if (player.hasPermission(node) || player.hasPermission(NAME + ".*") || player.hasPermission("*")) {
			return true;
		} else if (player.isOp()) {
			return true;
		}
		return false;
	}
	
	public static void ncpExempt(Player player, CheckType checktype)
	{
		NCPExemptionManager.exemptPermanently(player, checktype);
	}
	
	public static void ncpUnexempt(Player player, CheckType checktype)
	{
		NCPExemptionManager.unexempt(player, checktype);
	}
	
	public static boolean isncpExempt(Player player, CheckType checktype)
	{
		return NCPExemptionManager.isExempted(player, checktype);
	}
}
