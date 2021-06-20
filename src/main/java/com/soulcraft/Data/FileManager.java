package com.soulcraft.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * This handles and stores the information about
 * each file that is used within this plugin.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class FileManager {

	private SCSettingsManager manager;
	private Map<String, String> loc;
	
	/**
	 * Used to initialize the files that are used
	 * within this plugin as well as used for getting
	 * the YamlConfigurations.
	 * @param manager - Manager of plugin
	 */
	public FileManager(SCSettingsManager manager) {
		this.manager = manager;
		init();
	}
	
	// Stores all the file locations that are used for
	// getting the file of the given location.
	private void init() {
		loc = new HashMap<String, String>();
		String basePath = manager.getPlugin().getDataFolder().getAbsolutePath();
		
		if(!manager.getPlugin().getDataFolder().exists())
			manager.getPlugin().getDataFolder().mkdir();
		
		loc.put("CONFIG", basePath + "config.yml");
		loc.put("HELP", basePath + "help.txt");
		loc.put("DATA_FOLDER", basePath + "Data");
		loc.put("PLAYER_DATA", basePath + "Data/Player Data");
		loc.put("GUI_DATA", basePath + "Data/Gui Data");
		loc.put("MAIN_MENU", basePath + "Data/Gui Data/Main Menu.yml");
		loc.put("ADD_FRIEND", basePath + "Data/Gui Data/Add Friend.yml");
		loc.put("FRIEND_REQUEST", basePath + "Data/Gui Data/Friend Request.yml");
		loc.put("FRIENDS_MENU", basePath + "Data/Gui Data/Friends Menu.yml");
		loc.put("GIFT_MENU", basePath + "Data/Gui Data/Gift Menu.yml");
		loc.put("ADMIN_STORAGE", basePath + "Data/Gui Data/Admin Storage.yml");
		loc.put("SETTINGS_MENU", basePath + "Data/Gui Data/Settings Menu.yml");
		loc.put("ITEM_COLLECTION", basePath + "Data/Gui Data/Item Collection.yml");
		loc.put("BANKNOTE_MENU", basePath + "Data/Gui Data/BankNote Menu.yml");
		
		writeFile(getStream("config.yml"), getFile(loc.get("CONFIG")));
		writeFile(getStream("help.txt"), getFile(loc.get("HELP")));
		writeFile(null, getFile(loc.get("DATA_FOLDER")));
		writeFile(null, getFile(loc.get("PLAYER_DATA")));
		writeFile(null, getFile(loc.get("GUI_DATA")));
		writeFile(getStream("main_menu.yml"), getFile(loc.get("MAIN_MENU")));
		writeFile(getStream("add_friend.yml"), getFile(loc.get("ADD_FRIEND")));
		writeFile(getStream("friend_request.yml"), getFile(loc.get("FRIEND_REQUEST")));
		writeFile(getStream("friends_menu.yml"), getFile(loc.get("FRIENDS_MENU")));
		writeFile(getStream("gift_menu.yml"), getFile(loc.get("GIFT_MENU")));
		writeFile(getStream("admin_storage.yml"), getFile(loc.get("ADMIN_STORAGE")));
		writeFile(getStream("settings_menu.yml"), getFile(loc.get("SETTINGS_MENU")));
		writeFile(getStream("item_collect.yml"), getFile(loc.get("ITEM_COLLECTION")));
		writeFile(getStream("bank.yml"), getFile(loc.get("BANKNOTE_MENU")));
	}
	
	// Gets the file as a InputStream that can be
	// used to write the bytes into a file.
	private InputStream getStream(String fileName) { return manager.getPlugin().getClass().getResourceAsStream("resources/" + fileName); }
	
	/**
	 * When this is issued, the files are re-obtained
	 * and re-created if they do not exist.
	 */
	public void reload() { init(); }
	
	/**
	 * Gets the folder called Data that is within the Plugin DataFolder.
	 * This file only contains Two sub-folders, which in turn contain the
	 * files of information.
	 * @return File
	 */
	public File getDataFolder() { return getFile(loc.get("DATA_FOLDER")); }
	
	/**
	 * Gets the folder called Gui Data. This contain all the files that
	 * are used to configurate the Inventory Menus that are used for the
	 * plugin Friends system.
	 * @return File
	 */
	public File getGuiDataFolder() { return getFile(loc.get("GUI_DATA")); }
	
	/**
	 * Gets the folder that contains EVERY players data file. These data
	 * files contain the information about friends and chat settings. The
	 * folder will contain every player that has ever played on the server.
	 * @return File
	 */
	public File getPlayerDataFolder() { return getFile(loc.get("PLAYER_DATA")); }
	
	/**
	 * Gets YamlConfiguration of the given file. This takes the name of the file
	 * <b>WITHOUT</b> the extension at the end. If the file doesn't exist, or if
	 * the file is a folder, then this will return null. This only handles the
	 * Gui Data files.
	 * @param file - Name of file
	 * @return YamlConfiguration
	 */
	public YamlConfiguration getConfig(String file) { return getKey(file) != null ? YamlConfiguration.loadConfiguration(getFile(loc.get(getKey(file)))) : null; }
	
	/**
	 * Gets the File of the YML files found within the Gui Data folder.
	 * @param file - name of file
	 * @return File object
	 */
	public File getConfigAsFile(String file) { return getKey(file) != null ? getFile(loc.get(getKey(file))) : null; }
	
	// Runs through a set set of words and determines if the string
	// is one of the file names. If the file name is of the same,
	// then this will return the Key value of the file name containing
	// the path to the file
	private String getKey(String check) {
		switch (check.toLowerCase()) {
		case "config":
		case "config.yml":
			return "CONFIG";
			
		case "help":
		case "help.txt":
		case "info":
			return "HELP";
			
		case "data folder":
		case "data":
		case "data_folder":
			return "DATA_FOLDER";
			
		case "gui data":
		case "gui_data":
		case "gui folder":
		case "gui_folder":
		case "gui":
			return "GUI_DATA";
			
		case "player data":
		case "player_data":
		case "player data folder":
		case "player_data_folder":
		case "player folder":
		case "player_folder":
		case "player":
			return "PLAYER_DATA";
			
		case "main menu":
		case "main_menu":
		case "main":
			return "MAIN_MENU";
			
		case "add friend":
		case "add_friend":
		case "add friends":
		case "add_friends":
		case "add":
			return "ADD_FRIEND";
			
		case "friend request":
		case "friend_request":
		case "friend requests":
		case "friend_requests":
		case "request":
		case "requests":
			return "FRIEND_REQUEST";

		case "friend menu":
		case "friend_menu":
		case "friends menu":
		case "friends_menu":
		case "friends":
			return "FRIEND_MENU";
		
		case "gift menu":
		case "gift_menu":
		case "gifts menu":
		case "gifts_menu":
		case "gift":
		case "gifts":
			return "GIFT_MENU";
			
		case "admin storage":
		case "admin_storage":
		case "admin":
		case "storage":
			return "ADMIN_STORAGE";
			
		case "settings menu":
		case "settings_menu":
		case "setting menu":
		case "setting_menu":
		case "settings":
		case "setting":
			return "SETTINGS_MENU";
		
		case "item collect":
		case "item_collect":
		case "item collection":
		case "item_collection":
			return "ITEM_COLLECTION";
			
		case "bank":
		case "banknote":
		case "bank menu":
		case "bank_menu":
		case "banknote menu":
		case "banknote_menu":
			return "BANKNOTE_MENU";
			
		default:
			return null;
		}
	}
	
	// Grabs the stored file as an Input Stream
	// Gets each line of InputStream and writes
	// each byte to the config.yml file (toLoad)
	private void writeFile(InputStream fis, File toLoad) {
		if(fis == null) {
			if(!toLoad.exists())
				toLoad.mkdir();
			return;
		}
		
		// Stops writing over current files
		if(toLoad.exists())
			return;
		
		try {
			if(!toLoad.exists())
				toLoad.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(toLoad);
			
			int i = 0;
			byte[] buffer = new byte[1024];
			
			while((i = fis.read(buffer)) != -1)
				fos.write(buffer, 0, i);
			
			fis.close();
			fos.close();
			
		} catch (IOException e) {
			manager.getPlugin().getLogger().log(Level.SEVERE, "Error: Could not Load config data into the config file.");
			e.printStackTrace();
		}
	}
	
	// Gets the file based on the path.
	// This is used instead of storing
	// the files themselves because
	// the files can change during the
	// server running, os new instances
	// are needed for each usage.
	private File getFile(String path) {	return new File(path); }
}
