package com.minederp.kingdoms.games.bulldozer;

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

public class BulldozerGame extends Game {
	public final KingdomsPlugin kingdomsPlugin;

	private World gameWorld;
	GameLogic logic;
	private Random randomizer = new Random();
	private List<BulldozerPiece> pieces;

	

	public BulldozerGame(KingdomsPlugin kingdomsPlugin, World world) {
		this.kingdomsPlugin = kingdomsPlugin;
		gameWorld = world;
		pieces = new ArrayList<BulldozerPiece>();
		kingdomsPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(kingdomsPlugin, new Runnable() {
			@Override
			public void run() {

				for (BulldozerPiece doze : pieces) {
					doze.tick();
				}
			}
		}, 1, 10);
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
		if (!header.equals("b"))
			return;

		if (args.argsLength() == 0) {

			player.sendMessage("Bulldozer");
			return;
		}
		if (Helper.argEquals(args.getString(0), "CreateDoze") && args.length() == 4) {
			Block jc2;
			Block jc = player.getLocation().getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH);
			for (int i = 0; i < args.getInteger(1); i++) {
				jc2 = jc;
				for (int j = 0; j < args.getInteger(2); j++) {
					BulldozerPiece bd;
					pieces.add(bd = new BulldozerPiece(this, jc2));
					jc2 = jc2.getRelative(Helper.faceGetRight(bd.facing));
					jc2 = jc2.getRelative(Helper.faceGetRight(bd.facing));
				}
				jc = jc.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP);
			}
			return;

		}

		player.sendMessage("Command not found: " + args.getString(0));

	}

	@Override
	public void leaveGame(Player player) {

	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {

		return false;
	}

	@Override
	public boolean blockClick(Block block, Player clickedPlayer) {

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
	public boolean blockPlaced(Block block, Player player) {
		// TODO Auto-generated method stub
		return false;
	}
}
