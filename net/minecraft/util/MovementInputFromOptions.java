package net.minecraft.util;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventTest;

import de.verschwiegener.atero.module.modules.combat.Killaura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;

public class MovementInputFromOptions extends MovementInput {
    private final GameSettings gameSettings;

    Minecraft mc = Minecraft.getMinecraft();

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

	EventTest updatePlayerMovementState = new EventTest(moveForward, moveStrafe, mc.thePlayer.rotationYawHead, mc.thePlayer.rotationYaw, false, false);
	EventManager.call(updatePlayerMovementState);

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
        
	//if (updatePlayerMovementState.isSilentMoveFix() && (moveForward != 0 || moveStrafe != 0)) {
	 //   correctMovement2(moveForward, moveStrafe, EventPreMotionUpdate.getInstance.getYaw());
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

    public void getCorrectedMovement(float forward, float strafe, float yaw, float shouldYaw, boolean fixYaw) {
        float y = mc.thePlayer.rotationYaw;

        if(fixYaw) {
            y = shouldYaw;
        }

        if(fixYaw && yaw == y) return;

        // this is just the fraction stuff of mc
        float f4 = 0.91F;
        if (mc.thePlayer.onGround) {
            f4 = mc.theWorld.getBlockState(new BlockPos(MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(mc.thePlayer.posZ))).getBlock().slipperiness * 0.91F;
        }
        float f5;
        if (mc.thePlayer.onGround) {
            f5 = mc.thePlayer.getAIMoveSpeed() * (0.16277136F / (f4 * f4 * f4));
        } else {
            f5 = mc.thePlayer.jumpMovementFactor;
        }
        float f = strafe * strafe + forward * forward;
        f = f5 / f;

        float fStrafe = strafe * f;
        float fForward = forward * f;
        float realYawSin = MathHelper.sin(y * (float) Math.PI / 180.0F);
        float realYawCos = MathHelper.cos(y * (float) Math.PI / 180.0F);
        // these are the correct motion values for the current rotation (ClientSide)
        float realYawMotionX = fStrafe * realYawCos - fForward * realYawSin;
        float realYawMotionZ = fForward * realYawCos + fStrafe * realYawSin;

        float rotationYawSin = MathHelper.sin(yaw * (float) Math.PI / 180.0F);
        float rotationYawCos = MathHelper.cos(yaw * (float) Math.PI / 180.0F);

        // NaN is just used for the initial usage of the array
        float[] closest = new float[]{Float.NaN, 0, 0};

        // now go thought -1, 0 and 1 and check for the value with the lowest distance to the correct motion values
        for (int possibleStrafe = -1; possibleStrafe <= 1; possibleStrafe++) {
            for (int possibleForward = -1; possibleForward <= 1; possibleForward++) {
                float testFStrafe = possibleStrafe * f;
                float testFForward = possibleForward * f;
                float testYawMotionX = testFStrafe * rotationYawCos - testFForward * rotationYawSin;
                float testYawMotionZ = testFForward * rotationYawCos + testFStrafe * rotationYawSin;
                // calculate the distance between the test motion and the real motion
                float diffX = realYawMotionX - testYawMotionX;
                float diffZ = realYawMotionZ - testYawMotionZ;
                float distance = MathHelper.sqrt_float(diffX * diffX + diffZ * diffZ);
                // compare the distance to the stored one to fine the nearest forward and strafe values
                if (Float.isNaN(closest[0]) || distance < closest[0]) {
                    closest[0] = distance;
                    closest[1] = possibleForward;
                    closest[2] = possibleStrafe;
                }
            }
        }
        moveForward = closest[1];
        moveStrafe = closest[2];
    }

