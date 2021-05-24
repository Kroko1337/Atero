package de.verschwiegener.atero.module.modules.combat;



import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.events.callables.EventReceivedPacket;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.Util;
import net.minecraft.client.Minecraft;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Velocity extends Module {
    TimeUtils timeUtils;
    public static Setting setting;
    public Velocity() {
        super("Velocity", "Velocity", Keyboard.KEY_NONE, Category.Combat);
    }

    public void onEnable() {
        super.onEnable();
        setting = Management.instance.settingsmgr.getSettingByName(getName());
    }

    public void onDisable() {
        super.onDisable();
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @EventTarget
    public void onEvent(EventReceivedPacket ppe) {

        //System.out.println("Event");

        if (this.isEnabled()) {
            try {
                Packet p = ppe.getPacket();
                String modes = setting.getItemByName("VelocityMode").getCurrent();
                switch (modes) {
                    case "AAC":
                   //     if (mc.thePlayer.isCollidedHorizontally) {
                            if (Minecraft.thePlayer.hurtTime != 0) {

                                if (!Management.instance.modulemgr.getModuleByName("HighJump").isEnabled()) {
                                    final float SPEED = (float) MathHelper.getRandomDoubleInRange(new Random(), 0.1, 0.2);
                                    setSpeed(0.3);
                                    // mc.thePlayer.motionX = 0F;
                                    //  mc.thePlayer.motionZ = 0F;
                                }
                                if (Minecraft.thePlayer.onGround) {
                                    //Minecraft.thePlayer.motionY = 0.1F;
                                }
                            }
                    //    }
                        break;
                    case "WallReverse":
                            if (mc.thePlayer.isCollidedHorizontally) {
                        if (Minecraft.thePlayer.hurtTime != 0) {
                                setSpeed(0.2);
                            }

                        }
                        break;
                    case "NCP":
                        if (p instanceof S12PacketEntityVelocity) {
                            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
                            if (packet.getEntityID() == Minecraft.thePlayer.getEntityId())
                                ppe.setCancelled(true);
                        }
                        if (p instanceof net.minecraft.network.play.server.S27PacketExplosion)
                            ppe.setCancelled(true);
                        break;


                    case "Cubecraft":
                        if (Minecraft.thePlayer.hurtTime != 0) {
                            if (!Management.instance.modulemgr.getModuleByName("HighJump").isEnabled()) {
                                setSpeed(0.2F);
                            }
                        }

                        if (p instanceof S12PacketEntityVelocity) {
                            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
                            //   if (mc.thePlayer.ticksExisted % 2 == 1) {
                            if (packet.getEntityID() == Minecraft.thePlayer.getEntityId())

                            if (!Management.instance.modulemgr.getModuleByName("HighJump").isEnabled()) {
                                ppe.setCancelled(true);
                                //  }
                            }
                        }
                        if (p instanceof net.minecraft.network.play.server.S27PacketExplosion)
                            ppe.setCancelled(true);
                        break;

                }

            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void setup() {
        final ArrayList<SettingsItem> items = new ArrayList<>();
        ArrayList<String> modes = new ArrayList<>();
        modes.add("AAC");
        modes.add("NCP");
        modes.add("Cubecraft");
        modes.add("WallReverse");
        items.add(new SettingsItem("VelocityMode", modes, "AAC", "", ""));
        Management.instance.settingsmgr.addSetting(new Setting(this, items));
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
