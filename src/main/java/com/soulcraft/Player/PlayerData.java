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
	}
	
	/**
	 * Gets the Chat Settings for the given player.
	 * @return ChatSettings
	 */
	public ChatSettings getChatSettings() { return chatSettings; }
	
	/**
	 * Checks if the given player is a friend.
	 * @param player - Player to check
	 * @return True - if player is friend
	 */
	public boolean isFriend(OfflinePlayer player) { return friends.contains(player); }
	
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
	 * Clears the entire friends list.
	 */
	public void clearFriends() { friends.clear(); }
	
	// Custom Write Serialization for PlayerData
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		List<UUID> ids = new ArrayList<UUID>();
		
		friends.forEach(off -> { ids.add(off.getUniqueId()); });
		oos.writeObject(ids);
		oos.writeObject(player.getUniqueId());
	}
	
	// Custom Read Serialization for PlayerData
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		friends = new ArrayList<OfflinePlayer>();
		((List<UUID>) ois.readObject()).forEach( uuid -> { friends.add(Bukkit.getServer().getOfflinePlayer(uuid)); });
		player = Bukkit.getServer().getOfflinePlayer((UUID) ois.readObject());
	}
}
