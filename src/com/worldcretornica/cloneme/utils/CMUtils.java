package com.worldcretornica.cloneme.utils;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import com.worldcretornica.cloneme.Clone.Direction;

public class CMUtils {

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger("Minecraft");
	
	public static BlockFace getNewRotatedFace(BlockFace face, int rotation) {
		BlockFace newface = face;

		for (int ctr = 0; ctr < rotation; ctr++) {
			switch (newface) {
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
	
	public static BlockFace getNewMirrorFace(BlockFace face, Direction dir) {
		BlockFace newface = face;
		
		if(dir == Direction.NONE) return newface;

		switch (newface) 
		{
		case EAST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.WEST;
			}
			break;
		case EAST_NORTH_EAST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.WEST_NORTH_WEST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.EAST_SOUTH_EAST;
			}
			break;
		case EAST_SOUTH_EAST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.WEST_SOUTH_WEST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.EAST_NORTH_EAST;
			}
			break;
		case SOUTH:
			if( dir == Direction.NORTH)
			{
				newface = BlockFace.NORTH;
			}
			break;
		case SOUTH_EAST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.SOUTH_WEST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.NORTH_EAST;
			}
			break;
		case SOUTH_WEST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.SOUTH_EAST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.NORTH_WEST;
			}
			break;
		case SOUTH_SOUTH_EAST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.SOUTH_SOUTH_WEST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.NORTH_NORTH_EAST;
			}
			break;
		case SOUTH_SOUTH_WEST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.SOUTH_SOUTH_EAST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.NORTH_NORTH_WEST;
			}
			break;
		case WEST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.EAST;
			}
			break;
		case WEST_NORTH_WEST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.EAST_NORTH_EAST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.WEST_SOUTH_WEST;
			}
			break;
		case WEST_SOUTH_WEST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.EAST_SOUTH_EAST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.WEST_NORTH_WEST;
			}
			break;
		case NORTH:
			if( dir == Direction.NORTH)
			{
				newface = BlockFace.SOUTH;
			}
			break;
		case NORTH_EAST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.NORTH_WEST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.SOUTH_EAST;
			}
			break;
		case NORTH_NORTH_EAST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.NORTH_NORTH_WEST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.SOUTH_SOUTH_EAST;
			}
			break;
		case NORTH_NORTH_WEST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.NORTH_NORTH_EAST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.SOUTH_SOUTH_WEST;
			}
			break;
		case NORTH_WEST:
			if( dir == Direction.EAST)
			{
				newface = BlockFace.NORTH_EAST;
			}
			else if(dir == Direction.NORTH)
			{
				newface = BlockFace.SOUTH_WEST;
			}
			break;
		}

		return newface;
	}

	
	public static byte rotateDoorData(int rotation, byte lowerdata, byte upperdata, boolean upper) {
		for (int ctr = 0; ctr < rotation; ctr++) {
			switch (lowerdata) {
			case 0:
				lowerdata = 1;
				break;
			case 1:
				lowerdata = 2;
				break;
			case 2:
				lowerdata = 3;
				/*if (upperdata == 8)
				{
					upperdata = 9;
				}else{
					upperdata = 8;
				}*/
				break;
			case 3:
				lowerdata = 0;
				break;
			}
		}
		
		if(upper)
			return upperdata;
		else
			return lowerdata;
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
			case BIRCH_WOOD_STAIRS:
			case SANDSTONE_STAIRS:
			case SPRUCE_WOOD_STAIRS:
			case JUNGLE_WOOD_STAIRS:
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
				case 4:
					data = 6;
					break;
				case 5:
					data = 7;
					break;
				case 6:
					data = 5;
					break;
				case 7:
					data = 4;
					break;
				}
				break;
			case ANVIL:
				int anvilrotate = data % 2;
				
				if(anvilrotate == 0)
				{
					data += 1;
				}else{
					data -= 1;
				}
				break;
			case LOG:
				if (data >= 4 && data <= 7)
				{
					data += 4;
				}else if (data >= 8 && data <= 11)
				{
					data -= 4;
				}
				break;
			case LADDER:
			case WALL_SIGN:
			case CHEST:
			case FURNACE:
			case BURNING_FURNACE:
			case DISPENSER:
			case ENDER_CHEST:
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
			case WOOD_BUTTON:
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
			case IRON_DOOR_BLOCK:
			case TRIPWIRE_HOOK:
			case COCOA:
				
				int extra = data & ~0x3;
	            int withoutFlags = data & 0x3;
	            
	            switch (withoutFlags) {
		            case 0: data = (byte) (1 | extra); break;
		            case 1: data = (byte) (2 | extra); break;
		            case 2: data = (byte) (3 | extra); break;
		            case 3: data = (byte) (0 | extra); break;
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
				
			case BED_BLOCK:
				if(data == 0)
					data = 7;
				else if(data < 6)
					data -= 1;
				else if (data == 8)
					data = 15;
				else if (data < 16)
					data -= 1;
				
				break;
			}
		}

		return data;
	}

	public static byte mirrorDoorData(byte lowerdata, byte upperdata, Direction mirror, boolean upper) 
	{
		switch (lowerdata) 
		{
		case 0:
			if (mirror == Direction.EAST)
				if(upperdata == 8)
				{
					lowerdata = (byte) 2;
					upperdata = (byte) 9;
				}else{
					lowerdata = (byte) 2;
					upperdata = (byte) 8;
				}
			if (mirror == Direction.NORTH)
				if(upperdata == 8)
				{
					upperdata = (byte) 9;
				}else{
					upperdata = (byte) 8;
				}
			break;
		case 1:
			if (mirror == Direction.NORTH)
				if(upperdata == 8)
				{
					lowerdata = (byte) 3;
					upperdata = (byte) 9;
				}
				else
				{
					lowerdata = (byte) 3;
					upperdata = (byte) 8;
				}
			if (mirror == Direction.EAST)
				if(upperdata == 8)
				{
					upperdata = (byte) 9;
				}else{
					upperdata = (byte) 8;
				}
			break;
		case 2:
			if (mirror == Direction.EAST)
				if(upperdata == 8)
				{
					lowerdata = (byte) 0;
					upperdata = (byte) 9;
				}else{
					lowerdata = (byte) 0;
					upperdata = (byte) 8;
				}
			if (mirror == Direction.NORTH)
				if(upperdata == 8)
				{
					upperdata = (byte) 9;
				}else{
					upperdata = (byte) 8;
				}
			break;
		case 3:
			if (mirror == Direction.NORTH)
				if(upperdata == 8)
				{
					lowerdata = (byte) 1;
					upperdata = (byte) 9;
				}
				else
				{
					lowerdata = (byte) 1;
					upperdata = (byte) 8;
				}
			if (mirror == Direction.EAST)
				if(upperdata == 8)
				{
					upperdata = (byte) 9;
				}else{
					upperdata = (byte) 8;
				}
			break;
		}
		
		if(upper)
			return upperdata;
		else
			return lowerdata;
	}
	
	public static byte mirrorData(Material mat, byte data, Direction mirror) {
		// Some of this code is inspired by worldedit's source
		// https://github.com/sk89q/worldedit/blob/master/src/main/java/com/sk89q/worldedit/blocks/BlockData.java
		
		if(mirror == Direction.NONE) return data;
		
		switch (mat) {
		case BRICK_STAIRS:
		case WOOD_STAIRS:
		case COBBLESTONE_STAIRS:
		case NETHER_BRICK_STAIRS:
		case SMOOTH_STAIRS:
		case BIRCH_WOOD_STAIRS:
		case SPRUCE_WOOD_STAIRS:
		case SANDSTONE_STAIRS:
		case JUNGLE_WOOD_STAIRS:
			switch (data) {
			case 0:
				if (mirror == Direction.EAST)
					data = 1;
				break;
			case 1:
				if (mirror == Direction.EAST)
					data = 0;
				break;
			case 2:
				if (mirror == Direction.NORTH)
					data = 3;
				break;
			case 3:
				if (mirror == Direction.NORTH)
					data = 2;
				break;
			}
			break;
		case LADDER:
		case WALL_SIGN:
		case CHEST:
		case FURNACE:
		case BURNING_FURNACE:
		case ENDER_CHEST:
		case DISPENSER:
			switch (data) {
			case 2:
				if (mirror == Direction.NORTH)
					data = 3;
				break;
			case 3:
				if (mirror == Direction.NORTH)
					data = 2;
				break;
			case 4:
				if (mirror == Direction.EAST)
					data = 5;
				break;
			case 5:
				if (mirror == Direction.EAST)
					data = 4;
				break;
			}
			break;
		case TORCH:
		case REDSTONE_TORCH_ON:
		case REDSTONE_TORCH_OFF:
			switch (data) {
			case 1:
				if (mirror == Direction.NORTH)
					data = 2;
				break;
			case 2:
				if (mirror == Direction.NORTH)
					data = 1;
				break;
			case 3:
				if (mirror == Direction.EAST)
					data = 4;
				break;
			case 4:
				if (mirror == Direction.EAST)
					data = 3;
				break;
			}
			break;
		case RAILS:
			switch (data) {
			case 6:
				if (mirror == Direction.NORTH)
					data = 8;
				break;
			case 8:
				if (mirror == Direction.NORTH)
					data = 6;
				break;
			case 7:
				if (mirror == Direction.EAST)
					data = 9;
				break;
			case 9:
				if (mirror == Direction.EAST)
					data = 7;
				break;
			}
			// rails continue below
		case POWERED_RAIL:
		case DETECTOR_RAIL:
			switch (data & 0x7) {
			case 0:
				if (mirror == Direction.NORTH)
					data = (byte) (1 | (data & ~0x7));
				break;
			case 1:
				if (mirror == Direction.NORTH)
					data = (byte) (0 | (data & ~0x7));
				break;
			case 2:
				if (mirror == Direction.NORTH)
					data = (byte) (3 | (data & ~0x7));
				break;
			case 3:
				if (mirror == Direction.NORTH)
					data = (byte) (2 | (data & ~0x7));
				break;
			case 4:
				if (mirror == Direction.EAST)
					data = (byte) (5 | (data & ~0x7));
				break;
			case 5:
				if (mirror == Direction.EAST)
					data = (byte) (4 | (data & ~0x7));
				break;
			}
			break;
		case LEVER:
			int lthrown = data & 0x8;
			int lwithoutthrown = data & ~0x8;
			switch (lwithoutthrown) {
			case 1:
				if (mirror == Direction.EAST)
					data = (byte) (2 | lthrown);
				break;
			case 2:
				if (mirror == Direction.EAST)
					data = (byte) (1 | lthrown);
				break;
			case 3:
				if (mirror == Direction.NORTH)
					data = (byte) (4 | lthrown);
				break;
			case 4:
				if (mirror == Direction.NORTH)
					data = (byte) (3 | lthrown);
				break;
			case 5:
				if (mirror == Direction.EAST)
					data = (byte) (5 | lthrown);
				break;
			case 6:
				if (mirror == Direction.NORTH)
					data = (byte) (6 | lthrown);
				break;
			}
			break;
		case STONE_BUTTON:
		case WOOD_BUTTON:
			int thrown = data & 0x8;
			int withoutthrown = data & ~0x8;
			switch (withoutthrown) {
			case 1:
				if (mirror == Direction.EAST)
					data = (byte) (2 | thrown);
				break;
			case 2:
				if (mirror == Direction.EAST)
					data = (byte) (1 | thrown);
				break;
			case 3:
				if (mirror == Direction.NORTH)
					data = (byte) (4 | thrown);
				break;
			case 4:
				if (mirror == Direction.NORTH)
					data = (byte) (3 | thrown);
				break;
			}
			break;
		case WOODEN_DOOR:
		case IRON_DOOR_BLOCK:
		case COCOA:
		case TRIPWIRE_HOOK:
			switch (data) {
			case 0:
				if (mirror == Direction.EAST)
					data = (byte) (5 );
				if (mirror == Direction.NORTH)
					data = (byte) (7 );
				break;
			case 1:
				if (mirror == Direction.NORTH)
					data = (byte) (6 );
				if (mirror == Direction.EAST)
					data = (byte) (4 );
				break;
			case 2:
				if (mirror == Direction.EAST)
					data = (byte) (7 );
				if (mirror == Direction.NORTH)
					data = (byte) (5 );
				break;
			case 3:
				if (mirror == Direction.NORTH)
					data = (byte) (4 );
				if (mirror == Direction.EAST)
					data = (byte) (6 );
				break;
			case 4:
				if (mirror == Direction.EAST)
					data = (byte) (1 );
				if (mirror == Direction.NORTH)
					data = (byte) (3 );
				break;
			case 5:
				if (mirror == Direction.EAST)
					data = (byte) (0);
				if (mirror == Direction.NORTH)
					data = (byte) (2 );
				break;
			case 6:
				if (mirror == Direction.NORTH)
					data = (byte) (1);
				if (mirror == Direction.EAST)
					data = (byte) (3 );
				break;
			case 7:
				if (mirror == Direction.EAST)
					data = (byte) (2 );
				if (mirror == Direction.NORTH)
					data = (byte) (0);
				break;
			}
			break;
		case SIGN_POST:
			if (mirror == Direction.EAST)
				data = (byte) ((16 - data) & 0xf);
			else if (mirror == Direction.NORTH)
				data = (byte) ((8 - data) & 0xf);
			break;
		case PUMPKIN:
		case JACK_O_LANTERN:
			switch (data) {
			case 0:
				if (mirror == Direction.NORTH)
					data = 2;
				break;
			case 1:
				if (mirror == Direction.EAST)
					data = 3;
				break;
			case 2:
				if (mirror == Direction.NORTH)
				data = 0;
				break;
			case 3:
				if (mirror == Direction.EAST)
					data = 1;
				break;
			}
			break;
		case DIODE_BLOCK_ON:
		case DIODE_BLOCK_OFF:
			int dir = data & 0x03;
			int delay = data - dir;
			switch (dir) {
			case 0:
				if (mirror == Direction.NORTH)
					data = (byte) (2 | delay);
				break;
			case 1:
				if (mirror == Direction.EAST)
					data = (byte) (3 | delay);
				break;
			case 2:
				if (mirror == Direction.NORTH)
					data = (byte) (0 | delay);
				break;
			case 3:
				if (mirror == Direction.EAST)
					data = (byte) (1 | delay);
				break;
			}
			break;
		case TRAP_DOOR:
			int withoutorientation = data & ~0x3;
			int orientation = data & 0x3;
			switch (orientation) {
			case 0:
				if (mirror == Direction.NORTH)
					data = (byte) (1 | withoutorientation);
				break;
			case 1:
				if (mirror == Direction.NORTH)
					data = (byte) (0 | withoutorientation);
				break;
			case 2:
				if (mirror == Direction.EAST)
					data = (byte) (3 | withoutorientation);
				break;
			case 3:
				if (mirror == Direction.EAST)
					data = (byte) (2 | withoutorientation);
				break;
			}
			break;
		case PISTON_BASE:
		case PISTON_STICKY_BASE:
		case PISTON_EXTENSION:
			final int rest = data & ~0x7;
			switch (data & 0x7) {
			case 2:
				if (mirror == Direction.NORTH)
					data = (byte) (3 | rest);
				break;
			case 3:
				if (mirror == Direction.NORTH)
					data = (byte) (2 | rest);
				break;
			case 4:
				if (mirror == Direction.EAST)
					data = (byte) (5 | rest);
				break;
			case 5:
				if (mirror == Direction.EAST)
					data = (byte) (4 | rest);
				break;
			}
			break;
		case BROWN_MUSHROOM:
		case RED_MUSHROOM:
			switch (data) {
			case 1:
				if (mirror == Direction.EAST)
					data = 3;
				if (mirror == Direction.NORTH)
					data = 7;
				break;
			case 2:
				if (mirror == Direction.NORTH)
					data = 8;
				break;
			case 3:
				if (mirror == Direction.EAST)
					data = 1;
				if (mirror == Direction.NORTH)
					data = 9;
				break;
			case 4:
				if (mirror == Direction.EAST)
					data = 6;
				break;
			case 6:
				if (mirror == Direction.EAST)
					data = 4;
				break;
			case 7:
				if (mirror == Direction.NORTH)
					data = 1;
				if (mirror == Direction.EAST)
					data = 9;
				break;
			case 8:
				if (mirror == Direction.NORTH)
					data = 2;
				break;
			case 9:
				if (mirror == Direction.NORTH)
					data = 3;
				if (mirror == Direction.EAST)
					data = 7;
				break;
			}
			break;
		case VINE:
			switch (data) {
			case 1:
				if (mirror == Direction.NORTH)
					data = 4;
				break;
			case 2:
				if (mirror == Direction.EAST)
					data = 8;
				break;
			case 3:
				if (mirror == Direction.NORTH)
					data = 6;
				if (mirror == Direction.EAST)
					data = 9;
				break;
			case 4:
				if (mirror == Direction.NORTH)
					data = 1;
				break;
			case 6:
				if (mirror == Direction.EAST)
					data = 3;
				if (mirror == Direction.NORTH)
					data = 12;
				break;
			case 7:
				if (mirror == Direction.EAST)
					data = 13;
				break;
			case 8:
				if (mirror == Direction.EAST)
					data = 2;
				break;
			case 9:
				if (mirror == Direction.NORTH)
					data = 12;
				if (mirror == Direction.EAST)
					data = 3;
				break;
			case 11:
				if (mirror == Direction.NORTH)
					data = 14;
				break;
			case 12:
				if (mirror == Direction.EAST)
					data = 6;
				if (mirror == Direction.NORTH)
					data = 9;
				break;
			case 13:
				if (mirror == Direction.EAST)
					data = 7;
				break;
			case 14:
				if (mirror == Direction.NORTH)
					data = 11;
				break;
			}
			break;
		case FENCE_GATE:
			switch (data) {
			case 4:
				if (mirror == Direction.EAST)
					data = 6;
				break;
			case 5:
				if (mirror == Direction.NORTH)
					data = 7;
				break;
			case 6:
				if (mirror == Direction.EAST)
					data = 4;
				break;
			case 7:
				if (mirror == Direction.NORTH)
					data = 5;
				break;
			}

			//data = (byte) (((data + 1) & 0x3) | (data & ~0x3));
			break;
			
		case BED_BLOCK:
			switch (data) {
			case 0:	case 4:
				if (mirror == Direction.NORTH)
					data = 2; break;
			case 1:	case 5:
				if (mirror == Direction.EAST)
					data = 3; break;
			case 2:	case 6:
				if (mirror == Direction.NORTH)
					data = 0; break;
			case 3:	case 7:
				if (mirror == Direction.EAST)
					data = 1; break;
			case 8:	case 12:
				if (mirror == Direction.NORTH)
					data = 10; break;
			case 9:	case 13:
				if (mirror == Direction.EAST)
					data = 11; break;
			case 10: case 14:
				if (mirror == Direction.NORTH)
					data = 8; break;
			case 11: case 15:
				if (mirror == Direction.EAST)
					data = 9; break;
			}
			
			break;
		}

		return data;
	}
}
