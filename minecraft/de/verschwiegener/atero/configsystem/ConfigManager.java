package de.verschwiegener.atero.configsystem;

import java.util.ArrayList;

public class ConfigManager {

    private ArrayList<Config> configs = new ArrayList<>();

    public Config getConfigByName(String name) {
        for(Config config : configs) {
            if(config.getName().equalsIgnoreCase(name)) {
                return config;
            }
        }
        return null;
    }

    public ArrayList<Config> getConfigs() {
        return configs;
    }

}
