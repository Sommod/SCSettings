package com.soulcraft.Data;

/**
 * This creates and removes the timer that is used
 * for managing items. Items themselves will continue
 * to count down and show how much time is left, but will
 * not update once the time has hit 0. This updates each
 * of the items every (1) second (20 ticks) and checks
 * updates the items location.
 *
 * @author Sommod
 * @version 1.0
 *
 */
public class Updater {

	private SCSettingsManager manager;
	private int task;
	
	public Updater(SCSettingsManager manager) {
		this.manager = manager;
	}
	
	protected void initTimer() {
		task = manager.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(manager.getPlugin(), () -> {
			manager.getItemManager().clean();
		}, 0L, 20L);
	}
	
	/**
	 * Attempts to cancel the given task. This is accessible through
	 * the Manager class of this plugin.
	 */
	protected void cancelTimer() { manager.getPlugin().getServer().getScheduler().cancelTask(task); }
	
	/**
	 * Checks if the task is currently running on the server.
	 * @return True - if currently running
	 */
	public boolean isRunning() { return manager.getPlugin().getServer().getScheduler().isCurrentlyRunning(task); }
	
	/**
	 * Checks if the current task has been cancelled. More
	 * specifically, this checks that the task is not running
	 * @return True - if Cancelled
	 */
	public boolean isCancelled() { return !isRunning(); }

}
