package com.soulcraft.Items;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Handles and store all items and their data.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class ItemManager {

	private  Map<UUID, ItemData> items;
	private SCSettingsManager manager;
	
	/**
	 * Creates a new ItemManager for the items of the plugin.
	 * Note that when this is first initialized, this checks
	 * the file that stores the data. If this is called twice,
	 * then the second will grab the data that is stored but
	 * none of the changes that could have occurred. All changes
	 * are changed only when the plugin saves the data to the file.
	 * @param manager - Manager of the plugin
	 */
	public ItemManager(SCSettingsManager manager) {
		this.manager = manager;
		File check = new File(manager.getPlugin().getDataFolder(), "Data/Item Data/Items.sc");
		
		if(check.exists())
			init(check, false);
		else if((check = new File(manager.getPlugin().getDataFolder(), "Data/Item Data/Backup Items.yml")).exists())
			init(check, true);
		else
			items = new HashMap<UUID, ItemData>();
	}
	
	// Attempts to grab the data from the file. If the file
	// is corrupt or has some other error, then a new ArrayList
	// is created more errors.
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void init(File file, boolean isYML) {
		items = new HashMap<UUID, ItemData>();
		
		if(!isYML) {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				items = (Map<UUID, ItemData>) ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		} else {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			for(String i : config.getConfigurationSection("").getKeys(false)) {
				ItemStack item = config.getItemStack(i + ".item");
				OfflinePlayer gifter = config.getOfflinePlayer(i + ".gifter");
				OfflinePlayer receiver = config.getOfflinePlayer(i + ".receiver");
				long expireTime = config.getLong(i + ".time");
				int location = config.getInt(i + ".location");
				UUID id = UUID.fromString(i + ".id");
				
				items.put(id, new ItemData(item, gifter, receiver, expireTime, location, id));
			}
		}
	}
	
	/**
	 * Saves all the item data to the given file
	 * save location.
	 */
	public void save() {
		try {
			File file = new File(manager.getPlugin().getDataFolder(), "Data/Item Data/Items.sc");
			
			if(!file.exists())
				file.createNewFile();
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(items);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			backupSave();
		}
	}
	
	// Saves all the data as a YML file to avoid
	// loss of data.
	private void backupSave() {
		File file = new File(manager.getPlugin().getDataFolder(), "Data/Item Data/Backup Items.yml");
		
		try {
			file.createNewFile();
			YamlConfiguration config = new YamlConfiguration();
			
			for(int i = 0; i < items.size(); i++) {
				ItemData data = items.get(i);
				config.createSection(i + "");
				
				config.set(i +".id", data.getID().toString());
				config.set(i + ".item", data.getItem());
				config.set(i + ".time", data.getExpireTime());
				config.set(i + ".gifter", data.getGifter());
				config.set(i + ".receiver", data.getReceiver());
				config.set(i + ".location", data.isInReceiver() ? 1 : data.isInGifter() ? 2 : 3);
			}
			
			config.save(file);
			
		} catch (IOException e) {
			e.printStackTrace();
			manager.getPlugin().getLogger().log(Level.SEVERE, "Error, could not save ItemData to files... data will be lost when plugin disables. Suggest items are remvoed prior to disabling.");
		}
	}
	
	/**
	 * This method runs through the entire list of ItemData's
	 * and checks if the item has expired. If the item has
	 * expired, then the item is updated in it's location.
	 * If the final location of the item is the Admin Storage
	 * and is expired, then the item is deleted and completely
	 * lost.
	 */
	public void clean() {
		List<ItemData> toRemove = new ArrayList<ItemData>();

		for(ItemData i : items.values()) {
			if(i.isExpired()) {
				if(!i.isInGifter() && !i.isInReceiver())
					toRemove.add(i);
				else {
					updateItemData(i);
				}
			}
		}
		
		if(!toRemove.isEmpty()) {
			for(ItemData i : toRemove)
				items.remove(i.getID());
		}
	}
	
	// Updates the given ItemData by converting the time
	// within the config.yml and setting it as the new
	// expire time.
	private void updateItemData(ItemData data) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(manager.getPlugin().getDataFolder(), "config.yml"));
		String time;
		
		if(data.isInGifter()) {
			time = config.getString("friends.collect time limit");
			long expireTime = getExpireTime(time.split(" "));
			
			if(expireTime == -1L) {
				data.setExpireTime(-1L);
				return;
			}
			
			data.setExpireTime(expireTime + System.currentTimeMillis());
			
		} else {
			time = config.getString("friends.storage time limit");
			long expireTime = getExpireTime(time.split(" "));
			
			if(expireTime == -1L) {
				data.setExpireTime(-1L);
				return;
			}
			
			data.setExpireTime(expireTime + System.currentTimeMillis());
		}
		
	}
	
	/**
	 * Manually updates the given ItemData. This is mostly used
	 * when the player issues the command, or using the GUI system
	 * to decline a gift.
	 * @param itemData - ItemData to update
	 */
	public void updateItem(ItemData itemData) { updateItemData(itemData); }
	
	/**
	 * Gets a list of all the ItemData objects that are present within the system.
	 * @return List of ItemData
	 */
	public List<ItemData> getAllItemData() { return new ArrayList<ItemData>(items.values()); }
	
	/**
	 * Gets the items that the given player can collect. This is used for
	 * both the players that are receiving a gift and the players that
	 * have an item returned to them.
	 * @param player
	 * @return
	 */
	public List<ItemData> getStorage(OfflinePlayer player) {
		List<ItemData> toReturn = new ArrayList<ItemData>();
		
		for(ItemData d : items.values()) {
			if(d.isInReceiver() && d.getReceiver().equals(player))
				toReturn.add(d);
			else if(d.isInGifter() && d.getGifter().equals(player))
				toReturn.add(d);
		}
		
		return toReturn;
	}
	
	/**
	 * Gets a list of all items that are in the Admin Storage
	 * section
	 * @return List of Admin ItemData's
	 */
	public List<ItemData> getAdminStorage() {
		List<ItemData> toReturn = new ArrayList<ItemData>();
		
		for(ItemData d : items.values()) {
			if(!d.isInGifter() & !d.isInReceiver())
				toReturn.add(d);
		}
		
		return toReturn;
	}
	
	/**
	 * Gets the ItemData based on the stored UUID attached to the item.
	 * If no UUID exists by the given UUID, then this will return null
	 * @param id - UUID of item
	 * @return ItemData - if found, otherwise null
	 */
	public ItemData getItemData(UUID id) { return items.get(id); }
	
	/**
	 * Removes the given ItemData based on the UUID provided.
	 * If no ID of this ItemData exists, then nothing happens.
	 * @param id - UUID of ItemData
	 */
	public void removeItemData(UUID id) { items.remove(id); }
	
	/**
	 * Checks if the given UUID exists, essentially checking
	 * if an ItemData exists.
	 * @param id - UUID of ItemData
	 * @return True - If UUID of ItemData exists
	 */
	public boolean isItemData(UUID id) { return items.containsKey(id); }
	
	/**
	 * Adds a new item to the stored item data within the ItemManager.
	 * @param player - Sender of gift
	 * @param friend - Receiver of gift
	 * @param item - Item that is the gift
	 */
	public void addItem(OfflinePlayer player, OfflinePlayer friend, ItemStack item) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(manager.getPlugin().getDataFolder(), "config.yml"));
		ItemData id = new ItemData(item, player, friend, getExpireTime(config.getString("friends.gift time limit").split(" ")));
		
		items.put(id.getID(), id);
	}
	
	// used to get the time of the given string array
	private long getExpireTime(String[] split) {
		String[] calc = split;
		
		if(calc.length == 1 && calc[0].equalsIgnoreCase("none")) {
			return -1L;
		}
		
		long totalTime = 0L;
		
		for(String section : calc) {
			if(section.toLowerCase().contains("d"))
				totalTime += ((((Long.parseLong(section.substring(0, section.length())) * 24) * 60) * 60) * 1000);
			else if(section.toLowerCase().contains("h"))
				totalTime += (((Long.parseLong(section.substring(0, section.length())) * 60) * 60) * 1000);
			else
				totalTime += ((Long.parseLong(section.substring(0, section.length())) * 60) * 1000);
		}
		
		return totalTime;
	}

}
