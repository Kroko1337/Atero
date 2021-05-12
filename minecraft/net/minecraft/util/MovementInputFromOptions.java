package net.minecraft.util;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventTest;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.modules.combat.Killaura;
import de.verschwiegener.atero.util.TimeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;

public class MovementInputFromOptions extends MovementInput {
    private final GameSettings gameSettings;

    Minecraft mc = Minecraft.getMinecraft();
    private float lastForward, lastStrafe;
    private TimeUtils timer = new TimeUtils();

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
	this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState() {
	this.moveStrafe = 0.0F;
	this.moveForward = 0.0F;

	if (this.gameSettings.keyBindForward.isKeyDown()) {
	    ++this.moveForward;
	}

	if (this.gameSettings.keyBindBack.isKeyDown()) {
	    --this.moveForward;
	}

	if (this.gameSettings.keyBindLeft.isKeyDown()) {
	    ++this.moveStrafe;
	}

	if (this.gameSettings.keyBindRight.isKeyDown()) {
	    --this.moveStrafe;
	}
	//System.out.println("Input: ");

	//EventTest updatePlayerMovementState = new EventTest(moveForward, moveStrafe, mc.thePlayer.rotationYawHead, mc.thePlayer.rotationYaw, false, false);
	//EventManager.call(updatePlayerMovementState);

	// final UpdatePlayerMovementState updatePlayerMovementState = new
	// UpdatePlayerMovementState(moveForward, moveStrafe, getYaw(),
	// getPlayer().rotationYaw, false, false).onFire();
	//moveForward = updatePlayerMovementState.getMoveForward();
	//moveStrafe = updatePlayerMovementState.getMoveStrafe();
	
	//moveForward = updatePlayerMovementState.getMoveForward();
        //moveStrafe = updatePlayerMovementState.getMoveStrafe();
	
        //System.out.println("MoveStrafe: " + moveStrafe);
        //System.out.println("MoveForward: " + moveForward);
        //System.out.println("Silent: " + updatePlayerMovementState.getYaw());
	
	//System.out.println("MoveSTrafe1: " + moveStrafe + " Forward1: " + moveForward);
	
	//if(useCorrectMovement() && (moveForward != 0 || moveStrafe != 0) && timer.hasReached(100)) {
	    //timer.reset();
	   // mc.thePlayer.setSprinting(false);
	    //boolean last = testlast(moveForward, moveStrafe);
	    //System.out.println("Last: " + last);
	    //if(!last) {
		//testfix(moveForward, moveStrafe);
	    //}
	//}
	//System.out.println("MoveSTrafe2: " + moveStrafe + " Forward2: " + moveForward);
        
	//if (updatePlayerMovementState.isSilentMoveFix() && (moveForward != 0 || moveStrafe != 0)) {
	//    correctMovement2(moveForward, moveStrafe, EventPreMotionUpdate.getInstance.getYaw());
	    //getCorrectedMovement(moveForward, moveStrafe, EventPreMotionUpdate.getInstance.getYaw(), 0, false);
	//}

	this.jump = this.gameSettings.keyBindJump.isKeyDown();
	this.sneak = this.gameSettings.keyBindSneak.isKeyDown();

	if (this.sneak) {
	    this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
	    this.moveForward = (float) ((double) this.moveForward * 0.3D);
	}
	//System.out.println("MoveStrafe: " + moveStrafe);
       //System.out.println("MoveForward: " + moveForward);
    }
    
    private boolean useCorrectMovement() {
	return(Management.instance.modulemgr.getModuleByName("Killaura").isEnabled() && Killaura.instance.getSetting().getItemByName("CorrectMM").isState() && Killaura.instance.getTarget() != null);
    }
    
