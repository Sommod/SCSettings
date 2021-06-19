package com.soulcraft.GUI;

import java.io.File;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Items.ItemData;

/**
 * This contains the data and information
 * for creating and handling the menu that
 * is used for collecting both items that
 * were sent as items and items returned.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class ItemCollectionMenu extends AbstractMenu {

	public ItemCollectionMenu(SCSettingsManager manager, File configFile) {
		super(manager, configFile);
	}

	@Override
	public void open(Player player) {
		Inventory clone = getBaseInventory();
		List<ItemData> items = getManager().getItemManager().getStorage(player);
		
		for(int i = 10, k = 0; i < clone.getSize() - 9 && k < items.size(); i++, k++) {
			if((i + 1) % 9 == 0) {
				i++;
				continue;
			}
			
			clone.setItem(i, items.get(k).getDisplayItem());
		}
		
		if(isUsingFillOption() && !isBorder())
			clone = getFilledInventory(clone);
		
		player.openInventory(clone);
	}

}
