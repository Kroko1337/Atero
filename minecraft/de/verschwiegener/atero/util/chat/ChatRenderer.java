package de.verschwiegener.atero.util.chat;

import java.awt.Color;
import java.util.HashMap;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;

public class ChatRenderer {
	
	HashMap<String, Color> colorCodes = new HashMap<>();
	Fontrenderer fr;
	String chars = "0123456789abcdef";
	
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
		fr = Management.instance.fntmgr.getFontByName("InterChat").getFontrenderer();
	}
	
	public void drawChat(String line, int x, int y) {
		String[] colorargs = line.replace("§", "#§").split("#");
		int xoffset = 0;
		for(int i = 0; i < colorargs.length;i++) {
			String args = colorargs[i];
			if(args.startsWith("§")) {
				if(!illegalchars.contains((args.substring(1,2).toLowerCase()))){
					Color c = colorCodes.get(args.substring(1,2).toLowerCase());
					fr.drawString(args.substring(2), x + xoffset, y, c.getRGB());
					xoffset += fr.getStringWidth(args.substring(2));
				}else {
					Color c = new Color(255, 255, 255);
					fr.drawString(args.substring(2), x + xoffset, y, c.getRGB());
					xoffset += fr.getStringWidth(args.substring(2));
				}
			
			}else {
				Color c = new Color(255, 255, 255);
				fr.drawString(args, x + xoffset, y, c.getRGB());
				xoffset += fr.getStringWidth(args);
			}
			
		}
	}

}
