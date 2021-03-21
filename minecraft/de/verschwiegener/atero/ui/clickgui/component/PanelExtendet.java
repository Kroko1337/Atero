package de.verschwiegener.atero.ui.clickgui.component;

import java.util.ArrayList;
import java.util.function.Predicate;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.Panel;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;
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
		int yoffset = -3;
		if(Management.instance.settingsmgr.getSettingByName(ModuleName) != null && !Management.instance.settingsmgr.getSettingByName(ModuleName).getItems().isEmpty()) {
			for(SettingsItem si : Management.instance.settingsmgr.getSettingByName(ModuleName).getItems()) {
				System.out.println("Items");
				switch (si.getCategory()) {
				case Checkbox:
					yoffset += 12;
					components.add(new ComponentCheckBox(si.getName(), yoffset, this));
					break;
					
				case Combobox:
					
					break;
					
				case Slider:
					yoffset += 12;
					components.add(new ComponentSlider(si.getName(), yoffset, this));
					break;
				}
			}
			height = yoffset + 9;
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
	public void extendPanel(String name) {
		int count = components.indexOf(getComponentByName(name));
		System.out.println("Count: " + count + " Size: " + components.size());
		for(int i = count; i < components.size();i++) {
			try {
				Component c = components.get(i);
				c.y += 12;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void collapsePanel(String name) {
		System.out.println("Comp: " + name);
		int count = components.indexOf(getComponentByName(name));
		System.out.println("Count: " + count + " Size: " + components.size());
		for(int i = count + 1; i < components.size();i++) {
			try {
				Component c = components.get(i);
				c.y -= 12;
			}catch(Exception e) {
				e.printStackTrace();
			}
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
