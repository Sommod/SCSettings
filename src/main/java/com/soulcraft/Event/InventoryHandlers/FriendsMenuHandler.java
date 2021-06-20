package com.soulcraft.Event.InventoryHandlers;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.AbstractHandler;
import com.soulcraft.GUI.FriendsMenu;
import com.soulcraft.Player.PlayerData;

/**
 * Handles the actions done within the Friends menu
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class FriendsMenuHandler extends AbstractHandler<FriendsMenu> {

	public FriendsMenuHandler(InventoryClickEvent event, SCSettingsManager manager) {
		super(event, manager, manager.getGuiManager().getFriendsMenu());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute() {
		getEvent().setCancelled(true); // Defaulted to Cancel
		
		if(isTopInventory()) {
			if(getMenu().isButton(getEvent().getSlot())) {
				if(openMenu())
					return;
				
				switch (getMenu().getButton(getEvent().getSlot())) {
				case NEXT_PAGE:
					getManager().getGuiManager().getFriendsMenu().openPage(getPlayer(), getManager().getGuiManager().getFriendsMenu().getPage(getPlayer()) + 1);
					break;
					
				case PREVIOUS_PAGE:
					getManager().getGuiManager().getFriendsMenu().openPage(getPlayer(), getManager().getGuiManager().getFriendsMenu().getPage(getPlayer()) - 1);

				default:
					break;
				}
			
			} else { 
				ItemStack item = getEvent().getInventory().getItem(getEvent().getSlot());
				
				if(item.getType() == Material.PLAYER_HEAD) {
					OfflinePlayer target = asPlayer(item.getItemMeta().getDisplayName().substring(2));
					
					if(getEvent().getClick() == ClickType.LEFT) {
						getManager().getGuiManager().getGiftMenu().open(getPlayer());
						getManager().getGuiManager().giftMenu(getPlayer(), target);
					} else if(getEvent().getClick() == ClickType.RIGHT) {
						getManager().getPlayerManager().getPlayerData(getPlayer()).removeFriend(target);
						getManager().getPlayerManager().getPlayerData(target).removeFriend(getPlayer());
						
						getPlayer().sendMessage("§b" + target.getName() + "§c has been removed as a friend...");
						getEvent().getInventory().setItem(getEvent().getSlot(), null);
					
					} else if(getEvent().getClick() == ClickType.MIDDLE) {
						PlayerData data = getManager().getPlayerManager().getPlayerData(getPlayer());
						
						if(data.getChatSettings().isViewingFriendchat(target)) {
							data.getChatSettings().removeFriendChat(target);
							getPlayer().sendMessage("§acYou have removed §b" + target.getName() + "§c from the list of viewable messages from.");
						} else {
							if(data.getChatSettings().isIgnoringGlobalChat())
								data.getChatSettings().setIgnoreGlobalChat(false);
							data.getChatSettings().addFriendChat(target);
							getPlayer().sendMessage("§aYou have added §b" + target.getName() + "§a to the list of viewable messages from.");
						}
					}
				}
			}
		}
	}
	
	// Gets the Player object based on the player name
	private OfflinePlayer asPlayer(String name) {
		for(OfflinePlayer off : getManager().getPlugin().getServer().getOfflinePlayers()) {
			if(off.getName().equalsIgnoreCase(name))
				return off;
		}
		
		return null;
	}

}
