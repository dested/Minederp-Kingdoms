package com.minederp.kingdoms.games.construction;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class ConstructionModelPrinter {

	final ConstructionModel model;
	private final List<ConstructionChest> constructionChests;
	private final Location homeblock;

	public ConstructionModelPrinter(ConstructionModel model, List<ConstructionChest> constructionChests, Location homeblock) {
		this.model = model;
		this.constructionChests = constructionChests;
		this.homeblock = homeblock;
		alreadyPrinted = new ArrayList<ConstructionChunkPrinter>(model.Chunks.size());

		for (ConstructionChunk chunk : model.Chunks) {
			alreadyPrinted.add(new ConstructionChunkPrinter(chunk));
		}
	}

	List<ConstructionChunkPrinter> alreadyPrinted;

	public ConstructionTaskPiece getNextPiece(int chunkIndex) {
		ConstructionChest chest;

		ConstructionChunkPrinter dc = alreadyPrinted.get(chunkIndex);

		while (true) {
			if (dc.printX >= dc.chunk.width) {
				dc.printX = 0;
				dc.printZ++;
			}
			if (dc.printZ >= dc.chunk.length) {
				dc.printX = 0;
				dc.printZ = 0;
				dc.printY++;
			}
			if (dc.printY >= dc.chunk.height) {
				return null;
			}
			if (dc.chunk.chunk[dc.printX][dc.printY][dc.printZ])
				return new ConstructionTaskPiece(new ConstructionModelPiece((dc.printX++) + homeblock.getBlockX(), dc.printY + homeblock.getBlockY(),
						dc.printZ + homeblock.getBlockZ()), constructionChests.get(chunkIndex));
			dc.printX++;
		}
	}
}
