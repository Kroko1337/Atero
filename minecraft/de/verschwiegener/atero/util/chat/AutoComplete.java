package de.verschwiegener.atero.util.chat;

import java.util.Arrays;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.command.Command;
import de.verschwiegener.atero.module.Module;

public class AutoComplete {

    public static boolean hasAutoComplete;

    public static String onAutoComplete(final String text) {
	if (!text.startsWith("."))
	    return "";
	AutoComplete.hasAutoComplete = false;

	final String[] args = text.substring(1).split(" ");
	if (args.length > 1) {
	    final Command c = Management.instance.commandmgr.getCommandByName(args[0]);
	    if (c != null) {
		for(String str : c.getComplete()) {
		    final String[] args2 = str.split(" ");
			final String lastArgument = args2[args.length - 2];
			if (lastArgument != null) {
			    switch (lastArgument) {
			    case "<module>":
				final Module m = Management.instance.modulemgr.getModulebyStartsWith(args[args.length - 1]);
				try {
				    if (m != null) {
					if (text.endsWith(" ")) {
					    System.out.println("Module: " + m.getName());
					    AutoComplete.hasAutoComplete = true;
					    return m.getName().toLowerCase().replaceFirst(args[args.length - 1].toLowerCase(),
						    "") + str.toLowerCase().replaceFirst("<module>", "");
					} else {
					    AutoComplete.hasAutoComplete = true;
					    return m.getName().toLowerCase().replaceFirst(args[args.length - 1].toLowerCase(),
						   "") + " " + str.toLowerCase().replaceFirst("<module>", "");
					}
				    }
				} catch (final NullPointerException ex) {
				    ex.printStackTrace();
				}
			    case "<key>":

			    }
			}
		}
	    }
	} else if (!args[0].isEmpty()) {
	    final Command c = Management.instance.commandmgr.getCommandByStartsWith(args[0]);
	    if (c != null) {
		System.out.println("Return");
		if (text.endsWith(" ")) {
		    AutoComplete.hasAutoComplete = true;
		    return c.getSyntax().toLowerCase().replaceFirst(args[0].toLowerCase(), "") + c.getComplete()[0];
		} else {
		    AutoComplete.hasAutoComplete = true;
		    return c.getSyntax().toLowerCase().replaceFirst(args[0].toLowerCase(), "") + " " + c.getComplete()[0];
		}
	    }
	}
	return "";
    }
    public static String onAutoComplete2(final String text) {
	if (!text.startsWith("."))
	    return text;
	final String[] args = text.substring(1).split(" ");
	System.out.println("Args: " + Arrays.toString(args));
	if (args.length > 1) {
	}else {
	    final Command c = Management.instance.commandmgr.getCommandByStartsWith(args[0]);
	    if(c != null) {
		return "." + c.getSyntax();
	    }
	}
	return text;
    }
}
