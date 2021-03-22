package de.verschwiegener.atero.ui.clickgui.component.components;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.component.Component;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;

public class ComponentCheckBox extends Component {
	
	Design d;

	public ComponentCheckBox(String name, int y, PanelExtendet pe) {
		super(name, y, pe);
		d = Management.instance.designmgr.getDesignByName(Management.instance.selectedDesign);
	}
	
	
	@Override
	public void drawComponent(int x, int y) {
		super.drawComponent(x, y);
		d.drawCheckbox(this, getY());
	}
	@Override
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
		if(button == 0) {
			if(d.isCheckboxHovered(x, y, this)) {
				setChange(true);
				Management.instance.settingsmgr.getSettingByName(getPanelExtendet().getName()).getItemByName(getName()).toggleState();
			}
		}
	}
	public int getXPos() {
		return getPanelExtendet().getPanel().getX() + getPanelExtendet().getAnimationX() + getPanelExtendet().getWidth();
	}
	public int getYPos() {
		return getPanelExtendet().getPanel().getY() + getPanelExtendet().getY() + getY();
	}
	
}
