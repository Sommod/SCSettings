package com.soulcraft.Player;

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

import com.soulcraft.Data.SCSettingsManager;

/**
 * Contains all the PlayetData's for each person as well
 * as the action to save all data to the files to the
 * server folder.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class PlayerManager {
	
	private Map<UUID, PlayerData> data;
	private SCSettingsManager manager;

	public PlayerManager(SCSettingsManager manager) {
		reload(manager);
	}
	
	public void reload(SCSettingsManager manager) {
		this.manager = manager;
		data = new HashMap<UUID, PlayerData>();
		File folder = new File(manager.getPlugin().getDataFolder(), "Data/Player Data");
		
		if(folder.listFiles().length != 0)
			initData(folder);
		
		for(OfflinePlayer check : manager.getPlugin().getServer().getOfflinePlayers()) {
			if(!data.containsKey(check.getUniqueId()))
				data.put(check.getUniqueId(), new PlayerData(check));
		}
	}
	
	// Grabs the data from the stored files folder
	private void initData(File folder) {
		List<File> filesToDelete = new ArrayList<File>();
		
		for(File playerDataFile : folder.listFiles()) {
			
			String fileExt = playerDataFile.toString().substring(playerDataFile.toString().lastIndexOf('.') + 1);
			
			if(fileExt.equalsIgnoreCase("file")) {
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(playerDataFile));
					data.put(UUID.fromString(playerDataFile.getName()), (PlayerData) ois.readObject());
					ois.close();
				} catch (IOException | ClassNotFoundException e) {
					manager.getPlugin().getLogger().log(Level.WARNING, "Error, could not load Player Data (" + playerDataFile.getName() + ").");
					e.printStackTrace();
				}
			
			// If file is a .YML backup file
			} else if(fileExt.equalsIgnoreCase("yml")) {
				YamlConfiguration config = YamlConfiguration.loadConfiguration(playerDataFile);
				String playerID = config.getConfigurationSection("").getKeys(false).iterator().next();
				PlayerData playerData = new PlayerData(Bukkit.getServer().getOfflinePlayer(UUID.fromString(playerID)));
				
				config.getStringList(playerID + ".friends").forEach(uuid -> { playerData.addFriend(Bukkit.getServer().getOfflinePlayer(UUID.fromString(uuid))); });
				playerData.getChatSettings().setAllowFriendRequets(config.getBoolean(playerID + ".chat settings.allow requests"));
				playerData.getChatSettings().setIgnoreGlobalChat(config.getBoolean(playerID + ".chat settings.ignore chat"));
				playerData.getChatSettings().setNotifyFriendJoin(config.getBoolean(playerID + ".chat settings.notify friend join"));
				playerData.getChatSettings().setDeathMessages(config.getBoolean(playerID + ".chat settings.death messages"));
				
				data.put(playerData.getPlayer().getUniqueId(), playerData);
				filesToDelete.add(playerDataFile);
			} else
				continue;
			
			// Deletes the backup files only
			// Object saved files remain until the next
			// save. But .YML files are removed to
			// checking if the file exists in the saving
			// method
			if(!filesToDelete.isEmpty())
				filesToDelete.forEach(file -> { file.delete(); });
			
		}
	}
	
	/**
	 * Saves all the PlayerData to the server.
	 */
	public void saveData() {
		for(PlayerData pd : data.values()) {
			File playerDataFile = new File(manager.getPlugin().getDataFolder(), "Data/Player Data/" + pd.getPlayer().getUniqueId().toString() + ".file");
			
			try {
				
				if(playerDataFile.exists())
					playerDataFile.delete();
				playerDataFile.createNewFile();
				
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(playerDataFile));
				oos.writeObject(pd);
				oos.flush();
				oos.close();
				
			} catch (IOException e) {
				manager.getPlugin().getLogger().log(Level.WARNING, "Error, could not save data into ../plugins/SCSettings/Data/PlayerData/" + pd.getPlayer().getUniqueId() + ". Attempting backup save of data...");
				e.printStackTrace();
				
				if(playerDataFile.exists())
					playerDataFile.delete();
				
				backupSave(pd);
			}
		}
	}
	
	// Tries to complete a backup save when the Object Serialization fails.
	// This will save all the data by parsing it into Strings into a
	// Bukkit YamlConfiguration file.
	private void backupSave(PlayerData playerData) {
		try {
			File file = new File(manager.getPlugin().getDataFolder(), "Data/Player Data/" + playerData.getPlayer().getUniqueId().toString() + ".yml");
			file.createNewFile();
			
			YamlConfiguration config = new YamlConfiguration();
			List<String> friendsList = new ArrayList<String>();
			String playerID = playerData.getPlayer().getUniqueId().toString();
			
			playerData.getAllFriends().forEach(off -> { friendsList.add(off.getUniqueId().toString()); });
			
			config.set(playerID + ".friends", friendsList);
			config.set(playerID + ".chat settings.allow requests", playerData.getChatSettings().isAllowingFriendRequest());
			config.set(playerID + ".chat settings.ignore chat", playerData.getChatSettings().isIgnoringGlobalChat());
			config.set(playerID + ".chat settings.notify friend join", playerData.getChatSettings().isNotifiedFriendJoin());
			config.set(playerID + ".chat settings.death messages", playerData.getChatSettings().isSeeingDeathMessages());
			
			config.save(file);
			
		} catch (IOException e) {
			manager.getPlugin().getLogger().log(Level.SEVERE, "Error, could not create Backup file ../plugin/SCSettings/Data/Player Data/" + playerData.getPlayer().getUniqueId().toString() + ".yml... Data will be lost.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the PlayerData of the given player exists.
	 * @param player - Player to check
	 * @return True - if PlayerData exists
	 */
	public boolean exists(OfflinePlayer player) { return data.containsKey(player.getUniqueId()); }
	
	/**
	 * Gets the PlayerData of the given player. If the PlayerData
	 * does not exist, then a null value will be returned.
	 * @param player - player to get PlayerData of
	 * @return PlayerData - if found, otherwise NULL
	 */
	public PlayerData getPlayerData(OfflinePlayer player) { return data.get(player.getUniqueId()); }
	
	/**
	 * Creates a new PlayerData for the given player. Note: if
	 * the PlayerData of the given player already exists, then
	 * the current PlayerData will be deleted and a new PlayerData
	 * will be created for the player.
	 * @param player - player to create PlayerData
	 */
	public void createNewPlayerData(OfflinePlayer player) {
		if(exists(player))
			deletePlayerData(player);
		
		data.put(player.getUniqueId(), new PlayerData(player));
	}
	
	/**
	 * Deletes the PlayerData of the given player.
	 * @param player - player to remove PlayerData of
	 */
	public void deletePlayerData(OfflinePlayer player) { data.remove(player.getUniqueId()); }

}
