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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

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
			prog.awardCriteria("dead");
		}
		
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
	
	
	public void treecounter(TreeFellEvent e) {
		Player p = e.getPlayer();
		UUID UUID = p.getUniqueId();
		p.sendMessage("yes");
		
		int value = plugin.infom.get(UUID).getTrees();
		plugin.infom.get(UUID).setTrees(value+1);
		
		if (!timers.containsKey(UUID)) {
			timers.put(UUID, new Timers(0, 3));
		}
		else {
			timers.get(UUID).setTree(3);
		}
		
	}
	
}
