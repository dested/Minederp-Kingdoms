package com.minederp.kingdoms.games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.minederp.kingdoms.util.Game;
import com.sk89q.minecraft.util.commands.CommandContext;

public class GameLogic extends Game {

	List<Game> Games = new ArrayList<Game>();

	public void addGame(Game g) {
		Games.add(g);
		g.onLoad();

	}

	@Override
	public void onLoad() {
		for (Game g : Games) {
			g.onLoad();
		}
	}

	@Override
	public boolean canPlayerJoin(Player player) {
		for (Game g : Games)
			if (g.canPlayerJoin(player))
				return true;
		return false;
	}

	@Override
	public void updatePlayerGamePosition(Player player, Location to) {
		for (Game g : Games)
			g.updatePlayerGamePosition(player, to);

	}

	@Override
	public void joinGame(Player player) {
		for (Game g : Games)
			g.joinGame(player);

	}

	@Override
	public void leaveGame(Player player) {
		for (Game g : Games)
			g.leaveGame(player);
	}

	@Override
	public void processCommand(CommandContext args, Player player) {
		for (Game g : Games)
			g.processCommand(args, player);
	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {
		for (Game g : Games)
			if (g.blockDestroyed(block, clickedPlayer))
				return true;
		return false;
	}

	@Override
	public void blockClick(Block block, Player clickedPlayer) {
		for (Game g : Games)
			g.blockClick(block, clickedPlayer);
	}

	@Override
	public void playerRespawn(Player player) {
		for (Game g : Games)
			g.playerRespawn(player);

	}

	@Override
	public void playerDied(Player player) {
		for (Game g : Games)
			g.playerDied(player);
	}

	@Override
	public boolean playerFight(Player damagee, Player damager) {
		for (Game g : Games)
			if (g.playerFight(damagee, damager))
				return true;
		return false;

	}

	@Override
	public void playerDying(Player entity) {
		for (Game g : Games)
			g.playerDying(entity);
	}

}
