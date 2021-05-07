package de.verschwiegener.atero.design.font.fonts.chat;

import de.verschwiegener.atero.design.font.Font;
import de.verschwiegener.atero.design.font.Fontrenderer;

public class ChatTextFont extends Font{

	static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUWVXYZ0123456789()<>§$%&?/{}[].,;-_|=-+*´`:\"";
	
	public ChatTextFont() {
		super("InterChat", Fontrenderer.getFontByName("Inter-ExtraLight"), 4F, 5F, chars);
	}

}
