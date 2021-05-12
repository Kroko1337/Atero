package de.verschwiegener.atero.module.modules.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.text.html.parser.Entity;

import god.buddy.aot.BCompiler;
import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPostMotionUpdate;
import com.darkmagician6.eventapi.events.callables.EventPreMotionUpdate;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.chat.ChatUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class Scaffold extends Module {

    public static Scaffold instance;
    Minecraft mc = Minecraft.getMinecraft();
    private BlockData blockData;
    private float[] lastrotation;
    public static Setting setting;
    private int[] forbiddenBlocks = {5};
    public boolean allowdown;
    private TimeUtils timer = new TimeUtils();
    private long lastSneak;
    private boolean downenabled;
    private double sameY;
    

    public Scaffold() {
	super("Scaffold", "Scaffold", Keyboard.KEY_NONE, Category.World);
	instance = this;
    }

    public void setup() {
	final ArrayList<SettingsItem> items = new ArrayList<>();
	items.add(new SettingsItem("Delay", 0, 500, 0, ""));
	items.add(new SettingsItem("Safewalk", false, ""));
	items.add(new SettingsItem("Sprint", false, ""));
	items.add(new SettingsItem("Sneak", false, ""));
	items.add(new SettingsItem("Silent", true, "SilentModes"));
	ArrayList<String> modes = new ArrayList<>();
	modes.add("Switch");
	modes.add("Spoof");
	items.add(new SettingsItem("SilentModes", modes, "Switch", "", ""));
	items.add(new SettingsItem("Down", false, ""));
	items.add(new SettingsItem("SameY", false, ""));
	Management.instance.settingsmgr.addSetting(new Setting(this, items));
    }

    @Override
    public void onEnable() {
	super.onEnable();
	setting = Management.instance.settingsmgr.getSettingByName(getName());
	allowdown = (setting.getItemByName("Down").isState()) && !mc.gameSettings.keyBindJump.isKeyDown() ? true : false;
	lastSneak = 0;
	sameY = mc.thePlayer.posY;
    }
    public void onDisable() {
	super.onDisable();
	 mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @EventTarget
    public void onPre(EventPreMotionUpdate pre) {
	if (isEnabled()) {
	    
	    if (!setting.getItemByName("Sprint").isState()) {
		mc.thePlayer.setSprinting(false);
	    }

	    blockData = find(new Vec3(0, 0, 0));
	    if (blockData != null || lastrotation != null) {
		
		if(setting.getItemByName("Down").isState()) {
		    if(mc.gameSettings.keyBindSneak.isPressed()) {
			    if(System.currentTimeMillis() - lastSneak < 250) {
				downenabled = !downenabled;
				ChatUtil.sendMessageWithPrefix("Down Scaffold is now " + ((downenabled) ? "enabled" : "disabled"));
			    }
			    lastSneak = System.currentTimeMillis();
			}
		}
		
		allowdown = (setting.getItemByName("Down").isState() && downenabled) && !mc.gameSettings.keyBindJump.isKeyDown() ? true : false;
		
		float[] rotation = (blockData == null) ? lastrotation : rotations(blockData);
		pre.setYaw(rotation[0]);
		pre.setPitch(rotation[1]);
		//mc.thePlayer.motionX *= 0.9D;
	        //mc.thePlayer.motionZ *= 0.9D;
		lastrotation = rotation;
	    }
	}
    }

    @EventTarget
    public void onPost(EventPostMotionUpdate post) {
	if (isEnabled()) {

	    if (!setting.getItemByName("Sprint").isState()) {
		mc.thePlayer.setSprinting(false);
	    }
	    
	    MovingObjectPosition mvp = mc.thePlayer.rayTrace(3, mc.timer.elapsedPartialTicks);
	    if (mvp.typeOfHit == MovingObjectType.BLOCK && timer.hasReached(setting.getItemByName("Delay").getCurrentValue())) {
		timer.reset();
		
		
		if(setting.getItemByName("Sneak").isState()) {
		    BlockPos blockPos = new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0D,
	                    mc.thePlayer.posZ);
	            mc.gameSettings.keyBindSneak.pressed = mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air;
		}
		
		float x = (float) (mvp.hitVec.xCoord - blockData.getPos().getX());
		float y = (float) (mvp.hitVec.yCoord - blockData.getPos().getY());
		float z = (float) (mvp.hitVec.zCoord - blockData.getPos().getZ());

		int currentslot = mc.thePlayer.inventory.currentItem;
		int blockSlot = 0;
		String mode = setting.getItemByName("SilentModes").getCurrent();
		if (setting.getItemByName("Silent").isState()) {
		    
		    switch (mode) {
		    case "Switch":
			blockSlot = getBlockSlot();
			mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot));
			mc.thePlayer.inventory.currentItem = blockSlot;
			mc.playerController.updateController();
			break;

		    case "Spoof":
			if (blockSlot != -1) {
		            if (mc.thePlayer.inventory.getStackInSlot(blockSlot) == null || !(mc.thePlayer.inventory.getStackInSlot(blockSlot).getItem() instanceof ItemBlock))
		        	blockSlot = -1;
		        }
		        if (blockSlot == -1) {
		            blockSlot = getBlockSlot();
		            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(blockSlot));
		            
		        }
		        
		        Minecraft.getMinecraft().rightClickMouse(blockSlot);
		        
			break;
		    }
		}
		
		if((setting.getItemByName("Silent").isState() && mode == "Switch") || !setting.getItemByName("Silent").isState()) {
		    if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock && !Arrays.asList(forbiddenBlocks).contains(mc.thePlayer.inventory.getCurrentItem().getItem())) {
			    if (mc.playerController.onPlayerRightClick(Minecraft.thePlayer, mc.theWorld,
		                        mc.thePlayer.getHeldItem(), mvp.getBlockPos(), blockData.getFacing(), mvp.hitVec)) {
		                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
			    }
			}
		}
		
		if(setting.getItemByName("Sneak").isState()) {
			mc.gameSettings.keyBindSneak.pressed = false;
		}
		
		if (setting.getItemByName("Silent").isState()) {
		    mc.thePlayer.inventory.currentItem = currentslot;
		    mc.playerController.updateController();
		}
	    }
	}
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack s = Minecraft.thePlayer.inventoryContainer.getSlot(36 + i).getStack();
            if (s != null && s.getItem() instanceof ItemBlock && !Arrays.asList(forbiddenBlocks).contains(s.getItem().getBlockId()))
                return i;
        }
        return 0;
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
	public static boolean canSafewalk() {
		try {
			if(!instance.allowdown && setting.getItemByName("Safewalk").isState() && instance.blockData != null && Minecraft.getMinecraft().thePlayer.onGround) {
				if (Minecraft.thePlayer.hurtTime > 0) {
					return false;
				}
				return true;
			}
			return false;
		}catch(NullPointerException ex) {

		}
		return false;
	}
    
    public void onRender() {
    }
    
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private float[] rotations(Scaffold.BlockData blockData) {
	double x = blockData.getPos().getX() + 0.5D - mc.thePlayer.posX
		+ blockData.getFacing().getFrontOffsetX() / 2.0D;
	double z = blockData.getPos().getZ() + 0.5D - mc.thePlayer.posZ
		+ blockData.getFacing().getFrontOffsetZ() / 2.0D;
	double y = blockData.getPos().getY() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + 0.9;
	double ymax = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
	double allmax = MathHelper.sqrt_double(x * x + z * z);
	float ROTTS4 = (float) MathHelper.getRandomDoubleInRange(new Random(), 180, 178);

	float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
	float pitch = (float) (-Math.atan2(y, allmax) * ROTTS4 / Math.PI);
	//if (yaw < 0.0F)
	    //yaw += 360.0F;
	float f2 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
	float f3 = f2 * f2 * f2 * 1.2F;
	yaw -= yaw % f3;
	pitch -= pitch % (f3 * f2);
	return new float[] { yaw, pitch };
    }

    

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    Scaffold.BlockData find(Vec3 offset3) {
	try {
	    double x = mc.thePlayer.posX;
	    double y = setting.getItemByName("SameY").isState() ? sameY : mc.thePlayer.posY + (allowdown ? -1 : 0);
	    double z = mc.thePlayer.posZ;
	    EnumFacing[] invert = new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH,
		    EnumFacing.EAST, EnumFacing.WEST };
	    BlockPos position = new BlockPos(new Vec3(x, y, z).add(offset3)).offset(EnumFacing.DOWN);
	    for (EnumFacing facing : EnumFacing.values()) {
		BlockPos offset = position.offset(facing);
		if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir
			|| rayTrace(mc.thePlayer.getLook(0.0f), getPositionByFace(offset, invert[facing.ordinal()])))
		    continue;
		return new Scaffold.BlockData(invert[facing.ordinal()], offset);
	    }
	    BlockPos[] offsets = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1),
		    new BlockPos(0, 0, 1), new BlockPos(0, 0, 2), new BlockPos(0, 0, -2), new BlockPos(2, 0, 0),
		    new BlockPos(-2, 0, 0) };
	    for (BlockPos offset : offsets) {
		BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
		if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir))
		    continue;
		for (EnumFacing facing : EnumFacing.values()) {
		    BlockPos offset2 = offsetPos.offset(facing);
		    if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir || rayTrace(
			    mc.thePlayer.getLook(0.01f), getPositionByFace(offset, invert[facing.ordinal()])))
			continue;
		    return new Scaffold.BlockData(invert[facing.ordinal()], offset2);
		}
	    }
	} catch (NullPointerException e) {

	}
	return null;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
	Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0,
		(double) facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
	Vec3 point = new Vec3((double) position.getX() + 0.5, (double) position.getY() + 0.5,
		(double) position.getZ() + 0.5);
	return point.add(offset);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private boolean rayTrace(Vec3 origin, Vec3 position) {
	Vec3 difference = position.subtract(origin);
	int steps = 20;
	double x = difference.xCoord / (double) 10;
	double y = difference.yCoord / (double) 10;
	double z = difference.zCoord / (double) 10;
	Vec3 point = origin;
	for (int i = 0; i < steps; ++i) {
	    BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
	    IBlockState blockState = mc.theWorld.getBlockState(blockPosition);
	    if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir)
		continue;
	    AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox(mc.theWorld, blockPosition,
		    blockState);
	    if (boundingBox == null) {
		boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	    }
	    if (!boundingBox.offset(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ())
		    .isVecInside(point))
		continue;
	    return true;
	}
	return false;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public class BlockData {
	private EnumFacing facing;
	private BlockPos pos;

	public BlockData(EnumFacing facing, BlockPos pos) {
	    this.facing = facing;
	    this.pos = pos;
	}

	public EnumFacing getFacing() {
	    return facing;
	}

	public BlockPos getPos() {
	    return pos;
	}
    }

}
