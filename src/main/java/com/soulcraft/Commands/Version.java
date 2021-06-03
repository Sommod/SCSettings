package com.soulcraft.Commands;

import org.bukkit.command.CommandSender;

import com.soulcraft.Data.Perms;
import com.soulcraft.Data.SCSettingsManager;

/**
 * Handles the Version information.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class Version extends CommandManger {

	public Version(CommandSender sender, String command, String[] args, SCSettingsManager manager) {
		super(sender, command, args, manager);
	}
	
	@Override
	public void execute() {
		if(isPlayer() && !getPlayer().hasPermission(Perms.USE.toString())) {
			getPlayer().sendMessage(getConfig().getString("friends.no permission"));
			return;
		}
		
		getCommandSender().sendMessage("§c----------------");
		getCommandSender().sendMessage("§6Name: §7Survival Settings");
		getCommandSender().sendMessage("§6Authors: §7SoulCraft Development Team");
		getCommandSender().sendMessage("§6Version: §7" + getManager().getPlugin().getDescription().getVersion());
		getCommandSender().sendMessage("§c----------------");
	}

}
