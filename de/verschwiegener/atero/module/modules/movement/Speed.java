package de.verschwiegener.atero.module.modules.movement;


import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.module.modules.combat.Killaura;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;
import java.util.Random;

public class Speed extends Module {
	TimeUtils timeUtils;
	public Speed() {
		super("Speed", "Speed", Keyboard.KEY_NONE, Category.Movement);
	}

	public void onEnable() {
			
		super.onEnable();
	}

	public void onDisable() {
		Minecraft.getMinecraft().timer.timerSpeed = 1F;
		super.onDisable();
	}
	 @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
	public void onUpdate() {
		if (this.isEnabled()) {
			super.onUpdate();
			 if (Minecraft.getMinecraft().gameSettings.keyBindForward.pressed || Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed
		                || Minecraft.getMinecraft().gameSettings.keyBindRight.pressed || Minecraft.getMinecraft().gameSettings.keyBindBack.pressed) {
				 Minecraft.getMinecraft().gameSettings.keyBindSprint.pressed = true;

		            if (Minecraft.thePlayer.onGround) {
		            	Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
		           //     Minecraft.thePlayer.jump();
		                float TIMMER1 = (float) MathHelper.getRandomDoubleInRange(new Random(), 4.0, 1.0);
		            if(Minecraft.getMinecraft().thePlayer.onGround){

		                if (Minecraft.thePlayer.ticksExisted % 25 == 0) {
		                	Minecraft.getMinecraft().timer.timerSpeed = 80;
		                }else{
		                	Minecraft.getMinecraft().timer.timerSpeed = 4;
		                }
		                if (Minecraft.thePlayer.ticksExisted % 10 == 0) {
		                	Minecraft.getMinecraft().timer.timerSpeed = 1;
		                }
		                if(Killaura.target !=null){
		                	Minecraft.getMinecraft().timer.timerSpeed= 1.0F;
		                 }
		                }

		                //Minecraft.thePlayer.motionY -= 0.02099D;
		            }
		        }
				}
			}
		}


