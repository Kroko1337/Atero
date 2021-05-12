package de.verschwiegener.atero.design.font.fonts;

import de.verschwiegener.atero.design.font.Font;
import de.verschwiegener.atero.design.font.Fontrenderer;

public class WaterMarkFont extends Font{

    static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUWVXYZ0123456789<>�$%&/{}()[].,;-_|=-+*�`\"";

    public WaterMarkFont() {
        super("WaterMarkFont", Fontrenderer.getFontByName("OfficinaSans-Book"), 15F, 5F, chars);
    }
}
