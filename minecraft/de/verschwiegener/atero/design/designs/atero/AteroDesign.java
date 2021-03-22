package de.verschwiegener.atero.design.designs.atero;

import java.awt.Color;
import java.text.DecimalFormat;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.util.Util;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.module.ModuleManager;
import de.verschwiegener.atero.settings.SettingsItem.Category;
import de.verschwiegener.atero.ui.clickgui.Button;
import de.verschwiegener.atero.ui.clickgui.Panel;
import de.verschwiegener.atero.ui.clickgui.component.Component;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCombobox;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentSlider;

public class AteroDesign extends Design {
	
	Fontrenderer frArrayList;
	Fontrenderer frText;
	
	Color colorBlue, colorBlack, colorGray;
	private static DecimalFormat df = new DecimalFormat("0.0");

	public AteroDesign() {
		super("AteroDesign");
		int Atero;
		frArrayList = Management.instance.fontmgr.getFontByName("ArrayListFont").getFontrenderer();
		frText = Management.instance.fontmgr.getFontByName("Inter").getFontrenderer();
		setClickGuiPanelYOffset(15);
		colorBlack = new Color(28, 28, 28);
		colorBlue = new Color(0, 161, 249);
		colorGray = new Color(45, 45, 45);
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
		
		RenderUtil.fillRect(p.getX(), p.getY(), p.getWidth(), p.getAnimationheight(), colorBlack);
		RenderUtil.fillRect(p.getX(), p.getY() + getClickGuiPanelYOffset() - 1, p.getWidth(), 1, colorBlue);
		frText.drawString(p.getName(), (p.getX() * 2) + (p.getWidth() - (frText.getStringWidth(p.getName()) / 2)), (p.getY() * 2), Color.white.getRGB());
		for(Button b : p.getModules()) {
			if(p.getState() == 1) {
				b.drawButton();
			}
		}
	}
	
	@Override
	public boolean isClickGUIPanelHovered(int mouseX, int mouseY, Panel panel) {
		return mouseX > (panel.getX()) && mouseX < (panel.getX() + panel.getWidth()) && mouseY > (panel.getY() - 1) && mouseY < (panel.getY() + getClickGuiPanelYOffset());
	}

	@Override
	public void drawClickGuiModuleButton(Button b) {
		if(!b.getPanel().isAnimate() && !(b.getPanel().getState() == 2)) {
			if(!Management.instance.modulemgr.getModuleByName(b.getName()).isEnabled()) {
				frText.drawString(b.getName(), (b.getPanel().getX() * 2) + (b.getPanel().getWidth() - (frText.getStringWidth(b.getName()) / 2)), (b.getPanel().getY() * 2) + (b.getY() * 2), Color.WHITE.getRGB());
			}else {
				frText.drawString(b.getName(), (b.getPanel().getX() * 2) + (b.getPanel().getWidth() - (frText.getStringWidth(b.getName()) / 2)), (b.getPanel().getY() * 2) + (b.getY() * 2), colorBlue.getRGB());
			}
		}

	}

	@Override
	public void drawPanelExtendet(PanelExtendet panelExtendet, int x, int y) {
		if(panelExtendet.isAnimate()) {
			switch (panelExtendet.getState()) {
			case 1:
				panelExtendet.setAnimationX(panelExtendet.getAnimationX() + 1);
				if(panelExtendet.getAnimationX() == panelExtendet.getWidth() || panelExtendet.getAnimationX() > panelExtendet.getWidth()) {
					panelExtendet.setAnimate(false);
					panelExtendet.setAnimationX(panelExtendet.getWidth());
					panelExtendet.setState(2);
				}
				break;
			case 2:
				panelExtendet.setAnimationX(panelExtendet.getAnimationX() - 1);
				if(panelExtendet.getAnimationX() == 17 || panelExtendet.getAnimationX() < 17) {
					panelExtendet.setAnimate(false);
					panelExtendet.setAnimationX(17);
					panelExtendet.setState(1);
				}
				break;
			}
		}
		if((panelExtendet.isAnimate() || panelExtendet.getState() != 1) && panelExtendet.getPanel().getState() == 1) {
			RenderUtil.fillRect(panelExtendet.getPanel().getX() + panelExtendet.getPanel().getWidth() + 1, panelExtendet.getPanel().getY() + panelExtendet.getY(), panelExtendet.getAnimationX(), panelExtendet.getHeight(), colorBlack);
		}
	}

	@Override
	public void drawCheckbox(ComponentCheckBox c, int y) {
		if(!c.isParentextendet()) {
			frText.drawString(c.getName(),
					(c.getPanelExtendet().getPanel().getX() + c.getPanelExtendet().getWidth() + 3) * 2,
					(c.getYPos()) * 2 - c.getPanelExtendet().getPanel().getD().getClickGuiPanelYOffset(),
					Color.white.getRGB());
			if (Management.instance.settingsmgr.getSettingByName(c.getPanelExtendet().getName()).getItemByName(c.getName())
					.isState()) {
				RenderUtil.fillRect(c.getXPos() - 12, c.getYPos() - 5, 9, 9, colorBlue);
			} else {
				RenderUtil.fillRect(c.getXPos() - 12, c.getYPos() - 5, 9, 9, colorGray);
			}
		}
	}
	@Override
	public boolean isCheckboxHovered(int mouseX, int mouseY, ComponentCheckBox checkbox) {
		return mouseX > (checkbox.getXPos() - 15) && mouseX < (checkbox.getXPos() - 3) && mouseY > (checkbox.getYPos() - 7) && mouseY < (checkbox.getYPos() + 4);
	}

