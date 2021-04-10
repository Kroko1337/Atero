package de.verschwiegener.atero.ui.clickgui.component.components;

import java.awt.Color;
import java.text.DecimalFormat;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.component.Component;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.util.MathHelper;

public class ComponentSlider extends Component {
    private boolean selected;
    private final Fontrenderer fontRenderer;

    private final DecimalFormat df = new DecimalFormat("0.0");

    public ComponentSlider(String name, int y, PanelExtendet pe) {
	super(name, y, pe);
	this.fontRenderer = Management.instance.fontrenderer;
    }

    @Override
    public void drawComponent(int x, int y) {
	super.drawComponent(x, y);
	if (!isParentextendet()) {
	    double percent = (getItem().getCurrentValue() - getItem().getMinValue())
		    / (getItem().getMaxValue() - getItem().getMinValue());
	    fontRenderer.drawString(getName(), (getComponentX() + 3) * 2, getComponentY() * 2 - getPanelExtendet().getPanel().getPanelYOffset(), Color.white.getRGB());
	    fontRenderer.drawString(String.valueOf(df.format(getItem().getCurrentValue())),(getPanelExtendet().getPanel().getX() + (getPanelExtendet().getWidth() * 2)) * 2 - 40, (getComponentY() * 2) - getPanelExtendet().getPanel().getPanelYOffset(), Color.WHITE.getRGB());
	    RenderUtil.fillRect(getComponentX() + 1, (getComponentY()) + 5, getPanelExtendet().getWidth(), 1.5D,
		    Management.instance.colorGray);
	    RenderUtil.fillRect(getComponentX() + 1, (getComponentY()) + 5, percent * getPanelExtendet().getWidth(),
		    1.5D, Management.instance.colorBlue);
	}
	if (selected) {
	    if (isSliderHoveredNoneY(x, y)) {
		getItem().setCurrentValue(getSliderValue(x, y));
	    }
	}
    }

    public void onMouseClicked(int x, int y, int button) {
	super.onMouseClicked(x, y, button);
	if (button == 0) {
	    if (isSliderHovered(x, y)) {
		selected = true;
	    }
	}
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int state) {
	super.onMouseReleased(mouseX, mouseY, state);
	selected = false;
    }

    private boolean isSliderHoveredNoneY(int mouseX, int mouseY) {
	return mouseX > (getPanelExtendet().getPanel().getX() + 1)
		&& mouseX < (getPanelExtendet().getPanel().getX() + (getPanelExtendet().getWidth() * 2) + 1);
    }

    private float getSliderValue(int mouseX, int mouseY) {
	double dif = getItem().getMaxValue() - getItem().getMinValue();
	double x = (mouseX - (getComponentX() + 1));
	double value = getItem().getMinValue()
		+ MathHelper.clamp_double(x / getPanelExtendet().getWidth(), 0.0D, 1.0D) * dif;
	return (float) value;
    }

    private boolean isSliderHovered(int mouseX, int mouseY) {
	return mouseX > (getPanelExtendet().getPanel().getX() + 1)
		&& mouseX < (getPanelExtendet().getPanel().getX() + (getPanelExtendet().getWidth() * 2) + 1)
		&& mouseY > (getComponentY() + 4.5) && mouseY < (getComponentY() + 7);
    }

}
