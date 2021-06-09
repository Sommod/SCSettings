package com.soulcraft.Commands;

import org.bukkit.command.CommandSender;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Player.PlayerData;

/**
 * Handles the command Settings
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class Settings extends CommandManger {

	public Settings(CommandSender sender, String command, String[] args, SCSettingsManager manager) {
		super(sender, command, args, manager);
	}
	
	@Override
	public void execute() {
		if(isPlayer() && !getPlayer().hasPermission(Perms.USE.toString())) {
			noPermisison();
			return;
		} else if(isConsole()) {
			getConsole().sendMessage("§cConsole cannot perform this command.");
			return;
		} else if(getArgs().length != 2) {
			getPlayer().sendMessage("§cError: The number of arguments must be two...");
			return;
		}
		
		PlayerData pd = getManager().getPlayerManager().getPlayerData(getPlayer());
		
		switch (getArgs()[1].toLowerCase()) {
		case "chat":
		case "ignore":
		case "c":
		case "i":
		case "ignorechat":
		case "ic":
			pd.getChatSettings().setIgnoreGlobalChat(!pd.getChatSettings().isIgnoringGlobalChat());
			break;
			
		case "deathmessages":
		case "deathmessage":
		case "dm":
		case "death":
		case "messages":
		case "d":
		case "m":
			pd.getChatSettings().setDeathMessages(!pd.getChatSettings().isSeeingDeathMessages());
			break;
			
		case "friendjoin":
		case "friends":
		case "friend":
		case "fj":
		case "join":
		case "j":
			pd.getChatSettings().setNotifyFriendJoin(!pd.getChatSettings().isNotifiedFriendJoin());
			break;

		case "friendrequest":
		case "fq":
		case "request":
		case "requests":
		case "r":
			pd.getChatSettings().setAllowFriendRequets(!pd.getChatSettings().isAllowingFriendRequest());
			break;
			
		default:
			getPlayer().sendMessage("§cError, there is not setting option by that tag. To view a list of changable settings,"
					+ " §ctype §b/SCSettings List Settings§c.");
			return;
		}
		
		getPlayer().sendMessage("§aSurvival Settings Updated!");
	}

}
