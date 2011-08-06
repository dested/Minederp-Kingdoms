package com.minederp.kingdoms.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.minederp.kingdoms.games.GameItem;
import com.minederp.kingdoms.games.GameLogic;
import com.sk89q.worldedit.blocks.BlockType;

public class PolygonBuilder {
	public int drawingPolygon;
	private int polygonSetIndex = 0;
	private int polygonMoveIndex = 0;
	public Polygon polygon;
	private final GameLogic gameLogic;
	private Player actingPlayer;

	public PolygonBuilder(Polygon pl, GameLogic logic) {
		polygon = pl;
		this.gameLogic = logic;
	}

	private void drawPolygon(Material mat, Material mat2, World world) {
		if (polygon == null || polygon.size() == 0)
			return;
		for (int i = 0; i < polygon.size() - 1; i++) {
			PolygonPoint p = polygon.get(i);
			PolygonPoint p2 = polygon.get(i + 1);
			drawLineInternal(world, mat, p.X, p.Z, p2.X, p2.Z);

		}
		PolygonPoint p = polygon.get(polygon.size() - 1);
		PolygonPoint p2 = polygon.get(0);
		drawLineInternal(world, mat, p.X, p.Z, p2.X, p2.Z);

		for (int i = 0; i < polygon.size(); i++) {
			p = polygon.get(i);
			Block bl = world.getBlockAt(p.X, p.Y, p.Z);
			gameLogic.addBlockForReprint(new GameItem(p.X, p.Y, p.Z, bl.getTypeId(), bl.getData(), "Line"));
			bl.setType(mat2);
		}

		if (polygonMoveIndex > 0) {

			p = polygon.get(polygonMoveIndex - 1);
			Block bl = world.getBlockAt(p.X, p.Y, p.Z);
			gameLogic.addBlockForReprint(new GameItem(p.X, p.Y, p.Z, bl.getTypeId(), bl.getData(), "Line"));
			bl.setType(Material.CLAY_BRICK);
		} else {
			p = polygon.get(polygonSetIndex - 1);
			Block bl = world.getBlockAt(p.X, p.Y, p.Z);
			gameLogic.addBlockForReprint(new GameItem(p.X, p.Y, p.Z, bl.getTypeId(), bl.getData(), "Line"));
			bl.setType(Material.GOLD_BLOCK);
		}
	}

	public void drawLineInternal(World world, Material type, int p1X, int p1Y, int p2X, int p2Y) {
		int dx = Math.abs(p2X - p1X);
		int dy = Math.abs(p2Y - p1Y);
		int sx = 1, sy = 1, e2;
		int error = dx - dy;
		if (p1X > p2X)
			sx = -1;
		if (p1Y > p2Y)
			sy = -1;
		while (true) {
			Block bl = null;
			int Y;
			for (Y = 127; Y > 0; Y--) {
				bl = world.getBlockAt(p1X, Y, p1Y);
				if (!BlockType.canPassThrough(bl.getTypeId()))
					break;
			}

			gameLogic.addBlockForReprint(new GameItem(p1X, Y, p1Y, bl.getTypeId(), bl.getData(), "Line"));
			bl.setType(type);
			if (p1X == p2X && p1Y == p2Y)
				break;
			e2 = error * 2;
			if (e2 > -dy) {
				error -= dy;
				p1X += sx;
			}
			if (e2 < dx) {
				error += dx;
				p1Y += sy;
			}
		}
	}

	public boolean blockClick(BlockFace face, Block clickedBlock, Player player) {

		if (drawingPolygon > 0 && actingPlayer.getName().equals(player.getName())) {
			gameLogic.clearReprint(player.getWorld(), "Line");

			int y;
			Location loc = clickedBlock.getLocation();

			y = loc.getBlockY();

			if (drawingPolygon == 2) {
				drawingPolygon = 1;
				polygon.setIndex(polygonMoveIndex, loc.getBlockX(), y, loc.getBlockZ());
				polygonMoveIndex = -1;
				player.sendMessage(ChatColor.GREEN + "Corner Moved.");
				drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
				return true;
			}

			if (player.isSneaking()) {
				player.sendMessage(ChatColor.GREEN + "Corner Removed.");
				polygon.remove(loc.getBlockX(), y, loc.getBlockZ());
				polygonSetIndex--;

			} else {

				for (int i = 0; i < polygon.size(); i++) {
					PolygonPoint p = polygon.get(i);
					if (p.X == loc.getBlockX() && p.Z == loc.getBlockZ()) {
						polygonSetIndex = i + 1;
						player.sendMessage(ChatColor.GREEN + "Corner Selected.");

						drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
						return true;
					}
				}

				if (polygonSetIndex == polygon.size()) {
					polygon.add(new PolygonPoint(loc.getBlockX(), y, loc.getBlockZ()));
					polygonSetIndex++;
				} else {
					polygon.add(polygonSetIndex, new PolygonPoint(loc.getBlockX(), y, loc.getBlockZ()));
					polygonSetIndex++;
				}

				player.sendMessage(ChatColor.GREEN + "Corner Added.");
			}

			drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
			return true;
		}
		return false;
	}

	public void showWalls(World w, boolean b) {
		if (b)
			drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, w);
		else {

			gameLogic.clearReprint(w, "Line");
		}
	}

	public void startPolygon(Player player) {
		player.sendMessage(ChatColor.LIGHT_PURPLE + "    The next block you click will be the first corner");
		player.sendMessage(ChatColor.AQUA + "      Right click the last block to end the polygon");
		player.sendMessage(ChatColor.GOLD + "      Sneak + click to remove a corner.");

		drawingPolygon = 1;
		actingPlayer = player;
		drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
	}

	public boolean blockPlaced(BlockFace face, Block clickedBlock, Player player) {

		if (drawingPolygon == 1 && actingPlayer.getName().equals(player.getName())) {

			if (player.isSneaking()) {
				drawingPolygon = 2;

				Location loc = clickedBlock.getLocation();
				for (int i = 0; i < polygon.size(); i++) {
					PolygonPoint p = polygon.get(i);
					polygonSetIndex = -1;
					if (p.X == loc.getBlockX() && p.Z == loc.getBlockZ()) {
						polygonMoveIndex = i + 1;
						player.sendMessage(ChatColor.GREEN + "Corner Selected For Movement.");
						drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());

					}
				}
				return true;

			} else {

				drawingPolygon = 0;
				gameLogic.clearReprint(player.getWorld(), "Line");
			}
			return true;
		}
		return false;
	}

}
