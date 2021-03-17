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
import de.verschwiegener.atero.ui.clickgui.component.Component;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;

public class AteroDesign extends Design {
	
	Fontrenderer frArrayList;
	Fontrenderer frText;

	public AteroDesign() {
		super("AteroDesign");
		int Atero;
		frArrayList = Management.instance.fontmgr.getFontByName("ArrayListFont").getFontrenderer();
		frText = Management.instance.fontmgr.getFontByName("Inter").getFontrenderer();
	}
	
	@Override
	public void drawGUIIngame(GuiIngame guiIngame) {
		if(Management.instance.modulechange) {
			Management.instance.modulechange = false;
			Util.sortModuleList(Management.instance.modulemgr.modules, Management.instance.fontmgr.getFontByName("ArrayListFont"));
		}
		int yoffset = 0;
		int xoffset = 10;
		ModuleManager mm = Management.instance.modulemgr;
		ScaledResolution sr = new ScaledResolution(guiIngame.mc);
		for(int i = 0; i < Management.instance.modulemgr.modules.size();i++) {
			if(Management.instance.modulemgr.modules.get(i).isEnabled()) {
				frArrayList.drawString(mm.modules.get(i).getName(), ((sr.getScaledWidth() * 2) - frArrayList.getStringWidth(mm.modules.get(i).getName())) - xoffset, yoffset, Color.black.getRGB());
				yoffset = yoffset + frArrayList.getBaseStringHeight();
			}
		}
		
	}

	@Override
	public void drawClickGuiPanel(Panel p) {
		//Animates the extension
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
		
		RenderUtil.drawRectRound(p.getX(), p.getY(), p.getWidth(), p.getAnimationheight(), 5, Color.gray);
		frText.drawString(p.getName(), (p.getX() * 2), (p.getY() * 2) - 20, Color.blue.getRGB());
		for(Button b : p.getModules()) {
			if(p.getState() == 1) {
				b.drawButton();
			}
		}
	}

	@Override
	public void drawClickGuiButton(Button b) {
		if(!b.getP().isAnimate() && !(b.getP().getState() == 2)) {
			if(!Management.instance.modulemgr.getModuleByName(b.getName()).isEnabled()) {
				frText.drawString(b.getName(), (b.getP().getX() * 2), (b.getP().getY() * 2) + (b.getY() * 2) - 24, Color.blue.getRGB());
			}else {
				frText.drawString(b.getName(), (b.getP().getX() * 2), (b.getP().getY() * 2) + (b.getY() * 2) - 24, Color.BLACK.getRGB());
			}
		}

	}

	@Override
	public void drawPanelExtendet(PanelExtendet p, int x, int y) {
		//Animates the extension
		if(p.isAnimate()) {
			switch (p.getState()) {
			case 1:
				p.setAnimationX(p.getAnimationX() + 1);
				if(p.getAnimationX() == p.getWidth() || p.getAnimationX() > p.getWidth()) {
					p.setAnimate(false);
					p.setAnimationX(p.getWidth());
					p.setState(2);
				}
				break;
			case 2:
				p.setAnimationX(p.getAnimationX() - 1);
				if(p.getAnimationX() == 17 || p.getAnimationX() < 17) {
					p.setAnimate(false);
					p.setAnimationX(17);
					p.setState(1);
				}
				break;
			}
		}
		if((p.isAnimate() || p.getState() != 1) && p.getP().getState() == 1) {
			RenderUtil.drawRectRound(p.getP().getX() + p.getP().getWidth() - 10, p.getP().getY() + p.getY() - 2, p.getAnimationX(), p.getHeight(), 5, Color.gray);
		}
	}

	@Override
	public void drawCheckbox(ComponentCheckBox c, int y) {
		frText.drawString(c.getName(), (c.getPe().getP().getX() + c.getPe().getWidth() - 10) * 2, (c.getYPos() * 2) - 5, Color.BLUE.getRGB());
		if(Management.instance.settingsmgr.getSettingByName(c.getPe().getName()).getItemByName(c.getName()).isState()) {
			//RenderUtil.fillRect(c.getXPos() - 40, c.getYPos(), 10, 10, Color.BLUE);
			RenderUtil.fillCircle(c.getXPos() - 35, c.getYPos() + 5, 10, true, Color.BLUE);
		}
		RenderUtil.drawStringCircle(c.getXPos() - 35, c.getYPos() + 5, 10, false, Color.black);
		//RenderUtil.drawRectRound(c.getXPos() - 40,  c.getYPos(), 10, 10, 1, Color.black);
		//RenderUtil.drawRect(c.getXPos() - 40, c.getYPos(), 10, 10, Color.black, 1F);
	}

}
