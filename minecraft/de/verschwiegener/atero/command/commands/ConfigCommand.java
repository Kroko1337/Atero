package de.verschwiegener.atero.command.commands;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.command.Command;
import de.verschwiegener.atero.configsystem.Config;
import de.verschwiegener.atero.util.chat.ChatUtil;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("Config", "Manages your configs", new String[]{"list"});
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length != 3 && args.length != 2) {
            ChatUtil.sendMessageWithPrefix("Invalid syntax! Syntax: .config list, .config add [Config], .config remove [Config], .config load [Config]");
            return;
        }
        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("add")) {
                if (Management.instance.configManager.getConfigByName(args[2]) != null) {
                    ChatUtil.sendMessageWithPrefix("Error! This config already exists");
                    return;
                }
                Config config = new Config(args[2]);
                Management.instance.configManager.getConfigs().add(config);
                config.saveConfig();
                ChatUtil.sendMessageWithPrefix("You successfully saved your current settings into a config named " + args[2]);
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (Management.instance.configManager.getConfigByName(args[2]) == null) {
                    ChatUtil.sendMessageWithPrefix("Error! This config doesn't exist");
                    return;
                }
                Config config = new Config(args[2]);
                config.deleteConfig();
                Management.instance.configManager.getConfigs().remove(config);
                ChatUtil.sendMessageWithPrefix("You successfully removed the config named " + args[2]);
            } else if (args[1].equalsIgnoreCase("load")) {
                if (Management.instance.configManager.getConfigByName(args[2]) == null) {
                    ChatUtil.sendMessageWithPrefix("Error! This config doesn't exist");
                    return;
                }
                Config config = new Config(args[2]);
                config.loadConfig();
                ChatUtil.sendMessageWithPrefix("You successfully loaded the config named " + args[2]);
            } else {
                ChatUtil.sendMessageWithPrefix("Invalid syntax! Syntax: .config list, .config add [Config], .config remove [Config], .config load [Config]");
            }
        } else if (args[1].equalsIgnoreCase("list")) {
            if (Management.instance.configManager.getConfigs().isEmpty()) {
                ChatUtil.sendMessageWithPrefix("You don't have any configs yet!");
                return;
            }
            for (Config config : Management.instance.configManager.getConfigs())
                ChatUtil.sendMessageWithPrefix(config.getName());
        } else {
            ChatUtil.sendMessageWithPrefix("Invalid syntax! Syntax: .config list, .config add [Config], .config remove [Config], .config load [Config]");
        }
    }

}
