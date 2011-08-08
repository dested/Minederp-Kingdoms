package com.minederp.kingdoms.towns.content;

import com.minederp.kingdoms.util.Polygon;

public interface PolygonChecker {
	public void save(Polygon polygon);
	public boolean collides(Polygon polygon);
}
