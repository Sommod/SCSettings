package com.soulcraft.Event.InventoryHandlers;

import org.bukkit.event.inventory.InventoryClickEvent;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.AbstractHandler;
import com.soulcraft.GUI.GiftMenu;

/**
 * Handles the action done within the Gifting Menu.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class GiftMenuHandler extends AbstractHandler<GiftMenu> {

	public GiftMenuHandler(InventoryClickEvent event, SCSettingsManager manager, GiftMenu menu) {
		super(event, manager, menu);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute() {
		if(isTopInventory()) {
			if(getMenu().isButton(getEvent().getSlot())) {
				getEvent().setCancelled(true);
				
				if(openMenu()) {
					getManager().getGuiManager().removeRecevier(getPlayer());
					return;
				}
				
				switch (getMenu().getButton(getEvent().getSlot())) {
				case CONFIRM:
					if(getEvent().getInventory().getItem(23) == null) {
						getPlayer().sendMessage("§cNo item to send to friend.");
					}
					
					getManager().getItemManager().addItem(getPlayer(), getManager().getGuiManager().getReceiver(getPlayer()), getEvent().getInventory().getItem(23));
					getEvent().getInventory().setItem(23, null);
					getPlayer().sendMessage("§aItem has been set as a gift.");
					break;

				case ERROR:
					getPlayer().sendMessage("§Error, unknown action occurred, please information SoulCraft Administration. Error Code: GMH-34.");
					break;
					
				default:
					break;
				}
				
			} else {
				if(getEvent().getSlot() != 23)
					getEvent().setCancelled(true);
			}
		}
	}

}
