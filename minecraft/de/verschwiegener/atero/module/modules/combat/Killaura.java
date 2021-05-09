package de.verschwiegener.atero.module.modules.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventTest;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;

public class Killaura extends Module {

    public static EntityLivingBase target, preaimtarget;

    public static float yaw;
    public static float pitch;
    public static float[] facing;
    
    double reach = 0;
    private Setting setting, targetset;

    private final TimeUtils timer = new TimeUtils();

    private final Minecraft mc = Minecraft.getMinecraft();

    public Killaura() {
	super("KillAura", "KillAura", Keyboard.KEY_NONE, Category.Combat);
    }

    public static float getYaw() {
	return Killaura.yaw;
    }

    public static boolean hasTarget() {
	return Killaura.target != null || Killaura.preaimtarget != null;
    }

    public static float[] Intavee(final EntityPlayerSP player, final EntityLivingBase target) {
	final float RotationPitch2 = (float) MathHelper.getRandomDoubleInRange(new Random(), 94, 97);
	final float RotationPitch = (float) MathHelper.getRandomDoubleInRange(new Random(), 92, RotationPitch2);
	final float RotationYaw = (float) MathHelper.getRandomDoubleInRange(new Random(), 0.2, 0.4);
	final double posX = target.posX - player.posX;
	final float RotationY = (float) MathHelper.getRandomDoubleInRange(new Random(), 0.1, RotationYaw);
	final double posY = target.posY + target.getEyeHeight() - (player.posY + player.getEyeHeight() + RotationY);
	final double posZ = target.posZ - player.posZ;
	final double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
	float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / Math.PI) - RotationPitch;
	float pitch = (float) -(Math.atan2(posY, var14) * 180 / Math.PI);
	final float f2 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
	final float f3 = f2 * f2 * f2 * 1.2F;
	yaw -= yaw % f3;
	pitch -= pitch % (f3 * f2);

