package com.worldcretornica.cloneme;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet29DestroyEntity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.worldcretornica.cloneme.Clone.Direction;

public class CloneManager {

	private final CloneMe plugin;

	private Map<String, Set<Clone>> clones = new HashMap<String, Set<Clone>>();

	public CloneManager(CloneMe plugin) {
		this.plugin = plugin;
	}

	public void spawnClone(Player player, long xpos, long ypos, long zpos, int rotation, Direction dir, String name) {
		Location start = player.getLocation();

		Clone clone = new Clone(player.getName(), xpos, ypos, zpos, rotation, dir, start, player.getWorld(), plugin.getNPCManager(), name);

		clone.setItemInHand(player.getItemInHand());
		clone.setSneaking(player.isSneaking());

		if (!clones.containsKey(player.getName())) {
			clones.put(player.getName(), new HashSet<Clone>());
		}
		clones.get(player.getName()).add(clone);

		if (plugin.getNPCManager() != null) {
			Packet20NamedEntitySpawn p20 = clone.makeNamedEntitySpawnPacket();
			Packet29DestroyEntity p29 = new Packet29DestroyEntity(clone.getNPC().getEntityId());

			for (Player p : plugin.getServer().getOnlinePlayers()) {
				((CraftPlayer) p).getHandle().netServerHandler.sendPacket(p29);
				((CraftPlayer) p).getHandle().netServerHandler.sendPacket(p20);
			}
		}
	}

	public void removeAllClones() {
		for (String player : clones.keySet()) {
			removeClones(player);
		}
	}

	public void removeClones(Player player) {
		removeClones(player.getName());
	}

	public void removeClones(String player) {
		for (Clone clone : getClones(player)) {
			if (plugin.getNPCManager() != null) {
				Packet29DestroyEntity p29 = new Packet29DestroyEntity(clone.getNPC().getEntityId());

				for (Player p : plugin.getServer().getOnlinePlayers()) {
					((CraftPlayer) p).getHandle().netServerHandler.sendPacket(p29);
				}
			}
			clone.remove();
		}
		clones.remove(player);
	}

	public Map<String, Set<Clone>> getClones() {
		return clones;
	}

	public Set<Clone> getClones(Player player) {
		return getClones(player.getName());
	}

	public Set<Clone> getClones(String player) {
		return clones.get(player);
	}

	public boolean hasClones(Player player) {
		return hasClones(player.getName());
	}

	public boolean hasClones(String player) {
		return clones.containsKey(player);
	}

}
