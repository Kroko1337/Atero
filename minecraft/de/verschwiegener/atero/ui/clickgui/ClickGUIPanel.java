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
import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.render.RenderUtil;

public class ClickGUIPanel {
	
	private final String name;
	private int x, y, width, yOffset, animationHeight, extensionX, panelYOffset;
	private ArrayList<ClickGUIButton> modules = new ArrayList<>();
	private Fontrenderer fontRenderer;
	private boolean animate, candrag;
	private int state = 2;
	private double dragx, dragy;
	private PanelExtendet pExtendet;
	private ArrayList<PanelExtendet> ePanels = new ArrayList<>();
	
	//Design d;
	
	public ClickGUIPanel(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		panelYOffset = 15;
		//d = Management.instance.designmgr.getDesignByName(Management.instance.selectedDesign);
		fontRenderer = Management.instance.fontmgr.getFontByName("Inter").getFontrenderer();
		yOffset = 15;
		animationHeight = yOffset;
		//Sets the base Width
		width = 100;
		for(Module m : Management.instance.modulemgr.modules) {
			if(m.getCategory().toString().equalsIgnoreCase(name)) {
				//passt die Panel Width an die Module Name Width an
				if((fontRenderer.getStringWidth(m.getName()) + 10) > width) {
					width = fontRenderer.getStringWidth(m.getName());
				}
				modules.add(new ClickGUIButton(m.getName(), yOffset, this));
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
		
		/*if(isAnimate()) {
			switch (getState()) {
			case 1:
				setAnimationheight(getAnimationheight() - 1);
				if(getAnimationheight() == 15 || getAnimationheight() < 15) {
					setAnimate(false);
					setAnimationheight(15);
					setState(2);
				}
				break;
			case 2:
				setAnimationheight(getAnimationheight() + 1);
				if(getAnimationheight() == -getYoffset() || getAnimationheight() > getYoffset()) {
					setAnimate(false);
					setAnimationheight(getYoffset());
					setState(1);
				}
			}
		}*/
		
		
		RenderUtil.fillRect(x, y, width, animationHeight, Management.instance.colorBlack);
		RenderUtil.fillRect(x, y + getPanelYOffset() - 1, width, 1, Management.instance.colorBlue);
		fontRenderer.drawString(name, (x * 2) + (width - (fontRenderer.getStringWidth2(name))), (y * 2), Color.white.getRGB());
		for(ClickGUIButton b : modules) {
			if(state == 1) {
				b.drawButton();
			}
		}
	}
	
	public void onMouseClicked(int x, int y, int mouseButton) {
		for(PanelExtendet pe : ePanels) {
			pe.onMouseClicked(x, y, mouseButton);
		}
		switch (mouseButton) {
		case 0:
			ClickGUIButton br = getButtonByPosition(this, x, y);
			if(br != null) {
				System.out.println("Toggle");
				//Toggles the module
				if(state == 1) {
					Management.instance.modulemgr.getModuleByName(br.getName()).toggle();
				}
			}else if(isClickGUIPanelHovered(x, y)) {
				//Drags
				candrag = true;
				dragx = this.x - x;
				dragy = this.y - y;
			}
			break;
		case 1:
			ClickGUIButton bl = getButtonByPosition(this, x, y);
			if(bl != null) {
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
	
	private void animateExtension(PanelExtendet pExtendet) {
	    TimeUtils animationTimer = new TimeUtils();
	    Management.instance.EXECUTOR_SERVICE.submit(() -> {
		while(pExtendet.isAnimate()) {
		    //if (pExtendet.isAnimate()) {
		    if(animationTimer.hasReached(5)) {
			animationTimer.reset();
			switch (pExtendet.getState()) {
			case 1:
			    pExtendet.setAnimationX(pExtendet.getAnimationX() + 1);
			    if (pExtendet.getAnimationX() == width
				    || pExtendet.getAnimationX() > width) {
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
		    //}
		}
		});
	}
	
	private boolean isClickGUIPanelHovered(int mouseX, int mouseY) {
		return mouseX > (x) && mouseX < (x + width) && mouseY > (y - 1) && mouseY < (y + getPanelYOffset());
	}
	
	public void onMouseReleased(int x, int y, int state) {
		if(state == 0){
			candrag = false;
		}
		for(PanelExtendet pe: ePanels) {
			pe.onMouseReleased(x, y, state);
		}
	}
	
	public ClickGUIButton getButtonByPosition(final ClickGUIPanel p, final int x, final int y) {
		return modules.stream().filter(new Predicate<ClickGUIButton>() {	
			@Override
			public boolean test(ClickGUIButton module) {
				return x > (p.getX()) && x < (p.getX() + p.getWidth()) && y > (p.getY() + module.getY()) && y < (p.getY() + module.getY() + getPanelYOffset());
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

	public ArrayList<ClickGUIButton> getModules() {
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
	
	public PanelExtendet getPanelByModuleName(final String name) {
		return ePanels.stream().filter(new Predicate<PanelExtendet>() {
			@Override
			public boolean test(PanelExtendet module) {
				return module.getName().equalsIgnoreCase(name);
			}
		}).findFirst().orElse(null);
	}

	public int getPanelYOffset() {
	    return panelYOffset;
	}

}
