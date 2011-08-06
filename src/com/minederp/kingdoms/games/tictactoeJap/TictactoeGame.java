package com.minederp.kingdoms.games.tictactoeJap;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftMonster;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.games.Game;
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.util.Helper;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class TictactoeGame extends Game {
	public final KingdomsPlugin kingdomsPlugin;
	private World gameWorld;
	GameLogic logic;
	private Random randomizer = new Random();

	public TictactoeGame(KingdomsPlugin kingdomsPlugin, World world) {
		this.kingdomsPlugin = kingdomsPlugin;
		gameWorld = world;
	}

	@Override
	public boolean canPlayerJoin(Player player) {
		return true;
	}

	@Override
	public void onLoad(GameLogic logic) {
		this.logic = logic;

	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {

	}

	@Override
	public void joinGame(Player player) {

	}

	@Override
	public void processCommand(String header, CommandContext args, Player player) {
		if (!header.equals("tc"))
			return;

		if (args.argsLength() == 0) {

			player.sendMessage("Tic Tac Toe");
			return;
		}
		if (Helper.argEquals(args.getString(0), "StartGame")) {
			Block blockPlayerIsStandingOn = player.getLocation().getBlock();

			return;
		}

		player.sendMessage("Tic Tac Toe Command not found: " + args.getString(0));

	}

	@Override
	public void leaveGame(Player player) {

	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {

		return false;
	}

	@Override
	public boolean blockClick(BlockFace face,Block block, Player clickedPlayer) {

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

		return false;

	}

	@Override
	public void playerDying(Player entity) {

	}

	@Override
	public void entityDied(Entity entity, EntityDeathEvent event) {

	}

	@Override
	public void entityHurt(Entity entity, EntityDamageEvent event) {

	}

	@Override
	public boolean blockPlaced(BlockFace face,Block block, Player player) {
		return false;
	}

	@Override
	public void playerQuit(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerJoin(Player player) {
		// TODO Auto-generated method stub
		
	}}
