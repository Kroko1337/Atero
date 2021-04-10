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
		child = Management.instance.settingsmgr.getSettingByName(pe.getName()).getItemByName(item.getChild());
		valid = true;
		if((child != null)) {
			parentvalid = true;
			change = true;
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
		if(parentvalid && isChange()) {
		    System.out.println("Change");
			setChange(false);
			if(item.isState()) {
			    System.out.println("extend1");
				Component c = pe.getComponentByName(child.getName());
				if(c.parentextendet) {
					c.parentextendet = false;
					 System.out.println("extend");
					pe.extendPanelByItemName(child.getName());
					c.setValid(true);
				}
			}else {
			    System.out.println("Collapse1");
				Component c = pe.getComponentByName(child.getName());
				if(!c.parentextendet) {
				    System.out.println("Collapse");
					c.parentextendet = true;
					pe.collapsePanelByItemName(c.getName());
					c.setValid(false);
				}
			}
		}
	}
	public void onMouseClicked(int x, int y, int button) {}
	public void onMouseReleased(int mouseX, int mouseY, int state) {}

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
