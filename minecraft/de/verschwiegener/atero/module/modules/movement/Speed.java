package de.verschwiegener.atero.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.module.modules.combat.Killaura;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Speed extends Module {

    private Setting setting;

    public Speed() {
	super("Speed", "Speed", Keyboard.KEY_NONE, Category.Movement);
    }

    public void onEnable() {
	super.onEnable();
	setting = Management.instance.settingsmgr.getSettingByName(getName());
    }

    public void onDisable() {
	Minecraft.getMinecraft().timer.timerSpeed = 1F;
	super.onDisable();
    }

    @Override
    public void setup() {
	super.setup();
	final ArrayList<SettingsItem> items = new ArrayList<>();
	final ArrayList<String> modes = new ArrayList<>();
	modes.add("HypixelOnGround");
	items.add(new SettingsItem("Modes", modes, "HypixelOnGround", "", ""));
	Management.instance.settingsmgr.addSetting(new Setting(this, items));
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
	if (this.isEnabled()) {
	    super.onUpdate();
	    String mode = setting.getItemByName("Modes").getCurrent();
	    switch (mode) {
	    case "HypixelOnGround":
		hypixelOnGround();
		break;

	    default:
		break;
	    }
	}
    }

    private void hypixelOnGround() {
	if (Minecraft.getMinecraft().gameSettings.keyBindForward.pressed
		|| Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed
		|| Minecraft.getMinecraft().gameSettings.keyBindRight.pressed
		|| Minecraft.getMinecraft().gameSettings.keyBindBack.pressed) {
	    Minecraft.getMinecraft().gameSettings.keyBindSprint.pressed = true;

	    if (Minecraft.thePlayer.onGround) {
		Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
		// Minecraft.thePlayer.jump();
		float TIMMER1 = (float) MathHelper.getRandomDoubleInRange(new Random(), 4.0, 1.0);
		if (Minecraft.getMinecraft().thePlayer.onGround) {

		    if (Minecraft.thePlayer.ticksExisted % 25 == 0) {
			Minecraft.getMinecraft().timer.timerSpeed = 80;
		    } else {
			Minecraft.getMinecraft().timer.timerSpeed = 4;
		    }
		    if (Minecraft.thePlayer.ticksExisted % 10 == 0) {
			Minecraft.getMinecraft().timer.timerSpeed = 1;
		    }
		    if (Killaura.target != null) {
			Minecraft.getMinecraft().timer.timerSpeed = 1.0F;
		    }
		}

		// Minecraft.thePlayer.motionY -= 0.02099D;
	    }
	}
    }
}
