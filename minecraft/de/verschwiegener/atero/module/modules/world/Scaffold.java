package de.verschwiegener.atero.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPostMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventSendPacket;
import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.module.modules.movement.HighJump;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.RotationRecode2;
import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.chat.ChatUtil;
import god.buddy.aot.BCompiler;
import io.netty.util.internal.MathUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Scaffold extends Module {

    public static Scaffold instance;
    public static Setting setting;
    public boolean allowdown;

    private float[] lastrotation;
    private int[] forbiddenBlocks = {5};
    private TimeUtils timer = new TimeUtils();
    public  float yaw;
    public static float[] lastRot;
    public static float lastYaw, lastPitch;
    public static Scaffold.BlockData data;
    private int currentItem;
    private boolean switched;

    public Scaffold() {
        super("Scaffold", "Scaffold", Keyboard.KEY_NONE, Category.World);
        instance = this;
    }

    public void setup() {
        final ArrayList<SettingsItem> items = new ArrayList<>();
        items.add(new SettingsItem("Delay", 0, 500, 0, ""));
        items.add(new SettingsItem("Safewalk", false, ""));
        items.add(new SettingsItem("Sprint", false, ""));
        items.add(new SettingsItem("Sneak", false, ""));
        items.add(new SettingsItem("Silent", true, "SilentModes"));
        ArrayList<String> modes = new ArrayList<>();
        modes.add("Switch");
        modes.add("Spoof");
        items.add(new SettingsItem("SilentModes", modes, "Switch", "", ""));
        items.add(new SettingsItem("Down", false, ""));
        items.add(new SettingsItem("SameY", false, ""));
        Management.instance.settingsmgr.addSetting(new Setting(this, items));
    }

    @Override
    public void onEnable() {
        currentItem = -1;

        switched = false;

        super.onEnable();

        setting = Management.instance.settingsmgr.getSettingByName(getName());
        allowdown = (setting.getItemByName("Down").isState()) && !mc.gameSettings.keyBindJump.isKeyDown() ? true : false;
        //  silentSlot = -1;
        //mc.getCurrentServerData().serverIP.equalsIgnoreCase("Cubecraft.net")

    }

    public void onDisable() {
        switched = false;
        if (mc.thePlayer.inventory.currentItem != currentItem)
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

        super.onDisable();
    }






    @EventTarget
    public void onUpdate() {

  //  mc.timer.timerSpeed = 25F;

        if(mc.gameSettings.keyBindJump.pressed && !mc.gameSettings.keyBindForward.pressed){
            if(mc.thePlayer.onGround){
                mc.gameSettings.keyBindJump.pressed = true;
                mc.timer.timerSpeed = 0.6F;
            }else{
                final float HYPXIEL = (float) MathHelper.getRandomDoubleInRange(new Random(), 7, 10);
                final float TOWER = (float) MathHelper.getRandomDoubleInRange(new Random(), 7, 10);
                mc.timer.timerSpeed = TOWER;
            }

        }
        if(!mc.gameSettings.keyBindJump.pressed){
            mc.timer.timerSpeed = 1F;
        }

        data = find(new Vec3(0, 0, 0));
        if (this.data != null && getBlockSlot() != -1) {
            int lastSlot = Minecraft.thePlayer.inventory.currentItem;
            if(!switched) {
                currentItem = getBlockSlot();
                if(mc.thePlayer.inventory.currentItem != currentItem) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(getBlockSlot()));
                }else {
                    switched = true;
                }
            }
            if(switched) {
                if(mc.thePlayer.inventory.getStackInSlot(currentItem) == null || mc.thePlayer.inventory.getStackInSlot(currentItem).stackSize == 0) {
                    currentItem = -1;
                    switched = false;
                    return;
                }
            }
            mc.playerController.updateController();
            Vec3 hitVec = (new Vec3(BlockData.getPos())).addVector(0.5D, 0.5D, 0.5D)
                    .add((new Vec3(BlockData.getFacing().getDirectionVec())).multi(0.5D));
            if(currentItem != -1 && switched) {
                if (mc.playerController.onPlayerRightClick(Minecraft.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(currentItem), BlockData.getPos(), BlockData.getFacing(), hitVec)) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }
            }
        }
        Minecraft.getMinecraft().thePlayer.setSprinting(false);
    }

    @EventTarget
    public void onPacketSend(EventSendPacket eventSendPacket) {
        Packet<?> packet = eventSendPacket.getPacket();
        if(packet instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange c09PacketHeldItemChange = (C09PacketHeldItemChange)packet;
            if(getBlockSlot() == c09PacketHeldItemChange.getSlotId() && !switched) {
                switched = true;
            }else {
                if(switched) {
                    eventSendPacket.setCancelled(true);
                }
            }
        }
    }



    @EventTarget
    public void onPost(EventPostMotionUpdate post) {

    }

    @EventTarget
    public void onPre(EventPreMotionUpdate pre) {
        float[] rotation = data == null ? lastRot : RotationRecode2.rotationrecode7(this.data);
        if(!Management.instance.modulemgr.getModuleByName("TEst").isEnabled()) {
            pre.setYaw(rotation[0]);
            lastYaw = rotation[0];
            pre.setPitch(85.5F);
        }else{
            pre.setYaw(mc.thePlayer.rotationYawHead + 160);
            pre.setPitch(80.5F);
        }
        final float Pitch = (float) MathHelper.getRandomDoubleInRange(new Random(), 85, 85.2);


        //lastPitch = rotation[1];




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
    private Scaffold.BlockData find(Vec3 offset3) {

        double x = Minecraft.getMinecraft().thePlayer.posX;
        double y = Minecraft.getMinecraft().thePlayer.posY;
        double z = Minecraft.getMinecraft().thePlayer.posZ;

        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(new Vec3(x, y , z).add(offset3)).offset(EnumFacing.DOWN);
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


    public static class BlockData {
        private static EnumFacing facing;

        private static BlockPos pos;

        public BlockData(EnumFacing facing, BlockPos pos) {
            BlockData.facing = facing;
            BlockData.pos = pos;
        }

        public static EnumFacing getFacing() {
            return facing;
        }

        public static BlockPos getPos() {
            return pos;
        }
    }
    public int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack s = Minecraft.thePlayer.inventory.getStackInSlot(i);
            if (s != null && s.getItem() instanceof ItemBlock && !Arrays.asList(forbiddenBlocks).contains(s.getItem().getBlockId()))
                return i;
        }
        return -1;
    }
}
