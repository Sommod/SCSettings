package com.soulcraft.GUI;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Player.PlayerData;

/**
 * Handles the creation of the Friends menu
 * for each person that opens the menu.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class FriendsMenu extends AbstractMenu {

	private ItemStack head = new ItemStack(Material.PLAYER_HEAD);
	private Map<Player, Integer> page;
	
	public FriendsMenu(SCSettingsManager manager) {
		super(manager, new File(manager.getPlugin().getDataFolder(), "Data/Gui Data/Friend Menu.yml"));
		page = new HashMap<Player, Integer>();
	}

	@Override
	public void open(Player player) {
		player.closeInventory();
		page.put(player, 1);
		player.openInventory(getBaseInventory());
		openPage(player, 1);
	}
	
	/**
	 * Changes the page the player is currently viewing.
	 * @param player - Player to affect
	 * @param page - Page to set
	 */
	public void openPage(Player player, int page) {
		PlayerData pData = getManager().getPlayerManager().getPlayerData(player);
		
		
		// Removes any items from the menu
		for(int i = 10; i < getBaseInventory().getSize() - 9; i++) {
			if((i + 1) % 9 == 0) {
				i++;
				continue;
			}
		
			player.getOpenInventory().getTopInventory().setItem(i, null);
		}
		
		// Sets the player heads
		for(int i = 0, k = (page * 28); i < getBaseInventory().getSize() && k < pData.getAllFriends().size(); i++) {
			ItemStack temp = head.clone();
			SkullMeta meta = (SkullMeta) temp.getItemMeta();
			
			meta.setOwningPlayer(pData.getAllFriends().get(k));
			meta.setDisplayName("§b" + pData.getAllFriends().get(k).getName());
			meta.setLore(Arrays.asList("§6Left Click: §aSend Gift", "§6Middle Button: §7View Chat Only", "§6Right Click: §cRemove Friend"));
			
			temp.setItemMeta(meta);
			
			player.getOpenInventory().getTopInventory().setItem(i, temp);
		}
		
		if(isUsingFillOption() && !isBorder()) {
			for(int i = 0; i < getBaseInventory().getSize(); i++) {
				if(player.getOpenInventory().getTopInventory().getItem(i) == null)
					player.getOpenInventory().getTopInventory().setItem(i, getFillItem());
			}
		}
	}

}
