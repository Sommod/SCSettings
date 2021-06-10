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
		
		int loc = 0;
		
		switch (getArgs()[1].toLowerCase()) {
		case "settings":
		case "setting":
		case "s":
		case "set":
		case "sets":
			break;
			
		case "friends":
		case "friend":
		case "f":
			loc = 1;
			break;
			
		case "requests":
		case "request":
		case "r":
		case "friendrequest":
		case "friendsrequests":
			loc = 2;
			break;

		default:
			getPlayer().sendMessage("§cError, given sub-command is not supported for the §blist§c command.");
			return;
		}
		
		if(loc == 1)
			settings();
		else if(loc == 2)
			friends();
		else
			requests();
		
	}
	
	// Shows all the individuals that have sent a friend request
	private void requests() {
		getPlayer().sendMessage("§c§m----§f Friend Requests §c§m----");
		getManager().getPlayerManager().getPlayerData(getPlayer())
		.getAllFriendRequests().forEach(off -> { getPlayer().sendMessage(off.getName()); } );
		getPlayer().sendMessage("§c§m-------------------------");
	}
	
	// Lists the settings that are currently available to be changed.
	private void settings() {
		getPlayer().sendMessage("§c§m----§f Settings §c§m----");
		getPlayer().sendMessage("§bTogglable Settings:"
				+ "\n§6Chat, join, ignore, death.");
		getPlayer().sendMessage("§c§m------------------");
	}
	
	// Lists all friends that the player has
	private void friends() {
		getPlayer().sendMessage("§c§m----§f Friends §c§m----");
		getManager().getPlayerManager().getPlayerData(getPlayer())
		.getAllFriends().forEach(off -> { getPlayer().sendMessage(off.getName()); });
		getPlayer().sendMessage("§c§m-----------------");
	}

}
