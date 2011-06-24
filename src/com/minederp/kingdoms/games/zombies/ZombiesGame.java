package com.minederp.kingdoms.games.zombies;

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

public class ZombiesGame extends Game {
	public boolean gameIsHappening;

	private List<Player> waitingInTheArea;
	private List<ZombiePlayer> playingInTheArea;

	private int waitTime = 7; // in seconds
	private Rectangle gameLocation = new Rectangle();
	public int drawingRectangle;
	private final KingdomsPlugin kingdomsPlugin;
	HashMap<String, String> playerInventories = new HashMap<String, String>();
	private Player actingPlayer;
	List<Item> blocksForReprint = new ArrayList<Item>();

	List<Location> playerSpawns = new ArrayList<Location>();
	List<Location> zombieSpawns = new ArrayList<Location>();

	private boolean setHumanSpawn;

	private boolean setZombieSpawn;


	private Runnable zombieMaker = new Runnable() {

		@Override
		public void run() {
			if (playingInTheArea.size() == 0) {

				return;
			}

			int jc = logic.randomizer.nextInt(4 * playingInTheArea.size());

			for (int i = 0; i < jc; i++) {

				CreatureType t;
				switch (logic.randomizer.nextInt(3)) {
				case 0:
					t = CreatureType.SKELETON;
					break;
				case 1:
					t = CreatureType.ZOMBIE;
					break;
				case 2:
					t = CreatureType.PIG_ZOMBIE;
					break;
				default:
					t = CreatureType.CREEPER;
					break;
				}

				Location j = zombieSpawns.get(logic.randomizer.nextInt(zombieSpawns.size()));
				zombieEntities.add(gameWorld.spawnCreature(new Location(j.getWorld(), j.getBlockX(), j.getBlockY() + 1, j.getBlockZ()), t)
						.getEntityId());
			}
		}

	};

	private World gameWorld;

	private GameLogic logic;

	public ZombiesGame(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;
	}

	@Override
	public boolean canPlayerJoin(Player player) {
		return true;
	}

	@Override
	public void onLoad(GameLogic logic) {
		this.logic = logic;
		waitingInTheArea = new ArrayList<Player>();
		playingInTheArea = new ArrayList<ZombiePlayer>();
	}

	public class Item {
		public Item(int x2, int y2, int z2, int typeId, byte data) {
			X = x2;
			Y = y2;
			Z = z2;
			Data = data;
			Type = typeId;
		}

