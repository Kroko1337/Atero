package de.verschwiegener.atero;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventManager;

import de.verschwiegener.atero.command.CommandManager;
import de.verschwiegener.atero.design.DesignManager;
import de.verschwiegener.atero.design.font.FontManager;
import de.verschwiegener.atero.module.ModuleManager;
import de.verschwiegener.atero.module.modules.render.ClickGui;
import de.verschwiegener.atero.ui.clickgui.ClickGUI;
import net.minecraft.client.gui.GuiScreen;

public class Management {
	
	public static Management instance = new Management();
	
	public final String CLIENT_NAME = "Atero";
	public final String CLIENT_VERSION = "0.0.0.1";
	public String selectedDesign = "AteroDesign";
	
	public boolean modulechange;
	
	public ModuleManager mdlmgr;
	public CommandManager cmdmgr;
	public DesignManager dsnmgr;
	public FontManager fntmgr;
	public ClickGUI clckgui;
	
	public void start() {
		mdlmgr = new ModuleManager();
		cmdmgr = new CommandManager();
		dsnmgr = new DesignManager();
		fntmgr = new FontManager();
		
		clckgui = new ClickGUI();
	}
	
	public String getTitle() {
		return CLIENT_NAME + " Version:" + CLIENT_VERSION;
	}
	
	public void onKey(int key) {
		if(Keyboard.isKeyDown(key)) {
			mdlmgr.onKey(key);
		}
	}

}
