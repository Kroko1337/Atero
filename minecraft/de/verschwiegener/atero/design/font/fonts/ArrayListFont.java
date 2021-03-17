package de.verschwiegener.atero.design.font.fonts;

import de.verschwiegener.atero.design.font.Font;
import de.verschwiegener.atero.design.font.Fontrenderer;

public class ArrayListFont extends Font{
	
	static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUWVXYZ0123456789<>§$%&/{}()[].,;-_|=-+*´`\"";

	public ArrayListFont() {
		super("ArrayListFont", Fontrenderer.getFontByName("Inter-ExtraLight"), 5F, 5F, chars);
	}
}
