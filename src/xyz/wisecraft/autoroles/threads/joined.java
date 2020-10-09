package xyz.wisecraft.autoroles.threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.wisecraft.autoroles.Main;
import xyz.wisecraft.autoroles.data.DataMethods;
import xyz.wisecraft.autoroles.data.Playerdata;

public class joined extends BukkitRunnable {

	private Main plugin = Main.getPlugin(Main.class);

	private File file;
	private UUID UUID;
	private String name;


	public joined (String name, UUID UUID, File file) {
		this.setFile(file);
		this.setUuid(UUID);
		this.setName(name);
	}

	@Override
	public void run() {
		DataMethods.writeCheck(file);
		
		BufferedReader reader = null;
		
		String uuid = UUID.toString();
		
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			boolean empty = reader.readLine() == null;
			if (empty) { 
			     try {
			    	 InputStream resource = plugin.getResource("Data_template.yml"); 
			    	 Playerdata.copyInputStreamToFile(resource, file, uuid);
			    	 
			    	 plugin.console.sendMessage("File for player " + uuid + " has been created");
			    	 plugin.console.sendMessage("Saved as " + uuid + ".yml");
			    	 
			    	 //Change the template's default name into the Player's current name
			    	 FileConfiguration config = Playerdata.getConfig(file, uuid);
			    	 config.set("Name", name);
			    	 
					} catch (IOException e) {
					}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Check if the name in the config is the same as the player's current name, if not, correct it. 
		FileConfiguration config = Playerdata.getConfig(file, uuid); 	
		if (config.getString("Name") != name) {
    		config.set("Name", name);
		}
		
   	 	new SavingConfig(file).runTask(plugin);
   	 	
		// Add joined player to infop ConcurrentHashMap
		Object[] data = {config.getString("Name"), config.getInt("BlocksBroke"), config.getInt("BlocksPlace"), config.getInt("DiaBroke"), config.getInt("Time"), config.getInt("Timber")};
		DataMethods.infopPut(UUID, data);
	}
	
	public File getFile() {
		return file;}
	public void setFile(File file) {
		this.file = file;}
	public UUID getUuid() {
		return UUID;}
	public void setUuid(UUID UUID) {
		this.UUID = UUID;}
	public String getName() {
		return name;}
	public void setName(String name) {
		this.name = name;}

}
