package de.verschwiegener.atero.ui.clickgui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;

import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends GuiScreen {

    ArrayList<ClickGUIPanel> panels = new ArrayList<>();

    public ClickGUI() {
	int x = 100;
	int y = 50;
	for (Category c : Category.values()) {
	    panels.add(new ClickGUIPanel(c.name(), x, y));
	    x += 100;
	}
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	super.drawScreen(mouseX, mouseY, partialTicks);
	for (ClickGUIPanel p : panels) {
	    p.drawScreen(mouseX, mouseY, partialTicks);
	}
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	super.mouseClicked(mouseX, mouseY, mouseButton);
	for (ClickGUIPanel p : panels) {
	    p.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	switch (mouseButton) {
	case 1:
	    ClickGUIPanel p2 = getPanelButtonByPosition(mouseX, mouseY);
	    if (p2 != null) {
		p2.animate = true;
	    }
	    break;
	}
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
	super.mouseReleased(mouseX, mouseY, state);
	for (ClickGUIPanel p : panels) {
	    p.onMouseReleased(mouseX, mouseY, state);
	}
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
	super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
	super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    public ClickGUIPanel getPanelButtonByPosition(final int x, final int y) {
	return panels.stream().filter(new Predicate<ClickGUIPanel>() {
	    @Override
	    public boolean test(ClickGUIPanel module) {
		return x > (module.getX()) && x < (module.getX() + module.getWidth()) && y > module.getY()
			&& y < (module.getY() + module.d.getClickGuiPanelYOffset());
	    }
	}).findFirst().orElse(null);
    }

}
