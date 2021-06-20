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
					String name = item.getItemMeta().getDisplayName().substring(2);
					OfflinePlayer target = asPlayer(name);
					
					if(getEvent().getClick() == ClickType.LEFT) {
						PlayerData data = getManager().getPlayerManager().getPlayerData(getPlayer());
						
						data.removeFriendRequest(target);
						data.addFriend(target);
						getManager().getPlayerManager().getPlayerData(target).addFriend(getPlayer());
						
						getPlayer().sendMessage("§aYou have added §b" + name + "§a as a friend.");
						getEvent().getInventory().setItem(getEvent().getSlot(), null);
						
						if(target.isOnline())
							target.getPlayer().sendMessage("§6§lNotice: §b" + getPlayer().getName() + "§f has added you as a friend!");
					
					} else if(getEvent().getClick() == ClickType.RIGHT) {
						getManager().getPlayerManager().getPlayerData(getPlayer()).removeFriendRequest(target);
						getPlayer().sendMessage("§cYou have declined the friend request from §b" + target.getName());
						getEvent().getInventory().setItem(getEvent().getSlot(), null);
						
						if(target.isOnline())
							target.getPlayer().sendMessage("§6§lNotice: §b" + getPlayer().getName() + "§f has declined being your friend...");
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
