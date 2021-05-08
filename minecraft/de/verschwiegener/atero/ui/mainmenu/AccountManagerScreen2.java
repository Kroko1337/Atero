package de.verschwiegener.atero.ui.mainmenu;

import java.io.IOException;
import java.util.ArrayList;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.util.components.CustomGuiButton;
import net.minecraft.client.gui.GuiScreen;

public class AccountManagerScreen2 extends GuiScreen {
    
    ArrayList<AccountManagerItem> items = new ArrayList<>();
    
    public AccountManagerScreen2() {
	int YOffset = 0;
	for(Account account : Management.instance.accountmgr.accounts) {
	    items.add(new AccountManagerItem(this, YOffset, account));
	    YOffset += 20;
	}
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new CustomGuiButton(1, width - 100, height - 30, 100, 10, "Login", true));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	super.drawScreen(mouseX, mouseY, partialTicks);
	items.forEach(item -> item.drawScreen(mouseX, mouseY, partialTicks));
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
}
