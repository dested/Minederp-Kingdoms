package com.minederp.kingdoms.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockCanBuildEvent;

import com.minederp.kingdoms.KingdomsPlugin;

import sun.net.www.content.text.plain;

public class KingdomsBlockListener extends BlockListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsBlockListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	public void onBlockDamage(BlockDamageEvent event) {


	}

	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(!this.kingdomsPlugin.ctfGame.blockDestroyed(event.getBlock(), event.getPlayer()));
	}
}
