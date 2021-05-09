package de.verschwiegener.atero.module.modules.movement;


import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;

public class Fly extends Module {
    TimeUtils timeUtils;
    public Fly() {
        super("Fly", "Fly", Keyboard.KEY_NONE, Category.Movement);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        Minecraft.thePlayer.capabilities.isFlying = false;
        super.onDisable();
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
        if (this.isEnabled()) {
            super.onUpdate();

            Minecraft.thePlayer.cameraYaw = 0.1f;
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.jump();
            }
            Minecraft.thePlayer.capabilities.isFlying = true;

        }
    }

}
