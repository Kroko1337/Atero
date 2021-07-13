package de.verschwiegener.atero.font;

public class FontManager {

    public static GlyphPageFontRenderer ROBOTOTHIN_20;
    public static GlyphPageFontRenderer Arial;

    public FontManager() {
        ROBOTOTHIN_20 = GlyphPageFontRenderer.create("Roboto-Thin", 20, false, false, false, true);
        Arial = GlyphPageFontRenderer.create("Arial", 20, false, false, false, true);

    }

}
