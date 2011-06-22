package com.minederp.kingdoms.towns;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.games.Game;
import com.minederp.kingdoms.games.GameItem;
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.util.Helper;
import com.minederp.kingdoms.util.Polygon;
import com.minederp.kingdoms.util.PolygonPoint;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.blocks.BlockType;

public class TownsGame extends Game {
	private List<Player> playersInTheArea;
	private List<Player> notPlayersInTheArea;

	private Polygon townLocation = new Polygon();
	public int drawingPolygon;
	private final KingdomsPlugin kingdomsPlugin;
	private int polygonSetIndex = 0;
	private int polygonMoveIndex = 0;

	private Player actingPlayer;
	private GameLogic logic;

	public TownsGame(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;
	}

	@Override
	public boolean canPlayerJoin(Player player) {
		return true;
	}

	@Override
	public void onLoad(GameLogic logic) {
		this.logic = logic;
		playersInTheArea = new ArrayList<Player>();
		notPlayersInTheArea = new ArrayList<Player>();
		townLocation = new Polygon();
	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {
		logic.clearReprint(movingPlayer.getWorld(), "Drawing");

		if (townLocation.contains(to.getBlockX(), to.getBlockZ())) {

			if (Helper.containsPlayers(playersInTheArea, movingPlayer)) {
				return;
			}
			notPlayersInTheArea.remove(movingPlayer);
			movingPlayer.sendMessage(ChatColor.AQUA + " You have entered the town.");
			playersInTheArea.add(movingPlayer);
			return;
		}

		if (Helper.containsPlayers(notPlayersInTheArea, movingPlayer)) {
			return;
		}

		movingPlayer.sendMessage(ChatColor.AQUA + " You have entered wilderness...");
		notPlayersInTheArea.add(movingPlayer);
		playersInTheArea.remove(movingPlayer);
	}

	@Override
	public void processCommand(String header, CommandContext args, Player player) {

		if (!header.equals("t"))
			return;

		if (args.argsLength() == 0 || args.getString(0).toLowerCase().equals("help")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.LIGHT_PURPLE + "Towns");

			player.sendMessage(sb.toString());
			return;
		}

		if (Helper.argEquals(args.getString(0), "ShowWalls")) {
			if (args.length() == 3) {
				if (Helper.argEquals(args.getString(1), "True")) {
					showWalls(player.getWorld(), true);
				} else if (Helper.argEquals(args.getString(1), "False")) {
					showWalls(player.getWorld(), false);
				}
			} else
				showWalls(player.getWorld(), true);
			return;
		}
		if (Helper.argEquals(args.getString(0), "SetPolygon")) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "    The next block you click will be the first corner");
			player.sendMessage(ChatColor.AQUA + "      Right click the last block to end the polygon");
			player.sendMessage(ChatColor.GOLD + "      Sneak + click to remove a corner.");

