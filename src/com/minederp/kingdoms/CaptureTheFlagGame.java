package com.minederp.kingdoms;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sun.security.action.GetLongAction;

import com.sk89q.minecraft.util.commands.CommandContext;

public class CaptureTheFlagGame extends Game {
	public boolean happening;
	public List<CTFTeam> teams;

	public Rectangle gameLocation = new Rectangle();
	public int drawingRectangle;

	public CaptureTheFlagGame() {
		teams = new ArrayList<CTFTeam>();
		teams.add(new CTFTeam("Red", DyeColor.RED));
		teams.add(new CTFTeam("Blue", DyeColor.BLUE));
	}

	@Override
	public boolean canPlayerJoin(Player player) {

		return true;
	}

	@Override
	public void onLoad() {
		triedToAdd = new ArrayList<Player>();
	}

	List<Player> triedToAdd;

	private int waitTime = 7; // in seconds

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {

		if (!happening)
			return;

		boolean inGame = false;
		CTFTeam playingTeam = null;
		for (CTFTeam team : teams) {
			if (comparePlayers(team.players, movingPlayer)) {
				playingTeam = team;
				inGame = true;
			}
		}
		if (gameLocation.x < to.getBlockX()
				&& gameLocation.x + gameLocation.width > to.getBlockX()
				&& gameLocation.y < to.getBlockZ()
				&& gameLocation.y + gameLocation.height > to.getBlockZ()) {

			if (inGame) {

				return;
			}

			if (comparePlayers(triedToAdd, movingPlayer)) {
				return;
			}

			movingPlayer.sendMessage(ChatColor.AQUA
					+ " Type \"/ctf join\" to join the capture the flag game.");
			triedToAdd.add(movingPlayer);
			return;
		}

		if (inGame) {
			playingTeam.players.remove(movingPlayer);

			for (CTFTeam team : teams) {
				if (team.withFlag.getName().equals(movingPlayer.getName())) {
					movingPlayer.getWorld().getBlockAt(team.flag)
							.setType(Material.IRON_BLOCK);
					movingPlayer.getInventory().remove(team.flagItem);
					team.withFlag = null;
				}
			}

			movingPlayer.getInventory().setHelmet(new ItemStack(Material.AIR));

			movingPlayer.sendMessage(ChatColor.GREEN
					+ "You Has left the CTF Game");

			sendAllPlayers(ChatColor.GREEN + movingPlayer.getDisplayName()
					+ " Has left the team " + playingTeam.name);
		} else {
			triedToAdd.remove(movingPlayer);
		}
	}

	public boolean comparePlayers(List<Player> players, Player play) {
		for (Player player : players) {
			if (player.getName().equals(play.getName())) {
				return true;
			}
		}
		return false;
	}

	public void sendAllPlayers(String message) {
		for (CTFTeam team : teams) {
			sendAllGamePlayers(team.players, message);
		}
	}

	public void sendAllGamePlayers(List<Player> players, String message) {
		for (Player player : players) {
			player.sendMessage(message);
		}
	}

	public void joinGame(Player player) {
		int lowestPlayers = Integer.MAX_VALUE;
		CTFTeam playingTeam = null;
		for (CTFTeam team : teams) {
			if (team.players.size() < lowestPlayers) {
				lowestPlayers = team.players.size();
				playingTeam = team;
			}
		}

		player.sendMessage(ChatColor.YELLOW + " You have joined team "
				+ playingTeam.name);

		sendAllPlayers(ChatColor.AQUA + player.getDisplayName()
				+ " has joined team " + playingTeam.name);

		playingTeam.players.add(player);

		player.getInventory().setHelmet(
				new ItemStack(Material.WOOL, 1, (short) 0, playingTeam.color
						.getData()));
		if (comparePlayers(triedToAdd, player)) {
			triedToAdd.remove(player);
		}

		player.teleport(playingTeam.spawn);
	}

	public void processCommand(CommandContext args, Player player) {
		if (args.argsLength() == 0
				|| args.getString(0).toLowerCase().equals("getteams")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.DARK_BLUE + "Capture the flag teams");
			for (CTFTeam team : teams) {
				sb.append(ChatColor.GOLD + " " + team.name + " " + team.points
						+ " Points (" + team.players.size() + ")");
			}
			player.sendMessage(sb.toString());
			return;
		}

		if (args.getString(0).toLowerCase().equals("join")) {
			joinGame(player);
			return;
		}

