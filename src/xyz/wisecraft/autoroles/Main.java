package xyz.wisecraft.autoroles;


import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import com.earth2me.essentials.IEssentials;

import net.luckperms.api.LuckPerms;
import xyz.wisecraft.autoroles.data.DataMethods;
import xyz.wisecraft.autoroles.data.Infop;
import xyz.wisecraft.autoroles.data.Playerdata;
import xyz.wisecraft.autoroles.data.Timers;
import xyz.wisecraft.autoroles.listeners.QuestEvents;
import xyz.wisecraft.autoroles.listeners.ZimListeners;
import xyz.wisecraft.autoroles.threads.autosave;
import xyz.wisecraft.autoroles.threads.joined;


public class Main extends JavaPlugin{
	
	// Useful variables, the infop HashMap is current player's data. This is to store the data in RAM
	public ConcurrentHashMap<UUID, Infop> infom = new ConcurrentHashMap<UUID, Infop>(); 
	public static ConcurrentHashMap<UUID, Timers> timers = new ConcurrentHashMap<UUID, Timers>(); 
    public ConsoleCommandSender console = getServer().getConsoleSender();
    public static LuckPerms luck;
    public static IEssentials ess;
    
    
	//On server start
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new ZimListeners(), this);
		this.getServer().getPluginManager().registerEvents(new QuestEvents(), this);
		this.getCommand("autoroles").setExecutor(new AutorolesCommand());
		this.getCommand("autoroles").setTabCompleter(new TabCompletion());
		IEssentials ess = (IEssentials) this.getServer().getPluginManager().getPlugin("Essentials");
		Main.ess = ess;
		
		
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
		    LuckPerms api = provider.getProvider();
		    Main.luck = api;
		}
		
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
		UUID UUID = p.getUniqueId();
		File file = Playerdata.getFile(UUID.toString());
		String name = p.getName();
		BukkitRunnable r1 = new joined(name, UUID, file);
		r1.runTaskAsynchronously(Main.getPlugin(Main.class));
				}
			}
		}.runTask(Main.getPlugin(Main.class));
		
		//Auto Save Players' configs. 
		new autosave(Bukkit.getConsoleSender()).runTaskTimer(Main.getPlugin(Main.class), 12000, 12000);
		
		//Countdowns
		new BukkitRunnable() {
			public void run() {
				
				for (Entry<UUID, Timers> values : timers.entrySet()) {
					
					Timers timers = values.getValue();
					
					if (timers.getFly() > 0) {
						
						int value = timers.getFly()-1;
						timers.setFly(value);
						//Bukkit.getPlayer(values.getKey()).sendMessage(Integer.toString(value));
					}
					if (timers.getTree() > 0) {
						
						int value = timers.getTree()-1;
						timers.setTree(value);
						//Bukkit.getPlayer(values.getKey()).sendMessage(Integer.toString(value));
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 20, 20);
		
		
		//Increase time (in minutes) by 1. 
		new BukkitRunnable() {
			public void run() {
				
				Map<UUID, Infop> copy = new ConcurrentHashMap<UUID, Infop>(infom);
				
		    	for (Entry<UUID, Infop> entry : copy.entrySet()) {
		    		
		    		UUID UUID = entry.getKey();
		    		if (!ess.getUser(UUID).isAfk()) {
		    	
		    			Infop data = entry.getValue();
		    			int time = 1 + data.getTime();
		    			data.setTime(time);
		    			
		    		}
		    	}
		    }
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 1200, 1200);
	
		// Give criterias
		new BukkitRunnable() {
			public void run() {
				NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "citizen");
				Advancement a = Bukkit.getAdvancement(key);
				
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					
					AdvancementProgress prog = p.getAdvancementProgress(a);
					Infop data = infom.get(p.getUniqueId());
					
					if (!prog.isDone() && (data.getTime() >= 120)) 
						prog.awardCriteria("citizen");
					
				}
			}
		}.runTaskTimer(Main.getPlugin(Main.class), 36000, 36000);
	
	}
	
	
	//On server close
	@Override
	public void onDisable() { 
    	
    	for (Entry<UUID, Infop> entry : infom.entrySet()) {
    		try {
    		UUID UUID = entry.getKey();
    		String uuid = UUID.toString();
    		File file = Playerdata.getFile(uuid);
    		Object[] data = DataMethods.convert(entry.getValue());
    		Playerdata.set(uuid, file, data);
            Playerdata.saveConfig(file);
    		}
    	catch (Exception e) {
    		this.console.sendMessage("couldn't save data due to: " + e);
    	}
        }
    	
    	}
}

