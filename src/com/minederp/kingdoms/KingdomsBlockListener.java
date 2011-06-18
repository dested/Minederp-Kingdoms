package com.minederp.kingdoms;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockCanBuildEvent;

import sun.net.www.content.text.plain;

public class KingdomsBlockListener extends BlockListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsBlockListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	public void onBlockDamage(BlockDamageEvent event) {


	}

	public void onBlockBreak(BlockBreakEvent event) {
	}
}
