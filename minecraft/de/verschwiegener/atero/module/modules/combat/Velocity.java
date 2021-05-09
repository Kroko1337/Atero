package de.verschwiegener.atero.module.modules.combat;



import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;

public class Velocity extends Module {
    TimeUtils timeUtils;
    public Velocity() {
        super("Velocity", "Velocity", Keyboard.KEY_NONE, Category.Combat);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
        if (this.isEnabled()) {
            super.onUpdate();
            try{
                //XD
            if (Minecraft.thePlayer.hurtTime != 0) {
                Minecraft.thePlayer.motionX = 0F;
                Minecraft.thePlayer.motionZ = 0F;
            }
            }catch (NullPointerException e) {

            }
        }
    }
}
