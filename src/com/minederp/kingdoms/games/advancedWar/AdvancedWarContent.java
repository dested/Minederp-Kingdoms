package com.minederp.kingdoms.games.advancedWar;

import java.awt.Color;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sun.security.action.GetLongAction;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.games.GameLogic;
import com.minederp.kingdoms.games.kingdoms.KingdomPlayerCacher;
import com.minederp.kingdoms.games.kingdoms.content.TownContent;
import com.minederp.kingdoms.orm.KingdomPlayer;
import com.minederp.kingdoms.orm.Town;
import com.minederp.kingdoms.util.Helper;
import com.minederp.kingdoms.util.InventoryStash;
import com.minederp.kingdoms.util.PolygonPoint;

public class AdvancedWarContent {

	private KingdomsPlugin plugin;
	private GameLogic logic;
	private TownContent myAttackingTown;
	private TownContent myDefendingTown;

	private AdvancedWarStep warStep;
	private InventoryStash wager1;
	private InventoryStash wager2;

	enum AdvancedWarStep {
		Declared, Agreed, Started, Resovled, ChoosingChest, ChoosingResponse, ChoosingSecondChest, AcceptingSecondWager
	}

	public AdvancedWarContent(AdvancedWarStep step, Town t, Town t2, KingdomsPlugin plugin, GameLogic logic) {
		warStep = step;
		this.plugin = plugin;
		this.logic = logic;
		myAttackingTown = plugin.townGame.getTown(t);
		myDefendingTown = plugin.townGame.getTown(t2);
		load();
		processStep();
	}

	private void processStep() {
		switch (warStep) {
		case Declared:
			KingdomPlayer gov;
			if ((gov = myAttackingTown.townPlayers.getPlayer(myAttackingTown.myTown.getGovernor().getPlayerName())) != null) {
				Player v = plugin.getServer().getPlayer(gov.getPlayerName());
				v.sendMessage("The next chest you click will be wagered");
				warStep = AdvancedWarStep.ChoosingChest;
			}
			break;
		case ChoosingChest:
			KingdomPlayer gov2;
			if ((gov2 = myDefendingTown.townPlayers.getPlayer(myDefendingTown.myTown.getGovernor().getPlayerName())) != null) {
				Player v = plugin.getServer().getPlayer(gov2.getPlayerName());
				v.sendMessage(myAttackingTown.myTown.getTownName() + " Has declared war on you. He has wagered " + wager1.toString()
						+ ". Please either " + ChatColor.BLUE + "/aw DeclineWar  " + ChatColor.WHITE
						+ " or /aw AcceptWar and click a chest to wager.");

				warStep = AdvancedWarStep.ChoosingResponse;
			}
			break;
		case ChoosingResponse:
			warStep = AdvancedWarStep.ChoosingSecondChest;
			break;
		case ChoosingSecondChest:
			warStep = AdvancedWarStep.AcceptingSecondWager;

			KingdomPlayer gov1;
			if ((gov1 = myAttackingTown.townPlayers.getPlayer(myAttackingTown.myTown.getGovernor().getPlayerName())) != null) {
				Player v = plugin.getServer().getPlayer(gov1.getPlayerName());
				v.sendMessage(myDefendingTown.myTown.getTownName() + " Has accepted your wager. They has wagered " + wager2.toString()
						+ ". Please /aw AcceptWar and accept the offer and begin the act of war.");

			}

			break;
		case AcceptingSecondWager:

			warStep = AdvancedWarStep.Started;

			for (KingdomPlayerCacher kpc : myAttackingTown.townPlayers) {
				kpc.player
						.sendMessage(ChatColor.DARK_AQUA
								+ " War has been declared on the town "
								+ myDefendingTown.myTown.getTownName()
								+ ". You must defend your town from enemies. You can also /aw Engage to teleport near the town. The goal is to destory the towns heart, so be sure to bring a Pickaxe. ");
			}

			for (KingdomPlayerCacher kpc : myDefendingTown.townPlayers) {
				kpc.player
						.sendMessage(ChatColor.DARK_AQUA
								+ " War has been declared on the town "
								+ myAttackingTown.myTown.getTownName()
								+ ". You must defend your town from enemies. You can also /aw Engage to teleport near the town. The goal is to destory the towns heart, so be sure to bring a Pickaxe. ");
			}
			break;
		case Resovled:
			break;
		case Started:
			break;

		}

	}

	private void load() {

	}

	public boolean blockPlaced(BlockFace face, Block clickedBlock, Player player) {

		if (myAttackingTown.isInTown(clickedBlock.getLocation()) && !myAttackingTown.townPlayers.contains(player)) {
			return true;
		}
		if (myDefendingTown.isInTown(clickedBlock.getLocation()) && !myDefendingTown.townPlayers.contains(player)) {
			return true;
		}

		return false;
	}

