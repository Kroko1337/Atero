package de.verschwiegener.atero.font;

public class FontManager {

    public static GlyphPageFontRenderer ROBOTOTHIN_20;

    public FontManager() {
        ROBOTOTHIN_20 = GlyphPageFontRenderer.create("Roboto-Thin", 20, false, false, false, true);

    }

}
