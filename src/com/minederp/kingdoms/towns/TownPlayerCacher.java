package com.minederp.kingdoms.towns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.minederp.kingdoms.orm.KingdomPlayer;

public class TownPlayerCacher {
	private List<KingdomPlayerCacher> players;

	public TownPlayerCacher() {
		players = new ArrayList<KingdomPlayerCacher>();
	}

	public void add(KingdomPlayer kp, Player p) {
		players.add(new KingdomPlayerCacher(kp, p));
	}

	public void empty() {
		players.clear();

	}

	public boolean contains(Player player) {

		for (KingdomPlayerCacher p : players) {
			if (p.player.equals(player))
				return true;
		}
		return false;
	}

}
