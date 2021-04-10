package de.verschwiegener.atero.module.modules;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.audio.Streamer;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.audio.AudioPanel;
import de.verschwiegener.atero.ui.clickgui.ClickGUI;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;
import net.minecraft.client.Minecraft;

public class Test extends Module {
	
	
	public Test() {
		super("Test", "Test", Keyboard.KEY_NONE, Category.Movement);
	}
	
	@Override
	public void setup() {
		super.setup();
		ArrayList<SettingsItem> items = new ArrayList<>();
		items.add(new SettingsItem("TEst2", false,"TEst"));
		items.add(new SettingsItem("TEst", false,""));
		items.add(new SettingsItem("TEst3", false,""));
		items.add(new SettingsItem("TEst4", true,""));
		items.add(new SettingsItem("TEst5", 0, 100, 50,""));
		ArrayList<String> items2 = new ArrayList<>();
		items2.add("Test");
		items2.add("LOLOLOLOLO");
		items2.add("Hallo");
		items.add(new SettingsItem("Combobox", items2, "Test", "", ""));
		Management.instance.settingsmgr.addSetting(new Setting(this, items));
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		Minecraft.getMinecraft().displayGuiScreen(new AudioPanel());
		toggle();
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

}
