package com.soulcraft.Event.InventoryHandlers;

import java.util.List;
import java.util.UUID;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.AbstractHandler;
import com.soulcraft.GUI.AdminStorage;
import com.soulcraft.Items.ItemData;

/**
 * Handles the actions done within the Admin Storage.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class AdminStorageHandler extends AbstractHandler<AdminStorage> {

	public AdminStorageHandler(InventoryClickEvent event, SCSettingsManager manager, AdminStorage menu) {
		super(event, manager, menu);
	}
	
	@Override
	public void execute() {
		getEvent().setCancelled(true); // Defaulted to cancel
		
		if(isTopInventory()) {
			if(getMenu().isButton(getEvent().getSlot())) {
				if(openMenu())
					return;
			
			} else {
				if(getEvent().getSlot() % 9 != 0 && (getEvent().getSlot() + 1) % 9 != 0 && getEvent().getSlot() > 9 && getEvent().getSlot() < 45) {
					ItemStack displayItem = getEvent().getInventory().getItem(getEvent().getSlot());
					
					if(displayItem == null)
						return;
					
					List<String> lore = displayItem.getItemMeta().getLore();
					UUID id = UUID.fromString(lore.get(lore.size() - 1).substring(10));
					ItemData itemData = getManager().getItemManager().getItemData(id);
					
					if(itemData == null) {
						getPlayer().sendMessage("§dSorry, item no longer exists... Refresh page to get updated view.");
						return;
					}
					
					if(getEvent().getClick() == ClickType.LEFT) {
						if(!itemData.getGifter().isOnline()) {
							getPlayer().sendMessage("§cCannot return item, player is not online...");
							return;
						} else if(itemData.getGifter().getPlayer().getInventory().firstEmpty() == -1) {
							getPlayer().sendMessage("§cCannot return item, no space in the players inventory.");
							return;
						}
						
						itemData.getGifter().getPlayer().getInventory().addItem(itemData.getItem());
						getEvent().getInventory().setItem(getEvent().getSlot(), null);
						getPlayer().sendMessage("§aItem has been returned to player.");
						
					} else if(getEvent().getClick() == ClickType.RIGHT) {
						getEvent().getInventory().setItem(getEvent().getSlot(), null);
						getPlayer().sendMessage("§bItem has been Deleted...");
						
					} else if(getEvent().getClick() == ClickType.MIDDLE) {
						if(getPlayer().getInventory().firstEmpty() == -1) {
							getPlayer().sendMessage("§cYou do not have any empty slots to grab item.");
							return;
						}
						
						getPlayer().getInventory().addItem(itemData.getItem());
						getPlayer().sendMessage("§bSuccessfully obtained item.");
					}
					
					getManager().getItemManager().removeItemData(id);
				}
			}
		}
	}

}
