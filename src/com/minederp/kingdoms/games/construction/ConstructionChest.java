package com.minederp.kingdoms.games.construction;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.block.CraftChest;

public class ConstructionChest {

	private ConstructionJob job;
	private Chest chest;

	public ConstructionChest(ConstructionJob job, Chest chest) {
		this.setJob(job);
		this.setChest(chest);
	}

	public void setChest(Chest chest) {
		this.chest = chest;
	}

	public Chest getChest() {
		chest=new CraftChest(chest.getBlock());
		return chest;
	}

	public void setJob(ConstructionJob job) {
		this.job = job;
	}

	public ConstructionJob getJob() {
		return job;
	}

}
