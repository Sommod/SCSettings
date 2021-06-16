package com.soulcraft.Event.InventoryHandlers;

import org.bukkit.event.inventory.InventoryClickEvent;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.AbstractHandler;
import com.soulcraft.GUI.AbstractMenu.Button;
import com.soulcraft.GUI.MainMenu;

/**
 * This handles the action that is performed within the main
 * menu.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class MainMenuHandler extends AbstractHandler<MainMenu> {

	public MainMenuHandler(InventoryClickEvent event, SCSettingsManager manager) {
		super(event, manager, manager.getGuiManager().getMainMenu());
	}
	
	@Override
	public void execute() {
		getEvent().setCancelled(true); // Main Menu will always stop from grabbing the item.

		if(isTopInventory() && getMenu().isButton(getEvent().getSlot())) {
			Button pressed = getMenu().getButton(getEvent().getSlot());
			
			switch (pressed) {
			case MAIN_MENU:
				return;
				
			case ADD_MENU:
				getManager().getGuiManager().getAddFriend().open(getPlayer());
				return;
				
			case FRIEND_REQUEST_MENU:
				getManager().getGuiManager().getFriendRequestMenu().open(getPlayer());
				return;
				
			case FRIENDS_MENU:
				getManager().getGuiManager().getFriendsMenu().open(getPlayer());
				return;

			case SETTINGS_MENU:
				getManager().getGuiManager().getSettingsMenu().open(getPlayer());
				return;
				
			case ERRROR:
				getPlayer().sendMessage("§cError, please information the Administration about the error code.\n§cError Code: AM-45");
				return;
				
			case ACCEPT_ALL:
			case DECLINE_ALL:
			case NEXT_PAGE:
			case PREVIOUS_PAGE:
			case CONFIRM:
				return;
								
			default:
				getPlayer().sendMessage("§cUnknown error occurred. Please give the error code the administration.\n§c Error Code: MMH-29.");
				break;
			}
		}
	}

}
