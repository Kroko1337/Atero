package de.verschwiegener.atero;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventManager;

import de.verschwiegener.atero.command.CommandManager;
import de.verschwiegener.atero.design.DesignManager;
import de.verschwiegener.atero.design.font.FontManager;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.module.ModuleManager;
import de.verschwiegener.atero.module.modules.render.ClickGui;
import de.verschwiegener.atero.settings.SettingsManager;
import de.verschwiegener.atero.ui.clickgui.ClickGUI;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class Management {
	
	public static Management instance = new Management();
	
	public final String CLIENT_NAME = "Atero";
	public final String CLIENT_VERSION = "0.0.0.1";
	public String selectedDesign = "AteroDesign";
	
	public boolean modulechange;
	
	public FontManager fontmgr;
	public SettingsManager settingsmgr;
	public ModuleManager modulemgr;
	public CommandManager commandmgr;
	public DesignManager designmgr;
	public ClickGUI clickgui;
	public Fontrenderer fontrenderer;
	
	public void start() {
		fontmgr = new FontManager();
		settingsmgr = new SettingsManager();
		modulemgr = new ModuleManager();
		commandmgr = new CommandManager();
		designmgr = new DesignManager();
		
		fontrenderer = fontmgr.getFontByName("Inter").getFontrenderer();
		
		clickgui = new ClickGUI();
	}
	
	public String getTitle() {
		return CLIENT_NAME + " Version:" + CLIENT_VERSION;
	}
	
	public void onKey(int key) {
		if(Keyboard.isKeyDown(key)) {
			modulemgr.onKey(key);
		}
	}

}
