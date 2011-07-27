package com.minederp.kingdoms.towns;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

public class KingdomsGame extends Game {

	private List<Player> playersInTheArea;
	private List<Player> notPlayersInTheArea;

	private Polygon kingdomSplit = new Polygon();
	public int drawingPolygon;
	private final KingdomsPlugin kingdomsPlugin;
	private int polygonSetIndex = 0;

	private Player actingPlayer;
	private GameLogic logic;

	public KingdomsGame(KingdomsPlugin kingdomsPlugin) {
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
		kingdomSplit = new Polygon();
	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {  
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
		if (Helper.argEquals(args.getString(0), "ModifyLine")) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "    The next block you click will be the first corner");
			player.sendMessage(ChatColor.AQUA + "      Right click the last block to end the polygon");
			player.sendMessage(ChatColor.GOLD + "      Sneak + click to remove a corner.");

			drawingPolygon = 1;
			actingPlayer = player;
			drawPolygon(Material.OBSIDIAN, Material.DIAMOND_BLOCK, player.getWorld());

			return;
		}
		if (Helper.argEquals(args.getString(0), "Kingdom")) {
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
	public boolean blockClick(BlockFace face,Block clickedBlock, Player player) {

		 
		return false;
	}

	private void drawPolygon(Material mat, Material mat2, World world) {
		 
	}

	@Override
	public boolean blockPlaced(BlockFace face,Block block, Player player) {
		if (drawingPolygon == 1 && actingPlayer.getName().equals(player.getName())) {
			drawingPolygon = 0;
			logic.clearReprint(player.getWorld(), "Line");
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
		return false;

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