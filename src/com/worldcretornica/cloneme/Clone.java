package com.worldcretornica.cloneme;

import java.util.logging.Logger;

import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet20NamedEntitySpawn;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tk.npccreatures.npcs.NPCManager;
import tk.npccreatures.npcs.entity.HumanNPC;
import tk.npccreatures.npcs.entity.NPC;
import tk.npccreatures.npcs.nms.NPCHuman;
import tk.npccreatures.npcs.NPCType;

public class Clone {

    public final Logger logger = Logger.getLogger("Minecraft");

    public enum Direction {
	NONE, EAST, WEST, SOUTH, NORTH,
	/*
	 * northeast, northwest, southeast, southwest
	 */
    }

    public String owner;
    public long xtrans;
    public long ytrans;
    public long ztrans;
    public int rotation;
    public Direction mirror;
    public double xstart;
    public double ystart;
    public double zstart;
    public int xbstart;
    public int ybstart;
    public int zbstart;
    public NPC npc;
    public String name;

    /*
     * public Clone() { owner = ""; xtrans = 0; ytrans = 0; ztrans = 0; rotation
     * = 0; mirror = Direction.NONE; xstart = 0; ystart = 0; zstart = 0; xbstart
     * = 0; ybstart = 0; zbstart = 0; npc = null; name = ""; // SHOULDN'T BE
     * USED }
     */

    public Clone(String owner, long xtrans, long ytrans, long ztrans,
	    int rotation, Direction mirror, double xstart, double ystart,
	    double zstart, int xbstart, int ybstart, int zbstart, World world,
	    NPCManager manager, String name) {
	this.owner = owner;
	this.xtrans = xtrans;
	this.ytrans = ytrans;
	this.ztrans = ztrans;
	this.rotation = rotation;
	if (mirror == Direction.WEST) {
	    this.mirror = Direction.EAST;
	} else if (mirror == Direction.SOUTH) {
	    this.mirror = Direction.NORTH;
	} else {
	    this.mirror = mirror;
	}
	this.xstart = xstart;
	this.ystart = ystart;
	this.zstart = zstart;
	this.xbstart = xbstart;
	this.ybstart = ybstart;
	this.zbstart = zbstart;
	this.name = name;

	if (manager != null) {
	    npc = manager.spawnNPC(owner, getNewLocation(new Location(world,
		    xstart, ystart, zstart)), NPCType.HUMAN);

	    ((NPCHuman) npc.getEntity()).name = name;
	}
    }

    public BlockFace getNewFace(BlockFace face) {
	BlockFace newface = face;

	for (int ctr = 0; ctr < rotation; ctr++) {
	    switch (face) {
	    case EAST:
		newface = BlockFace.SOUTH;
		break;
	    case EAST_NORTH_EAST:
		newface = BlockFace.SOUTH_SOUTH_EAST;
		break;
	    case EAST_SOUTH_EAST:
		newface = BlockFace.SOUTH_SOUTH_WEST;
		break;
	    case SOUTH:
		newface = BlockFace.WEST;
		break;
	    case SOUTH_EAST:
		newface = BlockFace.SOUTH_WEST;
		break;
	    case SOUTH_WEST:
		newface = BlockFace.NORTH_WEST;
		break;
	    case SOUTH_SOUTH_EAST:
		newface = BlockFace.WEST_SOUTH_WEST;
		break;
	    case SOUTH_SOUTH_WEST:
		newface = BlockFace.WEST_NORTH_WEST;
		break;
	    case WEST:
		newface = BlockFace.NORTH;
		break;
	    case WEST_NORTH_WEST:
		newface = BlockFace.NORTH_NORTH_EAST;
		break;
	    case WEST_SOUTH_WEST:
		newface = BlockFace.NORTH_NORTH_WEST;
		break;
	    case NORTH:
		newface = BlockFace.EAST;
		break;
	    case NORTH_EAST:
		newface = BlockFace.SOUTH_EAST;
		break;
	    case NORTH_NORTH_EAST:
		newface = BlockFace.EAST_SOUTH_EAST;
		break;
	    case NORTH_NORTH_WEST:
		newface = BlockFace.EAST_SOUTH_EAST;
		break;
	    case NORTH_WEST:
		newface = BlockFace.NORTH_EAST;
		break;
	    }
	}

	return newface;
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
	xstart = 0;
	ystart = 0;
	zstart = 0;
	xbstart = 0;
	ybstart = 0;
	zbstart = 0;
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

	double x = location.getX() - xstart;
	double z = location.getZ() - zstart;

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

	location.add(xstart, 0, zstart);

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

	double x = location.getBlockX() - xbstart;
	double z = location.getBlockZ() - zbstart;

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

	location.add(xbstart, 0, zbstart);

	return location.getBlock();
    }

