package de.verschwiegener.atero.settings;

import java.util.ArrayList;

import de.verschwiegener.atero.module.Module;

public class Setting {

    String name;
    ArrayList<SettingsItem> items;

    public Setting(final Module m, final ArrayList<SettingsItem> items) {
	name = m.getName();
	this.items = items;
    }

    public SettingsItem getItemByName(final String name) {
	return items.stream().filter(module -> module.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
		.findFirst().orElse(null);
    }

    public SettingsItem getItemByStartsWith(final String name) {
	return items.stream().filter(module -> module.getName().toLowerCase().startsWith(name.toLowerCase()))
		.findFirst().orElse(null);
    }
    public SettingsItem getItemByContains(final String name) {
	return items.stream().filter(module -> module.getName().toLowerCase().contains(name.toLowerCase()))
		.findFirst().orElse(null);
    }

    public ArrayList<SettingsItem> getItems() {
	return items;
    }

    public String getName() {
	return name;
    }

}
