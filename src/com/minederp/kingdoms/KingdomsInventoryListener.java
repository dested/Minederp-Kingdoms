package com.minederp.kingdoms;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryListener;
import org.bukkit.event.inventory.InventoryOpenedClosedEvent;
import org.bukkit.event.inventory.InventoryTransactionEvent;

public class KingdomsInventoryListener extends InventoryListener {
	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsInventoryListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;
		// TODO Auto-generated constructor stub
	}

	public void onInventoryTransaction(InventoryTransactionEvent event) {
		event.setCancled(true);

	}

	public void onInventoryOpened(InventoryOpenedClosedEvent event) {
		event.setCancled(false);

	}

}
