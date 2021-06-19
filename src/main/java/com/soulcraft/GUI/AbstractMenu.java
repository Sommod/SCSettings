package com.soulcraft.GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
	private ItemStack fillItem;
	private boolean border;
	private String title;
	
	/**
	 * Initializes a new Inventory as well
	 * as runs through the config to determine
	 * what type of setup to have for the menu.
	 * If the option is set to have a border,
	 * then the this will auto-fill in the border
	 */
	public AbstractMenu(SCSettingsManager manager, File configFile) {
		this.manager = manager;
		buttons = new HashMap<Integer, Button>();
		YamlConfiguration fileConfig = YamlConfiguration.loadConfiguration(configFile);
		title = fileConfig.getString("name").replace('&', '§');
		inv = manager.getPlugin().getServer().createInventory(null, fileConfig.isInt("size") ? fileConfig.getInt("size") : 54, title);
		
		// Adds each item found in the slots of the config
		for(String slotNum : fileConfig.getConfigurationSection("slots").getKeys(false)) {
			ItemStack toSet = new ItemStack(Material.getMaterial(fileConfig.getString("slots." + slotNum + ".item")), fileConfig.getInt("slots." + slotNum + ".amount"));
			ItemMeta meta = toSet.getItemMeta();
			List<String> lore = new ArrayList<String>();
			
			meta.setDisplayName(fileConfig.getString("slots." + slotNum + ".name").replace('&', '§'));
			fileConfig.getStringList("slots." + slotNum + ".lore").forEach(line -> { lore.add(line.replace('&', '§')); });
			meta.setLore(lore);
			
			if(fileConfig.getBoolean("slots." + slotNum + ".enchanted")) {
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			if(fileConfig.isString("slots." + slotNum + ".button"))
				buttons.put(Integer.parseInt(slotNum) - 1, Button.getButton(fileConfig.getString("slots." + slotNum + ".button")));
			
			toSet.setItemMeta(meta);
			inv.setItem(Integer.parseInt(slotNum) - 1, toSet);
		}
		
		// Gets the fill type and store the item to set.
		if(fileConfig.getBoolean("fill.use")) {
			border = fileConfig.getString("fill.fill type").equalsIgnoreCase("border");
			ItemStack item = new ItemStack(Material.getMaterial(fileConfig.getString("fill.item")), fileConfig.getInt("fill.amount"));
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			
			meta.setDisplayName(fileConfig.getString("fill.name").replace('&', '§'));
			fileConfig.getStringList("fill.lore").forEach(line -> { lore.add(line.replace('&', '§')); });
			meta.setLore(lore);
			
			if(fileConfig.getBoolean("fill.enchanted")) {
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			item.setItemMeta(meta);
			fillItem = item;
			
			// If the option is border, then the border
			// is created and stored now.
			if(border) {
				// Top
				for(int i = 0; i < 9; i++) {
					if(inv.getItem(i) == null)
						inv.setItem(i, fillItem);
				}
				
				// Sides
				for(int i = 17; i < inv.getSize(); i += 9) {
					if(inv.getItem(i) == null)
						inv.setItem(i, fillItem);
					if(inv.getItem(i - 8) == null)
						inv.setItem(i - 8, fillItem);
				}
				
				// Bottom
				for(int i = inv.getSize() - 9; i < inv.getSize(); i++) {
					if(inv.getItem(i) == null)
						inv.setItem(i, fillItem);
				}
				
			}
		} else
			fillItem = null;
	}
	
	/**
	 * Gets the stored Inventory object This gets
	 * the none formatted inventory.
	 * @return Inventory of Menu
	 */
	protected Inventory getBaseInventory() {
		Inventory clone = manager.getPlugin().getServer().createInventory(null, inv.getSize(), title);
		clone.setContents(inv.getContents());
		return clone;
	}
	
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
	 * Returns the given inventory with the fill item placed into all empty slots.
	 * @param changedInv - Inventory with placed items
	 * @return changedInv
	 */
	protected Inventory getFilledInventory(Inventory changedInv) {
		
		for(int i = 0; i < changedInv.getSize(); i++) {
			if(changedInv.getItem(i) == null)
				changedInv.setItem(i, fillItem);
		}
		
		return changedInv;
	}
	
	/**
	 * Checks if the fill type is either a border or fill all
	 * empty slots. If this is using a border type, then this
	 * will return true.
	 * @return True - if Border and not Fill
	 */
	public boolean isBorder() { return border; }
	
	/**
	 * Checks if the menu is using any filling option. If the
	 * menu does not contain an options, then this will return
	 * false.
	 * @return True - if using fill option
	 */
	public boolean isUsingFillOption() { return fillItem != null; }
	
	/**
	 * Checks if the given slot number contains a button.
	 * @param slot - slot to check
	 * @return True - if button
	 */
	public boolean isButton(int slot) { return buttons.containsKey(slot); }
	
	/**
	 * Checks if the given string value is the same as the title
	 * of this menu. If it is, then this will return True.
	 * @param check - Inventory name
	 * @return True - if name matches
	 */
	public boolean isTitle(String check) {
		if(!check.contains("§"))
			return check.equals(title.substring(4));
		else
			return check.equals(title);
	}
	
	/**
	 * Gets the button of the given slots number. If the slot given is
	 * not a number, then this will return the Button ERROR.
	 * @param slot - slot to get button
	 * @return Button
	 */
	public Button getButton(int slot) { return buttons.get(slot); }
	
	/**
	 * Gets the item that is to fill an inventory.
	 * @return ItemStack
	 */
	protected ItemStack getFillItem() { return fillItem; }
	
	/**
	 * Gets the title of the inventory. This returns
	 * the formatted for Minecraft, ie '§' is the
	 * symbol present, not '&'.
	 * @return Title
	 */
	protected String getTitle() { return title; }
	
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
		MAIN_MENU, FRIENDS_MENU, ADD_MENU, SETTINGS_MENU, 
		NEXT_PAGE, PREVIOUS_PAGE, FRIEND_REQUEST_MENU,
		DECLINE_ALL, ACCEPT_ALL, CONFIRM, ERROR;
		
		/**
		 * Gets the Button Enumeruation based on the String value
		 * given. If the given string value is not found, then
		 * this will return the ERROR button
		 * @param value - String name of button
		 * @return Button
		 */
		public static Button getButton(String value) {
			for(Button button : Button.values()) {
				if(button.toString().equals(value))
					return button;
			}
			
			return ERROR;
		}
	}
	
}
