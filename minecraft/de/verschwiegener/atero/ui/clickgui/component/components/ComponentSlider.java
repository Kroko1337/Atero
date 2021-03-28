package de.verschwiegener.atero.ui.clickgui.component.components;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.component.Component;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;

public class ComponentSlider extends Component{
	Design d;
	boolean selected;

	public ComponentSlider(String name, int y, PanelExtendet pe) {
		super(name, y, pe);
		d = Management.instance.currentDesign;
	}
	@Override
	public void drawComponent(int x, int y) {
		super.drawComponent(x, y);
		d.drawSlider(this, getY());
		if (selected) {
			if (d.isSliderHoveredNoneY(x, y, this)) {  
				getItem().setCurrentValue(d.getSliderValue(x, y, this));
			}
		}
	}
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
		if(button == 0) {
			if (d.isSliderHovered(x, y, this)) {
				selected = true;
			}
		}
	}
	@Override
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		super.onMouseReleased(mouseX, mouseY, state);
		selected = false;
	}

}
