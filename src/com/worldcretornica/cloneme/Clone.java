package com.worldcretornica.cloneme;

import java.util.logging.Logger;

import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet20NamedEntitySpawn;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.worldcretornica.cloneme.utils.CMUtils;

import tk.npccreatures.npcs.NPCManager;
import tk.npccreatures.npcs.entity.HumanNPC;
import tk.npccreatures.npcs.entity.NPC;
import tk.npccreatures.npcs.nms.NPCHuman;
import tk.npccreatures.npcs.NPCType;

public class Clone {

    public final Logger logger = Logger.getLogger("Minecraft"); // TODO

    public enum Direction {
	NONE, EAST, WEST, SOUTH, NORTH,
	/*
	 * NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST
	 */
    }

    private String owner;
    private long xtrans;
    private long ytrans;
    private long ztrans;
    private int rotation;
    private Direction mirror;
  /*  private double xstart;
    private double ystart;
    private double zstart;
    private int xbstart;
    private int ybstart;
    private int zbstart; */
    private Location startingLocation;
    private NPC npc;
    private String name;

    /*
     * public Clone() { owner = ""; xtrans = 0; ytrans = 0; ztrans = 0; rotation
     * = 0; mirror = Direction.NONE; xstart = 0; ystart = 0; zstart = 0; xbstart
     * = 0; ybstart = 0; zbstart = 0; npc = null; name = ""; // SHOULDN'T BE
     * USED }
     */

    public Clone(String owner, long xtrans, long ytrans, long ztrans,
	    int rotation, Direction mirror, Location startingLocation,/*double xstart, double ystart,
	    double zstart, int xbstart, int ybstart, int zbstart, */ World world,
	    NPCManager manager, String name) {
	this.owner = owner;
	this.xtrans = xtrans;
	this.ytrans = ytrans;
	this.ztrans = ztrans;
	this.rotation = rotation;
	this.startingLocation = startingLocation;
	if (mirror == Direction.WEST) {
	    this.mirror = Direction.EAST;
	} else if (mirror == Direction.SOUTH) {
	    this.mirror = Direction.NORTH;
	} else {
	    this.mirror = mirror;
	}
	/*this.xstart = xstart;
	this.ystart = ystart;
	this.zstart = zstart;
	this.xbstart = xbstart;
	this.ybstart = ybstart;
	this.zbstart = zbstart; */
	this.name = name;

	if (manager != null) {
	    npc = manager.spawnNPC(owner, getNewLocation(new Location(world,
		    startingLocation.getX(), startingLocation.getY(), startingLocation.getZ())), NPCType.HUMAN);

	    ((NPCHuman) npc.getEntity()).name = name;
	}
    }
    
    public String getOwner() {
	return owner;
    }
    
    public String getName() {
	return name;
    }
    
    public NPC getNPC() {
	return npc;
    }
    
    public Location getStartingLocation() {
	return startingLocation;
    }

    public void remove() {
	if (npc != null) {
	    npc.removeFromWorld();
	}
	owner = "";
	xtrans = 0;
	ytrans = 0;
	ztrans = 0;
	rotation = 0;
	mirror = Direction.NONE;
	/*xstart = 0;
	ystart = 0;
	zstart = 0;
	xbstart = 0;
	ybstart = 0;
	zbstart = 0; */
	npc = null;
    }

    public void move(Location playerlocation) {
	if (npc != null) {
	    Location newloc = getNewLocation(playerlocation);

	    npc.teleport(newloc);
	}
    }

    public Location getNewLocation(Location oldlocation) {
	Location location = oldlocation.clone();
	location.add(xtrans, ytrans, ztrans);

	double x = location.getX() - startingLocation.getX();
	double z = location.getZ() - startingLocation.getZ();

	if (rotation == 90) {
	    location.setX(-z);
	    location.setZ(x);
	} else if (rotation == 180) {
	    location.setX(-x);
	    location.setZ(-z);
	} else if (rotation == 270) {
	    location.setX(z);
	    location.setZ(-x);
	} else {
	    location.setX(x);
	    location.setZ(z);
	}

	if (mirror == Direction.EAST || mirror == Direction.WEST) {
	    location.setZ(-location.getZ());
	    location.setYaw(180 - (oldlocation.getYaw() + rotation));
	} else if (mirror == Direction.NORTH || mirror == Direction.SOUTH) {
	    location.setX(-location.getX());
	    location.setYaw(360 - (oldlocation.getYaw() + rotation));
	} else {
	    location.setYaw(oldlocation.getYaw() + rotation);
	}

	location.add(startingLocation.getX(), 0, startingLocation.getZ());

	return location;
    }

