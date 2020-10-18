package xyz.wisecraft.autoroles.threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.luckperms.api.model.group.Group;
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
		if (!config.getString("Name").equals(name)) {
    		config.set("Name", name);
		}
		
		
		// Give old timers their achievement
		if (config.getString("oldtimer").equalsIgnoreCase("new")) {
		new BukkitRunnable() {
			public void run() {
				
				
				
				Player p = Bukkit.getPlayer(UUID);
				Group group = Main.luck.getGroupManager().getGroup(Main.luck.getUserManager().getUser(p.getUniqueId()).getPrimaryGroup());
				if (group.getWeight().getAsInt() >= 2 && p.hasPlayedBefore()) {
					NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "citizen");
					Advancement a = Bukkit.getAdvancement(key);
					AdvancementProgress prog = p.getAdvancementProgress(a);
					NamespacedKey key2 = new NamespacedKey(Main.getPlugin(Main.class), "old_timer");
					Advancement a2 = Bukkit.getAdvancement(key2);
					AdvancementProgress prog2 = p.getAdvancementProgress(a2);
					
					for (String criteria : prog.getRemainingCriteria()) {
						prog.awardCriteria(criteria);
					}
					config.set("oldtimer", "yes");
					prog2.awardCriteria("manual");
					
				}
				else {
					config.set("oldtimer", "no");
				}
				
		   	 	new SavingConfig(file).run();
		   	 	
				// Add joined player to infop ConcurrentHashMap
				Object[] data = {config.getString("Name"), config.getInt("BlocksBroke"), config.getInt("BlocksPlace"), config.getInt("DiaBroke"), config.getInt("Time"), config.getInt("Timber"), config.get("oldtimer")};
				DataMethods.infopPut(UUID, data);
				
			}
		}.runTask(Main.getPlugin(Main.class));
		}
		else {
	   	 	new SavingConfig(file).runTask(plugin);
	   	 	
			// Add joined player to infop ConcurrentHashMap
			Object[] data = {config.getString("Name"), config.getInt("BlocksBroke"), config.getInt("BlocksPlace"), config.getInt("DiaBroke"), config.getInt("Time"), config.getInt("Timber"), config.get("oldtimer")};
			DataMethods.infopPut(UUID, data);
		}
		
		

		


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
