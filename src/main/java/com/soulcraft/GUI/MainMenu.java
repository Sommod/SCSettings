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

	public MainMenu(SCSettingsManager manager, String name) {
		super(manager, name, new File(manager.getPlugin().getDataFolder(), "Data/Gui Data/Main Menu.yml"));
	}

	@Override
	public void open(Player player) {
		if(isUsingFillOption() && !isBorder()) {
			Inventory toOpen = getBaseInventory();
			toOpen = getFilledInventory(toOpen);
			
			player.closeInventory();
			player.openInventory(toOpen);
		}
	}

}
