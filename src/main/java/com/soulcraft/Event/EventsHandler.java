package com.soulcraft.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.soulcraft.Data.SCSettingsManager;

public class EventsHandler implements Listener {
	
	private SCSettingsManager manager;

	public EventsHandler(SCSettingsManager manager) {
		manager.getPlugin().getServer().getPluginManager().registerEvents(this, manager.getPlugin());
		this.manager = manager;
	}
	
	@EventHandler
	public void onClickEvent(InventoryClickEvent event) {
		
	}

}
