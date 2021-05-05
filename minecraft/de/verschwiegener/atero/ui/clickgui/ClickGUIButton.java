package de.verschwiegener.atero.ui.clickgui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGUIButton {

    private final String name;
    private final int y;
    private final ClickGUIPanel panel;
    private final Fontrenderer fontRenderer;
    private boolean drawCircle;
    private int circleX, circleY;
    private int circleAnimationDiameter = 0;

    public ClickGUIButton(String name, int y, ClickGUIPanel p) {
	this.name = name;
	this.y = y;
	this.panel = p;
	this.fontRenderer = Management.instance.fontrenderer;
    }

    public void drawButton() {
	if (!panel.isAnimate() && !(panel.getState() == 2)) {
	    if(drawCircle) {
		    GL11.glEnable(GL11.GL_SCISSOR_TEST);
		    final ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
		    final int scaleFactor = scale.getScaleFactor();
		    GL11.glScissor((panel.getX()) * scaleFactor, (((panel.getY() + y + 15) * scaleFactor) - Minecraft.getMinecraft().displayHeight) /-1, panel.getWidth() * scaleFactor, 15 * scaleFactor);
		    RenderUtil.drawCircle(panel.getX() - circleX, (panel.getY() + y) - circleY, circleAnimationDiameter, new Color(48, 48, 48), true);
		    GL11.glDisable(GL11.GL_SCISSOR_TEST);
	    }
	    if (!Management.instance.modulemgr.getModuleByName(name).isEnabled()) {
		fontRenderer.drawString(name,
			(panel.getX() * 2) + (panel.getWidth() - (fontRenderer.getStringWidth(name) / 2)),
			(panel.getY() * 2) + (y * 2), Color.WHITE.getRGB());
	    } else {
		fontRenderer.drawString(name,
			(panel.getX() * 2) + (panel.getWidth() - (fontRenderer.getStringWidth(name) / 2)),
			(panel.getY() * 2) + (y * 2), Management.instance.colorBlue.getRGB());
	    }
	    if(Management.instance.clickgui.getSettingByName(name) != null && Management.instance.clickgui.isHasSearched()) {
		 RenderUtil.drawRect(panel.getX() + 1, panel.getY() + y + 1, panel.getWidth() - 2, panel.getPanelYOffset() - 2, Management.instance.clickgui.getColorSearch(), 0.5F);
	    }
	}
    }
    public String getName() {
	return name;
    }
    public int getY() {
	return y;
    }
    public void setCircleAnimationDiameter(int circleAnimationDiameter) {
	this.circleAnimationDiameter = circleAnimationDiameter;
    }
    public void setDrawCircle(boolean drawCircle) {
	this.drawCircle = drawCircle;
    }
    public boolean isDrawCircle() {
	return drawCircle;
    }
    public void setCircleX(int circleX) {
	this.circleX = circleX;
    }
    public void setCircleY(int circleY) {
	this.circleY = circleY;
    }

}
