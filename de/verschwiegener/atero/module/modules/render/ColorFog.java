package de.verschwiegener.atero.module.modules.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;

public class ColorFog extends Module {

    public ColorFog() {
        super("ColorFog", "ColorFog", Keyboard.KEY_NONE, Category.Render);
    }


    @Override
    public void setup() {
        super.setup();
        ArrayList<SettingsItem> items = new ArrayList<>();
        items.add(new SettingsItem("ColorPink", true));
        items.add(new SettingsItem("ColorBlue", false));
        Management.instance.settingsmgr.addSetting(new Setting(this, items));
    }
}