    public void correctMovement(float forward, float strafe, float yaw) {
	EntityLivingBase player = mc.thePlayer;
	
	System.err.println("Client: " + player.rotationYaw + " Server: " + yaw);

	// Calculate Server Rotation
	float serverRotationSin = MathHelper.sin(yaw * (float) Math.PI / 180.0F);
	float serverRotationCos = MathHelper.cos(yaw * (float) Math.PI / 180.0F);

	// Calculate Client MotionX and Z
	// See EntityLiving Base L.1428 ff
	float blockslipperiness = 0.91F;
	if (player.onGround) {
	    blockslipperiness = mc.theWorld.getBlockState(new BlockPos(MathHelper.floor_double(player.posX),
		    MathHelper.floor_double(player.getEntityBoundingBox().minY) - 1,
		    MathHelper.floor_double(player.posZ))).getBlock().slipperiness * 0.91F;
	}
	float slipperiness = 0.16277136F / (blockslipperiness * blockslipperiness * blockslipperiness);
	float friction;
	if (player.onGround) {
	    friction = player.getAIMoveSpeed() * slipperiness;
	} else {
	    friction = player.jumpMovementFactor;
	}
	float forwardStrafe = strafe * strafe + forward * forward;
	forwardStrafe = friction / forwardStrafe;
	strafe = strafe * forwardStrafe;
	forward = forward * forwardStrafe;
	float clientRotationSin = MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F);
	float clientRotationCos = MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F);
	float clientMotionX = strafe * clientRotationCos - forward * clientRotationSin;
	float clientMotionZ = forward * clientRotationCos + strafe * clientRotationSin;

	System.out.println("MotionX: " + (clientMotionX - player.motionX));
	System.out.println("MotionZ: " + (clientMotionZ + player.motionZ));
	
	
	// Bruteforces all Possible Combinations of 1,0,-1 and finds the one closest to
	// the client Motionx and Z
	float[] current = new float[] { 0, 0};
	float distance = Float.NaN;
	for (int allStrafe = -1; allStrafe <= 1; allStrafe++) {
	    for (int allForward = -1; allForward <= 1; allForward++) {
		//System.out.println("AllStrafe: " + allStrafe);
		//System.out.println("AllForward: " + allForward);
		//float difMotion = calculateMotionDif(allStrafe, allForward, forwardStrafe, serverRotationSin, serverRotationCos, clientMotionX, clientMotionZ);
		float strafeN = allStrafe * forwardStrafe;
		float forwardN = allForward * forwardStrafe;
		float motionX = strafeN * serverRotationCos - forwardN * serverRotationSin;
		float motionZ = forwardN * serverRotationCos + strafeN * serverRotationSin;
		float motionXdif = (clientMotionX - motionX);
		float motionZdif = (clientMotionZ - motionZ);
		float difMotion = MathHelper.sqrt_float(motionXdif * motionXdif + motionZdif * motionZdif);
		//System.out.println("DifMotion: " + difMotion);
		//System.out.println("Forward: " + allForward);
		//System.out.println("Strafe: " + allStrafe);
		if(Float.isNaN(distance) || difMotion < distance) {
		    distance = difMotion;
		    current[0] = allStrafe;
		    current[1] = allForward;
		}
	    }
	}
	System.out.println("Current: " + current[1] + "Strafe: " + current[0]);
	moveStrafe = current[0];
	moveForward = current[1];

    }
    
    public void correctMovement2(float forward, float strafe, float yaw) {
   	EntityLivingBase player = mc.thePlayer;
   	
   	System.err.println("Client: " + player.rotationYaw + " Server: " + yaw);

   	// Calculate Server Rotation
   	float serverRotationSin = MathHelper.sin(yaw * (float) Math.PI / 180.0F);
   	float serverRotationCos = MathHelper.cos(yaw * (float) Math.PI / 180.0F);

   	// Calculate Client MotionX and Z
   	// See EntityLiving Base L.1428 ff
	float f4 = 0.91F;

	if (player.onGround) {
	    f4 = mc.theWorld.getBlockState(new BlockPos(MathHelper.floor_double(player.posX),
		    MathHelper.floor_double(player.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(player.posZ)))
		    .getBlock().slipperiness * 0.91F;
	}

	float f = 0.16277136F / (f4 * f4 * f4);
	float f5;

	if (player.onGround) {
	    f5 = player.getAIMoveSpeed() * f;
	} else {
	    f5 = player.jumpMovementFactor;
	}
	float f2 = strafe * strafe + forward * forward;
	f2 = f5 / f2;
	strafe = strafe * f2;
	forward = forward * f2;
	float f1 = MathHelper.sin(player.rotationYawHead * (float) Math.PI / 180.0F);
	float f3 = MathHelper.cos(player.rotationYawHead * (float) Math.PI / 180.0F);
	float motionX = (strafe * f3 - forward * f1);
	float motionZ = (forward * f3 + strafe * f1);

   	System.out.println("MotionX: " + (motionX - player.motionX));
   	System.out.println("MotionZ: " + (motionZ + player.motionZ));
   	
   	
   	// Bruteforces all Possible Combinations of 1,0,-1 and finds the one closest to
   	// the client Motionx and Z
   	float[] current = new float[] { 0, 0};
   	float distance = Float.NaN;
   	for (int allStrafe = -1; allStrafe <= 1; allStrafe++) {
   	    for (int allForward = -1; allForward <= 1; allForward++) {
   		//System.out.println("AllStrafe: " + allStrafe);
   		//System.out.println("AllForward: " + allForward);
   		//float difMotion = calculateMotionDif(allStrafe, allForward, forwardStrafe, serverRotationSin, serverRotationCos, clientMotionX, clientMotionZ);
   		float strafeN = allStrafe * f2;
   		float forwardN = allForward * f2;
   		float motionXN = strafeN * serverRotationCos - forwardN * serverRotationSin;
   		float motionZN = forwardN * serverRotationCos + strafeN * serverRotationSin;
   		float motionXdif = (motionX - motionXN);
   		float motionZdif = (motionZ - motionZN);
   		float difMotion = MathHelper.sqrt_float(motionXdif * motionXdif + motionZdif * motionZdif);
   		//System.out.println("DifMotion: " + difMotion);
   		//System.out.println("Forward: " + allForward);
   		//System.out.println("Strafe: " + allStrafe);
   		if(Float.isNaN(distance) || difMotion < distance) {
   		    distance = difMotion;
   		    current[0] = allStrafe;
   		    current[1] = allForward;
   		}
   	    }
   	}
   	System.out.println("Current: " + current[1] + "Strafe: " + current[0]);
   	moveStrafe = current[0];
   	moveForward = current[1];

       }

    private float calculateMotionDif(int allStrafe, int allForward, float forwardStrafe, float serverRotationSin,
	    float serverRotationCos, float clientMotionX, float clientMotionZ) {
	float strafe = allStrafe * forwardStrafe;
	float forward = allForward * forwardStrafe;
	float motionX = strafe * serverRotationCos - forward * serverRotationSin;
	float motionZ = forward * serverRotationCos + strafe * serverRotationSin;
	float motionXdif = (clientMotionX - motionX);
	float motionZdif = (clientMotionZ - motionZ);
	return MathHelper.sqrt_float(motionXdif * motionXdif + motionZdif * motionZdif);
    }

}
