package com.minederp.kingdoms.games.kingdoms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

import com.minederp.kingdoms.orm.KingdomPlayer;

public class TownPlayerCacher implements Iterable<KingdomPlayerCacher> {
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
			if (player.equals(p.player))
				return true;
		}
		return false;
	}	
	public KingdomPlayer getPlayer(Player player) {

		for (KingdomPlayerCacher p : players) {
			if (player.equals(p.player))
				return p.kingdomPlayer;
		}
		return null;
	}

	public boolean contains(String name) {
		name = name.toLowerCase();

		for (KingdomPlayerCacher p : players) {
			if (p.kingdomPlayer.getPlayerName().toLowerCase().equals(name))
				return true;
		}
		return false;
	}

	public KingdomPlayer getPlayer(String name) {
		name = name.toLowerCase();

		for (KingdomPlayerCacher p : players) {
			if (p.kingdomPlayer.getPlayerName().toLowerCase().equals(name))
				return p.kingdomPlayer;
		}
		return null;
	}

	@Override
	public Iterator<KingdomPlayerCacher> iterator() {
return players.iterator();
	}

}
