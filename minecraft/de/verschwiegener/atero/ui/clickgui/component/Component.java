package de.verschwiegener.atero.ui.clickgui.component;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.settings.SettingsItem.Category;

public class Component {
	
	String name;
	int y;
	PanelExtendet pe;
	
	SettingsItem item, child;
	boolean parentextendet, parentvalid, valid;
	private boolean change;
	
	public Component(String name, int y, PanelExtendet pe) {
		this.name = name;
		this.y = y;
		this.pe = pe;
		item = Management.instance.settingsmgr.getSettingByName(pe.getName()).getItemByName(name);
		if(item.getChild() != null) {
		    child = Management.instance.settingsmgr.getSettingByName(pe.getName()).getItemByName(item.getChild());
			valid = true;
			if((child != null)) {
				parentvalid = true;
				change = true;
			}
		}
	}
	
	public String getName() {
		return name;
	}
	public int getY() {
		return y;
	}
	public int getComponentX() {
		return getPanelExtendet().getPanel().getX() + getPanelExtendet().getWidth();
	}
	public int getComponentY() {
		return getY() + getPanelExtendet().getPanel().getY() + getPanelExtendet().getY();
	}
	public PanelExtendet getPanelExtendet() {
		return pe;
	}
	public SettingsItem getItem() {
		return item;
	}
	public SettingsItem getChild() {
		return child;
	}
	
	public void drawComponent(int x, int y) {
	    if (parentvalid && isChange()) {
		setChange(false);
		if (item.isState()) {
		    Component c = pe.getComponentByName(child.getName());
		    if (c.parentextendet) {
			c.parentextendet = false;
			pe.extendPanelByItemName(child.getName());
			c.setValid(true);
		    }
		} else {
		    Component c = pe.getComponentByName(child.getName());
		    if (!c.parentextendet) {
			c.parentextendet = true;
			pe.collapsePanelByItemName(c.getName());
			c.setValid(false);
		    }
		}
	    }
	}
	public void onMouseClicked(int x, int y, int button) {}
	public void onMouseReleased(int mouseX, int mouseY, int state) {}
	public void onKeyTyped(char typedChar, int keyCode) {}
	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}
	public boolean isParentextendet() {
		return parentextendet;
	}
	public void setValid(boolean valid) {
	    this.valid = valid;
	}
	public boolean isValid() {
	    return valid;
	}

}
