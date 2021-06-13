package com.soulcraft.Data;

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
		
		plugin.getLogger().info("Registering Configuration...");
		fileManager = new FileManager(this);
		
		plugin.getLogger().info("Configuration complete");
		plugin.getLogger().info("Registering commands...");
		new Commands(this);
		
		plugin.getLogger().info("Configuration Registered!");
		plugin.getLogger().info("Regisetering Events...");
		registerEvents();
		
		plugin.getLogger().info("Eventes Registeted!");
		plugin.getLogger().info("Registering Player Data...");
		playerManager = new PlayerManager(this);
		
		plugin.getLogger().info("Player Data Registered!");
		plugin.getLogger().info("Registering Gui..");
		//TODO: Initialize Gui Manager
		
		plugin.getLogger().info("Gui Registeted!");
		
		for(String f : footer)
		 plugin.getLogger().info(f);
	}
	
	private void registerEvents() {
		timer = new Updater(this);
		timer.initTimer();
	}
	
	/**
	 * Used to reload the entire plugin.
	 */
	public void reload() {
		
	}
	
	public SCSettings getPlugin() { return plugin; }
	public PlayerManager getPlayerManager() { return playerManager; }
	public ItemManager getItemManager() { return itemManager; }
	public FileManager getFileManager() { return fileManager; }
	public Blacklist getBlacklist() { return blacklist; }
	public Updater getTimer() { return timer; }
	
}
