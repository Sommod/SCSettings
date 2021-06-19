package com.soulcraft.Event.InventoryHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.AbstractHandler;
import com.soulcraft.GUI.ItemCollectionMenu;
import com.soulcraft.Items.ItemData;

/**
 * Handles the actions that are performed within the
 * ItemCollectionMenu.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class ItemCollectHandler extends AbstractHandler<ItemCollectionMenu> {

	public ItemCollectHandler(InventoryClickEvent event, SCSettingsManager manager, ItemCollectionMenu menu) {
		super(event, manager, menu);
	}
	
	@Override
	public void execute() {
		getEvent().setCancelled(true); // Defaulted To Cancel
		
		if(isTopInventory()) {
			if(getMenu().isButton(getEvent().getSlot())) {
				if(openMenu())
					return;
			
				switch (getMenu().getButton(getEvent().getSlot())) {
				case ACCEPT_ALL:
					List<ItemData> toRemove = new ArrayList<ItemData>();
					
					if(getPlayer().getInventory().firstEmpty() == -1) {
						getPlayer().sendMessage("§cNo space in inventory to collect any items.");
						return;
					}
					
					for(ItemData itemData : getManager().getItemManager().getStorage(getPlayer())) {
						if(getPlayer().getInventory().firstEmpty() == -1)
							break;
						
						getPlayer().getInventory().addItem(itemData.getItem());
						toRemove.add(itemData);
					}
					
					for(ItemData itemData : toRemove)
						getManager().getItemManager().removeItemData(itemData.getID());
					break;
	
				case DECLINE_ALL:
					List<ItemData> items = getManager().getItemManager().getStorage(getPlayer());
					
					for(ItemData itemData : items) {
						if(itemData.isInReceiver())
							getManager().getItemManager().updateItem(itemData);
					}
					
					getPlayer().closeInventory();
					getManager().getGuiManager().getItemCollectionMenu().open(getPlayer());
					break;
					
				case ERROR:
					getPlayer().sendMessage("§cError, could not handle action, please inform SoulCraft Administration. Error Code: ICH-28");
					break;
					
				default:
					break;
				}
				
			} else {
				if(getEvent().getSlot() % 9 != 0 && (getEvent().getSlot() + 1) % 9 != 0 && getEvent().getSlot() > 9 && getEvent().getSlot() < 45) {
					ItemStack displayItem = getEvent().getInventory().getItem(getEvent().getSlot());
					
					if(displayItem == null)
						return;
					else if(!displayItem.getItemMeta().hasLore())
						return;
					
					List<String> lore = displayItem.getItemMeta().getLore();
					UUID id;
					
					try {
						id = UUID.fromString(lore.get(lore.size() - 1).substring(10));
					} catch (IllegalArgumentException e) {
						return;
					}
					
					ItemData itemData = getManager().getItemManager().getItemData(id);
					
					if(!getManager().getItemManager().getStorage(getPlayer()).contains(itemData)) {
						getPlayer().sendMessage("§cError, item is no longer in your possession. Please update view.");
						return;
					}
					
					if(getEvent().getClick() == ClickType.LEFT) {
						if(getPlayer().getInventory().firstEmpty() == -1) {
							getPlayer().sendMessage("§cNo space in inventory to collect item.");
							return;
						}
						
						getPlayer().getInventory().addItem(itemData.getItem());
						getPlayer().sendMessage("§aItem Collected!");
						getManager().getItemManager().removeItemData(id);
						
					} else if(getEvent().getClick() == ClickType.RIGHT) {
						if(itemData.isInGifter()) {
							getPlayer().sendMessage("§bThis is your item, cannot decline.");
							return;
						}
						
						getManager().getItemManager().updateItem(itemData);
						getPlayer().sendMessage("§dItem declined, returning to gifter.");
						
					} else
						return;
					
					getPlayer().getInventory().setItem(getEvent().getSlot(), null);
				}
			}
		}
	}

}
