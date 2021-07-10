package de.verschwiegener.atero.module.modules.world;


import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPostMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;
import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Random;

public class AutoEagle extends Module {
    public static float[] lastRot;
    public static float lastPitch;
    TimeUtils timeUtils;
    private Setting setting;
    private Scaffold.BlockData blockData;

    public AutoEagle() {
        super("AutoEagle", "AutoEagle", Keyboard.KEY_NONE, Category.World);
    }

    public void onEnable() {
        mc.gameSettings.keyBindSneak.pressed = false;
        mc.thePlayer.setSprinting(false);
        super.onEnable();
    }

    public void onDisable() {
        mc.gameSettings.keyBindSneak.pressed = false;
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
        pre.setYaw((mc.thePlayer.rotationYawHead + 180));
        mc.thePlayer.setSprinting(false);

        if (blockData != null) {
            if (blockData.getFacing() == EnumFacing.NORTH) {

            }
        }

        if (mc.gameSettings.keyBindLeft.pressed) {
            pre.setYaw((mc.thePlayer.rotationYaw + 100F));
        }
        if (mc.gameSettings.keyBindRight.pressed) {
            pre.setYaw((mc.thePlayer.rotationYaw + 270F));
        }
        if (mc.gameSettings.keyBindForward.pressed) {
            final float yaw = (float) MathHelper.getRandomDoubleInRange(new Random(), 165, 168);
            if (setting.getItemByName("180").isState()) {
                pre.setYaw((mc.thePlayer.rotationYaw + 180));
            } else {
                pre.setYaw((mc.thePlayer.rotationYaw + 175));
            }
        }
        pre.setPitch(82);

    }


    @EventTarget
    public void onPost(EventPostMotionUpdate post) {

    }

    public void onUpdateClick() {
        if (this.isEnabled()) {
            super.onUpdateClick();
            if (setting.getItemByName("Sprinting").isState()) {
                mc.rightClickMouse();
            }
            mc.thePlayer.setSprinting(false);
            BlockPos blockPos = new BlockPos(mc.getMinecraft().thePlayer.posX, mc.getMinecraft().thePlayer.posY - 1, mc.getMinecraft().thePlayer.posZ);
            if (mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air) {
                if (!setting.getItemByName("Sprinting").isState()) {
                    mc.rightClickMouse();
                }
                mc.gameSettings.keyBindSneak.pressed = true;
                mc.gameSettings.keyBindSprint.pressed = false;
            } else {
                mc.gameSettings.keyBindSneak.pressed = false;
                if (setting.getItemByName("Sprinting").isState()) {
                    System.out.println("True");
                    mc.gameSettings.keyBindSprint.pressed = true;
                    // mc.thePlayer.setSprinting(true);
                }
            }

            this.blockData = find(new Vec3(0, 0, 0));
        }
    }

    private Scaffold.BlockData find(Vec3 offset3) {

        double x = Minecraft.getMinecraft().thePlayer.posX;
        double y = Minecraft.getMinecraft().thePlayer.posY;
        double z = Minecraft.getMinecraft().thePlayer.posZ;

        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(new Vec3(x, y, z).add(offset3)).offset(EnumFacing.DOWN);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = position.offset(facing);
            if (Minecraft.getMinecraft().theWorld.getBlockState(offset).getBlock() instanceof BlockAir || rayTrace(Minecraft.getMinecraft().thePlayer.getLook(0.0f), getPositionByFace(offset, invert[facing.ordinal()])))
                continue;
            return new Scaffold.BlockData(invert[facing.ordinal()], offset);
        }
        BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 0, 2), new BlockPos(0, 0, -2), new BlockPos(2, 0, 0), new BlockPos(-2, 0, 0)};
        for (BlockPos offset : offsets) {
            BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
            if (!(Minecraft.getMinecraft().theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offset2 = offsetPos.offset(facing);
                if (Minecraft.getMinecraft().theWorld.getBlockState(offset2).getBlock() instanceof BlockAir || rayTrace(Minecraft.getMinecraft().thePlayer.getLook(0.01f), getPositionByFace(offset, invert[facing.ordinal()])))
                    continue;
                return new Scaffold.BlockData(invert[facing.ordinal()], offset2);
            }
        }
        return null;
    }

    private Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
        Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0, (double) facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
        Vec3 point = new Vec3((double) position.getX() + 0.5, (double) position.getY() + 0.5, (double) position.getZ() + 0.5);
        return point.add(offset);
    }

    private boolean rayTrace(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        int steps = 10;
        double x = difference.xCoord / (double) steps;
        double y = difference.yCoord / (double) steps;
        double z = difference.zCoord / (double) steps;
        Vec3 point = origin;
        for (int i = 0; i < steps; ++i) {
            BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
            IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPosition);
            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir) continue;
            AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, blockPosition, blockState);
            if (boundingBox == null) {
                boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            }
            if (!boundingBox.offset(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()).isVecInside(point))
                continue;
            return true;
        }
        return false;
    }

}




