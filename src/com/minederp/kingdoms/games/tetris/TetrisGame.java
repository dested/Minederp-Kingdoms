package com.minederp.kingdoms.games.tetris;

import java.awt.Point;
import java.util.Random;

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
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.util.Helper;
import com.sk89q.minecraft.util.commands.CommandContext;

public class TetrisGame extends Game {
	public final KingdomsPlugin kingdomsPlugin;
	private World gameWorld;
	GameLogic logic;
	private Random randomizer = new Random();
	private Block startBlock;
	private BlockFace startFace;
	private Block topRight;
	private Block nextPieceOne;
	private Block nextPieceTwo;
	private int gameoverClear = 22;

	public TetrisGame(KingdomsPlugin kingdomsPlugin, World world) {
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

		peices = new TetrisPiece[7];

		peices[0] = new TetrisPiece(new int[][] { { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		peices[1] = new TetrisPiece(new int[][] { { 2, 0, 0 }, { 2, 2, 2 }, { 0, 0, 0 } });
		peices[2] = new TetrisPiece(new int[][] { { 0, 0, 3 }, { 3, 3, 3 }, { 0, 0, 0 } });
		peices[3] = new TetrisPiece(new int[][] { { 4, 4 }, { 4, 4 } });
		peices[4] = new TetrisPiece(new int[][] { { 0, 5, 5 }, { 5, 5, 0 }, { 0, 0, 0 } });
		peices[5] = new TetrisPiece(new int[][] { { 0, 6, 0 }, { 6, 6, 6 }, { 0, 0, 0 } });
		peices[6] = new TetrisPiece(new int[][] { { 7, 7, 0 }, { 0, 7, 7 }, { 0, 0, 0 } });

		kingdomsPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(kingdomsPlugin, new Runnable() {
			@Override
			public void run() {
				tetrisTick();
			}
		}, 1, 15);
	}

	TetrisPiece nextPiece1;
	TetrisPiece nextPiece2;
	TetrisPiece curPiece;
	Point curLocation = null;
	private int clearYInd = -1;

	private boolean players = false;

	private void tetrisTick() {
		if (startBlock == null || !players)
			return;

		if (nextPiece2 == null) {
			nextPiece2 = peices[randomizer.nextInt(7)].cloneMe();
		}
		if (nextPiece1 == null) {
			nextPiece1 = peices[randomizer.nextInt(7)].cloneMe();
		}
		if (curPiece == null) {
			curPiece = peices[randomizer.nextInt(7)].cloneMe();
		}

		switch (step) {
		case GameOver:
			PlayingPlayer = null;
			if (gameoverClear > 0) {
				gameoverClear--;

			} else {
				gameoverClear = 22;
				startGame(null);
				step = TetrisStep.RunGame;
				players = false;
			}
			drawGame(true);
			return;
		case ClearLine:
			for (int y = clearYInd; y > 0; y--) {
				for (int x = 0; x < 10; x++) {
					gameboard[y][x] = gameboard[y - 1][x];
				}
			}
			clearYInd = -1;
		case CheckLine:
			step = TetrisStep.CheckLine;
			for (int i = 19; i >= 0; i--) {
				boolean good = true;
				for (int j = 0; j < 10; j++) {
					if (gameboard[i][j] == 0) {
						good = false;
						break;
					}
				}
				if (good) {

					clearYInd = i;
					step = TetrisStep.ClearLine;
					break;
				}
			}
			if (step == TetrisStep.CheckLine)
				step = TetrisStep.RunGame;

			break;
		case RunGame:

			break;
		}

		drawGame(false);
		if (step == TetrisStep.RunGame && checkCollide(true)) {
			step = TetrisStep.CheckLine;
		}

	}

	TetrisStep step = TetrisStep.RunGame;

	private void drawGame(boolean gameover) {

		Block at = topLeft.getRelative(BlockFace.DOWN).getRelative(getRight(startFace));
		Block begin = at;
		Block start = at;
		if (!gameover) {
			drawPiece(nextPieceOne.getRelative(BlockFace.DOWN).getRelative(getRight(startFace)), nextPiece1, true);
			drawPiece(nextPieceTwo.getRelative(BlockFace.DOWN).getRelative(getRight(startFace)), nextPiece2, true);

			Block curJ = start;
			for (int i = 0; i < curLocation.x; i++) {
				curJ = curJ.getRelative(getRight(startFace));
			}
			for (int i = 0; i < curLocation.y; i++) {
				curJ = curJ.getRelative(BlockFace.DOWN);
			}

			for (int i = 0; i < 20; i++) {
				for (int a = 0; a < 10; a++) {
					at.setType(Material.AIR);
					at = at.getRelative(getRight(startFace));
				}
				at = start = start.getRelative(BlockFace.DOWN);
			}
			drawPiece(curJ, curPiece, false);
		}
		at = start = begin;
		for (int i = 0; i < 20; i++) {
			for (int a = 0; a < 10; a++) {
				if (gameoverClear <= i) {
					at.setType(Material.WOOL);
					at.setData((byte) 14);
					at = at.getRelative(getRight(startFace));
					continue;
				}
				if (i == clearYInd) {
					at.setType(Material.GLOWSTONE);

				} else {
					switch (gameboard[i][a]) {
					case 1:
						at.setType(Material.WOOL);
						at.setData((byte) 3);
						break;
					case 2:
						at.setType(Material.WOOL);
						at.setData((byte) 11);
						break;
					case 3:
						at.setType(Material.WOOL);
						at.setData((byte) 1);
						break;
					case 4:
						at.setType(Material.WOOL);
						at.setData((byte) 4);
						break;
					case 5:
						at.setType(Material.WOOL);
						at.setData((byte) 5);
						break;
					case 6:
						at.setType(Material.WOOL);
						at.setData((byte) 10);
						break;
					case 7:
						at.setType(Material.WOOL);
						at.setData((byte) 14);
						break;

					}
				}
				at = at.getRelative(getRight(startFace));
			}
			at = start = start.getRelative(BlockFace.DOWN);
		}
	}

	private boolean checkCollide(boolean decrease) {

		curLocation.y++;
		boolean bad = false;
		for (int i = 0; i < curPiece.PieceInfo.length; i++) {
			for (int j = 0; j < curPiece.PieceInfo.length; j++) {

				if (curPiece.PieceInfo[i][j] > 0 && (i + curLocation.y > 19 || gameboard[i + curLocation.y][j + curLocation.x] > 0))
					bad = true;

			}
		}

		if (bad) {
			curLocation.y--;

			for (int i = 0; i < curPiece.PieceInfo.length; i++) {
				for (int j = 0; j < curPiece.PieceInfo.length; j++) {
					if (curPiece.PieceInfo[i][j] > 0)
						gameboard[i + curLocation.y][j + curLocation.x] = curPiece.PieceInfo[i][j];
				}
			}

			curPiece = nextPiece1;
			nextPiece1 = nextPiece2;
			nextPiece2 = null;
			curLocation = new Point(4, 0);

			for (int i = 0; i < curPiece.PieceInfo.length; i++) {
				for (int j = 0; j < curPiece.PieceInfo.length; j++) {
					if (curPiece.PieceInfo[i][j] > 0 && (i + curLocation.y > 19 || gameboard[i + curLocation.y][j + curLocation.x] > 0)) {

						gameoverClear = 20;
						step = TetrisStep.GameOver;

						return false;
					}
				}
			}
			return true;
		}
		if (!decrease)
			curLocation.y--;
		return false;
	}

	private void drawPiece(Block at, TetrisPiece pc, boolean clear) {
		Block start = at;
		Block atc = at;
		if (clear) {
			for (int i = 0; i < 4; i++) {

				for (int a = 0; a < 4; a++) {
					at.setType(Material.AIR);
					at = at.getRelative(getRight(startFace));
				}
				at = start = start.getRelative(BlockFace.DOWN);
			}
		}
		at = start = atc;
		for (int i = 0; i < pc.PieceInfo.length; i++) {

			for (int a = 0; a < pc.PieceInfo.length; a++) {

				switch (pc.PieceInfo[i][a]) {
				case 1:
					at.setType(Material.WOOL);
					at.setData((byte) 3);
					break;
				case 2:
					at.setType(Material.WOOL);
					at.setData((byte) 11);
					break;
				case 3:
					at.setType(Material.WOOL);
					at.setData((byte) 1);
					break;
				case 4:
					at.setType(Material.WOOL);
					at.setData((byte) 4);
					break;
				case 5:
					at.setType(Material.WOOL);
					at.setData((byte) 5);
					break;
				case 6:
					at.setType(Material.WOOL);
					at.setData((byte) 10);
					break;
				case 7:
					at.setType(Material.WOOL);
					at.setData((byte) 14);
					break;

				}
				at = at.getRelative(getRight(startFace));

			}
			at = start = start.getRelative(BlockFace.DOWN);
		}

	}

	public TetrisPiece[] peices;

	public int[][] gameboard;
	private Block topLeft;
	private Block moveLeft;
	private Block moveRight;

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {

	}

	@Override
	public void joinGame(Player player) {

	}

	@Override
	public void processCommand(String header, CommandContext args, Player player) {
		if (!header.equals("tet"))
			return;

		if (args.argsLength() == 0) {

			player.sendMessage("Tetris!");
			return;
		}
		if (Helper.argEquals(args.getString(0), "CreateTetris")) {
			Block st = player.getLocation().getBlock();

			BlockFace face = getCardinalDirection(player);

			startFace = face;
			startGame(st);

			players = false;
			PlayingPlayer = null;

			return;
		}

		player.sendMessage("Tetris Command not found: " + args.getString(0));

	}

	private void startGame(Block st) {
		curLocation = new Point(4, 0);
		curPiece = null;
		nextPiece1 = null;
		nextPiece2 = null;
		gameboard = new int[20][10];

		BlockFace face = startFace;

		if (st == null) {
			Block tl = topLeft.getRelative(getRight(face)).getRelative(BlockFace.DOWN);
			Block startTl = tl;

			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < 10; j++) {
					tl.setTypeId(0);
					tl = tl.getRelative(getRight(face));
				}
				tl = startTl = startTl.getRelative(BlockFace.DOWN);
			}
			return;
		}

		Block m1;
		m1 = st.getRelative(BlockFace.DOWN).getRelative(face).getRelative(face).getRelative(face).getRelative(face);

		for (int i = 0; i < 13; i++) {
			m1 = m1.getRelative(BlockFace.UP);
		}

		startBlock = m1.getRelative(BlockFace.UP);
		startBlock.setType(Material.OBSIDIAN);

		Block mj = m1;
		for (int i = 0; i < 6; i++) {
			mj = mj.getRelative(getLeft(face));
		}

		Block startmj = mj;
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 13; i++) {
				mj.setType(Material.GLASS);
				mj = mj.getRelative(getRight(face));
			}
			mj = startmj = startmj.getRelative(face.getOppositeFace());
		}

