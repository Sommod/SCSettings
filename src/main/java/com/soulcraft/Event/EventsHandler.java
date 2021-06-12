package com.soulcraft.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Registers all Events that this plugin uses as
 * well as used to unregister all events when
 * this plugin is disabled.
 *
 * @author Armeriness
 * @author Sommod
 * @version 1.0
 *
 */
public class EventsHandler implements Listener {
	
	private SCSettingsManager manager;
	
	/**
	 * Registers the events that are handled within this
	 * plugin.
	 * @param manager
	 */
	public EventsHandler(SCSettingsManager manager) {
		manager.getPlugin().getServer().getPluginManager().registerEvents(this, manager.getPlugin());
		this.manager = manager;
	}
	
	/**
	 * Unregisters all events that are within this class.
	 * Requires the Manager class of the plugin to disable.
	 * @param manager - Manager of plugin
	 */
	public void unRegisterEvents(SCSettingsManager manager) { HandlerList.unregisterAll(manager.getPlugin()); }
	
	@EventHandler
	public void onClickEvent(InventoryClickEvent event) {
		
	}

}
