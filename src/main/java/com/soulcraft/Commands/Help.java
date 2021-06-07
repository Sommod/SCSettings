package com.soulcraft.Commands;

import org.bukkit.command.CommandSender;

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
			getPlayer().sendMessage("§c§m----§f Help §c§m----");
			getPlayer().sendMessage(getHelpDisplay());
			getPlayer().sendMessage("§c§m--------------");
		} else {
			//TODO: Handle Help message Type
		}
	}
	
	private String[] getHelpDisplay() {
		if(isPlayer()) {
			// TODO: check permissions, get command(s)
		}
		
		return new String[0];
	}

}
