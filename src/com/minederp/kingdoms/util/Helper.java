package com.minederp.kingdoms.util;

import java.util.List;

import org.bukkit.entity.Player;

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
}
