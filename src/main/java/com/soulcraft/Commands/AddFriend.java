package com.soulcraft.Commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;

/**
 * Handles the action of sending a friend request as well
 * as accepting any friend requests.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class AddFriend extends CommandManger {

	public AddFriend(CommandSender sender, Command command, String[] args, SCSettingsManager manager) {
		super(sender, command, args, manager);
	}
	
	@Override
	public void execute() {
		if(isConsole()) {
			getConsole().sendMessage("§cError, console cannot perform this command.");
			return;
		} else if(!getPlayer().hasPermission(Perms.USE.toString())) {
			noPermisison();
			return;
		} else if(getArgs().length != 2) {
			getPlayer().sendMessage("§cError, invalid amount of arguments given.");
			return;
		}
		
		OfflinePlayer target = null;
		
		for(OfflinePlayer off : getManager().getPlugin().getServer().getOfflinePlayers()) {
			if(off.getName().equalsIgnoreCase(getArgs()[1]))
					target = off;
		}
		
		if(target == null) {
			getPlayer().sendMessage("§cError, no player by that name.");
			return;
		} else if(getManager().getPlayerManager().getPlayerData(getPlayer()).isFriend(target)) {
			getPlayer().sendMessage("§cThat player is already a friend");
			return;
		} else if(!getManager().getPlayerManager().getPlayerData(getPlayer()).isFriendRequest(target) &&
				!getManager().getPlayerManager().getPlayerData(target).getChatSettings().isAllowingFriendRequest()) {
			getPlayer().sendMessage("§cPlayer is not accepting any friend requests right now.");
			return;
		}
		
		if(getManager().getPlayerManager().getPlayerData(getPlayer()).isFriendRequest(target)) {
			getManager().getPlayerManager().getPlayerData(getPlayer()).removeFriendRequest(target);
			getManager().getPlayerManager().getPlayerData(getPlayer()).addFriend(target);
			getManager().getPlayerManager().getPlayerData(target).addFriend(getPlayer());
			getPlayer().sendMessage("§b" + target.getName() + "§a has been added to your friends list.");
		} else {
			getManager().getPlayerManager().getPlayerData(target).addFriendRequest(getPlayer());
			getPlayer().sendMessage("§aFriend request has been sent!");
		}
	}

}
