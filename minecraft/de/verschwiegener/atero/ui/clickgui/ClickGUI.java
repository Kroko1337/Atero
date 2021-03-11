package de.verschwiegener.atero.ui.clickgui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;

import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends GuiScreen {
	
	ArrayList<Panel> panels = new ArrayList<>();
	
	public ClickGUI() {
		int x = 100;
		int y = 50;
		for(Category c : Category.values()) {
			panels.add(new Panel(c.name(), x, y));
			x += 200;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		for(Panel p : panels) {
			p.drawScreen(mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(Panel p : panels) {
			p.onMouseClicked(mouseX, mouseY, mouseButton);
		}
		
		switch (mouseButton) {
		case 1:
			Panel p2 = getPanelButtonByPosition(mouseX, mouseY);
			if(p2 != null) {
				p2.animate = true;
			}
			break;
		}
	}
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		for(Panel p : panels) {
			p.onMouseReleased(mouseX, mouseY, state);
		}
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	public Panel getPanelByPosition(final int x, final int y) {
		return panels.stream().filter(new Predicate<Panel>() {	
			@Override
			public boolean test(Panel module) {
				return x > (module.x - 6) && x < (module.x + module.width - 15) && y > module.y - 10 && y < (module.y + module.yOffset);
			}
		}).findFirst().orElse(null);
	}
	
	public Panel getPanelButtonByPosition(final int x, final int y) {
		return panels.stream().filter(new Predicate<Panel>() {	
			@Override
			public boolean test(Panel module) {
				return x > (module.x - 6) && x < (module.x + module.width - 15) && y > module.y - 10 && y < (module.y + 5);
			}
		}).findFirst().orElse(null);
	}
	
	public Panel getPanelByName(final String name) {
		return panels.stream().filter(new Predicate<Panel>() {	
			@Override
			public boolean test(Panel module) {
				return module.name.equalsIgnoreCase(name);
			}
		}).findFirst().orElse(null);
	}
	
	public double normalize(double in) {
		if(in < 0) {
			return in /-1;
		}
		return in;
	}

}
