package com.soulcraft.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import com.soulcraft.SCSettings;
import com.soulcraft.Commands.Commands;
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
	private FileManager fileManager;
	private Blacklist blacklist;
	private Commands commands;
	private Updater timer;
	
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
		
		plugin.getLogger().info("Configuration complete");
		plugin.getLogger().info("Registering commands...");
		registerCommands();
		
		
		plugin.getLogger().info("Configuration Registered!");
		
		//TODO: Input details about what is initialized.
		
		for(String f : footer)
		 plugin.getLogger().info(f);
	}
	
	private void registerCommands() { this.commands = new Commands(this); }
	
	private void registerEvents() {
		timer = new Updater(this);
		timer.initTimer();
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
	}
	
	/**
	 * Used to reload the entire plugin.
	 */
	public void reload() {
		
	}
	
	public SCSettings getPlugin() { return plugin; }
	public PlayerManager getPlayerManager() { return playerManager; }
	public ItemManager getItemManager() { return itemManager; }
	public Blacklist getBlacklist() { return blacklist; }
	public Updater getTimer() { return timer; }
	
}
