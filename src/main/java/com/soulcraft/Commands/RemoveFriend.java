package com.soulcraft.Commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Player.PlayerData;

/**
 * Handles both the action to remove a friend from
 * the friend list as well as the action to decline
 * a friend request.
 *
 * @author Armeriness
 * @author Sommod
 * @version 1.0
 *
 */
public class RemoveFriend extends CommandManger {

	public RemoveFriend(CommandSender sender, String command, String[] args, SCSettingsManager manager) {
		super(sender, command, args, manager);
	}
	
	@Override
	public void execute() {
		if(isConsole()) {
			getConsole().sendMessage("§cError, Console cannot perform this action.");
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
		}
		
		PlayerData data = getManager().getPlayerManager().getPlayerData(getPlayer());
		
		if(!data.isFriend(target) || !data.isFriendRequest(target)) {
			getPlayer().sendMessage("§Player is not a friend or has sent a friend request.");
			return;
		}
		
		if(data.isFriend(target)) {
			data.removeFriend(target);
			getManager().getPlayerManager().getPlayerData(target).removeFriend(getPlayer());
			getPlayer().sendMessage("§aPlayer has been removed as a friend.");
		} else {
			data.removeFriendRequest(target);
			getPlayer().sendMessage("§aYou have decline the friend request of §b" + target.getName() + "§a.");
		}
	}

}
