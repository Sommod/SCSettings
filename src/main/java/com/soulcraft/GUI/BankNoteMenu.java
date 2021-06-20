package com.soulcraft.GUI;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Menu that contains and stores the information
 * about the Banknote Menu
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class BankNoteMenu extends AbstractMenu {

	public BankNoteMenu(SCSettingsManager manager) {
		super(manager, new File(manager.getPlugin().getDataFolder(), "Data/Gui Data/BankNote Menu.yml"));
	}

	@Override
	public void open(Player player) {
		Inventory get = getBaseInventory();
		
		if(isUsingFillOption() && !isBorder())
			get = getFilledInventory(get);
		
		player.openInventory(get);
	}

}
