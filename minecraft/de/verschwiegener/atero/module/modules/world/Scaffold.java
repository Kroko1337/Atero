package de.verschwiegener.atero.module.modules.world;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.text.html.parser.Entity;

import de.verschwiegener.atero.util.RotationRecode2;
import god.buddy.aot.BCompiler;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPostMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {

    Minecraft mc = Minecraft.getMinecraft();
    private BlockData blockData;
    private int silentSlot, lastSlot;
    private ItemStack silentStack;
    public static float[] lastRot;
    public static float lastYaw, lastPitch;
    public static Scaffold.BlockData data;

    public Scaffold() {
        super("Scaffold", "Scaffold", Keyboard.KEY_NONE, Category.World);
    }

    @Override
    public void onEnable() {
        silentSlot = -1;
        mc.thePlayer.setSprinting(false);
        super.onEnable();
    }

    public void onDisable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        Minecraft.thePlayer.var19 = false;
        super.onDisable();
    }

    public void setup() {
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {

        blockData = find(new Vec3(0, 0, 0));
        mc.thePlayer.setSprinting(false);
        if (silentSlot != -1) {
            if (mc.thePlayer.inventory.getStackInSlot(silentSlot) == null || !(mc.thePlayer.inventory.getStackInSlot(silentSlot).getItem() instanceof ItemBlock))
                silentSlot = -1;
        }
        if (silentSlot == -1) {
            silentSlot = getBlockSlot();
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(silentSlot));
        }
        if(blockData != null){
            Minecraft.getMinecraft().rightClickMouse(silentSlot);
        }

        Minecraft.thePlayer.var19 = false;
        try {

            if (Minecraft.thePlayer.hurtTime > 0) {
                Minecraft.thePlayer.var19 = false;
            }
            if (Minecraft.thePlayer.isSprinting()) {
                Minecraft.thePlayer.var19 = false;
            }
            if (Minecraft.thePlayer.capabilities.isFlying) {
                Minecraft.thePlayer.var19 = false;
            }
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.thePlayer.var19 = false;
            }
            if (!Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.var19 = false;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }



    @EventTarget
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onPost(EventPostMotionUpdate post) {
        BlockPos blockPos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - 0.0D, Minecraft.getMinecraft().thePlayer.posZ);
        Minecraft.getMinecraft().rightClickMouse(silentSlot);


    }

    public int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack s = Minecraft.thePlayer.inventoryContainer.getSlot(36 + i).getStack();
            if (s != null && s.getItem() instanceof ItemBlock && s.getItem() != ItemBlock.getItemById(54))
                return i;
        }
        return 0;
    }

    @EventTarget
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onPre(EventPreMotionUpdate pre) {
        pre.setYaw(aac3Rotations(blockData)[0]);
        pre.setPitch(aac3Rotations(blockData)[1]);

    }
    }

    Scaffold.BlockData find(Vec3 offset3) {
        try{
            double x = mc.thePlayer.posX;
            double y = mc.thePlayer.posY;
            double z = mc.thePlayer.posZ;

            EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
            BlockPos position = new BlockPos(new Vec3(x, y, z).add(offset3)).offset(EnumFacing.DOWN);
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offset = position.offset(facing);
                if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir || rayTrace(mc.thePlayer.getLook(0.0f), getPositionByFace(offset, invert[facing.ordinal()])))
                    continue;
                return new Scaffold.BlockData(invert[facing.ordinal()], offset);
            }
            BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 0, 2), new BlockPos(0, 0, -2), new BlockPos(2, 0, 0), new BlockPos(-2, 0, 0)};
            for (BlockPos offset : offsets) {
                BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
                if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
                for (EnumFacing facing : EnumFacing.values()) {
                    BlockPos offset2 = offsetPos.offset(facing);
                    if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir || rayTrace(mc.thePlayer.getLook(0.01f), getPositionByFace(offset, invert[facing.ordinal()])))
                        continue;
                    return new Scaffold.BlockData(invert[facing.ordinal()], offset2);
                }
            }
        }catch (NullPointerException e) {

        }
        return null;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    float[] aac3Rotations(Scaffold.BlockData blockData) {
        double x = blockData.getPos().getX() + 0.5D - mc.thePlayer.posX + blockData.getFacing().getFrontOffsetX() / 2.0D;
        double z = blockData.getPos().getZ() + 0.5D - mc.thePlayer.posZ + blockData.getFacing().getFrontOffsetZ() / 2.0D;
        double y = blockData.getPos().getY() + 0.5D;

        double ymax = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        double allmax = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(ymax, allmax) * 180.0D / Math.PI);
        final float f2 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float f3 = f2 * f2 * f2 * 1.2F;
        yaw -= yaw % f3;
        pitch -= pitch % (f3 * f2);
        if (yaw < 0.0F) yaw += 360.0F;
        return new float[]{yaw, pitch};
    }

    private Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
	Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0,
		(double) facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
	Vec3 point = new Vec3((double) position.getX() + 0.5, (double) position.getY() + 0.5,
		(double) position.getZ() + 0.5);
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

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public class BlockData {
        private EnumFacing facing;
        private BlockPos pos;

        public BlockData(EnumFacing facing, BlockPos pos) {
            this.facing = facing;
            this.pos = pos;
        }

        public EnumFacing getFacing() {
            return facing;
        }

        public BlockPos getPos() {
            return pos;
        }
    }


}
