package com.minederp.kingdoms.orm;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.minederp.kingdoms.KingdomsPlugin;

public class SpreadAnimation {

	public static SpreadAnimation makeSpreadAnimation(ResultSet st) {
		SpreadAnimation f = new SpreadAnimation();
		try {
			f.setSpreadAnimationID(st.getInt("SpreadAnimationID"));
			f.setAnimationSpeed(st.getInt("AnimationSpeed"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return f;
	}

	private static List<SpreadAnimation> loopThroughSpreadAnimation(ResultSet st) {
		List<SpreadAnimation> fm = new ArrayList<SpreadAnimation>();
		try {
			while (st.next()) {
				fm.add(makeSpreadAnimation(st));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fm;
	}

	private int _SpreadAnimationID;

	public int getSpreadAnimationID() {
		return _SpreadAnimationID;
	}

	public void setSpreadAnimationID(int aSpreadAnimationID) {
		_SpreadAnimationID = aSpreadAnimationID;
	}

	public static List<SpreadAnimation> getAllBySpreadAnimationID(int aSpreadAnimationID) {

		try {
			return loopThroughSpreadAnimation(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimation", "where SpreadAnimationID="
					+ aSpreadAnimationID + ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimation getFirstBySpreadAnimationID(int aSpreadAnimationID) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimation", "where SpreadAnimationID=" + aSpreadAnimationID + "");
			if (fc.next())
				return makeSpreadAnimation(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int _AnimationSpeed;

	public int getAnimationSpeed() {
		return _AnimationSpeed;
	}

	public void setAnimationSpeed(int aAnimationSpeed) {
		_AnimationSpeed = aAnimationSpeed;
	}

	public static List<SpreadAnimation> getAllByAnimationSpeed(int aAnimationSpeed) {

		try {
			return loopThroughSpreadAnimation(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimation", "where AnimationSpeed=" + aAnimationSpeed
					+ ""));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static SpreadAnimation getFirstByAnimationSpeed(int aAnimationSpeed) {
		try {
			ResultSet fc = KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimation", "where AnimationSpeed=" + aAnimationSpeed + "");
			if (fc.next())
				return makeSpreadAnimation(fc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<SpreadAnimation> getAll() {
		try {
			return loopThroughSpreadAnimation(KingdomsPlugin.wrapper.selectQuery("*", "SpreadAnimation", ""));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insert() {
		try {
			KingdomsPlugin.wrapper.insertQuery("SpreadAnimation", "default," + _AnimationSpeed + "");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		try {
			KingdomsPlugin.wrapper.updateQuery("UPDATE SpreadAnimation SET AnimationSpeed= " + _AnimationSpeed + " where SpreadAnimationID="
					+ _SpreadAnimationID + " ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object b) {
		if (b != null && b instanceof SpreadAnimation)
			if (((SpreadAnimation) b)._SpreadAnimationID == _SpreadAnimationID)
				return true;
		return false;

	}

	public void delete() {
		try {
			KingdomsPlugin.wrapper.deleteQuery("SpreadAnimation", "SpreadAnimationID = " + _SpreadAnimationID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}