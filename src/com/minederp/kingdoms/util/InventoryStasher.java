package com.minederp.kingdoms.util;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.minederp.kingdoms.KingdomsPlugin;

public class InventoryStasher {

	HashMap<String, InventoryStash> stashes = new HashMap<String, InventoryStash>();
	private final KingdomsPlugin plugin;

	public InventoryStasher(KingdomsPlugin plugin) {
		this.plugin = plugin;

	}

	public String AddInventory(PlayerInventory inv, boolean clearInventory) {
		String hash = UUID.randomUUID().toString();

		stashes.put(
				hash,
				new InventoryStash(inv.getContents(), inv.getHelmet(), inv
						.getChestplate(), inv.getLeggings(), inv.getBoots()));

		if (clearInventory) {
			inv.setContents(new ItemStack[36]);
			inv.setBoots(null);
			inv.setLeggings(null);
			inv.setChestplate(null);
			inv.setHelmet(null);
		}

		return hash;

	}

	public void RefillInventory(String hash, PlayerInventory inv) {

		InventoryStash f = stashes.get(hash);

		if (f != null) {
			inv.setContents(f.getContents());
			inv.setBoots(f.getFeet());
			inv.setLeggings(f.getLegs());
			inv.setChestplate(f.getChest());
			inv.setHelmet(f.getHelmet());
		}
	}

	public String AddInventory(Inventory inv, boolean clearInventory) {
		String hash = UUID.randomUUID().toString();

		stashes.put(hash, new InventoryStash(inv.getContents()));

		if (clearInventory) {
			inv.setContents(new ItemStack[36]);
		}

		return hash;

	}

	public void RefillInventory(String hash, Inventory inv) {

		InventoryStash f = stashes.get(hash);

		if (f != null) {
			inv.setContents(f.getContents());
		}
	}

}
