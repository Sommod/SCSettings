package com.soulcraft.Commands;

import org.bukkit.command.CommandSender;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;

/**
 * Handles the command for listing both settings
 * that can be edited and the friends on a persons
 * friends list.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class List extends CommandManger {

	public List(CommandSender sender, String command, String[] args, SCSettingsManager manager) {
		super(sender, command, args, manager);
	}
	
	@Override
	public void execute() {
		if(isPlayer() && !getPlayer().hasPermission(Perms.USE.toString())) {
			noPermisison();
			return;
		} else if(isConsole()) {
			getConsole().sendMessage("§cError, console cannot perform this command.");
			return;
		} else if(getArgs().length != 2) {
			getPlayer().sendMessage("§cError, invalid amount of arguments given.");
			return;
		}
		
		boolean settings = false;
		
		switch (getArgs()[1].toLowerCase()) {
		case "settings":
		case "setting":
		case "s":
		case "set":
		case "sets":
			settings = true;
			break;
			
		case "friends":
		case "friend":
		case "f":
			break;

		default:
			getPlayer().sendMessage("§cError, given sub-command is not supported for the §blist§c command.");
			return;
		}
		
		if(settings)
			settings();
		else
			friends();
		
	}
	
	private void settings() {
		getPlayer().sendMessage("§c§m----§f Settings §c§m----");
		getPlayer().sendMessage("§bTogglable Settings:"
				+ "\n§6Chat, join, ignore, death.");
		getPlayer().sendMessage("§c§m------------------");
	}
	
	private void friends() {
		getManager().getPlayerManager().getPlayerData(getPlayer())
		.getAllFriends().forEach(off -> { getPlayer().sendMessage(off.getName()); });
	}

}
