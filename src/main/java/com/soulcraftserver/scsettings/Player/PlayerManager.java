package com.soulcraftserver.scsettings.Player;

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

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.soulcraftserver.aacore.API.Managers.FileManager;
import com.soulcraftserver.scsettings.Data.Manager;
import com.soulcraftserver.scsettings.Items.ItemData;

/**
 * Manages and handles the PlayerData information of
 * each player. Handles the saving and getting of the
 * information from the player data file stored wtihin
 * plugin data folder.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class PlayerManager {
	
	private Map<UUID, PlayerData> players;
	private Manager manager;
	
	/**
	 * Creates a PlayerData manager that contains all information about each
	 * player.
	 * @param manager - Plugin Manager
	 */
	public PlayerManager(Manager manager) {
		this.manager = manager;
		initPlayers();
	}
	
	/**
	 * Gets the MAP of all the PlayerDatas. Each PlayerData is attached
	 * to a specific players UUID.
	 * @return Map{@code <UUID, PlayerData>}
	 */
	public Map<UUID, PlayerData> getAllPlayers() { return players; }
	
	/**
	 * Gets the PlayerData based on the UUID of player. If the PlayerData
	 * does not exist, then this will return null.
	 * @param id - UUID of player 
	 * @return PlayerData
	 */
	public PlayerData getPlayer(UUID id) { return players.get(id); }
	
	/**
	 * Gets the PlayerData based on the UUID of player. If the PlayerData
	 * does not exist, then this will return null.
	 * @param player - Player to get
	 * @return PlayerData
	 */
	public PlayerData getPlayer(OfflinePlayer player) { return getPlayer(player.getUniqueId()); }
	
	@SuppressWarnings("unchecked")
	private void initPlayers() {
		if((new File(manager.getPlugin().getDataFolder(), "Data/backup playerdata save.yml")).exists()) {
			File backup = new File(manager.getPlugin().getDataFolder(), "Data/backup playerdata save.yml");
			YamlConfiguration config = YamlConfiguration.loadConfiguration(backup);
			
			// Outter Loop
			for(String playerDataUUID : config.getConfigurationSection("").getKeys(false)) {
				PlayerData playerData = new PlayerData(Bukkit.getServer().getOfflinePlayer(UUID.fromString(playerDataUUID)));
				
				// Friends
				for(String uid : config.getStringList(playerDataUUID + ".friends"))
					playerData.addFriend(Bukkit.getServer().getOfflinePlayer(UUID.fromString(uid)));
				
				// Settings
				
				// ItemDats
				for(String ItemDataUID : config.getConfigurationSection(playerDataUUID + ".itemdata").getKeys(false)) {
					String path = playerDataUUID + ".itemdata." + ItemDataUID + ".";
					
					ItemStack item = ItemStack.deserialize((Map<String, Object>) config.get(path + "item"));
					UUID senderUID = UUID.fromString(path + "senderuid");
					UUID friendUID = UUID.fromString(path + "frienduid");
					long expireTime = config.getLong(path + "expiretime");
					ItemData id = new ItemData(Bukkit.getServer().getOfflinePlayer(senderUID), Bukkit.getServer().getOfflinePlayer(friendUID), item, expireTime, new ArrayList<String>(), manager);
					
					if(UUID.fromString(playerDataUUID).equals(senderUID))
						id.setFormat(manager.getConfiguration().getStringList("gui.format gift"));
					else if(UUID.fromString(playerDataUUID).equals(friendUID))
						id.setFormat(manager.getConfiguration().getStringList("gui.format return"));
					
					playerData.addItem(id);
				}
			}
			
			backup.delete();
			
		// Object Serialization Handler
		} else {
			File savedFile = new File(manager.getPlugin().getDataFolder(), "Data/Player Data.sc");
			
			if(!savedFile.exists()) {
				players = new HashMap<UUID, PlayerData>();
				return;
			}
			
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedFile));
				players = (Map<UUID, PlayerData>) ois.readObject();
				ois.close();
			} catch (Exception e) {
				FileManager.logExceptionToFile(manager.getPlugin(), e);
				players = new HashMap<UUID, PlayerData>();
			}
		}
	}
	
	/**
	 * Saves all the player data to the given file. This has two
	 * save-type methods. The first is to store the data into a 
	 * file using Object Serialization, should that fail, then
	 * the data is saved into YMl file as a backup file.
	 */
	public void save() {
		File playersFile = new File(manager.getPlugin().getDataFolder(), "Data/Player Data.sc");
		playersFile.delete();
		
		try {
			if(!playersFile.exists())
				playersFile.createNewFile();
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(playersFile));
			oos.writeObject(players);
			oos.flush();
			oos.close();
			
		} catch (IOException e) {
			FileManager.logExceptionToFile(manager.getPlugin(), e);
			manager.logMessage("Error, could not save player data to file... Attempting backup save");
			backupSave();
		}
	}
	
	// Performs the backup save of all the player data.
	// The data is saved with the top level being the players
	// UUID, with the Items being another Configuration Section,
	// the Settings being a configuration Section, and the friends
	// as a list of UUID's.
	private void backupSave() {
		File ymlfile = new File(manager.getPlugin().getDataFolder(), "Data/backup playerdata file.yml");
		
		if(ymlfile.exists())
			ymlfile.delete();
		
		try {
			ymlfile.createNewFile();
			YamlConfiguration config = new YamlConfiguration();
			
			// Outter Loop (Main config Sections [UUID of playerData])
			for(PlayerData playerData : players.values()) {
				config.createSection(playerData.getID().toString());
				config.createSection(playerData.getID().toString() + ".itemdata");
				config.createSection(playerData.getID().toString() + ".settings");
				config.createSection(playerData.getID().toString() + ".friends");
				
				//String settingsPath = playerData.getID().toString() + ".settings.";
				String itemPath = playerData.getID().toString() + ".itemdata.";
				
				// Friends
				List<String> friendsToString = new ArrayList<String>();
				for(UUID fID : playerData.getFriends())
					friendsToString.add(fID.toString());
				config.set(playerData.getID().toString() + ".friends", friendsToString);
				
				// Settings
				
				// ItemData
				if(!playerData.getAllItems().isEmpty()) {
					List<ItemData> items = playerData.getAllItems();
					
					// ItemData Loop (itemdata section)
					for(ItemData id : items) {
						config.set(itemPath + "." + id.getID().toString() + ".item", id.getItem().serialize());
						config.set(itemPath + "." + id.getID().toString() + ".expiretime", id.getExpireTime());
						config.set(itemPath + "." + id.getID().toString() + ".senderuid", id.getSenderUID().toString());
						config.set(itemPath + "." + id.getID().toString() + "frienduid", id.getFriendUID().toString());
					}
				}
			}
			
			config.save(ymlfile);
			
		} catch (IOException e) {
			FileManager.logExceptionToFile(manager.getPlugin(), e);
			manager.logMessage(Level.SEVERE, "NOTICE: Could not save player data to plugin folder. Data will be lost on next plugin shutdown...");
		}
	}

}
