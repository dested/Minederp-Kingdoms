package com.minederp.kingdoms.towns.content;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.orm.Kingdom;
import com.minederp.kingdoms.orm.KingdomPlayer;
import com.minederp.kingdoms.orm.Town;
import com.minederp.kingdoms.towns.TownPlayerCacher;
import com.minederp.kingdoms.util.Helper;
import com.minederp.kingdoms.util.Polygon;
import com.minederp.kingdoms.util.PolygonBuilder;
import com.minederp.kingdoms.util.PolygonPoint;
import com.sk89q.minecraft.util.commands.CommandContext;

public class KingdomContent {
	public Kingdom myKingdom;

	private final KingdomsPlugin plugin;
	private final GameLogic logic;
	private TownPlayerCacher kingdomPlayers;

	public KingdomContent(Kingdom t, KingdomsPlugin plugin, GameLogic logic) {
		this.plugin = plugin;
		this.logic = logic;
		myKingdom= t;
		load();
	}

	public void addPlayerToKingdom(KingdomPlayer player) {
		player.setKingdomID(myKingdom.getKingdomID());
		player.update();
	}

	private void loadPlayers() {
		kingdomPlayers.empty();
		for (KingdomPlayer kp : KingdomPlayer.getAllByKingdomID(myKingdom.getKingdomID())) {
			kingdomPlayers.add(kp, plugin.getServer().getPlayer(kp.getPlayerName()));
			// getplayer returns null if logged off
		}
	}

	public void playerJoin(Player player) {
		loadPlayers();
	}

	public void playerQuit(Player player) {
		loadPlayers();
	}

	private void load() { 
		loadPlayers();
	}

	public boolean playerMove(Player movingPlayer, Location to) {
		 
		return false;
	}

	public void save() {
		myKingdom.update();
	}

	public boolean playerCommand(CommandContext args, Player player) {

		if (!kingdomPlayers.contains(player))
			return false;

		if (args.argsLength() == 0 ||Helper.argEquals(args.getString(0), "Help")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.LIGHT_PURPLE + "Kingdom help");

			player.sendMessage(sb.toString());
			return true;
		}
  
		return true;
	}

	public boolean blockClick(BlockFace face, Block clickedBlock, Player player) {
	 
		return false;
	}

	public boolean blockPlaced(BlockFace face, Block clickedBlock, Player player) {
		 
		return false;
	}
}
