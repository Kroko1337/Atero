package de.verschwiegener.atero.ui.clickgui;

import java.awt.Color;
import java.util.ArrayList;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;
import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.render.RenderUtil;

public class ClickGUIPanel {

    private final String name;
    private int x, y, width, yOffset, animationHeight;
    private final int panelYOffset;
    private final ArrayList<ClickGUIButton> modules = new ArrayList<>();
    private final Fontrenderer fontRenderer;
    private boolean animate, candrag;
    private int state = 2;
    private double dragx, dragy;
    private PanelExtendet pExtendet;
    private final ArrayList<PanelExtendet> ePanels = new ArrayList<>();

    public ClickGUIPanel(final String name, final int x, final int y) {
	this.name = name;
	this.x = x;
	this.y = y;
	panelYOffset = 15;
	fontRenderer = Management.instance.fontmgr.getFontByName("Inter").getFontrenderer();
	yOffset = 15;
	animationHeight = yOffset;
	// Sets the base Width
	width = 100;
	for (final Module m : Management.instance.modulemgr.modules) {
	    if (m.getCategory().toString().equalsIgnoreCase(name)) {
		// passt die Panel Width an die Module Name Width an
		if (fontRenderer.getStringWidth(m.getName()) + 10 > width) {
		    width = fontRenderer.getStringWidth(m.getName());
		}
		modules.add(new ClickGUIButton(m.getName(), yOffset, this));
		ePanels.add(new PanelExtendet(m.getName(), yOffset, this));
		yOffset += panelYOffset;
	    }
	}
    }

    private void animateExtension(final PanelExtendet pExtendet) {
	new TimeUtils();
	Management.instance.EXECUTOR_SERVICE.submit(() -> {
	    while (pExtendet.isAnimate()) {
		// if (pExtendet.isAnimate()) {
		if (TimeUtils.hasReached(5)) {
		    TimeUtils.reset();
		    switch (pExtendet.getState()) {
		    case 1:
			pExtendet.setAnimationX(pExtendet.getAnimationX() + 1);
			if (pExtendet.getAnimationX() == width || pExtendet.getAnimationX() > width) {
			    pExtendet.setAnimate(false);
			    pExtendet.setAnimationX(width);
			    pExtendet.setState(2);
			}
			break;
		    case 2:
			pExtendet.setAnimationX(pExtendet.getAnimationX() - 1);
			if (pExtendet.getAnimationX() == 17 || pExtendet.getAnimationX() < 17) {
			    pExtendet.setAnimate(false);
			    pExtendet.setAnimationX(17);
			    pExtendet.setState(1);
			}
			break;
		    }
		}
		// }
	    }
	});
    }

    public void colapseAll() {
	for (final PanelExtendet pe : ePanels) {
	    pe.setState(2);
	    pe.setAnimate(true);
	}
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
	// Drags the panel
	if (candrag) {
	    x = (int) (dragx + mouseX);
	    y = (int) (dragy + mouseY);
	}
	// draws panel extendet
	for (final PanelExtendet pe : ePanels) {
	    pe.drawScreen(mouseX, mouseY);
	}

	/*
	 * if(isAnimate()) { switch (getState()) { case 1:
	 * setAnimationheight(getAnimationheight() - 1); if(getAnimationheight() == 15
	 * || getAnimationheight() < 15) { setAnimate(false); setAnimationheight(15);
	 * setState(2); } break; case 2: setAnimationheight(getAnimationheight() + 1);
	 * if(getAnimationheight() == -getYoffset() || getAnimationheight() >
	 * getYoffset()) { setAnimate(false); setAnimationheight(getYoffset());
	 * setState(1); } } }
	 */

	RenderUtil.fillRect(x, y, width, animationHeight, Management.instance.colorBlack);
	RenderUtil.fillRect(x, y + getPanelYOffset() - 1, width, 1, Management.instance.colorBlue);
	fontRenderer.drawString(name, x * 2 + width - fontRenderer.getStringWidth2(name), y * 2, Color.white.getRGB());
	if (state == 1) {
	    modules.forEach(ClickGUIButton::drawButton);
	}
    }

    public int getAnimationheight() {
	return animationHeight;
    }

    public ClickGUIButton getButtonByPosition(final ClickGUIPanel p, final int x, final int y) {
	return modules.stream().filter(module -> x > p.getX() && x < p.getX() + p.getWidth()
		&& y > p.getY() + module.getY() && y < p.getY() + module.getY() + getPanelYOffset()).findFirst()
		.orElse(null);
    }

    public ArrayList<ClickGUIButton> getModules() {
	return modules;
    }

    public String getName() {
	return name;
    }

    public PanelExtendet getPanelByModuleName(final String name) {
	return ePanels.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public int getPanelYOffset() {
	return panelYOffset;
    }

    public int getState() {
	return state;
    }

    public int getWidth() {
	return width;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public int getYoffset() {
	return yOffset;
    }

    public boolean isAnimate() {
	return animate;
    }

    private boolean isClickGUIPanelHovered(final int mouseX, final int mouseY) {
	return mouseX > x && mouseX < x + width && mouseY > y - 1 && mouseY < y + getPanelYOffset();
    }

    public void keyTyped(final char typedChar, final int keyCode) {
	ePanels.forEach(panel -> panel.onKeyTyped(typedChar, keyCode));
    }

    public void onMouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
	ePanels.forEach(panel -> panel.onMouseClicked(mouseX, mouseY, mouseButton));
	switch (mouseButton) {
	case 0:
	    final ClickGUIButton br = getButtonByPosition(this, mouseX, mouseY);
	    if (br != null) {
		if (state == 1) {
		    Management.instance.modulemgr.getModuleByName(br.getName()).toggle();
		}
	    } else if (isClickGUIPanelHovered(mouseX, mouseY)) {
		// Drags
		candrag = true;
		dragx = x - mouseX;
		dragy = y - mouseY;
	    }
	    break;
	case 1:
	    final ClickGUIButton bl = getButtonByPosition(this, mouseX, mouseY);
	    if (bl != null) {
		if (pExtendet != null && !pExtendet.getName().equalsIgnoreCase(bl.getName())) {
		    pExtendet.setState(2);
		    pExtendet.setAnimate(true);
		    animateExtension(pExtendet);
		}
		pExtendet = getPanelByModuleName(bl.getName());
		pExtendet.switchState();
		animateExtension(pExtendet);
	    }
	    break;
	}
    }

    public void onMouseReleased(final int mouseX, final int mouseY, final int state) {
	if (state == 0) {
	    candrag = false;
	}
	ePanels.forEach(panel -> panel.onMouseReleased(mouseX, mouseY, state));
    }

    public void setAnimate(final boolean animate) {
	this.animate = animate;
    }

    public void setAnimationheight(final int animationheight) {
	animationHeight = animationheight;
    }

    public void setState(final int state) {
	this.state = state;
    }

}
