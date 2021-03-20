package de.verschwiegener.atero.ui.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;
import de.verschwiegener.atero.util.render.RenderUtil;

public class Panel {
	
	String name;
	int x, y, width, yOffset, animationHeight, extensionX;
	ArrayList<Button> modules = new ArrayList<>();
	Fontrenderer fr;
	boolean animate, candrag;
	int state = 2;
	double dragx, dragy;
	PanelExtendet pExtendet;
	ArrayList<PanelExtendet> ePanels = new ArrayList<>();
	
	Design d;
	
	public Panel(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		d = Management.instance.designmgr.getDesignByName(Management.instance.selectedDesign);
		fr = Management.instance.fontmgr.getFontByName("Inter").getFontrenderer();
		yOffset = d.getClickGuiPanelYOffset();
		animationHeight = yOffset;
		//Sets the base Width
		width = 100;
		for(Module m : Management.instance.modulemgr.modules) {
			if(m.getCategory().toString().equalsIgnoreCase(name)) {
				//passt die Panel Width an die Module Name Width an
				if((fr.getStringWidth(m.getName()) + 10) > width) {
					width = fr.getStringWidth(m.getName());
				}
				modules.add(new Button(m.getName(), yOffset, this));
				ePanels.add(new PanelExtendet(m.getName(), yOffset, this));
				yOffset += 15;
			}
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//Drags the panel
		if(candrag) {
			this.x = (int) (this.dragx + mouseX);
			this.y = (int) (this.dragy + mouseY);
		}
		//draws panel extendet
		for(PanelExtendet pe : ePanels) {
			pe.drawScreen(mouseX, mouseY);
		}
		//draws the panel
		d.drawClickGuiPanel(this);
	}
	
	public void onMouseClicked(int x, int y, int mouseButton) {
		for(PanelExtendet pe : ePanels) {
			pe.onMouseClicked(x, y, mouseButton);
		}
		switch (mouseButton) {
		case 0:
			Button br = getButtonByPosition(this, x, y);
			if(br != null) {
				System.out.println("Toggle");
				//Toggles the module
				if(state == 1) {
					Management.instance.modulemgr.getModuleByName(br.name).toggle();
				}
			}else if(d.isClickGUIPanelHovered(x, y, this)) {
				//Drags
				candrag = true;
				dragx = this.x - x;
				dragy = this.y - y;
			}
			break;
		case 1:
			Button bl = getButtonByPosition(this, x, y);
			if(bl != null) {
				if(pExtendet != null && !pExtendet.getName().equalsIgnoreCase(bl.getName())) {
					pExtendet.setState(2);
					pExtendet.setAnimate(true);
				}
				pExtendet = getPanelByModuleName(bl.getName());
				pExtendet.switchState();
			}
			break;
		}
	}
	
	public void onMouseReleased(int x, int y, int state) {
		if(state == 0){
			candrag = false;
		}
		for(PanelExtendet pe: ePanels) {
			pe.onMouseReleased(x, y, state);
		}
	}
	
	public Button getButtonByPosition(final Panel p, final int x, final int y) {
		return modules.stream().filter(new Predicate<Button>() {	
			@Override
			public boolean test(Button module) {
				return x > (p.getX()) && x < (p.getX() + p.getWidth()) && y > (p.getY() + module.getY()) && y < (p.getY() + module.getY() + p.d.getClickGuiPanelYOffset());
			}
		}).findFirst().orElse(null);
	}
	
	public void colapseAll() {
		for(PanelExtendet pe : ePanels) {
			pe.setState(2);
			pe.setAnimate(true);
		}
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getYoffset() {
		return yOffset;
	}

	public int getAnimationheight() {
		return animationHeight;
	}

	public ArrayList<Button> getModules() {
		return modules;
	}

	public boolean isAnimate() {
		return animate;
	}

	public int getState() {
		return state;
	}
	public void setAnimationheight(int animationheight) {
		this.animationHeight = animationheight;
	}
	
	public void setAnimate(boolean animate) {
		this.animate = animate;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	public Design getD() {
		return d;
	}
	
	public PanelExtendet getPanelByModuleName(final String name) {
		return ePanels.stream().filter(new Predicate<PanelExtendet>() {
			@Override
			public boolean test(PanelExtendet module) {
				return module.getName().equalsIgnoreCase(name);
			}
		}).findFirst().orElse(null);
	}

}
