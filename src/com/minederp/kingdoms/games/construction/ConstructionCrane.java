package com.minederp.kingdoms.games.construction;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import com.minederp.kingdoms.games.GameItem;
import com.minederp.kingdoms.util.Helper;
import com.minederp.kingdoms.util.Tuple2;

public class ConstructionCrane {
	public int craneID;
	private static int IDS = 0;
	private final ConstructionJob constructionJob;
	private final Block block;

	public ConstructionCrane(ConstructionJob constructionJob, Block block) {
		craneID = IDS++;
		this.constructionJob = constructionJob;
		this.block = block;
		arms.add(BlockFace.NORTH);
		arms.add(BlockFace.NORTH);
		currentIdealHeight = constructionJob.getIdealPoleHeight(craneID);
	}

	private int poleHeight = 7;
	private int hangLength = 2;
	int currentIdealHeight;

	ConstructionTask currentTask;
	ConstructionTaskPiece currentTaskPiece;
	CraneStep currentStep = CraneStep.Empty;
	int modelChunkIndex = -1;

	public void tick() {
		if (currentTask == null) {
			currentTask = constructionJob.getNextTask(block.getLocation());
			if (currentTask == null) {
				animate();
				return;
			}
			modelChunkIndex = currentTask.getChunkIndex(block.getLocation());
		}
		if (currentTaskPiece == null) {
			currentTaskPiece = currentTask.getNextPiece(modelChunkIndex);
			if (currentTaskPiece == null) {
				currentStep = CraneStep.Empty;
				animate();
				return;
			}
		}
		animate();

	}

	Location getCurrentHead() {
		Block bl = block;
		for (int i = 0; i < poleHeight; i++) {
			bl = bl.getRelative(BlockFace.UP);
		}
		for (BlockFace arm : arms) {
			bl = bl.getRelative(arm);
		}
		for (int i = hangLength; i >= 0; i--) {
			bl = bl.getRelative(BlockFace.DOWN);
		}
		return bl.getLocation();
	}

	private List<BlockFace> arms = new ArrayList<BlockFace>();
	private Material headPiece;

	private int getIdealPoleHeight() {
		return constructionJob.getIdealPoleHeight(craneID);
	}

	private void animate() {
		currentIdealHeight = getIdealPoleHeight();
		if (poleHeight != currentIdealHeight) {
			poleHeight = currentIdealHeight;

		}
		ConstructionChest cs;
		switch (currentStep) {
		case EmptyWatchChest:

			cs = currentTaskPiece.getChest();
			if (Helper.firstNonEmpty(cs.getChest().getInventory()) != null) {
				currentStep = CraneStep.GoingToChest;
				break;
			}

			if (hangLength > 0) {
				hangLength -= 1;
				break;
			}

			if (arms.size() > 2) {
				arms.remove(arms.size() - 1);
				break;
			}
			if (arms.size() == 1) {
				arms.add(BlockFace.NORTH);
				break;
			}

			break;
		case Empty:
			if (currentTaskPiece == null) {
				if (hangLength > 0) {
					hangLength -= 1;
					break;
				}
				if (poleHeight != currentIdealHeight) {
					if (poleHeight > currentIdealHeight)
						poleHeight -= 1;
					else
						poleHeight += 1;
					break;
				}
				if (arms.size() > 2) {
					arms.remove(arms.size() - 1);
					break;
				}
				if (arms.size() == 1) {
					arms.add(BlockFace.NORTH);
					break;
				}
				break;
			}
			currentStep = CraneStep.GoingToChest;
		case GoingToChest:// fall through if the current task isnt null

			if (currentTaskPiece != null) {
				cs = currentTaskPiece.getChest();
				Location loc = cs.getChest().getBlock().getRelative(BlockFace.UP).getLocation();
				Location head = getCurrentHead();
				if (loc.equals(head)) {

					Tuple2<ItemStack, Integer> d = Helper.firstNonEmpty(cs.getChest().getInventory());

					if (d == null) {
						currentStep = CraneStep.EmptyWatchChest;
						break;
					}

					if (d.item1.getAmount() > 1) {
						d.item1.setAmount(d.item1.getAmount() - 1);

					} else {
						cs.getChest().getInventory().setItem(d.item2, null);

					}
					headPiece = d.item1.getType();
					currentStep = CraneStep.RetractOneY;
				} else
					moveToLocation(head, loc);
			}
			break;
		case RetractOneY:
			hangLength -= 1;

			currentStep = CraneStep.GoingToSpot;
			break;
		case GoingToSpot:
			ConstructionModelPiece sp = currentTaskPiece.getConstructionModelPiece();

			Location cc = new Location(block.getWorld(), sp.getPrintX(), sp.getPrintY(), sp.getPrintZ());

			if (currentTaskPiece != null) {
				Location loc = cc.getBlock().getRelative(BlockFace.UP).getLocation();
				Location head = getCurrentHead();
				if (moveToLocation(head, loc)) {
					currentTaskPiece = null;
					cc.getBlock().setType(headPiece);
					headPiece = null;
					currentStep = CraneStep.RetractToChestY;
				}
			}

			break;
		case RetractToChestY:
			if (poleHeight != currentIdealHeight) {
				if (poleHeight > currentIdealHeight) {
					poleHeight -= 1;
					hangLength--;
				} else {
					poleHeight += 1;
					hangLength++;
				}
				break;
			}
			if (currentTaskPiece != null) {
				ConstructionChest cs1 = currentTaskPiece.getChest();
				Location loc = cs1.getChest().getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation();
				Location head = getCurrentHead();
				if (loc.getBlockY() == (head.getY())) {
					currentStep = CraneStep.GoingToChest;
				} else {
					if (head.getBlockY() < loc.getBlockY()) {
						hangLength--;
					} else {
						hangLength++;
					}
				}
			}
			break;

		}
		drawCrane();
	}

