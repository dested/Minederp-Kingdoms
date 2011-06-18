package com.minederp.kingdoms.games.ctf;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CTFTeam {
	public int points;
	public DyeColor color;
	public boolean setSpawn;
	public boolean setFlag;
	public Location spawn;
	public Location flag;
	public String name;
	public List<Player> players;

	public Player withFlag;
	public ItemStack flagItem;

	public CTFTeam(String name, DyeColor col) {
		this.name = name;
		players = new ArrayList<Player>();

		color=col;
	}

}