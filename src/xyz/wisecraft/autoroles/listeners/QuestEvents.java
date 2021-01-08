package xyz.wisecraft.autoroles.listeners;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.songoda.ultimatetimber.events.TreeFellEvent;

import me.ryanhamshire.GPFlags.event.PlayerClaimBorderEvent;
import xyz.wisecraft.autoroles.Main;
import xyz.wisecraft.autoroles.data.Timers;

public class QuestEvents implements Listener {

	private Main plugin = Main.getPlugin(Main.class);
	private ConcurrentHashMap<UUID, Timers> timers = Main.timers;
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		String message = e.getMessage();
		
		
		if (message.equalsIgnoreCase("/tpr")) {
			
			Player p = e.getPlayer();
			NamespacedKey key = new NamespacedKey(plugin, "welcome_wild");
			Advancement a = Bukkit.getAdvancement(key);
			AdvancementProgress prog = p.getAdvancementProgress(a);
			
			if (!prog.isDone())
				prog.awardCriteria("tpr");
			
		}
		else if (message.equalsIgnoreCase("/spawn")) {
					
			Player p = e.getPlayer();
			NamespacedKey key = new NamespacedKey(plugin, "spawn");
			Advancement a = Bukkit.getAdvancement(key);
			AdvancementProgress prog = p.getAdvancementProgress(a);
					
					
				if (!prog.isDone())
					prog.awardCriteria("spawn");
		}
	}
	
	@EventHandler
	public void FlyingAccident(PlayerDeathEvent e) {
		Player p = e.getEntity().getPlayer();
		Timers times = timers.get(p.getUniqueId());

		if (times.getFly() > 0 & e.getDeathMessage().equalsIgnoreCase(p.getName() + " fell from a high place")) {
			NamespacedKey key = new NamespacedKey(plugin, "flying_accident");
			Advancement a = Bukkit.getAdvancement(key);
			AdvancementProgress prog = p.getAdvancementProgress(a); 
			
			e.setDeathMessage(p.getName() + " didn't have flight");
			if (!prog.isDone())
			prog.awardCriteria("deadfall");
			return;
		}
		else if (e.getDeathMessage().equalsIgnoreCase(p.getName() + " experienced kinetic energy") ) {
			NamespacedKey key = new NamespacedKey(plugin, "accident_flying");
			Advancement a = Bukkit.getAdvancement(key);
			AdvancementProgress prog = p.getAdvancementProgress(a); 
			
			
			if (!prog.isDone())
			prog.awardCriteria("wall");
			return;
		}
		else if (times.getTree() > 0 & e.getDeathMessage().equalsIgnoreCase(p.getName() + " died")) {
			NamespacedKey key = new NamespacedKey(plugin, "move");
			Advancement a = Bukkit.getAdvancement(key);
			AdvancementProgress prog = p.getAdvancementProgress(a); 
			
			e.setDeathMessage(p.getName() + " was crushed under a tree");
			if (!prog.isDone())
			prog.awardCriteria("move");
		}
		
		}

	@EventHandler
	public void citizen(PlayerAdvancementDoneEvent e) {
			new BukkitRunnable() {
				public void run() {
					//Citizen
					NamespacedKey citKey = new NamespacedKey(Main.getPlugin(Main.class), "citizen");
					Advancement citA = Bukkit.getAdvancement(citKey);
					String citizen = citA.getKey().getKey();
					String event = e.getAdvancement().getKey().getKey();
					
					if (citizen.equals(event))
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + e.getPlayer().getName() + " parent add citizen");
					
					
					//Iron stash
					NamespacedKey ironKey = new NamespacedKey(Main.getPlugin(Main.class), "hidden_iron");
					Advancement ironA = Bukkit.getAdvancement(ironKey);
					String iron = ironA.getKey().getKey();
					if (iron.equals(event)) {
						Player p = e.getPlayer();
						
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:give " + p.getName() + " iron_ore 6");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:give " + p.getName() + " minecraft:iron_ingot 3");
						
					}
						
						
					
						
						
				}
			}.runTask(Main.getPlugin(Main.class));
	}
	
	@EventHandler
	public void FlyTimer(PlayerClaimBorderEvent e) {
		Player p = e.getPlayer();
		UUID UUID = p.getUniqueId();
		
		if (!timers.containsKey(UUID)) {
			timers.put(UUID, new Timers(10, 0));
		}
		else {
			timers.get(UUID).setFly(10);
		}
	}
	
	@EventHandler
	public void treecounter(TreeFellEvent e) {
		Player p = e.getPlayer();
		UUID UUID = p.getUniqueId();
		

		NamespacedKey key = new NamespacedKey(plugin, "timber");
		Advancement a = Bukkit.getAdvancement(key);
		AdvancementProgress prog = p.getAdvancementProgress(a); 
		if (!prog.isDone())
			prog.awardCriteria("tree");
		
		
		int trees = plugin.infom.get(UUID).getTrees();
		plugin.infom.get(UUID).setTrees(trees+1);
		
		if (trees+1 > 999) {
			NamespacedKey key2 = new NamespacedKey(plugin, "lumberjack");
			Advancement a2 = Bukkit.getAdvancement(key2);
			AdvancementProgress prog2 = p.getAdvancementProgress(a2); 
			if (!prog2.isDone())
				prog.awardCriteria("tree1000");
			
		}
		if (trees+1 > 4999) {
			NamespacedKey key2 = new NamespacedKey(plugin, "juggerjack");
			Advancement a2 = Bukkit.getAdvancement(key2);
			AdvancementProgress prog2 = p.getAdvancementProgress(a2); 
			if (!prog2.isDone())
				prog.awardCriteria("tree5000");
		}
		
		if (!timers.containsKey(UUID)) {
			timers.put(UUID, new Timers(0, 6));
		}
		else {
			timers.get(UUID).setTree(6);
		}
	}
}
