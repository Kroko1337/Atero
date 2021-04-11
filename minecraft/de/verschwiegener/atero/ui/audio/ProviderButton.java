package de.verschwiegener.atero.ui.audio;

import java.awt.Color;

import de.verschwiegener.atero.Management;

public class ProviderButton {
    
    String name;
    int y;
    
    public ProviderButton(String name, int y) {
	this.name = name;
	this.y = y;
    }
    
    public void drawButton(AudioPanel panel, int x, int y) {
	if(panel.getCurrentProvider() != null && panel.getCurrentProvider().toString() == name) {
	    Management.instance.fontrenderer.drawString(name, (x * 2) + ((panel.getWidth() / 5) - Management.instance.fontrenderer.getStringWidth2(name)), (y + this.y) * 2, Management.instance.colorBlue.getRGB());
	}else {
	    Management.instance.fontrenderer.drawString(name, (x * 2) + ((panel.getWidth() / 5) - Management.instance.fontrenderer.getStringWidth2(name)), (y + this.y) * 2, Color.WHITE.getRGB());
	}
    }

}
