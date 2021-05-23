package de.verschwiegener.atero.command.commands;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.command.Command;
import de.verschwiegener.atero.util.chat.ChatUtil;
import de.verschwiegener.atero.util.files.config.Config;

public class ConfigCommand extends Command{

    public ConfigCommand() {
	super("Config", "Loads a config", new String[] {"locale list", "locale load <config>", "locale save <name> <description> <serverIP>", "online list", "online list <config>"});
    }

    @Override
    public void onCommand(String[] args) {
	System.err.println("args[1]: " + args[1]);
	switch (args[1]) {
	case "locale":
	    System.err.println("Args[2]: " + args[2]);
	    switch (args[2]) {
	    case "list":
		ChatUtil.sendMessage("§6------§fConfigs §6------");
		for(Config config : Management.instance.configmgr.configs) {
		    ChatUtil.addConfigMessage(config);
		}
		break;
	    case "load":
		try {
		    System.err.println("Load");
		    Management.instance.configmgr.getConfigByName(args[3]).loadConfig();
		    ChatUtil.sendMessageWithPrefix("Config loaded");
		} catch (NullPointerException e) {
		   ChatUtil.sendMessageWithPrefix("Config not found: " + args[3]);
		}
		break;
	    case "save":
		if(args.length == 5) {
		    Management.instance.configmgr.configs.add(new Config(args[3], args[4], args[5]));
		}else {
		    Management.instance.configmgr.configs.add(new Config(args[3]));
		}
		break;
	    }
	    break;

	case "online":
	    switch (args[2]) {
	    case "list":
		ChatUtil.sendMessage("§6------§fConfigs §6------");
		for(Config config : Management.instance.configmgr.configs) {
		    ChatUtil.addConfigMessage(config);
		}
		break;
	    case "load":
		try {
		} catch (NullPointerException e) {
		   ChatUtil.sendMessageWithPrefix("Config not found: " + args[3]);
		}
		break;
	    }
	    break;
	}
    }

}
