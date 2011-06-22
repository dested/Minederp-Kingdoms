package com.minederp.kingdoms.games.construction;

import java.util.ArrayList;
import java.util.List;

public class ConstructionModel {
	private final String modelName;

	public ConstructionModel(String mn) {
		modelName = mn;
	}

	public ConstructionModelPiece getNextPiece() {

		return null;
	}

	public String getModelName() {
		return modelName;
	}

	List<ConstructionChunk> Chunks = new ArrayList<ConstructionChunk>();

	public void addModelChunk(ConstructionChunk cn) {
		Chunks.add(cn);
	}

}
