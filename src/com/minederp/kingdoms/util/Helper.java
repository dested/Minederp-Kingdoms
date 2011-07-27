package com.minederp.kingdoms.util;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;

public class Helper {
	public static void messagePlayerInList(List<Player> players, String message) {
		for (Player player : players) {
			player.sendMessage(message);
		}
	}

	public static boolean containsPlayers(List<Player> players, Player play) {
		for (Player player : players) {
			if (player.getName().equals(play.getName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean argEquals(String string, String string2) {

		if (string.toLowerCase().equals(string2.toLowerCase()))
			return true;
		StringBuilder sb = new StringBuilder();
		for (char c : string2.toCharArray()) {
			String h = Character.toString(c);
			if (h.toUpperCase().equals(h)) {
				sb.append(h);
			}
		}
		if (sb.toString().toLowerCase().equals(string.toLowerCase()))
			return true;

		return false;

	}

	public static int highestInsdex(double[] distances) {

		double highest = 0;
		int hIndex = 0;

		for (int i = 0; i < distances.length; i++) {
			double d = distances[i];
			if (highest > d) {
				hIndex = i;
				highest = d;
			}
		}
		return hIndex;
	}

	public static int lowestIndex(double[] distances) {

		double lowest = Double.MAX_VALUE;
		int lIndex = 0;

		for (int i = 0; i < distances.length; i++) {
			double d = distances[i];
			if (lowest < d) {
				lIndex = i;
				lowest = d;
			}
		}
		return lIndex;
	}

	public static Tuple2<ItemStack, Integer> firstNonEmpty(Inventory inventory) {
		int index = 0;
		for (ItemStack st : inventory.getContents()) {
			if (st == null) {
				index++;
				continue;
			}
			if (st.getAmount() == 0 || st.getTypeId() == 0) {
				index++;
				continue;
			}
			return new Tuple2<ItemStack, Integer>(st, index);
		}
		return null;
	}

	public static BlockFace faceGetRight(BlockFace facing) {
		switch (facing) {
		case EAST:
			return BlockFace.SOUTH;
		case NORTH:
			return BlockFace.EAST;
		case SOUTH:
			return BlockFace.WEST;
		case WEST:
			return BlockFace.NORTH;

		}
		return null;
	}

	public static BlockFace faceGetLeft(BlockFace facing) {
		switch (facing) {
		case EAST:
			return BlockFace.NORTH;
		case NORTH:
			return BlockFace.WEST;
		case SOUTH:
			return BlockFace.WEST;
		case WEST:
			return BlockFace.SOUTH;

		}
		return null;
	}

	public static BlockFace faceGetOpposite(BlockFace facing) {
		switch (facing) {
		case EAST:
			return BlockFace.WEST;
		case NORTH:
			return BlockFace.SOUTH;
		case SOUTH:
			return BlockFace.NORTH;
		case WEST:
			return BlockFace.EAST;
		case DOWN:
			return BlockFace.UP;
		case UP:
			return BlockFace.DOWN;

		}
		return null;
	}

	public static void drawRectangle(Block relative, Block relative2, Material type) {
		CuboidRegion cb = new CuboidRegion(new Vector(relative.getX(), relative.getY(), relative.getZ()), new Vector(relative2.getX(),
				relative2.getY(), relative2.getZ()));
		World wl = relative.getWorld();

		for (Iterator<com.sk89q.worldedit.BlockVector> iterator = cb.iterator(); iterator.hasNext();) {
			com.sk89q.worldedit.BlockVector tp = (com.sk89q.worldedit.BlockVector) iterator.next();
			wl.getBlockAt(tp.getBlockX(), tp.getBlockY(), tp.getBlockZ()).setType(type);
		}

	}
}
