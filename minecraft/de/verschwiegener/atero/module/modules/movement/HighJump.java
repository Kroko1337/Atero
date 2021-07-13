package de.verschwiegener.atero.module.modules.movement;

import net.minecraft.client.Minecraft;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.Util;
import god.buddy.aot.BCompiler;

public class HighJump extends Module {
    TimeUtils timeUtils;
    private double posY;
    boolean Dmg = false;
    boolean Jump = false;

    public HighJump() {
	super("HighJump", "HighJump", Keyboard.KEY_NONE, Category.Movement);
    }

    public void onEnable() {
	// posY = mc.thePlayer.posY;
	for (double distance = 0.06; distance < 3.075; distance += 0.06) {
	    Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
		    Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.06, Minecraft.thePlayer.posZ, false));
	    distance += 0.1E-8D;
	    Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
		    Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1E-8D, Minecraft.thePlayer.posZ, false));
	}
	Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
		Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
	Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
	mc.thePlayer.jump();

	super.onEnable();
    }

    public void onDisable() {
	Minecraft.thePlayer.speedInAir = 0.02F;
	Minecraft.getMinecraft().timer.timerSpeed = 1F;
	Dmg = false;
	Jump = false;
	super.onDisable();
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {

	if (this.isEnabled()) {
	    super.onUpdate();
	    // mc.thePlayer.posY = mc.thePlayer.prevPosY;

	    if (this.isEnabled()) {
		super.onUpdate();
		// mc.thePlayer.posY = mc.thePlayer.prevPosY;

		if (Minecraft.thePlayer.onGround)
		    Jump = true;
		if (Minecraft.thePlayer.onGround)
		    mc.gameSettings.keyBindJump.pressed = true;
		if (Minecraft.thePlayer.onGround) {
		    mc.timer.timerSpeed = 0.2F;
		} else if (Minecraft.thePlayer.hurtTime > 0) {
		}
		if (mc.thePlayer.ticksExisted % 2 == 0) {
		    mc.timer.timerSpeed = 1F;
		} else {
		    mc.timer.timerSpeed = 0.6F;
		}

		if (Minecraft.thePlayer.hurtTime > 0)
		    Dmg = true;

		if (Dmg)
		    if (Minecraft.thePlayer.hurtTime > 0) {

			// Minecraft.thePlayer.motionY = 0;
			Minecraft.thePlayer.motionY += 0.0099;
		    }
		if (Dmg)
		    if (Minecraft.thePlayer.hurtTime > 0) {
			mc.gameSettings.keyBindForward.pressed = true;
			Util.setSpeed( 0.4D);
		    }
	    }
	}

	if (Dmg)
	    if (Minecraft.thePlayer.hurtTime > 0) {

		// Minecraft.thePlayer.motionY = 0;
		Minecraft.thePlayer.motionY += 0.0035;
	    }
	if (Dmg)
	    if (Minecraft.thePlayer.hurtTime > 0) {
		mc.gameSettings.keyBindForward.pressed = true;
		Util.setSpeed( 0.4D);
	    }
    }
    // }
    // mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
    // mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction());
    // }

    // mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
    // mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction());
    // }
}
