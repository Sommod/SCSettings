package com.soulcraft.GUI;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Handles the menu for the player about either accepting,
 * declining or collecting non-claimed items. All items
 * remain here within this menu until they expire twice,
 * then they are moved to the Admin Storage Menu.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class GiftMenu extends AbstractMenu {
	
	public GiftMenu(SCSettingsManager manager, String name, File configFile) {
		super(manager, name, configFile);
	}

	@Override
	public void open(Player player) {
		Inventory toOpen = getBaseInventory();
		
		if(isUsingFillOption() && !isBorder())
			toOpen = getFilledInventory(toOpen);
		
		toOpen.setItem(23, null);
		player.closeInventory();
		player.openInventory(toOpen);
	}

}
