package com.soulcraft.GUI;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Contains the GUI information and items that were un-claimed by
 * both the gifter and gifted after a period of time. Only people with
 * the given permission(s) may access this storage to either take, give or
 * delete items from this storage.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class AdminStorage extends AbstractMenu {

	
	//TODO: Create info for constructor
	//TODO: Create a new file pointer to file location
	public AdminStorage(SCSettingsManager manager, String name, File configFile) {
		super(manager, name, configFile);
	}

	@Override
	public void open(Player player) {
		Inventory inv = getBaseInventory();
		
		for(int i = 10, k = 0; i < inv.getSize() && k < getManager().getItemManager().getAdminStorage().size(); i++) {
			if((i + 1) % 9 == 0) {
				i++;
				continue;
			}
			
			inv.setItem(i, getManager().getItemManager().getAdminStorage().get(k).getDisplayItem());
		}
		
		if(isUsingFillOption() && !isBorder())
			inv = getFilledInventory(inv);
		
		player.openInventory(inv);
	}

}
