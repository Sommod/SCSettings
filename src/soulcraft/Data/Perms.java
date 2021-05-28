package soulcraft.Data;

/**
 * Contains a list this plugin's permissions.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public enum Perms {

	/** <b>survival.{@code *}</b> */OP("*"),
	/** <b>survival.use</b> */USE("use"),
	/** <b>survival.ignorechat</b> */IGNORE_CHAT("ignorechat"),
	/** <b>survival.settings</b> */SETTINGS("settings"),
	/** <b>survival.override</b> */OVERRIDE("override"),
	/** <b>survival.storage</b> */STORAGE("storage"),
	/** <b>survival.spy</b> */SPY("spy");
	
	private String perm;
	
	private Perms(String value) { perm = value; }
	
	@Override
	public String toString() { return "survival." + perm; }
	
	
}
