package de.verschwiegener.atero.design.designs.atero;

import java.awt.Color;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.util.Util;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.module.ModuleManager;
import de.verschwiegener.atero.ui.clickgui.Button;
import de.verschwiegener.atero.ui.clickgui.Panel;

public class AteroDesign extends Design{

	public AteroDesign() {
		super("AteroDesign");
		int Atero;
	}
	
	@Override
	public void drawGUIIngame(GuiIngame guiIngame) {
		if(Management.instance.modulechange) {
			Management.instance.modulechange = false;
			Util.sortModuleList(Management.instance.mdlmgr.modules, Management.instance.fntmgr.getFontByName("Inter"));
		}
		int yoffset = 0;
		int xoffset = 10;
		Fontrenderer fr = Management.instance.fntmgr.getFontByName("Inter").getFontrenderer();
		ModuleManager mm = Management.instance.mdlmgr;
		ScaledResolution sr = new ScaledResolution(guiIngame.mc);
		for(int i = 0; i < Management.instance.mdlmgr.modules.size();i++) {
			if(Management.instance.mdlmgr.modules.get(i).isEnabled()) {
				fr.drawString(mm.modules.get(i).getName(), ((sr.getScaledWidth() * 2) - fr.getStringWidth(mm.modules.get(i).getName())) - xoffset, yoffset, Color.black.getRGB());
				yoffset = yoffset + fr.getBaseStringHeight();
			}
		}
		
	}

	@Override
	public void drawClickGuiPanel(Panel p) {
		if(p.isAnimate()) {
			switch (p.getState()) {
			case 1:
				p.setAnimationheight(p.getAnimationheight() - 1);
				if(p.getAnimationheight() == 15 || p.getAnimationheight() < 15) {
					p.setAnimate(false);
					p.setAnimationheight(15);
					p.setState(2);
				}
				break;
			case 2:
				p.setAnimationheight(p.getAnimationheight() + 1);
				if(p.getAnimationheight() == -p.getYoffset() || p.getAnimationheight() > p.getYoffset()) {
					p.setAnimate(false);
					p.setAnimationheight(p.getYoffset());
					p.setState(1);
				}
			}
		}
		
		if(p.isAnimateextension()) {
			switch (p.getStateextension()) {
			case 1:
				p.setExtensionX(p.getExtensionX() - 1);
				if(p.getExtensionX() == 0 || p.getExtensionX() < 0) {
					p.setAnimateextension(false);
					p.setExtensionX(0);
					p.setStateextension(2);
				}
				break;
			case 2:
				p.setExtensionX(p.getExtensionX() + 1);
				if(p.getExtensionX() == p.getExtensionXMax() || p.getExtensionX() > p.getExtensionXMax()) {
					p.setAnimateextension(false);
					p.setExtensionX(p.getExtensionXMax());
					p.setStateextension(1);
					
				}
				break;
			}
		}
		
		
		RenderUtil.drawRectRound((p.getX() * 2) + p.getExtensionX(), (p.getY()), p.getExtensionXMax(), 20, 5, Color.gray);
		
		RenderUtil.drawRectRound(p.getX(), p.getY(), p.getWidth(), p.getAnimationheight(), 5, Color.gray);
		p.getFr().drawString(p.getName(), (p.getX() * 2), (p.getY() * 2) - 20, Color.blue.getRGB());
		for(Button b : p.getModules()) {
			if(p.getState() == 1) {
				b.drawButton();
			}
		}
	}

	@Override
	public void drawClickGuiButton(Button b) {
		if(!b.getP().isAnimate() && !(b.getP().getState() == 2)) {
			if(!Management.instance.mdlmgr.getModuleByName(b.getName()).isEnabled()) {
				b.getP().getFr().drawString(b.getName(), (b.getP().getX() * 2), (b.getP().getY() * 2) + (b.getY() * 2) - 24, Color.blue.getRGB());
			}else {
				b.getP().getFr().drawString(b.getName(), (b.getP().getX() * 2), (b.getP().getY() * 2) + (b.getY() * 2) - 24, Color.BLACK.getRGB());
			}
		}

	}

}
