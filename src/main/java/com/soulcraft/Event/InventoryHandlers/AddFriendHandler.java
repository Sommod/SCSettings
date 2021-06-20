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
							if(getEvent().getClick() == ClickType.LEFT) {
								data.removeFriendRequest(asPlayer(name));
								data.addFriend(asPlayer(name));
								getManager().getPlayerManager().getPlayerData(asPlayer(name)).addFriend(getPlayer());
								getPlayer().sendMessage("§aYou have accepted the friend request. You are now friends with §b" + name + "§a.");
								
								if(asPlayer(name).isOnline())
									asPlayer(name).getPlayer().sendMessage("§6§lNotice: §b" + getPlayer().getName() + "§f has accepted you as a friend!");
								
							} else if(getEvent().getClick() == ClickType.RIGHT) {
								data.removeFriendRequest(asPlayer(name));
								getPlayer().sendMessage("§cYou have declined the friend request.");
								
								if(asPlayer(name).isOnline())
									asPlayer(name).getPlayer().sendMessage("§6§lNotice: §b" + getPlayer().getName() + "§f has declined to be your friend...");
							}
						} else {
							if(getManager().getPlayerManager().getPlayerData(asPlayer(name)).getChatSettings().isAllowingFriendRequest()) {
								getManager().getPlayerManager().getPlayerData(asPlayer(name)).addFriendRequest(getPlayer());
								getPlayer().sendMessage("§bYou have sent a friend request to §6" + name + "§b.");
								
								if(asPlayer(name).isOnline())
									asPlayer(name).getPlayer().sendMessage("§6§lNotice: §b" + getPlayer().getName() + "§f has sent you a friend request!");
							} else
								getPlayer().sendMessage("§cThat player is not accepting friend requests at this moment...");
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
