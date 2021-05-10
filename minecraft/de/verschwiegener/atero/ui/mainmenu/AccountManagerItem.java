package de.verschwiegener.atero.ui.mainmenu;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import de.verschwiegener.atero.util.account.Account;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.network.NetworkPlayerInfo;

public class AccountManagerItem {
    
    AccountManagerScreen2 screen;
    int YOffset;
    Account account;
    
    public AccountManagerItem(AccountManagerScreen2 screen, int YOffset, Account account) {
	this.screen = screen;
	this.YOffset = YOffset;
	this.account = account;
    }
    
    public void drawScreen(int mouseX, int mouseY, int x, int y, int listwidth, int slotheight, boolean isSelected) {
	RenderUtil.drawRect(x, y + YOffset, listwidth, slotheight, Color.RED, 2);
    }
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

}
