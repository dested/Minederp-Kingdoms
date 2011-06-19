package com.minederp.kingdoms.games.zombies;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.util.Game;
import com.sk89q.minecraft.util.commands.CommandContext;

public class ZombiesGame extends Game {

	public ZombiesGame(KingdomsPlugin kingdomsPlugin) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canPlayerJoin(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updatePlayerGamePosition(Player player, Location to) {
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

	@Override
	public void processCommand(CommandContext args, Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void blockClick(Block block, Player clickedPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerRespawn(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerDied(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean playerFight(Player damagee, Player damager) {
		// TODO Auto-generated method stub
		return false;
	}/*
	public boolean gameIsHappening;
	public Player currentZombie;
	private int waitTime = 7; // in seconds
	private final KingdomsPlugin kingdomsPlugin;
	HashMap<String, String> playerInventories = new HashMap<String, String>();

	public ZombiesGame(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin; 
	}

	@Override
	public boolean canPlayerJoin(Player player) {
		return true;
	}

	@Override
	public void onLoad() {
 
	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {

		if (!gameIsHappening)
			return;
 
	}
  
	public void messagePlayerInList(List<Player> players, String message) {
		for (Player player : players) {
			player.sendMessage(message);
		}
	}

	@Override
	public void joinGame(Player player) {
		 
		player.sendMessage(ChatColor.YELLOW + " You have Ar " + playingTeam.name);
		playerInventories.put(player.getName(), kingdomsPlugin.inventoryStasher.AddInventory(player.getInventory(), true));
		messagePlayersInGame(ChatColor.AQUA + player.getDisplayName() + " has joined team " + playingTeam.name);
		playingTeam.players.add(player);
		player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 0, playingTeam.color.getData()));
		if (containsPlayers(playersInTheArea, player)) {
			playersInTheArea.remove(player);
		}

		player.teleport(playingTeam.spawn);
	}

	private Player actingPlayer;

	@Override
	public void processCommand(CommandContext args, Player player) {
		if (args.argsLength() == 0 || args.getString(0).toLowerCase().equals("getteams")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.LIGHT_PURPLE + "Capture the flag teams");
			for (ZombieTeam team : teams) {
				sb.append(ChatColor.GOLD + " " + team.name + " " + team.points + " Points (" + team.players.size() + ")");
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
			messagePlayerInList(player.getWorld().getPlayers(), ChatColor.LIGHT_PURPLE + "Capture the flag has started.");
			gameIsHappening = true;
			for (Player wPlayer : player.getWorld().getPlayers()) {
				Location to = wPlayer.getLocation();

				if (gameLocation.x < to.getBlockX() && gameLocation.x + gameLocation.width > to.getBlockX() && gameLocation.y < to.getBlockZ()
						&& gameLocation.y + gameLocation.height > to.getBlockZ()) {

					wPlayer.sendMessage(ChatColor.AQUA + " Type \"/ctf join\" to join the capture the flag game.");
					playersInTheArea.add(wPlayer);
					return;
				}
			}
			return;
		}
		if (args.getString(0).toLowerCase().equals("endgame") || args.getString(0).toLowerCase().equals("eg")) {
			gameIsHappening = false;
			messagePlayerInList(player.getWorld().getPlayers(), ChatColor.LIGHT_PURPLE + "Capture the flag has ended.");
			for (ZombieTeam team : teams) {

				for (Player pl : team.players) {
					kingdomsPlugin.inventoryStasher.RefillInventory(playerInventories.get(pl.getName()), pl.getInventory());
				}

				team.players.clear();
				team.points = 0;
			}

			return;
		}
		if (args.getString(0).toLowerCase().equals("setrectangle") || args.getString(0).toLowerCase().equals("sr")) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "The next block you click will be the top left of the rectangle.");
			drawingRectangle = 1;
			actingPlayer = player;
			return;
		}
		for (ZombieTeam team : teams) {
			if (args.getString(0).toLowerCase().equals(team.name.toLowerCase())
					|| args.getString(0).toLowerCase().startsWith(Character.toString(team.name.toLowerCase().charAt(0)))) {

				if (args.getString(1).toLowerCase().equals("setflag") || args.getString(1).toLowerCase().equals("sf")) {
					player.sendMessage(ChatColor.GOLD + "The next block you click will be the flag. It will be converted to a flag.");
					team.setSpawn = false;
					team.setFlag = true;
					actingPlayer = player;

					return;
				}

				if (args.getString(1).toLowerCase().equals("setspawn") || args.getString(1).toLowerCase().equals("ss")) {
					player.sendMessage(ChatColor.GOLD + "The next block you click will be the Spawn. It will be converted to Gold.");
					team.setSpawn = true;
					team.setFlag = false;
					actingPlayer = player;

					return;
				}

				StringBuilder sb = new StringBuilder(); 
				sb.append(ChatColor.LIGHT_PURPLE + team.name + " Information:   " + team.points + " Points");
				for (Player cplayer : team.players) {
					sb.append(ChatColor.AQUA + " " + cplayer.getDisplayName());
				}
				player.sendMessage(sb.toString());
				return;
			}
		}
	}

	@Override
	public void leaveGame(Player player) {

		for (ZombieTeam teamc : teams) {
			if (containsPlayers(teamc.players, player))
				return;
			teamc.players.remove(player);

			for (ZombieTeam team : teams) {
				if (team.withFlag != null && team.withFlag.getName().equals(player.getName())) {
					drawFlag(team.flag, team.color, true);
					player.getInventory().remove(team.flagItem);
					team.withFlag = null;
				}
			}
			kingdomsPlugin.inventoryStasher.RefillInventory(playerInventories.get(player.getName()), player.getInventory());

			player.sendMessage(ChatColor.GREEN + "You Has left the CTF Game");

			messagePlayersInGame(ChatColor.GREEN + player.getDisplayName() + " Has left the team " + teamc.name);

		}

	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {
		if (block.getType() != Material.FENCE) {
			if (gameLocation.x <= block.getX() && gameLocation.x + gameLocation.width >= block.getX() && gameLocation.y <= block.getZ()
					&& gameLocation.y + gameLocation.height >= block.getZ()) {
				return false;
			}

			return true;
		}

		for (ZombieTeam team : teams) {

			if (team.withFlag != null || team.flag == null || team.spawn == null || block.getLocation() == null)
				continue;

			Location blockLoc = block.getLocation();

			if (blockLoc.getZ() == team.flag.getZ() && blockLoc.getX() == team.flag.getX()) {
				if (containsPlayers(team.players, clickedPlayer)) {
					clickedPlayer.sendMessage(ChatColor.RED + "You cannot capture your own flag");
					return false;
				}

				for (ZombieTeam fteam : teams) {
					if (!fteam.name.equals(team.name.toLowerCase())) {
						if (containsPlayers(fteam.players, clickedPlayer)) {

							drawFlag(team.flag, team.color, false);

							team.withFlag = clickedPlayer;
							messagePlayersInGame(clickedPlayer.getDisplayName() + " has captured the " + team.name + " flag.");

							if (clickedPlayer.getInventory().getItem(0).getType() == Material.AIR) {
								clickedPlayer.getInventory().setItem(0,
										team.flagItem = new ItemStack(Material.WOOL, 1, (short) 1, team.color.getData()));
							} else {
								ItemStack itm;
								clickedPlayer.getInventory().remove(itm = clickedPlayer.getInventory().getItem(0));
								clickedPlayer.getInventory().setItem(0,
										team.flagItem = new ItemStack(Material.WOOL, 1, (short) 1, team.color.getData()));
								clickedPlayer.getInventory().addItem(itm);
							}

							return false;
						}
					}
				}
			}

		}
		return false;// never destory the pole

	}

	@Override
	public void blockClick(Block block, Player clickedPlayer) {

		if (drawingRectangle > 0 && actingPlayer != null && clickedPlayer.getName().equals(actingPlayer.getName())) {
			drawRectangleLogic(block, clickedPlayer);

			return;
		}

		for (ZombieTeam team : teams) {
			if (team.setFlag && actingPlayer != null && clickedPlayer.getName().equals(actingPlayer.getName())) {
				actingPlayer = null;

				team.flag = block.getLocation();
				drawFlag(block.getLocation(), team.color, true);

				team.setFlag = false;
				team.setSpawn = false;
				return;
			}

			if (team.setSpawn && actingPlayer != null && clickedPlayer.getName().equals(actingPlayer.getName())) {
				actingPlayer = null;

				block.setType(Material.GOLD_BLOCK);
				block = block.getRelative(BlockFace.UP);
				team.spawn = block.getLocation();

				team.setFlag = false;
				team.setSpawn = false;
				return;
			}
			if (team.flag == null || team.spawn == null || block == null || block.getLocation() == null)
				continue;

			if (block.getRelative(BlockFace.UP).getLocation().equals(team.spawn)) {
				if (!containsPlayers(team.players, clickedPlayer)) {
					clickedPlayer.sendMessage(" This is not your teams spawn.");
				} else {
					for (ZombieTeam fteam : teams) {

						if (fteam.withFlag != null && fteam.withFlag.getName().equals(clickedPlayer.getName())) {

							team.points++;

							drawFlag(fteam.flag, fteam.color, true);

							clickedPlayer.getInventory().removeItem(fteam.flagItem);
							fteam.flagItem = null;
							clickedPlayer.sendMessage(" You have captured " + fteam.name + "'s flag.");
							messagePlayersInGame(clickedPlayer.getDisplayName() + " has captured " + fteam.name + "'s flag.  (" + team.points
									+ " points)");

							fteam.withFlag = null;
							return;
						}
					}

					clickedPlayer.sendMessage(" You do not have the flag.");
				}
			}
		}
	}

	private void drawRectangleLogic(Block block, Player clickedPlayer) {
		switch (drawingRectangle) {
		case 1:
			clickedPlayer.sendMessage(ChatColor.AQUA + "Please clicked the far corner of the rectangle.");
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

			if (gameLocation.x > gameLocation.x + gameLocation.width)
				for (int x = gameLocation.x; x > gameLocation.x + gameLocation.width; x--) {

					if (door++ % 15 == 7) {

						block.getWorld().getBlockAt(x, block.getY(), gameLocation.y).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY(), gameLocation.y + gameLocation.height).setType(Material.AIR);

						block.getWorld().getBlockAt(x, block.getY() + 1, gameLocation.y).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY() + 1, gameLocation.y + gameLocation.height).setType(Material.AIR);

						for (int y = block.getY() + 2; y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y).setType(Material.GLASS);
							block.getWorld().getBlockAt(x, y, gameLocation.y + gameLocation.height).setType(Material.GLASS);
						}

					} else {
						for (int y = block.getY(); y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y).setType(Material.GLASS);
							block.getWorld().getBlockAt(x, y, gameLocation.y + gameLocation.height).setType(Material.GLASS);
						}
					}

				}
			else
				for (int x = gameLocation.x; x < gameLocation.x + gameLocation.width; x++) {

					if (door++ % 15 == 7) {

						block.getWorld().getBlockAt(x, block.getY(), gameLocation.y).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY(), gameLocation.y + gameLocation.height).setType(Material.AIR);

						block.getWorld().getBlockAt(x, block.getY() + 1, gameLocation.y).setType(Material.AIR);
						block.getWorld().getBlockAt(x, block.getY() + 1, gameLocation.y + gameLocation.height).setType(Material.AIR);

						for (int y = block.getY() + 2; y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y).setType(Material.GLASS);
							block.getWorld().getBlockAt(x, y, gameLocation.y + gameLocation.height).setType(Material.GLASS);
						}

					} else {
						for (int y = block.getY(); y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y).setType(Material.GLASS);
							block.getWorld().getBlockAt(x, y, gameLocation.y + gameLocation.height).setType(Material.GLASS);
						}
					}

				}

			if (gameLocation.y > gameLocation.y + gameLocation.height)
				for (int z = gameLocation.y; z > gameLocation.y + gameLocation.height; z--) {

					if (door++ % 15 == 7) {

						block.getWorld().getBlockAt(gameLocation.x, block.getY(), z).setType(Material.AIR);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY(), z).setType(Material.AIR);

						block.getWorld().getBlockAt(gameLocation.x, block.getY() + 1, z).setType(Material.AIR);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY() + 1, z).setType(Material.AIR);

						for (int y = block.getY() + 2; y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z).setType(Material.GLASS);
							block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, y, z).setType(Material.GLASS);
						}

					} else {
						for (int y = block.getY(); y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z).setType(Material.GLASS);
							block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, y, z).setType(Material.GLASS);
						}
					}
				}
			else
				for (int z = gameLocation.y; z < gameLocation.y + gameLocation.height; z++) {
					if (door++ % 15 == 7) {

						block.getWorld().getBlockAt(gameLocation.x, block.getY(), z).setType(Material.AIR);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY(), z).setType(Material.AIR);

						block.getWorld().getBlockAt(gameLocation.x, block.getY() + 1, z).setType(Material.AIR);
						block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, block.getY() + 1, z).setType(Material.AIR);

						for (int y = block.getY() + 2; y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z).setType(Material.GLASS);
							block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, y, z).setType(Material.GLASS);
						}

					} else {
						for (int y = block.getY(); y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z).setType(Material.GLASS);
							block.getWorld().getBlockAt(gameLocation.x + gameLocation.width, y, z).setType(Material.GLASS);
						}
					}
				}

			break;
		}
	}

	private void drawFlag(Location center, DyeColor color, boolean drawFlag) {
		int rad = 2;

		Block block = center.getWorld().getBlockAt(center);

		int centerX = center.getBlockX();
		int centerZ = center.getBlockZ();

		for (int i = centerX - (rad); i < centerX + rad + 1; i++) {
			for (int a = centerZ - (rad); a < centerZ + rad + 1; a++) {
				block = center.getWorld().getBlockAt(i, center.getBlockY(), a);
				block.setTypeIdAndData(Material.WOOL.getId(), color.getData(), false);
			}
		}

		int poleSize = 9;
		for (int i = 0; i < poleSize; i++) {
			block = center.getWorld().getBlockAt(center.getBlockX(), center.getBlockY() + 1 + i, center.getBlockZ());
			block.setType(Material.FENCE);
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 1; j < 5; j++) {
				block = center.getWorld().getBlockAt(center.getBlockX() + j, center.getBlockY() + poleSize - i, center.getBlockZ());

				if (drawFlag)

					block.setTypeIdAndData(Material.WOOL.getId(), color.getData(), false);
				else
					block.setType(Material.AIR);
			}
		}

	}

	Timer t = new Timer();

	@Override
	public void playerDied(final Player player) {
		for (final ZombieTeam fteam : teams) {
			if (fteam.withFlag != null && fteam.withFlag.getName().equals(player.getName())) {
				drawFlag(fteam.flag, fteam.color, true);

				messagePlayersInGame(player.getDisplayName() + " has died. The " + fteam.name + " flag has been returned.");
				fteam.withFlag = null;
				return;
			}
		}
	}

	@Override
	public void playerRespawn(final Player player) {
		for (final ZombieTeam fteam : teams) {
			if (containsPlayers(fteam.players, player)) {
				player.sendMessage("You will be ready to play in " + waitTime + " seconds.");
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						player.teleport(fteam.spawn);
						player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 0, fteam.color.getData()));
						player.sendMessage("You have rejoined the war.");
						messagePlayersInGame(ChatColor.GREEN + player.getDisplayName() + " has rejoined the war.");
					}

				}, waitTime * 1000);
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						player.teleport(fteam.spawn);
					}

				}, 100);

				return;
			}
		}
	}

	@Override
	public boolean playerFight(Player damagee, Player damager) {
		for (ZombieTeam team : teams) {
			if (containsPlayers(team.players, damagee)) {
				if (containsPlayers(team.players, damager)) {
					damager.sendMessage(ChatColor.RED + "Cannot fight members of your team");
					return false;
				}
				return true;
			}

		}
		return true;

	}*/
}