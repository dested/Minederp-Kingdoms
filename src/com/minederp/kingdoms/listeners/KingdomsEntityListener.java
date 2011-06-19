package com.minederp.kingdoms.listeners;

import net.minecraft.server.EntityPlayer;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import com.minederp.kingdoms.KingdomsPlugin;

public class KingdomsEntityListener extends EntityListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsEntityListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;

	}

	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof CraftPlayer) {

			kingdomsPlugin.gameLogic.playerDied((Player) event.getEntity());
			return;
		}
		if (event.getEntity() instanceof CraftEntity) {

			kingdomsPlugin.gameLogic.entityDied((Entity) event.getEntity(), event);

		}
	}

	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof CraftEntity) {
			kingdomsPlugin.gameLogic.entityHurt((Entity) event.getEntity(), event);
		}
		if (event.getEntity() instanceof CraftPlayer) {

			if (((Player) event.getEntity()).getHealth() == 0) {
				kingdomsPlugin.gameLogic.playerDying((Player) event.getEntity());
			}

			if (event instanceof EntityDamageByEntityEvent && event.getEntity() instanceof Player
					&& ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
				event.setCancelled(!kingdomsPlugin.gameLogic.playerFight((Player) event.getEntity(),
						(Player) ((EntityDamageByEntityEvent) event).getDamager()));
			}
		}
	}
}