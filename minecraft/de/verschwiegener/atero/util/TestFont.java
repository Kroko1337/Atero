package de.verschwiegener.atero.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.texture.DynamicTexture;

public class TestFont {
    
    private final DynamicTexture fontTexture; 
    //increase size for more glyphs
    private final GlyphMetrics[] fontGlyphMetrics = new GlyphMetrics[256];
    
    public TestFont() {
    }
    
    private DynamicTexture generateBitMap(Font font) {
	BufferedImage bitmap = new BufferedImage(fontGlyphMetrics.length * 4, fontGlyphMetrics.length * 4,
		BufferedImage.TYPE_INT_ARGB);
	Graphics2D graphics = (Graphics2D) bitmap.getGraphics();
	graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	graphics.setFont(font);
	int xPos = 0, yPos = 0;
	FontMetrics metrics = graphics.getFontMetrics();
	for (int i = 0; i < fontGlyphMetrics.length; i++) {
	    Rectangle2D glyphsizeRect = metrics.getStringBounds(Character.toString((char) i), graphics);
	    int width = (glyphsizeRect.getBounds()).width + 8;
	    int height = (glyphsizeRect.getBounds()).height;
	    if (width + xPos >= fontGlyphMetrics.length * 4) {
		xPos = 0;
		yPos += height;
	    }

	    graphics.drawString(Character.toString((char) i), xPos + 1, yPos + metrics.getAscent());

	    GlyphMetrics glyph = new GlyphMetrics(xPos, yPos, width, height);
	    fontGlyphMetrics[i] = glyph;
	    yPos += width;
	}
	return new DynamicTexture(bitmap);
    }
    public DynamicTexture getFontTexture() {
	return fontTexture;
    }

}
