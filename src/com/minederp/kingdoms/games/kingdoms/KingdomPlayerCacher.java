package com.minederp.kingdoms.games.kingdoms;

import org.bukkit.entity.Player;

import com.minederp.kingdoms.orm.KingdomPlayer;

public class KingdomPlayerCacher {
	public KingdomPlayerCacher(KingdomPlayer kp, Player player) {
		kingdomPlayer = kp;
		this.player = player;
	}

	public KingdomPlayer kingdomPlayer;
	public Player player;

}
