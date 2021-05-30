package de.verschwiegener.atero.util.chat;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.opengl.GL11;

import de.verschwiegener.atero.util.GlyphMetrics;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.StringUtils;

public class TestFontRenderer {
    
    public static void drawString(String text, int posX, int posY, Color color, ChatFont font) {
	if(text == null || text == "")
	    return;
	GlStateManager.pushMatrix();
	boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
	GlStateManager.enableBlend();
	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	GlStateManager.enableTexture2D();
	GlStateManager.disableLighting();
	GL11.glColor4d((color.getRed() / 255.0F), (color.getGreen() / 255.0F), (color.getBlue() / 255.0F),
		(color.getAlpha() / 255.0F));
	GL11.glScaled(0.5D, 0.5D, 0.5D);
	float x = posX;
	float y = posY;
	
	long startTime = 0;
	long stopTime= 0;
	char[] characters = text.toCharArray();
	
	//for(int i = 0; i < text.length();i++) {
	startTime = System.currentTimeMillis();
	for(char glyph : characters) {
	    //char glyph = text.charAt(i);
	    //char glyph = c;
	    //startTime = System.currentTimeMillis();
	    //if (font.glyphMap.containsKey(glyph)) {
	    if(font.glyphMap.length > glyph) {
		//GlyphMetrics metric = font.glyphMap.get(glyph);
		GlyphMetrics metric = font.glyphMap[glyph];
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, metric.getGLTextureID());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, 10240, 9729);
		GL11.glTexParameteri(3553, 10240, 9729);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		// Draw texture, see ShaderRenderer
		double height = metric.getHeight();
		double width = metric.getWidth();
		GL11.glBegin(7);
		GL11.glTexCoord2d(0.0D, 0.0D);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2d(0.0D, 1.0D);
		GL11.glVertex2d(x, y + height);
		GL11.glTexCoord2d(1.0D, 1.0D);
		GL11.glVertex2d(x + width, y + height);
		GL11.glTexCoord2d(1.0D, 0.0D);
		GL11.glVertex2d(x + width, y);
		GL11.glEnd();
		x += width - 8;
	    } else {
		// draw with mc fontRenderer
	    }
	}
	//TODO Performace improven
	//CustomChatTextField zieht auch gut wegen getStringWith und drawString
	stopTime = System.currentTimeMillis();
	System.out.println("Dif: " + (startTime - stopTime));
	
	if (!blend) {
	    GlStateManager.disableBlend();
	}
	GlStateManager.bindTexture(0);
	GlStateManager.popMatrix();
	GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
    }
    public static int getStringWidthClean(String text, ChatFont font) {
	int width = 0;
	text = StringUtils.stripControlCodes(text);
	for(int i = 0; i < text.length();i++) {
	    char glyph = text.charAt(i);
	    //if(font.glyphMap.containsKey(glyph)) {
		//width += font.glyphMap.get(glyph).getWidth();
	    //}
	}
	return width;
    }
    public static int getStringWidth(String text, ChatFont font) {
	int width = 0;
	for(int i = 0; i < text.length();i++) {
	    char glyph = text.charAt(i);
	    //if(font.glyphMap.containsKey(glyph)) {
		//width += font.glyphMap.get(glyph).getWidth();
	    //}
	}
	return width;
    }

}
