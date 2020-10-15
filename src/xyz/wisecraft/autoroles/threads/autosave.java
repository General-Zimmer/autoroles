package xyz.wisecraft.autoroles.threads;

import java.io.File;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.wisecraft.autoroles.Main;
import xyz.wisecraft.autoroles.data.DataMethods;
import xyz.wisecraft.autoroles.data.Infop;
import xyz.wisecraft.autoroles.data.Playerdata;

public class autosave extends BukkitRunnable {

	private static Main plugin = Main.getPlugin(Main.class);

	
	private CommandSender sender;
	
	public autosave(CommandSender sender) {
		this.setSender(sender);
	}
	
	@Override
	public void run() {
    	try {
    	for (Entry<UUID, Infop> entry : plugin.infom.entrySet()) {
    		UUID UUID = entry.getKey();
    		String uuid = UUID.toString();
    		File file = Playerdata.getFile(uuid);
    		Object[] data = DataMethods.convert(entry.getValue());
    		Playerdata.set(uuid, file, data);
            Playerdata.saveConfig(file);
            
    		}
    	if (sender instanceof Player) {
    		plugin.console.sendMessage("Manuel: Data saved by " + sender.getName());
    		sender.sendMessage(ChatColor.GREEN + "Data has been saved");
    	}

    	}
        	catch (Exception e) {
        		plugin.console.sendMessage("couldn't save data due to: " + e);
        }
		
	}


	public CommandSender getSender() {
		return sender;
	}


	public void setSender(CommandSender sender) {
		this.sender = sender;
	}
	

}
