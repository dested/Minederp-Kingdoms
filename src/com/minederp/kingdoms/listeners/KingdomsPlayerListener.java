package com.minederp.kingdoms.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.games.GameItem;
import com.minederp.kingdoms.orm.KingdomPlayer;
import com.minederp.kingdoms.util.Helper;

public class KingdomsPlayerListener extends PlayerListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsPlayerListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	public void onPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();

		KingdomPlayer p = KingdomPlayer.getFirstByPlayerName(player.getName());
		if (p == null) {
			p = new KingdomPlayer();
			p.setPlayerName(event.getPlayer().getName());
			p.setPlayerNickName(event.getPlayer().getName());
			p.setKingdomID(0);
			p.insert();

			player.sendMessage(ChatColor.LIGHT_PURPLE + " Welcome to the game. Please read the help map. (Help map not available)");

			Helper.messagePlayerInList(player.getWorld().getPlayers(), ChatColor.LIGHT_PURPLE + p.getPlayerNickName()
					+ " Has just begun his Kingdoms Journey.");
		} else {
			player.sendMessage(ChatColor.AQUA + " Welcome back to the game. Please read the help map. (Help map not available)");
			Helper.messagePlayerInList(player.getWorld().getPlayers(), ChatColor.LIGHT_PURPLE + p.getPlayerNickName()
					+ " Has resumed his Kingdoms Journey.");
		}
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
	}

	public void onPlayerMove(PlayerMoveEvent event) {
		Location l = event.getPlayer().getLocation();
		event.getPlayer().getLocation().getBlock().getChunk().setLight(l.getBlockX(), l.getBlockY(), l.getBlockZ(), 15);

		kingdomsPlugin.gameLogic.updatePlayerGamePosition(event.getPlayer(), event.getTo());

	}

	public void onPlayerRespawn(PlayerRespawnEvent event) {

		kingdomsPlugin.gameLogic.playerRespawn(event.getPlayer());

	}

	List<Point> points = new ArrayList<Point>();

	class Point {
		public Point(int x, int y, int z) {
			X = x;
			Y = y;
			Z = z;
		}

		private int X;
		private int Y;
		private int Z;

		public void setX(int x) {
			X = x;
		}

		public int getX() {
			return X;
		}

		public void setY(int y) {
			Y = y;
		}

		public int getY() {
			return Y;
		}

		public void setZ(int z) {
			Z = z;
		}

		public int getZ() {
			return Z;
		}
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		kingdomsPlugin.gameLogic.blockClick(event.getClickedBlock(), event.getPlayer());

		kingdomsPlugin.gameLogic.clearReprint(event.getPlayer(), "Line");

		Location loc = event.getClickedBlock().getLocation();
		points.add(new Point(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

		for (int i = 0; i < points.size() - 1; i++) {
			Point p = points.get(i);
			Point p2 = points.get(i + 1);
			drawLineInternal(event.getPlayer().getWorld(), Material.OBSIDIAN, p.X, p.Z, p2.X, p2.Z, p.Y);
		}
		Point p = points.get(points.size() - 1);
		Point p2 = points.get(0);
		drawLineInternal(event.getPlayer().getWorld(), Material.OBSIDIAN, p.X, p.Z, p2.X, p2.Z, p.Y);

	}

	public void drawLineInternal(World world, Material type, int p1X, int p1Y, int p2X, int p2Y, int Y) {
		int dx = Math.abs(p2X - p1X);
		int dy = Math.abs(p2Y - p1Y);
		int sx = 1, sy = 1, e2;
		int error = dx - dy;
		if (p1X > p2X)
			sx = -1;
		if (p1Y > p2Y)
			sy = -1;
		while (true) {
			Block bl = world.getBlockAt(p1X, Y, p1Y);
			kingdomsPlugin.gameLogic.blocksForReprint.add(new GameItem(p1X, Y, p1Y, bl.getTypeId(), bl.getData(), "Line"));
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

	public void onPlayerLogin(PlayerLoginEvent event) {
	}

	public void onPlayerPreLogin(PlayerPreLoginEvent event) {

	}

}
