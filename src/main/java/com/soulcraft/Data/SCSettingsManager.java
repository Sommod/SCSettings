package com.soulcraft.Data;

import org.bukkit.plugin.RegisteredServiceProvider;

import com.soulcraft.SCSettings;
import com.soulcraft.Commands.Commands;
import com.soulcraft.Event.EventsHandler;
import com.soulcraft.GUI.GuiManager;
import com.soulcraft.Items.ItemManager;
import com.soulcraft.Player.PlayerManager;

import net.milkbowl.vault.economy.Economy;

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
	private GuiManager guiManager;
	private EventsHandler events;
	private Blacklist blacklist;
	private Updater timer;
	
	private Economy economy;
	
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
		blacklist = new Blacklist(this);
		
		plugin.getLogger().info("Configuration complete");
		plugin.getLogger().info("Registering commands...");
		new Commands(this);
		
		plugin.getLogger().info("Configuration Registered!");
		plugin.getLogger().info("Registering Player Data...");
		playerManager = new PlayerManager(this);
		
		plugin.getLogger().info("Player Data Registered!");
		plugin.getLogger().info("Registering Gui..");
		guiManager = new GuiManager(this);
		
		plugin.getLogger().info("Gui Registeted!");
		plugin.getLogger().info("Registering Items...");
		itemManager = new ItemManager(this);
		
		plugin.getLogger().info("Items Registered!");
		plugin.getLogger().info("Regisetering Events...");
		registerEvents();
		
		plugin.getLogger().info("Events Registeted!");
		plugin.getLogger().info("Registering Extra Data...");
		RegisteredServiceProvider<Economy> provider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		economy = provider != null ? provider.getProvider() : null;
		
		for(String f : footer)
		 plugin.getLogger().info(f);
	}
	
	private void registerEvents() {
		timer = new Updater(this);
		timer.initTimer();
		events = new EventsHandler(this);
	}
	
	public void onShutdown() {
		plugin.getLogger().info("Shutting down.. Running saving processess");
		plugin.getLogger().info("Saving Player Data");
		playerManager.saveData();
		
		plugin.getLogger().info("Saving Item Data");
		itemManager.save();
		
		plugin.getLogger().info("Removing Events");
		getTimer().cancelTimer();
		events.unRegisterEvents(this);
		
		plugin.getLogger().info("Save Complete. Shutting down... D:");
	}
	
	/**
	 * Used to reload the entire plugin.
	 */
	public void reload() {
		fileManager.reload();
		playerManager.reload(this);
		itemManager.reload(this);
		guiManager.reload(this);
		blacklist.reload(this);
	}
	
	public SCSettings getPlugin() { return plugin; }
	public PlayerManager getPlayerManager() { return playerManager; }
	public ItemManager getItemManager() { return itemManager; }
	public FileManager getFileManager() { return fileManager; }
	public GuiManager getGuiManager() { return guiManager; }
	public Blacklist getBlacklist() { return blacklist; }
	public Updater getTimer() { return timer; }
	public Economy getEconomy() { return economy; }
	
}