			drawingPolygon = 1;
			actingPlayer = player;
			drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());

			return;
		}

	}

	private void showWalls(World w, boolean b) {
		if (b)
			drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, w);
		else {

			logic.clearReprint(w, "Line");
		}
	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {
		return false;
	}

	@Override
	public boolean blockClick(Block clickedBlock, Player player) {

		if (drawingPolygon > 0 && actingPlayer.getName().equals(player.getName())) {
			logic.clearReprint(player.getWorld(), "Line");

			int y;
			Location loc = clickedBlock.getLocation();
			// if (townLocation.size() > 1) {
			// y = townLocation.get(0).Y;
			// } else
			y = loc.getBlockY();

			if (drawingPolygon == 2) {
				drawingPolygon = 1;
				townLocation.setIndex(polygonMoveIndex, loc.getBlockX(), y, loc.getBlockZ());
				polygonMoveIndex = -1;
				player.sendMessage(ChatColor.GREEN + "Corner Moved.");
				drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
				return true;
			}

			if (player.isSneaking()) {
				player.sendMessage(ChatColor.GREEN + "Corner Removed.");
				townLocation.remove(loc.getBlockX(), y, loc.getBlockZ());
				polygonSetIndex--;

			} else {

				for (int i = 0; i < townLocation.size(); i++) {
					PolygonPoint p = townLocation.get(i);
					if (p.X == loc.getBlockX() && p.Z == loc.getBlockZ()) {
						polygonSetIndex = i + 1;
						player.sendMessage(ChatColor.GREEN + "Corner Selected.");

						drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
						return true;
					}
				}

				if (polygonSetIndex == townLocation.size()) {
					townLocation.add(new PolygonPoint(loc.getBlockX(), y, loc.getBlockZ()));
					polygonSetIndex++;
				} else {
					townLocation.add(polygonSetIndex, new PolygonPoint(loc.getBlockX(), y, loc.getBlockZ()));
					polygonSetIndex++;
				}

				player.sendMessage(ChatColor.GREEN + "Corner Added.");
			}

			drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());
			return true;
		}
		return false;
	}

	private void drawPolygon(Material mat, Material mat2, World world) {
		if (townLocation == null || townLocation.size() == 0)
			return;
		for (int i = 0; i < townLocation.size() - 1; i++) {
			PolygonPoint p = townLocation.get(i);
			PolygonPoint p2 = townLocation.get(i + 1);
			drawLineInternal(world, mat, p.X, p.Z, p2.X, p2.Z);

		}
		PolygonPoint p = townLocation.get(townLocation.size() - 1);
		PolygonPoint p2 = townLocation.get(0);
		drawLineInternal(world, mat, p.X, p.Z, p2.X, p2.Z);

		for (int i = 0; i < townLocation.size(); i++) {
			p = townLocation.get(i);
			Block bl = world.getBlockAt(p.X, p.Y, p.Z);
			kingdomsPlugin.gameLogic.addBlockForReprint(new GameItem(p.X, p.Y, p.Z, bl.getTypeId(), bl.getData(), "Line"));
			bl.setType(mat2);
		}

		if (polygonMoveIndex >= 0) {

			p = townLocation.get(polygonMoveIndex - 1);
			Block bl = world.getBlockAt(p.X, p.Y, p.Z);
			kingdomsPlugin.gameLogic.addBlockForReprint(new GameItem(p.X, p.Y, p.Z, bl.getTypeId(), bl.getData(), "Line"));
			bl.setType(Material.CLAY_BRICK);
		} else {
			p = townLocation.get(polygonSetIndex - 1);
			Block bl = world.getBlockAt(p.X, p.Y, p.Z);
			kingdomsPlugin.gameLogic.addBlockForReprint(new GameItem(p.X, p.Y, p.Z, bl.getTypeId(), bl.getData(), "Line"));
			bl.setType(Material.GOLD_BLOCK);
		}
	}

	@Override
	public boolean blockPlaced(Block block, Player player) {
		if (drawingPolygon == 1 && actingPlayer.getName().equals(player.getName())) {

			if (player.isSneaking()) {
				drawingPolygon = 2;

				Location loc = block.getLocation();
				for (int i = 0; i < townLocation.size(); i++) {
					PolygonPoint p = townLocation.get(i);
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
				logic.clearReprint(player.getWorld(), "Line");
			}
			return true;
		}
		return false;
	}

	@Override
	public void playerDied(final Player player) {

	}

	@Override
	public void playerRespawn(final Player player) {

	}

	@Override
	public boolean playerFight(Player damagee, Player damager) {
		return true;

	}

	@Override
	public void playerDying(Player entity) {

	}

	@Override
	public void entityDied(Entity entity, EntityDeathEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entityHurt(Entity entity, EntityDamageEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void joinGame(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leaveGame(Player player) {
		// TODO Auto-generated method stub

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

			kingdomsPlugin.gameLogic.addBlockForReprint(new GameItem(p1X, Y, p1Y, bl.getTypeId(), bl.getData(), "Line"));
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

}