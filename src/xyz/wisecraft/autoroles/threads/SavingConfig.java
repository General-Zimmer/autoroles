package xyz.wisecraft.autoroles.threads;

import java.io.File;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.wisecraft.autoroles.data.Playerdata;

public class SavingConfig extends BukkitRunnable {

	private File file;
	
	public SavingConfig (File file) {
		this.setFile(file);
	}
	
	@Override
	public void run() {
		Playerdata.saveConfig(file);
	}

	public File getFile() {
		return file;}
	public void setFile(File file) {
		this.file = file;}
}
