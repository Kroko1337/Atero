package de.verschwiegener.atero.module.modules.combat;

import java.sql.Time;
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
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;

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
	TimeUtils timer = new TimeUtils();
	Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void setup() {
		super.setup();
		ArrayList<SettingsItem> items = new ArrayList<>();
		items.add(new SettingsItem("KeepSprint", true, ""));
		items.add(new SettingsItem("FakeBlock", false, ""));
		items.add(new SettingsItem("AutoBlock", false, ""));
		Management.instance.settingsmgr.addSetting(new Setting(this, items));
	}

	@Override
	public void onEnable() {
		super.onEnable();
		setting = Management.instance.settingsmgr.getSettingByName(getName());
		targetset = Management.instance.settingsmgr.getSettingByName("Target");
	}

	// TODO Settings Reworken
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (isEnabled()) {
			try {
				target = getClosest(3.0);
				
				if (target == null || Minecraft.getMinecraft().currentScreen != null) {
					return;
				}
				//Sprint
				if (!setting.getItemByName("KeepSprint").isState()) {
					Minecraft.getMinecraft().thePlayer.setSprinting(false);
				}
				//FakeBlock
				if (setting.getItemByName("FakeBlock").isState()) {
					Minecraft.getMinecraft().thePlayer.setItemInUse(Minecraft.getMinecraft().thePlayer.getHeldItem(),
							5);
				}
				float CCPS = (float) Math.random() * (140 - 130 + 1) + 130; 
				//float CCPS = (float) MathHelper.getRandomDoubleInRange(new Random(), 140, 100);
				System.out.println("CCPS: " + CCPS);
				if (target.isSwingInProgress) {
					if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit != null) {
						Minecraft.getMinecraft().clickMouse();

					}
				} else {
					if (timer.hasReached(CCPS)) {
						timer.reset();
						Minecraft.getMinecraft().clickMouse();
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
			}
		}
	}

	/// TODO Reworken
	/*private EntityLivingBase getClosest(double range) {
		double dist = range;
		EntityLivingBase target = null;
		for (Object object : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			Entity entity = (Entity) object;
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase player = (EntityLivingBase) entity;
				if (canAttack(player) && mc.thePlayer.canEntityBeSeen(player)) {
					double currentDist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity((Entity) player);
					if (currentDist <= dist) {
						dist = currentDist;
						target = player;
					}
				}
			}
		}
		return target;
	}*/
	private EntityLivingBase getClosest(double range) {
		EntityLivingBase target = null;
		double currentdist = range;
		for(Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if(entity instanceof EntityLivingBase) {
				if(canAttack((EntityLivingBase) entity) && mc.thePlayer.canEntityBeSeen(entity)) {
					double distance = mc.thePlayer.getDistanceToEntity(entity);
					if(distance < currentdist) {
						currentdist = distance;
						target = (EntityLivingBase) entity;
					}
				}
			}
		}
		return target;
	}

	// TODO Reworken
	public boolean canAttack(EntityLivingBase player) {
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
		if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity((Entity) player) > 3)
			return false;
		return (player != Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().thePlayer.getDistanceToEntity((Entity) player) <= 3);
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

	public static float interpolateRotation(float yaw, float pitch, float partialTicks) {
		float f = MathHelper.wrapAngleTo180_float(pitch - yaw);
		if (f > partialTicks)
			f = partialTicks;
		if (f < -partialTicks)
			f = -partialTicks;
		return yaw + f;
	}

	public static float getYaw() {
		return yaw;
	}

	// TODO Names Reworken
	public static float[] getEntityRotations(EntityPlayerSP player, EntityLivingBase target) {
		final double posX = target.posX - player.posX;
		final double posY = target.posY + target.getEyeHeight() - (player.posY + player.getEyeHeight() + 0.5);
		final double posZ = target.posZ - player.posZ;
		final double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
		float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f;
		float pitch = (float) (-(Math.atan2(posY, var14) * 180.0 / Math.PI));
		float mouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
		// Wenn kein Schade macht
		// float f3 = mouseSensitivity * mouseSensitivity * mouseSensitivity * 1.2F;
		float sensitivityOffset = (mouseSensitivity * 3) * 1.2F;
		yaw -= yaw % sensitivityOffset;
		pitch -= pitch % (sensitivityOffset * mouseSensitivity);
		return new float[] { yaw, pitch };
	}

}
