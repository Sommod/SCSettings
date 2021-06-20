package com.soulcraft.Event.InventoryHandlers;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.AbstractHandler;
import com.soulcraft.GUI.AddFriend;
import com.soulcraft.Player.PlayerData;

/**
 * Handles the actions for the Add Friend menu.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class AddFriendHandler extends AbstractHandler<AddFriend> {

	public AddFriendHandler(InventoryClickEvent event, SCSettingsManager manager) {
		super(event, manager, manager.getGuiManager().getAddFriend());
	}
	
	@Override
	public void execute() {
		getEvent().setCancelled(true); // Default cancel all action
		
		if(isTopInventory()) {
			if(getMenu().isButton(getEvent().getSlot())) {
				if(openMenu())
					return;
				
				switch (getMenu().getButton(getEvent().getSlot())) {
				
				case ERROR:
					getPlayer().sendMessage("§cError, please information the Administration about the error code.\n§cError Code: AM-45");
					return;
					
				case NEXT_PAGE:
					getManager().getGuiManager().getAddFriend().openNextPage(getPlayer(), getManager().getGuiManager().getAddFriend().getPlayer(getPlayer()) + 1);
					return;
					
				case PREVIOUS_PAGE:
					getManager().getGuiManager().getAddFriend().openNextPage(getPlayer(), getManager().getGuiManager().getAddFriend().getPlayer(getPlayer()) - 1);
					return;
					
				case CONFIRM:
				case ADD_MENU:
				case ACCEPT_ALL:
				case DECLINE_ALL:
					return;
									
				default:
					getPlayer().sendMessage("§cUnknown error occurred. Please give the error code the administration.\n§c Error Code: MMH-29.");
					break;
				}
			} else {
				if(getEvent().getInventory().getItem(getEvent().getSlot()) != null) {
					ItemStack item = getEvent().getInventory().getItem(getEvent().getSlot());
					
					if(item.getType() == Material.PLAYER_HEAD) {
						String name = item.getItemMeta().getDisplayName().substring(4);
						PlayerData data = getManager().getPlayerManager().getPlayerData(getPlayer());
						
						if(data.isFriend(name))
							return;
						else if(data.isFriendRequest(asPlayer(name))) {
							
						} else {
							if(getManager().getPlayerManager().getPlayerData(asPlayer(name)).getChatSettings().isAllowingFriendRequest()) {
								if(getEvent().getClick() == ClickType.LEFT) {
									data.addFriend(asPlayer(name));
									data.removeFriendRequest(asPlayer(name));
									getManager().getPlayerManager().getPlayerData(asPlayer(name)).addFriend(getPlayer());
									getPlayer().sendMessage("§b" + name + "§a has been added as a friend!");
								} else {
									data.removeFriendRequest(asPlayer(name));
									getPlayer().sendMessage("§cYou have declined the friend requests...");
								}
							} else
								getPlayer().sendMessage("§b" + name + "§c is not accepting friend requests right now.");
						}
					}
				}
			}
		}
	}
	
	private OfflinePlayer asPlayer(String name) {
		for(OfflinePlayer off : getManager().getPlugin().getServer().getOfflinePlayers()) {
			if(off.getName().equalsIgnoreCase(name))
				return off;
		}
		
		return null;
	}

}
