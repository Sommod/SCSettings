package com.soulcraft.Event.InventoryHandlers;

import org.bukkit.event.inventory.InventoryClickEvent;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.AbstractHandler;
import com.soulcraft.GUI.FriendRequestMenu;

public class FriendRequestHandler extends AbstractHandler<FriendRequestMenu> {

	public FriendRequestHandler(InventoryClickEvent event, SCSettingsManager manager, FriendRequestMenu menu) {
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
				
			}
		}
	}

}
