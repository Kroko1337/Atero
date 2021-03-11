package de.verschwiegener.atero.design;

import de.verschwiegener.atero.ui.clickgui.Button;
import de.verschwiegener.atero.ui.clickgui.Panel;
import net.minecraft.client.gui.GuiIngame;

public abstract class Design {
	
	String name;
	
	public Design(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public abstract void drawGUIIngame(GuiIngame guiIngame);
	public void drawTabUI() {}
	
	public abstract void drawClickGuiPanel(Panel p);
	public abstract void drawClickGuiButton(Button b);

}
