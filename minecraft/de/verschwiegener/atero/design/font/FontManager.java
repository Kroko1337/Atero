package de.verschwiegener.atero.design.font;

import java.util.ArrayList;
import java.util.function.Predicate;

import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.design.font.fonts.ArrayListFont;
import de.verschwiegener.atero.design.font.fonts.ChatFont;

import de.verschwiegener.atero.design.font.fonts.ChatTextFont;
public class FontManager {
	
	ArrayList<Font> fonts = new ArrayList<>();
	
	public FontManager() {
		fonts.add(new ArrayListFont());
		fonts.add(new ChatFont());
		fonts.add(new ChatTextFont());
	}
	
	public Font getFontByName(final String name) {
		return fonts.stream().filter(new Predicate<Font>() {
			@Override
			public boolean test(Font module) {
				return module.getName().equalsIgnoreCase(name);
			}
		}).findFirst().orElse(null);
	}

}
