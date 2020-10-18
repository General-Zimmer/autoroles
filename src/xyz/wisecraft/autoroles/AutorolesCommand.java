package xyz.wisecraft.autoroles;

import java.io.File;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import xyz.wisecraft.autoroles.data.DataMethods;
import xyz.wisecraft.autoroles.data.Infop;
import xyz.wisecraft.autoroles.data.Playerdata;
import xyz.wisecraft.autoroles.threads.autosave;

public class AutorolesCommand implements CommandExecutor {
	
	private static Main plugin = Main.getPlugin(Main.class);
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		if (sender.hasPermission("autoroles.manage") && cmd.getName().equalsIgnoreCase("autoroles")) {
			switch(args[0]) {
			case "save":
				
				new autosave(sender).runTask(plugin);
		    	
				
				return true;
			case "load":
				
				for (Entry<UUID, Infop> entry : plugin.infom.entrySet()) {

					UUID UUID = entry.getKey();
					String uuid = UUID.toString();
					File file = Playerdata.getFile(uuid);
					YamlConfiguration dataset = Playerdata.loadConfig(file); 
					
					plugin.infom.remove(UUID);
					
					Object[] data = {dataset.getString("Name"), dataset.getInt("BlocksBroke"), dataset.getInt("BlocksPlace"), dataset.getInt("DiaBroke"), dataset.getInt("Time"), dataset.getInt("Timber"), dataset.getString("oldtimer")};
					plugin.infom.put(UUID, DataMethods.reconvert(data)); 
				}
				plugin.console.sendMessage("Manuel: Data has been loaded by " + sender.getName());
				sender.sendMessage("Data has been loaded");
				return true;
				
			case "rank":
				NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "citizen");
				Advancement a = Bukkit.getAdvancement(key);
				
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					
					AdvancementProgress prog = p.getAdvancementProgress(a);
					Infop data = plugin.infom.get(p.getUniqueId());
					
					if (!prog.isDone() && (data.getTime() >= 120)) 
						prog.awardCriteria("citizen");
					
				}

				
			plugin.console.sendMessage("Manuel: Data has been loaded by " + sender.getName());
			sender.sendMessage("Data has been loaded");
			return true;
			default: 
			return false;
			}
		
		}
		else {return false;}
		
	}
			 
	
}
