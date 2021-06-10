package com.soulcraft.Commands;

import org.bukkit.command.CommandSender;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Items.ItemData;

/**
 * Handles the command for accessing and getting items
 * from the Admin Storage.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class AdminStorage extends CommandManger {

	public AdminStorage(CommandSender sender, String command, String[] args, SCSettingsManager manager) {
		super(sender, command, args, manager);
	}
	
	@Override
	public void execute() {
		if(isConsole()) {
			getConsole().sendMessage("§cError, console cannot perform this command.");
			return;
		} else if(!getPlayer().hasPermission(Perms.STORAGE.toString())) {
			noPermisison();
			return;
		}
		
		if(getArgs().length == 1) {
			getPlayer().sendMessage("§c§m----§f Admin Storage §c§m----");
			for(int i = 0; i < getManager().getItemManager().getAdminStorage().size(); i++) {
				ItemData id = getManager().getItemManager().getAdminStorage().get(i);
				getPlayer().sendMessage("§7(§b" + (i + 1) + "§7) §r" + id.getItem().getItemMeta().getDisplayName() + " | §6" + id.getGifter().getName() + " §r| §6" + id.getReceiver().getName());
			}
			getPlayer().sendMessage("§c§m-----------------------");
		} else {
			int collect;
			
			try {
				collect = Integer.parseInt(getArgs()[1]);
			} catch (NumberFormatException e) {
				getPlayer().sendMessage("§cError, you must put a whole number to collect an item.");
				return;
			}
			
			if(collect < 1) {
				getPlayer().sendMessage("§cNumber must be greater than 0.");
				return;
			}
			
			ItemData id = getManager().getItemManager().getAdminStorage().get(collect - 1);

			if(getPlayer().getInventory().firstEmpty() == -1) {
				getPlayer().sendMessage("§c Must have at least (1) empty slot to perform this command.");
				return;
			}
			
			getPlayer().getInventory().addItem(id.getItem());
			getManager().getItemManager().removeItemData(id.getID());
			
			getPlayer().sendMessage("§aItem Collected.");
		}
	}

}
