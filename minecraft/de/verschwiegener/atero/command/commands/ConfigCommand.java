package de.verschwiegener.atero.command.commands;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.command.Command;
import de.verschwiegener.atero.util.chat.ChatUtil;
import de.verschwiegener.atero.util.files.config.Config;
import de.verschwiegener.atero.util.files.config.ConfigType;

public class ConfigCommand extends Command {

    public ConfigCommand() {
	super("Config", "Loads a config", new String[] { "locale list", "locale load <config>",
		"locale save <name> <description> <serverIP>", "online list", "online list <config>" });
    }

    @Override
    public void onCommand(final String[] args) {
	System.err.println("args[1]: " + args[1]);
	switch (args[1]) {
	case "locale":
	    switch (args[2]) {
	    
	    case "list":
		ChatUtil.sendMessage("§6------§fConfigs §6------");
		for (final Config config : Management.instance.configmgr.configs) {
		    if(config.getType() == ConfigType.Locale) {
			ChatUtil.addConfigMessage(config);
		    }
		}
		break;
		
	    case "load":
		try {
		    Management.instance.configmgr.getConfigByName(args[3], ConfigType.Locale).loadConfig();
		    ChatUtil.sendMessageWithPrefix("Config loaded");
		} catch (final NullPointerException e) {
		    ChatUtil.sendMessageWithPrefix("Config not found: " + args[3]);
		}
		break;
		
	    case "save":
		if (args.length == 5) {
		    try {
			Management.instance.configmgr.configs
				.add(new Config(args[3], args[4], args[5], ConfigType.Locale));
		    } catch (final Exception e) {
			Management.instance.configmgr.configs.add(new Config(args[3], ConfigType.Locale));
		    }
		} else {
		    Management.instance.configmgr.configs.add(new Config(args[3], ConfigType.Locale));
		}
		break;
	    }
	    break;

	case "online":
	    switch (args[2]) {
	    case "list":
		ChatUtil.sendMessage("§6------§fConfigs §6------");
		for (final Config config : Management.instance.configmgr.configs) {
		    if(config.getType() == ConfigType.Online) {
			ChatUtil.addConfigMessage(config);
		    }
		}
		break;
	    case "load":
		try {
		    Management.instance.configmgr.getConfigByName(args[3], ConfigType.Online).loadConfig();
		} catch (final NullPointerException e) {
		    ChatUtil.sendMessageWithPrefix("Config not found: " + args[3]);
		}
		break;
	    }
	    break;
	}
    }

}
