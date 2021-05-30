package de.verschwiegener.atero.module.modules.movement;


import net.minecraft.client.Minecraft;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
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
        HighJump.setSpeed(0);
        Minecraft.thePlayer.capabilities.isFlying = false;
        super.onDisable();
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
        if (this.isEnabled()) {
            super.onUpdate();

           // Minecraft.thePlayer.sendQueue.addToSendQueue((Packet) new C03PacketPlayer(true));
        //    Minecraft.thePlayer.cameraYaw = 0.1f;
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.jump();
            }
            HighJump.setSpeed(1.5);
         //   Minecraft.thePlayer.capabilities.isFlying = true;

        }


        if(mc.thePlayer.ticksExisted % 2 == 0) {
            if(Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed) {
                mc.thePlayer.motionY = -1.2F;
            }else {
                mc.thePlayer.motionY = -0.1F;
            }
        }else{
            if(Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
                mc.thePlayer.motionY = 1.2F;
            }else {
                mc.thePlayer.motionY = 0.1F;
            }
        }

       // }
    }


}
