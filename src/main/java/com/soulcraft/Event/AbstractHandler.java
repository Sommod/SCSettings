package com.soulcraft.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Super class used for all EventHandler classes.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public abstract class AbstractHandler {

	private InventoryClickEvent event;
	private SCSettingsManager manager;
	
	public AbstractHandler(InventoryClickEvent event, SCSettingsManager manager) {
		this.manager = manager;
		this.event = event;
	}
	
	protected SCSettingsManager getManager() { return manager; }
	
	public InventoryClickEvent getEvent() { return event; }
	
	public Player getPlayer() { return (Player) event.getWhoClicked(); }
	
	public Inventory getTopInventory() { return event.getInventory(); }
	
	public boolean isTopInventory() { return event.getClickedInventory() == null ? false : event.getClickedInventory().equals(getTopInventory()); }
	
	public boolean clickedOutside() { return event.getClickedInventory() == null; }
}
