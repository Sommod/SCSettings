package soulcraft.Data;

import java.io.File;

import cold.fyre.API.Managers.PluginManager;
import soulcraft.Friends;
import soulcraft.GUI.AdminStorage;
import soulcraft.Player.PlayerManager;

/** 
 * Main manager of the plugin. This contains all the objects
 * of the main class files of the plugin. All classes are
 * connected through this class. Objects are initialized,
 * files created and other miscellaneous actions are found
 * within this class. However, specific actions pertaining
 * to the server are found within their respetive classes.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class Manager extends PluginManager<Friends> {
	
	private PlayerManager playerManager;
	private AdminStorage adminStorage;

	public Manager(Friends plugin) {
		super(plugin);
	}
	
	@Override
	public void onStartup() {
		logMessage(getHeaderMessage());
		logMessage(getFooterMessage());
	}
	
	@Override
	public void onShutdown() {
		super.onShutdown();
	}
	
	@Override
	public boolean onReload() {
		return super.onReload();
	}
	
	private void regsisterCommands() {
		
	}
	
	private void registerEvents() {
		
	}
	
	private void registerConfigs() {
		createFile(getPlugin().getClass().getResourceAsStream("/Resources/config.yml"), new File(getPlugin().getDataFolder(), "config.yml"));
	}
	
	public PlayerManager getPlayerManager() { return playerManager; }
	public AdminStorage getAdminStorage() { return adminStorage; }

}
