package com.minederp.kingdoms.listeners;

import org.bukkit.event.map.MapIndexCreatedEvent;
import org.bukkit.event.map.MapInitializeEvent;
import org.bukkit.event.map.MapListener;

import com.minederp.kingdoms.KingdomsPlugin;
import com.minederp.kingdoms.maps.ChestWatcherBase;


public class KingdomsMapListener extends MapListener {

    private final KingdomsPlugin kingdomsPlugin;

	public KingdomsMapListener(KingdomsPlugin kingdomsPlugin) {
		this.kingdomsPlugin = kingdomsPlugin;
		// TODO Auto-generated constructor stub
	}

	public void onMapInitialize(MapInitializeEvent event) {
    	if (event.getMapView().getId() == 7) {
			event.getMapView().registerVirtualBase(
					new ChestWatcherBase(kingdomsPlugin), kingdomsPlugin);
			return;
		}
    	
    	
    }
	public void onMapIndexCreated(MapIndexCreatedEvent event) {
    	if (event.getIndex()== 7) {
    		event.shouldSkip(true);
		} 	
    	
    }

}
