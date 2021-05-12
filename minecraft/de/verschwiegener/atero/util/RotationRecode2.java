package de.verschwiegener.atero.util;



import de.verschwiegener.atero.module.modules.world.Scaffold;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationRecode2 {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static float[] rotationrecode7(Scaffold.BlockData blockData) {
        double x = blockData.getPos().getX() + 0.5D - mc.thePlayer.posX + blockData.getFacing().getFrontOffsetX() / 2.0D;
        double z = blockData.getPos().getZ() + 0.5D - mc.thePlayer.posZ + blockData.getFacing().getFrontOffsetZ() / 2.0D;
        double y = blockData.getPos().getY() + 0.5D;
        double ymax = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        double allmax = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(ymax, allmax) * 180.0D / Math.PI);
        if (yaw < 0.0F) yaw += 360.0F;
        return new float[]{yaw, pitch};
    }
    public static float[] aac3Rotations(Scaffold.BlockData blockData) {
        Entity temp = new EntitySnowball(mc.theWorld);
        temp.posX = blockData.getPos().getX() + 0.5D - mc.thePlayer.posX + blockData.getFacing().getFrontOffsetX() / 2.5D;
        temp.posZ = blockData.getPos().getZ() + 0.5D - mc.thePlayer.posZ + blockData.getFacing().getFrontOffsetZ() / 2.5D;
        temp.posY = blockData.getPos().getY() + 0.5D;
        double ymax = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - temp.posY;
        double allmax = MathHelper.sqrt_double(temp.posX * temp.posX + temp.posZ * temp.posZ);
        float yaw = (float) ((float) (Math.atan2(temp.posZ, temp.posX) * 180 / Math.PI) - Math.PI * 28.65);
        float pitch = (float) ((float) (Math.atan2(ymax, allmax) * 180 / Math.PI));
        if (yaw < 0.0F) yaw += 360.0F;
        return new float[]{yaw, pitch};
    }


    public static float[] rotationrecode3(Scaffold.BlockData blockdata) {
        double x = blockdata.getPos().getX() + 0.5 - mc.thePlayer.posX;
        double z = blockdata.getPos().getZ() + 0.5 - mc.thePlayer.posZ;
        double y = blockdata.getPos().getY() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + 0.5;
        double allmax = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-Math.atan2(y, allmax) * 180.0D / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float[] rotationrecode33(Scaffold.BlockData blockdata) {
        double x = blockdata.getPos().getX() + 0.5 - mc.thePlayer.posX;
        double z = blockdata.getPos().getZ() + 0.5 - mc.thePlayer.posZ;
        double y = blockdata.getPos().getY() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + 0.5;
        double allmax = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-Math.atan2(y, allmax) * 180.0D / Math.PI);
        return new float[]{yaw, pitch};
    }


    public static float[] rotationrecode4(Scaffold.BlockData blockdata) {
        double x = blockdata.getPos().getX() + 0.6 - mc.thePlayer.posX;
        double z = blockdata.getPos().getZ() + 0.5 - mc.thePlayer.posZ;
        double y = blockdata.getPos().getY() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + 0.5;
        double allmax = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 33.0D / Math.PI) - 1.0F;
        float pitch = (float) (-Math.atan2(y, allmax) * 140.0D / Math.PI);
        return new float[]{yaw, pitch};
    }





    public static float[] rotationEnderMan(Vec3 vec, EntityLivingBase entity) {
        double x = vec.xCoord - mc.thePlayer.posX + mc.thePlayer.renderOffsetX / 2;
        double z = vec.zCoord - mc.thePlayer.posZ + mc.thePlayer.renderOffsetZ / 2;
        double y = vec.yCoord + entity.getEyeHeight() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());

        //double posY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - 0.10000000149011612D;
        //double y = mc.thePlayer.getEntityBoundingBox().minY + (double)(mc.thePlayer.height / 3.0F) - posY;

        double sqrt = (double) MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) ((MathHelper.func_181159_b(z, x) * 180.0D / Math.PI)) - 90.0F;
        float pitch = (float) (-((MathHelper.func_181159_b(y, sqrt) * 180.0D / Math.PI)));

        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw < 0) {
            yaw += 360;
        }
        if (pitch < -90) {
            pitch -= 90;
        }

        System.out.println("Yaw: " + yaw);
        System.out.println("Pitch: " + pitch);

        return new float[]{yaw, pitch};
    }


//	public static float[] rotationrecode21(de.verschwiegener.slinky.Module.Modules.BlockFly.BlockData blockdata) {
//		// TODO Auto-generated method stub
//		return null;
//	}


}

