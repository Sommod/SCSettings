package com.soulcraft.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Items.ItemData;

/**
 * Used for collecting any gifts that the player may have as well
 * as any items that were not accepted by the occupant that received
 * the item.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class AcceptGifts extends CommandManger {

	public AcceptGifts(CommandSender sender, Command command, String[] args, SCSettingsManager manager) {
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
		
		int emptySlots = 0;
		
		for(int i = 0; i < 36; i++) {
			if(getPlayer().getInventory().getItem(i) == null)
				emptySlots++;
		}
		
		if(emptySlots == 0) {
			getPlayer().sendMessage("§cYou cannot accept any gifts... empty some space in your inventory first.");
			return;
		}
		
		List<ItemData> itemsToCollect = getManager().getItemManager().getStorage(getPlayer());
		
		if(itemsToCollect.isEmpty()) {
			getPlayer().sendMessage("§6Nothing to Collect...");
			return;
		}
		
		int k = 0;
		
		for(int i = 0; i < 36 && k < itemsToCollect.size(); i++) {
			if(getPlayer().getInventory().getItem(i) != null)
				continue;
			
			getPlayer().getInventory().setItem(i, itemsToCollect.get(k).getItem());
			k++;
		}
		
		if(k != 0) {
			k--;
			
			for(; k > -1; k--)
				getManager().getItemManager().removeItemData(itemsToCollect.get(k).getID());
		}
		
		if(itemsToCollect.isEmpty())
			getPlayer().sendMessage("§aAll Items Collected!");
		else
			getPlayer().sendMessage("§aItems Collected. Ran out of Inventory Space; items still left to collect.");
	}

}
