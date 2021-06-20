package com.soulcraft.Event.InventoryHandlers;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.AbstractHandler;
import com.soulcraft.GUI.FriendRequestMenu;
import com.soulcraft.Player.PlayerData;

/**
 * Handles the actions done within the FriendRequestMenu
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class FriendRequestHandler extends AbstractHandler<FriendRequestMenu> {

	public FriendRequestHandler(InventoryClickEvent event, SCSettingsManager manager) {
		super(event, manager, manager.getGuiManager().getFriendRequestMenu());
	}
	
	@Override
	public void execute() {
		getEvent().setCancelled(true); // Defaulted to cancel
		
		if(isTopInventory()) {
			if(getMenu().isButton(getEvent().getSlot())) {
				if(openMenu())
					return;
				
			} else {
				ItemStack item = getEvent().getInventory().getItem(getEvent().getSlot());
				
				if(item.getType() == Material.PLAYER_HEAD) {
					if(getEvent().getClick() == ClickType.LEFT) {
						String name = item.getItemMeta().getDisplayName().substring(2);
						PlayerData data = getManager().getPlayerManager().getPlayerData(getPlayer());
						OfflinePlayer target = asPlayer(name);
					
						data.removeFriendRequest(target);
						data.addFriend(target);
						getManager().getPlayerManager().getPlayerData(target).addFriend(getPlayer());
						
						getPlayer().sendMessage("§aYou have added §b" + name + "§a as a friend.");
						getEvent().getInventory().setItem(getEvent().getSlot(), null);
					
					} else if(getEvent().getClick() == ClickType.RIGHT) {
						getManager().getPlayerManager().getPlayerData(getPlayer()).removeFriendRequest(asPlayer(item.getItemMeta().getDisplayName().substring(2)));
						getPlayer().sendMessage("§cYou have declined the friend request from §b" + item.getItemMeta().getDisplayName().substring(2));
						getEvent().getInventory().setItem(getEvent().getSlot(), null);
					}
				}
			}
		}
	}
	
	// Gets the player object from the name
	private OfflinePlayer asPlayer(String name) {
		for(OfflinePlayer off : getManager().getPlugin().getServer().getOfflinePlayers()) {
			if(off.getName().equalsIgnoreCase(name))
				return off;
		}
		
		return null;
	}

}
