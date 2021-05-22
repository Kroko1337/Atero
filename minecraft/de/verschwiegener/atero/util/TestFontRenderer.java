package de.verschwiegener.atero.util;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class TestFontRenderer {
    
    private void drawString(String text, int posX, int posY, Color color, TestFont font) {
	if(text == null)
	    return;
	//ynamicTexture texture = font.getFontTexture();
	GL11.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	GlStateManager.pushMatrix();
	GlStateManager.enableBlend();
	GlStateManager.enableTexture2D();
	GlStateManager.blendFunc(770, 771);
    }

}
