package de.verschwiegener.atero.ui.clickgui;

import java.awt.Color;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;

public class Button {
	
	String name;
	int y;
	Panel p;
	Design d;
	
	public Button(String name, int y, Panel p) {
		this.name = name;
		this.y = y;
		this.p = p;
		d = Management.instance.designmgr.getDesignByName(Management.instance.selectedDesign);
	}
	
	public void drawButton() {
		d.drawClickGuiButton(this);
	}
	
	public String getName() {
		return name;
	}
	public Panel getP() {
		return p;
	}
	public int getY() {
		return y;
	}
	
}
