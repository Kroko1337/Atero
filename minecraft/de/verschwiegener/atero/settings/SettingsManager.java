package de.verschwiegener.atero.settings;

import java.util.ArrayList;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Module;

public class SettingsManager {

    ArrayList<Setting> settings = new ArrayList<>();

    public void addSetting(final Setting setting) {
	settings.add(setting);
    }

    public ArrayList<Module> getSearchResult(final String search) {
	final ArrayList<Module> result = new ArrayList<>();
	for (final Setting s : settings) {
	    if(s.getName().equalsIgnoreCase(search)) {
		result.add(s.getModule());
		break;
	    }
	    if(s.getItemByStartsWith(search) != null) {
		result.add(s.getModule());
		break;
	    }
	    if(s.getItemByContains(search) != null) {
		result.add(s.getModule());
		break;
	    }
	}
	for(Module m : Management.instance.modulemgr.modules) {
	    if(m.getName().equalsIgnoreCase(search)) {
		if(!result.contains(m)) {
		    result.add(m);
		}
	    }
	}
	return result;
	
    }

	public ArrayList<Setting> getSettings() {
		return settings;
	}

	public Setting getSettingByName(final String name) {
	return settings.stream().filter(module -> module.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
		.findFirst().orElse(null);
    }

}
