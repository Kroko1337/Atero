package de.verschwiegener.atero.settings;

import java.util.ArrayList;
import java.util.function.Predicate;

import de.verschwiegener.atero.module.Module;

public class Setting {
	
	String name;
	ArrayList<SettingsItem> items;
	
	public Setting(Module m,ArrayList<SettingsItem> items) {
		this.name = m.getName();
		this.items = items;
	}
	public String getName() {
		return name;
	}
	public ArrayList<SettingsItem> getItems() {
		return items;
	}
	
	public SettingsItem getItemByName(final String name) {
		return items.stream().filter(new Predicate<SettingsItem>() {
			@Override
			public boolean test(SettingsItem module) {
				return module.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase());
			}
		}).findFirst().orElse(null);
	}

}
