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

	private Map<String, AbstractMenu> menus;
	private SCSettingsManager manager;
	
	public GuiManager(SCSettingsManager manager) {
		this.manager = manager;
		init();
	}
	
	private void init() {
		menus = new HashMap<String, AbstractMenu>();
		//TODO: Initialize Menus
		//TODO: Put into Map
		//TODO: Create Getter for Menu
	}
}
