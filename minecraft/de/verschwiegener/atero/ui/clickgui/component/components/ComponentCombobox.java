package de.verschwiegener.atero.ui.clickgui.component.components;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.ui.clickgui.component.Component;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;

public class ComponentCombobox extends Component {
	
	Design d;
	boolean extendet;

	public ComponentCombobox(String name, int y, PanelExtendet pe) {
		super(name, y, pe);
		d = Management.instance.currentDesign;
		extendet = false;
	}
	
	@Override
	public void drawComponent(int x, int y) {
		super.drawComponent(x, y);
		d.drawCombobox(this, this.getY());
	}
	@Override
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
		if (button == 0) {
			if (d.isComboboxHovered(x, y, this)) {
				extendet = !extendet;
				if (extendet) {
					getPanelExtendet().extendPanelByYOffset(12 * getItem().getModes().size(), getName());
				} else {
					getPanelExtendet().collapsePanelByYOffse(12 * getItem().getModes().size(), getName());
				}
			}else if (extendet) {
				getItem().setCurrent(d.getComboboxItem(x, y, this));
			}
		}
	}
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		super.onMouseReleased(mouseX, mouseY, state);
	}
	public boolean isExtendet() {
		return extendet;
	}

}
