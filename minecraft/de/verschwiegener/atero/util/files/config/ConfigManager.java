package de.verschwiegener.atero.util.files.config;

import java.util.ArrayList;

import de.verschwiegener.atero.module.Module;

public class ConfigManager {
    
    public ArrayList<Config> configs = new ArrayList<>();

    
    public Config getConfigByName(final String name, ConfigType type) {
	return configs.stream().filter(config -> config.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()) && (config.getType() == type))
		.findFirst().orElse(null);
    }
}