    public byte getNewBlockData(Block oldblock) {
	byte data = oldblock.getData();
	Material mat = oldblock.getType();

	if (rotation == 90) {
	    data = rotateData(1, mat, data);
	} else if (rotation == 180) {
	    data = rotateData(2, mat, data);
	} else if (rotation == 270) {
	    data = rotateData(3, mat, data);
	}

	data = mirrorData(mat, data);

	return data;
    }

    public byte rotateData(int rotation, Material mat, byte data) {
	// Some of this code is inspired by worldedit's source
	// https://github.com/sk89q/worldedit/blob/master/src/main/java/com/sk89q/worldedit/blocks/BlockData.java
	for (int ctr = 0; ctr < rotation; ctr++) {
	    switch (mat) {
	    case BRICK_STAIRS:
	    case WOOD_STAIRS:
	    case COBBLESTONE_STAIRS:
	    case NETHER_BRICK_STAIRS:
	    case SMOOTH_STAIRS:
		switch (data) {
		case 0:
		    data = 2;
		    break;
		case 1:
		    data = 3;
		    break;
		case 2:
		    data = 1;
		    break;
		case 3:
		    data = 0;
		    break;
		}
		break;
	    case LADDER:
	    case WALL_SIGN:
	    case CHEST:
	    case FURNACE:
	    case BURNING_FURNACE:
	    case DISPENSER:
		switch (data) {
		case 2:
		    data = 5;
		    break;
		case 3:
		    data = 4;
		    break;
		case 4:
		    data = 2;
		    break;
		case 5:
		    data = 3;
		    break;
		}
		break;
	    case TORCH:
	    case REDSTONE_TORCH_ON:
	    case REDSTONE_TORCH_OFF:
		switch (data) {
		case 1:
		    data = 3;
		    break;
		case 2:
		    data = 4;
		    break;
		case 3:
		    data = 2;
		    break;
		case 4:
		    data = 1;
		    break;
		}
		break;
	    case RAILS:
		switch (data) {
		case 6:
		    data = 7;
		    break;
		case 7:
		    data = 8;
		    break;
		case 8:
		    data = 9;
		    break;
		case 9:
		    data = 6;
		    break;
		}
		// rails continue below
	    case POWERED_RAIL:
	    case DETECTOR_RAIL:
		switch (data & 0x7) {
		case 0:
		    data = (byte) (1 | (data & ~0x7));
		    break;
		case 1:
		    data = (byte) (0 | (data & ~0x7));
		    break;
		case 2:
		    data = (byte) (5 | (data & ~0x7));
		    break;
		case 3:
		    data = (byte) (4 | (data & ~0x7));
		    break;
		case 4:
		    data = (byte) (2 | (data & ~0x7));
		    break;
		case 5:
		    data = (byte) (3 | (data & ~0x7));
		    break;
		}
		break;
	    case LEVER:
		int lthrown = data & 0x8;
		int lwithoutthrown = data & ~0x8;
		switch (lwithoutthrown) {
		case 1:
		    data = (byte) (3 | lthrown);
		    break;
		case 2:
		    data = (byte) (4 | lthrown);
		    break;
		case 3:
		    data = (byte) (2 | lthrown);
		    break;
		case 4:
		    data = (byte) (1 | lthrown);
		    break;
		case 5:
		    data = (byte) (6 | lthrown);
		    break;
		case 6:
		    data = (byte) (5 | lthrown);
		    break;
		}
		break;
	    case STONE_BUTTON:
		int thrown = data & 0x8;
		int withoutthrown = data & ~0x8;
		switch (withoutthrown) {
		case 1:
		    data = (byte) (3 | thrown);
		    break;
		case 2:
		    data = (byte) (4 | thrown);
		    break;
		case 3:
		    data = (byte) (2 | thrown);
		    break;
		case 4:
		    data = (byte) (1 | thrown);
		    break;
		}
		break;
	    case WOODEN_DOOR:
	    case IRON_DOOR:
		int tophalf = data & 0x8;
		int swung = data & 0x4;
		int withoutflags = data & ~(0x8 | 0x4);
		switch (withoutflags) {
		case 0:
		    data = (byte) (1 | tophalf | swung);
		    break;
		case 1:
		    data = (byte) (2 | tophalf | swung);
		    break;
		case 2:
		    data = (byte) (3 | tophalf | swung);
		    break;
		case 3:
		    data = (byte) (0 | tophalf | swung);
		    break;
		}
		break;
	    case SIGN_POST:
		data = (byte) ((data + 4) % 16);
		break;
	    case PUMPKIN:
	    case JACK_O_LANTERN:
		switch (data) {
		case 0:
		    data = 1;
		    break;
		case 1:
		    data = 2;
		    break;
		case 2:
		    data = 3;
		    break;
		case 3:
		    data = 0;
		    break;
		}
		break;
	    case DIODE_BLOCK_ON:
	    case DIODE_BLOCK_OFF:
		int dir = data & 0x03;
		int delay = data - dir;
		switch (dir) {
		case 0:
		    data = (byte) (1 | delay);
		    break;
		case 1:
		    data = (byte) (2 | delay);
		    break;
		case 2:
		    data = (byte) (3 | delay);
		    break;
		case 3:
		    data = (byte) (0 | delay);
		    break;
		}
		break;
	    case TRAP_DOOR:
		int withoutorientation = data & ~0x3;
		int orientation = data & 0x3;
		switch (orientation) {
		case 0:
		    data = (byte) (3 | withoutorientation);
		    break;
		case 1:
		    data = (byte) (2 | withoutorientation);
		    break;
		case 2:
		    data = (byte) (0 | withoutorientation);
		    break;
		case 3:
		    data = (byte) (1 | withoutorientation);
		    break;
		}
		break;
	    case PISTON_BASE:
	    case PISTON_STICKY_BASE:
	    case PISTON_EXTENSION:
		final int rest = data & ~0x7;
		switch (data & 0x7) {
		case 2:
		    data = (byte) (5 | rest);
		    break;
		case 3:
		    data = (byte) (4 | rest);
		    break;
		case 4:
		    data = (byte) (2 | rest);
		    break;
		case 5:
		    data = (byte) (3 | rest);
		    break;
		}
		break;
	    case BROWN_MUSHROOM:
	    case RED_MUSHROOM:
		if (data < 10)
		    data = (byte) ((data * 3) % 10);
		break;
	    case VINE:
		data = (byte) (((data << 1) | (data >> 3)) & 0xf);
		break;
	    case FENCE_GATE:
		switch (data) {
		case 0:
		    data = 3;
		    break;
		case 1:
		    data = 2;
		    break;
		case 2:
		    data = 0;
		    break;
		case 3:
		    data = 1;
		    break;
		case 4:
		    data = 5;
		    break;
		case 5:
		    data = 6;
		    break;
		case 6:
		    data = 7;
		    break;
		case 7:
		    data = 4;
		    break;
		}

		data = (byte) (((data + 1) & 0x3) | (data & ~0x3));
		break;
	    }
	}

	return data;
    }