	@Override
	public void drawSlider(ComponentSlider c, int y) {
		if(!c.isParentextendet()) {
			double percent = (c.getItem().getCurrentValue() - c.getItem().getMinValue()) / (c.getItem().getMaxValue() - c.getItem().getMinValue());
			frText.drawString(c.getName(), (c.getPanelExtendet().getPanel().getX() + c.getPanelExtendet().getWidth() + 3) * 2, (c.getY() + c.getPanelExtendet().getPanel().getY() + c.getPanelExtendet().getY()) * 2 - c.getPanelExtendet().getPanel().getD().getClickGuiPanelYOffset(), Color.white.getRGB());
			double value = Math.round(c.getItem().getCurrentValue() * 100) / 10;
			frText.drawString(String.valueOf(df.format(c.getItem().getCurrentValue())), (c.getPanelExtendet().getPanel().getX() + (c.getPanelExtendet().getWidth() * 2)) * 2 - 40, (c.getY() + c.getPanelExtendet().getPanel().getY() + c.getPanelExtendet().getY()) * 2 - c.getPanelExtendet().getPanel().getD().getClickGuiPanelYOffset(), Color.WHITE.getRGB());
			RenderUtil.fillRect((c.getPanelExtendet().getPanel().getX() + c.getPanelExtendet().getWidth() + 1), (c.getY() + c.getPanelExtendet().getPanel().getY() + c.getPanelExtendet().getY()) + 5, c.getPanelExtendet().getWidth(), 1.5D, colorGray);
			RenderUtil.fillRect((c.getPanelExtendet().getPanel().getX() + c.getPanelExtendet().getWidth() + 1), (c.getY() + c.getPanelExtendet().getPanel().getY() + c.getPanelExtendet().getY()) + 5, percent * c.getPanelExtendet().getWidth(), 1.5D, colorBlue);
		}
	}
	
	@Override
	public boolean isSliderHovered(int mouseX, int mouseY, ComponentSlider slider) {
		return mouseX > (slider.getPanelExtendet().getPanel().getX() + 1) && mouseX < (slider.getPanelExtendet().getPanel().getX() + (slider.getPanelExtendet().getWidth() * 2) + 1) && mouseY > (slider.getY() + slider.getPanelExtendet().getY() + slider.getPanelExtendet().getPanel().getY() + 4.5) && mouseY < (slider.getY() + slider.getPanelExtendet().getY() + slider.getPanelExtendet().getPanel().getY() + 7);
	}

	@Override
	public float getSliderValue(int mouseX, int mouseY, ComponentSlider slider) {
		double dif = slider.getItem().getMaxValue() - slider.getItem().getMinValue();
		double x = (mouseX - (slider.getPanelExtendet().getPanel().getX() + slider.getPanelExtendet().getWidth() + 1));
		double value = slider.getItem().getMinValue() + MathHelper.clamp_double(x / slider.getPanelExtendet().getWidth(), 0.0D, 1.0D) * dif;
		return (float) value;
	}

	@Override
	public boolean isSliderHoveredNoneY(int mouseX, int mouseY, ComponentSlider slider) {
		return mouseX > (slider.getPanelExtendet().getPanel().getX() + 1) && mouseX < (slider.getPanelExtendet().getPanel().getX() + (slider.getPanelExtendet().getWidth() * 2) + 1);
	}
	
	@Override
	public void drawCombobox(ComponentCombobox ccb, int y) {
		if(!ccb.isParentextendet()) {
			int textx = (ccb.getPanelExtendet().getPanel().getX() + ccb.getPanelExtendet().getWidth() + 1);
			int texty = (ccb.getY() + ccb.getPanelExtendet().getPanel().getY() + ccb.getPanelExtendet().getY()) - 6;
			frText.drawString(ccb.getName(), (textx + (frText.getStringWidth(ccb.getName()) / 4)) * 2, texty * 2, colorGray.WHITE.getRGB());
			RenderUtil.fillRect((ccb.getPanelExtendet().getPanel().getX() + ccb.getPanelExtendet().getWidth() + 1), (ccb.getY() + ccb.getPanelExtendet().getPanel().getY() + ccb.getPanelExtendet().getY()) + 6, ccb.getPanelExtendet().getWidth(), 1, colorBlue);
			if(ccb.isExtendet()) {
				texty += 2;
				for(String str : ccb.getItem().getModes()) {
					texty += 12;
					//frText.drawString(str, (textx + 2) * 2, texty * 2, Color.WHITE.getRGB());
					if(str == ccb.getItem().getCurrent()) {
						//RenderUtil.fillRect((ccb.getPanelExtendet().getPanel().getX() + ccb.getPanelExtendet().getWidth() + 2), (ccb.getY() + ccb.getPanelExtendet().getPanel().getY() + ccb.getPanelExtendet().getY()) + 9, 1, 11, colorBlue);
						frText.drawString(str, (textx + 2) * 2, texty * 2, colorBlue.getRGB());
					}else {
						frText.drawString(str, (textx + 2) * 2, texty * 2, Color.WHITE.getRGB());
					}
				}
			}
		}
	}

	@Override
	public boolean isComboboxHovered(int mouseX, int mouseY, ComponentCombobox ccb) {
		return mouseX > (ccb.getPanelExtendet().getPanel().getX() + 1) && mouseX < (ccb.getPanelExtendet().getPanel().getX() + (ccb.getPanelExtendet().getWidth() * 2) + 1)
				&& mouseY > (ccb.getY() + ccb.getPanelExtendet().getPanel().getY() + ccb.getPanelExtendet().getY() - 6) && mouseY < (ccb.getY() + ccb.getPanelExtendet().getPanel().getY() + ccb.getPanelExtendet().getY() + 7);
	}
	//TODO isComboboxModesHoveres

}
