package de.verschwiegener.atero.settings;

import java.util.ArrayList;

public class SettingsManager {

    ArrayList<Setting> settings = new ArrayList<>();

    public void addSetting(final Setting setting) {
	settings.add(setting);
    }

    public ArrayList<Setting> getSearchResult(final String search) {
	final ArrayList<Setting> result = new ArrayList<>();
	for (final Setting s : settings) {
	    if (s.getName().toLowerCase().startsWith(search.toLowerCase()) || (s.getItemByStartsWith(search) != null)) {
		result.add(s);
	    }
	}
	return result;
    }

    public Setting getSettingByName(final String name) {
	return settings.stream().filter(module -> module.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
		.findFirst().orElse(null);
    }

}