    public Block setNewBlock(Block oldblock, boolean settoair) {
	Block newblock = getNewBlockLocation(oldblock);
	Byte data = getNewBlockData(oldblock);
	Material mat = oldblock.getType();

	if (settoair) {
	    newblock.setType(Material.AIR);
	} else {
	    newblock.setTypeIdAndData(mat.getId(), data, true);

	    if (mat.equals(Material.WOODEN_DOOR)
		    || mat.equals(Material.IRON_DOOR)) {
		newblock.getWorld()
			.getBlockAt(newblock.getLocation().clone().add(0, 1, 0))
			.setTypeIdAndData(mat.getId(), (byte) (data | 0x8),
				true);
	    }
	}

	return newblock;
    }

    public Block getNewBlockLocation(Block oldblock) {
	Location location = oldblock.getLocation().clone();

	location.add(xtrans, ytrans, ztrans);

	double x = location.getBlockX() - startingLocation.getBlockX();
	double z = location.getBlockZ() - startingLocation.getBlockZ();

	if (rotation == 90) {
	    location.setX(-z);
	    location.setZ(x);
	} else if (rotation == 180) {
	    location.setX(-x);
	    location.setZ(-z);
	} else if (rotation == 270) {
	    location.setX(z);
	    location.setZ(-x);
	} else {
	    location.setX(x);
	    location.setZ(z);
	}

	if (mirror == Direction.EAST || mirror == Direction.WEST) {
	    location.setZ(-location.getBlockZ());
	} else if (mirror == Direction.NORTH || mirror == Direction.SOUTH) {
	    location.setX(-location.getBlockX());
	}

	location.add(startingLocation.getBlockX(), 0, startingLocation.getBlockZ());

	return location.getBlock();
    }

    public byte getNewBlockData(Block oldblock) {
	byte data = oldblock.getData();
	Material mat = oldblock.getType();

	if (rotation == 90) {
	    data = CMUtils.rotateData(1, mat, data);
	} else if (rotation == 180) {
	    data = CMUtils.rotateData(2, mat, data);
	} else if (rotation == 270) {
	    data = CMUtils.rotateData(3, mat, data);
	}

	data = CMUtils.mirrorData(mat, data, mirror);

	return data;
    }

    public boolean breakBlock(Block breakblock, Player p) {
	Block block = getNewBlockLocation(breakblock);

	if (p.getGameMode() == GameMode.SURVIVAL) {
	    if (block.getType() != Material.AIR) {
		p.getInventory().addItem(
			new ItemStack(block.getType(), 1, block.getData()));
	    }
	}

	setNewBlock(breakblock, true);

	return true;
    }

    public boolean placeBlock(Block placeblock, Player p) {
	if (p.getGameMode() == GameMode.SURVIVAL) {
	    boolean found = false;

	    for (ItemStack is : p.getInventory().getContents()) {
		if (is != null && is.getData() != null) {
		    if (is.getData().getItemType() == placeblock.getType()
			    && is.getData().getData() == placeblock.getData()) {
			if (is.getAmount() == 1) {
			    p.getInventory().remove(is);
			} else {
			    is.setAmount(is.getAmount() - 1);
			}
			setNewBlock(placeblock, false);
			return true;
		    }
		}
	    }

	    if (!found) {
		return false;
	    }
	}

	setNewBlock(placeblock, false);

	return true;
    }

    public void setItemInHand(ItemStack is) {
	if (npc != null) {
	    if (is.getAmount() == 0) {
		((NPCHuman) npc.getEntity()).getPlayer().setItemInHand(null);
	    } else {
		((NPCHuman) npc.getEntity()).getPlayer().setItemInHand(
			is.clone());
	    }
	}
    }

    public void setSneaking(boolean sneak) {
	if (npc != null) {
	    ((HumanNPC) npc).setSneaking(sneak);
	}
    }

    public void doArmSwing() {
	if (npc != null) {
	    ((HumanNPC) npc).animateArmSwing();
	}
    }

    public Packet20NamedEntitySpawn makeNamedEntitySpawnPacket() {
	return makeNamedEntitySpawnPacket(name);
    }

    // Copied from ModDisguise by desmin88
    // See:
    // https://github.com/desmin88/MobDisguise/blob/master/src/me/desmin88/mobdisguise/utils/PacketUtils.java
    public Packet20NamedEntitySpawn makeNamedEntitySpawnPacket(String Name) {
	Location loc = npc.getLocation();
	Packet20NamedEntitySpawn packet = new Packet20NamedEntitySpawn();
	packet.a = npc.getEntityId();
	packet.b = name; // Set the name of the player to the name they want.
	packet.c = (int) loc.getX();
	packet.c = MathHelper.floor(loc.getX() * 32.0D);
	packet.d = MathHelper.floor(loc.getY() * 32.0D);
	packet.e = MathHelper.floor(loc.getZ() * 32.0D);
	packet.f = (byte) ((int) loc.getYaw() * 256.0F / 360.0F);
	packet.g = (byte) ((int) (loc.getPitch() * 256.0F / 360.0F));
	packet.h = ((NPCHuman) npc.getEntity()).getPlayer().getItemInHand()
		.getTypeId();
	return packet;
    }
}
