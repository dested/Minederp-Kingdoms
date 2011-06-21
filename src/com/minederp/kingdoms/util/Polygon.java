package com.minederp.kingdoms.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
	List<PolygonPoint> points = new ArrayList<PolygonPoint>();
	java.awt.Polygon myPoly = new java.awt.Polygon();

	public Polygon() {

	}

	public void add(PolygonPoint p) {
		points.add(p);
		myPoly.addPoint(p.X, p.Z);
	}

	public void add(int index, PolygonPoint p) {
		points.add(index, p);

		myPoly = new java.awt.Polygon();
		int ind = 0;
		for (PolygonPoint pc : points) {
			if (index == ind)
				myPoly.addPoint(p.X, p.Z);
			myPoly.addPoint(pc.X, pc.Z);
			ind++;
		}

	}

	public boolean contains(Point p) {
		return myPoly.contains(p);
	}

	public boolean contains(int x, int z) {
		return myPoly.contains(new Point(x, z));
	}

	public void remove(int X, int Y, int Z) {

		for (PolygonPoint p : points) {
			if (p.X == X && p.Y == Y && p.Z == Z) {
				points.remove(p);
				break;
			}
		}

		myPoly = new java.awt.Polygon();
		for (PolygonPoint p : points) {
			myPoly.addPoint(p.X, p.Z);
		}
	}

	public int size() {
		return points.size();
	}

	public PolygonPoint get(int i) {
		return points.get(i);
	}
}