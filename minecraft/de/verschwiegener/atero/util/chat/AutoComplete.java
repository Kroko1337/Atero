package de.verschwiegener.atero.util.chat;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.command.Command;
import de.verschwiegener.atero.module.Module;

public class AutoComplete {
	
	static boolean hasAutoComplete;

	public static String onAutoComplete(String text) {
		if (!text.startsWith("."))
			return "";

		hasAutoComplete = false;
		
		final String[] args = text.substring(1).split(" ");
		if (args.length > 1) {
			final Command c = Management.instance.cmdmgr.getCommandByName(args[0]);
			if (c != null) {
				final String[] args2 = c.getComplete().split(" ");
				final String lastArgument = args2[args.length - 2];
				if (lastArgument != null) {
					switch (lastArgument) {
					case "<module>":
						final Module m = Management.instance.mdlmgr.getModulebyStartsWith(args[args.length - 1]);
						try {
							if (m != null) {
								if(text.endsWith(" ")) {
									hasAutoComplete = true;
									return m.getName().toLowerCase().replaceFirst(args[args.length - 1].toLowerCase(), "") + c.getComplete().toLowerCase().replaceFirst("<module>", "");
								}else {
									hasAutoComplete = true;
									return m.getName().toLowerCase().replaceFirst(args[args.length - 1].toLowerCase(), "") + " " + c.getComplete().toLowerCase().replaceFirst("<module>", "");	
								}
							}
						} catch (NullPointerException ex) {
							ex.printStackTrace();
						}
					case "<key>":
						
					}
				}
			}
		} else if (!args[0].isEmpty()) {
			final Command c = Management.instance.cmdmgr.getCommandByStartsWith(args[0]);
			if (c != null) {
				if(text.endsWith(" ")) {
					hasAutoComplete = true;
					return c.getSyntax().toLowerCase().replaceFirst(args[0].toLowerCase(), "") + c.getComplete();
				}else {
					hasAutoComplete = true;
					return c.getSyntax().toLowerCase().replaceFirst(args[0].toLowerCase(), "") + " " + c.getComplete();
				}
			}
		}
		return "";
	}

}
