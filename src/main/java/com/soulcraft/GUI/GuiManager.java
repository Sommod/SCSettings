package com.soulcraft.GUI;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.soulcraft.Data.SCSettingsManager;

/**
 * This contains all the Gui Menus.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class GuiManager {

	private Map<Class<?>, AbstractMenu> menus;
	private Map<OfflinePlayer, OfflinePlayer> players;
	private SCSettingsManager manager;
	
	public GuiManager(SCSettingsManager manager) {
		this.manager = manager;
		players = new HashMap<OfflinePlayer, OfflinePlayer>();
		init();
	}
	
	private void init() {
		menus = new HashMap<Class<?>, AbstractMenu>();
		
		menus.put(MainMenu.class, new MainMenu(manager));
		menus.put(FriendsMenu.class, new FriendsMenu(manager));
		menus.put(FriendRequestMenu.class, new FriendRequestMenu(manager));
		menus.put(AddFriend.class, new AddFriend(manager));
		menus.put(GiftMenu.class, new GiftMenu(manager));
		menus.put(SettingsMenu.class, new SettingsMenu(manager));
		menus.put(AdminStorage.class, new AdminStorage(manager));
		menus.put(ItemCollectionMenu.class, new ItemCollectionMenu(manager));
	}
	
	public MainMenu getMainMenu() { return getMenu(MainMenu.class); }
	public FriendsMenu getFriendsMenu() { return getMenu(FriendsMenu.class); }
	public FriendRequestMenu getFriendRequestMenu() { return getMenu(FriendRequestMenu.class); }
	public AddFriend getAddFriend() { return (AddFriend) getMenu(AddFriend.class); }
	public GiftMenu getGiftMenu() { return getMenu(GiftMenu.class); }
	public SettingsMenu getSettingsMenu() { return getMenu(SettingsMenu.class); }
	public AdminStorage getAdminStorage() { return getMenu(AdminStorage.class); }
	public ItemCollectionMenu getItemCollectionMenu() { return getMenu(ItemCollectionMenu.class); }
	
	@SuppressWarnings("unchecked")
	protected <T extends AbstractMenu> T getMenu(Class<T> clazz) { return (T) menus.get(clazz); }
	
	/**
	 * Checks if the given tile is of one of the menus.
	 * @param titleCheck
	 * @return True - if a menu is the same
	 */
	public boolean isMenu(String titleCheck) {
		for(AbstractMenu am : menus.values()) {
			if(am.isTitle(titleCheck))
				return true;
		}

		return false;
	}
	
	/**
	 * Gets the super class of the given menu.
	 * @param title - Title of Menu
	 * @return AbstractMenu
	 */
	public AbstractMenu getMenu(String title) {
		for(AbstractMenu am : menus.values()) {
			if(am.isTitle(title))
				return am;
		}
		
		return null;
	}
	
	/**
	 * Gets the Person who is receiving a gift based on the 
	 * gifter. If the given person is not gifing any items,
	 * this will normally return null.
	 * @param player - Gifter
	 * @return Receiver
	 */
	public OfflinePlayer getReceiver(Player player) { return players.get(player); }
	
	/**
	 * Adds the data to connect to players when sending items to players.
	 * This should not be used manually as this could cause errors. This
	 * method is used via the event handling.
	 * @param player - Gifter
	 * @param target - Receiver
	 */
	@Deprecated
	public void giftMenu(Player player, OfflinePlayer target) { players.put(player, target); }
	
	/**
	 * Removes the data that connects the gifter to the receiver
	 * of the gift. This is unnecessary to use manually and may
	 * cause errors otherwise. This is used by the event handling.
	 * @param gfter - Person sending gift
	 */
	@Deprecated
	public void removeRecevier(OfflinePlayer gifter) { players.remove(gifter); }
}
