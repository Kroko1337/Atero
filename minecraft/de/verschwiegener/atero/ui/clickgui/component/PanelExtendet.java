package de.verschwiegener.atero.ui.clickgui.component;

import java.util.ArrayList;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.ClickGUIPanel;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCombobox;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentSlider;
import de.verschwiegener.atero.util.render.RenderUtil;

public class PanelExtendet {

    private final String name;
    private final ClickGUIPanel panel;
    private boolean animate, isEmpty;
    private int state, animationX, height;
    private final int width;
    private final int y;

    ArrayList<Component> components = new ArrayList<>();

    public PanelExtendet(final String ModuleName, final int y, final ClickGUIPanel p) {
	this.y = y;
	panel = p;
	name = ModuleName;
	state = 1;
	animate = true;
	width = 100;
	int yoffset = 8;
	if (Management.instance.settingsmgr.getSettingByName(ModuleName) != null
		&& !Management.instance.settingsmgr.getSettingByName(ModuleName).getItems().isEmpty()) {
	    final int size = Management.instance.settingsmgr.getSettingByName(ModuleName).getItems().size();
	    for (int i = 0; i < size; i++) {
		final SettingsItem si = Management.instance.settingsmgr.getSettingByName(ModuleName).getItems().get(i);
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
	} else {
	    isEmpty = true;
	}
    }

    public void collapsePanelByItemName(final String name) {
	final Component component = getComponentByName(name);
	final int count = components.indexOf(component);
	int offset = 0;
	if (component instanceof ComponentSlider) {
	    offset = 15;
	}
	if (component instanceof ComponentCombobox) {
	    offset = 13;
	}
	if (component instanceof ComponentCheckBox) {
	    offset = 13;
	}
	for (int i = count; i < components.size(); i++) {
	    try {
		final Component c = components.get(i);
		c.y -= offset;
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	}

	height -= offset;
    }

    public void collapsePanelByYOffset(final int yoffset, final String name) {
	final int count = components.indexOf(getComponentByName(name));
	for (int i = count + 1; i < components.size(); i++) {
	    try {
		final Component c = components.get(i);
		c.y -= yoffset;
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	}
	height -= yoffset;
    }

    public void drawScreen(final int mouseX, final int mouseY) {
	if (!isEmpty) {
	   /*if (animate) {
		switch (state) {
		case 1:
		    setAnimationX(animationX + 1);
		    if (animationX == width || animationX > width) {
			animate = false;
			setAnimationX(width);
			setState(2);
		    }
		    break;
		case 2:
		    setAnimationX(animationX - 1);
		    if (animationX == 17 || animationX < 17) {
			animate = false;
			setAnimationX(17);
			setState(1);
		    }
		    break;
		}
	    }*/
	    if ((animate || state != 1) && panel.getState() == 1) {
		RenderUtil.fillRect(panel.getX() + panel.getWidth() + 1, panel.getY() + y, animationX, height,
			Management.instance.colorBlack);
	    }

	    if (getState() == 2 && !isAnimate() && getPanel().getState() == 1) {
		for (final Component c : components) {
		    c.drawComponent(mouseX, mouseY);
		}
	    }
	}
    }

    public void extendPanelByItemName(final String name) {
	final Component component = getComponentByName(name);
	final int count = components.indexOf(component);
	int offset = 0;
	if (component instanceof ComponentSlider) {
	    offset = 15;
	}
	if (component instanceof ComponentCombobox) {
	    offset = 13;
	}
	if (component instanceof ComponentCheckBox) {
	    offset = 13;
	}
	for (int i = count; i < components.size(); i++) {
	    try {
		final Component c = components.get(i);
		c.y += offset;
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	}

	height += offset;

    }

    public void extendPanelByYOffset(final int yoffset, final String name) {
	final int count = components.indexOf(getComponentByName(name));
	for (int i = count + 1; i < components.size(); i++) {
	    try {
		final Component c = components.get(i);
		c.y += yoffset;
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	}
	height += yoffset;
    }

    public int getAnimationX() {
	return animationX;
    }

    public Component getComponentByName(final String name) {
	return components.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public int getHeight() {
	return height;
    }

    public String getName() {
	return name;
    }

    public ClickGUIPanel getPanel() {
	return panel;
    }

    public int getState() {
	return state;
    }

    public int getWidth() {
	return width;
    }

    public int getY() {
	return y;
    }

    public boolean isAnimate() {
	return animate;
    }

    public void onMouseClicked(final int x, final int y, final int mousebutton) {
	if (!isEmpty && (getState() == 2 && !isAnimate() && getPanel().getState() == 1)) {
	    for (final Component c : components) {
		c.onMouseClicked(x, y, mousebutton);
	    }
	}
    }

    public void onMouseReleased(final int mouseX, final int mouseY, final int state) {
	for (final Component c : components) {
	    c.onMouseReleased(mouseX, mouseY, state);
	}
    }

    public void setAnimate(final boolean animate) {
	this.animate = animate;
    }

    public void setAnimationX(final int animationX) {
	this.animationX = animationX;
    }

    public void setState(final int state) {
	this.state = state;
    }

    public void switchState() {
	animate = true;
    }

}
