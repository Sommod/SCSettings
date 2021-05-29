package com.soulcraft.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * Contains the information about the players
 * chat settings.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class ChatSettings implements Serializable {
	
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 6068695006569854096L;
	
	private boolean seeDeathMessages;
	private boolean notifyFriendJoin;
	private boolean allowFriendRequests;
	private boolean ignoreGlobalChat;
	private transient List<OfflinePlayer> friendsChat;
	
	/**
	 * Creates a new ChatSettings for the given player. These
	 * settings are saved and stored within the given player's
	 * PlayerData object. By default, Friends chat setting is
	 * reset each time the player logs into the server.
	 */
	public ChatSettings() {
		seeDeathMessages = true;
		notifyFriendJoin = false;
		allowFriendRequests = true;
		ignoreGlobalChat = false;
		friendsChat = new ArrayList<OfflinePlayer>();
	}
	
	/**
	 * Check if the player can see death messages.
	 * @return True - if can see death messages
	 */
	public boolean isSeeingDeathMessages() { return seeDeathMessages; }
	
	/**
	 * Checks if the player is notified when a friend joins
	 * the server.
	 * @return True - if can see friend join notifications
	 */
	public boolean isNotifiedFriendJoin() { return notifyFriendJoin; }
	
	/**
	 * Checks if the given player is accepting friend requests.
	 * @return True - if accept requests
	 */
	public boolean isAllowingFriendRequest() { return allowFriendRequests; }
	
	/**
	 * Checks if the player is ignoring the global chat. Note:
	 * this will be turned off (false) when the player sets
	 * their chat to only view a friend(s) chat messages.
	 * @return True - if ignoring chat
	 */
	public boolean isIgnoringGlobalChat() { return ignoreGlobalChat; }
	
	/**
	 * Checks if the player has set their chat to only view
	 * friends messages.
	 * @param player - Friend to check
	 * @return True - if player has set their chat to player
	 */
	public boolean isViewingFriendchat(OfflinePlayer player) { return friendsChat.contains(player); }
	
	/**
	 * Gets the list of all friends that the player is viewing
	 * chat messages from. Will return an empty list if not
	 * setting chat to view friends only.
	 * @return List{@code <OfflinePlayer>} - List of friends
	 */
	public List<OfflinePlayer> getFriendsChat() { return friendsChat; }
	
	/**
	 * Sets whether the player will see death messages or not.
	 * @param value - True or False
	 */
	public void setDeathMessages(boolean value) { seeDeathMessages = value; }
	
	/**
	 * Sets whether the player will be notified when a friend
	 * joins the server.
	 * @param value - True or False
	 */
	public void setNotifyFriendJoin(boolean value) { notifyFriendJoin = value; }
	
	/**
	 * Sets whether the player can be sent a friend request.
	 * @param value - True or False
	 */
	public void setAllowFriendRequets(boolean value) { allowFriendRequests = value; }
	
	/**
	 * Sets whether the player will ignore the global chat.
	 * @param value - True or False
	 */
	public void setIgnoreGlobalChat(boolean value) { ignoreGlobalChat = value; }
	
	/**
	 * Adds a player to the list of friends chat messages
	 * that will appear in chat.
	 * @param player - Friend to add
	 */
	public void addFriendChat(OfflinePlayer player) {
		if(!isViewingFriendchat(player))
			friendsChat.add(player);
	}
	
	/**
	 * Removes a friend from the list chat messages to display
	 * @param player - Friend to remove
	 */
	public void removeFriendChat(OfflinePlayer player) { friendsChat.remove(player); }
	
	/**
	 * Clears the entire list of friends chat only messages. This
	 * resets the chat to the normal setting for the payer to view
	 * the normal global chat.
	 */
	public void clearFriendChat() { friendsChat.clear(); }
	
	// Custom Write Method
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		List<UUID> ids = new ArrayList<UUID>();
		
		for(OfflinePlayer off : friendsChat)
			ids.add(off.getUniqueId());
		
		oos.writeObject(ids);
	}
	
	// Custom Read Method
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		friendsChat = new ArrayList<OfflinePlayer>();
		
		@SuppressWarnings("unchecked")
		List<UUID> ids = (List<UUID>) ois.readObject();
		
		ids.forEach(uuid -> { friendsChat.add(Bukkit.getServer().getOfflinePlayer(uuid)); });
	}
}
