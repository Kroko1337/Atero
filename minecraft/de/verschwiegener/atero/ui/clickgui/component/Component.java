package de.verschwiegener.atero.ui.clickgui.component;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.settings.SettingsItem.Category;

public class Component {
	
	String name;
	int y;
	PanelExtendet pe;
	
	SettingsItem item, parent;
	boolean parentextendet, parentvalid;
	
	public Component(String name, int y, PanelExtendet pe) {
		this.name = name;
		this.y = y;
		this.pe = pe;
		item = Management.instance.settingsmgr.getSettingByName(pe.getName()).getItemByName(name);
		parent = Management.instance.settingsmgr.getSettingByName(pe.getName()).getItemByName(item.getParent());
		if((parent != null) && parent.getCategory() == Category.Checkbox) {
			parentvalid = true;
		}
		//TODO PanelExtend und PanelRetract adden
		//Hier nicht, da die anderen Components noch nicht in der ArrayList sind
		/*if((parent != null) && parent.getCategory() == Category.Checkbox) {
			if(parent.isState()) {
				System.out.println("Extend");
				pe.extendPanel(name);
				parentextendet = true;
			}else {
				//pe.collapsePanel(name);
				parentextendet = false;
			}
		}*/
	}
	
	public String getName() {
		return name;
	}
	public int getY() {
		return y;
	}
	public PanelExtendet getPanelExtendet() {
		return pe;
	}
	public SettingsItem getItem() {
		return item;
	}
	public SettingsItem getParent() {
		return parent;
	}
	
	public void drawComponent(int x, int y) {}
	public void onMouseClicked(int x, int y, int button) {}
	public void onMouseReleased(int mouseX, int mouseY, int state) {}

}
