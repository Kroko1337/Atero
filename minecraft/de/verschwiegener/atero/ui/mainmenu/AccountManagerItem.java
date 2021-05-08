package de.verschwiegener.atero.ui.mainmenu;

import java.io.IOException;

public class AccountManagerItem {
    
    AccountManagerScreen2 screen;
    int YOffset;
    Account account;
    
    public AccountManagerItem(AccountManagerScreen2 screen, int YOffset, Account account) {
	this.screen = screen;
	this.YOffset = YOffset;
	this.account = account;
    }
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	
    }
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

}
