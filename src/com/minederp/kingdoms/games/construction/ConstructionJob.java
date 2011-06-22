package com.minederp.kingdoms.games.construction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.entity.Player;

import com.minederp.kingdoms.util.Helper;

public class ConstructionJob {
	final ConstructionGame game;
	private List<ConstructionCrane> cranes;
	private List<ConstructionChest> chests;
	private List<ConstructionTask> tasks;
	private String jobName;
	public Player addingChestPlayer;
	public Player addingCranePlayer;

	public Player addingTaskPlayer;
	private Player addingTaskPlayerChest;
	public ConstructionModel addingTaskPlayerModel;
	private ConstructionTask addingTaskPlayerChestTask;

	public ConstructionJob(ConstructionGame game, String name) {
		this.game = game;
		setJobName(name);
		setCranes(new ArrayList<ConstructionCrane>());
		setChests(new ArrayList<ConstructionChest>());
		tasks = (new ArrayList<ConstructionTask>());
	}

	public ConstructionGame getGame() {
		return game;
	}

	public void setCranes(List<ConstructionCrane> cranes) {
		this.cranes = cranes;
	}

	public List<ConstructionCrane> getCranes() {
		return cranes;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setChests(List<ConstructionChest> chests) {
		this.chests = chests;
	}

	public List<ConstructionChest> getChests() {
		return chests;
	}

	public boolean blockClick(Block block, Player player) {
		if (addingChestPlayer != null && addingChestPlayer.equals(player)) {
			if (block.getType() == Material.CHEST) {
				addingChestPlayer = null;
				chests.add(new ConstructionChest(this, new CraftChest(block)));
				player.sendMessage("Chest added to job: " + jobName);
				return true;
			}
		}
		if (addingCranePlayer != null && addingCranePlayer.equals(player)) {
			if (block.getType() == Material.GOLD_BLOCK) {
				addingCranePlayer = null;
				cranes.add(new ConstructionCrane(this, block));
				player.sendMessage("Crane added to job: " + jobName);
				return true;
			}
		}
		if (addingTaskPlayer != null && addingTaskPlayer.equals(player)) {
			addingTaskPlayerChest = addingTaskPlayer;

			addingTaskPlayerChestTask = new ConstructionTask(this, addingTaskPlayerModel, block);
			addingTaskPlayer = null;
			addingTaskPlayerModel = null;

			player.sendMessage("Task added to job: " + jobName);
			player.sendMessage("The next chest that you click will be assigned to this task.");
			block.setType(Material.OBSIDIAN);
			return true;
		}
		if (addingTaskPlayerChest != null && addingTaskPlayerChest.equals(player)) {
			for (ConstructionChest chest : chests) {
				Location loc = chest.getChest().getBlock().getLocation();
				if (block.getLocation().equals(loc)) {
					tasks.add(addingTaskPlayerChestTask);
					addingTaskPlayerChestTask.addConstructionChests(chest);
					if (addingTaskPlayerChestTask.getModel().Chunks.size() == addingTaskPlayerChestTask.constructionChests.size()) {
						addingTaskPlayerChest = null;
						addingTaskPlayerChestTask = null;
						player.sendMessage("Chest set to task. The task will begin immediatly.");
					} else {
						player.sendMessage("Please add another chest.");
					}
					Block d;
					(d = block.getRelative(BlockFace.DOWN)).setType(Material.BRICK);
					d.getRelative(-1, 0, -1).setType(Material.BRICK);
					d.getRelative(-1, 0, 0).setType(Material.BRICK);
					d.getRelative(-1, 0, 1).setType(Material.BRICK);
					d.getRelative(1, 0, -1).setType(Material.BRICK);
					d.getRelative(1, 0, 0).setType(Material.BRICK);
					d.getRelative(1, 0, 1).setType(Material.BRICK);
					d.getRelative(0, 0, -1).setType(Material.BRICK);
					d.getRelative(0, 0, 0).setType(Material.BRICK);
					d.getRelative(0, 0, 1).setType(Material.BRICK);
					return true;
				}
			}
		}
		return false;
	}

	public void jobTick() {
		for (ConstructionCrane crane : cranes) {
			crane.tick();
		}
	}

	protected ConstructionTask getNextTask(Location loc) {
		if (tasks.size() == 0)
			return null;
		double[] distances = new double[tasks.size()];
		int index = 0;
		for (ConstructionTask task : tasks) {
			Location loc1 = task.block.getLocation();
			distances[index] = loc1.distance(loc);
			index++;
		}
		return tasks.get(Helper.lowestIndex(distances));
	}
}
