package de.verschwiegener.atero.module.modules.movement;


import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;

public class Sprint extends Module {
	TimeUtils timeUtils;
	public Sprint() {
		super("Sprint", "Sprint", Keyboard.KEY_NONE, Category.Movement);
	}

	public void onEnable() {
			Minecraft.getMinecraft().thePlayer.setSprinting(true);
		super.onEnable();
	}

	public void onDisable() {
		Minecraft.getMinecraft().thePlayer.setSprinting(false);
		super.onDisable();
	}
	 @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
	public void onUpdate() {
		if (this.isEnabled()) {
			super.onUpdate();


				if (Management.instance.modulemgr.getModuleByName("Scaffold").isEnabled()) {
					Minecraft.getMinecraft().thePlayer.setSprinting(false);
				} else {
					if(Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
					Minecraft.getMinecraft().thePlayer.setSprinting(true);
				}
				}
			}
		}

}
