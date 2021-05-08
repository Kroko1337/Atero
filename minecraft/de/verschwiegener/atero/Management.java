package de.verschwiegener.atero;

import java.awt.Color;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventManager;

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
import de.verschwiegener.atero.module.ModuleManager;
import de.verschwiegener.atero.module.modules.render.ClickGui;
import de.verschwiegener.atero.proxy.ProxyManager;
import de.verschwiegener.atero.settings.SettingsManager;
import de.verschwiegener.atero.ui.clickgui.ClickGUI;
import de.verschwiegener.atero.ui.mainmenu.AccountManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryNormal;

public class Management {

    public static Management instance = new Management();

    public final String CLIENT_NAME = "Atero";
    public final String CLIENT_VERSION = "0.0.0.1";
    public String selectedDesign = "AteroDesign";

    public Color colorBlue = new Color(0, 161, 249);
    public Color colorBlack = new Color(28, 28, 28);
    public Color colorGray = new Color(45, 45, 45);

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
	
	GIFmgr.addGif(new GIF("TEst", "hero"));
	
	fontrenderer = fontmgr.getFontByName("Inter").getFontrenderer();
	fontrendererBold = new Fontrenderer(Fontrenderer.getFontByName("Inter-ExtraLight"), 4F, 4F,"", true, false);
	streamer = new Streamer();
	proxymgr = new ProxyManager();
	accountmgr = new AccountManager();

	clickgui = new ClickGUI();

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
