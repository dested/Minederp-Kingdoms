package com.minederp.kingdoms.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.entity.CraftFireball;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.maps.MapDrawer;
import org.bukkit.util.Vector;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.games.GameItem;
import com.minederp.kingdoms.orm.KingdomPlayer;
import com.minederp.kingdoms.util.Helper;
import com.sun.org.apache.xerces.internal.util.IntStack;

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
		// Location l = event.getPlayer().getLocation();
		// event.getPlayer().getLocation().getBlock().getChunk().setLight(l.getBlockX(),
		// l.getBlockY(), l.getBlockZ(), 15);

		kingdomsPlugin.gameLogic.updatePlayerGamePosition(event.getPlayer(), event.getTo());

	}

	public void onPlayerRespawn(PlayerRespawnEvent event) {

		kingdomsPlugin.gameLogic.playerRespawn(event.getPlayer());

	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		switch (event.getAction()) {
		case LEFT_CLICK_AIR:

			/*
			 * Player player = event.getPlayer();
			 * 
			 * Fireball fb = player.getWorld().spawn(player.getLocation(),
			 * Fireball.class);
			 * fb.setDirection(player.getVelocity().multiply(50));
			 * fb.setYield(1);
			 */

			/*
			 * World w = event.getPlayer().getWorld();
			 * 
			 * int chunksPerPiece = 1; int mapSize = 1000;
			 * 
			 * int chunkX = 0, chunkZ = 0; int per = mapSize / chunksPerPiece /
			 * 16 + 1; int[][] worldMap = new int[per][per];
			 * 
			 * int lX = 0, lY = 0; int defaultBlock = Material.OBSIDIAN.getId();
			 * 
			 * for (int j = 0; j < mapSize; j += chunksPerPiece * 16) {
			 * System.out.print("X: " + lX + " Y: " + lY + " "); chunkZ = 0; lY
			 * = 0; for (int j1 = 0; j1 < mapSize; j1 += chunksPerPiece * 16) {
			 * 
			 * int[] ints = new int[chunksPerPiece * 16 * chunksPerPiece * 16];
			 * for (int xChunk = 0; xChunk < chunksPerPiece; xChunk++) { for
			 * (int yChunk = 0; yChunk < chunksPerPiece; yChunk++) { if
			 * (w.loadChunk(chunkX, chunkZ, true)) { // va.getHighestBlockYAt(x,
			 * z) // Chunk va = w.getChunkAt(chunkX, chunkZ); // if
			 * (w.loadChunk(chunkX, chunkZ, false)) { w.loadChunk(chunkX,
			 * chunkZ); for (int i = 0; i < 16; i++) { for (int k = 0; k < 16;
			 * k++) { int y=0; for (int yy = 127; yy >= 0; yy--) { int id =
			 * w.getBlockTypeIdAt(j + i, yy, j1 + k); if (id != 0) { y = yy;
			 * break; } }
			 * 
			 * int id = w.getBlockAt(j + i, y - 1, j1 + k).getTypeId();
			 * 
			 * ints[(i + (xChunk * 16)) + (k + (yChunk * 16)) * (chunksPerPiece)
			 * * 16] = id; } } } /* } else { for (int i = 0; i < 16; i++) { for
			 * (int k = 0; k < 16; k++) { ints[(i + (xChunk * 16)) + (k +
			 * (yChunk * 16)) * (chunksPerPiece) * 16] = defaultBlock; } } } /
			 * chunkZ++; } chunkX++; }
			 * 
			 * HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>();
			 * for (int i = 0; i < ints.length; i++) { int k = ints[i]; Integer
			 * f; if ((f = ids.get(k)) != null) { ids.put(k, f + 1); } else
			 * ids.put(k, 1); } int[] iids = new int[ids.size()]; int index = 0;
			 * Set<Entry<Integer, Integer>> st = ids.entrySet(); for
			 * (Entry<Integer, Integer> i : st) { iids[index++] = i.getValue();
			 * } int highest = 0; int hIndex = 0; for (int i = 0; i <
			 * iids.length; i++) { int k = iids[i]; if (k > highest) { highest =
			 * k; hIndex = i; } } index = 0; for (Entry<Integer, Integer> i :
			 * st) { if (index == hIndex) break; worldMap[lY][lX] = i.getKey();
			 * index++; } lY++; } lX++; }
			 * 
			 * for (int x = 0; x < per; x++) { for (int y = 0; y < per; y++) {
			 * int fd = worldMap[y][x]; System.out.println(x + " " + y + "  " +
			 * fd); w.getBlockAt(x + 100, 80, y + 100).setTypeId(fd); } }
			 * 
			 * event.getPlayer().teleport(new Location(w, 100, 90, 100));
			 */
			break;
		case LEFT_CLICK_BLOCK:
			event.setCancelled(kingdomsPlugin.blockClick(event.getBlockFace(),event.getClickedBlock(), event.getPlayer()));

			break;
		case PHYSICAL:
			break;
		case RIGHT_CLICK_AIR:
			if(true)return ;
			if (player.isSneaking()) {
				Block bl = player.getLocation().getBlock();
				int j = 0;
				while (true) {
					bl = bl.getRelative(BlockFace.UP);
					bl.setType(Material.FENCE);
					j++;
					if (j > 10)
						break;
				}
				bl = bl.getRelative(BlockFace.UP);
				bl.setType(Material.GLASS);
				int c = 0;
				while (true) {
					bl = bl.getRelative(BlockFace.DOWN);
					bl.setType(Material.GLASS);
					bl.getRelative(-c, 0, c).setType(Material.GLASS);
					bl.getRelative(-c, 0, 0).setType(Material.GLASS);
					bl.getRelative(-c, 0, -c).setType(Material.GLASS);

					bl.getRelative(0, 0, c).setType(Material.GLASS);
					bl.getRelative(0, 0, 0).setType(Material.GLASS);
					bl.getRelative(0, 0, -c).setType(Material.GLASS);
					bl.getRelative(c, 0, c).setType(Material.GLASS);
					bl.getRelative(c, 0, 0).setType(Material.GLASS);
					bl.getRelative(c, 0, -c).setType(Material.GLASS);

					c++;
					if (c > 10)
						break;
				}
			}
			break;
		case RIGHT_CLICK_BLOCK:
			event.setCancelled(this.kingdomsPlugin.gameLogic.blockPlaced(event.getBlockFace(),event.getClickedBlock(), event.getPlayer()));

			break;

		}

		;

	}

	public void onPlayerLogin(PlayerLoginEvent event) {
	}

	public void onPlayerPreLogin(PlayerPreLoginEvent event) {

	}

}
