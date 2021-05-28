package soulcraft.GUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cold.fyre.API.Managers.FileManager;
import soulcraft.Data.Manager;
import soulcraft.Items.ItemData;

/**
 * Contains the ItemDatas of items that were
 * not collected in time from both the gifter
 * and gifted. This also handles the GUI creation
 * of the inventory and displaying each ItemData
 * into the GUI. Finally, this also handles its
 * own saving of data to the File within the
 * plugin data folder.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class AdminStorage {

	private List<ItemData> items;
	private Map<Player, Integer> page;
	private Manager manager;
	
	/**
	 * Creates a new Manager of the Admin Storage. This stores
	 * any item that was not claimed by either the gifter or the
	 * receiver and is now stored here. Only those with access
	 * can either give back an item. They also have the ability
	 * to take the item into their own inventory and delete the
	 * item entirely.
	 * @param manager - Plugin Manager
	 */
	public AdminStorage(Manager manager) {
		this.manager = manager;
		page = new HashMap<Player, Integer>();
		initData();
	}
	
	// Obtains the data from the save files
	// Or creates a new ArrayList if no data
	// is found.
	@SuppressWarnings("unchecked")
	private void initData() {
		File file = new File(manager.getPlugin().getDataFolder(), "Data/Backup Admin Storage Save.yml");
		
		if(file.exists()) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			// ItemData
			for(String ItemDataUID : config.getConfigurationSection("").getKeys(false)) {
				String path = ItemDataUID + ".";
				
				ItemStack item = ItemStack.deserialize((Map<String, Object>) config.get(path + "item"));
				UUID senderUID = UUID.fromString(path + "senderuid");
				UUID friendUID = UUID.fromString(path + "frienduid");
				long expireTime = config.getLong(path + "expiretime");
				ItemData id = new ItemData(Bukkit.getServer().getOfflinePlayer(senderUID), Bukkit.getServer().getOfflinePlayer(friendUID), item, expireTime, new ArrayList<String>(), manager);
				items.add(id);
			}
			
			file.delete();
			
		// Object Serialization
		} else {
			file = new File(manager.getPlugin().getDataFolder(), "Data/Admin Storage Save.sc");
			
			if(!file.exists()) {
				items = new ArrayList<ItemData>();
				return;
			}
			
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				items = (List<ItemData>) ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				FileManager.logExceptionToFile(manager.getPlugin(), e);
			}
		}
	}
	
	/**
	 * Adds a new ItemData to the Admin Storage
	 * @param item - ItemData to add
	 */
	public void addItemData(ItemData item) { items.add(item); }
	
	/**
	 * Removes an ItemData from the Admin Storage. This is used
	 * when the administrator returns/deletes the item and when
	 * the timer runs out and the plugin removes the item.
	 * @param id - UUID of ItemData
	 */
	public void removeItemData(UUID id) { removeItemData(getItem(id)); }
	
	/**
	 * Removes an ItemData from the Admin Storage. This is used
	 * when the administrator returns/deletes the item and when
	 * the timer runs out and the plugin removes the item.
	 * @param item - ItemData to remove
	 */
	public void removeItemData(ItemData item) { removeItemData(item); }
	
	/**
	 * Obtains the list of all the items within the Admin Storage
	 * @return List{@code <ItemData>}
	 */
	public List<ItemData> getAllItems() { return items; }
	
	/**
	 * Gets a specific ItemData from the Admin Storage based
	 * on the UUID of the ItemData. If the UUID does not pertain
	 * to any of the ItemDatas, then a NULL value is returned.
	 * @param id - UUID of ItemData
	 * @return ItemData - if found, otherwise NULL.
	 */
	public ItemData getItem(UUID id) {
		for(ItemData item : items) {
			if(item.getID().equals(id))
				return item;
		}
		
		return null;
	}
	
	/**
	 * Opens this Admin menu with the access to scroll from
	 * page to page and see all items stored as well as the
	 * ability to collect/return items.
	 * @param player - player to open menu
	 */
	public void openMenu(Player player) {
		player.closeInventory();
		Inventory menu = Bukkit.getServer().createInventory(null, 54, "&c&lAdmin Storage");
		page.put(player, 0);
		
		for(int i = 0; i < 45 && i < items.size(); i++)
			menu.setItem(i, items.get(i).getDisplayItem());
		
		player.openInventory(menu);		
		fillInventory(player);
	}
	
	/**
	 * Sets whether to increase or decrease the page view.
	 * If the player is trying to go to the next page, then
	 * this will imply a TRUE value, otherwise this will go
	 * back a page (if not already at base page).<br>
	 * Note: This requires that {@link #openMenu(Player)} has
	 * been ran first!
	 * @param player - Player to change
	 * @param increase - True for next page
	 */
	public void setPage(Player player, boolean increase) {
		player.getOpenInventory().getTopInventory().clear();
		page.put(player, increase ? page.get(player) + 1 : page.get(player) - 1);
		
		for(int i = (page.get(player) * 45), k = 0; k < 45 && i < items.size(); k++, i++)
			player.getOpenInventory().getTopInventory().setItem(k, items.get(i).getDisplayItem());
		
		fillInventory(player);
	}
	
	// Fills the empty slots of the inventory when
	// the inventory has been loaded onto the players screen.
	private void fillInventory(Player player) {
		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta im = back.getItemMeta();
		
		im.setDisplayName("§6Previous Page");
		back.setItemMeta(im);
		
		ItemStack refresh = new ItemStack(Material.NETHER_STAR);
		im = refresh.getItemMeta();
		
		im.setDisplayName("§aRefresh");
		refresh.setItemMeta(im);
		
		ItemStack forward = new ItemStack(Material.ARROW);
		im = forward.getItemMeta();
		
		im.setDisplayName("§6Next Page");
		forward.setItemMeta(im);
		
		player.getOpenInventory().getTopInventory().setItem(48, back);
		player.getOpenInventory().getTopInventory().setItem(49, refresh);
		player.getOpenInventory().getTopInventory().setItem(50, forward);
		
		if(manager.getConfiguration().getBoolean("gui.fill.use")) {
			ItemStack fillItem = new ItemStack(Material.getMaterial(manager.getConfiguration().getString("gui.fill.item")), manager.getConfiguration().getInt("gui.fill.amount"));
			im = fillItem.getItemMeta();
			
			im.setDisplayName(manager.getConfiguration().getString("gui.fill.name").replace('&', '§'));
			im.setLore(manager.getConfiguration().getStringList("gui.fill.lore"));
			
			if(manager.getConfiguration().getBoolean("gui.fill.enchant")) {
				im.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
				im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			fillItem.setItemMeta(im);
			
			for(int i = 0; i < 54; i++) {
				if(player.getOpenInventory().getTopInventory().getItem(i) == null)
					player.getOpenInventory().getTopInventory().setItem(i, fillItem);
			}
		}
	}
	
	/**
	 * This is to remove a player from the map that pertains
	 * to the page the player is viewing on the Admin Storage.
	 * This is usually called on either changing/closing
	 * of inventory or leaving of server.
	 * @param player - Player to remove
	 */
	public void removeFromPage(Player player) { page.remove(player); }
}
