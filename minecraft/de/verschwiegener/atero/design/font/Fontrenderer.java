package de.verschwiegener.atero.design.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.lang.reflect.Executable;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import de.verschwiegener.atero.Management;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;

public class Fontrenderer {
	private final UnicodeFont unicodefont;
	private final float antiAliasingFactor;
	private final float fontSize;
	
	public Fontrenderer(Font font, float fontSize, float antiAliasingFactor, String chars) {
		this.antiAliasingFactor = antiAliasingFactor;
		this.fontSize = fontSize;
		unicodefont = new UnicodeFont(font.deriveFont(fontSize * antiAliasingFactor));
		//unicodefont.addAsciiGlyphs();
		unicodefont.addGlyphs(chars);
		unicodefont.getEffects().add(new ColorEffect(Color.white));
		//unicodefont.addGlyphs(chars);
		try {
			unicodefont.loadGlyphs();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void drawString(String text, float x, float y, int color) {
		if(text == null) {
			return;
		}
		GL11.glPushMatrix();
		GL11.glPushAttrib(1048575);
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		
		GL11.glScaled(0.5F, 0.5F, 0.5F);
		//unicodefont.addGlyphs(text);
		//try {
			//unicodefont.loadGlyphs();
		//}catch(Exception e) {	
		//}
		unicodefont.drawString(x, y, text, new org.newdawn.slick.Color(color));
		
		/*boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
		boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
		if(!blend)
			GL11.glEnable(GL11.GL_BLEND);
		if(lighting)
			GL11.glDisable(GL11.GL_LIGHTING);
		if(texture)
			GL11.glDisable(GL11.GL_TEXTURE_2D);*/
		
		
		//GL11.glEnd();
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
	public void drawStringWithShadows(String text, int x, int y, float depth, int color) {
		drawString(StringUtils.stripControlCodes(text), x + depth, y + depth, 0x000000);
		drawString(text, x, y, color);
	}
	
	public static Font getFontByName(String name) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, Management.class.getResourceAsStream("/assets/minecraft/" + Management.instance.CLIENT_NAME.toLowerCase() + "/fonts/" + name + ".ttf"));
		}catch(FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public int gerStringHeight(String text) {
		return unicodefont.getHeight(text) / 2;
	}
	public int getStringWidth(String text) {
		return unicodefont.getWidth(text);
	}
	public int getSpaceWidth() {
		return unicodefont.getSpaceWidth();
	}
	public int getStringWidth2(String text) {
		return (int) (unicodefont.getWidth(text) / 2);
	}
	public int getBaseStringHeight() {
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		return unicodefont.getHeight(alphabet) / 2;
	}

}
