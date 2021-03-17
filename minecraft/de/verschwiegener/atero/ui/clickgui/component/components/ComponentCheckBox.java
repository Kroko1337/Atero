package de.verschwiegener.atero.ui.clickgui.component.components;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
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
			if(isHovered(x, y)) {
				Management.instance.settingsmgr.getSettingByName(getPe().getName()).getItemByName(getName()).toggleState();
			}
		}
	}
	public int getXPos() {
		return getPe().getP().getX() + (getPe().getWidth() * 2);
	}
	public int getYPos() {
		return getPe().getP().getY() + getY();
	}
	
	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX > (getXPos() - 40) && mouseX < (getXPos() - 30) && mouseY > getYPos() && mouseY < getYPos() + 10;
	}
	
}
