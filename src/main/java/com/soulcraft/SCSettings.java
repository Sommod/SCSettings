package com.soulcraft;

import org.bukkit.plugin.java.JavaPlugin;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Main class of the plugin. Nothing here is stored expect the PluginManager.
 * All initializations are found within the PluginManager class.<br>
 * <b>Copyright (C) 2020; Sommod</b><br><br>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br><br>
 * 
 * Link to GNU Licenses: 
 * <a href="https://www.gnu.org/licenses/">gnu.org/licenses</a>
 * 
 * @author Sommod
 * @since 1.0
 *
 */
public class SCSettings extends JavaPlugin {
	
	private SCSettingsManager manager;
	
	@Override
	public void onEnable() {
		manager = new SCSettingsManager(this);
	}
	
	@Override
	public void onDisable() {
		if(manager != null)
			manager.onShutdown();
	}

}
