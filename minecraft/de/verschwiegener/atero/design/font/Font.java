package de.verschwiegener.atero.design.font;

import java.awt.Color;

public class Font {
	
	String name;
	Fontrenderer fontrenderer;
	
	public Font(String name, java.awt.Font font, float fontsize, float antiAliasing, String chars) {
		this.name = name;
		this.fontrenderer = new Fontrenderer(font, fontsize, antiAliasing, chars, false, false);
	}
	
	public Font(String name, java.awt.Font font, float fontsize, float antiAliasing, String chars, boolean bold, boolean italic) {
		this.name = name;
		this.fontrenderer = new Fontrenderer(font, fontsize, antiAliasing, chars, bold, italic);
	}
	
	public String getName() {
		return name;
	}
	public Fontrenderer getFontrenderer() {
		return fontrenderer;
	}
	
	public void drawString(String text, int x, int y,Color color) {
		fontrenderer.drawString(text, x, y, color.getRGB());
	}
	public void drawString(String text, int x, int y, int color) {
		fontrenderer.drawString(text, x, y, color);
	}
	public void drawStringWithShadow(String text, int x, int y, int depth, Color color) {
		fontrenderer.drawStringWithShadows(text, x, y, depth, color.getRGB());;
	}

}
