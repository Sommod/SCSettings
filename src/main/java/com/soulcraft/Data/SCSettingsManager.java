package com.soulcraft.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import com.soulcraft.SCSettings;
import com.soulcraft.Items.ItemManager;
import com.soulcraft.Player.PlayerManager;

/**
 * Contains all the manager-type classes. Initializes Any manager
 * classes as well as registering Events that are handled within
 * this plugin. All manager-type classes store an instance of this
 * class to interconnected.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class SCSettingsManager {
	
	private SCSettings plugin;
	private final String[] header = new String[]{"################", "#+------------+#", "Initializing SCSettings..."};
	private final String[] footer = new String[]{"SCSettings Active!", "#+------------+#", "################"};
	
	private PlayerManager playerManager;
	private ItemManager itemManager;
	
	/**
	 * Creates a new Manager object for the SCSettings plugin. This activates
	 * all the initialization of all the manager-type objects.
	 * @param plugin - SCSettings plugin class object
	 */
	public SCSettingsManager(SCSettings plugin) {
		this.plugin = plugin;
		
		// Display plugin progress
		for(String h : header)
			plugin.getLogger().info(h);
		
		// Configuration
		plugin.getLogger().info("Registering Configuration...");
		registerConfiguration();
		plugin.getLogger().info("Configuration Registered!");
		
		//TODO: Input details about what is initialized.
		
		for(String f : footer)
		 plugin.getLogger().info(f);
	}
	
	private void registerCommands() {
		
	}
	
	private void registerEvents() {
		
	}
	
	/**
	 * Registers any and all config-type files.
	 */
	private void registerConfiguration() {
		
		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		
		// Ensures folders exists
		File file = new File(plugin.getDataFolder(), "Data");
					
		if(!file.exists())
			file.mkdir();
		file = new File(plugin.getDataFolder(), "Data/Player Data");
		
		if(!file.exists())
			file.mkdir();
		file = new File(plugin.getDataFolder(), "Data/Gui Data");
		
		if(!file.exists())
			file.mkdir();
		
		writeFile(plugin.getClass().getResourceAsStream("config.yml"), new File(plugin.getDataFolder(), "config.yml"));
		writeFile(plugin.getClass().getResourceAsStream("add_friend.yml"), new File(plugin.getDataFolder(), "Data/Gui Data/Add Friend.yml"));
		writeFile(plugin.getClass().getResourceAsStream("friend_request.yml"), new File(plugin.getDataFolder(), "Data/Gui Data/Friend Request.yml"));
		writeFile(plugin.getClass().getResourceAsStream("main_menu.yml"), new File(plugin.getDataFolder(), "Data/Gui Data/Main Menu.yml"));
	}
	
	// Grabs the stored file as an Input Stream
	// Gets each line of InputStream and writes
	// each byte to the config.yml file (toLoad)
	private void writeFile(InputStream fis, File toLoad) {
		try {
			toLoad.createNewFile();
			
			if(!toLoad.exists())
				toLoad.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(toLoad);
			
			if(!toLoad.exists())
				toLoad.createNewFile();
			
			int i = 0;
			byte[] buffer = new byte[1024];
			
			while((i = fis.read(buffer)) != -1)
				fos.write(buffer, 0, i);
			
			fis.close();
			fos.close();
			
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Error: Could not Load config data into the config file.");
			e.printStackTrace();
		}
	}	
	public SCSettings getPlugin() { return plugin; }
	public PlayerManager getPlayerManager() { return playerManager; }
	public ItemManager getItemManager() { return itemManager; }
	
}
