package com.minederp.kingdoms.games.construction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.smartcardio.Card;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import sun.util.logging.resources.logging;

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
		arms.add(BlockFace.UP);
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
		for (BlockFace arm : arms) {
			bl = bl.getRelative(arm);
		}
		return bl.getLocation();
	}
/*
	private List<Location> astar(Location start, Location end) {
		List<Location> closedSet = new ArrayList<Location>();
		List<Location> openSet = new ArrayList<Location>();
		openSet.add(start);
		HashMap<Location, Location> cameFrom = new HashMap<Location, Location>();

		HashMap<Location, Integer> gScore = new HashMap<Location, Integer>();
		gScore.put(start, 0);

		HashMap<Location, Double> hScore = new HashMap<Location, Double>();
		hScore.put(start, start.distance(end));

		HashMap<Location, Double> fScore = new HashMap<Location, Double>();
		fScore.put(start, hScore.get(start));

		while (openSet.size() > 0) {
			double lowestfs = Double.MAX_VALUE;
			Location x = null;
			for (Location l : openSet) {
				if (fScore.get(l) < lowestfs) {
					x = l;
					lowestfs = fScore.get(l);
				}
			}
			if (x.equals(end)) {

				List<Location> gf = reconstructPath(cameFrom, cameFrom.get(end));
				gf.add(end);
				return gf;
			}

			openSet.remove(x);
			closedSet.add(x);

			for (Location y : neighborNodes(x)) {
				if (closedSet.contains(y))
					continue;
				boolean tentative_is_better;

				int tentative_g_score = gScore.get(x) + 1;
				if (tentative_g_score > 15)
					continue;
				if (!openSet.contains(y)) {
					openSet.add(y);
					tentative_is_better = true;
				} else if (tentative_g_score < gScore.get(y)) {
					tentative_is_better = true;
				} else
					tentative_is_better = false;
				if (tentative_is_better) {
					cameFrom.put(y, x);
					Double df = (double) tentative_g_score;
					gScore.put(y, tentative_g_score);
					hScore.put(y, df += start.distance(end));
					fScore.put(y, df);
				}
			}
		}
		return null;
	}

	private List<Location> neighborNodes(Location x) {
		World w = x.getWorld();
		List<Location> arl = new ArrayList<Location>();
		Location loc;
		if (w.getBlockTypeIdAt(loc = new Location(w, x.getBlockX(), x.getBlockY() + 1, x.getBlockZ())) == 0)
			arl.add(loc);
		if (w.getBlockTypeIdAt(loc = new Location(w, x.getBlockX(), x.getBlockY() - 1, x.getBlockZ())) == 0)
			arl.add(loc);

		if (w.getBlockTypeIdAt(loc = new Location(w, x.getBlockX() - 1, x.getBlockY(), x.getBlockZ())) == 0)
			arl.add(loc);
		if (w.getBlockTypeIdAt(loc = new Location(w, x.getBlockX() + 1, x.getBlockY(), x.getBlockZ())) == 0)
			arl.add(loc);
		if (w.getBlockTypeIdAt(loc = new Location(w, x.getBlockX(), x.getBlockY(), x.getBlockZ() - 1)) == 0)
			arl.add(loc);
		if (w.getBlockTypeIdAt(loc = new Location(w, x.getBlockX(), x.getBlockY(), x.getBlockZ() + 1)) == 0)
			arl.add(loc);
		return arl;
	}

	private List<Location> reconstructPath(HashMap<Location, Location> cameFrom, Location location) {
		List<Location> lp;
		if (cameFrom.get(location) != null) {
			lp = reconstructPath(cameFrom, cameFrom.get(location));
			lp.add(location);
			return lp;
		}
		lp = new ArrayList<Location>();
		lp.add(location);
		return lp;
	}*/

	private List<BlockFace> arms = new ArrayList<BlockFace>();
	private Material headPiece;
	boolean emptyswitch = true;

	private void animate() {

		ConstructionChest cs;
		switch (currentStep) {
		case EmptyWatchChest:

			if (emptyswitch) {
				if (arms.size() == 1) {
					emptyswitch = false;
					break;
				}
				arms.remove(arms.size() - 1);
				break;
			}

			switch (arms.size()) {
			case 1:
				arms.clear();
				arms.add(BlockFace.UP);
				arms.add(BlockFace.UP);
				break;
			case 2:
				arms.add(BlockFace.UP);
				break;
			case 3:
				arms.add(BlockFace.UP);
				break;
			case 4:
				arms.add(BlockFace.UP);
				break;
			case 5:
				arms.add(BlockFace.UP);
				break;
			case 6:
				arms.add(BlockFace.NORTH);
				break;
			case 7:
				arms.add(BlockFace.NORTH);
				break;
			case 8:
				arms.add(BlockFace.DOWN);
				break;
			default:
				emptyswitch = true;
				cs = currentTaskPiece.getChest();
				if (Helper.firstNonEmpty(cs.getChest().getInventory()) != null) {
					currentStep = CraneStep.GoingToChest;
					break;
				}
				break;
			}

			break;
		case Empty:

			switch (arms.size()) {
			case 1:
				arms.clear();
				arms.add(BlockFace.UP);
				arms.add(BlockFace.UP);
				break;
			case 2:
				arms.add(BlockFace.UP);
				break;
			case 3:
				arms.add(BlockFace.UP);
				break;
			case 4:
				arms.add(BlockFace.UP);
				break;
			case 5:
				arms.add(BlockFace.UP);
				break;
			case 6:
				arms.add(BlockFace.NORTH);
				break;
			case 7:
				arms.add(BlockFace.NORTH);
				break;
			case 8:
				arms.add(BlockFace.DOWN);
				break;
			default:
				if (currentTaskPiece != null)
					currentStep = CraneStep.GoingToChest;
				break;
			}

			break;
		case RetractToEmpty:
			if (currentTaskPiece == null) {
				if (arms.size() > 1) {
					arms.remove(arms.size() - 1);
					break;
				}
				if (arms.size() == 1) {
					currentStep = CraneStep.Empty;
					break;
				}
				break;
			}
			currentStep = CraneStep.GoingToChest;
			break;
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
					currentStep = CraneStep.RetractY;
				} else
					moveToLocation(head, loc);
			}
			break;
		case RetractY:
			int i;
			for (i = 0; i < arms.size(); i++) {
				if (arms.get(i) != BlockFace.UP)
					break;
			}
			if (i > 5) {
				arms.remove(0);
				break;
			} else if (i < 5) {
				arms.add(0, BlockFace.UP);
				break;
			} else if (i == 5) {
				BlockFace cur = null;
				int step = 0;
				for (i = 0; i < arms.size(); i++) {
					if (step == 0) {
						if (arms.get(i) != BlockFace.UP) {

							cur = arms.get(i);
							step = 1;
							continue;
						}
					} else if (step == 1) {
						if (arms.get(i) != cur) {
							step = 2;
							continue;
						}
					} else if (step == 2) {
						if (arms.get(i) == BlockFace.DOWN) {
							step = 3;
							continue;
						} else
							break;
					} else if (step == 3) {
						if (arms.get(i) == BlockFace.DOWN) {
							i++;
							break;
						} else {
							break;
						}
					}
				}
				if (i + 1 == arms.size()) {
					currentStep = CraneStep.GoingToSpot;// fall through;
				} else {
					arms.remove(arms.size() - 1);
					break;
				}
			} else {
				break;
			}
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
					currentStep = CraneStep.GoingToChest;
				}
			}
			break;
		}
		drawCrane(arms, true, null);
	}

	private boolean moveToLocation(Location head, Location loc) {

		if (!head.equals(loc)) {

			
			
/*			List<Location> farw = astar(head, loc);
			if (far == null) {
				if (arms.size() > 0)
					arms.remove(arms.size() - 1);
				return false;
			} else if (far.size() > 1) {

				List<Location> ls = new ArrayList<Location>();
				Block gc;
				ls.add((gc = block).getLocation());
				for (BlockFace f : arms) {
					ls.add((gc = gc.getRelative(f)).getLocation());
				}

				//BlockFace fc = head.getBlock().getFace(far.get(1).getBlock());

				switch (fc) {
				case DOWN:
				case UP:
					if (!tryblockadd(fc, ls, true)) {
						if (!tryblockmove(Helper.faceGetOpposite(fc), ls)) {
							arms.add(fc);
						}
					}
					break;
				case EAST:
				case NORTH:
				case SOUTH:
				case WEST:
					if (!tryblockmove(Helper.faceGetOpposite(fc), ls)) {
						if (!tryblockadd(fc, ls, true)) {
							if (!tryblockadd(fc, ls, false)) {
								arms.add(fc);
							}
						}
					}
					break;
				}

				return false;
			}
*/
		}
		return true;
	}

	private boolean tryblockmove(BlockFace fc, List<Location> ls) {
		BlockFace oldf = null;
		int index = 0;

		for (BlockFace f : arms) {
			if (f != oldf) {
				if (oldf != null) {
					// bend
					if (f == fc) {
						if (!tryRemove(index, arms, ls)) {
							index++;
							continue;
						}
						return true;
					}
				}
				oldf = f;
			}
			index++;
		}
		return false;
	}

	private boolean tryblockadd(BlockFace fc, List<Location> ls, boolean dontSkip) {
		BlockFace oldf = null;
		int index = 0;

		for (BlockFace f : arms) {
			if (f != oldf) {
				if (oldf != null) {
					// bend
					if (!dontSkip || f == fc) {
						if (!tryAdd(index, fc, arms, ls)) {
							index++;
							continue;
						}
						return true;
					}
				}
				oldf = f;
			}
			index++;
		}
		return false;
	}

	private boolean tryRemove(int fc, List<BlockFace> arms2, List<Location> ls) {

		ArrayList<BlockFace> fd = new ArrayList<BlockFace>(arms2);
		fd.remove(fc);
		if (drawCrane(fd, false, ls)) {
			arms = fd;
			return true;
		}

		return false;
	}

	private boolean tryAdd(int fc, BlockFace fc2, List<BlockFace> arms2, List<Location> ls) {

		ArrayList<BlockFace> fd = new ArrayList<BlockFace>(arms2);
		fd.add(fc, fc2);
		if (drawCrane(fd, false, ls)) {
			arms = fd;
			return true;
		}

		return false;
	}

	private boolean drawCrane(List<BlockFace> armsz, boolean print, List<Location> ls) {
		constructionJob.game.logic.clearReprint(block.getWorld(), "crane" + craneID);
		Block bl = block;
		for (BlockFace arm : armsz) {
			bl = bl.getRelative(arm);

			if (!print) {
				if (bl.getTypeId() != 0) {
					if (!ls.contains(bl.getLocation()))
						return false;
				}
			}
			if (print) {
				constructionJob.game.logic.addBlockForReprint(new GameItem(bl.getX(), bl.getY(), bl.getZ(), bl.getTypeId(), bl.getData(), "crane"
						+ craneID));
				bl.setType(Material.FENCE);
			}
		}

		if (headPiece != null) {
			if (print) {
				bl.setType(headPiece);
			}
		}
		return true;

	}

	public enum CraneStep {
		Empty, GoingToChest, GoingToSpot, EmptyWatchChest, RetractToEmpty, RetractY
	}

}
