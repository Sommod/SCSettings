package com.soulcraft.GUI;

import java.util.HashMap;
import java.util.Map;

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
	private SCSettingsManager manager;
	
	public GuiManager(SCSettingsManager manager) {
		this.manager = manager;
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
	}
	
	public MainMenu getMainMenu() { return getMenu(MainMenu.class); }
	public FriendsMenu getFriendsMenu() { return getMenu(FriendsMenu.class); }
	public FriendRequestMenu getFriendRequestMenu() { return getMenu(FriendRequestMenu.class); }
	public AddFriend getAddFriend() { return (AddFriend) getMenu(AddFriend.class); }
	public GiftMenu getGiftMenu() { return getMenu(GiftMenu.class); }
	public SettingsMenu getSettingsMenu() { return getMenu(SettingsMenu.class); }
	public AdminStorage getAdminStorage() { return getMenu(AdminStorage.class); }
	
	@SuppressWarnings("unchecked")
	protected <T extends AbstractMenu> T getMenu(Class<T> clazz) { return (T) menus.get(clazz); }
}
