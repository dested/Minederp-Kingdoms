package com.minederp.kingdoms.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
	List<PolygonPoint> points = new ArrayList<PolygonPoint>();
	java.awt.Polygon myPoly = new java.awt.Polygon();

	public Polygon() {

	}

	public static Polygon deserialize(String content) {

		Polygon p = new Polygon();
		if (content==null || content.length() == 0)
			return p;
		for (String item : content.split("&")) {
			String[] locs = item.split(",");
			p.add(new PolygonPoint(Integer.parseInt(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2])));
		}
		return p;

	}

	public String serialize() {
		StringBuffer sb = new StringBuffer();

		for (PolygonPoint pc : points) {
			sb.append(pc.X + "," + pc.Y + "," + pc.Z + "&");
		}

		return sb.toString();
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

	public void setIndex(int polygonMoveIndex, int x, int y, int z) {
		points.set(polygonMoveIndex, new PolygonPoint(x, y, z));
		myPoly = new java.awt.Polygon();
		for (PolygonPoint p : points) {
			myPoly.addPoint(p.X, p.Z);
		}
	}
}