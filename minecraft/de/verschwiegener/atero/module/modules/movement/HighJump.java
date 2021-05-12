package de.verschwiegener.atero.module.modules.movement;



import net.minecraft.client.Minecraft;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MinecraftError;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;

public class HighJump extends Module {
    TimeUtils timeUtils;
    boolean Dmg = false;
    public HighJump() {
        super("HighJump", "HighJump", Keyboard.KEY_NONE, Category.Movement);
    }

    public void onEnable() {

        super.onEnable();
    }

    public void onDisable() {
        Minecraft.thePlayer.speedInAir = 0.02F;
        Minecraft.getMinecraft().timer.timerSpeed = 1F;
        Dmg = false;
        super.onDisable();
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
        if (this.isEnabled()) {
            super.onUpdate();
            if (Minecraft.thePlayer.onGround) {
              Minecraft.getMinecraft().timer.timerSpeed = 0.1F;
            }else{
                Minecraft.getMinecraft().timer.timerSpeed = 1.1F;
            }
            if (Minecraft.thePlayer.hurtTime > 0) {
                Dmg = true;
            }
            if (Dmg) {
                if (Minecraft.thePlayer.hurtTime > 0) {
                    Minecraft.getMinecraft().timer.timerSpeed = 1F;
                    Minecraft.thePlayer.motionY = 0.70F;
                }
            }
        }
        }

    public static void damage() {

    }

    public static float getMaxFallDist() {
        PotionEffect potioneffect = Minecraft.thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
        return (float)(Minecraft.thePlayer.getMaxFallHeight() + f);
    }
    public static void setSpeed(double speed) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double yaw = (double) player.rotationYaw;
        boolean isMoving = player.moveForward != 0.0F || player.moveStrafing != 0.0F;
        boolean isMovingForward = player.moveForward > 0.0F;
        boolean isMovingBackward = player.moveForward < 0.0F;
        boolean isMovingRight = player.moveStrafing > 0.0F;
        boolean isMovingLeft = player.moveStrafing < 0.0F;
        boolean isMovingSideways = isMovingLeft || isMovingRight;
        boolean isMovingStraight = isMovingForward || isMovingBackward;
        if (isMoving) {
            if (isMovingForward && !isMovingSideways) {
                yaw += 0.0D;
            } else if (isMovingBackward && !isMovingSideways) {
                yaw += 180.0D;
            } else if (isMovingForward && isMovingLeft) {
                yaw += 45.0D;
            } else if (isMovingForward) {
                yaw -= 45.0D;
            } else if (!isMovingStraight && isMovingLeft) {
                yaw += 90.0D;
            } else if (!isMovingStraight && isMovingRight) {
                yaw -= 90.0D;
            } else if (isMovingBackward && isMovingLeft) {
                yaw += 135.0D;
            } else if (isMovingBackward) {
                yaw -= 135.0D;
            }

            yaw = Math.toRadians(yaw);
            player.motionX = -Math.sin(yaw) * speed;
            player.motionZ = Math.cos(yaw) * speed;
        }

    }
}
