package de.verschwiegener.atero.ui.clickgui.component;

import java.util.ArrayList;
import java.util.function.Predicate;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.Panel;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCombobox;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentSlider;

public class PanelExtendet {
	
	String name;
	Panel p;
	Design d;
	boolean animate, isEmpty;
	int state, animationX, height, width, y;
	
	ArrayList<Component> components = new ArrayList<>();
	
	public PanelExtendet(String ModuleName, int y, Panel p) {
		this.y = y;
		this.p = p;
		this.name = ModuleName;
		d = Management.instance.designmgr.getDesignByName(Management.instance.selectedDesign);
		state = 2;
		animate = true;
		width = 100;
		//height = 100;
		int yoffset = 8;
		if(Management.instance.settingsmgr.getSettingByName(ModuleName) != null && !Management.instance.settingsmgr.getSettingByName(ModuleName).getItems().isEmpty()) {
			for(SettingsItem si : Management.instance.settingsmgr.getSettingByName(ModuleName).getItems()) {
				System.out.println("Items");
				switch (si.getCategory()) {
				case Checkbox:
					components.add(new ComponentCheckBox(si.getName(), yoffset, this));
					yoffset += 13;
					break;
					
				case Combobox:
					components.add(new ComponentCombobox(si.getName(), yoffset, this));
					yoffset += 13;
					break;
					
				case Slider:
					components.add(new ComponentSlider(si.getName(), yoffset, this));
					yoffset += 15;
					break;
				}
			}
			height = yoffset - 4;
		}else {
			isEmpty = true;
		}
	}
	
	public void drawScreen(int x, int y) {
		if(!isEmpty) {
			d.drawPanelExtendet(this, x, y);
			if((getState() == 2 && !isAnimate()) && getPanel().getState() == 1) {
				for(Component c : components) {
					c.drawComponent(x, y);
				}
			}
		}
	}
	public void onMouseClicked(int x, int y, int mousebutton) {
		if(!isEmpty) {
			if((getState() == 2 && !isAnimate()) && getPanel().getState() == 1) {
				for(Component c : components) {
					c.onMouseClicked(x, y, mousebutton);
				}
			}
		}
	}
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		for(Component c : components) {
			c.onMouseReleased(mouseX, mouseY, state);
		}
	}
	public void extendPanelByItemName(String name) {
		int count = components.indexOf(getComponentByName(name));
		for(int i = count; i < components.size();i++) {
			try {
				if(count == 2) {
					Component c = components.get(i);
					if(c instanceof ComponentSlider) {
						c.y += 15;
					}else if(c instanceof ComponentCheckBox) {
						c.y += 15;
					}else if(c instanceof ComponentCombobox) {
						c.y += 15;
					}
				}else {
					Component c = components.get(i);
					if(c instanceof ComponentSlider) {
						c.y += 15;
					}else if(c instanceof ComponentCheckBox) {
						c.y += 12;
					}else if(c instanceof ComponentCombobox) {
						c.y += 12;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		Component c = getComponentByName(name);
		if(c instanceof ComponentCheckBox) {
			height += 12;
		}else if(c instanceof ComponentSlider) {
			height += 15;
		}else if(c instanceof ComponentCombobox) {
			height += 12;
		}
	}
	public void extendPanelByYOffset(int yoffset, String name) {
		int count = components.indexOf(getComponentByName(name));
		for(int i = count + 1; i < components.size();i++) {
			try {
				Component c = components.get(i);
				c.y += yoffset;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		height += yoffset;
	}
	public void collapsePanelByYOffse(int yoffset, String name) {
		int count = components.indexOf(getComponentByName(name));
		for(int i = count + 1; i < components.size();i++) {
			try {
				Component c = components.get(i);
				c.y -= yoffset;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		height -= yoffset;
	}
	public void collapsePanelByItemName(String name) {
		int count = components.indexOf(getComponentByName(name));
		for(int i = count; i < components.size();i++) {
			try {
				if(count == 2) {
					Component c = components.get(i);
					if(c instanceof ComponentSlider) {
						c.y -= 15;
					}else if (c instanceof ComponentCheckBox) {
						c.y -= 15;
					}else if (c instanceof ComponentCombobox) {
						c.y -= 15;
					}
				}else {
					Component c = components.get(i);
					if(c instanceof ComponentSlider) {
						c.y -= 15;
					}else if (c instanceof ComponentCheckBox) {
						c.y -= 12;
					}else if(c instanceof ComponentCombobox) {
						c.y -= 12;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		Component c = getComponentByName(name);
		if(c instanceof ComponentCheckBox) {
			height -= 12;
		}else if(c instanceof ComponentSlider) {
			height -= 15;
		}else if(c instanceof ComponentSlider) {
			height -= 12;
		}
	}
	public Component getComponentByName(final String name) {
		return components.stream().filter(new Predicate<Component>() {	
			@Override
			public boolean test(Component module) {
				return module.getName().equalsIgnoreCase(name);
			}
		}).findFirst().orElse(null);
	}
	public void switchState() {
		animate = true;
	}
	public boolean isAnimate() {
		return animate;
	}
	public void setAnimate(boolean animate) {
		this.animate = animate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public void setAnimationX(int animationX) {
		this.animationX = animationX;
	}
	public int getAnimationX() {
		return animationX;
	}
	public int getHeight() {
		return height;
	}
	public Panel getPanel() {
		return p;
	}
	public int getY() {
		return y;
	}
	public String getName() {
		return name;
	}
	public int getWidth() {
		return width;
	}

}