		Block m = m1.getRelative(getLeft(face));
		m.setType(Material.OBSIDIAN);
		m = m.getRelative(getLeft(face));
		m.setType(Material.OBSIDIAN);

		(moveLeft = m.getRelative(BlockFace.UP)).setType(Material.OBSIDIAN);

		m = m.getRelative(getLeft(face));
		m.setType(Material.OBSIDIAN);
		m = m.getRelative(getLeft(face));
		m.setType(Material.OBSIDIAN);

		m = m1.getRelative(getRight(face));
		m.setType(Material.OBSIDIAN);
		m = m.getRelative(getRight(face));
		m.setType(Material.OBSIDIAN);

		(moveRight = m.getRelative(BlockFace.UP)).setType(Material.OBSIDIAN);

		m = m.getRelative(getRight(face));
		m.setType(Material.OBSIDIAN);
		m = m.getRelative(getRight(face));
		m.setType(Material.OBSIDIAN);

		for (int i = 0; i < 13; i++) {
			m1 = m1.getRelative(BlockFace.DOWN);
		}
		for (int i = 0; i < 20; i++) {
			m1 = m1.getRelative(face);
		}

		for (int i = 0; i < 5; i++) {
			m1 = m1.getRelative(getLeft(face));
		}

		m1 = m1.getRelative(BlockFace.UP);

