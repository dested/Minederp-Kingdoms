package com.minederp.kingdoms.listeners;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.minederp.kingdoms.KingdomsPlugin;

public class KingdomsBlockListener extends BlockListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsBlockListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	public void onBlockDamage(BlockDamageEvent event) {

	}

	public void onBlockPlace(BlockPlaceEvent event) {
	}

	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(this.kingdomsPlugin.gameLogic.blockDestroyed(event.getBlock(), event.getPlayer()));
	}

	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		if (event.getNewCurrent() > 0) {
			if (event.getBlock().getType() == Material.PUMPKIN) {
				event.getBlock().setType(Material.JACK_O_LANTERN);
			} else if (event.getBlock().getType() == Material.NETHERRACK) {
				event.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
			} else if (event.getBlock().getType() == Material.LAPIS_BLOCK) {
				event.getBlock().getRelative(BlockFace.UP).setType(Material.GLOWSTONE);
			}
		} else {
			if (event.getBlock().getType() == Material.JACK_O_LANTERN) {
				event.getBlock().setType(Material.PUMPKIN);
			} else if (event.getBlock().getType() == Material.NETHERRACK) {
				event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
			} else if (event.getBlock().getType() == Material.GLOWSTONE) {
				event.getBlock().getRelative(BlockFace.UP).setType(Material.LAPIS_BLOCK);
			}
		}
	}
}
