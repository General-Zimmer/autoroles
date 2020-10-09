package xyz.wisecraft.autoroles.threads;

import java.io.File;
import java.util.Map.Entry;
import java.util.UUID;

import xyz.wisecraft.autoroles.Main;
import xyz.wisecraft.autoroles.data.DataMethods;
import xyz.wisecraft.autoroles.data.Infop;
import xyz.wisecraft.autoroles.data.Playerdata;

public class AllPlayerSaving implements Runnable {
	
	private static Main plugin = Main.getPlugin(Main.class);
	
	
	
	@Override
	public void run() {
    	try {
    		
    	for (Entry<UUID, Infop> entry : plugin.infom.entrySet()) {
    		UUID UUID = entry.getKey();
    		String uuid = UUID.toString();
    		File file = Playerdata.getFile(uuid);
    		Object[] data = DataMethods.convert(entry.getValue());
    		Playerdata.set(uuid, file, data);
        	new SavingConfig(file).runTask(plugin);
    		}
    	}
    	catch (Exception e) {
    		plugin.console.sendMessage("couldn't save data due to: " + e);
    	}
	}
}
