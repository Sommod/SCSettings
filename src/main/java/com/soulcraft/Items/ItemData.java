package com.soulcraft.Items;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Contains information about the gift item.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class ItemData implements Serializable {

	private static final long serialVersionUID = -5430632964146147939L;
	private UUID id;
	
	private transient ItemStack item;
	private transient OfflinePlayer gifter;
	private transient OfflinePlayer receiver;
	private long expireTime;
	private int location;
	
	/**
	 * Creates a new class that holds the given item.
	 * @param item - Item to store
	 * @param gifter - Person sending gift
	 * @param receiver - Person to get gift
	 * @param expireTime - Time when the timer runs out.
	 */
	public ItemData(ItemStack item, OfflinePlayer gifter, OfflinePlayer receiver, long expireTime) {
		this.item = item;
		this.gifter = gifter;
		this.receiver = receiver;
		this.expireTime = expireTime;
		location = 1;
		id = UUID.randomUUID();
	}
	
	/**
	 * This is not used normally, this handles the backup saving of this data.
	 */
	@Deprecated
	public ItemData(ItemStack item, OfflinePlayer gifter, OfflinePlayer receiver, long expireTime, int location, UUID id) {
		this.item = item;
		this.gifter = gifter;
		this.receiver = receiver;
		this.expireTime = expireTime;
		this.location = location > 0 && location < 4 ? location : 1;
		this.id = id;
	}
	
	/**
	 * Gets the item stored within this class.
	 * @return ItemStack
	 */
	public ItemStack getItem() { return item; }
	
	/**
	 * Gets the edit item that contains the specific lore for the
	 * given inventory it's to appear in.
	 * @return ItemStack (Edited)
	 */
	public ItemStack getDisplayItem() {
		ItemStack display = item.clone();
		ItemMeta meta = display.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		if(location == 1) {
			lore.add("§6Left Click: §aAccept");
			lore.add("§6Right Click: §cDecline");
			lore.add("§6Time Left: §f" + formatTime());
			lore.add("§7------------------------");
			lore.addAll(meta.getLore());
		} else if(location == 2) {
			lore = meta.getLore();
			lore.add("§6Time Left: §f" + formatTime());
		} else {
			lore.add("§6Left Click: §aReturn Item");
			lore.add("§6Right Click: §cDelete Item");
			lore.add("§6Middle Button: §bTake Item");
			lore.add("§6Time Left: §f" + formatTime());
			lore.add("§7--------------------------------");
			lore.addAll(meta.getLore());
		}
		
		lore.add("§0id: §0§k" + id.toString());
		
		meta.setLore(lore);
		display.setItemMeta(meta);
		
		return display;
	}
	
	// Converts the given time left into readable time
	private String formatTime() {
		String time = "";
		long timeLeft = expireTime - System.currentTimeMillis();
		long days = TimeUnit.MILLISECONDS.toDays(timeLeft);
		long hrs = TimeUnit.MILLISECONDS.toHours(timeLeft) % 24;
		long mins = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60;
		long secs = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60;
		
		time = (days < 10 ? "0" + days + ":" : days + ":") +
			   (hrs < 10 ? "0" + hrs + ":" : hrs + ":") +
			   (mins < 10 ? "0" + mins + ":" : mins + ":") +
			   (secs < 10 ? "0" + secs : secs + "");
		
		return time;
	}
	
	/**
	 * Sets the time in which the item will expire from it's given location.
	 * @param time - Time of expire in Long format
	 */
	public void setExpireTime(long time) {
		expireTime = time;
		location++;
	}
	
	/**
	 * Checks if the item is within the storage of the person
	 * receiving the gift.
	 * @return True - if in receiver storage
	 */
	public boolean isInReceiver() { return location == 1; }
	
	/**
	 * Checks if the item is in the storage of the person who
	 * originally sent the gift.
	 * @return True - if returned to sender
	 */
	public boolean isInGifter() { return location == 2; }
	
	/**
	 * Checks if the item has expired from it's given loation.
	 * @return True - if time is past the given time
	 */
	public boolean isExpired() { return System.currentTimeMillis() >= expireTime && expireTime != -1; }
	
	/**
	 * Gets the long value of the time in which the timer
	 * for this item will expire
	 * @return long
	 */
	public long getExpireTime() { return expireTime; }
	
	/**
	 * Gets the person sending the gift.
	 * @return OfflinePlayer
	 */
	public OfflinePlayer getGifter() { return gifter; }
	
	/**
	 * Gets the stored player of the person receiving the gift.
	 * @return OfflinePlayer Object of receiver
	 */
	public OfflinePlayer getReceiver() { return receiver; }
	
	/**
	 * Gets the Unique ID for this ItemData. This is used to
	 * determine what item was clicked on in the menu.
	 * @return UUID
	 */
	public UUID getID() { return id; }
	
	/**
	 * Quick method for checking if the given UUID is the same
	 * as the one stored within this ItemData Class.
	 * @param id - UUID to check
	 * @return True - if UUID are both the same
	 */
	public boolean isItemData(UUID id) { return this.id.equals(id); }
	
	// Custom Serialization
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		
		oos.writeObject(item.serialize());
		oos.writeObject(gifter.getUniqueId());
		oos.writeObject(receiver.getUniqueId());
	}
	
	// Custom Serialization
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		
		item = ItemStack.deserialize((Map<String, Object>) ois.readObject());
		gifter = Bukkit.getServer().getOfflinePlayer((UUID) ois.readObject());
		receiver = Bukkit.getServer().getOfflinePlayer((UUID) ois.readObject());
	}
	
}
