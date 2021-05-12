package de.verschwiegener.atero.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.module.modules.combat.Killaura;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Speed extends Module {

	private Setting setting;

	public Speed() {
		super("Speed", "Speed", Keyboard.KEY_NONE, Category.Movement);
	}

	public void onEnable() {

		super.onEnable();
		setting = Management.instance.settingsmgr.getSettingByName(getName());
	}

	public void onDisable() {
		Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
		Minecraft.getMinecraft().timer.timerSpeed = 1F;
		super.onDisable();
	}

	@Override
	public void setup() {
		super.setup();
		final ArrayList<SettingsItem> items = new ArrayList<>();
		final ArrayList<String> modes = new ArrayList<>();
		modes.add("HypixelOnGround");
		modes.add("Hypixel");
		modes.add("Cubecraft");
		modes.add("Cubecraft1vs1");
		modes.add("Vanilla");
		modes.add("Vanilla2");
		items.add(new SettingsItem("Modes", modes, "HypixelOnGround", "", ""));
		Management.instance.settingsmgr.addSetting(new Setting(this, items));
	}

	@BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
	public void onUpdate() {
		if (this.isEnabled()) {
			super.onUpdate();
			String mode = setting.getItemByName("Modes").getCurrent();
			switch (mode) {
				case "HypixelOnGround":
					hypixelOnGround();
					break;

				case "Cubecraft":
					boolean boost = (Math.abs(mc.thePlayer.rotationYawHead - mc.thePlayer.rotationYaw) < 90.0F);
					if (mc.thePlayer.onGround) {
						Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = true;
					} else {
						if (mc.thePlayer.fallDistance > 0.08F) {
							mc.timer.timerSpeed =1.0F;
						}else{
							mc.timer.timerSpeed =1.5F;
						}

						double currentSpeed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
						double speed = boost ? 1 : 1D;
						double direction = getDirection();
						mc.thePlayer.motionX = -Math.sin(direction) * speed * currentSpeed;
						mc.thePlayer.motionZ = Math.cos(direction) * speed * currentSpeed;

					}

					break;

				case "Cubecraft1vs1":
					boolean boost2 = (Math.abs(mc.thePlayer.rotationYawHead - mc.thePlayer.rotationYaw) < 90.0F);
					if (mc.thePlayer.onGround) {

						Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = true;
					}else{
						mc.timer.timerSpeed = 1F;
						double currentSpeed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
						double speed = boost2 ? 1 : 1D;
						double direction = getDirection();
						mc.thePlayer.motionX = -Math.sin(direction) * speed * currentSpeed;
						mc.thePlayer.motionZ = Math.cos(direction) * speed * currentSpeed;

					}

					break;
				case "Vanilla":
					if (Minecraft.thePlayer.onGround) {
						Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = true;
					}else{
						HighJump.setSpeed(1.0);
					}
					break;
				case "Hypixel":
					if (mc.thePlayer.onGround) {
						mc.timer.timerSpeed =3F;
						Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = true;
						if (Killaura.instance.getTarget() != null) {
							mc.timer.timerSpeed = 1F;
						}
					}else{
						if (mc.thePlayer.fallDistance > 0.05F) {
							mc.timer.timerSpeed =1.0F;
						}else{

						}
						if (Killaura.instance.getTarget() != null) {
							//mc.timer.timerSpeed = 1F;
						}

						if (Killaura.instance.getTarget() != null) {
							//	mc.timer.timerSpeed = 1F;
						}
					}
					break;
				case "Vanilla2":
					if (Minecraft.thePlayer.onGround) {
						Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = true;
					}else{
						HighJump.setSpeed(0.5);
					}
					break;
			}
		}
	}

	private void hypixelOnGround() {
		boolean boost = (Math.abs(mc.thePlayer.rotationYawHead - mc.thePlayer.rotationYaw) < 90.0F);
		if (Minecraft.getMinecraft().gameSettings.keyBindForward.pressed
				|| Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed
				|| Minecraft.getMinecraft().gameSettings.keyBindRight.pressed
				|| Minecraft.getMinecraft().gameSettings.keyBindBack.pressed) {
			Minecraft.getMinecraft().gameSettings.keyBindSprint.pressed = true;

			if (Minecraft.thePlayer.onGround) {
				Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
				// Minecraft.thePlayer.jump();
				float TIMMER1 = (float) MathHelper.getRandomDoubleInRange(new Random(), 4.0, 1.0);
				if (Minecraft.getMinecraft().thePlayer.onGround) {

					if (Minecraft.thePlayer.ticksExisted % 4 == 0) {
						Minecraft.getMinecraft().timer.timerSpeed = 10;
					} else {
						Minecraft.getMinecraft().timer.timerSpeed = 2;
					}
					if (Minecraft.thePlayer.ticksExisted % 10 == 0) {
						Minecraft.getMinecraft().timer.timerSpeed = 1;
					}
					if (Killaura.instance.getTarget() != null) {

						if (mc.thePlayer.onGround) {
							Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = true;
							mc.timer.timerSpeed = 1F;
						}
					}
				}
			}}

	}
	public void addMotion(double speed, float yaw) {
		Minecraft.thePlayer.motionX -= (MathHelper.sin((float) Math.toRadians(yaw)) * speed);
		Minecraft.thePlayer.motionZ += (MathHelper.cos((float) Math.toRadians(yaw)) * speed);
	}
	public void setSpeed(double speed, float yaw) {
		Minecraft.thePlayer.motionX = -Math.sin(Math.toRadians(getDirection(yaw))) * speed;
		Minecraft.thePlayer.motionZ = Math.cos(Math.toRadians(getDirection(yaw))) * speed;
	}
	public float getDirection(float rotationYaw) {
		float left = Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed ? Minecraft.getMinecraft().gameSettings.keyBindBack.pressed ? 45 : Minecraft.getMinecraft().gameSettings.keyBindForward.pressed ? -45 : -90 : 0;
		float right = Minecraft.getMinecraft().gameSettings.keyBindRight.pressed ? Minecraft.getMinecraft().gameSettings.keyBindBack.pressed ? -45 : Minecraft.getMinecraft().gameSettings.keyBindForward.pressed ? 45 : 90 : 0;
		float back = Minecraft.getMinecraft().gameSettings.keyBindBack.pressed ? + 180 : 0;
		float yaw = left + right + back;
		return rotationYaw + yaw;
	}
	public static float getDirection() {
		Minecraft mc = Minecraft.getMinecraft();
		float var1 = mc.thePlayer.rotationYaw;

		if (mc.thePlayer.moveForward < 0.0F) {
			var1 += 180.0F;
		}

		float forward = 1.0F;

		if (mc.thePlayer.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.thePlayer.moveForward > 0.0F) {
			forward = 0.5F;
		}

		if (mc.thePlayer.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}

		if (mc.thePlayer.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}

		var1 *= 0.017453292F;
		return var1;
	}

}
