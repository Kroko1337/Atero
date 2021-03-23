package de.verschwiegener.atero.util.chat;

import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;

public class ChatRenderer {
	
	HashMap<String, Color> colorCodes = new HashMap<>();
	Fontrenderer fr, frbold, fritalic;
	String colorcodes = "0123456789abcdef";
	Color c = new Color(255, 255, 255);
	Color messagecolor;
	boolean bold, italic, underline, strikethrough, noise, effekt;
	Random rnd = new Random();
	String illegalchars = "n";
	
	public ChatRenderer() {
		colorCodes.putIfAbsent("0", new Color(0, 0, 0));
		colorCodes.putIfAbsent("1", new Color(0, 0, 170));
		colorCodes.putIfAbsent("2", new Color(0, 170, 0));
		colorCodes.putIfAbsent("3", new Color(0, 170, 170));
		colorCodes.putIfAbsent("4", new Color(170, 0, 0));
		colorCodes.putIfAbsent("5", new Color(170, 0, 170));
		colorCodes.putIfAbsent("6", new Color(255, 170, 0));
		colorCodes.putIfAbsent("7", new Color(170, 170, 170));
		colorCodes.putIfAbsent("8", new Color(85, 85, 85));
		colorCodes.putIfAbsent("9", new Color(85, 85, 255));
		colorCodes.putIfAbsent("a", new Color(85, 255, 85));
		colorCodes.putIfAbsent("b", new Color(85, 255, 255));
		colorCodes.putIfAbsent("c", new Color(255, 85, 85));
		colorCodes.putIfAbsent("d", new Color(255, 85, 255));
		colorCodes.putIfAbsent("e", new Color(255, 255, 85));
		colorCodes.putIfAbsent("f", new Color(255, 255, 255));
		fr = Management.instance.fontmgr.getFontByName("InterChat").getFontrenderer();
		frbold = Management.instance.fontmgr.getFontByName("InterChatBold").getFontrenderer();
		fritalic = Management.instance.fontmgr.getFontByName("InterChatItalic").getFontrenderer();
		messagecolor = Color.white;
	}
	
	
	public void drawchat2(String line, int x, int y) {
		//System.out.println("Line: " + line);
		String[] args = line.replace("§", "#§").split("#");
		int xoffset = 0;
		messagecolor = Color.white;
		bold = false;
		italic = false;
		underline = false;
		strikethrough = false;
		noise = false;
		effekt = false;
		for(String str : args) {
			if(str.length() > 0) {
				if(colorcodes.contains(str.substring(1, 2))) {
					messagecolor = colorCodes.get(str.substring(1,2).toLowerCase());
				}else {
					switch (str.substring(1, 2)) {
					case "k":
						effekt = true;
						noise = true;
						break;
					case "m":
						effekt = true;
						strikethrough = true;
						break;
					case "o":
						effekt = true;
						italic = true;
						bold = false;
						break;
					case "l":
						effekt = true;
						bold = true;
						italic = false;
						break;
					case "n":
						effekt = true;
						underline = true;
						break;
					case "r":
						messagecolor = Color.white;
						bold = false;
						italic = false;
						underline = false;
						strikethrough = false;
						noise = false;
						effekt = false;
						break;
					}
				}
				if(effekt) {
					if(bold) {
						if(str.startsWith("§")) {
							frbold.drawString(str.substring(2), x + xoffset, y, messagecolor.getRGB());
							xoffset += frbold.getStringWidth(str.substring(2));
						}else {
							frbold.drawString(str, x + xoffset, y, messagecolor.getRGB());
							xoffset += frbold.getStringWidth(str);
						}
					}else if(italic) {
						if(str.startsWith("§")) {
							fritalic.drawString(str.substring(2), x + xoffset, y, messagecolor.getRed());
							xoffset += fritalic.getStringWidth(str.substring(2));
						}else {
							fritalic.drawString(str, x + xoffset, y, messagecolor.getRed());
							xoffset += fritalic.getStringWidth(str);
						}
					}else if (noise) {
						fr.drawString(getrandomString(str.length() - 2), x + xoffset, y, messagecolor.getRGB());
						xoffset += fr.getStringWidth(str);
					}
					if(underline) {
						drawLine(x + xoffset, y + 1, fr.getStringWidth(str.substring(2)));
					}
					if(strikethrough) {
						drawLine(x + xoffset, y - 2, fr.getStringWidth(str.substring(2)));
					}
				}else {
					if(str.startsWith("§")) {
						fr.drawString(str.substring(2), x + xoffset, y, messagecolor.getRGB());
						xoffset += fr.getStringWidth(str.substring(2));
					}else {
						fr.drawString(str, x + xoffset, y, messagecolor.getRGB());
						xoffset += fr.getStringWidth(str);
					}
				}
			}else {
			}
		}
	}
	public void drawLine(int x, int y, int width) {
		GL11.glScaled(0.5f, 0.5f, 0.5f);
		GL11.glLineWidth(2F);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x + width, y);
		GL11.glEnd();
	}
	
	public String getrandomString(int count) {
		String str = "";
		for(int i = 0; i < count;i++) {
			str += (char) (rnd.nextInt(26) + 'a');
		}
		return str;
	}

}