		int Type;
		byte Data;
		int X;
		int Y;
		int Z;
	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {
		if (blocksForReprint.size() > 0) {
			World w = movingPlayer.getWorld();
			for (Item it : blocksForReprint) {
				w.getBlockAt(it.X, it.Y, it.Z).setTypeIdAndData(it.Type, it.Data, false);
			}

			blocksForReprint.clear();
		}
		if (drawingRectangle == 2 && actingPlayer != null && movingPlayer.getName().equals(actingPlayer.getName())) {

			Rectangle mRectangle = new Rectangle(gameLocation);

			if (movingPlayer.getLocation().getBlockX() < mRectangle.x) {
				mRectangle.width = mRectangle.x - movingPlayer.getLocation().getBlockX();
				mRectangle.x = movingPlayer.getLocation().getBlockX();
			} else
				mRectangle.width = movingPlayer.getLocation().getBlockX() - mRectangle.x;

			if (movingPlayer.getLocation().getBlockZ() < mRectangle.y) {
				mRectangle.height = mRectangle.y - movingPlayer.getLocation().getBlockZ();
				mRectangle.y = movingPlayer.getLocation().getBlockZ();
			} else
				mRectangle.height = movingPlayer.getLocation().getBlockZ() - mRectangle.y;

			int door = 0;
			int y = to.getBlockY() - 1;
			Block bc;
			if (mRectangle.x > mRectangle.x + mRectangle.width)
				for (int x = mRectangle.x; x >= mRectangle.x + mRectangle.width; x--) {

					blocksForReprint.add(new Item(x, y, mRectangle.y, (bc = movingPlayer.getWorld().getBlockAt(x, y, mRectangle.y)).getTypeId(), bc
							.getData()));
					bc.setType(Material.DIAMOND_BLOCK);
					blocksForReprint.add(new Item(x, y, mRectangle.y + mRectangle.height, (bc = movingPlayer.getWorld().getBlockAt(x, y,
							mRectangle.y + mRectangle.height)).getTypeId(), bc.getData()));
					bc.setType(Material.DIAMOND_BLOCK);

				}

			else
				for (int x = mRectangle.x; x <= mRectangle.x + mRectangle.width; x++) {
					blocksForReprint.add(new Item(x, y, mRectangle.y, (bc = movingPlayer.getWorld().getBlockAt(x, y, mRectangle.y)).getTypeId(), bc
							.getData()));
					bc.setType(Material.DIAMOND_BLOCK);
					blocksForReprint.add(new Item(x, y, mRectangle.y + mRectangle.height, (bc = movingPlayer.getWorld().getBlockAt(x, y,
							mRectangle.y + mRectangle.height)).getTypeId(), bc.getData()));
					bc.setType(Material.DIAMOND_BLOCK);

				}

			if (mRectangle.y > mRectangle.y + mRectangle.height)
				for (int z = mRectangle.y; z >= mRectangle.y + mRectangle.height; z--) {

					blocksForReprint.add(new Item(mRectangle.x, y, z, (bc = movingPlayer.getWorld().getBlockAt(mRectangle.x, y, z)).getTypeId(), bc
							.getData()));
					bc.setType(Material.DIAMOND_BLOCK);
					blocksForReprint.add(new Item(mRectangle.x + mRectangle.width, y, z, (bc = movingPlayer.getWorld().getBlockAt(
							mRectangle.x + mRectangle.width, y, z)).getTypeId(), bc.getData()));
					bc.setType(Material.DIAMOND_BLOCK);

				}
			else
				for (int z = mRectangle.y; z <= mRectangle.y + mRectangle.height; z++) {

					blocksForReprint.add(new Item(mRectangle.x, y, z, (bc = movingPlayer.getWorld().getBlockAt(mRectangle.x, y, z)).getTypeId(), bc
							.getData()));
					bc.setType(Material.DIAMOND_BLOCK);
					blocksForReprint.add(new Item(mRectangle.x + mRectangle.width, y, z, (bc = movingPlayer.getWorld().getBlockAt(
							mRectangle.x + mRectangle.width, y, z)).getTypeId(), bc.getData()));
					bc.setType(Material.DIAMOND_BLOCK);

				}

		}

		if (!gameIsHappening)
			return;

		boolean inGame = false;
		if (myContainsPlayers(playingInTheArea, movingPlayer)) {
			inGame = true;
		}

		if (gameLocation.x < to.getBlockX() && gameLocation.x + gameLocation.width > to.getBlockX() && gameLocation.y < to.getBlockZ()
				&& gameLocation.y + gameLocation.height > to.getBlockZ()) {

			if (inGame) {
				return;
			}

			if (Helper.containsPlayers(waitingInTheArea, movingPlayer)) {
				return;
			}

			movingPlayer.sendMessage(ChatColor.AQUA + " Type \"/z join\" to join the Zombies .");
			waitingInTheArea.add(movingPlayer);
			return;
		}

		if (inGame) {
			leaveGame(movingPlayer);
		} else {
			waitingInTheArea.remove(movingPlayer);
		}
	}

	private boolean myContainsPlayers(List<ZombiePlayer> playingInTheArea2, Player movingPlayer) {
		for (ZombiePlayer player : playingInTheArea2) {
			if (player.player.getName().equals(movingPlayer.getName())) {
				return true;
			}
		}
		return false;
	}

