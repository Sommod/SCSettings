package com.soulcraft.Data;

/**
 * Contains the information about each command.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public enum CInfo {
	
	SCSETTINGS("SCSettings", "§cSCSurvival §7[§cSurvival §7| §cSurv §7| §cs§7]§f: Main command that handles the survival settings."),
	HELP("Help", "§cHelp §7[§ch§7]§f: Lists all commands or shows the information page of the gien command."),
	SETTINGS("Settings", "§cSettings §7[§cset§7]§f: Used to change chat settings. You can view a list of settings to change by using the command §b/SCSurvival List Settings§f."),
	LIST("List", "§cist §7[§cl§7]§f: Lists either all possible settings to change, or lists all friends you currently have."),
	SEND("Send", "§cSend §7[§c§7]§f: Sends a gift to your friend. To send an item, you must have something in your hand to send."),
	ACCEPT_REWARD("AcceptReward", "§cAcceptReward §7[§caccept §7| §car§7]§f: Accepts a gift from your friend or accepts all the rewards that you've been gifted."),
	DECLINE_REWARD("DeclineReward", "§cDeclineReward §7[§cdecline §7| §cd§7]§f: Declines the gift your friend sent you, or you can decline all gifts any friend has sent you."),
	ADD("Add", "§cAdd §7[§ca§7]§f: Sends a friend request to the person."),
	REMOVE("Remove", "§cRemove §7[§cr§7]§f: Removes a friend from your list."),
	MONEY("Money", "§cMoney §7[§ccash §7| §cnote §7| §cm§7]§f: Obtains a Banknote with the specified amount of money. This can be used to store money physically and used to "
			+ "§fsend money to you friends."),
	STORAGE("Storage", "§cStorage §7[§cstr§7]§f: Obtain items in the Admin Storage containing any unclaimed items."),
	RELOAD("Reload", "§cReload §7[§crl§7]§f: Reloads the plugin and data.");
	
	private String name;
	private String message;
	
	private CInfo(String name, String message) {
		this.name = name;
		this.message = message;
	}
	
	/**
	 * Gets the command that is used.
	 * @return String command
	 */
	public String getCommandName() { return name; }
	
	/**
	 * Gets the info message of the command
	 * @return String message
	 */
	public String getMessage() { return message; }
	
	/**
	 * Gets the Header and Footer of the command information.
	 * @return String Array
	 */
	public String[] getHeaderFooter() { return convert(); }
	
	// Gets the Header and footer of the command.
	@SuppressWarnings("unused")
	private String[] convert() {
		String toReturn[] = new String[2];
		toReturn[0] = "§c§m----§f Help: " + name + " §c§m----";
		toReturn[1] = "§c§m------------";
		
		for(char c : name.toCharArray())
			toReturn[1] += "-";
		
		return toReturn;
	}

}
