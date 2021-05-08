package de.verschwiegener.atero.ui.mainmenu;

public class AccountManagerItem {
    
    AccountManagerScreen2 screen;
    int YOffset;
    Account account;
    
    public AccountManagerItem(AccountManagerScreen2 screen, int YOffset, Account account) {
	this.screen = screen;
	this.YOffset = YOffset;
	this.account = account;
    }

}
