package de.verschwiegener.atero.module.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;

public class Test extends Module {

	public Test() {
		super("Test", "Test", Keyboard.KEY_NONE, Category.Combat);
	}
	
	@Override
	public void setup() {
		super.setup();
		ArrayList<SettingsItem> items = new ArrayList<>();
		items.add(new SettingsItem("TEst", false,""));
		items.add(new SettingsItem("TEst2", true,"TEst"));
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
	public void onUpdate() {
		super.onUpdate();
		
	}

}
