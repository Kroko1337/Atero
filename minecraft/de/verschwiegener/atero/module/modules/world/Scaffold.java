package de.verschwiegener.atero.module.modules.world;

import java.util.Random;

import javax.swing.text.html.parser.Entity;

import god.buddy.aot.BCompiler;
import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPostMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;

import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
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

    public Scaffold() {
	super("Scaffold", "Scaffold", Keyboard.KEY_NONE, Category.World);
    }
    @Override
    public void onEnable() {
        mc.thePlayer.setSprinting(false);
        super.onEnable();
    }

    public void onDisable() {
        Minecraft.thePlayer.var19 = false;
        super.onDisable();
    }


    public void setup() {
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
        Minecraft.thePlayer.var19 = true;
        try {

            if (Minecraft.thePlayer.hurtTime > 0) {
                Minecraft.thePlayer.var19 = false;
            }
            if (Minecraft.thePlayer.isSprinting()) {
                Minecraft.thePlayer.var19 = true;
            }
            if (Minecraft.thePlayer.capabilities.isFlying) {
                Minecraft.thePlayer.var19 = true;
            }
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.thePlayer.var19 = true;
            }
            if (!Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.var19 = true;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        blockData = find(new Vec3(0, 0, 0));

        mc.thePlayer.setSprinting(false);

       // mc.thePlayer.motionX *= 0.508500000F;
        //mc.thePlayer.motionZ *= 0.508500000F;
    }




    @EventTarget
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onPost(EventPostMotionUpdate post) {
        BlockPos blockPos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - 0.0D, Minecraft.getMinecraft().thePlayer.posZ);
        Minecraft.getMinecraft().rightClickMouse();

    }




    @EventTarget
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onPre(EventPreMotionUpdate pre) {

        float ROTTS4 = (float) MathHelper.getRandomDoubleInRange(new Random(), 150, 176);
        float ROTS2 = (float) MathHelper.getRandomDoubleInRange(new Random(), 82, 79);

        float ROTS = (float) MathHelper.getRandomDoubleInRange(new Random(), 166, 169);

        float yaw = mc.thePlayer.rotationYaw + 180;

        float pitch = ROTS2;

        float f2 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f3 = f2 * f2 * f2 * 1.2F;
        yaw -= yaw % f3;
        pitch -= pitch % (f3 * f2);

        pre.setYaw(aac3Rotations(blockData)[0]);
        pre.setPitch(aac3Rotations(blockData)[1]);
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
        //EntitySnowball temp = new EntitySnowball(mc.theWorld);
        double x = blockData.getPos().getX() + 0.5D - mc.thePlayer.posX + blockData.getFacing().getFrontOffsetX() / 2.0D;
        double z = blockData.getPos().getZ() + 0.5D - mc.thePlayer.posZ + blockData.getFacing().getFrontOffsetZ() / 2.0D;
        double y = blockData.getPos().getY() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + 0.9;
        double ymax = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        double allmax = MathHelper.sqrt_double(x * x + z * z);
        float ROTTS2 = (float) MathHelper.getRandomDoubleInRange(new Random(), 175, 180);
        float ROTTS3 = (float) MathHelper.getRandomDoubleInRange(new Random(), 26.75, 27.70);
        float ROTTS33 = (float) MathHelper.getRandomDoubleInRange(new Random(), 180, 181);
        float ROTTS333 = (float) MathHelper.getRandomDoubleInRange(new Random(), 179, 180);
        float ROTTS4 = (float) MathHelper.getRandomDoubleInRange(new Random(), 180, 178);

        float yaw = (float)(Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-Math.atan2(y, allmax) * ROTTS4 / Math.PI);
        System.out.println("Pitch1: " + pitch);
        if (yaw < 0.0F) yaw += 360.0F;
        float f2 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f3 = f2 * f2 * f2 * 1.2F;
        yaw -= yaw % f3;
        pitch -= pitch % (f3 * f2);
        //if(pitch < 83) {
            //pitch = 83;
        //}
        System.out.println("Pitch2: " + pitch);
        return new float[]{yaw, pitch};
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
        Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0, (double) facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
        Vec3 point = new Vec3((double) position.getX() + 0.5, (double) position.getY() + 0.5, (double) position.getZ() + 0.5);
        return point.add(offset);
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private boolean rayTrace(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        int steps = 20;
        double x = difference.xCoord / (double) 10;
        double y = difference.yCoord / (double) 10;
        double z = difference.zCoord / (double) 10;
        Vec3 point = origin;
        for (int i = 0; i < steps; ++i) {
            BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
            IBlockState blockState = mc.theWorld.getBlockState(blockPosition);
            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir) continue;
            AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox(mc.theWorld, blockPosition, blockState);
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
