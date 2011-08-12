package com.minederp.kingdoms.util;

import java.awt.Point;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.minederp.kingdoms.games.GameItem;
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.games.kingdoms.content.PolygonChecker;
import com.sk89q.worldedit.blocks.BlockType;

public class PolygonBuilder {
	public String guid = UUID.randomUUID().toString();
	public int drawingPolygon;
	private int setIndex = -1;
	private int moveIndex = -1;
	public Polygon polygon;
	private final GameLogic gameLogic;
	private Player actingPlayer;
	private final PolygonChecker polygonSave;

	public PolygonBuilder(Polygon pl, GameLogic logic, PolygonChecker polygonSave) {
		polygon = pl;
		this.gameLogic = logic;
		this.polygonSave = polygonSave;

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
			gameLogic.addBlockForReprint(new GameItem(p.X, p.Y, p.Z, bl.getTypeId(), bl.getData(), "Line" + guid));
			bl.setType(mat2);
		}

		if (moveIndex >= 0) {

			p = polygon.get(moveIndex);
			Block bl = world.getBlockAt(p.X, p.Y, p.Z);
			gameLogic.addBlockForReprint(new GameItem(p.X, p.Y, p.Z, bl.getTypeId(), bl.getData(), "Line" + guid));
			bl.setType(Material.CLAY_BRICK);

		}
		if (setIndex >= 0) {

			p = polygon.get(setIndex);
			Block bl = world.getBlockAt(p.X, p.Y, p.Z);
			gameLogic.addBlockForReprint(new GameItem(p.X, p.Y, p.Z, bl.getTypeId(), bl.getData(), "Line" + guid));
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

			gameLogic.addBlockForReprint(new GameItem(p1X, Y, p1Y, bl.getTypeId(), bl.getData(), "Line" + guid));
			bl.setType(type);

			world.playEffect(bl.getLocation().add(0, 1, 0), Effect.SMOKE, 1);
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
			gameLogic.clearReprint(player.getWorld(), "Line" + guid);

			int y;
			Location loc = clickedBlock.getLocation();

			y = loc.getBlockY();

			if (drawingPolygon == 2) {
				drawingPolygon = 1;
				polygon.setIndex(moveIndex, loc.getBlockX(), y, loc.getBlockZ());

				setIndex = moveIndex;
				moveIndex = -1;
				player.sendMessage(ChatColor.GREEN + "Corner Moved.");
				drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
				return true;
			}

			if (player.isSneaking()) {

				for (int i = 0; i < polygon.size(); i++) {
					PolygonPoint p = polygon.get(i);
					if (p.X == loc.getBlockX() && p.Z == loc.getBlockZ()) {
						player.sendMessage(ChatColor.GREEN + "Corner Removed.");
						polygon.remove(loc.getBlockX(), y, loc.getBlockZ());

						if (setIndex >= i) {
							setIndex--;
						}
					}
				}
			} else {

				for (int i = 0; i < polygon.size(); i++) {
					PolygonPoint p = polygon.get(i);
					if (p.X == loc.getBlockX() && p.Z == loc.getBlockZ()) {
						setIndex = i;
						player.sendMessage(ChatColor.GREEN + "Corner Selected.");

						drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
						return true;
					}
				}

				PolygonPoint p;
				if (setIndex == 0) {
					polygon.add(p = new PolygonPoint(loc.getBlockX(), y, loc.getBlockZ()));

				} else if (setIndex == polygon.size() - 1) {
					polygon.add(p = new PolygonPoint(loc.getBlockX(), y, loc.getBlockZ()));
					setIndex++;
				} else {
					setIndex++;
					polygon.add(setIndex, p = new PolygonPoint(loc.getBlockX(), y, loc.getBlockZ()));

				}
				if (polygonSave.collides(polygon)) {
					polygon.remove(p);
					setIndex--;
					player.sendMessage(ChatColor.GREEN + "Collision detected.");
				} else
					player.sendMessage(ChatColor.GREEN + "Corner Added.");
			}

			drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
			return true;
		}
		return false;
	}

	public void showWalls(World w, boolean b) {
		shouldShowWalls = b;
		if (b) {
			drawingPolygon = 1;

			drawPolygon(Material.CLAY, Material.DIAMOND_BLOCK, w);
		} else {

			gameLogic.clearReprint(w, "Line" + guid);
		}
	}

	boolean shouldShowWalls = false;

	public void showWalls(World w) {
		shouldShowWalls = !shouldShowWalls;
		if (shouldShowWalls) {
			drawPolygon(Material.CLAY, Material.DIAMOND_BLOCK, w);
		} else {

			gameLogic.clearReprint(w, "Line" + guid);
		}
	}

	public void startPolygon(Player player) {
		player.sendMessage(ChatColor.LIGHT_PURPLE + "    The next block you click will be the first corner");
		player.sendMessage(ChatColor.AQUA + "      Right click the last block to end the polygon");
		player.sendMessage(ChatColor.GOLD + "      Sneak + click to remove a corner.");

		drawingPolygon = 1;
		actingPlayer = player;
		setIndex = 0;
		drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
	}

	public boolean blockPlaced(BlockFace face, Block clickedBlock, Player player) {

		if (drawingPolygon == 1 && actingPlayer.getName().equals(player.getName())) {

			if (player.isSneaking()) {
				drawingPolygon = 2;

				Location loc = clickedBlock.getLocation();
				for (int i = 0; i < polygon.size(); i++) {
					PolygonPoint p = polygon.get(i);
					setIndex = -1;
					if (p.X == loc.getBlockX() && p.Z == loc.getBlockZ()) {
						moveIndex = i;
						player.sendMessage(ChatColor.GREEN + "Corner Selected For Movement.");
						drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());

					}
				}
				return true;

			} else {
				setIndex = -1;
				moveIndex = -1;
				shouldShowWalls = false;
				drawingPolygon = 0;
				gameLogic.clearReprint(player.getWorld(), "Line" + guid);

				polygonSave.save(polygon);
			}
			return true;
		}
		return false;
	}

	public boolean contains(Location location) {
return polygon.contains(new Point(location.getBlockX(),location.getBlockZ()));
	}
}
