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

public class TownContent {
	public Town myTown;
	private PolygonBuilder townPolygon;

	private List<Player> playersInTheArea;

	private final KingdomsPlugin plugin;
	private final GameLogic logic;
	private TownPlayerCacher townPlayers;

	public TownContent(Town t, KingdomsPlugin plugin, GameLogic logic) {
		this.plugin = plugin;
		this.logic = logic;
		myTown = t;
		load();
	}

	public TownContent(String name, KingdomPlayer governor, Kingdom kingdom, KingdomsPlugin plugin, GameLogic logic) {
		this.plugin = plugin;
		this.logic = logic;

		Town t = new Town();
		t.setGovernorID(governor.getKingdomPlayerID());
		t.setTownName(name);
		t.setKingdomID(kingdom.getKingdomID());
		t.insert();
		myTown = Town.getFirstByTownName(name);
		
		addPlayerToTown(governor);
		load();
	}

	private void addPlayerToTown(KingdomPlayer player) {
		player.setTownID(myTown.getTownID());
		player.update();
	}

	private void loadPlayers() {
		townPlayers.empty();
		for (KingdomPlayer kp : KingdomPlayer.getAllByTownID(myTown.getTownID())) {
			townPlayers.add(kp, plugin.getServer().getPlayer(kp.getPlayerName()));
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
		playersInTheArea = new ArrayList<Player>();
		townPolygon = new PolygonBuilder(Polygon.deserialize(myTown.getTownPolygon()), logic);
		townPlayers = new TownPlayerCacher();
		
		loadPlayers();
	}

	public boolean playerMove(Player movingPlayer, Location to) {
		logic.clearReprint(movingPlayer.getWorld(), "Drawing" + myTown.getTownName());

		if (townPolygon.polygon.contains(to.getBlockX(), to.getBlockZ())) {

			if (Helper.containsPlayers(playersInTheArea, movingPlayer)) {
				return true;
			}
			movingPlayer.sendMessage(ChatColor.AQUA + " You have entered the town.");
			playersInTheArea.add(movingPlayer);
			return true;
		}

		playersInTheArea.remove(movingPlayer);
		return false;
	}

	public void save() {
		myTown.update();
	}

	public boolean playerCommand(CommandContext args, Player player) {

		if (!townPlayers.contains(player))
			return false;

		if (args.argsLength() == 0 || Helper.argEquals(args.getString(0), "Help")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.LIGHT_PURPLE + "Towns");

			player.sendMessage(sb.toString());
			return true;
		}

		if (Helper.argEquals(args.getString(0), "ShowWalls")) {
			if (args.length() == 3) {
				if (Helper.argEquals(args.getString(1), "True")) {
					townPolygon.showWalls(player.getWorld(), true);
				} else if (Helper.argEquals(args.getString(1), "False")) {
					townPolygon.showWalls(player.getWorld(), false);
				}
			} else
				townPolygon.showWalls(player.getWorld(), true);
			return true;
		}
		if (Helper.argEquals(args.getString(0), "SetPolygon")) {
			townPolygon.startPolygon(player);

			return true;
		}

		return true;
	}

	public boolean blockClick(BlockFace face, Block clickedBlock, Player player) {
		if (townPolygon.blockClick(face, clickedBlock, player))

			return true;

		return false;
	}

	public boolean blockPlaced(BlockFace face, Block clickedBlock, Player player) {
		if (townPolygon.blockPlaced(face, clickedBlock, player))

			return true;

		return false;
	}
}
