package de.verschwiegener.atero.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.verschwiegener.atero.design.font.Font;
import de.verschwiegener.atero.module.Module;

public class Util {
	
	/**
	 * Sorts the Modules after the Name length
	 * @param modules
	 */
	public static void sortModuleList(ArrayList<Module> modules, final Font font) {
		Collections.sort(modules, new Comparator<Module>() {

			@Override
			public int compare(Module o1, Module o2) {
				if(font.getFontrenderer().getStringWidth(o1.getName()) > font.getFontrenderer().getStringWidth(o2.getName())) {
					return -1;
				}
				if(font.getFontrenderer().getStringWidth(o1.getName()) < font.getFontrenderer().getStringWidth(o2.getName())) {
					return 1;
				}
				return 0;
			}
		});
	}
	
	
}
