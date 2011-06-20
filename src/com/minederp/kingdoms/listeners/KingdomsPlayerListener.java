package com.minederp.kingdoms.listeners;

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
import com.minederp.kingdoms.orm.KingdomPlayer;
import com.minederp.kingdoms.util.Helper;

public class KingdomsPlayerListener extends PlayerListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsPlayerListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	public void onPlayerJoin(PlayerJoinEvent event) {

	 	event.getPlayer().getInventory().setHelmet(new ItemStack(Material.GLOWSTONE));
		 event.getPlayer().getLocation().getBlock().getChunk().
		
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

		kingdomsPlugin.gameLogic.updatePlayerGamePosition(event.getPlayer(), event.getTo());

	}

	public void onPlayerRespawn(PlayerRespawnEvent event) {

		kingdomsPlugin.gameLogic.playerRespawn(event.getPlayer());

	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		kingdomsPlugin.gameLogic.blockClick(event.getClickedBlock(), event.getPlayer());
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
	}

	public void onPlayerPreLogin(PlayerPreLoginEvent event) {

	}

}
