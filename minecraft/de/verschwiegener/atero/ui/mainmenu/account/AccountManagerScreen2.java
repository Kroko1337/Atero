package de.verschwiegener.atero.ui.mainmenu.account;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Font;
import de.verschwiegener.atero.util.LoginUtil;
import de.verschwiegener.atero.util.Util;
import de.verschwiegener.atero.util.account.Account;
import de.verschwiegener.atero.util.chat.ChatFontRenderer;
import de.verschwiegener.atero.util.chat.ChatRenderer;
import de.verschwiegener.atero.util.components.CustomGuiButton;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class AccountManagerScreen2 extends GuiScreen {
    
    ArrayList<AccountManagerItem> items = new ArrayList<>();
    private GuiScreen parentGUI;
    private int xOffset;
    private int selectedAccountSlot;
    private int scrollOffset;
    private Font baseFont;
    private int YOffset = 0;
    private boolean extension;
    private int extensionID;
    
    public AccountManagerScreen2(GuiScreen parentGUI) {
	this.parentGUI = parentGUI;
	selectedAccountSlot = -1;
	baseFont = new Font("baseFont", Util.getFontByName("Inter-ExtraLight"), 3F);
	for(Account account : Management.instance.accountmgr.getAccounts()) {
	    items.add(new AccountManagerItem(this, YOffset, account));
	    YOffset += 30;
	}
	LoginUtil.reset();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new CustomGuiButton(1, width - 40, height - 40, 33, 30, "Login", true));
        this.buttonList.add(new CustomGuiButton(2, width - 80, height - 25, 70, 30, "Add Account", true));
        this.buttonList.add(new CustomGuiButton(3, 10, height - 25, 35, 30, "Back", true));
        items.forEach(item -> item.setExtension(false));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	xOffset = (width / 2) - 100;
	RenderUtil.drawBackround(width, height);
	ChatFontRenderer.drawString("AccountManager", xOffset + (ChatFontRenderer.getStringWidth("AccountManager") / 4), 10, Color.WHITE);
	ChatFontRenderer.drawString(LoginUtil.status, 10, 10, LoginUtil.statusColor);
	for(int i = 0; i < items.size();i++) {
	    items.get(i).drawScreen(mouseX, mouseY, xOffset, 40 - scrollOffset, 200, 30, (i == selectedAccountSlot) ? true : false);
	}
	
	super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	super.mouseClicked(mouseX, mouseY, mouseButton);
	if (!extension) {
	    if (mouseButton == 0) {
		for (int i = 0; i < items.size(); i++) {
		    if (items.get(i).isHovered(mouseX, mouseY)) {
			if (selectedAccountSlot == i) {
			    LoginUtil.LoginAccount(items.get(i).getAccount());
			    selectedAccountSlot = 0;
			} else {
			    selectedAccountSlot = i;
			}
			break;
		    }
		    selectedAccountSlot = -1;
		}
	    }
	}
	if(!items.isEmpty()) {
	    //Throws exception when the item is deleted
	    try {
		items.forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
	    }catch(Exception e) {
	    }
	}
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
	super.mouseReleased(mouseX, mouseY, state);
    }
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
	int wheelD = Mouse.getEventDWheel() / 10;
	if(wheelD != 0 && items.size() * 30 > height - 60) {
	    scrollOffset += -wheelD;
	    if(scrollOffset < 0) {
		scrollOffset = 0;
	    }
	    if(height + scrollOffset > items.size() * 30 + 60 && scrollOffset > 0) {
		scrollOffset = items.size() * 30 + 60 - height;
	    }
	}
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
	switch (button.id) {
	case 1:
	    if(selectedAccountSlot != -1) {
		LoginUtil.LoginAccount(items.get(selectedAccountSlot).getAccount());
	    }else {
		 mc.displayGuiScreen(new AccountLogin(this, "Login"));
	    }
	    break;
	case 2:
	    mc.displayGuiScreen(new AccountLogin(this, "Add"));
	    break;

	case 3:
	    mc.displayGuiScreen(parentGUI);
	    break;
	}
    }
    
    public void handleArgs(String mode, String email, String passwort) {
	if(mode.equalsIgnoreCase("Login")) {
	    LoginUtil.login(email, passwort);
	}else if(mode.equalsIgnoreCase("Add")) {
	    Account account = new Account(email, passwort);
	    Management.instance.accountmgr.getAccounts().add(account);
	    items.add(new AccountManagerItem(this, YOffset, account));
	    YOffset += 15;
	}else if(mode.equalsIgnoreCase("Edit")) {
	    Account account = Management.instance.accountmgr.getAccounts().get(Management.instance.accountmgr.getAccounts().indexOf(items.get(extensionID).getAccount()));
	    account.setEmail(email);
	    account.setPassword(passwort);
	}
    }
    public Font getBaseFont() {
	return baseFont;
    }
    public void setExtension(boolean extension) {
	this.extension = extension;
    }
    public void setExtensionID(int extensionID) {
	this.extensionID = extensionID;
    }
}
