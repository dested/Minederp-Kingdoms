package com.minederp.kingdoms.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryListener;
import org.bukkit.event.inventory.InventoryOpenedClosedEvent;
import org.bukkit.event.inventory.InventoryTransactionEvent;
import org.bukkit.event.inventory.TransactionType;
import org.bukkit.inventory.Inventory;

import com.minederp.kingdoms.KingdomsPlugin;

public class KingdomsInventoryListener extends InventoryListener {
	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsInventoryListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;
		// TODO Auto-generated constructor stub
	}

	public void onInventoryTransaction(InventoryTransactionEvent event) {
		if (true)
			return;
		if (one != null && two != null && two.getName().equals(event.getContainer().getName())) {
			if (event.getRightType() == TransactionType.Chest && event.getLeft() != null) {
				one.addItem(event.getLeft());
				event.setLeft(null);
			}
		}
	}

	Inventory one;
	Inventory two;

	public void onInventoryOpened(InventoryOpenedClosedEvent event) {
		if (true)
			return;
		if (one == null)
			one = event.getContainer();
		else
			two = event.getContainer();

		event.setCancled(false);

	}

}
