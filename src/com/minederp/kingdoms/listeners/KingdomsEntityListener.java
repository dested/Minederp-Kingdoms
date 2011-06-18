package com.minederp.kingdoms.listeners;

import net.minecraft.server.EntityPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import com.minederp.kingdoms.KingdomsPlugin;

public class KingdomsEntityListener extends EntityListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsEntityListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntity() instanceof EntityPlayer){

			kingdomsPlugin.ctfGame.playerDied((Player)event.getEntity());

		}
	}
}