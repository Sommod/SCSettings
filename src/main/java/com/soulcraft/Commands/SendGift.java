package com.soulcraft.Commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Player.PlayerData;

/**
 * Handles the action of sending a gift through
 * the command. Unlike the action done through
 * the GUI system, this requires that the item
 * be in the player's Main Hand to send.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class SendGift extends CommandManger {

	public SendGift(CommandSender sender, Command command, String[] args, SCSettingsManager manager) {
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
		
		ItemStack toSend = getPlayer().getInventory().getItemInMainHand();
		
		if(toSend == null) {
			getPlayer().sendMessage("§cError, you must put something in your Main Hand to send to your friend.");
			return;
		} else if(getManager().getBlacklist().isBlacklisted(toSend)) {
			getPlayer().sendMessage("§cError, that item is not allowed to be sent as a gift.");
			return;
		}

		OfflinePlayer target = null;
		PlayerData data = getManager().getPlayerManager().getPlayerData(getPlayer());
		
		if(!data.isFriend(getArgs()[1])) {
			getPlayer().sendMessage("§cThat person is not a friend and canont send them a gift.");
			return;
		}
		
		target = data.getFriend(getArgs()[1]);
		
		if(target == null) {
			getPlayer().sendMessage("§cAn Error occurred, please contact SoulCraft Administration. Error Code: SG-61");
			return;
		}
		
		getManager().getItemManager().addItem(getPlayer(), target, toSend);
		getPlayer().sendMessage("§aGift has been sent!");
	}

}
