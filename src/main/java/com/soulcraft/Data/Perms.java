package com.soulcraft.Data;

import org.bukkit.permissions.Permission;

/**
 * Contains the String values of all the plugin's permissions.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public enum Perms {
	
	/** <b>scsettings.*</b> */OP("*"),
	/** <b>scsettings.use</b> */USE("use"),
	/** <b>scsettings.ignorechat</b> */IGNORE_CHAT("ignorechat"),
	/** <b>scsettings.override</b> */OVERRIDE("override"),
	/** <b>scsettings.storage</b> */STORAGE("storage");
	
	private Permission permission;
	
	private Perms(String value) {
		permission = new Permission("scsettings." + value);
	}
	
	@Override
	public String toString() { return permission.getName(); }
	
	public Permission asPermission() { return permission; }
}
