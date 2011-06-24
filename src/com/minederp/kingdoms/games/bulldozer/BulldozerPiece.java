package com.minederp.kingdoms.games.bulldozer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import sun.security.action.GetBooleanAction;

import com.minederp.kingdoms.games.GameItem;
import com.minederp.kingdoms.util.Helper;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.commands.WorldEditCommands;

public class BulldozerPiece {
	private static int IDS = 0;
	private int dozerID = IDS++;
	private Block engine;
	private final BulldozerGame game;
	final BlockFace facing;

	public BulldozerPiece(BulldozerGame game, Block engine) {
		this.game = game;
		this.engine = engine;

		facing = BlockFace.NORTH;

	}

	private boolean running = false;

	private int counter = 0;

	public void tick() {
		running = true;
		if (!running)
			return;

		if (counter % 8 == 0)
			engine = engine.getRelative(facing);
		counter++;
		bladeIndex++;
		drawBlocks();
	}

	int bladeIndex = 0;
	int bladeLength =15;

	private void drawBlocks() {
		game.logic.clearReprint(engine.getWorld(), "dozer" + dozerID);

		Block bl = engine.getRelative(facing);

		Block tl = bl.getRelative(0, -1, 0).getRelative(Helper.faceGetLeft(facing));

		for (int i = 0; i < bladeLength; i++) {
			tl = tl.getRelative(Helper.faceGetRight(facing));
		}

		BlockFace right = Helper.faceGetRight(facing);
//		Helper.drawRectangle(bl.getRelative(BlockFace.UP).getRelative(Helper.faceGetLeft(facing)), tl, Material.OBSIDIAN);


		bl = bl.getRelative(facing);

		drawBlade(bl.getRelative(facing));

	}

	private Block bladePos(Block bl, int index) {

		switch (index) {
		case 0:
			return bl.getRelative(BlockFace.DOWN).getRelative(facing);
		case 1:
			return bl.getRelative(facing);
		case 2:
			return bl.getRelative(BlockFace.UP).getRelative(facing);
		case 3:
			return bl.getRelative(BlockFace.UP);
		case 4:
			return bl.getRelative(BlockFace.UP).getRelative(Helper.faceGetOpposite(facing));
		case 5:
			return bl.getRelative(Helper.faceGetOpposite(facing));
		case 6:
			return bl.getRelative(BlockFace.DOWN).getRelative(Helper.faceGetOpposite(facing));
		case 7:
			return bl.getRelative(BlockFace.DOWN);
		}
		return null;
	}

	private void drawBlade(Block bl) {
		Block bc = bl;
		for (int i = 0; i < bladeLength; i++) {
			addToBlock(bladePos(bc, ((bladeIndex + i) % 8)), Material.IRON_ORE);
			addToBlock(bc, Material.FENCE);
			bc = bc.getRelative(Helper.faceGetRight(facing));
		}
	}

	private void addToBlock(Block bl, Material ml) {
		game.logic.addBlockForReprint(new GameItem(bl.getX(), bl.getY(), bl.getZ(), bl.getTypeId(), bl.getData(), "dozer" + dozerID,true));
		bl.setType(ml);
	}

}
