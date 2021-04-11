package de.verschwiegener.atero.ui.multiplayer;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.ServerSelectionList;

public class CustomGUISlotRenderer {

    private int selectedEntryIndex = 0;
    private Fontrenderer fontRenderer;
    private final GuiMultiplayer guiMultiplayer;

    public CustomGUISlotRenderer(GuiMultiplayer guiMultiplayer) {
	fontRenderer = Management.instance.fontrenderer;
	this.guiMultiplayer = guiMultiplayer;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	
	for (int i = 0; i < guiMultiplayer.getServerListSelector().getServerListNormal().size(); i++) {
	    if (mouseY > (40 * i + 20) && mouseY < (40 * i + 53)) {
		if (mouseX > ((guiMultiplayer.width / 2) - 150) && mouseX < ((guiMultiplayer.width / 2) + 150)) {
		    selectedEntryIndex = i;
		    break;
		}
	    }
	}
	
	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	fontRenderer.drawString("Multiplayer",
		((guiMultiplayer.width / 2) * 2) - fontRenderer.getStringWidth2("Multiplayer"), 10,
		Color.white.getRGB());
	for (int i = 0; i < guiMultiplayer.getServerListSelector().getServerListNormal().size(); i++) {
	    ServerListEntryNormal entry = guiMultiplayer.getServerListSelector().getServerListNormal().get(i);
	    entry.drawEntry(i, (guiMultiplayer.width / 2) - 150, 40 * i + 20, 300, 20, mouseX, mouseY,
		    (i == selectedEntryIndex && isHovered(mouseX, mouseY) && !guiMultiplayer.isCustomGui()) ? true : false, (!guiMultiplayer.isCustomGui()) ? true: false);

	}
    }

    public void handleMouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (isHovered(mouseX, mouseY)) {
	    int l = mouseX - ((guiMultiplayer.width / 2) - 150);
	    int i1 = mouseY - (40 * selectedEntryIndex + 20);
	    guiMultiplayer.getServerListSelector().getServerListNormal().get(selectedEntryIndex)
		    .mousePressed(selectedEntryIndex, mouseX, mouseY, mouseButton, l, i1);
	}
    }
    
    public void selectServer(int slot) {
	this.selectedEntryIndex = slot;
    }
    private boolean isHovered(int mouseX, int mouseY) {
	for (int i = 0; i < guiMultiplayer.getServerListSelector().getServerListNormal().size(); i++) {
	    if (mouseY > (40 * i + 20) && mouseY < (40 * i + 53)) {
		if (mouseX > ((guiMultiplayer.width / 2) - 150) && mouseX < ((guiMultiplayer.width / 2) + 150)) {
		    return true;
		}
	    }
	}
	return false;
    }
}
