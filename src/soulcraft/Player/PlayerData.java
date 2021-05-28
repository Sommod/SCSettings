package soulcraft.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import soulcraft.Items.ItemData;

/**
 * Contains the data about the player. This includes the
 * any items that were returned from being gifts and the
 * list of players that are friends.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class PlayerData implements Serializable {

	private static final long serialVersionUID = 6575110455181839290L;
	private UUID playerID;
	
	private List<UUID> friends;
	private Map<UUID, Calendar> dates;
	private List<ItemData> itemsToCollect;

	private Settings settings;
	
	/**
	 * Creates a new class holder for the given payer.
	 * Note that if two classes are created with the same
	 * player, errors may occur. Ensure checking for player
	 * data in the PlayerManager class.
	 * @param player - player to store
	 */
	public PlayerData(OfflinePlayer player) {
		playerID = player.getUniqueId();
		friends = new ArrayList<UUID>();
		itemsToCollect = new ArrayList<ItemData>();
		settings = new Settings();
	}
	
	/**
	 * Obtains this players data UUID.
	 * @return UUID
	 */
	public UUID getID() { return playerID; }
	
	/**
	 * Adds a player to the friends list. If the player is already
	 * a friend, then nothing changes.
	 * @param player - Player to add as friend.
	 */
	public void addFriend(OfflinePlayer player) {
		if(!isFriend(player)) {
			friends.add(player.getUniqueId());
			Calendar c = Calendar.getInstance();
			dates.put(playerID, c);
		}
	}
	
	/**
	 * Removes the given player from the players friend list.
	 * If the player is not a friend, then nothing changes.
	 * @param player - Player to remove as friend
	 */
	public void removeFriend(OfflinePlayer player) {
		if(isFriend(player)) {
			friends.remove(player.getUniqueId());
			dates.remove(player.getUniqueId());
		}
	}
	
	/**
	 * Gets the list of UUID of friends.
	 * @return List{@code <UUID>} - Friends
	 */
	public List<UUID> getFriends() { return friends; }
	
	/**
	 * Checks if the given player is a friend.
	 * @param player - Player to check
	 * @return True - if friend
	 */
	public boolean isFriend(OfflinePlayer player) {
		if(friends.isEmpty()) return false;
		
		for(UUID fID : friends) {
			if(player.getUniqueId().equals(fID))
				return true;
		}
		
		 return false;
	}
	
	/**
	 * Obtains the entire MAP object of the dates and
	 * friends.
	 * @return Map{@code <UUID, Date>}
	 */
	public Map<UUID, Calendar> getAllDates() { return dates; }
	
	/**
	 * Gets the Date of when this player and the given friend
	 * became friends.
	 * @param friendID - UUID of friend
	 * @return Calendar
	 */
	public Calendar getDate(UUID friendID) { return dates.get(friendID); }
	
	/**
	 * Gets the Date of when this player and the given friend
	 * became friends.
	 * @param player - Friend
	 * @return Calendar
	 */
	public Calendar getDate(OfflinePlayer player) { return getDate(player.getUniqueId()); }
	
	/**
	 * Checks if this player data is of the given player.
	 * @param player - Player to check
	 * @return True -  if Player data for player
	 */
	public boolean isPlayer(OfflinePlayer player) { return playerID.equals(player.getUniqueId()); }
	/**
	 * Checks if this player has any items to collect, wether a gift
	 * or an expired gift.
	 * @return True - if item to collect
	 */
	public boolean hasItemsToCollect() { return !itemsToCollect.isEmpty(); }
	
	/**
	 * Obtains the ItemData attached to the given UUID. Note that if
	 * no ItemData is attached to this UUId, then a null object will
	 * return.
	 * @param id - UUID of ItemData
	 * @return - ItemData if found, otherwise NULL.
	 */
	public ItemData getItemData(UUID id) {
		for(ItemData itemData : itemsToCollect) {
			if(itemData.getID().equals(id))
				return itemData;
		}
		return null;
	}
	
	/**
	 * Adds an ItemData to the list of stored datas. If the item
	 * is already within the list, then the item is not added.
	 * @param itemData - ItemData to add
	 */
	public void addItem(ItemData itemData) {
		if(getItemData(itemData.getID()) == null)
			itemsToCollect.add(itemData);
	}
	
	/**
	 * Removes the item from the list. Make sure to get the item
	 * before removing it, if it is going to another player or
	 * the admin storage.
	 * @param itemData - Remove ItemData of class
	 */
	public void removeItem(ItemData itemData) { itemsToCollect.remove(itemData); }
	
	/**
	 * Removes the item from the list. Make sure to get the item
	 * before removing it, if it is going to another player or
	 * the admin storage.
	 * @param id - UUID of ItemData
	 */
	public void removeItem(UUID id) { itemsToCollect.remove(getItemData(id)); }
	
	/**
	 * Grabs the list of ItemData's that are stored within this class.
	 * @return List{@code <ItemData>}
	 */
	public List<ItemData> getAllItems() { return itemsToCollect; }
	
	/**
	 * Sets this players settings. 
	 * @param settings - Settings class
	 */
	public void setSettings(Settings settings) {this.settings = settings; }
	
	/**
	 * Obtains this players Settings class and values within.
	 * @return Settings
	 */
	public Settings getSettings() { return settings; }
}
