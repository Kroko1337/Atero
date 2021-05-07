package de.verschwiegener.atero.design.font.fonts.chat;

import de.verschwiegener.atero.design.font.Font;
import de.verschwiegener.atero.design.font.Fontrenderer;

public class ChatTextFontItalic extends Font {
	
	static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUWVXYZ0123456789()<>§$%&?/{}[].,;:-_|=-+*´`\"";

	public ChatTextFontItalic() {
		super("InterChatItalic", Fontrenderer.getFontByName("Inter-ExtraLight"), 4F, 5F, chars, false, true);
	}

}
