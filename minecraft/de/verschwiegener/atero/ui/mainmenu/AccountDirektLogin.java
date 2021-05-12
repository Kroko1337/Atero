package de.verschwiegener.atero.ui.mainmenu;

import java.awt.Color;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.util.components.CustomGuiTextField;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;

public class AccountDirektLogin extends GuiScreen {
    
    AccountManagerScreen2 parent;
    CustomGuiTextField emailField;
    CustomGuiTextField passwordField;
    private Fontrenderer fontRenderer;
    
    public AccountDirektLogin(AccountManagerScreen2 parent) {
	this.parent = parent;
	this.fontRenderer = Management.instance.fontrenderer;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        int xPos = this.width / 2 - 150;
        int yPos = this.height / 2 - 150;
        this.emailField = new CustomGuiTextField(1, fontRendererObj, xPos + 100, yPos + 20, 180, 20);
        this.passwordField = new CustomGuiTextField(2, fontRendererObj, xPos + 100, yPos + 40, 180, 20);
    }
    
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        parent.drawScreen(mouseX, mouseY, partialTicks);
        int xPos = this.width / 2 - 150;
        int yPos = this.height /2 - 150;
        RenderUtil.drawRectRound(xPos, yPos, 300, 300, 5, Management.instance.colorBlack);
        emailField.drawTextBox();
        passwordField.drawTextBox();
        fontRenderer.drawString("Email:", xPos + 5, yPos + 20, Color.WHITE.getRGB());
        fontRenderer.drawString("Password;", xPos + 5, yPos + 40, Color.WHITE.getRGB());
    }

}
