package com.minederp.kingdoms.towns;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.games.Game;
import com.minederp.kingdoms.games.GameItem;
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.orm.KingdomPlayer;
import com.minederp.kingdoms.orm.Town;
import com.minederp.kingdoms.towns.content.TownContent;
import com.minederp.kingdoms.util.Helper;
import com.minederp.kingdoms.util.Polygon;
import com.minederp.kingdoms.util.PolygonPoint;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.blocks.BlockType;

public class TownsGame extends Game {
	private final KingdomsPlugin kingdomsPlugin;

	private List<Player> playersInWilderness;

	private GameLogic logic;
	private List<TownContent> towns;

	public TownsGame(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	@Override
	public boolean canPlayerJoin(Player player) {
		return true;
	}

	@Override
	public void onLoad(GameLogic logic) {
		this.logic = logic;

		playersInWilderness = new ArrayList<Player>();
		towns=new ArrayList<TownContent>();
		for (Town town : Town.getAll()) {
			towns.add(new TownContent(town, kingdomsPlugin, logic));
		}

	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {

		for (TownContent town : towns) {
			if (town.playerMove(movingPlayer, to)) {
				playersInWilderness.remove(movingPlayer);
				return;
			}
		}

		if (Helper.containsPlayers(playersInWilderness, movingPlayer)) {
			return;
		}

		movingPlayer.sendMessage(ChatColor.AQUA + " You have entered wilderness...");
		playersInWilderness.add(movingPlayer);

	}

	@Override
	public void processCommand(String header, CommandContext args, Player player) {

		if (!header.equals("t"))
			return;

		if (args.argsLength() > 0 && Helper.argEquals(args.getString(0), "CreateTown")) {
			// deduct money
			KingdomPlayer kp = KingdomPlayer.getFirstByPlayerName(player.getName());

			TownContent tc = new TownContent(args.getString(0), kp, kp.getKingdom(), kingdomsPlugin, logic);
			towns.add(tc);
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Town " + tc.myTown.getTownName() + " has been created.");
			return;
		}

		for (TownContent town : towns) {
			if (town.playerCommand(args, player)) {
				return;
			}
		}

		if (args.argsLength() == 0 || args.getString(0).toLowerCase().equals("help")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.LIGHT_PURPLE + "not in a town help");

			player.sendMessage(sb.toString());

		}

	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {
		return false;
	}

	@Override
	public void playerQuit(Player player) {
		for (TownContent town : towns) {
			town.playerQuit(player);
		}
	}

	@Override
	public void playerJoin(Player player) {
		for (TownContent town : towns) {
			town.playerJoin(player);
		}
	}

	@Override
	public boolean blockClick(BlockFace face, Block clickedBlock, Player player) {

		for (TownContent town : towns) {
			if (town.blockClick(face, clickedBlock, player)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean blockPlaced(BlockFace face, Block clickedBlock, Player player) {
		for (TownContent town : towns) {
			if (town.blockPlaced(face, clickedBlock, player)) {
				return true;
			}

		}

		return false;
	}

	@Override
	public void playerDied(final Player player) {

	}

	@Override
	public void playerRespawn(final Player player) {

	}

	@Override
	public boolean playerFight(Player damagee, Player damager) {
		return true;

	}

	@Override
	public void playerDying(Player entity) {

	}

	@Override
	public void entityDied(Entity entity, EntityDeathEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entityHurt(Entity entity, EntityDamageEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void joinGame(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leaveGame(Player player) {
		// TODO Auto-generated method stub

	}

}