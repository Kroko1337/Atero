package de.verschwiegener.atero.settings;

import java.util.ArrayList;
import java.util.function.Predicate;

public class SettingsManager {
	
	ArrayList<Setting> settings = new ArrayList<>();
	
	public void addSetting(Setting setting) {
		settings.add(setting);
	}
	
	
	public Setting getSettingByName(final String name) {
		return settings.stream().filter(new Predicate<Setting>() {
			@Override
			public boolean test(Setting module) {
				return module.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase());
			}
		}).findFirst().orElse(null);
	}

}
