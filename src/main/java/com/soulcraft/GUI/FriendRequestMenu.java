package com.soulcraft.GUI;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Player.PlayerData;

/**
 * Creates a new menu that is used for the players opening
 * their list of people who have sent them a friend request
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class FriendRequestMenu extends AbstractMenu {

	public FriendRequestMenu(SCSettingsManager manager) {
		super(manager, new File(manager.getPlugin().getDataFolder(), "Data/Gui Data/Friend Request.yml"));
	}

	@Override
	public void open(Player player) {
		Inventory toOpen = getBaseInventory();
		PlayerData pData = getManager().getPlayerManager().getPlayerData(player);
		
		for(int i = 10, k = 0; i < toOpen.getSize() - 9 && k < pData.getAllFriendRequests().size(); i++) {
			if((i + 1) % 9 == 0) {
				i++;
				continue;
			}
			
			ItemStack head = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) head.getItemMeta();
			
			meta.setOwningPlayer(pData.getAllFriendRequests().get(k));
			meta.setDisplayName("§b" + pData.getAllFriendRequests().get(k).getName());
			meta.setLore(Arrays.asList("§6Left Click: §aAccept", "§6Right Click: §cDecline"));
			
			head.setItemMeta(meta);
			
			toOpen.setItem(i, head);
		}
		
		if(isUsingFillOption() && !isBorder())
			toOpen = getFilledInventory(toOpen);
		
		player.closeInventory();
		player.openInventory(toOpen);
	}

}
