package com.minederp.kingdoms.games.kingdoms.content;

import com.minederp.kingdoms.util.Polygon;

public interface PolygonChecker {
	public void save(Polygon polygon);
	public boolean collides(Polygon polygon);
}
