package de.verschwiegener.atero.design.font.fonts;

import de.verschwiegener.atero.design.font.Font;
import de.verschwiegener.atero.design.font.Fontrenderer;

public class ChatFont extends Font{
	
	static String chars = "<>abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUWVXYZ0123456789§$%&/{}()[].,;-_|=-+*´`\"";
	
	public ChatFont() {
		super("Inter2", Fontrenderer.getFontByName("Inter-ExtraLight"), 4F, 5F, chars);
	}
	

}
