package de.verschwiegener.atero.util.chat;

import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ChatRenderer {

    HashMap<String, Color> colorCodes = new HashMap<>();
    Fontrenderer fontRenderer, fontRendererBold, fontRendererItalic;

    Color messagecolor;
    boolean bold, italic, underline, strikethrough, noise, effekt;
    Random rnd = new Random();

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
	colorCodes.putIfAbsent("g",  new Color(0, 161, 249));
	fontRenderer = Management.instance.fontmgr.getFontByName("InterChat").getFontrenderer();
	fontRendererBold = Management.instance.fontmgr.getFontByName("InterChatBold").getFontrenderer();
	fontRendererItalic = Management.instance.fontmgr.getFontByName("InterChatItalic").getFontrenderer();
	messagecolor = Color.white;
    }

    public void drawchat2(String line, int x, int y) {
	String[] args = line.replace("§", "#§").split("#");
	int xoffset = 0;
	messagecolor = Color.white;
	bold = false;
	italic = false;
	underline = false;
	strikethrough = false;
	noise = false;
	effekt = false;
	for (String str : args) {
	    if (str.length() > 1) {
		if (colorCodes.containsKey(str.substring(1, 2))) {
		    messagecolor = colorCodes.get(str.substring(1, 2).toLowerCase());
		} else {
		    //System.out.println("Case: " + str.substring(1, 2));
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
		if (effekt) {
		    if (bold) {
			if (str.startsWith("§")) {
			    fontRendererBold.drawString(str.substring(2), x + xoffset, y, messagecolor.getRGB());
			    xoffset += fontRendererBold.getStringWidth(str.substring(2));
			} else {
			    fontRendererBold.drawString(str, x + xoffset, y, messagecolor.getRGB());
			    xoffset += fontRendererBold.getStringWidth(str);
			}
		    } else if (italic) {
			if (str.startsWith("§")) {
			    fontRendererItalic.drawString(str.substring(2), x + xoffset, y, messagecolor.getRed());
			    xoffset += fontRendererItalic.getStringWidth(str.substring(2));
			} else {
			    fontRendererItalic.drawString(str, x + xoffset, y, messagecolor.getRed());
			    xoffset += fontRendererItalic.getStringWidth(str);
			}
		    } else if (noise) {
			fontRenderer.drawString(getRandomString(str.length() - 2), x + xoffset, y, messagecolor.getRGB());
			xoffset += fontRenderer.getStringWidth(str);
		    }
		    if (underline) {
			
			//drawLine(x + xoffset, y + (fontRenderer.getBaseStringHeight() * 2) - 3, fontRenderer.getStringWidth2(str.substring(2)), messagecolor);
			//fontRenderer.drawString(str.substring(2), x + xoffset, y, messagecolor.getRGB());
		    }
		    if (strikethrough) {
			//drawLine(x + xoffset, y + (fontRenderer.getBaseStringHeight()), fontRenderer.getStringWidth(str.substring(2)), messagecolor);
			//System.out.println("Str: " + str.substring(2));
			//fontRenderer.drawString(str.substring(2), x + xoffset, y, messagecolor.getRGB());
		    }
		} else {
		    if (str.startsWith("§")) {
			fontRenderer.drawString(str.substring(2), x + xoffset, y, messagecolor.getRGB());
			xoffset += fontRenderer.getStringWidth(str.substring(2));
		    } else {
			fontRenderer.drawString(str, x + xoffset, y, messagecolor.getRGB());
			xoffset += fontRenderer.getStringWidth(str);
		    }
		}
	    }
	}
    }

    private void drawLine(int x, int y, int width, Color color) {
	RenderUtil.enable();
	GL11.glScaled(0.5D, 0.5D, 0.5D);
	GL11.glBegin(GL11.GL_LINES);
	GL11.glVertex2f(x, y);
	GL11.glVertex2f(x + width, y);
	//GL11.glEnd();
	RenderUtil.disable();
	
	
	/*Tessellator tessellator1 = Tessellator.getInstance();
        WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
        GlStateManager.disableTexture2D();
        worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
        int l = -1;
        worldrenderer1.pos((double)(x + (float)l), (double)(y + (float)10), 0.0D).endVertex();
        worldrenderer1.pos((double)(x), (double)(y + (float)10), 0.0D).endVertex();
        worldrenderer1.pos((double)(x), (double)(y + (float)10 - 1.0F), 0.0D).endVertex();
        worldrenderer1.pos((double)(x + (float)l), (double)(y + (float)10 - 1.0F), 0.0D).endVertex();
        tessellator1.draw();
        GlStateManager.enableTexture2D();*/
	
    }

    private String getRandomString(int count) {
	String str = "";
	for (int i = 0; i < count; i++) {
	    str += (char) (rnd.nextInt(26) + 'a');
	}
	return str;
    }

}
