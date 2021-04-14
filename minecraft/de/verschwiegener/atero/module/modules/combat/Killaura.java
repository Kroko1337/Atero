package de.verschwiegener.atero.module.modules.combat;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.TimeUtils;
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

    public static float getYaw() {
	return Killaura.yaw;
    }

    public static boolean hasTarget() {
	return Killaura.target != null || Killaura.preaimtarget != null;
    }

    private Setting setting, targetset;
    private final TimeUtils timer = new TimeUtils();

    private final Minecraft mc = Minecraft.getMinecraft();

    public Killaura() {
	super("Killaura", "Killaura", Keyboard.KEY_NONE, Category.Combat);
    }

    // TODO Reworken
    private boolean canAttack(final EntityLivingBase player) {
	if (player == Minecraft.thePlayer)
	    return false;
	if (!Minecraft.thePlayer.canEntityBeSeen(player) && !setting.getItemByName("ThroughWalls").isState())
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
	//if (player.deathTime != 0)
	    //return false;
	return true;
    }

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

    // TODO Names Reworken
    private float[] getEntityRotations(final EntityPlayerSP player, final EntityLivingBase target,
	    final Vec3 targetVec) {
	final double posX = targetVec.xCoord - player.posX;
	final double posY = targetVec.yCoord + target.getEyeHeight() - (targetVec.yCoord + player.getEyeHeight() + 0.5);
	final double posZ = targetVec.zCoord - player.posZ;
	final double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
	float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f;
	float pitch = (float) -(Math.atan2(posY, var14) * 180.0 / Math.PI);
	final float mouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
	// Wenn kein Schaden macht
	// float f3 = mouseSensitivity * mouseSensitivity * mouseSensitivity * 1.2F;
	final float sensitivityOffset = mouseSensitivity * 3 * 1.2F;
	yaw -= yaw % sensitivityOffset;
	pitch -= pitch % (sensitivityOffset * mouseSensitivity);
	return new float[] { yaw, pitch };
    }

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

    private MovingObjectPosition getTarget(final float partialTicks, final double distance) {
	final Minecraft mc = Minecraft.getMinecraft();
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
	if ((omo != null && omo.typeOfHit == MovingObjectType.ENTITY) && (omo.entityHit instanceof EntityLivingBase))
	    return omo;
	return null;
    }

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

    private Vec3 linearpredict(final EntityLivingBase base) {
	final double x = base.posX + base.motionX;
	final double y = base.posY + base.motionY;
	final double z = base.posZ + base.motionZ;
	return new Vec3(x, y, z);
    }

    @Override
    public void onEnable() {
	super.onEnable();
	setting = Management.instance.settingsmgr.getSettingByName(getName());
	targetset = Management.instance.settingsmgr.getSettingByName("Target");
    }

    @EventTarget
    public void onPre(final EventPreMotionUpdate pre) {
	if (Killaura.target != null) {
	    Killaura.facing = getEntityRotations(Minecraft.thePlayer, Killaura.target, linearpredict(Killaura.target));
	    pre.setYaw(Killaura.yaw);
	    pre.setPitch(Killaura.pitch);
	    Killaura.yaw = interpolateRotation(Killaura.yaw, Killaura.facing[0], 180);
	    Killaura.pitch = interpolateRotation(Killaura.pitch, Killaura.facing[1], 180);
	} else if (Killaura.preaimtarget != null) {
	    Killaura.facing = getEntityRotations(Minecraft.thePlayer, Killaura.preaimtarget,
		    linearpredict(Killaura.preaimtarget));
	    pre.setYaw(Killaura.yaw);
	    pre.setPitch(Killaura.pitch);
	    Killaura.yaw = interpolateRotation(Killaura.yaw, Killaura.facing[0], 180);
	    Killaura.pitch = interpolateRotation(Killaura.pitch, Killaura.facing[1], 180);
	}
    }

    @Override
    public void onUpdate() {
	super.onUpdate();
	try {

	    if (Minecraft.getMinecraft().currentScreen != null)
		return;

	    final double reach = setting.getItemByName("Range").getCurrentValue();
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
	    }

	    if (Killaura.target == null) {
		Killaura.yaw = interpolateRotation(Killaura.yaw, Killaura.facing[0], 180);
		Killaura.pitch = interpolateRotation(Killaura.pitch, Killaura.facing[1], 180);
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

	    final float CCPS = (float) Math.random() * (140 - 130 + 1) + 130;
	    final MovingObjectPosition t = getTarget(mc.timer.elapsedTicks, reach);
	    if (t != null) {
		if (Minecraft.thePlayer.isSwingInProgress) {
		    mc.getNetHandler()
			    .addToSendQueue(new C02PacketUseEntity(t.entityHit, C02PacketUseEntity.Action.ATTACK));
		    Minecraft.thePlayer.attackTargetEntityWithCurrentItem(t.entityHit);
		} else if (TimeUtils.hasReached(CCPS)) {
		    TimeUtils.reset();
		    mc.getNetHandler()
			    .addToSendQueue(new C02PacketUseEntity(t.entityHit, C02PacketUseEntity.Action.ATTACK));
		    Minecraft.thePlayer.swingItem();
		    Minecraft.thePlayer.attackTargetEntityWithCurrentItem(t.entityHit);

		}
	    }
	    // AutoBlock
	    if (setting.getItemByName("AutoBlock").isState()) {
		Minecraft.getMinecraft();
		if (Minecraft.thePlayer.ticksExisted % 5 == 0) {
		    Minecraft.getMinecraft();
		    if (Minecraft.thePlayer.getHeldItem() != null) {
			Minecraft.getMinecraft();
			Minecraft.getMinecraft();
			Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.thePlayer,
				Minecraft.getMinecraft().theWorld, Minecraft.thePlayer.getHeldItem());
		    }
		    TimeUtils.reset();

		}
	    }
	} catch (final NullPointerException ex) {
	    ex.printStackTrace();
	}
    }

    @Override
    public void setup() {
	super.setup();
	final ArrayList<SettingsItem> items = new ArrayList<>();
	final ArrayList<String> targetModes = new ArrayList<>();
	targetModes.add("Nearest");
	targetModes.add("Lowest");
	targetModes.add("Highest");
	items.add(new SettingsItem("TargetMode", targetModes, "Nearest", "", ""));
	items.add(new SettingsItem("Range", 1.0F, 6.0F, 3.0F, ""));
	items.add(new SettingsItem("Preaim", false, "PreAimRange"));
	items.add(new SettingsItem("PreAimRange", 1.0F, 6.0F, 3.0F, ""));
	items.add(new SettingsItem("KeepSprint", true, ""));
	items.add(new SettingsItem("ThroughWalls", false, ""));
	items.add(new SettingsItem("AutoBlock", false, ""));
	items.add(new SettingsItem("CorrectMM", false, ""));
	Management.instance.settingsmgr.addSetting(new Setting(this, items));
    }

}
