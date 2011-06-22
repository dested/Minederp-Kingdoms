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

		double lowest= Double.MAX_VALUE;
		int lIndex = 0;

		for (int i = 0; i < distances.length; i++) {
			double d = distances[i];
			if (lowest< d) {
				lIndex = i;
				lowest = d;
			}
		}
		return lIndex;
	}
}
