package de.verschwiegener.atero.design.font;

import java.util.ArrayList;
import java.util.function.Predicate;

import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.design.font.fonts.ArrayListFont;
import de.verschwiegener.atero.design.font.fonts.TextFont;
import de.verschwiegener.atero.design.font.fonts.chat.ChatTextFont;
import de.verschwiegener.atero.design.font.fonts.chat.ChatTextFontBold;
import de.verschwiegener.atero.design.font.fonts.chat.ChatTextFontItalic;
public class FontManager {
	
	ArrayList<Font> fonts = new ArrayList<>();
	
	public FontManager() {
		fonts.add(new ArrayListFont());
		fonts.add(new TextFont());
		fonts.add(new ChatTextFont());
		fonts.add(new ChatTextFontBold());
		fonts.add(new ChatTextFontItalic());
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
