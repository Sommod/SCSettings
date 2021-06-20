package com.soulcraft.Event;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.soulcraft.Data.SCSettingsManager;
import com.soulcraft.Event.InventoryHandlers.AddFriendHandler;
import com.soulcraft.Event.InventoryHandlers.AdminStorageHandler;
import com.soulcraft.Event.InventoryHandlers.BankNoteHandler;
import com.soulcraft.Event.InventoryHandlers.FriendRequestHandler;
import com.soulcraft.Event.InventoryHandlers.FriendsMenuHandler;
import com.soulcraft.Event.InventoryHandlers.GiftMenuHandler;
import com.soulcraft.Event.InventoryHandlers.ItemCollectHandler;
import com.soulcraft.Event.InventoryHandlers.MainMenuHandler;
import com.soulcraft.Event.InventoryHandlers.SettingsMenuHandler;
import com.soulcraft.GUI.AbstractMenu;
import com.soulcraft.GUI.AddFriend;
import com.soulcraft.GUI.AdminStorage;
import com.soulcraft.GUI.BankNoteMenu;
import com.soulcraft.GUI.FriendRequestMenu;
import com.soulcraft.GUI.FriendsMenu;
import com.soulcraft.GUI.GiftMenu;
import com.soulcraft.GUI.ItemCollectionMenu;
import com.soulcraft.GUI.MainMenu;
import com.soulcraft.GUI.SettingsMenu;

/**
 * Registers all Events that this plugin uses as
 * well as used to unregister all events when
 * this plugin is disabled.
 *
 * @author Armeriness
 * @author Sommod
 * @version 1.0
 *
 */
public class EventsHandler implements Listener {
	
	private SCSettingsManager manager;
	
	/**
	 * Registers the events that are handled within this
	 * plugin.
	 * @param manager
	 */
	public EventsHandler(SCSettingsManager manager) {
		manager.getPlugin().getServer().getPluginManager().registerEvents(this, manager.getPlugin());
		this.manager = manager;
	}
	
	/**
	 * Unregisters all events that are within this class.
	 * Requires the Manager class of the plugin to disable.
	 * @param manager - Manager of plugin
	 */
	public void unRegisterEvents(SCSettingsManager manager) { HandlerList.unregisterAll(manager.getPlugin()); }
	
	@EventHandler
	public void onClickEvent(InventoryClickEvent event) {
		String title = event.getWhoClicked().getOpenInventory().getTitle();
		
		if(manager.getGuiManager().isMenu(title))
			convert(event, manager.getGuiManager().getMenu(title));
	}
	
	@EventHandler
	public void banknoteCollect(PlayerInteractEvent event) {
		if(event.getPlayer().getInventory().getItemInMainHand() != null && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAPER) {
			ItemStack note = event.getPlayer().getInventory().getItemInMainHand();
			ItemMeta meta = note.getItemMeta();
			
			if(meta.hasLore()) {
				if(meta.getLore().get(meta.getLore().size() - 1).equals("§0§kTIaUctETtiiAAbN")) {
					double amount = Double.parseDouble(meta.getDisplayName().substring(5).replaceAll(",", ""));
					manager.getEconomy().depositPlayer(event.getPlayer(), amount);
					event.getPlayer().sendMessage("§a$" + amount + "§b has been deposited into your account!");
					event.getPlayer().getInventory().setItemInMainHand(null);
				}
			}
		}
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		if(!manager.getPlayerManager().exists(event.getPlayer()))
			manager.getPlayerManager().createNewPlayerData(event.getPlayer());
	}
	
	// Creates the correct corresponding class the handles
	// the given menu.
	private void convert(InventoryClickEvent event, AbstractMenu menu) {
		if(menu instanceof AddFriend)
			new AddFriendHandler(event, manager);
		else if(menu instanceof AdminStorage)
			new AdminStorageHandler(event, manager);
		else if(menu instanceof BankNoteMenu)
			new BankNoteHandler(event, manager);
		else if(menu instanceof FriendRequestMenu)
			new FriendRequestHandler(event, manager);
		else if(menu instanceof FriendsMenu)
			new FriendsMenuHandler(event, manager);
		else if(menu instanceof GiftMenu)
			new GiftMenuHandler(event, manager);
		else if(menu instanceof ItemCollectionMenu)
			new ItemCollectHandler(event, manager);
		else if(menu instanceof MainMenu)
			new MainMenuHandler(event, manager);
		else if(menu instanceof SettingsMenu)
			new SettingsMenuHandler(event, manager);
	}

}
