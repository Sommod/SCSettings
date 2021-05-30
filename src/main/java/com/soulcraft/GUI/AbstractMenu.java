package com.soulcraft.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Parent class to all the GUI classes within this
 * package. Contains the methods and objects that
 * are stored from all the similar GUI's.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public abstract class AbstractMenu {

	private Inventory inv;
	private SCSettingsManager manager;
	
	/**
	 * Initializes a new Inventory as well
	 * as runs through the config to determine
	 * what type of setup to have for the menu.
	 * If the option is set to have a border,
	 * then the this will auto-fill in the border
	 * @param name - Name of Inventory
	 */
	public AbstractMenu(SCSettingsManager manager, String name) {
		this.manager = manager;
		inv = Bukkit.createInventory(null, 54, name.replace('&', '§'));
	}
	
	/**
	 * Gets the stored Inventory objet\ct
	 * @return Inventory
	 */
	protected Inventory getBaseInventory() { return inv; }
	
	/**
	 * Sets the stored Inventory object to the given parameter
	 * Inventory Object
	 * @param inventory - Inventory to set
	 */
	protected void setInventory(Inventory inventory) { inv = inventory; }
	
	/**
	 * Sets an item within the inventory
	 * @param item - Item to set (Can be null to remove item)
	 * @param slot - slot of inventory
	 */
	protected void setItem(ItemStack item, int slot) { inv.setItem(slot, item); }
	
	/**
	 * Obtains the manager object of the plugin.
	 * @return SCSettingsManager
	 */
	protected SCSettingsManager getManager() { return manager; }
	
	/**
	 * Opens the Inventory for the given player.
	 * @param player
	 */
	public abstract void open(Player player);
	
}
