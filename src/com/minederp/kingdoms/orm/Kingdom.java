package com.minederp.kingdoms.orm;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.minederp.kingdoms.KingdomsPlugin;

public class Kingdom {

	public static Kingdom makeKingdom(ResultSet st) {
		Kingdom f = new Kingdom();
		try {
			f.setKingdomID(st.getInt("KingdomID"));
			f.setKingdomName(st.getString("KingdomName"));
			f.setKingdomSpawn(st.getString("KingdomSpawn"));
			f.setKingdomHeart(st.getString("KingdomHeart"));
			f.setKingdomHomePolygon(st.getString("KingdomHomePolygon"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return f;
	}

	private static List<Kingdom> loopThroughKingdom(ResultSet st) {
		List<Kingdom> fm = new ArrayList<Kingdom>();
		try {
			while (st.next()) {
				fm.add(makeKingdom(st));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fm;
	}

	private int _KingdomID;

	public int getKingdomID() {
		return _KingdomID;
	}

	public void setKingdomID(int aKingdomID) {
		_KingdomID = aKingdomID;
	}

	public static List<Kingdom> getAllByKingdomID(int aKingdomID) {

		try {
			return loopThroughKingdom(KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomID=" + aKingdomID + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Kingdom getFirstByKingdomID(int aKingdomID) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomID=" + aKingdomID + "");
			if (fc.next())
				return makeKingdom(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String _KingdomName;

	public String getKingdomName() {
		return _KingdomName;
	}

	public void setKingdomName(String aKingdomName) {
		if (aKingdomName == null || aKingdomName.equals("null"))
			return;
		_KingdomName = aKingdomName;
	}

	public static List<Kingdom> getAllByKingdomName(String aKingdomName) {

		try {
			return loopThroughKingdom(KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomName='" + aKingdomName + "'"));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Kingdom getFirstByKingdomName(String aKingdomName) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomName='" + aKingdomName + "'");
			if (fc.next())
				return makeKingdom(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String _KingdomSpawn;

	public String getKingdomSpawn() {
		return _KingdomSpawn;
	}

	public void setKingdomSpawn(String aKingdomSpawn) {
		if (aKingdomSpawn == null || aKingdomSpawn.equals("null"))
			return;
		_KingdomSpawn = aKingdomSpawn;
	}

	public static List<Kingdom> getAllByKingdomSpawn(String aKingdomSpawn) {

		try {
			return loopThroughKingdom(KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomSpawn='" + aKingdomSpawn + "'"));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Kingdom getFirstByKingdomSpawn(String aKingdomSpawn) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomSpawn='" + aKingdomSpawn + "'");
			if (fc.next())
				return makeKingdom(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String _KingdomHeart;

	public String getKingdomHeart() {
		return _KingdomHeart;
	}

	public void setKingdomHeart(String aKingdomHeart) {
		if (aKingdomHeart == null || aKingdomHeart.equals("null"))
			return;
		_KingdomHeart = aKingdomHeart;
	}

	public static List<Kingdom> getAllByKingdomHeart(String aKingdomHeart) {

		try {
			return loopThroughKingdom(KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomHeart='" + aKingdomHeart + "'"));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Kingdom getFirstByKingdomHeart(String aKingdomHeart) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomHeart='" + aKingdomHeart + "'");
			if (fc.next())
				return makeKingdom(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String _KingdomHomePolygon;

	public String getKingdomHomePolygon() {
		return _KingdomHomePolygon;
	}

	public void setKingdomHomePolygon(String aKingdomHomePolygon) {
		if (aKingdomHomePolygon == null || aKingdomHomePolygon.equals("null"))
			return;
		_KingdomHomePolygon = aKingdomHomePolygon;
	}

	public static List<Kingdom> getAllByKingdomHomePolygon(String aKingdomHomePolygon) {

		try {
			return loopThroughKingdom(KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomHomePolygon='" + aKingdomHomePolygon + "'"));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Kingdom getFirstByKingdomHomePolygon(String aKingdomHomePolygon) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", "where KingdomHomePolygon='" + aKingdomHomePolygon + "'");
			if (fc.next())
				return makeKingdom(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<Kingdom> getAll() {
		try {
			return loopThroughKingdom(KingdomsPlugin.wrapper.selectQuery("*", "Kingdom", ""));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insert() {
		try {
			KingdomsPlugin.wrapper.insertQuery("Kingdom", "default,'" + _KingdomName + "', '" + _KingdomSpawn + "', '" + _KingdomHeart + "', '"
					+ _KingdomHomePolygon + "'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		try {
			KingdomsPlugin.wrapper.updateQuery("UPDATE Kingdom SET KingdomName= '" + _KingdomName + "' , KingdomSpawn= '" + _KingdomSpawn
					+ "' , KingdomHeart= '" + _KingdomHeart + "' , KingdomHomePolygon= '" + _KingdomHomePolygon + "' where KingdomID=" + _KingdomID
					+ " ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object b) {
		if (b instanceof Kingdom)
			if (((Kingdom) b)._KingdomID == _KingdomID)
				return true;
		return false;

	}

	public void delete() {
		try {
			KingdomsPlugin.wrapper.deleteQuery("Kingdom", "KingdomID = " + _KingdomID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}