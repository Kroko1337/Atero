package de.verschwiegener.atero.module.modules.world;


import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPostMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;
import de.verschwiegener.atero.module.modules.movement.HighJump;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.RotationRecode2;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;

import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AutoEagle extends Module {
    TimeUtils timeUtils;
    private Setting setting;
    public static float[] lastRot;
    public static float  lastPitch;
    public AutoEagle() {
        super("AutoEagle", "AutoEagle", Keyboard.KEY_NONE, Category.World);
    }

    public void onEnable() {
        mc.thePlayer.setSprinting(false);
        super.onEnable();
    }
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
        super.onDisable();
    }
    @Override
    public void setup() {
        super.setup();
        final ArrayList<SettingsItem> items = new ArrayList<>();
        items.add(new SettingsItem("Sprinting", true, ""));
        items.add(new SettingsItem("180", false, ""));
        Management.instance.settingsmgr.addSetting(new Setting(this, items));
        setting = Management.instance.settingsmgr.getSettingByName(getName());
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
        if (this.isEnabled()) {
            super.onUpdate();
        }
    }
    @EventTarget
    public void onPre(EventPreMotionUpdate pre) {
        mc.thePlayer.setSprinting(false);
        if(mc.gameSettings.keyBindLeft.pressed) {
            pre.setYaw((mc.thePlayer.rotationYaw + 100F));
        }
        if(mc.gameSettings.keyBindRight.pressed) {
            pre.setYaw((mc.thePlayer.rotationYaw + 270F));
        }
        if(mc.gameSettings.keyBindForward.pressed) {
            final float yaw = (float) MathHelper.getRandomDoubleInRange(new Random(), 165, 168);
            if (setting.getItemByName("180").isState()) {
                pre.setYaw((mc.thePlayer.rotationYaw + 180));
            }else {
                pre.setYaw((mc.thePlayer.rotationYaw + 165));
            }
        }
       pre.setPitch(82);

    }
        @EventTarget
    public void onPost(EventPostMotionUpdate post) {
            if (setting.getItemByName("Sprinting").isState()) {
                mc.rightClickMouse();
            }
            mc.thePlayer.setSprinting(false);
        BlockPos blockPos = new BlockPos(mc.getMinecraft().thePlayer.posX, mc.getMinecraft().thePlayer.posY - 1 , mc.getMinecraft().thePlayer.posZ);
        if (mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air) {
            if (!setting.getItemByName("Sprinting").isState()) {
                mc.rightClickMouse();
            }
           mc.gameSettings.keyBindSneak.pressed = true;
           mc.gameSettings.keyBindSprint.pressed = false;
        }else{
            mc.gameSettings.keyBindSneak.pressed = false;
            if (setting.getItemByName("Sprinting").isState()) {
                System.out.println("True");
                mc.gameSettings.keyBindSprint.pressed = true;
            }
            }
        }
    }




