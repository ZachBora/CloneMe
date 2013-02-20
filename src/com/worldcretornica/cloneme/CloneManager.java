package com.worldcretornica.cloneme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.worldcretornica.cloneme.Clone.Direction;

public class CloneManager {

	@SuppressWarnings("unused")
	private final CloneMe plugin;

	private Map<String, ClonedPlayer> clones = new HashMap<String, ClonedPlayer>();

	public CloneManager(CloneMe plugin) {
		this.plugin = plugin;
	}

	public void spawnClone(Player player, long xpos, long ypos, long zpos, int rotation, Direction dir, String name) {
		Location start = player.getLocation();

		if (!clones.containsKey(player.getName())) {
			clones.put(player.getName(), new ClonedPlayer());
		}
		
		Clone clone;
		try {
			clone = new Clone(player.getName(), xpos, ypos, zpos, rotation, dir, start, player.getWorld(), /*plugin.getNPCManager(),*/ name, clones.get(player.getName()));
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED + "Cannot create clone: " + e.getMessage());
			return;
		}
		
		clone.setItemInHand(player.getItemInHand());
		clone.setSneaking(player.isSneaking());

		
		clones.get(player.getName()).clones.add(clone);

		/*if (plugin.getNPCManager() != null) {
			Packet20NamedEntitySpawn p20 = clone.makeNamedEntitySpawnPacket();
			Packet29DestroyEntity p29 = new Packet29DestroyEntity(clone.getNPC().getEntityId());

			for (Player p : plugin.getServer().getOnlinePlayers()) {
				((CraftPlayer) p).getHandle().netServerHandler.sendPacket(p29);
				((CraftPlayer) p).getHandle().netServerHandler.sendPacket(p20);
			}
		}*/
	}
 
	public void removeAllClones() {
		for (String player : clones.keySet()) {
			removeClones(player);
		}
	}

	public void removeClones(OfflinePlayer opl) {
		removeClones(opl.getName());
	}

	public void removeClones(String player) {
		if(hasClones(player))
		{
			for (Clone clone : getClones(player)) {
				/*if (plugin.getNPCManager() != null) {
					Packet29DestroyEntity p29 = new Packet29DestroyEntity(clone.getNPC().getEntityId());
	
					for (Player p : plugin.getServer().getOnlinePlayers()) {
						((CraftPlayer) p).getHandle().netServerHandler.sendPacket(p29);
					}
				}*/
				clone.remove();
			}
			clones.remove(player);
		}
	}

	public Map<String, ClonedPlayer> getClones() {
		return clones;
	}

	public List<Clone> getClones(OfflinePlayer p) 
	{
		if(hasClones(p))
		{
			return clones.get(p.getName()).clones;
		}
		else
		{
			return null;
		}
	}

	public List<Clone> getClones(String player) 
	{
		OfflinePlayer p = Bukkit.getPlayer(player);
		
		if(p == null)
		{
			p = Bukkit.getOfflinePlayer(player);
		}

		if(p != null)
		{
			return getClones(p);
		}
		else
		{
			return null;
		}
	}

	public boolean hasClones(OfflinePlayer p) {
		return hasClones(p.getName());
	}

	public boolean hasClones(String player) {
		return clones.containsKey(player);
	}
	
	public void PauseClones(String player) {
		if(!hasClones(player))
		{
			clones.put(player, new ClonedPlayer());
		}
		clones.get(player).paused = true;
	}
	
	public void UnpauseClones(String player) {
		if(!hasClones(player))
		{
			clones.put(player, new ClonedPlayer());
		}
		clones.get(player).paused = false;
	}
	
	public boolean IsPaused(String player) {
		if(!hasClones(player))
		{
			clones.put(player, new ClonedPlayer());
		}
		return clones.get(player).paused;
	}
	
	public void SetLimit(String player, int Limit) {
		if(!hasClones(player))
		{
			clones.put(player, new ClonedPlayer());
		}
		clones.get(player).limit = Limit;
	}

}
