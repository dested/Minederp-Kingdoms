package com.minederp.kingdoms;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.CommandContext;

public abstract class Game {

	public abstract void onLoad();

	public abstract boolean canPlayerJoin(Player player);

	public abstract void updatePlayerGamePosition(Player player, Location to);

	public abstract void joinGame(Player player);

	public abstract void leaveGame(Player player);

	public abstract void processCommand(CommandContext args, Player player);

	public abstract boolean blockDestroyed(Block block, Player clickedPlayer);

	public abstract void blockClick(Block block, Player clickedPlayer);

	public abstract void playerRespawn(Player player);

	public abstract void playerDied(Player player);

	public abstract boolean playerFight(Player damagee, Player damager);

}