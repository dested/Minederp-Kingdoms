package com.minederp.kingdoms.util;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

public class InventoryStash {
	private ItemStack[] contents;
	private ItemStack helmet;
	private ItemStack chest;
	private ItemStack legs;
	private ItemStack feet;

	public InventoryStash(ItemStack[] contents) {
		this.setContents(contents);

	}

	public InventoryStash(ItemStack[] contents, ItemStack helmet, ItemStack chest, ItemStack legs, ItemStack feet) {
		this.setContents(contents);
		this.setHelmet(helmet.getTypeId() == 0 ? null : helmet);
		this.setChest(chest.getTypeId() == 0 ? null : chest);
		this.setLegs(legs.getTypeId() == 0 ? null : legs);
		this.setFeet(feet.getTypeId() == 0 ? null : feet);

	}

	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}

	public ItemStack[] getContents() {
		return contents;
	}

	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	public ItemStack getHelmet() {
		return helmet;
	}

	public void setChest(ItemStack chest) {
		this.chest = chest;
	}

	public ItemStack getChest() {
		return chest;
	}

	public void setLegs(ItemStack legs) {
		this.legs = legs;
	}

	public ItemStack getLegs() {
		return legs;
	}

	public void setFeet(ItemStack feet) {
		this.feet = feet;
	}

	public ItemStack getFeet() {
		return feet;
	}

	@Override
	public String toString() {
		HashMap<String, Integer> items = new HashMap<String, Integer>();
		if (helmet != null) {
			if (items.containsKey(helmet.getType().name()))
				items.put(helmet.getType().name(), items.get(helmet.getType().name()) + helmet.getAmount());
			else
				items.put(helmet.getType().name(), helmet.getAmount());

		}

		if (chest != null) {
			if (items.containsKey(chest.getType().name()))
				items.put(chest.getType().name(), items.get(chest.getType().name()) + chest.getAmount());
			else
				items.put(chest.getType().name(), chest.getAmount());

		}

		if (legs != null) {
			if (items.containsKey(legs.getType().name()))
				items.put(legs.getType().name(), items.get(legs.getType().name()) + legs.getAmount());
			else
				items.put(legs.getType().name(), legs.getAmount());

		}

		if (feet != null) {
			if (items.containsKey(feet.getType().name()))
				items.put(feet.getType().name(), items.get(feet.getType().name()) + feet.getAmount());
			else
				items.put(feet.getType().name(), feet.getAmount());

		}

		for (ItemStack itm : contents) {
			if (items.containsKey(itm.getType().name()))
				items.put(itm.getType().name(), items.get(itm.getType().name()) + itm.getAmount());
			else
				items.put(itm.getType().name(), itm.getAmount());
		}

		if (items.size() == 0)
			return "";

		StringBuilder sb = new StringBuilder();
		for (Entry<String, Integer> it : items.entrySet()) {
			sb.append(it.getValue() + " " + it.getKey() + ", ");
		}
		return sb.toString().substring(0, sb.length() - 2);
	}
}
