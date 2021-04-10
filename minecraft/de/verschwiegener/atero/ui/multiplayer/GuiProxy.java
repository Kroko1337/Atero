package de.verschwiegener.atero.ui.multiplayer;

import java.awt.Color;
import java.util.ArrayList;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.ui.mainmenu.CustomGuiButton;
import de.verschwiegener.atero.ui.mainmenu.CustomGuiTextField;
import de.verschwiegener.atero.util.chat.CustomChatTextField;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;

public class GuiProxy {
    
    Fontrenderer fontRenderer;
    
    public GuiProxy() {
	fontRenderer = Management.instance.fontrenderer;
    }
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	int x = (sr.getScaledWidth() / 2 ) - 100;
	int y = (sr.getScaledHeight() / 2) - 50;
	
	RenderUtil.drawRectRound(x, y, 200, 100,5, Management.instance.colorBlack);
    
	fontRenderer.drawString("Proxy", ((sr.getScaledWidth() / 2) * 2) - fontRenderer.getStringWidth2("Proxy"), (((sr.getScaledHeight() / 2) - 50) * 2) + (fontRenderer.getBaseStringHeight() / 2) - 2, Color.WHITE.getRGB());
	
	fontRenderer.drawString("IP:", x * 2 + 3, (y + 20) * 2, Color.WHITE.getRGB());
	RenderUtil.fillRect(x + fontRenderer.getStringWidth2("Port: ") + 3, y + 19, 100, 16, Management.instance.colorGray);
	
	fontRenderer.drawString("Port: ", x * 2 + 3, (y + 40) * 2, Color.WHITE.getRGB());
	RenderUtil.fillRect(x + fontRenderer.getStringWidth2("Port: ") + 3, y + 39, 100, 16, Management.instance.colorGray);
    }
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	
    }
    public void handleKeyboardInput(char typedChar, int keycode) {
	
    }
   
}
