package de.verschwiegener.atero.ui.mainmenu;

import java.io.IOException;
import java.util.ArrayList;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.util.account.Account;
import de.verschwiegener.atero.util.components.CustomGuiButton;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;

public class AccountManagerScreen2 extends GuiScreen {
    
    ArrayList<AccountManagerItem> items = new ArrayList<>();
    private GuiScreen parentGUI;
    private int xOffset;
    
    public AccountManagerScreen2(GuiScreen parentGUI) {
	this.parentGUI = parentGUI;
	int YOffset = 0;
	for(int i = 0; i < 10; i++) {
	    items.add(new AccountManagerItem(this, YOffset, null));
	    YOffset += 50;
	}
	for(Account account : Management.instance.accountmgr.getAccounts()) {
	    items.add(new AccountManagerItem(this, YOffset, account));
	    YOffset += 30;
	}
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new CustomGuiButton(1, width - 100, height - 50, 80, 15, "Login", true, true));
        this.buttonList.add(new CustomGuiButton(2, width - 100, height - 35, 80, 15, "Add", true, true));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	xOffset = (width / 2) - 150;
	RenderUtil.drawBackround(width, height);
	items.forEach(item -> item.drawScreen(mouseX, mouseY, xOffset, 40, 300, 50, false));
	super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	super.mouseClicked(mouseX, mouseY, mouseButton);
	items.forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
	super.mouseReleased(mouseX, mouseY, state);
    }
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }
}
