package com.soulcraft.GUI;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Handles the main menu for the plugin.
 *
 * @author Armeriness
 * @author Sommod
 * @version 1.0
 *
 */
public class MainMenu extends AbstractMenu {

	public MainMenu(SCSettingsManager manager) {
		super(manager, new File(manager.getPlugin().getDataFolder(), "Data/Gui Data/Main Menu.yml"));
	}

	@Override
	public void open(Player player) {
		Inventory toOpen = getBaseInventory();
		toOpen = getFilledInventory(toOpen);
		
		if(isUsingFillOption() && !isBorder())
			toOpen = getFilledInventory(toOpen);
		
		player.closeInventory();
		player.openInventory(toOpen);
	}

}
