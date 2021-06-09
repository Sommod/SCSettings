package com.soulcraft.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.soulcraft.Items.ItemData;

/**
 * Handles the items that appear in the blacklist. If the blacklist
 * is disabled, then all methods that check for item blacklist will
 * return corresponding to what the check is for.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class Blacklist {

	private SCSettingsManager manager;
	private List<Material> blacklist;
	
	public Blacklist(SCSettingsManager manager) {
		this.manager = manager;
		init();
	}
	
	private void init() {
		blacklist = new ArrayList<Material>();
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(manager.getPlugin().getDataFolder(), "config.yml"));
		
		if(!config.isList("friends.blacklisted items"))
			return;
		
		config.getStringList("friends.blacklisted items").forEach(name -> { blacklist.add(Material.getMaterial(name)); } );
	}
	
	/**
	 * Checks if the item type is allowed to be sent as a gift. This checks not the
	 * item amount, the name, lore or anything else; but checks the type of item that
	 * it is (Bedrock, Sponge, Paper... etc).
	 * @param item - Item to check
	 * @return True - if item is noted in the config
	 */
	public boolean isBlacklisted(ItemStack item) { return blacklist.isEmpty() ? false : blacklist.contains(item.getType()); }
	
	/**
	 * Similar to {@link #isBlacklisted(ItemStack)}, this checks if the item stored
	 * within the ItemData is blacklisted. This is usually used after the item has
	 * already been sent as a gift once and when a change in the blacklist was changed.
	 * @param itemData - ItemData to check
	 * @return True - if the Item in the ItemData is blacklisted
	 */
	public boolean isBlacklisted(ItemData itemData) { return isBlacklisted(itemData.getItem()); }
	
	public void reload(SCSettingsManager manager) {
		this.manager = manager;
		init();
	}

}
