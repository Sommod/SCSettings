package com.soulcraftserver.scsettings.GUI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.soulcraftserver.scsettings.Data.Manager;
import com.soulcraftserver.scsettings.Player.PlayerData;

/**
 * Handles the menu for the Friends menu. This class
 * is used to display, add, and remove friends from
 * the list. Each friend will, once clicked, open the
 * respective menu for handling sending an item to them.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class FriendsGui {

	private Manager manager;
	private Player player;
	private Inventory menu;
	private int page = 0;
	
	public FriendsGui(Manager manager, Player player) {
		this.player = player;
		this.manager = manager;
		menu = Bukkit.getServer().createInventory(player, 54, "�aFriends");
		fillInventory();
	}
	
	private void fillInventory() {
		List<UUID> friends = manager.getPlayerManager().getPlayer(player).getFriends();
		List<ItemStack> heads = new ArrayList<ItemStack>();
		
		friends.forEach(uuid -> {
			ItemStack headItem = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) headItem.getItemMeta();
			
			meta.setOwningPlayer(manager.getPlugin().getServer().getOfflinePlayer(uuid));
			meta.setDisplayName("�a�l" + manager.getPlugin().getServer().getOfflinePlayer(uuid).getName());
			meta.setLore(formatLore(manager.getConfiguration().getStringList("gui.format friend"), manager.getPlugin().getServer().getOfflinePlayer(uuid)));
			headItem.setItemMeta(meta);
			
			heads.add(headItem);
		});
		
		//TODO: Apply border/Fill in for menu
		//TODO: Input heads into menu
		//TODO: Add buttons for pages and return to main menu
	}
	
	private List<String> formatLore(List<String> format, OfflinePlayer friend) {
		for(int i = 0; i < format.size(); i++) {
			if(format.get(i).contains("%status%"))
				format.set(i, format.get(i).replaceAll("%status%", friend.isOnline() ? "�a�lOnline" : "�c�lOffline"));
			
			if(format.get(i).contains("%balance%"))
				format.set(i, format.get(i).replaceAll("%balance%", /** Get balance from Vault */""));
			
			if(format.get(i).contains("%date%")) {
				Calendar date = manager.getPlayerManager().getPlayer(player).getDate(friend);
				String formattedDate = date.get(Calendar.MONTH) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR);
				format.set(i, format.get(i).replaceAll("%date%", formattedDate));
			}
		}
		
		return format;
	}
}
