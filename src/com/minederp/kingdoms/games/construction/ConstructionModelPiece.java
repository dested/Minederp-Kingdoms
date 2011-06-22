package com.minederp.kingdoms.games.construction;

public class ConstructionModelPiece {

	private final int printX;
	private final int printZ;
	private final int printY;

	public ConstructionModelPiece(final int printX, final int printY, final int printZ) {
		this.printX = printX;
		this.printY = printY; 
		this.printZ = printZ;
	}

	public int getPrintX() {
		return printX;
	}

	public int getPrintY() {
		return printY;
	}

	public int getPrintZ() {
		return printZ;
	}

}
