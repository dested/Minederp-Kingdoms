package com.minederp.kingdoms.util;

import java.awt.Point;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
	List<PolygonPoint> points = new ArrayList<PolygonPoint>();
	java.awt.Polygon myPoly = new java.awt.Polygon();

	public Polygon() {

	}

	public static Polygon deserialize(String content) {

		Polygon p = new Polygon();
		if (content == null || content.length() == 0)
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

	public boolean collides(Polygon p) {
		return intersects(p);
	}

	private boolean intersects(Polygon shape) {
		/*
		 * Intersection formula used: (x4 - x3)(y1 - y3) - (y4 - y3)(x1 - x3) UA
		 * = --------------------------------------- (y4 - y3)(x2 - x1) - (x4 -
		 * x3)(y2 - y1)
		 * 
		 * (x2 - x1)(y1 - y3) - (y2 - y1)(x1 - x3) UB =
		 * --------------------------------------- (y4 - y3)(x2 - x1) - (x4 -
		 * x3)(y2 - y1)
		 * 
		 * if UA and UB are both between 0 and 1 then the lines intersect.
		 * 
		 * Source: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
		 */

		boolean result = false;
		Float points[] = getPoints(); // (x3, y3) and (x4, y4)
		Float thatPoints[] = shape.getPoints(); // (x1, y1) and (x2, y2)
		int length = points.length;
		int thatLength = thatPoints.length;
		double unknownA;
		double unknownB;

		// x1 = thatPoints[j]
		// x2 = thatPoints[j + 2]
		// y1 = thatPoints[j + 1]
		// y2 = thatPoints[j + 3]
		// x3 = points[i]
		// x4 = points[i + 2]
		// y3 = points[i + 1]
		// y4 = points[i + 3]
		for (int i = 0; i < length; i += 2) {
			int iNext = i + 2;
			if (iNext >= points.length) {
				iNext = 0;
			}

			for (int j = 0; j < thatLength; j += 2) {
				int jNext = j + 2;
				if (jNext >= thatPoints.length) {
					jNext = 0;
				}

				unknownA = (((points[iNext] - points[i]) * (double) (thatPoints[j + 1] - points[i + 1])) - ((points[iNext + 1] - points[i + 1]) * (thatPoints[j] - points[i])))
						/ (((points[iNext + 1] - points[i + 1]) * (thatPoints[jNext] - thatPoints[j])) - ((points[iNext] - points[i]) * (thatPoints[jNext + 1] - thatPoints[j + 1])));
				unknownB = (((thatPoints[jNext] - thatPoints[j]) * (double) (thatPoints[j + 1] - points[i + 1])) - ((thatPoints[jNext + 1] - thatPoints[j + 1]) * (thatPoints[j] - points[i])))
						/ (((points[iNext + 1] - points[i + 1]) * (thatPoints[jNext] - thatPoints[j])) - ((points[iNext] - points[i]) * (thatPoints[jNext + 1] - thatPoints[j + 1])));

				if (unknownA >= 0 && unknownA <= 1 && unknownB >= 0 && unknownB <= 1) {
					result = true;
					break;
				}
			}
			if (result) {
				break;
			}
		}

		return result;
	}

	private Float[] getPoints() {
		ArrayList<Float> tempPoints = new ArrayList<Float>();
		for (int i = 0; i < myPoly.npoints; i++) {
			tempPoints.add(new Float(myPoly.xpoints[i]));
			tempPoints.add(new Float(myPoly.ypoints[i]));
		}
		Float[] f = new Float[myPoly.npoints * 2];
		tempPoints.toArray(f);
		return f;
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

	public void remove(PolygonPoint pc) {
		points.remove(pc);
		myPoly = new java.awt.Polygon();
		for (PolygonPoint p : points) {
			myPoly.addPoint(p.X, p.Z);
		}

	}
}