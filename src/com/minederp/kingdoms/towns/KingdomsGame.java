package com.minederp.kingdoms.towns;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

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
import com.minederp.kingdoms.games.GameItem;
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.orm.Kingdom;
import com.minederp.kingdoms.orm.KingdomPlayer;
import com.minederp.kingdoms.orm.Town;
import com.minederp.kingdoms.towns.content.KingdomContent;
import com.minederp.kingdoms.towns.content.TownContent;
import com.minederp.kingdoms.util.Helper;
import com.minederp.kingdoms.util.Polygon;
import com.minederp.kingdoms.util.PolygonPoint;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.blocks.BlockType;

public class KingdomsGame extends Game {

	private final KingdomsPlugin kingdomsPlugin;

	private List<KingdomContent> kingdoms;

	private GameLogic logic;

	public KingdomsGame(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;
	}

	@Override
	public boolean canPlayerJoin(Player player) {
		return true;
	}

	@Override
	public void onLoad(GameLogic logic) {
		this.logic = logic;
		for (Kingdom k : Kingdom.getAll()) {
			kingdoms.add(new KingdomContent(k, kingdomsPlugin, logic));
		}

	}

	@Override
	public void updatePlayerGamePosition(Player movingPlayer, Location to) {

	}

	@Override
	public void processCommand(String header, CommandContext args, Player player) {

		if (!header.equals("k"))
			return;

		if (args.argsLength() > 0 && Helper.argEquals(args.getString(0), "Join")) {
			int kingdomID = -1;
			for (KingdomContent k : kingdoms) {
				if (k.myKingdom.getKingdomName().equals(args.getString(1))) {
					kingdomID = k.myKingdom.getKingdomID();
				}
			}
			if (kingdomID == -1) {
				player.sendMessage("Kingdom not found");
				return;
			}

			KingdomPlayer kp = KingdomPlayer.getFirstByPlayerName(player.getName());
			kp.setKingdomID(kingdomID);
			return;
		}

		for (KingdomContent k : kingdoms) {
			if (k.playerCommand(args, player)) {
				return;
			}
		}

		if (args.argsLength() == 0 || args.getString(0).toLowerCase().equals("help")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.LIGHT_PURPLE + "not in a kingdom help");

			player.sendMessage(sb.toString());

		}

	}

	@Override
	public boolean blockDestroyed(Block block, Player clickedPlayer) {
		return false;
	}

	@Override
	public boolean blockClick(BlockFace face, Block clickedBlock, Player player) {

		return false;
	}

	private void drawPolygon(Material mat, Material mat2, World world) {

	}

	@Override
	public boolean blockPlaced(BlockFace face, Block block, Player player) {

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
		// TODO Auto-generated method stub

	}

	@Override
	public void entityHurt(Entity entity, EntityDamageEvent event) {
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
	public void playerQuit(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerJoin(Player player) {
		// TODO Auto-generated method stub

	}

}