		if (args.getString(0).toLowerCase().equals("startgame")) {
			sendAllGamePlayers(player.getWorld().getPlayers(), ChatColor.BLUE
					+ "Capture the flag has started.");
			happening = true;
			for (Player wPlayer : player.getWorld().getPlayers()) {
				Location to = wPlayer.getLocation();

				if (gameLocation.x < to.getBlockX()
						&& gameLocation.x + gameLocation.width > to.getBlockX()
						&& gameLocation.y < to.getBlockY()
						&& gameLocation.y + gameLocation.height > to
								.getBlockY()) {

					wPlayer.sendMessage(ChatColor.AQUA
							+ " Type \"/ctf join\" to join the capture the flag game.");
					triedToAdd.add(wPlayer);
					return;
				}
			}
			return;
		}
		if (args.getString(0).toLowerCase().equals("endgame")) {
			happening = false;
			sendAllGamePlayers(player.getWorld().getPlayers(), ChatColor.BLUE
					+ "Capture the flag has ended.");
			for (CTFTeam team : teams) {
				team.players.clear();
				team.points = 0;
			}

			return;
		}
		if (args.getString(0).toLowerCase().equals("setrectangle")
				|| args.getString(0).toLowerCase().equals("sr")) {
			player.sendMessage(ChatColor.BLUE
					+ "The next block you click will be the top left of the rectangle.");
			drawingRectangle = 1;
			return;
		}
		for (CTFTeam team : teams) {
			if (args.getString(0).toLowerCase().equals(team.name.toLowerCase())
					|| args.getString(0)
							.toLowerCase()
							.startsWith(
									Character.toString(team.name.toLowerCase()
											.charAt(0)))) {

				if (args.getString(1).toLowerCase().equals("setflag")
						|| args.getString(1).toLowerCase().equals("sf")) {
					player.sendMessage(ChatColor.GOLD
							+ "The next block you click will be the flag. It will be converted to Iron.");
					team.setSpawn = false;
					team.setFlag = true;
					return;
				}

				if (args.getString(1).toLowerCase().equals("setspawn")
						|| args.getString(1).toLowerCase().equals("ss")) {
					player.sendMessage(ChatColor.GOLD
							+ "The next block you click will be the Spawn. It will be converted to Gold.");
					team.setSpawn = true;
					team.setFlag = false;

					return;
				}

				StringBuilder sb = new StringBuilder();
				sb.append(ChatColor.DARK_BLUE + team.name + " Information:   "
						+ team.points + " Points");
				for (Player cplayer : team.players) {
					sb.append(ChatColor.DARK_AQUA + " "
							+ cplayer.getDisplayName());
				}
				player.sendMessage(sb.toString());
				return;
			}
		}
	}

	public boolean blockDestroyed(Block block, Player clickedPlayer) {
		if (block.getType() != Material.FENCE)
			return true;

		for (CTFTeam team : teams) {

			if (team.withFlag != null || team.flag == null
					|| team.spawn == null || block.getLocation() == null)
				continue;

			Location blockLoc = block.getLocation();

			if (blockLoc.getZ() == team.flag.getZ()
					&& blockLoc.getX() == team.flag.getX()) {
				if (comparePlayers(team.players, clickedPlayer)) {
					clickedPlayer.sendMessage(ChatColor.RED
							+ "You cannot capture your own flag");
					return false;
				}

				for (CTFTeam fteam : teams) {
					if (!fteam.name.equals(team.name.toLowerCase())) {
						if (comparePlayers(fteam.players, clickedPlayer)) {

							drawFlag(team.flag, team.color, false);

							team.withFlag = clickedPlayer;
							sendAllPlayers(clickedPlayer.getDisplayName()
									+ " has captured the " + team.name
									+ " flag.");

							if(clickedPlayer.getInventory().getItem(0).getType()==Material.AIR){
								clickedPlayer.getInventory().setItem(0,
										team.flagItem = new ItemStack(
												Material.WOOL, 1, (short) 1,
												team.color.getData()));
							}else
							{
								ItemStack itm;
								clickedPlayer.getInventory().remove(itm=clickedPlayer.getInventory().getItem(0));
								clickedPlayer.getInventory().setItem(0,
										team.flagItem = new ItemStack(
												Material.WOOL, 1, (short) 1,
												team.color.getData()));
								clickedPlayer.getInventory().addItem(itm);
							}
							
							return true;
						}
					}
				}
			}

		}
		return true;

	}

	public void blockClick(Block block, Player clickedPlayer) {

		if (drawingRectangle > 0) {

			switch (drawingRectangle) {
			case 1:
				clickedPlayer.sendMessage(ChatColor.AQUA
						+ "Please clicked the far corner of the rectangle.");
				gameLocation.x = block.getX();
				gameLocation.y = block.getZ();
				drawingRectangle = 2;
				break;
			case 2:
				drawingRectangle = 0;
				clickedPlayer.sendMessage(ChatColor.AQUA
						+ "You have completed the rectangle.");

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

				if (gameLocation.x > gameLocation.x + gameLocation.width)
					for (int x = gameLocation.x; x > gameLocation.x
							+ gameLocation.width; x--) {
						for (int y = block.getY(); y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y)
									.setType(Material.GLASS);
							block.getWorld()
									.getBlockAt(
											x,
											y,
											gameLocation.y
													+ gameLocation.height)
									.setType(Material.GLASS);
						}
					}
				else
					for (int x = gameLocation.x; x < gameLocation.x
							+ gameLocation.width; x++) {
						for (int y = block.getY(); y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(x, y, gameLocation.y)
									.setType(Material.GLASS);
							block.getWorld()
									.getBlockAt(
											x,
											y,
											gameLocation.y
													+ gameLocation.height)
									.setType(Material.GLASS);
						}
					}

				if (gameLocation.y > gameLocation.y + gameLocation.height)
					for (int z = gameLocation.y; z > gameLocation.y
							+ gameLocation.height; z--) {
						for (int y = block.getY(); y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z)
									.setType(Material.GLASS);
							block.getWorld()
									.getBlockAt(
											gameLocation.x + gameLocation.width,
											y, z).setType(Material.GLASS);
						}
					}
				else
					for (int z = gameLocation.y; z < gameLocation.y
							+ gameLocation.height; z++) {
						for (int y = block.getY(); y < block.getY() + 8; y++) {
							block.getWorld().getBlockAt(gameLocation.x, y, z)
									.setType(Material.GLASS);
							block.getWorld()
									.getBlockAt(
											gameLocation.x + gameLocation.width,
											y, z).setType(Material.GLASS);
						}
					}

				break;
			}

		}

		for (CTFTeam team : teams) {
			if (team.setFlag) {
				team.flag = block.getLocation();
				drawFlag(block.getLocation(), team.color, true);

				team.setFlag = false;
				team.setSpawn = false;
				return;
			}

			if (team.setSpawn) {
				block.setType(Material.GOLD_BLOCK);
				block = block.getRelative(BlockFace.UP);
				team.spawn = block.getLocation();

				team.setFlag = false;
				team.setSpawn = false;
				return;
			}
			if (team.flag == null || team.spawn == null || block == null
					|| block.getLocation() == null)
				continue;

			if (block.getRelative(BlockFace.UP).getLocation().equals(team.spawn)) {
				if (!comparePlayers(team.players, clickedPlayer)) {
					clickedPlayer.sendMessage(" This is not your teams spawn.");
				} else {
					for (CTFTeam fteam : teams) {

						if (fteam.withFlag != null
								&& fteam.withFlag.getName().equals(
										clickedPlayer.getName())) {
							clickedPlayer.getWorld().getBlockAt(fteam.flag)
									.setType(Material.IRON_BLOCK);

							clickedPlayer.getInventory().removeItem(
									fteam.flagItem);
							fteam.flagItem = null;
							clickedPlayer.sendMessage(" You have captured "
									+ fteam.name + "'s flag.");
							sendAllPlayers(clickedPlayer.getDisplayName()
									+ " has captured " + fteam.name
									+ "'s flag.  (" + fteam.points + " points)");

							fteam.withFlag = null;
							return;
						}
					}

					clickedPlayer.sendMessage(" You do not have the flag.");
				}
			}
		}
	}

	private void drawFlag(Location center, DyeColor color, boolean drawFlag) {
		int rad = 3;

		Block block = center.getWorld().getBlockAt(center);

		int centerX = center.getBlockX();
		int centerZ = center.getBlockZ();

		for (int i = centerX - (rad); i < centerX + rad+1; i++) {
			for (int a = centerZ - (rad); a < centerZ + rad+1; a++) {
				block = center.getWorld().getBlockAt(i, center.getBlockY(), a);
				block.setTypeIdAndData(Material.WOOL.getId(), color.getData(),
						false);
			}
		}

		int poleSize = 9;
		for (int i = 0; i < poleSize; i++) {
			block = center.getWorld().getBlockAt(center.getBlockX(),
					center.getBlockY() + 1 + i, center.getBlockZ());
			block.setType(Material.FENCE);
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 1; j < 5; j++) {
				block = center.getWorld().getBlockAt(center.getBlockX() + j,
						center.getBlockY() + poleSize - i, center.getBlockZ());

				if (drawFlag)

					block.setTypeIdAndData(Material.WOOL.getId(),
							color.getData(), false);
				else
					block.setType(Material.AIR);
			}
		}

	}

	public void playerDied(Player player) {
		for (CTFTeam fteam : teams) {
			if (fteam.withFlag.getName().equals(player.getName())) {
				player.getWorld().getBlockAt(fteam.flag)
						.setType(Material.IRON_BLOCK);
				sendAllPlayers(player.getDisplayName() + " has died. The "
						+ fteam.name + " flag has been returned.");
				fteam.withFlag = null;
				return;
			}
		}
	}

	public void playerRespawn(final Player player) {
		for (final CTFTeam fteam : teams) {
			if (comparePlayers(fteam.players, player)) {
				player.sendMessage("You will be ready to play in " + waitTime
						+ " seconds.");
				Timer t = new Timer();
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						player.teleport(fteam.spawn);
						player.sendMessage("You have rejoined the war.");
						sendAllPlayers(player.getDisplayName()
								+ " has rejoined the war.");
					}
				}, waitTime * 1000);
				return;
			}
		}
	}
}
