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
 * Holds the information about each player.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class PlayerData implements Serializable {

	/**
	 *  Generated Serial ID
	 */
	private static final long serialVersionUID = -3317063853979062437L;
	
	private ChatSettings chatSettings;
	private transient List<OfflinePlayer> friends;
	private transient List<OfflinePlayer> friendRequests;
	private transient OfflinePlayer player;

	/**
	 * Creates a new PlayerData object for the given player.
	 * This also creates a ChatSettings object for the given
	 * player.
	 * @param player - Player to store info for
	 */
	public PlayerData(OfflinePlayer player) {
		this.player = player;
		chatSettings = new ChatSettings();
		friends = new ArrayList<OfflinePlayer>();
		friendRequests = new ArrayList<OfflinePlayer>();
		
	}
	
	/**
	 * Gets the Chat Settings for the given player.
	 * @return ChatSettings
	 */
	public ChatSettings getChatSettings() { return chatSettings; }
	
	/**
	 * Gets the OfflinePlayer object that is for this player data.
	 * @return OfflinePlayer of this PlayerData
	 */
	public OfflinePlayer getPlayer() { return player; }
	
	/**
	 * Gets the friend object based on the name of the given
	 * friend.
	 * @param name - NAme of player
	 * @return Friend - Otherwise null
	 */
	public OfflinePlayer getFriend(String name) {
		for(OfflinePlayer off : friends) {
			if(name.equalsIgnoreCase(off.getName()))
				return off;
		}
		
		 return null;
	}
	
	/**
	 * Gets the friend object based on the UUID of the given
	 * friend.
	 * @param id - UUID of player
	 * @return Friend - Otherwise null
	 */
	public OfflinePlayer getFriend(UUID id) {
		for(OfflinePlayer off : friends) {
			if(id.equals(off.getUniqueId()))
				return off;
		}
		
		 return null;
	}
	
	/**
	 * Checks if the given player is a friend.
	 * @param player - Player to check
	 * @return True - if player is friend
	 */
	public boolean isFriend(OfflinePlayer player) { return friends.contains(player); }
	
	/**
	 * Checks if the given player is a friend.
	 * @param name - Name of Player
	 * @return True - if player is a friend
	 */
	public boolean isFriend(String name) {
		for(OfflinePlayer off : friends) {
			if(off.getName().equalsIgnoreCase(name))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Adds a new player as a friend. If the player is
	 * already a friend, then nothing changes.
	 * @param player - player to add
	 */
	public void addFriend(OfflinePlayer player) {
		if(!isFriend(player))
			friends.add(player);
	}
	
	/**
	 * Removes the given player from the friends list.
	 * @param player - Player to remove.
	 */
	public void removeFriend(OfflinePlayer player) { friends.remove(player); }
	
	/**
	 * Gets a list of all friends of this player.
	 * @return List of friends
	 */
	public List<OfflinePlayer> getAllFriends() { return friends; }
	
	/**
	 * Clears the entire friends list.
	 */
	public void clearFriends() { friends.clear(); }
	
	/**
	 * Adds the given player to the list of individuals that have
	 * requested to be friends with this person.
	 * @param player - Player attempting to be friends
	 */
	public void addFriendRequest(OfflinePlayer player) {
		if(!friendRequests.contains(player))
			friendRequests.add(player);
	}
	
	/**
	 * Removes the given player from the list of people
	 * attempting to be a friend.
	 * @param player - Player to remove
	 */
	public void removeFriendRequest(OfflinePlayer player) { friendRequests.remove(player); }
	
	/**
	 * Checks if the player is on the list of people wanting
	 * to be a friend.
	 * @param player - Player to check
	 * @return True - if player is attempting to be friend
	 */
	public boolean isFriendRequest(OfflinePlayer player) { return friendRequests.contains(player); }
	
	/**
	 * Clears the list of friend requests.
	 */
	public void clearFriendRequests() { friendRequests.clear(); }
	
	/**
	 * Gets the entire list of friend requests.
	 * @return List of friend requests
	 */
	public List<OfflinePlayer> getAllFriendRequests() { return friendRequests; }
	
	// Custom Write Serialization for PlayerData
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		List<UUID> ids = new ArrayList<UUID>();
		
		friends.forEach(off -> { ids.add(off.getUniqueId()); });
		oos.writeObject(ids);
		ids.clear();
		
		friendRequests.forEach(off -> { ids.add(off.getUniqueId()); });
		oos.writeObject(ids);
		
		oos.writeObject(player.getUniqueId());
	}
	
	// Custom Read Serialization for PlayerData
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		friends = new ArrayList<OfflinePlayer>();
		friendRequests = new ArrayList<OfflinePlayer>();
		((List<UUID>) ois.readObject()).forEach(uuid -> { friends.add(Bukkit.getServer().getOfflinePlayer(uuid)); });
		((List<UUID>) ois.readObject()).forEach(uuid -> { friendRequests.add(Bukkit.getServer().getOfflinePlayer(uuid)); });
		player = Bukkit.getServer().getOfflinePlayer((UUID) ois.readObject());
	}
}
