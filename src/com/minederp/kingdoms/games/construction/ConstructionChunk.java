package com.minederp.kingdoms.games.construction;

public class ConstructionChunk {
	boolean[][][] chunk;
	int width;
	int height;
	int length;

	public ConstructionChunk(boolean[][][] chunk, int width, int height, int length) {
		this.chunk = chunk;
		this.width = width;
		this.height = height;
		this.length = length;
	}
}

