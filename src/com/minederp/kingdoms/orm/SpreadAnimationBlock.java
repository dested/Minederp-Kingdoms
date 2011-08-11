package com.minederp.kingdoms.orm;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.minederp.kingdoms.KingdomsPlugin;

public class SpreadAnimationBlock {

	public static SpreadAnimationBlock makeSpreadAnimationBlock(ResultSet st) {
		SpreadAnimationBlock f = new SpreadAnimationBlock();
		try {
			f.setSpreadAnimationBlockID(st.getInt("SpreadAnimationBlockID"));
			f.setSpreadAnimationID(st.getInt("SpreadAnimationID"));
			f.setFrameIndex(st.getInt("FrameIndex"));
			f.setBlockX(st.getInt("BlockX"));
			f.setBlockY(st.getInt("BlockY"));
			f.setBlockZ(st.getInt("BlockZ"));
			f.setMaterialID(st.getInt("MaterialID"));
			f.setDataID(st.getInt("DataID"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return f;
	}

	private static List<SpreadAnimationBlock> loopThroughSpreadAnimationBlock(ResultSet st) {
		List<SpreadAnimationBlock> fm = new ArrayList<SpreadAnimationBlock>();
		try {
			while (st.next()) {
				fm.add(makeSpreadAnimationBlock(st));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fm;
	}

	private int _SpreadAnimationBlockID;

	public int getSpreadAnimationBlockID() {
		return _SpreadAnimationBlockID;
	}

	public void setSpreadAnimationBlockID(int aSpreadAnimationBlockID) {
		_SpreadAnimationBlockID = aSpreadAnimationBlockID;
	}

	public static List<SpreadAnimationBlock> getAllBySpreadAnimationBlockID(int aSpreadAnimationBlockID) {

		try {
			return loopThroughSpreadAnimationBlock(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where SpreadAnimationBlockID="
					+ aSpreadAnimationBlockID + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimationBlock getFirstBySpreadAnimationBlockID(int aSpreadAnimationBlockID) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where SpreadAnimationBlockID=" + aSpreadAnimationBlockID
					+ "");
			if (fc.next())
				return makeSpreadAnimationBlock(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public SpreadAnimation getSpreadAnimation() {
		return SpreadAnimation.getFirstBySpreadAnimationID(_SpreadAnimationID);
	}

	private int _SpreadAnimationID;

	public int getSpreadAnimationID() {
		return _SpreadAnimationID;
	}

	public void setSpreadAnimationID(int aSpreadAnimationID) {
		_SpreadAnimationID = aSpreadAnimationID;
	}

	public static List<SpreadAnimationBlock> getAllBySpreadAnimationID(int aSpreadAnimationID) {

		try {
			return loopThroughSpreadAnimationBlock(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where SpreadAnimationID="
					+ aSpreadAnimationID + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimationBlock getFirstBySpreadAnimationID(int aSpreadAnimationID) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where SpreadAnimationID=" + aSpreadAnimationID + "");
			if (fc.next())
				return makeSpreadAnimationBlock(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int _FrameIndex;

	public int getFrameIndex() {
		return _FrameIndex;
	}

	public void setFrameIndex(int aFrameIndex) {
		_FrameIndex = aFrameIndex;
	}

	public static List<SpreadAnimationBlock> getAllByFrameIndex(int aFrameIndex) {

		try {
			return loopThroughSpreadAnimationBlock(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where FrameIndex=" + aFrameIndex
					+ ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimationBlock getFirstByFrameIndex(int aFrameIndex) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where FrameIndex=" + aFrameIndex + "");
			if (fc.next())
				return makeSpreadAnimationBlock(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int _BlockX;

	public int getBlockX() {
		return _BlockX;
	}

	public void setBlockX(int aBlockX) {
		_BlockX = aBlockX;
	}

	public static List<SpreadAnimationBlock> getAllByBlockX(int aBlockX) {

		try {
			return loopThroughSpreadAnimationBlock(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where BlockX=" + aBlockX + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimationBlock getFirstByBlockX(int aBlockX) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where BlockX=" + aBlockX + "");
			if (fc.next())
				return makeSpreadAnimationBlock(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int _BlockY;

	public int getBlockY() {
		return _BlockY;
	}

	public void setBlockY(int aBlockY) {
		_BlockY = aBlockY;
	}

	public static List<SpreadAnimationBlock> getAllByBlockY(int aBlockY) {

		try {
			return loopThroughSpreadAnimationBlock(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where BlockY=" + aBlockY + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimationBlock getFirstByBlockY(int aBlockY) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where BlockY=" + aBlockY + "");
			if (fc.next())
				return makeSpreadAnimationBlock(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int _BlockZ;

	public int getBlockZ() {
		return _BlockZ;
	}

	public void setBlockZ(int aBlockZ) {
		_BlockZ = aBlockZ;
	}

	public static List<SpreadAnimationBlock> getAllByBlockZ(int aBlockZ) {

		try {
			return loopThroughSpreadAnimationBlock(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where BlockZ=" + aBlockZ + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimationBlock getFirstByBlockZ(int aBlockZ) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where BlockZ=" + aBlockZ + "");
			if (fc.next())
				return makeSpreadAnimationBlock(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int _MaterialID;

	public int getMaterialID() {
		return _MaterialID;
	}

	public void setMaterialID(int aMaterialID) {
		_MaterialID = aMaterialID;
	}

	public static List<SpreadAnimationBlock> getAllByMaterialID(int aMaterialID) {

		try {
			return loopThroughSpreadAnimationBlock(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where MaterialID=" + aMaterialID
					+ ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimationBlock getFirstByMaterialID(int aMaterialID) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where MaterialID=" + aMaterialID + "");
			if (fc.next())
				return makeSpreadAnimationBlock(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int _DataID;

	public int getDataID() {
		return _DataID;
	}

	public void setDataID(int aDataID) {
		_DataID = aDataID;
	}

	public static List<SpreadAnimationBlock> getAllByDataID(int aDataID) {

		try {
			return loopThroughSpreadAnimationBlock(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where DataID=" + aDataID + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimationBlock getFirstByDataID(int aDataID) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", "where DataID=" + aDataID + "");
			if (fc.next())
				return makeSpreadAnimationBlock(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<SpreadAnimationBlock> getAll() {
		try {
			return loopThroughSpreadAnimationBlock(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimationBlock", ""));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insert() {
		try {
			KingdomsPlugin.wrapper.insertQuery("SpreadAnimationBlock", "default," + _SpreadAnimationID + ", " + _FrameIndex + ", " + _BlockX + ", "
					+ _BlockY + ", " + _BlockZ + ", " + _MaterialID + ", " + _DataID + "");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		try {
			KingdomsPlugin.wrapper.updateQuery("UPDATE SpreadAnimationBlock SET SpreadAnimationID= " + _SpreadAnimationID + " , FrameIndex= "
					+ _FrameIndex + " , BlockX= " + _BlockX + " , BlockY= " + _BlockY + " , BlockZ= " + _BlockZ + " , MaterialID= " + _MaterialID
					+ " , DataID= " + _DataID + " where SpreadAnimationBlockID=" + _SpreadAnimationBlockID + " ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object b) {
		if (b != null && b instanceof SpreadAnimationBlock)
			if (((SpreadAnimationBlock) b)._SpreadAnimationBlockID == _SpreadAnimationBlockID)
				return true;
		return false;

	}

	public void delete() {
		try {
			KingdomsPlugin.wrapper.deleteQuery("SpreadAnimationBlock", "SpreadAnimationBlockID = " + _SpreadAnimationBlockID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}