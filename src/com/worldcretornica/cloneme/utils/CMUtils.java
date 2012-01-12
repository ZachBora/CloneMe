package com.worldcretornica.cloneme.utils;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import com.worldcretornica.cloneme.Clone.Direction;

public class CMUtils {

	private CMUtils() {
	}

	public static BlockFace getNewFace(BlockFace face, int rotation) {
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

	public static byte rotateData(int rotation, Material mat, byte data) {
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

	public static byte mirrorData(Material mat, byte data, Direction mirror) {
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
			// TODO
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
}
