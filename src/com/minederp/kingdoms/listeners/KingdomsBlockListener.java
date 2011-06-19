package com.minederp.kingdoms.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener; 

import com.minederp.kingdoms.KingdomsPlugin;
 

public class KingdomsBlockListener extends BlockListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsBlockListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	public void onBlockDamage(BlockDamageEvent event) {


	}

	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(!this.kingdomsPlugin.gameLogic.blockDestroyed(event.getBlock(), event.getPlayer()));
	}
}