	public void messagePlayersInGame(String message) {
		for (ZombiePlayer player : playingInTheArea) {
			player.player.sendMessage(message);
		}
	}

	@Override
	public void joinGame(Player player) {

		if (myContainsPlayers(playingInTheArea, player))
			return;

		player.sendMessage(ChatColor.YELLOW + " You have joined Zombies");
		playerInventories.put(player.getName(), kingdomsPlugin.inventoryStasher.AddInventory(player.getInventory(), true));

		messagePlayersInGame(ChatColor.AQUA + player.getDisplayName() + " has joined Zombies ");
		playingInTheArea.add(new ZombiePlayer(player));

		player.getInventory().setHelmet(new ItemStack(Material.OBSIDIAN, 1, (short) 0, (byte) 0));

		player.getInventory().addItem(new ItemStack(Material.ARROW, 100, (short) 0, (byte) 0));
		player.getInventory().addItem(new ItemStack(Material.BOW, 1, (short) 0, (byte) 0));

		if (Helper.containsPlayers(waitingInTheArea, player)) {
			waitingInTheArea.remove(player);
		}

		player.teleport(getPlayerSpawn());
	}

	private Location getPlayerSpawn() {
		if (playerSpawns.size() == 0)
			return null;
		return playerSpawns.get(logic.randomizer.nextInt(playerSpawns.size()));
	}

	private Timer respawn = new Timer();

	private int zombieMaking;

	@Override
	public void processCommand(String header, CommandContext args, Player player) {
		if (!header.equals("z"))
			return;

		if (args.argsLength() == 0 || args.getString(0).toLowerCase().equals("getplayers")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.LIGHT_PURPLE + "Zombies players:");
			for (ZombiePlayer c : playingInTheArea) {
				sb.append(ChatColor.GOLD + " " + c.player.getDisplayName() + " " + c.Points + " Points\r\n");
			}
			player.sendMessage(sb.toString());
			return;
		}

		if (args.getString(0).toLowerCase().equals("join")) {
			joinGame(player);
			return;
		}
		if (args.getString(0).toLowerCase().equals("leave")) {
			leaveGame(player);
			return;
		}

		if (args.getString(0).toLowerCase().equals("startgame") || args.getString(0).toLowerCase().equals("sg")) {
			Helper.messagePlayerInList(player.getWorld().getPlayers(), ChatColor.LIGHT_PURPLE + "Zombies has begun.");
			gameIsHappening = true;

			gameWorld = player.getWorld();

			zombieMaking = kingdomsPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(kingdomsPlugin, zombieMaker, 10, 160);

			for (Player wPlayer : player.getWorld().getPlayers()) {
				Location to = wPlayer.getLocation();

				if (gameLocation.x < to.getBlockX() && gameLocation.x + gameLocation.width > to.getBlockX() && gameLocation.y < to.getBlockZ()
						&& gameLocation.y + gameLocation.height > to.getBlockZ()) {

					wPlayer.sendMessage(ChatColor.AQUA + " Type \"/zombies join\" to join the Zombies.");
					waitingInTheArea.add(wPlayer);
					return;
				}
			}
			return;
		}
		if (args.getString(0).toLowerCase().equals("endgame") || args.getString(0).toLowerCase().equals("eg")) {
			gameIsHappening = false;
			Helper.messagePlayerInList(player.getWorld().getPlayers(), ChatColor.LIGHT_PURPLE + "Zombies has ended.");

			kingdomsPlugin.getServer().getScheduler().cancelTask(zombieMaking);
			for (ZombiePlayer pl : playingInTheArea) {
				kingdomsPlugin.inventoryStasher.RefillInventory(playerInventories.get(pl.player.getName()), pl.player.getInventory());
			}
			playingInTheArea.clear();

			return;
		}

