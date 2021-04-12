package de.verschwiegener.atero.ui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.components.CustomGuiTextField;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class ClickGUI extends GuiScreen {

    private final ArrayList<ClickGUIPanel> panels = new ArrayList<>();
    private ArrayList<Setting> results = new ArrayList<>();
    private CustomGuiTextField searchBar;
    private Color colorSearch;
    private TimeUtils colorTimer = new TimeUtils();
    private boolean hasSearched;

    public ClickGUI() {
	int x = 100;
	colorSearch = new Color(255, 0, 0, 255);
	final int y = 50;
	for (final Category c : Category.values()) {
	    panels.add(new ClickGUIPanel(c.name(), x, y));
	    x += 100;
	}
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.searchBar = new CustomGuiTextField(1, fontRendererObj, width / 2 -50, -20, 100, 18);
        this.searchBar.setLight(true);
        this.searchBar.setVisible(true);
    }
    public Color getColorSearch() {
	return colorSearch;
    }
    private void dimmColor() {
	if(colorSearch.getAlpha() - 1 > 100) {
	    Color tempColor = new Color(colorSearch.getRed(), colorSearch.getGreen(), colorSearch.getBlue(), colorSearch.getAlpha() - 1);
	    colorSearch = tempColor;
	}else {
	    hasSearched = false;
	}
    }

    public void animateExtension(final ClickGUIPanel p2) {
	final TimeUtils animationTimer = new TimeUtils();
	Management.instance.EXECUTOR_SERVICE.submit(() -> {
	    while (p2.isAnimate()) {
		if (animationTimer.hasReached(10)) {
		    animationTimer.reset();
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
			if (p2.getAnimationheight() == -p2.getYoffset() || p2.getAnimationheight() > p2.getYoffset()) {
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
    public void updateScreen() {
        super.updateScreen();
        this.searchBar.updateCursorCounter();
    }
    
    public boolean isHasSearched() {
	return hasSearched;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
	super.drawScreen(mouseX, mouseY, partialTicks);
	this.searchBar.drawTextBox();
	panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
	if(hasSearched) {
	    //Dimm Delay
	    if(colorTimer.hasReached(40)) {
		    colorTimer.reset();
		    dimmColor();
		}   
	}
    }

    public ClickGUIPanel getPanelButtonByPosition(final int x, final int y) {
	return panels.stream().filter(module -> x > module.getX() && x < module.getX() + module.getWidth()
		&& y > module.getY() && y < module.getY() + module.getPanelYOffset()).findFirst().orElse(null);
    }
    public ArrayList<Setting> getResults() {
	return results;
    }
    
    public Setting getSettingByName(final String name) {
	return results.stream().filter(module -> module.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
		.findFirst().orElse(null);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
	if(keyCode == 1 && !this.searchBar.getVisible()) {
	    super.keyTyped(typedChar, keyCode);
	}
	this.searchBar.textboxKeyTyped(typedChar, keyCode);
	panels.forEach(panel -> panel.keyTyped(typedChar, keyCode));
	switch (keyCode) {
	case 28:
	    if(this.searchBar.isFocused()) {
		 animateSearchBar(1);
		 extendNeeded();
		 this.searchBar.setFocused(false);
	    }
	    if(!this.searchBar.getText().isBlank()) {
		results.clear();
		colorSearch = new Color(255, 0, 0, 255);
		hasSearched = true;
		results.addAll(Management.instance.settingsmgr.getSearchResult(this.searchBar.getText()));
	    }
	    
	    break;
	case 15:
	    animateSearchBar(2);
	    this.searchBar.setText("");
	    this.searchBar.setFocused(true);
	    break;
	case 1:
	    if(this.searchBar.getVisible()) {
		animateSearchBar(1);
	    }
	    break;
	}
    }
    
    private void animateSearchBar(int state) {
	final TimeUtils animationTimer = new TimeUtils();
	if(state == 2) {
	    this.searchBar.setVisible(true);
	}
	Management.instance.EXECUTOR_SERVICE.submit(() -> {
	    boolean animate = true;
	    while(animate) {
		if(animationTimer.hasReached(10)) {
			animationTimer.reset();
			switch (state) {
			case 1:
			    this.searchBar.yPosition -= 1;
			    if(this.searchBar.yPosition == -20 || this.searchBar.yPosition < -20) {
				animate = false;
				this.searchBar.setyPosition(-20);
				 this.searchBar.setVisible(false);
			    }
			    break;
			case 2:
			    System.out.println("Case 2");
			    this.searchBar.yPosition += 1;
			    System.out.println("Y: " + this.searchBar.yPosition);
			    if(this.searchBar.yPosition == 10 || this.searchBar.yPosition > 10) {
				animate = false;
				this.searchBar.setyPosition(10);
			    }
			    //Extend
			    break;
			}
		    }
	    }
	});
    }
    private void extendNeeded() {
	panels.forEach(panel -> panel.extendNeeded());
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
	super.mouseClicked(mouseX, mouseY, mouseButton);
	this.searchBar.mouseClicked(mouseX, mouseY, mouseButton);
	panels.forEach(panel -> panel.onMouseClicked(mouseX, mouseY, mouseButton));

	switch (mouseButton) {
	case 1:
	    final ClickGUIPanel p2 = getPanelButtonByPosition(mouseX, mouseY);
	    if (p2 != null) {
		p2.setAnimate(true);
		animateExtension(p2);
	    }
	    break;
	}
    }

    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton,
	    final long timeSinceLastClick) {
	super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
	super.mouseReleased(mouseX, mouseY, state);
	for (final ClickGUIPanel p : panels) {
	    p.onMouseReleased(mouseX, mouseY, state);
	}
    }

}
