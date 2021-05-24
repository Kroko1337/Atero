package de.verschwiegener.atero.util.files.config;

import java.util.ArrayList;

public class Config {

    private String name;
    private String description;
    private String recommendedServerIP;

    ArrayList<ConfigItem> items = new ArrayList<>();

    public Config(String name, String description, String recommendedServerIP, ArrayList<ConfigItem> items) {
	this.name = name;
	this.description = description;
	this.recommendedServerIP = recommendedServerIP;
	this.items = items;
    }
    
    public void loadConfig() {
	for(ConfigItem item : items) {
	    item.execute();
	}
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getRecommendedServerIP() {
	return recommendedServerIP;
    }

    public void setRecommendedServerIP(String recommendedServerIP) {
	this.recommendedServerIP = recommendedServerIP;
    }

    public ArrayList<ConfigItem> getItems() {
	return items;
    }

    public void setItems(ArrayList<ConfigItem> items) {
	this.items = items;
    }

}
