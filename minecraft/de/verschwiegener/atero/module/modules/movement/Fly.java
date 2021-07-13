package de.verschwiegener.atero.module.modules.movement;


import net.minecraft.client.Minecraft;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.Util;
import god.buddy.aot.BCompiler;

public class Fly extends Module {
    TimeUtils timeUtils;
    public Fly() {
        super("Fly", "Fly", Keyboard.KEY_NONE, Category.Movement);
    }

    public void onEnable() {
        if(mc.thePlayer.onGround) {
            mc.thePlayer.motionY = 0.42F;
        }
super.onEnable();
    }

    public void onDisable() {
        mc.timer.timerSpeed =1F;
	Util.setSpeed(0);
        Minecraft.thePlayer.capabilities.isFlying = false;
        super.onDisable();
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
        if (this.isEnabled()) {
            super.onUpdate();
            double value = 0.1;
                if(mc.thePlayer.ticksExisted % 3 == 0){
                    mc.thePlayer.motionY = 0.2;
                }else{
                    mc.thePlayer.motionY = -0.1;
                }
         //   mc.thePlayer.motionY = 0F;
            Util.setSpeed(1);
            if(mc.gameSettings.keyBindJump.pressed){
                mc.thePlayer.motionY = 0.42F;
            }
            if(mc.gameSettings.keyBindSneak.pressed){
                mc.thePlayer.motionY = -0.42F;
            }
        }
    }

    public static float getDirection() {
        float var1 = Minecraft.getMinecraft().thePlayer.rotationYaw;

        if (Minecraft.getMinecraft().thePlayer.moveForward < 0)
            var1 += 180F;
        float forward = 2F;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0)
            forward = -.6F;
        else if (Minecraft.getMinecraft().thePlayer.moveForward > 0)
            forward = .6F;
        else
            forward = 3F;

        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0)
            var1 -= 90F * forward;
        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0)
            var1 += 90F * forward;
        var1 *= .0173292F;
        return var1;
    }
}
