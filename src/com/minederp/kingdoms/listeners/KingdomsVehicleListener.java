package com.minederp.kingdoms.listeners;

import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import com.minederp.kingdoms.KingdomsPlugin;

public class KingdomsVehicleListener extends VehicleListener {

	private final KingdomsPlugin kingdomsPlugin;

	public KingdomsVehicleListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;
		System.out.print("exits");
		System.out.print("exits");
		System.out.print("exits");
		System.out.print("exits");
		System.out.print("exits");
		System.out.print("exits");
		System.out.print("exits");
		System.out.print("exits");

	}

	public void onVehicleEnter(VehicleEnterEvent event) {
		System.out.print("enter");
		System.out.print("enter");
		System.out.print("enter");
		System.out.print("enter");
	}

	public void onVehicleExit(VehicleExitEvent event) {
		System.out.print("exit");
		System.out.print("exit");
		System.out.print("exit");
	}

	public void onVehicleMove(VehicleMoveEvent event) {
		System.out.print("move");
		System.out.print("move");
		System.out.print("move");
	}

}
