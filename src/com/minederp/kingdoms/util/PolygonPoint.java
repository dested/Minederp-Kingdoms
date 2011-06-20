package com.minederp.kingdoms.util;

public class PolygonPoint {
	public PolygonPoint(int x, int y, int z) {
		X = x;
		Y = y;
		Z = z;
	}

	public int X;
	public int Y;
	public int Z;

	public void setX(int x) {
		X = x;
	}

	public int getX() {
		return X;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getY() {
		return Y;
	}

	public void setZ(int z) {
		Z = z;
	}

	public int getZ() {
		return Z;
	}
}