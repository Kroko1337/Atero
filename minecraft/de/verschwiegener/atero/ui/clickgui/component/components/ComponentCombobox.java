package de.verschwiegener.atero.ui.clickgui.component.components;

import java.awt.Color;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.ui.clickgui.component.Component;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;
import de.verschwiegener.atero.util.render.RenderUtil;

public class ComponentCombobox extends Component {

    private boolean extendet;
    private final Fontrenderer fontRenderer;

    public ComponentCombobox(String name, int y, PanelExtendet pe) {
	super(name, y, pe);
	extendet = false;
	fontRenderer = Management.instance.fontrenderer;
    }

    @Override
    public void drawComponent(int x, int y) {
	super.drawComponent(x, y);
	if (!isParentextendet()) {
	    int textx = (getComponentX() + 1);
	    int texty = (getComponentY()) - 6;
	    fontRenderer.drawString(getName(), (textx * 2) + (getPanelExtendet().getWidth() - fontRenderer.getStringWidth2(getName())), texty * 2, Color.WHITE.getRGB());
	    RenderUtil.fillRect(textx,
		    getComponentY() + 6, getPanelExtendet().getWidth(), 1, Management.instance.colorBlue);
	    if (extendet) {
		texty += 2;
		for (String str : getItem().getModes()) {
		    texty += 12;
		    // frText.drawString(str, (textx + 2) * 2, texty * 2, Color.WHITE.getRGB());
		    //System.out.println("Current: " + getItem().getCurrent());
		    if (str.equals(getItem().getCurrent())) {
			// RenderUtil.fillRect((ccb.getPanelExtendet().getPanel().getX() +
			// ccb.getPanelExtendet().getWidth() + 2), (ccb.getY() +
			// ccb.getPanelExtendet().getPanel().getY() + ccb.getPanelExtendet().getY()) +
			// 9, 1, 11, colorBlue);
			fontRenderer.drawString(str, (textx + 2) * 2 + (getPanelExtendet().getWidth() - (fontRenderer.getStringWidth2(str))), texty * 2,
				Management.instance.colorBlue.getRGB());
		    } else {
			fontRenderer.drawString(str, (textx + 2) * 2 + (getPanelExtendet().getWidth() - (fontRenderer.getStringWidth2(str))), texty * 2, Color.WHITE.getRGB());
		    }
		}
	    }
	}
    }

    @Override
    public void onMouseClicked(int x, int y, int button) {
	super.onMouseClicked(x, y, button);
	if (button == 0) {
	    if (extendet) {
		String value = getComboboxItem(x, y);
		if(value != null) {
		    setParentChange(true);
		    getItem().setCurrent(getComboboxItem(x, y));
		}
	    }
	}else if(button == 1) {
	    if (isComboboxHovered(x, y)) {
		extendet = !extendet;
		if (extendet) {
		    getPanelExtendet().extendPanelByYOffset(12 * getItem().getModes().size(), getName());
		} else {
		    getPanelExtendet().collapsePanelByYOffset(12 * getItem().getModes().size(), getName());
		}
	    }
	}
    }
    public void collapse() {
	if(extendet) {
	    extendet = false;
	    getPanelExtendet().collapsePanelByYOffset(12 * getItem().getModes().size(), getName());
	}
    }

    public void onMouseReleased(int mouseX, int mouseY, int state) {
	super.onMouseReleased(mouseX, mouseY, state);
    }

    public boolean isExtendet() {
	return extendet;
    }

    public boolean isComboboxHovered(int mouseX, int mouseY) {
	return mouseX > (getComponentX())
		&& mouseX < (getComponentX() + 1 + getPanelExtendet().getWidth())
		&& mouseY > (getComponentY() - 6) && mouseY < (getComponentY() + 7);
    }

    private String getComboboxItem(int mouseX, int mouseY) {
	int modeY = 2;
	for (String str : getItem().getModes()) {
	    modeY += 12;
	    if (mouseX > getComponentX() && mouseX < (getComponentX() + getPanelExtendet().getWidth() + 1)) {
		if (mouseY > (getComponentY() + 7) && mouseY < (getComponentY() + 7 + modeY)) {
		    return str;
		}
	    }
	}
	return null;
    }

}
