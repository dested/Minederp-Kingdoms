package com.minederp.kingdoms.orm;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.minederp.kingdoms.KingdomsPlugin;

public class Town {

	public static Town makeTown(ResultSet st) {
		Town f = new Town();
		try {
			f.setTownID(st.getInt("TownID"));
			f.setTownName(st.getString("TownName"));
			f.setTownSpawn(st.getString("TownSpawn"));
			f.setTownHeart(st.getString("TownHeart"));
			f.setKingdomID(st.getInt("KingdomID"));
			f.setGovernorID(st.getInt("GovernorID"));
			f.setTownPolygon(st.getString("TownPolygon"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return f;
	}

	private static List<Town> loopThroughTown(ResultSet st) {
		List<Town> fm = new ArrayList<Town>();
		try {
			while (st.next()) {
				fm.add(makeTown(st));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fm;
	}

	private int _TownID;

	public int getTownID() {
		return _TownID;
	}

	public void setTownID(int aTownID) {
		_TownID = aTownID;
	}

	public static List<Town> getAllByTownID(int aTownID) {

		try {
			return loopThroughTown(KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownID=" + aTownID + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Town getFirstByTownID(int aTownID) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownID=" + aTownID + "");
			if (fc.next())
				return makeTown(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String _TownName;

	public String getTownName() {
		return _TownName;
	}

	public void setTownName(String aTownName) {
		_TownName = aTownName;
	}

	public static List<Town> getAllByTownName(String aTownName) {

		try {
			return loopThroughTown(KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownName='" + aTownName + "'"));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Town getFirstByTownName(String aTownName) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownName='" + aTownName + "'");
			if (fc.next())
				return makeTown(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String _TownSpawn;

	public String getTownSpawn() {
		return _TownSpawn;
	}

	public void setTownSpawn(String aTownSpawn) {
		_TownSpawn = aTownSpawn;
	}

	public static List<Town> getAllByTownSpawn(String aTownSpawn) {

		try {
			return loopThroughTown(KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownSpawn='" + aTownSpawn + "'"));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Town getFirstByTownSpawn(String aTownSpawn) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownSpawn='" + aTownSpawn + "'");
			if (fc.next())
				return makeTown(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String _TownHeart;

	public String getTownHeart() {
		return _TownHeart;
	}

	public void setTownHeart(String aTownHeart) {
		_TownHeart = aTownHeart;
	}

	public static List<Town> getAllByTownHeart(String aTownHeart) {

		try {
			return loopThroughTown(KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownHeart='" + aTownHeart + "'"));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Town getFirstByTownHeart(String aTownHeart) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownHeart='" + aTownHeart + "'");
			if (fc.next())
				return makeTown(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Kingdom getKingdom() {
		return Kingdom.getFirstByKingdomID(_KingdomID);
	}

	private int _KingdomID;

	public int getKingdomID() {
		return _KingdomID;
	}

	public void setKingdomID(int aKingdomID) {
		_KingdomID = aKingdomID;
	}

	public static List<Town> getAllByKingdomID(int aKingdomID) {

		try {
			return loopThroughTown(KingdomsPlugin.wrapper.selectQuery("*", "Town", "where KingdomID=" + aKingdomID + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Town getFirstByKingdomID(int aKingdomID) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Town", "where KingdomID=" + aKingdomID + "");
			if (fc.next())
				return makeTown(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public KingdomPlayer getGovernor() {
		return KingdomPlayer.getFirstByKingdomPlayerID(_GovernorID);
	}

	private int _GovernorID;

	public int getGovernorID() {
		return _GovernorID;
	}

	public void setGovernorID(int aGovernorID) {
		_GovernorID = aGovernorID;
	}

	public static List<Town> getAllByGovernorID(int aGovernorID) {

		try {
			return loopThroughTown(KingdomsPlugin.wrapper.selectQuery("*", "Town", "where GovernorID=" + aGovernorID + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Town getFirstByGovernorID(int aGovernorID) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Town", "where GovernorID=" + aGovernorID + "");
			if (fc.next())
				return makeTown(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String _TownPolygon;

	public String getTownPolygon() {
		return _TownPolygon;
	}

	public void setTownPolygon(String aTownPolygon) {
		_TownPolygon = aTownPolygon;
	}

	public static List<Town> getAllByTownPolygon(String aTownPolygon) {

		try {
			return loopThroughTown(KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownPolygon='" + aTownPolygon + "'"));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Town getFirstByTownPolygon(String aTownPolygon) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "Town", "where TownPolygon='" + aTownPolygon + "'");
			if (fc.next())
				return makeTown(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<Town> getAll() {
		try {
			return loopThroughTown(KingdomsPlugin.wrapper.selectQuery("*", "Town", ""));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insert() {
		try {
			KingdomsPlugin.wrapper.insertQuery("Town", "default,'" + _TownName + "', '" + _TownSpawn + "', '" + _TownHeart + "', " + _KingdomID
					+ ", " + _GovernorID + ", '" + _TownPolygon + "'");

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		try {
			KingdomsPlugin.wrapper.updateQuery("UPDATE Town where TownID=" + _TownID + " SET TownName= '" + _TownName + "' , TownSpawn= '"
					+ _TownSpawn + "' , TownHeart= '" + _TownHeart + "' , KingdomID= " + _KingdomID + " , GovernorID= " + _GovernorID
					+ " , TownPolygon= '" + _TownPolygon + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try {
			KingdomsPlugin.wrapper.deleteQuery("Town", "TownID = " + _TownID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}