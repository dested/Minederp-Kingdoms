package com.minederp.kingdoms.games.construction;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import com.minederp.kingdoms.games.GameItem;

public class ConstructionCrane {
	private int craneID;
	private static int IDS = 0;
	private final ConstructionJob constructionJob;
	private final Block block;

	public ConstructionCrane(ConstructionJob constructionJob, Block block) {
		craneID = IDS++;
		this.constructionJob = constructionJob;
		this.block = block;
		arms.add(BlockFace.NORTH);
		arms.add(BlockFace.NORTH);
	}

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

	private int poleHeight = 12;
	private int hangLength = 7;
	private List<BlockFace> arms = new ArrayList<BlockFace>();
	private Material headPiece;

	private void animate() {

		switch (currentStep) {
		case Empty:
			if (currentTaskPiece == null) {
				if (hangLength > 0) {
					hangLength -= 1;
					break;
				}
				if (poleHeight != 5) {
					if (poleHeight > 5)
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
				ConstructionChest cs = currentTaskPiece.getChest();
				Location loc = cs.getChest().getBlock().getRelative(BlockFace.UP).getLocation();
				Location head = getCurrentHead();
				if (loc.equals(head)) {
					ItemStack d = cs.getChest().getInventory().getItem(0);
					d.setAmount(d.getAmount() - 1);
					if (d.getAmount() == 0)
						cs.getChest().getInventory().remove(d);
					headPiece = d.getType();
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
			if (poleHeight != 5) {
				if (poleHeight > 5) {
					poleHeight -= 1;
					hangLength--;
				} else {
					poleHeight += 1;
					hangLength++;
				}
				break;
			}
			if (currentTaskPiece != null) {
				ConstructionChest cs = currentTaskPiece.getChest();
				Location loc = cs.getChest().getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation();
				Location head = getCurrentHead();
				if (loc.getBlockY() == (head.getY())) {
					currentStep = CraneStep.GoingToChest;
				} else {
					if (head.getBlockY() < loc.getBlockY()) {
						hangLength--;
					}
				}
			}
			break;

		}
		drawCrane();
	}

	private boolean moveToLocation(Location head, Location loc) {
		if (!head.equals(loc)) {

			if (head.getBlockY() < loc.getBlockY()) {
				poleHeight++;
				return false;
			}

			if (true) {
				if (head.getBlockX() > loc.getBlockX()) {
					if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.SOUTH) {
						arms.remove(arms.size() - 1);
						return false;
					}
				}  if (head.getBlockX() < loc.getBlockX()) {
					if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.NORTH) {
						arms.remove(arms.size() - 1);
						return false;
					}
				}  if (head.getBlockZ() > loc.getBlockZ()) {
					if (arms.size() != 0 && arms.get(arms.size() - 1) == BlockFace.WEST) {
						arms.remove(arms.size() - 1);
						return false;
					}
				}  if (head.getBlockZ() < loc.getBlockZ()) {
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
			constructionJob.game.logic.blocksForReprint.add(new GameItem(bl.getX(), bl.getY(), bl.getZ(), bl.getTypeId(), bl.getData(), "crane"
					+ craneID));
			bl.setType(Material.FENCE);
		}
		for (BlockFace arm : arms) {
			bl = bl.getRelative(arm);
			constructionJob.game.logic.blocksForReprint.add(new GameItem(bl.getX(), bl.getY(), bl.getZ(), bl.getTypeId(), bl.getData(), "crane"
					+ craneID));
			bl.setType(Material.FENCE);
		}
		for (int i = hangLength; i >= 0; i--) {
			bl = bl.getRelative(BlockFace.DOWN);
			constructionJob.game.logic.blocksForReprint.add(new GameItem(bl.getX(), bl.getY(), bl.getZ(), bl.getTypeId(), bl.getData(), "crane"
					+ craneID));
			bl.setType(Material.FENCE);
		}
		if (headPiece != null) {
			bl.setType(headPiece);
		}

	}

	public enum CraneStep {
		Empty, GoingToChest, GoingToSpot, RetractToChestY, RetractOneY
	}

}
