package com.soulcraft.Event.InventoryHandlers;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.AbstractHandler;
import com.soulcraft.GUI.BankNoteMenu;

/**
 * Handles the action of getting an item that can be
 * used to send an amount of money to a player, even
 * if they are offline.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class BankNoteHandler extends AbstractHandler<BankNoteMenu> {

	public BankNoteHandler(InventoryClickEvent event, SCSettingsManager manager) {
		super(event, manager, manager.getGuiManager().getBanknoteMenu());
	}
	
	@Override
	public void execute() {
		getEvent().setCancelled(true); // Defaulted to cancel
		
		if(isTopInventory()) {
			if(getEvent().getInventory().getItem(getEvent().getSlot()) == null)
				return;
			
			else if(getEvent().getInventory().getItem(getEvent().getSlot()).getType() == Material.getMaterial(getManager().getFileManager().getConfig("bank").getString("slots." + (getEvent().getSlot() + 1 + ".item")))) {
				ItemStack item = getEvent().getInventory().getItem(getEvent().getSlot());
				double amount = -1D;
				
				try {
					String change = item.getItemMeta().getDisplayName().split("$")[1];
					
					if(change.contains(","))
						change = change.replaceAll(",", "");
					
					amount = Double.parseDouble(change);
					
					if(amount < 0)
						amount *= -1;
				} catch (NumberFormatException e) {
					getPlayer().sendMessage("§cError, could not give amony specified. Please inform SoulCraft Administration. Error Code: BNH-30");
					return;
				}
				
				if(getManager().getEconomy() == null) {
					getPlayer().sendMessage("§cCould not compelete action, no balance information.");
					return;
				} else if(getManager().getEconomy().getBalance(getPlayer()) < amount) {
					getPlayer().sendMessage("§cYou do not have enough money to get that Banknote.");
					return;
				} else if(getPlayer().getInventory().firstEmpty() == -1) {
					getPlayer().sendMessage("§cYou need to have an empty slot to get this banknote.");
					return;
				}
				
				getManager().getEconomy().withdrawPlayer(getPlayer(), amount);
				
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				
				lore.add("§0§kTIaUctETtiiAAbN"); // Contains code in lore to ensure actual banknote.
				
				meta.setDisplayName("§a§l$" + meta.getDisplayName().split("$")[1]);
				meta.setLore(lore);
				
				item.setItemMeta(meta);
				
				getPlayer().getInventory().addItem(item);
			}
		}
	}

}