		if (args.getString(0).toLowerCase().equals("setrectangle") || args.getString(0).toLowerCase().equals("sr")) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "The next block you click will be the top left of the rectangle.");
			drawingRectangle = 1;
			actingPlayer = player;
			return;
		}

		if (args.getString(0).toLowerCase().equals("sethuman") || args.getString(0).toLowerCase().equals("sh")) {
			player.sendMessage(ChatColor.GOLD + "The next block you click will be a Human Spawn. It will be converted to Iron.");
			setHumanSpawn = true;
			setZombieSpawn = false;
			actingPlayer = player;

			return;
		}

		if (args.getString(0).toLowerCase().equals("setzombie") || args.getString(0).toLowerCase().equals("sz")) {
			player.sendMessage(ChatColor.GOLD + "The next block you click will be a Zombie Spawn. It will be converted to Gold.");
			setHumanSpawn = false;
			setZombieSpawn = true;
			actingPlayer = player;

			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.LIGHT_PURPLE + "Zombies players:");
		for (ZombiePlayer c : playingInTheArea) {
			sb.append(ChatColor.GOLD + " " + c.player.getDisplayName() + " " + c.Points + " Points\r\n");
		}
		player.sendMessage(sb.toString());

	}

	@Override
	public void leaveGame(Player player) {

		if (!myContainsPlayers(playingInTheArea, player))
			return;

		for (ZombiePlayer c : playingInTheArea) {
			if (c.player.getName().equals(player.getName())) {

				playingInTheArea.remove(c);
				break;
			}
		}

		kingdomsPlugin.inventoryStasher.RefillInventory(playerInventories.get(player.getName()), player.getInventory());

		player.sendMessage(ChatColor.GREEN + "You have left the Zombies Game");

		messagePlayersInGame(ChatColor.GREEN + player.getDisplayName() + " Has left the Zombies game");

	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {
		if (gameIsHappening) {
			if (gameLocation.x <= block.getX() && gameLocation.x + gameLocation.width >= block.getX() && gameLocation.y <= block.getZ()
					&& gameLocation.y + gameLocation.height >= block.getZ()) {
				return true;
			}

		}
		return false;
	}

	@Override
	public boolean blockClick(Block block, Player clickedPlayer) {
		if (actingPlayer != null && clickedPlayer.getName().equals(actingPlayer.getName())) {
			if (drawingRectangle > 0) {
				drawRectangleLogic(block, clickedPlayer);
				return true;
			}
			if (setHumanSpawn) {
				playerSpawns.add(block.getRelative(BlockFace.UP).getLocation());
				block.setType(Material.IRON_BLOCK);
				setHumanSpawn = false;
				setZombieSpawn = false;
				return true;
			}
			if (setZombieSpawn) {
				zombieSpawns.add(block.getRelative(BlockFace.UP).getLocation());
				block.setType(Material.GOLD_BLOCK);
				setHumanSpawn = false;
				setZombieSpawn = false;
				return true;
			}

		}
		return false;

	}

	private void drawRectangleLogic(Block block, Player clickedPlayer) {
		switch (drawingRectangle) {
		case 1:
			clickedPlayer.sendMessage(ChatColor.AQUA + "Now please click the far corner of the rectangle.");
			gameLocation.x = block.getX();
			gameLocation.y = block.getZ();
			drawingRectangle = 2;
			break;
		case 2:
			actingPlayer = null;
			drawingRectangle = 0;
			clickedPlayer.sendMessage(ChatColor.AQUA + "You have completed the rectangle.");

			if (block.getX() < gameLocation.x) {
				gameLocation.width = gameLocation.x - block.getX();
				gameLocation.x = block.getX();
			} else
				gameLocation.width = block.getX() - gameLocation.x;

			if (block.getZ() < gameLocation.y) {
				gameLocation.height = gameLocation.y - block.getZ();
				gameLocation.y = block.getZ();
			} else
				gameLocation.height = block.getZ() - gameLocation.y;

			int door = 0;
			int wallHeight = 15;
			if (gameLocation.x > gameLocation.x + gameLocation.width)
				for (int x = gameLocation.x; x >= gameLocation.x + gameLocation.width; x--) {

					if (door++ % 15 == 7) {

						block.getWorld().getBlockAt(x, block.getY(), gameLocation.y).setType(Material.GLOWSTONE);
						block.getWorld().getBlockAt(x, block.getY(), gameLocation.y + gameLocation.height).setType(Material.GLOWSTONE);

						block.getWorld().getBlockAt(x, block.getY() + 1, gameLocation.y).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY() + 1, gameLocation.y + gameLocation.height).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY() + 2, gameLocation.y).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY() + 2, gameLocation.y + gameLocation.height).setType(Material.AIR);

						for (int y = block.getY() + 3; y < block.getY() + wallHeight; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y).setType(Material.GLASS);
							block.getWorld().getBlockAt(x, y, gameLocation.y + gameLocation.height).setType(Material.GLASS);
						}

					} else {
						for (int y = block.getY(); y < block.getY() + wallHeight; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y).setType(Material.GLASS);
							block.getWorld().getBlockAt(x, y, gameLocation.y + gameLocation.height).setType(Material.GLASS);
						}
					}

				}
			else
				for (int x = gameLocation.x; x <= gameLocation.x + gameLocation.width; x++) {

					if (door++ % 15 == 7) {

						block.getWorld().getBlockAt(x, block.getY(), gameLocation.y).setType(Material.GLOWSTONE);
						block.getWorld().getBlockAt(x, block.getY(), gameLocation.y + gameLocation.height).setType(Material.GLOWSTONE);

						block.getWorld().getBlockAt(x, block.getY() + 1, gameLocation.y).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY() + 1, gameLocation.y + gameLocation.height).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY() + 2, gameLocation.y).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY() + 2, gameLocation.y + gameLocation.height).setType(Material.AIR);

						for (int y = block.getY() + 3; y < block.getY() + wallHeight; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y).setType(Material.GLASS);
							block.getWorld().getBlockAt(x, y, gameLocation.y + gameLocation.height).setType(Material.GLASS);
						}

					} else {
						for (int y = block.getY(); y < block.getY() + wallHeight; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y).setType(Material.GLASS);
							block.getWorld().getBlockAt(x, y, gameLocation.y + gameLocation.height).setType(Material.GLASS);
						}
					}

				}

			if (gameLocation.y > gameLocation.y + gameLocation.height)
				for (int z = gameLocation.y; z >= gameLocation.y + gameLocation.height; z--) {

					if (door++ % 15 == 7) {

						block.getWorld().getBlockAt(gameLocation.x, block.getY(), z).setType(Material.GLOWSTONE);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY(), z).setType(Material.GLOWSTONE);

						block.getWorld().getBlockAt(gameLocation.x, block.getY() + 1, z).setType(Material.AIR);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY() + 1, z).setType(Material.AIR);

						block.getWorld().getBlockAt(gameLocation.x, block.getY() + 2, z).setType(Material.AIR);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY() + 2, z).setType(Material.AIR);

						for (int y = block.getY() + 3; y < block.getY() + wallHeight; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z).setType(Material.GLASS);
							block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, y, z).setType(Material.GLASS);
						}

					} else {
						for (int y = block.getY(); y < block.getY() + wallHeight; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z).setType(Material.GLASS);
							block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, y, z).setType(Material.GLASS);
						}
					}
				}
			else
				for (int z = gameLocation.y; z <= gameLocation.y + gameLocation.height; z++) {
					if (door++ % 10 == 5) {

						block.getWorld().getBlockAt(gameLocation.x, block.getY(), z).setType(Material.GLOWSTONE);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY(), z).setType(Material.GLOWSTONE);

						block.getWorld().getBlockAt(gameLocation.x, block.getY() + 1, z).setType(Material.AIR);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY() + 1, z).setType(Material.AIR);
						block.getWorld().getBlockAt(gameLocation.x, block.getY() + 2, z).setType(Material.AIR);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY() + 2, z).setType(Material.AIR);

						for (int y = block.getY() + 3; y < block.getY() + wallHeight; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z).setType(Material.GLASS);
							block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, y, z).setType(Material.GLASS);
						}

					} else {
						for (int y = block.getY(); y < block.getY() + wallHeight; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z).setType(Material.GLASS);
							block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, y, z).setType(Material.GLASS);
						}
					}
				}

			break;
		}
	}

	@Override
	public void playerDied(final Player player) {

		if (myContainsPlayers(playingInTheArea, player))
			messagePlayersInGame(player.getDisplayName() + " has died. He will be revived in " + waitTime + " seconds.");

	}

	@Override
	public void playerRespawn(final Player player) {
		final Location mv = getPlayerSpawn();
		if (myContainsPlayers(playingInTheArea, player)) {
			player.sendMessage("You will be ready to play in " + waitTime + " seconds.");
			respawn.schedule(new TimerTask() {
				@Override
				public void run() {
					player.teleport(mv);
					player.getInventory().setHelmet(new ItemStack(Material.OBSIDIAN, 1, (short) 0, (byte) 0));
					player.getInventory().addItem(new ItemStack(Material.ARROW, 100, (short) 0, (byte) 0));
					player.getInventory().addItem(new ItemStack(Material.BOW, 1, (short) 0, (byte) 0));

					player.sendMessage("You have rejoined Zombies.");
					messagePlayersInGame(ChatColor.GREEN + player.getDisplayName() + " has rejoined Zombies.");
				}

			}, waitTime * 1000);
			respawn.schedule(new TimerTask() {
				@Override
				public void run() {
					player.teleport(mv);
				}

			}, 100);
		}

	}

	@Override
	public boolean playerFight(Player damagee, Player damager) {

		if (myContainsPlayers(playingInTheArea, damagee) && myContainsPlayers(playingInTheArea, damager)) {
			damager.sendMessage(ChatColor.RED + "Cannot fight members of Zombies");
			return false;
		}
		return true;

	}

	@Override
	public void playerDying(Player entity) {
		if (myContainsPlayers(playingInTheArea, entity)) {
			entity.getInventory().clear();

		}
	}

	@Override
	public void entityDied(Entity entity, EntityDeathEvent event) {
		if (zombieEntities.contains(entity.getEntityId())) {
			zombieEntities.remove((Object) entity.getEntityId());
			event.getDrops().add(new ItemStack(Material.ARROW, logic.randomizer.nextInt(10)));
		}
	}

	private ArrayList<Integer> zombieEntities = new ArrayList<Integer>();

	@Override
	public void entityHurt(Entity entity, EntityDamageEvent event) {

		if (zombieEntities.contains(entity.getEntityId())) {
			if (event instanceof EntityDamageByEntityEvent) {

				if (((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
					Player player = ((Player) ((EntityDamageByEntityEvent) event).getDamager());
					if (myContainsPlayers(playingInTheArea, player)) {

						for (ZombiePlayer c : playingInTheArea) {
							if (c.player.getName().equals(player.getName())) {
								if (entity instanceof CraftMonster) {
									if (((CraftMonster) entity).getHealth() <= 0) {
										c.Points += 3;
										c.player.sendMessage(ChatColor.RED + "You have killed. Points: " + c.Points);
										messagePlayersInGame(ChatColor.RED + player.getDisplayName() + " has killed. Points: " + c.Points);
										return;
									}
								}

								c.Points++;
								c.player.sendMessage(ChatColor.GREEN + "You have attacked Points: " + c.Points);
								return;
							}
						}

					}
				}
			}
		}
	}

	@Override
	public boolean blockPlaced(Block block, Player player) {
		// TODO Auto-generated method stub
		return false;
	}
}
