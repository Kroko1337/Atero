package de.verschwiegener.atero.design.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import de.verschwiegener.atero.Management;
import net.minecraft.util.StringUtils;

public class Fontrenderer {
    public static Font getFontByName(final String name) {
	try {
	    return Font.createFont(Font.TRUETYPE_FONT, Management.class.getResourceAsStream(
		    "/assets/minecraft/" + Management.instance.CLIENT_NAME.toLowerCase() + "/fonts/" + name + ".ttf"));
	} catch (FontFormatException | IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private final UnicodeFont unicodefont;

    public Fontrenderer(final Font font, final float fontSize, final float antiAliasingFactor, final String chars,
	    final boolean bold, final boolean italic) {
	// unicodefont = new UnicodeFont(font.deriveFont(fontSize *
	// antiAliasingFactor));
	unicodefont = new UnicodeFont(font, (int) (fontSize * antiAliasingFactor), bold, italic);
	unicodefont.getEffects().add(new ColorEffect(Color.white));
	unicodefont.addAsciiGlyphs();
	unicodefont.addGlyphs(0, 65535);
	try {
	    unicodefont.loadGlyphs();
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }

    public void drawString(final String text, final float x, final float y, final int color) {
	if (text == null)
	    return;
	
	for(int i = 0; i < text.length();i++) {
	    //System.out.println("Char: " + text.charAt(i) + " Unicode: " + escapeNonAscii(Character.toString(text.charAt(i))));
	}
	GL11.glPushMatrix();
	GL11.glPushAttrib(1048575);
	GL11.glDisable(2929);
	GL11.glDisable(3553);
	GL11.glEnable(2848);
	GL11.glEnable(3042);
	GL11.glBlendFunc(770, 771);

	GL11.glScaled(0.5F, 0.5F, 0.5F);
	
	boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        if (!blend)
            GL11.glEnable(GL11.GL_BLEND);
        if (lighting)
            GL11.glDisable(GL11.GL_LIGHTING);
        if (texture)
            GL11.glDisable(GL11.GL_TEXTURE_2D);


	//unicodefont.addGlyphs(text);
	//try {
	    //unicodefont.loadGlyphs();
	//} catch (final Exception e) {
	    //e.printStackTrace();
	//}

	unicodefont.drawString(x, y, text, new org.newdawn.slick.Color(color));
	
	if (texture)
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (lighting)
            GL11.glEnable(GL11.GL_LIGHTING);
        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);

	GL11.glDisable(3042);
	GL11.glEnable(3553);
	GL11.glEnable(2929);
	GL11.glDisable(2848);
	GL11.glDisable(3042);
	GL11.glPopAttrib();
	GL11.glPopMatrix();
    }
    
    private static String escapeNonAscii(String str) {

	StringBuilder retStr = new StringBuilder();
	for (int i = 0; i < str.length(); i++) {
	    int cp = Character.codePointAt(str, i);
	    int charCount = Character.charCount(cp);
	    if (charCount > 1) {
		i += charCount - 1; // 2.
		if (i >= str.length()) {
		    throw new IllegalArgumentException("truncated unexpectedly");
		}
	    }

	    if (cp < 128) {
		retStr.appendCodePoint(cp);
	    } else {
		retStr.append(String.format("\\u%x", cp));
	    }
	}
	return retStr.toString();
    }

    public void drawStringScaled(final String text, final float x, final float y, final int color, final float scaled) {
	if (text == null)
	    return;
	GL11.glPushMatrix();
	GL11.glPushAttrib(1048575);
	GL11.glDisable(2929);
	GL11.glDisable(3553);
	GL11.glEnable(2848);
	GL11.glEnable(3042);
	GL11.glBlendFunc(770, 771);

	GL11.glScaled(scaled, scaled, scaled);

	// unicodefont.addGlyphs(text);
	try {
	    // unicodefont.loadGlyphs();
	} catch (final Exception e) {
	    e.printStackTrace();
	}

	unicodefont.drawString(x, y, text, new org.newdawn.slick.Color(color));

	GL11.glDisable(3042);
	GL11.glEnable(3553);
	GL11.glEnable(2929);
	GL11.glDisable(2848);
	GL11.glDisable(3042);
	GL11.glPopAttrib();
	GL11.glPopMatrix();
    }

    public void drawStringWithShadows(final String text, final int x, final int y, final float depth, final int color) {
	drawString(StringUtils.stripControlCodes(text), x + depth, y + depth, 0x000000);
	drawString(text, x, y, color);
    }

    public int getStringHeight(final String text) {
	return unicodefont.getHeight(text) / 2;
    }

    public int getBaseStringHeight() {
	final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
	return unicodefont.getHeight(alphabet) / 2;
    }

    public int getSpaceWidth() {
	return unicodefont.getSpaceWidth();
    }

    public int getStringWidth(final String text) {
	return unicodefont.getWidth(text);
    }

    public int getStringWidth2(final String text) {
	return unicodefont.getWidth(text) / 2;
    }

}
