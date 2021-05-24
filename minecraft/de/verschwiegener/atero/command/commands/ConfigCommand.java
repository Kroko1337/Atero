package de.verschwiegener.atero.command.commands;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.command.Command;
import de.verschwiegener.atero.util.chat.ChatUtil;
import de.verschwiegener.atero.util.files.config.Config;

public class ConfigCommand extends Command{

    public ConfigCommand(String syntax, String description, String[] complete) {
	super("Config", "Loads a config", new String[] {"locale list", "locale load <config>", "online list", "online list <config>"});
    }

    @Override
    public void onCommand(String[] args) {
	switch (args[1]) {
	case "locale":
	    switch (args[2]) {
	    case "list":
		ChatUtil.sendMessage("§6------§fConfigs §6------");
		for(Config config : Management.instance.configmgr.configs) {
		    ChatUtil.addConfigMessage(config);
		}
		break;
	    case "load":
		try {
		    Management.instance.configmgr.getConfigByName(args[3]).loadConfig();
		} catch (NullPointerException e) {
		   ChatUtil.sendMessageWithPrefix("Config not found: " + args[3]);
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
