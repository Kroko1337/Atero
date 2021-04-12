package de.verschwiegener.atero.ui.clickgui;

import java.awt.Color;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.util.render.RenderUtil;

public class ClickGUIButton {

    private final String name;
    private final int y;
    private final ClickGUIPanel panel;
    private final Fontrenderer fontRenderer;

    public ClickGUIButton(String name, int y, ClickGUIPanel p) {
	this.name = name;
	this.y = y;
	this.panel = p;
	this.fontRenderer = Management.instance.fontrenderer;
    }

    public void drawButton() {
	if (!panel.isAnimate() && !(panel.getState() == 2)) {
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

}
