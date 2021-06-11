package com.soulcraft.Commands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;

import net.milkbowl.vault.economy.Economy;

/**
 * Handles the command to create a banknote containing a selection
 * amount of money that can be used to send as a gift to a friend.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class BankNote extends CommandManger {

	public BankNote(CommandSender sender, Command command, String[] args, SCSettingsManager manager) {
		super(sender, command, args, manager);
	}
	
	@Override
	public void execute() {
		Economy eco = getManager().getPlugin().getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		
		if(eco == null) {
			getManager().getPlugin().getServer().getConsoleSender().sendMessage("§cError, Vault is not installed, cannot handle BankNotes.");
			if(isPlayer())
				getPlayer().sendMessage("§cAn Error occurred, could not get BankNote.");
			return;
		} else if(isConsole()) {
			getConsole().sendMessage("§cError, console cannot perform this command.");
			return;
		} else if(!getPlayer().hasPermission(Perms.USE.toString())) {
			noPermisison();
			return;
		} else if(getArgs().length != 2) {
			getPlayer().sendMessage("§cError, invalid amount of arguments.");
			return;
		}
		
		float value = -1F;
		
		try {
			value = Float.parseFloat(getArgs()[1]);
		} catch (NumberFormatException e) {
			getPlayer().sendMessage("§cError, you must put in a number value...");
			return;
		}
		
		if(value < 1) {
			getPlayer().sendMessage("§cYou must have a value of at least $1 to get a BankNote.");
			return;
		} else if(eco.getBalance(getPlayer()) < value) {
			getPlayer().sendMessage("§cError, you do not have enoug money to obtain that much money.");
			return;
		}
		
		ItemStack paper = new ItemStack(Material.PAPER);
		ItemMeta meta = paper.getItemMeta();
		
		meta.setDisplayName("§b§lBankNote");
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 1213, true);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS);
		meta.setLore(Arrays.asList("§7This banknote is used for sending money to friends","§7whether they are online of offline.","§6Balance: §a$" + value));
		paper.setItemMeta(meta);
		
		if(getPlayer().getInventory().firstEmpty() == -1) {
			getPlayer().sendMessage("§cCannot give BankNote. Need at least one empty slot to give BankNote.");
			return;
		}
		
		eco.withdrawPlayer(getPlayer(), value);
		getPlayer().getInventory().addItem(paper);
		getPlayer().sendMessage("§aBankNote obtained.");
	}

}
