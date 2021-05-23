package de.verschwiegener.atero.util.files.config;

import de.verschwiegener.atero.Management;

public class ConfigItem {

    String[] args;

    public ConfigItem(String[] args) {
	this.args = args;
    }

    public void execute() {
	switch (args[0]) {
	case "toggle":
	    Management.instance.modulemgr.getModuleByName(args[1]).toggle(Boolean.parseBoolean(args[2]));
	    break;
	case "set":
	    if (args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false")) {
		Management.instance.settingsmgr.getSettingByName(args[1]).getItemByName(args[2])
			.setState(Boolean.valueOf(args[3]));
		break;
	    } else if (isInt(args[3])) {
		Management.instance.settingsmgr.getSettingByName(args[1]).getItemByName(args[2]).setCurrentValue(Integer.parseInt(args[3]));
		break;
	    } else {
		Management.instance.settingsmgr.getSettingByName(args[1]).getItemByName(args[2]).setCurrent(args[3]);
		break;
	    }
	}
    }
    public String getMode() {
	return args[0];
    }
    public String[] getArgs() {
	return args;
    }

    private boolean isInt(String str) {
	try {
	    int x = Integer.parseInt(str);
	    return true;
	} catch (NumberFormatException e) {
	    return false;
	}

    }

}
