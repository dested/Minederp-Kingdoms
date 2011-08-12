package com.minederp.kingdoms.games.advancedWar;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.NoteBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.games.Game;
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.games.advancedWar.AdvancedWarContent.AdvancedWarStep;
import com.minederp.kingdoms.games.kingdoms.content.TownContent;
import com.minederp.kingdoms.orm.KingdomPlayer;
import com.minederp.kingdoms.orm.Town;
import com.minederp.kingdoms.util.Helper;
import com.sk89q.minecraft.util.commands.CommandContext;

public class AdvancedWarGame extends Game {

	public final KingdomsPlugin kingdomsPlugin;
	private World gameWorld;
	GameLogic logic;

	public AdvancedWarGame(KingdomsPlugin kingdomsPlugin, World world) {
		this.kingdomsPlugin = kingdomsPlugin;
		gameWorld = world;
	}

	List<AdvancedWarContent> wars;

	@Override
	public boolean canPlayerJoin(Player player) {
		return true;
	}

	@Override
	public void onLoad(GameLogic logic) {
		this.logic = logic;
		load();
	}

	private void load() {
		wars = new ArrayList<AdvancedWarContent>();

	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {

	}

	@Override
	public void joinGame(Player player) {

	}

	@Override
	public void processCommand(String header, CommandContext args, Player player) {
		if (!header.equals("aw"))
			return;

		if (args.argsLength() == 0) {
			player.sendMessage("Advanced War !");
			return;
		}
		if (Helper.argEquals(args.getString(0), "DeclareWar")) {
			KingdomPlayer kp = KingdomPlayer.getFirstByPlayerName(player.getName());
			if (kp.getTown().getGovernor().equals(kp)) {
				wars.add(new AdvancedWarContent(AdvancedWarStep.Declared, kp.getTown(), Town.getFirstByTownName(args.getString(1)), kingdomsPlugin,
						logic));
				return;
			} else {
				player.sendMessage(ChatColor.RED + "You do not have permission to declare war.");
				return;
			}
		}

		if (Helper.argEquals(args.getString(0), "AcceptWar")) {
			for (AdvancedWarContent awc : wars) {
				awc.acceptWar(player);
			}
		}
		if (Helper.argEquals(args.getString(0), "DeclineWar")) {
			for (AdvancedWarContent awc : wars) {
				awc.declineWar(player);
			}
		}
		if (Helper.argEquals(args.getString(0), "Engage")) {
			for (AdvancedWarContent awc : wars) {
				awc.engage(player);
			}
		}

		player.sendMessage("Advanced War Command not found: " + args.getString(0));
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

	@Override
	public void playerQuit(Player player) {
		for (AdvancedWarContent town : wars) {
			town.playerQuit(player);
		}
	}

	@Override
	public void playerJoin(Player player) {
		for (AdvancedWarContent town : wars) {
			town.playerJoin(player);
		}
	}

	@Override
	public boolean blockClick(BlockFace face, Block clickedBlock, Player player) {
		for (AdvancedWarContent town : wars) {
			if (town.blockClick(face, clickedBlock, player)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean blockPlaced(BlockFace face, Block clickedBlock, Player player) {
		for (AdvancedWarContent town : wars) {
			if (town.blockPlaced(face, clickedBlock, player)) {
				return true;
			}

		}

		return false;
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
	public void playerRespawn(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerDied(Player player) {
		// TODO Auto-generated method stub

	}
}
