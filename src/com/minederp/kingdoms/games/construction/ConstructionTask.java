package com.minederp.kingdoms.games.construction;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.minederp.kingdoms.util.Helper;

public class ConstructionTask {

	public final Block block;
	private final ConstructionJob constructionJob;
	public List<ConstructionChest> constructionChests;

	private ConstructionModelPrinter modelPrinter;

	public ConstructionTask(ConstructionJob constructionJob, ConstructionModel model, Block block) {
		this.constructionJob = constructionJob;
		this.block = block;
		constructionChests = new ArrayList<ConstructionChest>();
		modelPrinter = new ConstructionModelPrinter(model, constructionChests, block.getLocation());
	}

	public ConstructionTaskPiece getNextPiece(int chunkIndex) {
		return modelPrinter.getNextPiece(chunkIndex);
	}

	public ConstructionModel getModel() {
		return modelPrinter.model;
	}

	public void addConstructionChests(ConstructionChest constructionChests) {
		this.constructionChests.add(constructionChests);
	}

	public int getChunkIndex(Location loc) {
		double[] distances = new double[constructionChests.size()];
		int index = 0;
		for (ConstructionChest task : constructionChests) {
			Location loc1 = task.getChest().getBlock().getLocation();
			distances[index] = loc1.distance(loc);
			index++;
		}
		return Helper.lowestIndex(distances);

	}

}