	private boolean moveToLocation(Location head, Location loc) {

		if (poleHeight < currentIdealHeight) {
			poleHeight++;
			//return false;
		} else if (poleHeight > currentIdealHeight) {
			poleHeight--;
			//return false;
		}

		if (!head.equals(loc)) {

			if (head.getBlockY() < loc.getBlockY()) {
				if (hangLength >1 )
					hangLength --;
				else
					poleHeight++;
	//			return false;
			}

			if (true) {
				if (head.getBlockX() > loc.getBlockX()) {
					if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.SOUTH) {
						arms.remove(arms.size() - 1);
						return false;
					}
				}
				if (head.getBlockX() < loc.getBlockX()) {
					if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.NORTH) {
						arms.remove(arms.size() - 1);
						return false;
					}
				}
				if (head.getBlockZ() > loc.getBlockZ()) {
					if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.WEST) {
						arms.remove(arms.size() - 1);
						return false;
					}
				}
				if (head.getBlockZ() < loc.getBlockZ()) {
					if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.EAST) {
						arms.remove(arms.size() - 1);
						return false;
					}
				}
			}
			if (head.getBlockX() > loc.getBlockX()) {
				if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.SOUTH)
					arms.remove(arms.size() - 1);
				else
					arms.add(BlockFace.NORTH);
				return false;
			}
			if (head.getBlockX() < loc.getBlockX()) {
				if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.NORTH) {
					arms.remove(arms.size() - 1);
				} else {
					arms.add(BlockFace.SOUTH);
				}
				return false;
			}

			if (head.getBlockZ() > loc.getBlockZ()) {
				if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.WEST)
					arms.remove(arms.size() - 1);
				else
					arms.add(BlockFace.EAST);
				return false;
			}
			if (head.getBlockZ() < loc.getBlockZ()) {
				if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.EAST)
					arms.remove(arms.size() - 1);
				else
					arms.add(BlockFace.WEST);
				return false;
			}
			if (head.getBlockY() > loc.getBlockY()) {
				hangLength++;
				return false;
			}
		}
		return true;
	}

	private void drawCrane() {
		constructionJob.game.logic.clearReprint(block.getWorld(), "crane" + craneID);
		Block bl = block;
		for (int i = 0; i < poleHeight; i++) {
			bl = bl.getRelative(BlockFace.UP);
			constructionJob.game.logic.addBlockForReprint(new GameItem(bl.getX(), bl.getY(), bl.getZ(), bl.getTypeId(), bl.getData(), "crane"
					+ craneID));
			bl.setType(Material.FENCE);
		}
		for (BlockFace arm : arms) {
			bl = bl.getRelative(arm);
			constructionJob.game.logic.addBlockForReprint(new GameItem(bl.getX(), bl.getY(), bl.getZ(), bl.getTypeId(), bl.getData(), "crane"
					+ craneID));
			bl.setType(Material.FENCE);
		}
		for (int i = hangLength; i >= 0; i--) {
			bl = bl.getRelative(BlockFace.DOWN);
			constructionJob.game.logic.addBlockForReprint(new GameItem(bl.getX(), bl.getY(), bl.getZ(), bl.getTypeId(), bl.getData(), "crane"
					+ craneID));
			bl.setType(Material.FENCE);
		}
		if (headPiece != null) {
			bl.setType(headPiece);
		}

	}

	public enum CraneStep {
		Empty, GoingToChest, GoingToSpot, RetractToChestY, RetractOneY, EmptyWatchChest
	}

}
