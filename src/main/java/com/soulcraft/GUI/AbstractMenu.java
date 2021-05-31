package com.soulcraft.GUI;

import java.io.File;
import java.util.Map;

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
	private Map<Integer, Button> buttons;
	
	/**
	 * Initializes a new Inventory as well
	 * as runs through the config to determine
	 * what type of setup to have for the menu.
	 * If the option is set to have a border,
	 * then the this will auto-fill in the border
	 * @param name - Name of Inventory
	 */
	public AbstractMenu(SCSettingsManager manager, String name, File configFile) {
		this.manager = manager;
		//TODO: Create Inventory and load buttons into map
	}
	
	/**
	 * Gets the stored Inventory object This gets
	 * the none formatted inventory.
	 * @return Inventory of Menu
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
	 * Checks if the given slot number contains a button.
	 * @param slot - slot to check
	 * @return True - if button
	 */
	public boolean isButton(int slot) { return buttons.containsKey(slot); }
	
	/**
	 * Gets the button of the given slots number. If the slot given is
	 * not a number, then this will return the Button ERROR.
	 * @param slot - slot to get button
	 * @return Button
	 */
	public Button getButton(int slot) { return buttons.get(slot); }
	
	/**
	 * Opens the Inventory for the given player.
	 * @param player
	 */
	public abstract void open(Player player);
	
	/**
	 * Contains all the information for buttons that are applied
	 * to items within the Inventory.
	 *
	 * @author Sommod
	 * @version 1.0
	 *
	 */
	public enum Button {
		
		MAIN_MENU, FRIENDS_MENU, ADD_MENU, SETTINGS_MENU, ERRROR;

	}
	
}
