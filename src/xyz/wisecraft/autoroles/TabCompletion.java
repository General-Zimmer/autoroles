package xyz.wisecraft.autoroles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class TabCompletion implements TabCompleter{
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			 String alias,  String[] args) {
		List<String> cmds = new ArrayList<String>();
		
		 switch (args.length) {
		  case 1:
		    if (sender.hasPermission("autoroles.manage")) {
		    	cmds.add("save");
		    	cmds.add("load");
		    	cmds.add("rank");
		    }
		    return StringUtil.copyPartialMatches(args[0], cmds, new ArrayList<String>());
		   
		    //https://www.spigotmc.org/threads/tabcompleter-not-working.406512/
		  }
		return null;
		
	}

}