    public byte mirrorData(Material mat, byte data) {
	// Some of this code is inspired by worldedit's source
	// https://github.com/sk89q/worldedit/blob/master/src/main/java/com/sk89q/worldedit/blocks/BlockData.java
	switch (mat) {
	case BRICK_STAIRS:
	case WOOD_STAIRS:
	case COBBLESTONE_STAIRS:
	case NETHER_BRICK_STAIRS:
	case SMOOTH_STAIRS:
	    switch (data) {
	    case 0:
		if (mirror == Direction.NORTH)
		    data = 1;
		break;
	    case 1:
		if (mirror == Direction.NORTH)
		    data = 0;
		break;
	    case 2:
		if (mirror == Direction.EAST)
		    data = 3;
		break;
	    case 3:
		if (mirror == Direction.EAST)
		    data = 2;
		break;
	    }
	    break;
	case LADDER:
	case WALL_SIGN:
	case CHEST:
	case FURNACE:
	case BURNING_FURNACE:
	case DISPENSER:
	    switch (data) {
	    case 2:
		if (mirror == Direction.EAST)
		    data = 3;
		break;
	    case 3:
		if (mirror == Direction.EAST)
		    data = 2;
		break;
	    case 4:
		if (mirror == Direction.NORTH)
		    data = 5;
		break;
	    case 5:
		if (mirror == Direction.NORTH)
		    data = 4;
		break;
	    }
	    break;
	case TORCH:
	case REDSTONE_TORCH_ON:
	case REDSTONE_TORCH_OFF:
	    switch (data) {
	    case 1:
		if (mirror == Direction.EAST)
		    data = 2;
		break;
	    case 2:
		if (mirror == Direction.EAST)
		    data = 1;
		break;
	    case 3:
		if (mirror == Direction.NORTH)
		    data = 4;
		break;
	    case 4:
		if (mirror == Direction.NORTH)
		    data = 3;
		break;
	    }
	    break;
	case RAILS:
	    switch (data) {
	    case 6:
		if (mirror == Direction.EAST)
		    data = 8;
		break;
	    case 8:
		if (mirror == Direction.EAST)
		    data = 6;
		break;
	    case 7:
		if (mirror == Direction.NORTH)
		    data = 9;
		break;
	    case 9:
		if (mirror == Direction.NORTH)
		    data = 7;
		break;
	    }
	    // rails continue below
	case POWERED_RAIL:
	case DETECTOR_RAIL:
	    switch (data & 0x7) {
	    case 0:
		if (mirror == Direction.EAST)
		    data = (byte) (1 | (data & ~0x7));
		break;
	    case 1:
		if (mirror == Direction.EAST)
		    data = (byte) (0 | (data & ~0x7));
		break;
	    case 2:
		if (mirror == Direction.EAST)
		    data = (byte) (3 | (data & ~0x7));
		break;
	    case 3:
		if (mirror == Direction.EAST)
		    data = (byte) (2 | (data & ~0x7));
		break;
	    case 4:
		if (mirror == Direction.NORTH)
		    data = (byte) (5 | (data & ~0x7));
		break;
	    case 5:
		if (mirror == Direction.NORTH)
		    data = (byte) (4 | (data & ~0x7));
		break;
	    }
	    break;
	case LEVER:
	    int lthrown = data & 0x8;
	    int lwithoutthrown = data & ~0x8;
	    switch (lwithoutthrown) {
	    case 1:
		if (mirror == Direction.NORTH)
		    data = (byte) (2 | lthrown);
		break;
	    case 2:
		if (mirror == Direction.NORTH)
		    data = (byte) (1 | lthrown);
		break;
	    case 3:
		if (mirror == Direction.EAST)
		    data = (byte) (4 | lthrown);
		break;
	    case 4:
		if (mirror == Direction.EAST)
		    data = (byte) (3 | lthrown);
		break;
	    case 5:
		if (mirror == Direction.NORTH)
		    data = (byte) (5 | lthrown);
		break;
	    case 6:
		if (mirror == Direction.EAST)
		    data = (byte) (6 | lthrown);
		break;
	    }
	    break;
	case STONE_BUTTON:
	    int thrown = data & 0x8;
	    int withoutthrown = data & ~0x8;
	    switch (withoutthrown) {
	    case 1:
		data = (byte) (3 | thrown);
		break;
	    case 2:
		data = (byte) (4 | thrown);
		break;
	    case 3:
		data = (byte) (2 | thrown);
		break;
	    case 4:
		data = (byte) (1 | thrown);
		break;
	    }
	    break;
	case WOODEN_DOOR:
	case IRON_DOOR:
	    int tophalf = data & 0x8;
	    int swung = data & 0x4;
	    int withoutflags = data & ~(0x8 | 0x4);
	    switch (withoutflags) {
	    case 0:
		data = (byte) (1 | tophalf | swung);
		break;
	    case 1:
		data = (byte) (2 | tophalf | swung);
		break;
	    case 2:
		data = (byte) (3 | tophalf | swung);
		break;
	    case 3:
		data = (byte) (0 | tophalf | swung);
		break;
	    }
	    break;
	case SIGN_POST:
	    data = (byte) ((data + 4) % 16);
	    break;
	case PUMPKIN:
	case JACK_O_LANTERN:
	    switch (data) {
	    case 0:
		data = 1;
		break;
	    case 1:
		data = 2;
		break;
	    case 2:
		data = 3;
		break;
	    case 3:
		data = 0;
		break;
	    }
	    break;
	case DIODE_BLOCK_ON:
	case DIODE_BLOCK_OFF:
	    int dir = data & 0x03;
	    int delay = data - dir;
	    switch (dir) {
	    case 0:
		data = (byte) (1 | delay);
		break;
	    case 1:
		data = (byte) (2 | delay);
		break;
	    case 2:
		data = (byte) (3 | delay);
		break;
	    case 3:
		data = (byte) (0 | delay);
		break;
	    }
	    break;
	case TRAP_DOOR:
	    int withoutorientation = data & ~0x3;
	    int orientation = data & 0x3;
	    switch (orientation) {
	    case 0:
		data = (byte) (3 | withoutorientation);
		break;
	    case 1:
		data = (byte) (2 | withoutorientation);
		break;
	    case 2:
		data = (byte) (0 | withoutorientation);
		break;
	    case 3:
		data = (byte) (1 | withoutorientation);
		break;
	    }
	    break;
	case PISTON_BASE:
	case PISTON_STICKY_BASE:
	case PISTON_EXTENSION:
	    final int rest = data & ~0x7;
	    switch (data & 0x7) {
	    case 2:
		data = (byte) (5 | rest);
		break;
	    case 3:
		data = (byte) (4 | rest);
		break;
	    case 4:
		data = (byte) (2 | rest);
		break;
	    case 5:
		data = (byte) (3 | rest);
		break;
	    }
	    break;
	case BROWN_MUSHROOM:
	case RED_MUSHROOM:
	    if (data < 10)
		data = (byte) ((data * 3) % 10);
	    break;
	case VINE:
	    data = (byte) (((data << 1) | (data >> 3)) & 0xf);
	    break;
	case FENCE_GATE:
	    switch (data) {
	    case 0:
		data = 3;
		break;
	    case 1:
		data = 2;
		break;
	    case 2:
		data = 0;
		break;
	    case 3:
		data = 1;
		break;
	    case 4:
		data = 5;
		break;
	    case 5:
		data = 6;
		break;
	    case 6:
		data = 7;
		break;
	    case 7:
		data = 4;
		break;
	    }

	    data = (byte) (((data + 1) & 0x3) | (data & ~0x3));
	    break;
	}

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

    public void setIteMinHand(ItemStack is) {
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
