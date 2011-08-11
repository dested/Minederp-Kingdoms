package com.minederp.kingdoms.games.soundByte;

import java.awt.Point;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.NoteBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import sun.net.www.content.audio.basic;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.games.Game;
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.games.soundByte.SoundByteGame.NoteTracker;
import com.minederp.kingdoms.util.Helper;
import com.sk89q.minecraft.util.commands.CommandContext;

public class SoundByteGame extends Game {
	public final KingdomsPlugin kingdomsPlugin;
	private World gameWorld;
	GameLogic logic;

	public SoundByteGame(KingdomsPlugin kingdomsPlugin, World world) {
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

		kingdomsPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(kingdomsPlugin, new Runnable() {
			@Override
			public void run() {
				soundByteTick();
			}
		}, 1, 6);
	}

	Point curLocation = null;
	private int index = 0;
	private boolean created;

	private void soundByteTick() {
		if (created) {

			for (int i = 0; i < 24; i++) {
				NoteTracker nb = noteBlocks[((index - 1)+24) % 24][i];

				nb.tickBlock.getRelative(BlockFace.UP).setTypeId(0);
			}

			for (int i = 0; i < 24; i++) {
				NoteTracker nb = noteBlocks[index][i];
				if (nb.on) {
					nb.note.play();
				}
				nb.tickBlock.getRelative(BlockFace.UP).setType(Material.GLOWSTONE);
			}

			index++;
			if (index == 24)
				index = 0;
		}

	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {

	}

	@Override
	public void joinGame(Player player) {

	}

	@Override
	public void processCommand(String header, CommandContext args, Player player) {
		if (!header.equals("sb"))
			return;

		if (args.argsLength() == 0) {
			player.sendMessage("SoundByte!");
			return;
		}
		if (Helper.argEquals(args.getString(0), "CreateSoundByte")) {
			Block st = player.getLocation().getBlock();
			BlockFace face = getCardinalDirection(player);
			createBoard(st, face);
			created = true;
			return;
		}

		player.sendMessage("Tetris Command not found: " + args.getString(0));
	}

	public class NoteTracker {
		NoteBlock note;
		Boolean on;
		Block tickBlock;

		public NoteTracker(NoteBlock bl, Block tick) {
			on = false;
			note = bl;
			tickBlock = tick;
		}
	}

	NoteTracker[][] noteBlocks;

	private void createBoard(Block st, BlockFace face) {
		Block startBlock = st.getRelative(BlockFace.DOWN);
		noteBlocks = new NoteTracker[24][24];
		for (int y = 0; y < 10; y++) {
			startBlock = startBlock.getRelative(BlockFace.UP);
			Block startBL = startBlock;
			for (int x = 0; x < 24 ; x++) {
				Block at = startBL;
				for (int z = 0; z < 24 ; z++) {
					at.setTypeId(0);
					at = at.getRelative(getRight(face));

				}
				at = startBL = startBL.getRelative(face);

			}
		}

		startBlock = st.getRelative(BlockFace.UP);
		Block startBL = startBlock;

		for (int x = 0; x < 24 ; x += 1) {
			Block at = startBL;

			for (int z = 0; z < 24 ; z += 1) {
				at.setType(Material.NOTE_BLOCK);
				NoteBlock nb = ((NoteBlock) at.getState());

				nb.setNote((byte) (z ));

				Block tick = at.getRelative(BlockFace.UP).getRelative(BlockFace.UP);
				tick.setType(Material.BEDROCK);
				noteBlocks[x ][z ] = new NoteTracker(nb, tick);

				at = at.getRelative(getRight(face));
			}
			at = startBL = startBL.getRelative(face);
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
		if (created) {
			for (int x = 0; x < 24; x += 1) {
				for (int z = 0; z < 24; z += 1) {
					if (noteBlocks[x][z].tickBlock.equals(block)) {
						noteBlocks[x][z].on = !noteBlocks[x][z].on;
						noteBlocks[x][z].tickBlock.setType(noteBlocks[x][z].on ? Material.GLOWSTONE : Material.BEDROCK);
						noteBlocks[x][z].note.play();
						return true;
					}
				}
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
	public boolean blockPlaced(BlockFace face, Block block, Player player) {
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