	return new float[] { yaw, pitch };
    }


    private boolean canAttack(final EntityLivingBase player) {
	if (player == Minecraft.thePlayer)
	    return false;
	if (player instanceof EntityPlayer && !targetset.getItemByName("Player").isState())
	    return false;
	if (player instanceof net.minecraft.entity.passive.EntityAnimal
		&& !targetset.getItemByName("Animals").isState())
	    return false;
	if (player instanceof net.minecraft.entity.monster.EntityMob && !targetset.getItemByName("Mobs").isState())
	    return false;
	if (player instanceof net.minecraft.entity.passive.EntityVillager
		&& !targetset.getItemByName("Villager").isState())
	    return false;
	if (player.isOnSameTeam(Minecraft.thePlayer) && targetset.getItemByName("Teams").isState())
	    return false;
	if (player.isInvisible() && !targetset.getItemByName("Invisible").isState())
	    return false;
	if (player.isDead && !targetset.getItemByName("Death").isState())
	    return false;
	// if (player.deathTime != 0)
	// return false;
	return true;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private boolean canEntityBeSeen(final Entity entityIn) {
	if (!Minecraft.thePlayer.canEntityBeSeen(entityIn))
	    return mc.theWorld.rayTraceBlocks(new Vec3(Minecraft.thePlayer.posX,
		    Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ),
		    new Vec3(entityIn.posX, entityIn.posY, entityIn.posZ)) == null;
	else
	    return true;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public EntityLivingBase getClosestPlayer(final double distance) {
	double d0 = distance;
	EntityLivingBase entityplayer = null;
	for (final Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
	    if (entity instanceof EntityLivingBase && canAttack((EntityLivingBase) entity)) {
		final double d1 = Math.sqrt(entity.getDistanceSq(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY,
			Minecraft.thePlayer.posZ));
		if (d1 < d0) {
		    d0 = d1;
		    entityplayer = (EntityLivingBase) entity;
		}
	    }
	}
	return entityplayer;
    }

    /*@BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    // TODO Names Reworken
    private float[] getEntityRotations(final EntityPlayerSP player, final EntityLivingBase target,
	    final Vec3 targetVec) {
	final double posX = targetVec.xCoord - player.posX;
	double posY;
	if (targetVec.yCoord < Minecraft.thePlayer.posY) {
	    posY = targetVec.yCoord - (targetVec.yCoord + player.getEyeHeight()) + 0.1;
	} else {
	    posY = targetVec.yCoord + target.getEyeHeight() - (targetVec.yCoord + player.getEyeHeight() + 0.5);
	}
	final double posZ = targetVec.zCoord - player.posZ;
	final double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
	float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f;
	float pitch = (float) -(Math.atan2(posY, var14) * 180.0 / Math.PI);
	final float mouseSensitivity = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
	// Wenn kein Schaden macht
	// float f3 = mouseSensitivity * mouseSensitivity * mouseSensitivity * 1.2F;
	final float sensitivityOffset = mouseSensitivity * 3 * 1.2F;
	yaw -= yaw % sensitivityOffset;
	pitch -= pitch % (sensitivityOffset * mouseSensitivity);
	return new float[] { yaw, pitch };
    }*/

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public EntityLivingBase getHighestPlayer(final double distance) {
	EntityLivingBase target = null;
	for (final Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
	    if (entity instanceof EntityLivingBase && canAttack((EntityLivingBase) entity)
		    && entity.getDistance(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY,
			    Minecraft.thePlayer.posZ) < distance) {
		if (target != null && ((EntityLivingBase) entity).getHealth() < target.getHealth() || target == null) {
		    target = (EntityLivingBase) entity;
		}
	    }
	}
	return target;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public EntityLivingBase getLowestPlayer(final double distance) {
	EntityLivingBase target = null;
	for (final Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
	    if (entity instanceof EntityLivingBase && canAttack((EntityLivingBase) entity)
		    && entity.getDistance(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY,
			    Minecraft.thePlayer.posZ) < distance) {
		if (target != null && ((EntityLivingBase) entity).getHealth() < target.getHealth() || target == null) {
		    target = (EntityLivingBase) entity;
		}
	    }
	}
	return target;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private MovingObjectPosition getTarget(final float partialTicks, final double distance) {
	Entity pointedEntity;
	MovingObjectPosition omo = mc.renderViewEntity.rayTrace(distance, partialTicks);
	final double d1 = distance;
	final Vec3 vec3 = mc.renderViewEntity.getPositionEyes(partialTicks);
	final Vec3 vec31 = mc.renderViewEntity.getLook(partialTicks);
	final Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
	pointedEntity = null;
	Vec3 vec33 = null;
	final float f1 = 1.0F;
	final List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity,
		mc.renderViewEntity.boundingBox
			.addCoord(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance)
			.expand(f1, f1, f1));
	double d2 = d1;

	for (final Object element : list) {
	    final Entity entity = (Entity) element;

	    if (entity.canBeCollidedWith()) {
		final float f2 = entity.getCollisionBorderSize();
		final AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
		final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

		if (axisalignedbb.isVecInside(vec3)) {
		    if (0.0D < d2 || d2 == 0.0D) {
			pointedEntity = entity;
			vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
			d2 = 0.0D;
		    }
		} else if (movingobjectposition != null) {
		    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);

		    if (d3 < d2 || d2 == 0.0D) {
			if (entity == mc.renderViewEntity.ridingEntity) {
			    if (d2 == 0.0D) {
				pointedEntity = entity;
				vec33 = movingobjectposition.hitVec;
			    }
			} else {
			    pointedEntity = entity;
			    vec33 = movingobjectposition.hitVec;
			    d2 = d3;
			}
		    }
		}
	    }
	}
	if (pointedEntity != null && (d2 < d1 || omo == null)) {
	    omo = new MovingObjectPosition(pointedEntity, vec33);

	    if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
		mc.pointedEntity = pointedEntity;
	    }
	}
	if (omo != null && omo.typeOfHit == MovingObjectType.ENTITY && omo.entityHit instanceof EntityLivingBase)
	    return omo;
	return null;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private float interpolateRotation(final float yaw, final float pitch, final float partialTicks) {
	float f = MathHelper.wrapAngleTo180_float(pitch - yaw);
	if (f > partialTicks) {
	    f = partialTicks;
	}
	if (f < -partialTicks) {
	    f = -partialTicks;
	}
	return yaw + f;
    }

    /*@BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private Vec3 linearpredict(final EntityLivingBase base) {
	final double x = base.posX + base.motionX;
	final double y = base.posY + base.motionY;
	final double z = base.posZ + base.motionZ;
	return new Vec3(x, y, z);
    }*/

    @Override
    public void onEnable() {
	super.onEnable();
	setting = Management.instance.settingsmgr.getSettingByName(getName());
	targetset = Management.instance.settingsmgr.getSettingByName("Target");
    }

    @EventTarget
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onEvent(final EventTest event) {
	if (setting.getItemByName("CorrectMM").isState()
		&& (Killaura.target != null || Killaura.preaimtarget != null)) {
	    event.setYaw(EventPreMotionUpdate.getInstance.getYaw());
	    event.setSilentMoveFix(true);
	}
    }

    @EventTarget
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onPre(final EventPreMotionUpdate pre) {
	if ((Killaura.target != null)) {
	    Killaura.facing = Killaura.Intavee(Minecraft.thePlayer, Killaura.target);
	    pre.setYaw(Killaura.yaw);
	    pre.setPitch(Killaura.pitch);
	    // yaw = facing[0];
	    // pitch = facing[1];
	    Killaura.yaw = interpolateRotation(Killaura.yaw, Killaura.facing[0], 180);
	    Killaura.pitch = interpolateRotation(Killaura.pitch, Killaura.facing[1], 180);
	}else if(preaimtarget != null) {
	    Killaura.facing = Killaura.Intavee(Minecraft.thePlayer, Killaura.preaimtarget);
	    pre.setYaw(Killaura.yaw);
	    pre.setPitch(Killaura.pitch);
	    // yaw = facing[0];
	    // pitch = facing[1];
	    Killaura.yaw = interpolateRotation(Killaura.yaw, Killaura.facing[0], 180);
	    Killaura.pitch = interpolateRotation(Killaura.pitch, Killaura.facing[1], 180);
	}
    }

    @Override
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
	super.onUpdate();

	try {
	    Minecraft.getMinecraft();
	    if (Minecraft.currentScreen != null)
		return;

	    reach = setting.getItemByName("Range").getCurrentValue();
	    final String mode = setting.getItemByName("TargetMode").getCurrent();
	    if (mode == "Nearest") {
		Killaura.target = getClosestPlayer(reach);
	    } else if (mode == "Lowest") {
		Killaura.target = getLowestPlayer(reach);
	    } else if (mode == "Highest") {
		Killaura.target = getHighestPlayer(reach);
	    }

	    if (setting.getItemByName("PreAim").isState()) {
		Killaura.preaimtarget = getClosestPlayer(setting.getItemByName("PreAimRange").getCurrentValue());
	    } else {
		Killaura.preaimtarget = null;
	    }

	    if (Killaura.target == null) {
		if (Killaura.facing != null) {
		    Killaura.yaw = interpolateRotation(Killaura.yaw, Killaura.facing[0], 180);
		    Killaura.pitch = interpolateRotation(Killaura.pitch, Killaura.facing[1], 180);
		}
		return;
	    }

	    // Sprint
	    if (!setting.getItemByName("KeepSprint").isState()) {
		Minecraft.getMinecraft();
		Minecraft.thePlayer.setSprinting(false);
	    }
	    // FakeBlock
	    // if (setting.getItemByName("FakeBlock").isState()) {
	    // Minecraft.getMinecraft().thePlayer.setItemInUse(Minecraft.getMinecraft().thePlayer.getHeldItem(),5);
	    // }

	    // getWinkel(target);
	    if (!canEntityBeSeen(Killaura.target) && !setting.getItemByName("ThroughWalls").isState())
		return;

	    int maxCPS = (int) setting.getItemByName("MAXCPS").getCurrentValue();
	    int minCPS = (int) setting.getItemByName("MINCPS").getCurrentValue();
	    if(minCPS >= maxCPS) {
		minCPS = maxCPS - 1;
	    }
	    
	    final float CCPS = 1000 / random(minCPS, maxCPS);
	    final MovingObjectPosition t = getTarget(mc.timer.elapsedTicks, reach);
	    if ((t != null && t.typeOfHit == MovingObjectType.ENTITY || setting.getItemByName("ThroughWalls").isState())
		    && TimeUtils.hasReached(CCPS)) {
		TimeUtils.reset();
		// mc.clickMouse();
		Minecraft.thePlayer.swingItem();
		mc.getNetHandler()
			.addToSendQueue(new C02PacketUseEntity(Killaura.target, C02PacketUseEntity.Action.ATTACK));

	    }
	    // AutoBlock
	    if (setting.getItemByName("AutoBlock").isState()) {
		Minecraft.getMinecraft();
		if (Minecraft.thePlayer.ticksExisted % 5 == 0) {
		    Minecraft.getMinecraft();
		    if (Minecraft.thePlayer.getHeldItem() != null) {
			Minecraft.getMinecraft();
			Minecraft.getMinecraft();
			Minecraft.getMinecraft();
			Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.getMinecraft().theWorld,
				Minecraft.thePlayer.getHeldItem());
		    }
		    TimeUtils.reset();

		}
	    }
	} catch (final NullPointerException ex) {
	    ex.printStackTrace();
	}
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private int random(final int min, final int max) {
	final Random random = new Random();
	int zufallZahl = random.nextInt(max);
	while (zufallZahl < min) {
	    zufallZahl = random.nextInt(max);
	}
	return zufallZahl;
    }

    @Override
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void setup() {
	super.setup();
	final ArrayList<SettingsItem> items = new ArrayList<>();
	final ArrayList<String> targetModes = new ArrayList<>();
	targetModes.add("Nearest");
	targetModes.add("Lowest");
	targetModes.add("Highest");
	items.add(new SettingsItem("TargetMode", targetModes, "Nearest", "", ""));
	items.add(new SettingsItem("Range", 1.0F, 6.0F, 3.0F, ""));
	items.add(new SettingsItem("MAXCPS", 1, 20, 4, ""));
	items.add(new SettingsItem("MINCPS", 1, 20, 3, ""));
	items.add(new SettingsItem("Preaim", false, "PreAimRange"));
	items.add(new SettingsItem("PreAimRange", 1.0F, 6.0F, 3.0F, ""));
	items.add(new SettingsItem("KeepSprint", true, ""));
	items.add(new SettingsItem("ThroughWalls", false, ""));
	items.add(new SettingsItem("AutoBlock", false, ""));
	items.add(new SettingsItem("CorrectMM", false, ""));
	Management.instance.settingsmgr.addSetting(new Setting(this, items));
    }

}