    private boolean testlast(float forward, float strafe) {
	
	if(Float.isNaN(lastForward) || Float.isNaN(lastStrafe)) {
	    return false;
	}
	
	
	float slipperiness = 0.91F;
	if (mc.thePlayer.onGround) {
	    slipperiness = mc.theWorld.getBlockState(new BlockPos(MathHelper.floor_double(mc.thePlayer.posX),
		    MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(mc.thePlayer.posZ)))
		    .getBlock().slipperiness * 0.91F;
	}

	float moveSpeedOffset = 0.16277136F / (slipperiness * slipperiness * slipperiness);
	float friction;

	if (mc.thePlayer.onGround) {
	    friction = mc.thePlayer.getAIMoveSpeed() * moveSpeedOffset;
	} else {
	    friction = mc.thePlayer.jumpMovementFactor;
	}
	
	float f = strafe * strafe + forward * forward;
	
	f = friction / f;
	
	
	float clientStrafe = strafe * f;
	float clientForward = forward * f;
	float clientRotationSin = MathHelper.sin(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F) - 90;
	float clientRotationCos = MathHelper.cos(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
	float clientMotionX = (clientStrafe * clientRotationCos - clientForward * clientRotationSin);
	float clientMotionZ = (clientForward * clientRotationCos + clientStrafe * clientRotationSin);
	
	//System.out.println("MotionXDif: " + (clientMotionX - mc.thePlayer.motionX));
	//System.out.println("MotionZDif: " + (clientMotionZ - mc.thePlayer.motionZ));
	
	float serverRotationSin = MathHelper.sin(EventPreMotionUpdate.getInstance.getYaw() * (float) Math.PI / 180.0F) - 90;
	float serverRotationCos = MathHelper.cos(EventPreMotionUpdate.getInstance.getYaw() * (float) Math.PI / 180.0F);
	
	//System.out.println("F: " + f);
	
	float calcStrafe = lastStrafe * f;
	float calcForward = lastForward * f;
	
	//System.err.println("CalsStrafe: " + calcStrafe + " F: " + calcForward);
	
	float calcMotionX = (calcStrafe * serverRotationCos - calcForward * serverRotationSin);
	float calcMotionZ = (calcForward * serverRotationCos + calcStrafe * serverRotationSin);
	
	//System.err.println("CalcMotion: " + calcMotionX + " Z: " + calcMotionZ);
	
	float diffMotionX = calcMotionX - clientMotionX;
	float diffMotionZ = calcMotionZ - clientMotionZ;
	
	//System.out.println("DifMotion: " + diffMotionX + " Z: " + diffMotionZ);
	
	float distance = MathHelper.sqrt_float(diffMotionZ * diffMotionZ + diffMotionX * diffMotionX);
	
	System.out.println("Distance: " + distance);
	
	if(distance < 0.19) {
	    moveForward = lastForward;
	    moveStrafe = lastStrafe;
	    return true;
	}
	return false;
	
    }
    
    
    private void testfix(float forward, float strafe) {
	float slipperiness = 0.91F;
	if (mc.thePlayer.onGround) {
	    slipperiness = mc.theWorld.getBlockState(new BlockPos(MathHelper.floor_double(mc.thePlayer.posX),
		    MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(mc.thePlayer.posZ)))
		    .getBlock().slipperiness * 0.91F;
	}

	float moveSpeedOffset = 0.16277136F / (slipperiness * slipperiness * slipperiness);
	float friction;

	if (mc.thePlayer.onGround) {
	    friction = mc.thePlayer.getAIMoveSpeed() * moveSpeedOffset;
	} else {
	    friction = mc.thePlayer.jumpMovementFactor;
	}
	
	float f = strafe * strafe + forward * forward;
	
	f = friction / f;
	
	
	float clientStrafe = strafe * f;
	float clientForward = forward * f;
	float clientRotationSin = MathHelper.sin(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F) - 90;
	float clientRotationCos = MathHelper.cos(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
	float clientMotionX = (clientStrafe * clientRotationCos - clientForward * clientRotationSin);
	float clientMotionZ = (clientForward * clientRotationCos + clientStrafe * clientRotationSin);
	
	//System.out.println("MotionXDif: " + (clientMotionX - mc.thePlayer.motionX));
	//System.out.println("MotionZDif: " + (clientMotionZ - mc.thePlayer.motionZ));
	
	float serverRotationSin = MathHelper.sin(EventPreMotionUpdate.getInstance.getYaw() * (float) Math.PI / 180.0F) - 90;
	float serverRotationCos = MathHelper.cos(EventPreMotionUpdate.getInstance.getYaw() * (float) Math.PI / 180.0F);
	
	float smalestDistance = Float.NaN;
	float posibleForward = 0;
	float posibleStrafe = 0;

	 for (int strafevalue = -1; strafevalue <= 1; strafevalue++) {
	    for (int forwardvalue = -1; forwardvalue <= 1; forwardvalue++) {
		float calcStrafe = strafevalue * f;
		float calcForward = forwardvalue * f;
		float calcMotionX = (calcStrafe * serverRotationCos - calcForward * serverRotationSin);
		float calcMotionZ = (calcForward * serverRotationCos + calcStrafe * serverRotationSin);
		
		float diffMotionX = calcMotionX - clientMotionX;
		float diffMotionZ = calcMotionZ - clientMotionZ;
		float distance = MathHelper.sqrt_float(diffMotionX * diffMotionX + diffMotionZ * diffMotionZ);
		//System.out.println("Distance: " + distance + " Smalest: " + smalestDistance);
		if(Float.isNaN(smalestDistance) || distance < smalestDistance) {
		    posibleForward = forwardvalue;
		    posibleStrafe = strafevalue;
		    smalestDistance = distance;
		}
	    }
	}
	
	moveForward = posibleForward;
	moveStrafe = posibleStrafe;
	lastForward = posibleForward;
	lastStrafe = posibleStrafe;
    }
}
