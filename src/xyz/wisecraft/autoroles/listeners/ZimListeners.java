package xyz.wisecraft.autoroles.listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.wisecraft.autoroles.Main;
import xyz.wisecraft.autoroles.data.DataMethods;
import xyz.wisecraft.autoroles.data.Playerdata;
import xyz.wisecraft.autoroles.threads.SavingConfig;
import xyz.wisecraft.autoroles.threads.joined;


public class ZimListeners implements Listener {
	
	private Main plugin = Main.getPlugin(Main.class);
	
	// setup people for data collection
	@EventHandler
	public void onJoin(PlayerJoinEvent join) {
		// Variables
		Player p = join.getPlayer();
		UUID UUID = p.getUniqueId();
		File file = Playerdata.getFile(UUID.toString());
		String name = p.getName();
		BukkitRunnable r1 = new joined(name, UUID, file);
		r1.runTaskAsynchronously(plugin);
		}
	
	
	@EventHandler
	public void onLeave(PlayerQuitEvent left) {
		// variables
		Player p = left.getPlayer();
		UUID UUID = p.getUniqueId();
		String uuid = UUID.toString();
		File file = Playerdata.getFile(uuid);
		
		// Remove left player from the infop ConcurrentHashMap async
		new BukkitRunnable() {
			//Async code
			@Override
			public void run() {
				Playerdata.set(uuid, file, DataMethods.convert(plugin.infom.get(UUID)));
				plugin.infom.remove(UUID);
				new SavingConfig(file).runTaskAsynchronously(plugin);
				
			}
		}.runTaskAsynchronously(plugin);

	}
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) { 
		Player p = e.getPlayer();
		Block b = e.getBlock();
		Material m = b.getType();
		UUID UUID = p.getUniqueId();
		DataMethods.moreBroken(UUID, m); 


	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) { 
		Player p = e.getPlayer();
		UUID UUID = p.getUniqueId();
		DataMethods.morePlaced(UUID); 

		
	}

		
}
