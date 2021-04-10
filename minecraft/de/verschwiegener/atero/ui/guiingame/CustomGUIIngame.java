package de.verschwiegener.atero.ui.guiingame;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.audio.Stream;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.module.ModuleManager;
import de.verschwiegener.atero.ui.audio.AudioPanel;
import de.verschwiegener.atero.util.Util;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class CustomGUIIngame {

    public static void drawArrayList() {
	Fontrenderer fontRenderer = Management.instance.fontmgr.getFontByName("ArrayListFont").getFontrenderer();
	if (Management.instance.modulechange) {
	    Management.instance.modulechange = false;
	    Util.sortModuleList(Management.instance.modulemgr.modules,
		    Management.instance.fontmgr.getFontByName("ArrayListFont"));
	}
	int yoffset = 0;
	int xoffset = 10;
	ModuleManager mm = Management.instance.modulemgr;
	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	for (int i = 0; i < Management.instance.modulemgr.modules.size(); i++) {
	    if (Management.instance.modulemgr.modules.get(i).isEnabled()) {
		fontRenderer.drawString(mm.modules.get(i).getName(), ((sr.getScaledWidth() * 2) - fontRenderer.getStringWidth(mm.modules.get(i).getName()))- xoffset,
			yoffset, Color.black.getRGB());
		yoffset += fontRenderer.getBaseStringHeight() * 2;
	    }
	}
    }
    public static void drawMusikTab() {
	Stream stream = Management.instance.currentStream;
	if(stream != null) {
	    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	    final int scaleFactor = sr.getScaleFactor();
	    Fontrenderer fontRenderer = Management.instance.fontrendererBold;
	    drawImage(sr.getScaledWidth() - 54, sr.getScaledHeight() - 54, 50, 50, stream);
	    fontRenderer.drawString(stream.getFulltitle(), (sr.getScaledWidth() - 54) * 2 - (fontRenderer.getStringWidth(stream.getFulltitle())) - 5, (sr.getScaledHeight() - 54) * 2, Management.instance.colorGray.getRGB());
	    fontRenderer.drawString(stream.getArtist(), (sr.getScaledWidth() - 54) * 2 - (fontRenderer.getStringWidth(stream.getArtist())) - 5, (sr.getScaledHeight() - 54) * 2 + (fontRenderer.getBaseStringHeight() * 2), Management.instance.colorGray.getRGB());
	}
    }
    private static void drawImage(final int xPos, final int yPos, final int width, final int height, Stream stream) {
   	Minecraft.getMinecraft().getTextureManager().deleteTexture(stream.getLocation());
   	stream.setTexture(new DynamicTexture(stream.getImage()));
   	stream.setLocation(Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(stream.getChannelName(), stream.getTexture()));
   	stream.getTexture().updateDynamicTexture();
   	Minecraft.getMinecraft().getTextureManager().bindTexture(stream.getLocation());
   	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0.0F, 0.0F, width, height, width, height);
       }

}