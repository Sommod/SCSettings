package com.soulcraft.GUI;

import java.io.File;

import org.bukkit.entity.Player;

import com.soulcraft.Data.SCSettingsManager;

/**
 * Contains the GUI information and items that were un-claimed by
 * both the gifter and gifted after a period of time. Only people with
 * the given permission(s) may access this storage to either take, give or
 * delete items from this storage.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class AdminStorage extends AbstractMenu {

	public AdminStorage(SCSettingsManager manager, String name, File configFile) {
		super(manager, name, configFile);
	}

	@Override
	public void open(Player player) {
	}

}
