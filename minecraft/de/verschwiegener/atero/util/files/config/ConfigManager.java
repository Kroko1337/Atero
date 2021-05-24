package de.verschwiegener.atero.util.files.config;

import java.util.ArrayList;

import de.verschwiegener.atero.module.Module;

public class ConfigManager {
    
    public ArrayList<Config> configs = new ArrayList<>();

    
    public ConfigManager() {
	ArrayList<ConfigItem> items = new ArrayList<>();
	items.add(new ConfigItem(new String[] {"set", "Killaura", "true"}));
	items.add(new ConfigItem(new String[] {"set", "Scaffold", "false"}));
	items.add(new ConfigItem(new String[] {"set", "Chestealer", "true"}));
	configs.add(new Config("Test", "Description", "ServerIP", items));
    }
    
    public Config getConfigByName(final String name) {
	return configs.stream().filter(config -> config.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
		.findFirst().orElse(null);
    }
}
