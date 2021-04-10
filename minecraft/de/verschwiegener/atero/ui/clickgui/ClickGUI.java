package de.verschwiegener.atero.ui.clickgui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.util.TimeUtils;
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
		p2.setAnimate(true);
		animateExtension(p2);
	    }
	    break;
	}
    }
    private void animateExtension(ClickGUIPanel p2) {
	TimeUtils animationTimer = new TimeUtils();
	Management.instance.EXECUTOR_SERVICE.submit(() -> {
	    while (p2.isAnimate()) {
		if(animationTimer.hasReached(10)) {
		    animationTimer.reset();
		  //TODO return implementieren: https://www.baeldung.com/java-future
			System.out.println("LOL");
			switch (p2.getState()) {
			case 1:
			    p2.setAnimationheight(p2.getAnimationheight() - 1);
			    if (p2.getAnimationheight() == 15 || p2.getAnimationheight() < 15) {
				p2.setAnimate(false);
				p2.setAnimationheight(15);
				p2.setState(2);
			    }
			    break;
			case 2:
			    p2.setAnimationheight(p2.getAnimationheight() + 1);
			    if (p2.getAnimationheight() == -p2.getYoffset()
				    || p2.getAnimationheight() > p2.getYoffset()) {
				p2.setAnimate(false);
				p2.setAnimationheight(p2.getYoffset());
				p2.setState(1);
			    }
			}
		}
	    }
	});
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
			&& y < (module.getY() + module.getPanelYOffset());
	    }
	}).findFirst().orElse(null);
    }

}