	public boolean blockClick(BlockFace face, Block clickedBlock, Player player) {
		if (warStep != AdvancedWarStep.Started)
			return false;

		if (myAttackingTown.isInTown(clickedBlock.getLocation()) && !myAttackingTown.townPlayers.contains(player)) {
			Location loc = clickedBlock.getLocation();
			Location heart = Helper.parseLocation(clickedBlock.getWorld(), myAttackingTown.myTown.getTownHeart());
			if (heart == null)
				return true;
			if ((Math.abs(loc.getBlockX() - heart.getBlockX()) < 4)) {
				if ((Math.abs(loc.getBlockZ() - heart.getBlockZ()) < 4)) {
					if ((Math.abs(loc.getBlockY() - heart.getBlockY()) < 4)) {
						return false;
					}
				}
			}

			return true;
		}
		if (myDefendingTown.isInTown(clickedBlock.getLocation()) && !myDefendingTown.townPlayers.contains(player)) {
			Location loc = clickedBlock.getLocation();
			Location heart = Helper.parseLocation(clickedBlock.getWorld(), myDefendingTown.myTown.getTownHeart());
			if (heart == null)
				return true;
			if ((Math.abs(loc.getBlockX() - heart.getBlockX()) < 4)) {
				if ((Math.abs(loc.getBlockZ() - heart.getBlockZ()) < 4)) {
					if ((Math.abs(loc.getBlockY() - heart.getBlockY()) < 4)) {
						return false;
					}
				}
			}
			return true;
		}

		if (warStep == AdvancedWarStep.ChoosingChest) {
			Player v = plugin.getServer().getPlayer(myAttackingTown.myTown.getGovernor().getPlayerName());
			if (v.equals(player)) {
				BlockState state = clickedBlock.getState();
				if (state instanceof CraftChest) {
					CraftChest chest = (CraftChest) state;
					Inventory inv = chest.getInventory();
					ItemStack[] contents = inv.getContents();
					wager1 = new InventoryStash(contents);
					inv.clear();

					processStep();
					return true;
				}
			}
		}

		if (warStep == AdvancedWarStep.ChoosingSecondChest) {
			Player v = plugin.getServer().getPlayer(myDefendingTown.myTown.getGovernor().getPlayerName());
			if (v.equals(player)) {
				BlockState state = clickedBlock.getState();
				if (state instanceof CraftChest) {
					CraftChest chest = (CraftChest) state;
					Inventory inv = chest.getInventory();
					ItemStack[] contents = inv.getContents();
					wager2 = new InventoryStash(contents);
					inv.clear();
					processStep();
					return true;
				}
			}
		}
		return false;
	}

	public void playerQuit(Player player) {
	}

	public void playerJoin(Player player) {

		if (warStep == AdvancedWarStep.Started) {

			for (KingdomPlayerCacher kpc : myAttackingTown.townPlayers) {

				if (kpc.player.equals(player)) {
					kpc.player
							.sendMessage(ChatColor.DARK_AQUA
									+ " War has been declared on the town "
									+ myDefendingTown.myTown.getTownName()
									+ ". You must defend your town from enemies. You can also /aw Engage to teleport near the town. The goal is to destory the towns heart, so be sure to bring a Pickaxe. ");
					return;
				}
			}

			for (KingdomPlayerCacher kpc : myDefendingTown.townPlayers) {
				if (kpc.player.equals(player)) {
					kpc.player
							.sendMessage(ChatColor.DARK_AQUA
									+ " War has been declared on the town "
									+ myAttackingTown.myTown.getTownName()
									+ ". You must defend your town from enemies. You can also /aw Engage to teleport near the town. The goal is to destory the towns heart, so be sure to bring a Pickaxe. ");

					return;
				}
			}
		}
	}

	public void engage(Player player) {
		KingdomPlayer gov2;
		PolygonPoint ip;
		if ((gov2 = myDefendingTown.townPlayers.getPlayer(player)) != null) {
			ip = myAttackingTown.townPolygon.polygon.get(0);
			player.teleport(new Location(player.getWorld(), ip.X, ip.Y, ip.Z));
			return;
		}

		if ((gov2 = myAttackingTown.townPlayers.getPlayer(player)) != null) {
			ip = myDefendingTown.townPolygon.polygon.get(0);
			player.teleport(new Location(player.getWorld(), ip.X, ip.Y, ip.Z));
			return;
		}
	}

	public void declineWar(Player player) {

	}

	public void acceptWar(Player player) {

		if (warStep == AdvancedWarStep.ChoosingResponse) {
			KingdomPlayer gov2;
			if ((gov2 = myDefendingTown.townPlayers.getPlayer(myDefendingTown.myTown.getGovernor().getPlayerName())) != null) {
				Player v = plugin.getServer().getPlayer(gov2.getPlayerName());
				if (v.equals(player)) {
					v.sendMessage("The next chest you click will be wagered");
					processStep();
					return;
				}
			}

		}

		if (warStep == AdvancedWarStep.AcceptingSecondWager) {
			KingdomPlayer gov2;
			if ((gov2 = myAttackingTown.townPlayers.getPlayer(myAttackingTown.myTown.getGovernor().getPlayerName())) != null) {
				Player v = plugin.getServer().getPlayer(gov2.getPlayerName());
				if (v.equals(player)) {
					processStep();
					return;

				}
			}

		}

	}
}