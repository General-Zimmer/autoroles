package xyz.wisecraft.autoroles.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import xyz.wisecraft.autoroles.Main;

public class Playerdata {

	private static Main plugin = Main.getPlugin(Main.class);
	
	static File file = null;
	static FileConfiguration configuration = null;
	static OfflinePlayer player = null;


	/**
	* get the config from file
	* @return file configuration
	*/
	public static FileConfiguration getConfig(File file, String uuid) {
	    
	    
	    file = new File(plugin.getDataFolder() + "/Player_Data", uuid + ".yml");
	    configuration = YamlConfiguration.loadConfiguration(file);
	    
	    
	    return configuration;
	}

	/**
	* this will save the config to the file
	*/
	public static void saveConfig(File file) {
	    try {
	        configuration.save(file);
	    } catch (IOException e) {
	        plugin.console.sendMessage("Cannot save to " + file.getName() + "due to: " + e);
	    }
	    catch (NullPointerException e1) {
	    	plugin.console.sendMessage("Cannot save to " + file.getName() + "due to: " + e1);
	    }
	}

	/**
	* set an object to a certain path
	*/
	public static void set(String uuid, File file, Object[] values) {
	    
		FileConfiguration config = getConfig(file, uuid);
		
		String[] names = {"Name", "BlocksBroke", "BlocksPlace", "DiaBroke", "Time"}; 
    	HashMap<String, Object> data = new HashMap<String, Object>(); //LinkedHashMap an array,  
    	data.put(names[0], values[0]); //insert data with a name,
    	data.put(names[1], values[1]);
    	data.put(names[2], values[2]);
       	data.put(names[3], values[3]);
       	data.put(names[4], values[4]);
		data.forEach((key, value) -> config.set(key, value)); //and iterate over the LinkedHashMap while inserting the content
	}
	/**
	 * This will write into a player's file with the content of an inputStream
	 * 
	 */
    public static void copyInputStreamToFile(InputStream inputStream, File file, String uuid) 
		throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read); 
            }
        }
        catch (IOException e) {
            plugin.console.sendMessage("Cannot write data for " + uuid + " : " + e);
        }
    }
                
            
	
	/**
	* this will get the player's file
	* @return the player's uuid file
	*/
	public static File getFile(String uuid) {
	
	        File file = new File(plugin.getDataFolder() + "/Player_Data", uuid + ".yml");
	        return file;
	    }


	/**                               
	* this will reload the config
	 * @return YamlConfiguration
	*/
	public static YamlConfiguration loadConfig(File file) {
	    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
	    return data;
	}
}