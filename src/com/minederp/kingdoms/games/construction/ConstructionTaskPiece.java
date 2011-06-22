package com.minederp.kingdoms.games.construction;

public class ConstructionTaskPiece {
 

	private final ConstructionModelPiece constructionModelPiece;
	private final ConstructionChest chest;

	public ConstructionTaskPiece(ConstructionModelPiece constructionModelPiece,ConstructionChest chest) {
		this.constructionModelPiece = constructionModelPiece;
		this.chest = chest; 
	}

	public ConstructionChest getChest() {
		return chest;
	}

	public ConstructionModelPiece getConstructionModelPiece() {
		return constructionModelPiece;
	}

}
