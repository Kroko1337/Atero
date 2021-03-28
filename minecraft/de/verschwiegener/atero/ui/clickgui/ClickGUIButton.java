package de.verschwiegener.atero.ui.clickgui;

import java.awt.Color;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;

public class ClickGUIButton {
	
	String name;
	int y;
	ClickGUIPanel p;
	Design d;
	
	public ClickGUIButton(String name, int y, ClickGUIPanel p) {
		this.name = name;
		this.y = y;
		this.p = p;
		d = Management.instance.designmgr.getDesignByName(Management.instance.selectedDesign);
	}
	
	public void drawButton() {
		d.drawClickGuiModuleButton(this);
	}
	
	public String getName() {
		return name;
	}
	public ClickGUIPanel getPanel() {
		return p;
	}
	public int getY() {
		return y;
	}
	
}
