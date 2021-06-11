package com.soulcraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Handler for each sub-command that is issued. All
 * data checking and handling is done within the command
 * class, so only checking for the command is found here.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class Commands implements CommandExecutor {

	private SCSettingsManager manager;
	
	public Commands(SCSettingsManager manager) {
		this.manager = manager;
		manager.getPlugin().getCommand("SCSettings").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("SCSettings")) {
			if(args.length == 0) {
				new Version(sender, command, args, manager);
			} else {
				switch (args[0].toLowerCase()) {
				case "help":
				case "h":
					new Help(sender, command, args, manager);
					break;
					
				case "settings":
				case "set":
					new Settings(sender, command, args, manager);
					break;
					
				case "list":
				case "l":
					new List(sender, command, args, manager);
					break;
					
				case "send":
				case "s":
					new SendGift(sender, command, args, manager);
					break;
					
				case "acceptgift":
				case "accept":
				case "ag":
					new AcceptGifts(sender, command, args, manager);
					break;
					
				case "declinegift":
				case "decline":
				case "dg":
					new DeclineGift(sender, command, args, manager);
					break;
					
				case "add":
				case "a":
					new AddFriend(sender, command, args, manager);
					break;
					
				case "remove":
				case "r":
					new RemoveFriend(sender, command, args, manager);
					break;
					
				case "money":
				case "cash":
				case "note":
				case "m":
				case "c":
				case "n":
					new BankNote(sender, command, args, manager);
					break;
					
				case "storage":
				case "str":
					new AdminStorage(sender, command, args, manager);
					break;
					
				case "reload":
				case "rl":
					new Reload(sender, command, args, manager);
					break;
					
				default:
					sender.sendMessage("§cError, no command by that argument.");
					break;
				}
			}
			
			return true;
		}
		
		return false;
	}

}
