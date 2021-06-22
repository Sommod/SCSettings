package com.soulcraft.GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Player.PlayerData;

/**
 * Handles the menu that is for the Player's
 * Server Settings.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class SettingsMenu extends AbstractMenu {

	private ItemStack deathMessage, joinFriend, allowRequests, ignorechat;
	
	public SettingsMenu(SCSettingsManager manager) {
		super(manager, new File(manager.getPlugin().getDataFolder(), "Data/Gui Data/Settings Menu.yml"));
		initItems();
	}
	
	// Creates and stores the items that are used in
	// the settings menu.
	private void initItems() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(getManager().getPlugin().getDataFolder(), "Data/Gui Data/Settings Menu.yml"));
		ItemMeta meta;
		List<String> lore = new ArrayList<String>();
		
		// Death Messages
		if(config.isConfigurationSection("items.death messages")) {
			deathMessage = new ItemStack(Material.getMaterial(config.getString("items.death messages.item")), config.getInt("items.death messages.amount"));
			meta = deathMessage.getItemMeta();
			
			meta.setDisplayName(config.getString("items.death messages.name").replace('&', '§'));
			config.getStringList("items.death messages.lore").forEach(line -> { lore.add(line.replace('&', '§')); });
			meta.setLore(lore);
			lore.clear();
			
			if(config.getBoolean("items.death messages.enchanted")) {
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			 deathMessage.setItemMeta(meta);
		} else {
			deathMessage = new ItemStack(Material.OAK_SIGN, 1);
			meta = deathMessage.getItemMeta();
			
			meta.setDisplayName("§bToggle Death Messages");
			meta.setLore(Arrays.asList("§7Enable or Disable Death Messages."));
			
			deathMessage.setItemMeta(meta);
		}
		
		// Friend Join Notification
		if(config.isConfigurationSection("items.friend join notify")) {
			joinFriend = new ItemStack(Material.getMaterial(config.getString("items.friend join notify.item")), config.getInt("items.friend join notify.amount"));
			meta = joinFriend.getItemMeta();
			
			meta.setDisplayName(config.getString("items.friend join notify.name").replace('&', '§'));
			config.getStringList("items.friend join notify.lore").forEach(line -> { lore.add(line.replace('&', '§')); });
			meta.setLore(lore);
			lore.clear();
			
			if(config.getBoolean("items.friend join notify.enchanted")) {
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			 joinFriend.setItemMeta(meta);
		} else {
			joinFriend = new ItemStack(Material.EMERALD, 1);
			meta = joinFriend.getItemMeta();
			
			meta.setDisplayName("§bToggle Friend Join Notification");
			meta.setLore(Arrays.asList("§7Enable or Disable the messages of a friend joining the server."));
			
			joinFriend.setItemMeta(meta);
		}
		
		// Allow Friend Requests
		if(config.isConfigurationSection("items.death messages")) {
			allowRequests = new ItemStack(Material.getMaterial(config.getString("items.allow requests.item")), config.getInt("items.allow requests.amount"));
			meta = allowRequests.getItemMeta();
			
			meta.setDisplayName(config.getString("items.allow requests.name").replace('&', '§'));
			config.getStringList("items.allow requests.lore").forEach(line -> { lore.add(line.replace('&', '§')); });
			meta.setLore(lore);
			lore.clear();
			
			if(config.getBoolean("items.allow requests.enchanted")) {
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			 allowRequests.setItemMeta(meta);
		} else {
			allowRequests = new ItemStack(Material.DIAMOND, 1);
			meta = allowRequests.getItemMeta();
			
			meta.setDisplayName("§bToggle Allow Friend Requests");
			meta.setLore(Arrays.asList("§7Enable or Disable the ability for people to send you a friend request."));
			
			allowRequests.setItemMeta(meta);
		}
		
		// Ignore Chat
		if(config.isConfigurationSection("items.ignore chat")) {
			ignorechat = new ItemStack(Material.getMaterial(config.getString("items.ignore chat.item")), config.getInt("items.ignore chat.amount"));
			meta = ignorechat.getItemMeta();
			
			meta.setDisplayName(config.getString("items.ignore chat.name").replace('&', '§'));
			config.getStringList("items.ignore chat.lore").forEach(line -> { lore.add(line.replace('&', '§')); });
			meta.setLore(lore);
			lore.clear();
			
			if(config.getBoolean("items.ignore chat.enchanted")) {
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			 ignorechat.setItemMeta(meta);
		} else {
			ignorechat = new ItemStack(Material.PAPER, 1);
			meta = ignorechat.getItemMeta();
			
			meta.setDisplayName("§bToggle Ignore Global Chat");
			meta.setLore(Arrays.asList("§7Enable or Disable the ability to see the Global Chat."));
			
			ignorechat.setItemMeta(meta);
		}
		
		
	}

	@Override
	public void open(Player player) {
		Inventory toOpen = getBaseInventory();
		PlayerData pData = getManager().getPlayerManager().getPlayerData(player);
		
		// Items
		toOpen.setItem(10, deathMessage);
		toOpen.setItem(11, joinFriend);
		toOpen.setItem(12, allowRequests);
		toOpen.setItem(13, ignorechat);
		
		ItemStack display = new ItemStack(pData.getChatSettings().isSeeingDeathMessages() ? Material.LIME_DYE : Material.GRAY_DYE);
		toOpen.setItem(19, display);
		
		display = new ItemStack(pData.getChatSettings().isNotifiedFriendJoin() ? Material.LIME_DYE : Material.GRAY_DYE);
		toOpen.setItem(20, display);
		
		display = new ItemStack(pData.getChatSettings().isAllowingFriendRequest() ? Material.LIME_DYE : Material.GRAY_DYE);
		toOpen.setItem(21, display);
		
		display = new ItemStack(pData.getChatSettings().isIgnoringGlobalChat() ? Material.LIME_DYE : Material.GRAY_DYE);
		toOpen.setItem(22, display);
		
		if(isUsingFillOption() && !isBorder())
			toOpen = getFilledInventory(toOpen);
		
		player.closeInventory();
		player.openInventory(toOpen);
	}

}
