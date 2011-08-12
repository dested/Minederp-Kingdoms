package com.minederp.kingdoms.games.kingdoms.content;

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
import com.minederp.kingdoms.games.kingdoms.TownPlayerCacher;
import com.minederp.kingdoms.orm.Kingdom;
import com.minederp.kingdoms.orm.KingdomPlayer;
import com.minederp.kingdoms.orm.Town;
import com.minederp.kingdoms.orm.TownPlot;
import com.minederp.kingdoms.util.Helper;
import com.minederp.kingdoms.util.Polygon;
import com.minederp.kingdoms.util.PolygonBuilder;
import com.minederp.kingdoms.util.PolygonPoint;
import com.sk89q.minecraft.util.commands.CommandContext;

public class TownPlotContent {
	public TownPlot myTownPlot;
	public PolygonBuilder plotPolygon;
	private List<Player> playersInTheArea;
	private final KingdomsPlugin plugin;
	private final GameLogic logic;

	private KingdomPlayer owner;
	private final TownContent townContent;

	public TownPlotContent(TownContent tc, TownPlot tp, KingdomsPlugin plugin, GameLogic logic) {
		this.townContent = tc;
		this.plugin = plugin;
		this.logic = logic;
		myTownPlot = tp;
		load();
	}

	public TownPlotContent(TownContent tc, KingdomPlayer owner, Town town, KingdomsPlugin plugin, GameLogic logic) {
		this.townContent = tc;
		this.plugin = plugin;
		this.logic = logic;

		TownPlot tp = new TownPlot();
		tp.setOwnerID(owner.getKingdomPlayerID());
		tp.setTownID(town.getTownID());
		tp.insert();

		myTownPlot = TownPlot.getFirstByOwnerID(owner.getKingdomPlayerID());

		load();
	}

	public void playerJoin(Player player) {

	}

	public void playerQuit(Player player) {

	}

	private void load() {
		playersInTheArea = new ArrayList<Player>();
		final TownPlotContent me = this;
		plotPolygon = new PolygonBuilder(Polygon.deserialize(myTownPlot.getPlotPolygon()), logic, new PolygonChecker() {
			@Override
			public void save(Polygon polygon) {
				myTownPlot.setPlotPolygon(polygon.serialize());
				myTownPlot.update();
			}

			@Override
			public boolean collides(Polygon polygon) {

				return townContent.checkPlotCollision(me, polygon);
			}
		});
		owner = myTownPlot.getOwner();
	}

	public boolean playerMove(Player movingPlayer, Location to) {

		if (plotPolygon.polygon.contains(to.getBlockX(), to.getBlockZ())) {

			if (Helper.containsPlayers(playersInTheArea, movingPlayer)) {
				return true;
			}
			movingPlayer.sendMessage(ChatColor.AQUA + " You have entered the plot of " + myTownPlot.getOwner().getPlayerName() + ".");
			playersInTheArea.add(movingPlayer);
			return true;
		}

		playersInTheArea.remove(movingPlayer);
		return false;
	}

	public void save() {
		myTownPlot.update();
	}

	public boolean playerCommand(CommandContext args, Player player) {

		if (args.argsLength() == 0 || Helper.argEquals(args.getString(0), "Help")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.LIGHT_PURPLE + "Plot");

			player.sendMessage(sb.toString());
			return true;
		}

		if (Helper.argEquals(args.getString(0), "ShowWalls")) {
			if (args.length() == 3) {
				if (Helper.argEquals(args.getString(1), "True")) {
					plotPolygon.showWalls(player.getWorld(), true);
				} else if (Helper.argEquals(args.getString(1), "False")) {
					plotPolygon.showWalls(player.getWorld(), false);
				}
			} else
				plotPolygon.showWalls(player.getWorld());
			return true;
		}
		if (Helper.argEquals(args.getString(0), "SetPolygon")) {
			plotPolygon.startPolygon(player);

			return true;
		}

		return true;
	}

	public boolean blockClick(BlockFace face, Block clickedBlock, Player player) {
		if (plotPolygon.blockClick(face, clickedBlock, player))
			return true;

		if (!plotPolygon.polygon.contains(clickedBlock.getX(), clickedBlock.getX()))
			return false;

		if (!owner.getPlayerName().equals(player.getName())) {
			return true;
		}

		return false;
	}

	public boolean blockPlaced(BlockFace face, Block clickedBlock, Player player) {
		if (plotPolygon.blockPlaced(face, clickedBlock, player))
			return true;
		if (!plotPolygon.polygon.contains(clickedBlock.getX(), clickedBlock.getX()))
			return false;

		if (!owner.getPlayerName().equals(player.getName())) {
			return true;
		}

		return false;
	}
}
