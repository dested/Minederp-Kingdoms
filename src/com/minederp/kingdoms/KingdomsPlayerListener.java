package com.minederp.kingdoms;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class KingdomsPlayerListener extends PlayerListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsPlayerListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	public void onPlayerJoin(PlayerJoinEvent event) {

	}

	public void onPlayerQuit(PlayerQuitEvent event) {
	}

	public void onPlayerMove(PlayerMoveEvent event) {

		kingdomsPlugin.ctfGame.updatePlayerGamePosition(event.getPlayer(),
				event.getTo());

	}

	public void onPlayerRespawn(PlayerRespawnEvent event) {
		kingdomsPlugin.ctfGame.playerDied(event.getPlayer());

	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		kingdomsPlugin.ctfGame.blockClick(event.getClickedBlock(), event.getPlayer());
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
	}

	public void onPlayerPreLogin(PlayerPreLoginEvent event) {

	}

}
