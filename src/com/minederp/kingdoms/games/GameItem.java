package com.minederp.kingdoms.games;

public class GameItem {
	String Key;
	final boolean drop;

	public GameItem(int x2, int y2, int z2, int typeId, byte data, String key) {
		X = x2;
		Y = y2;
		Z = z2;
		Data = data;
		Type = typeId;
		Key = key;
		this.drop = false;
	}

	public GameItem(int x2, int y2, int z2, int typeId, byte data, String key, boolean drop) {
		X = x2;
		Y = y2;
		Z = z2;
		Data = data;
		Type = typeId;
		Key = key;
		this.drop = drop;
	}

	int Type;
	byte Data;
	int X;
	int Y;
	int Z;
}