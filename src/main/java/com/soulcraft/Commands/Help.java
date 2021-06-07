package com.soulcraft.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.soulcraft.Data.CInfo;
import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;

/**
 * Handles the command <tt>/SCSettings Help</tt>
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class Help extends CommandManger {

	public Help(CommandSender sender, String command, String[] args, SCSettingsManager manager) {
		super(sender, command, args, manager);
	}
	
	@Override
	public void execute() {
		if(isPlayer() && !getPlayer().hasPermission(Perms.USE.toString())) {
			noPermisison();
			return;
		}
		
		if(getArgs().length == 1) {
			getCommandSender().sendMessage("§c§m----§f Help §c§m----");
			getCommandSender().sendMessage(getHelpDisplay());
			getCommandSender().sendMessage("§c§m--------------");
		} else {
			CInfo getter = getInfo();
			
			getCommandSender().sendMessage(getter.getHeaderFooter()[0]);
			getCommandSender().sendMessage(getter.getMessage());
			getCommandSender().sendMessage(getter.getHeaderFooter()[1]);
		}
	}
	
	// Obtains the list of commands.
	private String[] getHelpDisplay() {
		List<String> collector = new ArrayList<String>();
		
		collector.add("§6/SCSettings: §fDisplays plugin information.");
		collector.add("§6/SCSettings Help [Command]: §fShows list of commands, or command information.");
		collector.add("§6/SCSettings Settings <Setting>: §fToggles the given setting value.");
		collector.add("§6/SCSettings List Settings: §fLists all possible toggleable settings.");
		collector.add("§6/SCSettings List Friends: §fLists all friends.");
		collector.add("§6/SCSettings Send <Friend>: §fSends the item in-hand to your friend.");
		collector.add("§6/SCSettings AcceptGift: §fAccepts all rewards until all are collected, or inventory is full.");
		collector.add("§6/SCSettings DeclineGift: §fDeclines all gifts");
		collector.add("§6/SCSettings Add <player>: §fSends a friend request to the given player.");
		collector.add("§6/SCSettings Remove <friend>: §fRemoves the person as a friend.");
		collector.add("§6/SCSettings money <amount>:§f obtains a BankNote of the given amount.");
		
		if(isPlayer()) {
			if(getPlayer().hasPermission(Perms.STORAGE.toString()) || getPlayer().isOp())
				collector.add("§6/SCSettings storage");
			if(getPlayer().hasPermission(Perms.RELOAD.toString()) || getPlayer().isOp())
				collector.add("§6/SCSettings Reload: §fReload the plugin and any data.");
			
		} else {
			collector.add("§6/SCSettings storage");
			collector.add("§6/SCSettings Reload: §fReload the plugin and any data.");
		}
		
		String[] toReturn = new String[collector.size()];
		
		for(int i = 0; i < collector.size(); i++)
			toReturn[i] = collector.get(i);
		
		return toReturn;
	}
	
	private CInfo getInfo() {
		switch (getArgs()[1].toLowerCase()) {
			case "scsettings":
			case "survival":
			case "surv":
				return CInfo.SCSETTINGS;
				
			case "help":
			case "h":
				return CInfo.HELP;
				
			case "settings":
			case "set":
				return CInfo.SETTINGS;
				
			case "list":
			case "l":
				return CInfo.LIST;
				
			case "send":
			case "s":
				return CInfo.SEND;
				
			case "acceptgift":
			case "accept":
			case "ag":
				return CInfo.ACCEPT_REWARD;
				
			case "declinegift":
			case "decline":
			case "dg":
				return CInfo.DECLINE_REWARD;
				
			case "add":
			case "a":
				return CInfo.ADD;
				
			case "remove":
			case "r":
				return CInfo.REMOVE;
			
			case "money":
			case "cash":
			case "note":
			case "m":
				return CInfo.MONEY;
				
			case "storage":
			case "str":
				return CInfo.STORAGE;
				
			case "reload":
			case "rl":
				return CInfo.RELOAD;
	
			default:
				return CInfo.ERROR;
		}
	}

}