		for (int i = 0; i < 12; i++) {
			m1 = m1.getRelative(getRight(face));
			m1.setType(Material.OBSIDIAN);
		}

		for (int i = 0; i < 21; i++) {
			m1 = m1.getRelative(BlockFace.UP);
			m1.setType(Material.OBSIDIAN);
		}
		topRight = m1;

		nextPieceOne = m1;
		for (int i = 0; i < 5; i++) {
			m1 = m1.getRelative(getRight(face));
			m1.setType(Material.OBSIDIAN);
		}
		for (int i = 0; i < 5; i++) {
			m1 = m1.getRelative(BlockFace.DOWN);
			m1.setType(Material.OBSIDIAN);
		}
		Block back = m1;
		for (int i = 0; i < 5; i++) {
			m1 = m1.getRelative(getLeft(face));
			m1.setType(Material.OBSIDIAN);
		}
		nextPieceTwo = m1;
		m1 = back;

		for (int i = 0; i < 5; i++) {
			m1 = m1.getRelative(BlockFace.DOWN);
			m1.setType(Material.OBSIDIAN);
		}
		for (int i = 0; i < 5; i++) {
			m1 = m1.getRelative(getLeft(face));
			m1.setType(Material.OBSIDIAN);
		}
		m1 = back;

		m1 = topRight;
		for (int i = 0; i < 11; i++) {
			m1 = m1.getRelative(getLeft(face));
			m1.setType(Material.OBSIDIAN);
		}

		topLeft = m1;

