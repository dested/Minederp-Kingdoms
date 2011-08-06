package com.minederp.kingdoms.games.construction;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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

public class ConstructionGame extends Game {
	private final KingdomsPlugin kingdomsPlugin;
	List<ConstructionJob> jobs;
	private World gameWorld;
	GameLogic logic;
	private Random randomizer = new Random();

	public ConstructionGame(KingdomsPlugin kingdomsPlugin, World world) {
		this.kingdomsPlugin = kingdomsPlugin;
		gameWorld = world;

		jobs = new ArrayList<ConstructionJob>();
		kingdomsPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(kingdomsPlugin, new Runnable() {
			@Override
			public void run() {
				for (ConstructionJob job : jobs) {
					try {
						job.jobTick();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}, 1, 5);
	}

	@Override
	public boolean canPlayerJoin(Player player) {
		return true;
	}

	@Override
	public void onLoad(GameLogic logic) {
		this.logic = logic;
		ConstructionModel mod;
		models.put("wall", mod = new ConstructionModel("wall"));
		boolean[][][] ml = new boolean[12][17][9];
		for (int x = 0; x < 12; x++) {
			for (int y = 0; y < 17; y++) {
				for (int z = 0; z < 9; z++) {
					if (y < 14) {
						if (x == 0 || x == 11 || x == 1 || x == 10) {
							if (!(z == 5 || z == 6) && y < 5)
								ml[x][y][z] = true;
						}
						if (z == 0 || z == 8) {
							ml[x][y][z] = true;
						}
					} else {
						ml[x][y][z] = true;
					}

				}
			}
		}
		mod.addModelChunk(new ConstructionChunk(ml, 12, 17, 9));
	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {

	}

	@Override
	public void joinGame(Player player) {

	}

	private HashMap<String, ConstructionModel> models = new HashMap<String, ConstructionModel>();

	@Override
	public void processCommand(String header, CommandContext args, Player player) {
		if (!header.equals("c"))
			return;

		if (args.argsLength() == 0) {

			player.sendMessage("Construction");
			return;
		}
		if (Helper.argEquals(args.getString(0), "CreateJob") && args.length() == 3) {
			jobs.add(new ConstructionJob(this, args.getString(1)));
			player.sendMessage("You have created the job " + args.getString(1));
			return;
		}
		if (Helper.argEquals(args.getString(0), "AddChestToJob") && args.length() == 3) {
			for (ConstructionJob job : jobs) {
				if (job.getJobName().toLowerCase().equals(args.getString(1))) {
					job.addingChestPlayer = player;
					job.addingCranePlayer = null;
					job.addingTaskPlayer = null;
					player.sendMessage("The next chest block you click will be defined as a chest.");
					return;
				}
			}
			player.sendMessage("Cannot find the job: " + args.getString(1));
			return;
		}
		if (Helper.argEquals(args.getString(0), "AddCraneToJob") && args.length() == 3) {
			for (ConstructionJob job : jobs) {
				if (job.getJobName().toLowerCase().equals(args.getString(1))) {
					player.sendMessage("The next gold block you click will be defined as a crane.");
					job.addingCranePlayer = player;
					job.addingChestPlayer = null;
					job.addingTaskPlayer = null;

					return;
				}
			}
			player.sendMessage("Cannot find the job: " + args.getString(1));
			return;
		}
		if (Helper.argEquals(args.getString(0), "AddTaskToJob") && args.length() == 4) {
			for (ConstructionJob job : jobs) {
				if (job.getJobName().toLowerCase().equals(args.getString(1))) {

					ConstructionModel m;
					if ((m = models.get(args.getString(2).toLowerCase())) == null) {
						player.sendMessage("Model is not defined.");
						return;
					}

					job.addingTaskPlayer = player;
					job.addingTaskPlayerModel = m;
					player.sendMessage("The next block you click will be defined as the center of the task block..");
					job.addingCranePlayer = null;
					job.addingChestPlayer = null;
					return;
				}
			}
			player.sendMessage("Cannot find the job: " + args.getString(1));
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
	public boolean blockClick(BlockFace face,Block block, Player clickedPlayer) {
		for (ConstructionJob job : jobs) {
			if (job.blockClick(block, clickedPlayer)) {
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
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void playerQuit(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerJoin(Player player) {
		// TODO Auto-generated method stub
		
	}
}
