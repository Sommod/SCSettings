package com.soulcraft.Commands;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Contains a variety of information that can be used
 * for handling each command.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public abstract class CommandManger {

	private Player player;
	private ConsoleCommandSender console;
	private CommandSender sender;
	
	private SCSettingsManager manager;
	private String command;
	private String[] args;
	
	public CommandManger(CommandSender sender, String command, String[] args, SCSettingsManager manager) {
		this.sender = sender;
		this.manager = manager;
		this.args = args;
		this.command = command;
		
		if(sender instanceof Player)
			player = (Player) sender;
		else
			console = (ConsoleCommandSender) sender;
		
		execute();
	}
	
	/**
	 * Gets the player who sent the command.
	 * @return Player
	 */
	protected Player getPlayer() { return player; }
	
	/**
	 * Gets the Console.
	 * @return ConsoleCommandSender
	 */
	protected ConsoleCommandSender getConsole() { return console; }
	
	/**
	 * Gets the standard CommandSender object
	 * @return CommandSender
	 */
	protected CommandSender getCommandSender() { return sender; }
	
	/**
	 * Gets the Manager of the plugin.
	 * @return SCSettingsManager
	 */
	protected SCSettingsManager getManager() { return manager; }
	
	/**
	 * Gets the issued command
	 * @return String
	 */
	protected String getCommand() { return command; }
	
	/**
	 * Gets the args of the command
	 * @return String Array
	 */
	protected String[] getArgs() { return args; }
	
	/**
	 * Checks if the command issuer was a player
	 * @return True - if player sent command
	 */
	protected boolean isPlayer() { return player != null; }
	
	/**
	 * Checks if the command was issued by the console
	 * @return True - if CommandSender is console.
	 */
	protected boolean isConsole() { return console != null; }
	
	/**
	 * Gets the YamlConfiguration of the plugin.
	 * @return YamlConfiguration
	 */
	protected YamlConfiguration getConfig() { return YamlConfiguration.loadConfiguration(new File(manager.getPlugin().getDataFolder(), "config.yml")); }
	
	/**
	 * This method is ran on the creation of this class each time.
	 * While it is not necessary to use this method, it is
	 * suggested considering it is ran already.
	 */
	public void execute() { }

}
