package de.verschwiegener.atero.design;

import de.verschwiegener.atero.ui.clickgui.Button;
import de.verschwiegener.atero.ui.clickgui.Panel;
import de.verschwiegener.atero.ui.clickgui.component.Component;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCombobox;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentSlider;
import net.minecraft.client.gui.GuiIngame;

public abstract class Design {
	
	String name;
	int clickGuiPanelYOffset;
	Panel clickGUIPanel;
	
	
	public Design(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void drawGUIIngame(GuiIngame guiIngame) {}
	public void drawTabUI() {}
	
	public void drawClickGuiPanel(Panel p) {}
	public abstract boolean isClickGUIPanelHovered(int mouseX, int mouseY, Panel panel);
	public void drawClickGuiModuleButton(Button b) {}
	public void drawPanelExtendet(PanelExtendet p, int x, int y) {}
	public void drawCheckbox(ComponentCheckBox c, int y) {}
	public abstract boolean isCheckboxHovered(int mouseX, int mouseY, ComponentCheckBox checkbox);
	
	public void drawSlider(ComponentSlider c, int y) {}
	public abstract boolean isSliderHovered(int mouseX, int mouseY, ComponentSlider slider);
	public abstract boolean isSliderHoveredNoneY(int mouseX, int mouseY, ComponentSlider slider);
	public abstract float getSliderValue(int mouseX, int mouseY, ComponentSlider slider);
	
	public void drawCombobox(ComponentCombobox ccb, int y) {}
	public abstract boolean isComboboxHovered(int mouseX, int mouseY, ComponentCombobox ccb);
	public abstract String getComboboxItem(int mouseX, int mouseY, ComponentCombobox ccb);
	
	public int getClickGuiPanelYOffset() {
		return clickGuiPanelYOffset;
	}
	
	public void setClickGuiPanelYOffset(int clickGuiPanelYOffset) {
		this.clickGuiPanelYOffset = clickGuiPanelYOffset;
	}

}
