package de.verschwiegener.atero;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.audio.Stream;
import de.verschwiegener.atero.audio.StreamManager;
import de.verschwiegener.atero.audio.Streamer;
import de.verschwiegener.atero.cape.GIF;
import de.verschwiegener.atero.cape.GIFManager;
import de.verschwiegener.atero.cape.GifLoader;
import de.verschwiegener.atero.command.CommandManager;
import de.verschwiegener.atero.design.font.FontManager;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.friend.FriendManager;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.module.ModuleManager;
import de.verschwiegener.atero.proxy.ProxyManager;
import de.verschwiegener.atero.settings.SettingsManager;
import de.verschwiegener.atero.ui.clickgui.ClickGUI;
import de.verschwiegener.atero.ui.clickgui.ClickGUIPanel;
import de.verschwiegener.atero.util.account.AccountManager;
import de.verschwiegener.atero.util.files.FileManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryNormal;

public class Management {

    public static Management instance = new Management();

    public final String CLIENT_NAME = "Atero";
    public final String CLIENT_VERSION = "B2";
    public String selectedDesign = "AteroDesign";

    public Color colorBlue = new Color(0, 161, 249);
    public Color colorBlack = new Color(28, 28, 28);
    public Color colorGray = new Color(45, 45, 45);
    
    public File CLIENT_DIRECTORY = new File("Atero");

    public boolean modulechange;

    public FontManager fontmgr;
    public SettingsManager settingsmgr;
    public ModuleManager modulemgr;

    public CommandManager commandmgr;
    public ClickGUI clickgui;
    public Fontrenderer fontrenderer;
    public Fontrenderer fontrendererBold;
    public StreamManager streamManager;
    public Stream currentStream;
    public Streamer streamer;
    public ProxyManager proxymgr;
    public GifLoader GIFLoader;
    public GIFManager GIFmgr;
    public FriendManager friendmgr;
    public FileManager fileManager;
    public AccountManager accountmgr;
    
    public ExecutorService EXECUTOR_SERVICE;
    public ExecutorService ANIMATION_EXECUTOR;
    
    public ServerListEntryNormal currentServer;

    public void start() {
	EXECUTOR_SERVICE = Executors.newFixedThreadPool(1);
	ANIMATION_EXECUTOR = Executors.newFixedThreadPool(15);
	fontmgr = new FontManager();
	settingsmgr = new SettingsManager();
	friendmgr = new FriendManager();
	modulemgr = new ModuleManager();
	commandmgr = new CommandManager();
	streamManager = new StreamManager();
	streamManager.updateStreams();
	GIFLoader = new GifLoader();
	GIFmgr = new GIFManager();
	
	GIFmgr.addGif(new GIF("dad", "dad"));
	GIFmgr.addGif(new GIF("Hentai", "test"));
	//GIFmgr.addGif(new GIF("Hero", "hero"));
	GIFmgr.addGif(new GIF("Fire", "tenor"));
	GIFmgr.addGif(new GIF("HAZE", "HAZE"));
	
	fontrenderer = fontmgr.getFontByName("Inter").getFontrenderer();
	fontrendererBold = new Fontrenderer(Fontrenderer.getFontByName("Inter-ExtraLight"), 4F, 4F,"", true, false);
	streamer = new Streamer();
	proxymgr = new ProxyManager();

	accountmgr = new AccountManager();

	fileManager = new FileManager();

	clickgui = new ClickGUI();
	
	//Load Modules
	try {
	    ArrayList<Object[]> modulevalues = fileManager.loadValues(new String[] {"Name", "Enable", "Key"}, CLIENT_DIRECTORY, "Modules");
	    System.out.println("ModuleValue: " + modulevalues.size());
	    for(Object[] object : modulevalues) {
		Module m = modulemgr.getModuleByName((String) object[0]);
		m.setEnabled((boolean) object[1]);
		m.setKey((int) object[2]);
	    }
	}catch(Exception e) {
	    e.printStackTrace();
	}
	//Load ClickGUI
	try {
	    ArrayList<Object[]> clickguivalues = fileManager.loadValues(new String[] { "Name", "XPOS", "YPos", "Extended" }, CLIENT_DIRECTORY, "ClickGUI");
	    for(Object[] object : clickguivalues) {
		System.out.println("Name: " + object[0]);
		ClickGUIPanel panel = clickgui.getPanelButtonByName((String) object[0]);
		panel.setX((int) object[1]);
		panel.setY((int) object[2]);
		panel.setExtended((boolean) object[3]);
	    }
	}catch(Exception e) {
	    e.printStackTrace();
	}

	colorBlue = Management.instance.settingsmgr.getSettingByName("ClickGui").getItemByName("TEST").getColor();

    }

    public void stop() {
	
	//Safe Modules
	try {
	    ArrayList<Object[]> modulevalues = new ArrayList<>();
	    for (Module m : modulemgr.modules) {
		modulevalues.add(new Object[] { m.getName(), m.isEnabled(), m.getKey() });
	    }
	    fileManager.saveValues(new String[] { "Name", "Enable", "Key" }, modulevalues, CLIENT_DIRECTORY, "Modules");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	//Safe ClickGUI
	try {
	    ArrayList<Object[]> clickguivalue = new ArrayList<>();
	    for(ClickGUIPanel panel : clickgui.getPanels()) {
		clickguivalue.add(new Object[] {panel.getName(), panel.getX(), panel.getY(), panel.isExtended()});
	    }
	    fileManager.saveValues(new String[] { "Name", "XPOS", "YPos", "Extended" }, clickguivalue, CLIENT_DIRECTORY, "ClickGUI");
	}catch(Exception e) {
	    
	}
    }
    
    public String getTitle() {
	return CLIENT_NAME + " Version:" + CLIENT_VERSION;
    }

    public void onKey(int key) {
	if (Keyboard.isKeyDown(key)) {
	    modulemgr.onKey(key);
	}
    }
    

}
