package com.soulcraftserver.scsettings.Items;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.soulcraftserver.scsettings.Data.Manager;

/**
 * Holds the item and any information
 * about the item.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class ItemData implements Serializable {

	private static final long serialVersionUID = 4710493833451462654L;
	
	private UUID senderUID, friendUID, id;
	private transient ItemStack item;
	private List<String> format;
	private long expireTime;
	private boolean loreLoc;
	private String timeFormat;
	
	/**
	 * Creates a new class to hold and handle either the item or money.
	 * @param sender - Gifter
	 * @param friend - Person receiving gift
	 * @param item - Item to send
	 * @param format - Format of information to put into lore
	 */
	public ItemData(OfflinePlayer sender, OfflinePlayer friend, ItemStack item, long expireTime, List<String> format, Manager manager) {
		senderUID = sender.getUniqueId();
		friendUID = friend.getUniqueId();
		this.format = format;
		id = UUID.randomUUID();
		this.item = item;
		this.expireTime = expireTime;
		loreLoc = manager.getConfiguration().getBoolean("gui.display above");
		timeFormat = manager.getConfiguration().getString("gui.time format");
		
		if(this.item == null)
			throw new NullPointerException("Error, Player: " + sender.getName() + " caused an error to make a NULL item.");
	}
	
	/**
	 * Obtains a copy of the item with an adjust
	 * that contains the information of the item
	 * in the lore 
	 * @return ItemStack (Adjusted Lore)
	 */
	public ItemStack getDisplayItem() {
		ItemStack toReturn = item.clone();
		ItemMeta im = toReturn.getItemMeta();
		long timeLeft = expireTime - System.currentTimeMillis();
		
		// Gets the time left for the lore.
		long days = timeLeft / (1000 * 60 * 60 * 24);
		long hours = timeLeft / ((1000 * 60 * 60) % 24);
		long mins = timeLeft / ((1000 * 60) % 60);
		long secs = (timeLeft / 1000) % 60;
		String time = formatTime(days, hours, mins, secs);
		
		// Implements the data into the holder values
		// of the format.
		List<String> displayFormat = new ArrayList<String>();
		for(String line : format) {
			if(line.contains("%sender%"))
				line.replaceAll("%sender%", Bukkit.getServer().getOfflinePlayer(senderUID).getName());
			
			if(line.contains("%receiver%"))
				line.replaceAll("%friend%", Bukkit.getServer().getOfflinePlayer(senderUID).getName());
			
			line.replaceAll("%time%", time);
			displayFormat.add(line);
		}
		
		List<String> imLore = im.getLore(); 
		if(loreLoc)
			displayFormat.addAll(imLore);
		else {
			imLore.addAll(displayFormat);
			displayFormat = imLore;
		}
		
		displayFormat.add("�0" + id.toString());
		im.setLore(displayFormat);
		
		return toReturn;
	}
	
	// Formats the time into the corresponding format-type.
	private String formatTime(long days, long hours, long mins, long secs) {
		if(timeFormat.equalsIgnoreCase("0000")) {	
			return (days < 10 ? "0" + days : days) + ":" +
				   (hours < 10 ? "0" + hours : hours) + ":" +
				   (mins < 10 ? "0" + mins : mins) + ":" +
				   (secs < 10 ? "0" + secs : secs);
		} else {
			return days + "D " + hours + "H " + mins +"M " + secs + "s";
		}
	}
	
	/**
	 * Returns the actual item without adjustment of lore.
	 * @return ItemStack
	 */
	public ItemStack getItem() { return item; }
	
	/**
	 * Obtains the UID of the owner of this item.
	 * @return UUID
	 */
	public UUID getSenderUID() { return senderUID; }
	
	/**
	 * Gets the UID of the person receiving the gift.
	 * @return UUID
	 */
	public UUID getFriendUID() { return friendUID; }
	
	/**
	 * Gets the UID of this item class. This is mainly used for
	 * obtaining the correct item when clicked within the GUI
	 * system.
	 * @return UID (This class)
	 */
	public UUID getID() { return id; }
	
	/**
	 * Checks if the item has expired, this is used by the automated system
	 * to determine when to move the item into the next location.
	 * @return Long (Time of expiration)
	 */
	public boolean isExpired() { return expireTime - System.currentTimeMillis() <= 0; }
	
	/**
	 * Gets the Long value of the time in which the item will expire
	 * from its given location (gifted, gifter, Admin storage)
	 * @return Long
	 */
	public long getExpireTime() { return expireTime; }
	
	/**
	 * Sets the time in which the item will 'expire'; more specifically,
	 * establishes when the item will transfer to who can claim the item.
	 * (or removed if in the Admin Storage).
	 * @param time
	 */
	public void setExpireTime(long time) { expireTime = time; }
	
	/**
	 * Sets the format the item will have. These will appear
	 * on the items lore with information about the item.
	 * @param format - String List
	 */
	public void setFormat(List<String> format) { this.format = format; }
	
	/**
	 * Updates the format location when a change occurs within the config.yml
	 * @param manager - Manager class
	 */
	public void update(Manager manager) {
		loreLoc = manager.getConfiguration().getBoolean("gui.display above");
		timeFormat = manager.getConfiguration().getString("gui.time format");
	}
	
	// Custom Serialization for ItemStack.
	// This is necessary as the class itself is
	// not serializable and is serialized in
	// the SerialItem class found within bukkit.
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		item = ItemStack.deserialize((Map<String, Object>) ois.readObject());
	}
	
	// Custom Serialization for ItemStack.
	// This is necessary as the class itself is
	// not serializable and is serialized in
	// the SerialItem class found within bukkit.
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		Map<String, Object> serializedItemStack = item.serialize();
		oos.writeObject(serializedItemStack);
	}
}