		Block tl = topLeft.getRelative(getRight(face)).getRelative(BlockFace.DOWN);
		Block startTl = tl;

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				tl.setTypeId(0);
				tl = tl.getRelative(getRight(face));
			}
			tl = startTl = startTl.getRelative(BlockFace.DOWN);
		}

		for (int i = 0; i < 21; i++) {
			m1 = m1.getRelative(BlockFace.DOWN);
			m1.setType(Material.OBSIDIAN);
		}

	}

	public static BlockFace getCardinalDirection(Player player) {
		double rot = (player.getLocation().getYaw() - 90) % 360;
		if (rot < 0) {
			rot += 360.0;
		}
		return getDirection(rot);
	}

	public static BlockFace getLeft(BlockFace bf) {
		switch (bf) {
		case EAST:
			return BlockFace.NORTH;
		case NORTH:
			return BlockFace.WEST;
		case SOUTH:
			return BlockFace.EAST;
		case WEST:
			return BlockFace.SOUTH;
		}
		return BlockFace.SELF;
	}

	public static BlockFace getRight(BlockFace bf) {
		return getLeft(bf).getOppositeFace();
	}

	private static BlockFace getDirection(double rot) {
		if (0 <= rot && rot < 22.5) {
			return BlockFace.NORTH;
		} else if (22.5 <= rot && rot < 67.5) {
			return BlockFace.NORTH;
		} else if (67.5 <= rot && rot < 112.5) {
			return BlockFace.EAST;
		} else if (112.5 <= rot && rot < 157.5) {
			return BlockFace.EAST;
		} else if (157.5 <= rot && rot < 202.5) {
			return BlockFace.SOUTH;
		} else if (202.5 <= rot && rot < 247.5) {
			return BlockFace.SOUTH;
		} else if (247.5 <= rot && rot < 292.5) {
			return BlockFace.WEST;
		} else if (292.5 <= rot && rot < 337.5) {
			return BlockFace.WEST;
		} else if (337.5 <= rot && rot < 360.0) {
			return BlockFace.NORTH;
		} else {
			return null;
		}
	}

	@Override
	public void leaveGame(Player player) {

	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {

		return false;
	}

	Player PlayingPlayer = null;

	@Override
	public boolean blockClick(BlockFace face, Block block, Player clickedPlayer) {
		if (startBlock == null)
			return false;
		if (block.getLocation().equals(startBlock.getLocation()) && (curPiece == null || step != TetrisStep.RunGame)) {
			if (!players) {
				PlayingPlayer = clickedPlayer;
				startGame(null);
				players = true;
				clickedPlayer.sendMessage("Game Started");
				return true;
			}
			if (!PlayingPlayer.equals(clickedPlayer))
				return true;
		}

		if (block.getLocation().equals(startBlock.getLocation())) {

			if (face == getLeft(startFace)) {
				curPiece.rotateLeft();
				for (int i = 0; i < curPiece.PieceInfo.length; i++) {
					for (int j = 0; j < curPiece.PieceInfo.length; j++) {

						if (curPiece.PieceInfo[i][j] > 0 && (j + curLocation.x > 9 || j + curLocation.x < 0))
							curPiece.rotateRight();
					}
				}

			} else if (face == getRight(startFace)) {
				curPiece.rotateRight();

				for (int i = 0; i < curPiece.PieceInfo.length; i++) {
					for (int j = 0; j < curPiece.PieceInfo.length; j++) {
						if (curPiece.PieceInfo[i][j] > 0 && (j + curLocation.x > 9 || j + curLocation.x < 0))
							curPiece.rotateLeft();
					}
				}

			}

			else if (face == startFace.getOppositeFace() || face == BlockFace.UP) {
				if (checkCollide(false)) {
					step = TetrisStep.CheckLine;
					return true;
				}
				curLocation.y++;
			}
			drawGame(false);
			if (checkCollide(false)) {
				step = TetrisStep.CheckLine;
			}

			return true;
		}
		if (!players)
			return true;
		if (block.getLocation().equals(moveRight.getLocation())) {
			if (checkCollide(false)) {
				return true;
			}

			curLocation.x++;
			for (int i = 0; i < curPiece.PieceInfo.length; i++) {
				for (int j = 0; j < curPiece.PieceInfo.length; j++) {
					if ((curPiece.PieceInfo[i][j] > 0 && (j + curLocation.x < 0 || j + curLocation.x > 9))) {
						curLocation.x--;
						return true;
					}
				}
			}
			drawGame(false);

			if (checkCollide(false)) {
				step = TetrisStep.CheckLine;
			}
			return true;
		}
		if (block.getLocation().equals(moveLeft.getLocation())) {
			if (checkCollide(false)) {
				return true;
			}
			curLocation.x--;
			for (int i = 0; i < curPiece.PieceInfo.length; i++) {
				for (int j = 0; j < curPiece.PieceInfo.length; j++) {
					if ((curPiece.PieceInfo[i][j] > 0 && (j + curLocation.x < 0 || j + curLocation.x > 9))) {
						curLocation.x++;
						return true;
					}
				}
			}
			drawGame(false);
			if (checkCollide(false)) {
				step = TetrisStep.CheckLine;
			}
			return true;
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
	public boolean blockPlaced(BlockFace face, Block block, Player player) {
		return false;
	}
}
