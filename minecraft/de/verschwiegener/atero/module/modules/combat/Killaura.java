package de.verschwiegener.atero.module.modules.combat;

import java.util.ArrayList;

import de.verschwiegener.atero.util.TimeUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.component.components.ComponentCheckBox;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.Random;

public class Killaura extends Module {

	public Killaura() {
		super("Killaura", "Killaura", Keyboard.KEY_NONE, Category.Combat);
	}

	public static EntityLivingBase target;
	public static EntityLivingBase finalTarget;
	public static float yaw;
	public static float pitch;
	private float lastYaw;
	private float lastPitch;
	public static float[] facing;
	private long delayCount;
	Setting setting, targetset;

	@Override
	public void setup() {
		super.setup();
		ArrayList<SettingsItem> items = new ArrayList<>();
		items.add(new SettingsItem("ColorPink", true, ""));
		items.add(new SettingsItem("ColorBlue", false, ""));
		Management.instance.settingsmgr.addSetting(new Setting(this, items));
	}

	@Override
	public void onEnable() {
		super.onEnable();
		setting = Management.instance.settingsmgr.getSettingByName(getName());
		targetset = Management.instance.settingsmgr.getSettingByName("Target");
	}
	//TODO Settings Reworken
	@Override
	public void onUpdate() {
		if (isEnabled()) {
			super.onUpdate();
			try {
				target = getClosest(3.0);
				if (target == null) {
					return;
				}
				if (Minecraft.getMinecraft().currentScreen == null) {
					if (target != null) {

						if (!setting.getItemByName("SPRINTKEEP").isState()) {
							Minecraft.getMinecraft().thePlayer.setSprinting(false);
						}

						if (setting.getItemByName("FAKEBLOCK").isState()) {
							Minecraft.getMinecraft().thePlayer
									.setItemInUse(Minecraft.getMinecraft().thePlayer.getHeldItem(), 5);
						}

						float CCPS = (float) MathHelper.getRandomDoubleInRange(new Random(), 140, 100);
						if (target.isSwingInProgress) {
							if (Minecraft.getMinecraft().objectMouseOver != null
									&& Minecraft.getMinecraft().objectMouseOver.entityHit != null) {
								Minecraft.getMinecraft().clickMouse();

							}
						} else {
							if (TimeUtils.hasReached(CCPS)) {
								Minecraft.getMinecraft().clickMouse();
							}

						}

					}

					if (setting.getItemByName("AB").isState()) {
						if (Minecraft.getMinecraft().thePlayer.ticksExisted % 5 == 0) {
							if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null)
								Minecraft.getMinecraft().playerController.sendUseItem(
										(EntityPlayer) Minecraft.getMinecraft().thePlayer,
										(World) Minecraft.getMinecraft().theWorld,
										Minecraft.getMinecraft().thePlayer.getHeldItem());
							TimeUtils.reset();

						}
					}
				}
			} catch (NullPointerException e) {
			}
		}
	}

	//TODO Reworken
	private EntityLivingBase getClosest(double range) {
		double dist = range;
		EntityLivingBase target = null;
		for (Object object : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			Entity entity = (Entity) object;
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase player = (EntityLivingBase) entity;
				if (canAttack(player)) {
					double currentDist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity((Entity) player);
					if (currentDist <= dist) {
						dist = currentDist;
						target = player;
					}
				}
			}
		}
		return target;
	}

	//TODO Reworken
	public boolean canAttack(EntityLivingBase player) {
		if (player instanceof EntityPlayer || player instanceof net.minecraft.entity.passive.EntityAnimal|| player instanceof net.minecraft.entity.monster.EntityMob || player instanceof net.minecraft.entity.passive.EntityVillager) {
			if (player instanceof EntityPlayer
					&& !targetset.getItemByName("KillauraPlayer").isState())
				return false;
			if (player instanceof net.minecraft.entity.passive.EntityAnimal
					&& !targetset.getItemByName("KillauraAnimals").isState())
				return false;
			if (player instanceof net.minecraft.entity.monster.EntityMob
					&& !targetset.getItemByName("KillauraMobs").isState())
				return false;
			if (player instanceof net.minecraft.entity.passive.EntityVillager
					&& !targetset.getItemByName("KillauraVillager").isState())
				return false;
			// TODO FriendManager
			// if
			// (Atero.instance.friendManager.friends.contains(player.getName().toLowerCase()))
			// return false;
		}
		if (player.isOnSameTeam((EntityLivingBase) Minecraft.thePlayer)
				&& targetset.getItemByName("KillauraTeams").isState())
			return false;
		if (player.isInvisible()
				&& !targetset.getItemByName("KillauraInvisible").isState())
			return false;
		if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity((Entity) player) > 3)
			return false;
		return (player != Minecraft.getMinecraft().thePlayer
				&& Minecraft.getMinecraft().thePlayer.getDistanceToEntity((Entity) player) <= 3);
	}

	@EventTarget
	public void onPre(EventPreMotionUpdate pre) {
		if (target != null) {
			this.facing = this.getEntityRotations(Minecraft.getMinecraft().thePlayer, target);
			pre.setYaw(yaw);
			pre.setPitch(pitch);
			yaw = interpolateRotation(yaw, facing[0], 180);
			pitch = interpolateRotation(pitch, facing[1], 180);
		}
	}

	public static float interpolateRotation(float yaw, float pitch, float maxOffset) {
		float f = MathHelper.wrapAngleTo180_float(pitch - yaw);
		if (f > maxOffset)
			f = maxOffset;
		if (f < -maxOffset)
			f = -maxOffset;
		return yaw + f;
	}

	public static float getYaw() {
		return yaw;
	}


	//TODO Names Reworken
	public static float[] getEntityRotations(EntityPlayerSP player, EntityLivingBase target) {
		final double posX = target.posX - player.posX;
		final double posY = target.posY + target.getEyeHeight() - (player.posY + player.getEyeHeight() + 0.5);
		final double posZ = target.posZ - player.posZ;
		final double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
		float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f;
		float pitch = (float) (-(Math.atan2(posY, var14) * 180.0 / Math.PI));
		float mouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
		//Wenn kein Schade macht
		//float f3 = mouseSensitivity * mouseSensitivity * mouseSensitivity * 1.2F;
		float sensitivityOffset = (mouseSensitivity * 3) * 1.2F;
		yaw -= yaw % sensitivityOffset;
		pitch -= pitch % (sensitivityOffset * mouseSensitivity);
		return new float[] { yaw, pitch };
	}

}
