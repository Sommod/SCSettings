package com.soulcraft.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Items.ItemData;

/**
 * Handles the action of decline gifts that are sent. This will ignore
 * any items that are returned due to the person not accepting the item.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class DeclineGift extends CommandManger {

	public DeclineGift(CommandSender sender, Command command, String[] args, SCSettingsManager manager) {
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
		}
		
		List<ItemData> items = getManager().getItemManager().getStorage(getPlayer());
		List<ItemData> toRemove = new ArrayList<ItemData>();
		
		if(items.size() == 0) {
			getPlayer().sendMessage("§cNothing to decline...");
			return;
		}
		
		for(ItemData id : items) {
			if(!id.getGifter().equals(getPlayer()))
				toRemove.add(id);
		}
		
		if(toRemove.size() == 0) {
			getPlayer().sendMessage("§cNothing to decline...");
			return;
		}
		
		for(ItemData id : toRemove)
			getManager().getItemManager().updateItem(id);
		
		getPlayer().sendMessage("§aGifts have been decline and sent back to the sender.");
	}

}
