package de.verschwiegener.atero.module.modules.render;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.ui.clickgui.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends Module{

	public ClickGui() {
		super("ClickGui", "ClickGui", Keyboard.KEY_G, Category.Combat);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		Minecraft.getMinecraft().displayGuiScreen(Management.instance.clickgui);
		toggle();
	}

}
