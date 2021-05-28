package com.soulcraftserver.scsettings.Player;

import java.util.UUID;

/**
 * Contains the data on what settings the player has. These settings
 * are reverted back to their defaulted settings each time the player
 * logs into the server.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class Settings {

	private boolean chat;
	private boolean ignore;
	
	private UUID friendChat;
	
	/**
	 * Creates a new settings class to be used for a PlayerData class.
	 */
	public Settings() { reset(); }
	
	/**
	 * Reverts all settings back to their default settings.
	 */
	public void reset() {
		chat = true;
		ignore = false;
		friendChat = null;
	}
	
	/**
	 * Checks if the player is in the main chat system.
	 * @return True - if in main chat
	 */
	public boolean isInMainChat() { return chat; }
	
	/**
	 * Checks if the player is ignoring the main chat.
	 * @return True - if ignoring chat
	 */
	public boolean isIgnoringChat() { return ignore; }
	
	/**
	 * Sets the chat that the player is talking in. This also
	 * stored the UUID of the player that the player is talking
	 * to. If the chat channel is TRUe (main), then the ID is to
	 * be NULL.
	 * @param value - Chat channel.
	 * @param id - UUID of friend, or NULL
	 */
	public void setChatChannel(boolean value, UUID id) {
		chat = value;
		friendChat = id;
		
		if((value && friendChat != null) || (!value && friendChat == null)) {
			chat = true;
			friendChat = null;
		}
	}
	
	/**
	 * Sets whether the player is ignoring the main chat or not.
	 * @param value - True/False
	 */
	public void setIgnoringChat(boolean value) { ignore = value; }
}
