package de.verschwiegener.atero.module.modules.combat;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import de.verschwiegener.atero.util.TimeUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.Random;
import java.util.UUID;

import javax.vecmath.Vector3d;

public class Killaura extends Module {

	public Killaura() {
		super("Killaura", "Killaura", Keyboard.KEY_NONE, Category.Combat);
	}

	public static EntityLivingBase target, preaimtarget;
	public static float yaw;
	public static float pitch;
	public static float[] facing;
	Setting setting, targetset;
	TimeUtils timer = new TimeUtils();
	Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void setup() {
		super.setup();
		ArrayList<SettingsItem> items = new ArrayList<>();
		items.add(new SettingsItem("Range", 1.0F, 6.0F, 3.0F, ""));
		items.add(new SettingsItem("Preaim", false, "PreAimRange"));
		items.add(new SettingsItem("PreAimRange", 1.0F, 6.0F, 3.0F, ""));
		items.add(new SettingsItem("KeepSprint", true, ""));
		//items.add(new SettingsItem("FakeBlock", false, ""));
		items.add(new SettingsItem("AutoBlock", false, ""));
		items.add(new SettingsItem("CorrectMM", false, ""));
		Management.instance.settingsmgr.addSetting(new Setting(this, items));
	}

	@Override
	public void onEnable() {
		super.onEnable();
		setting = Management.instance.settingsmgr.getSettingByName(getName());
		targetset = Management.instance.settingsmgr.getSettingByName("Target");
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (isEnabled()) {
			try {
				double reach = setting.getItemByName("Range").getCurrentValue();
				target = getClosestPlayer(reach);
				preaimtarget = getClosestPlayer(setting.getItemByName("PreAimRange").getCurrentValue());
				if (target == null || Minecraft.getMinecraft().currentScreen != null) {
					return;
				}
				//Sprint
				if (!setting.getItemByName("KeepSprint").isState()) {
					Minecraft.getMinecraft().thePlayer.setSprinting(false);
				}
				//FakeBlock
				//if (setting.getItemByName("FakeBlock").isState()) {
					//Minecraft.getMinecraft().thePlayer.setItemInUse(Minecraft.getMinecraft().thePlayer.getHeldItem(),5);
				//}
				
				float CCPS = (float) Math.random() * (140 - 130 + 1) + 130; 
				//Best Hit Vector
				MovingObjectPosition t = getTarget(mc.timer.elapsedTicks, reach);
				if(t != null) {
					EntityLivingBase entity = (EntityLivingBase) t.entityHit;
					//if (entity.isSwingInProgress) {
					if (mc.thePlayer.isSwingInProgress) {
						//mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(t, C02PacketUseEntity.Action.ATTACK));
						mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(t.entityHit, t.hitVec, C02PacketUseEntity.Action.ATTACK));
						mc.thePlayer.attackTargetEntityWithCurrentItem(t.entityHit);
					} else {
						if (timer.hasReached(CCPS)) {
							timer.reset();
							mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(t.entityHit, C02PacketUseEntity.Action.ATTACK));
							mc.thePlayer.swingItem();
							mc.thePlayer.attackTargetEntityWithCurrentItem(t.entityHit);
						
						}
					}
				}
				//AutoBlock
				if (setting.getItemByName("AutoBlock").isState()) {
					if (Minecraft.getMinecraft().thePlayer.ticksExisted % 5 == 0) {
						if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null)
							Minecraft.getMinecraft().playerController.sendUseItem(
									(EntityPlayer) Minecraft.getMinecraft().thePlayer,
									(World) Minecraft.getMinecraft().theWorld,
									Minecraft.getMinecraft().thePlayer.getHeldItem());
						TimeUtils.reset();

					}
				}
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
		}
	}
	public EntityLivingBase getClosestPlayer(double distance) {
		double d0 = distance;
		EntityLivingBase entityplayer = null;
		for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (entity instanceof EntityLivingBase) {
				if(canAttack((EntityLivingBase) entity) && mc.thePlayer.canEntityBeSeen(entity)) {
					double d1 = Math.sqrt(entity.getDistanceSq(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
					if (d1 < d0) {
						d0 = d1;
						entityplayer = (EntityLivingBase) entity;
					}
				}
			}
		}
		return entityplayer;
	}

	// TODO Reworken
	private boolean canAttack(EntityLivingBase player) {
		if (player instanceof EntityPlayer || player instanceof net.minecraft.entity.passive.EntityAnimal|| player instanceof net.minecraft.entity.monster.EntityMob|| player instanceof net.minecraft.entity.passive.EntityVillager) {
			if (player instanceof EntityPlayer && !targetset.getItemByName("Player").isState())
				return false;
			if (player instanceof net.minecraft.entity.passive.EntityAnimal
					&& !targetset.getItemByName("Animals").isState())
				return false;
			if (player instanceof net.minecraft.entity.monster.EntityMob
					&& !targetset.getItemByName("Mobs").isState())
				return false;
			if (player instanceof net.minecraft.entity.passive.EntityVillager
					&& !targetset.getItemByName("Villager").isState())
				return false;
			// TODO FriendManager
			// if
			// (Atero.instance.friendManager.friends.contains(player.getName().toLowerCase()))
			// return false;
		}
		if (player.isOnSameTeam((EntityLivingBase) Minecraft.thePlayer) && targetset.getItemByName("Teams").isState())
			return false;
		if (player.isInvisible() && !targetset.getItemByName("Invisible").isState())
			return false;
		return (player != Minecraft.getMinecraft().thePlayer);
	}

	@EventTarget
	public void onPre(EventPreMotionUpdate pre) {
		if(target != null) {
			this.facing = this.getEntityRotations(Minecraft.getMinecraft().thePlayer, target, linearpredict(target));
			pre.setYaw(yaw);
			pre.setPitch(pitch);
			yaw = interpolateRotation(yaw, facing[0], 180);
			pitch = interpolateRotation(pitch, facing[1], 180);
		}else if(preaimtarget != null) {
			this.facing = this.getEntityRotations(Minecraft.getMinecraft().thePlayer, preaimtarget, linearpredict(preaimtarget));
			pre.setYaw(yaw);
			pre.setPitch(pitch);
			yaw = interpolateRotation(yaw, facing[0], 180);
			pitch = interpolateRotation(pitch, facing[1], 180);
		}
	}
	public static float getYaw() {
		return yaw;
	}

	private float interpolateRotation(float yaw, float pitch, float partialTicks) {
		float f = MathHelper.wrapAngleTo180_float(pitch - yaw);
		if (f > partialTicks)
			f = partialTicks;
		if (f < -partialTicks)
			f = -partialTicks;
		return yaw + f;
	}

	// TODO Names Reworken
	private float[] getEntityRotations(EntityPlayerSP player, EntityLivingBase target, Vec3 targetVec) {
		final double posX = targetVec.xCoord - player.posX;
		double posY = targetVec.yCoord + target.getEyeHeight() - (targetVec.yCoord + player.getEyeHeight() + 0.5);
		final double posZ = targetVec.zCoord - player.posZ;
		final double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
		float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f;
		float pitch = (float) (-(Math.atan2(posY, var14) * 180.0 / Math.PI));
		float mouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
		// Wenn kein Schaden macht
		// float f3 = mouseSensitivity * mouseSensitivity * mouseSensitivity * 1.2F;
		float sensitivityOffset = (mouseSensitivity * 3) * 1.2F;
		yaw -= yaw % sensitivityOffset;
		pitch -= pitch % (sensitivityOffset * mouseSensitivity);
		return new float[] {yaw, pitch};
	}
	
	private Vec3 linearpredict(EntityLivingBase base) {
		double x = base.posX + base.motionX;
		double y = base.posY + base.motionY;
		double z = base.posZ + base.motionZ;
		return new Vec3(x, y, z);
	}
	
	private MovingObjectPosition getTarget(float partialTicks, double distance) {
		Minecraft mc = Minecraft.getMinecraft();
		Entity pointedEntity;
		MovingObjectPosition omo = mc.renderViewEntity.rayTrace(distance, partialTicks);
		double d1 = distance;
		Vec3 vec3 = mc.renderViewEntity.getPositionEyes(partialTicks);
		Vec3 vec31 = mc.renderViewEntity.getLook(partialTicks);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
		pointedEntity = null;
		Vec3 vec33 = null;
		float f1 = 1.0F;
		List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity,
				mc.renderViewEntity.boundingBox
						.addCoord(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance)
						.expand((double) f1, (double) f1, (double) f1));
		double d2 = d1;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity = (Entity) list.get(i);

			if (entity.canBeCollidedWith()) {
				float f2 = entity.getCollisionBorderSize();
				AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double) f2, (double) f2, (double) f2);
				MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

				if (axisalignedbb.isVecInside(vec3)) {
					if (0.0D < d2 || d2 == 0.0D) {
						pointedEntity = entity;
						vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
						d2 = 0.0D;
					}
				} else if (movingobjectposition != null) {
					double d3 = vec3.distanceTo(movingobjectposition.hitVec);

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
		if (omo != null) {
			if (omo.typeOfHit == MovingObjectType.ENTITY) {
				if (omo.entityHit instanceof EntityLivingBase) {
					return omo;
				}
			}
		}
		return null;
	}
	

}
