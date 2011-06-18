package com.minederp.kingdoms.util;

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
		this.setHelmet(helmet.getTypeId()==0?null:helmet);
		this.setChest(chest.getTypeId()==0?null:chest);
		this.setLegs(legs.getTypeId()==0?null:legs);
		this.setFeet(feet.getTypeId()==0?null:feet);

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


}
