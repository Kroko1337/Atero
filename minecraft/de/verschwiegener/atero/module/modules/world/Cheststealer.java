package de.verschwiegener.atero.module.modules.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.inventory.InventoryUtil;
import god.buddy.aot.BCompiler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Cheststealer extends Module {
    private TimeUtils timer = new TimeUtils();
    private Setting setting;
    private ArrayList<BlockPos> positions = new ArrayList<>();
    public static Cheststealer instance;
    public BlockPos currentChest;

    public Cheststealer() {
	super("ChestStealer", "ChestStealer", Keyboard.KEY_NONE, Category.World);
	instance = this;
    }

    private boolean isInventoryFull() {
	for (int index = 9; index <= 44; ++index) {
	    final ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
	    if (stack == null)
		return false;
	}
	return true;
    }

    private boolean isValidItem(final ItemStack itemStack) {
	return itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemSword
		|| itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood
		|| itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock;
    }

    @Override
    public void onDisable() {
	super.onDisable();
    }

    @Override
    public void onEnable() {
	super.onEnable();
	positions.clear();
    }

    @Override
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
	if (isEnabled()) {
	    super.onUpdate();
	    if (mc.thePlayer.openContainer instanceof ContainerChest) {
		final ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
		float delay = setting.getItemByName("Delay").getCurrentValue();
		boolean intelligent = setting.getItemByName("Intelligent").isState();
		boolean random = setting.getItemByName("Random").isState();
		if (timer.hasReached(delay)) {
		    timer.reset();
		    ArrayList<Integer> needIds = InventoryUtil.getneedItems(chest, intelligent);
		    if (!needIds.isEmpty() && !isInventoryFull()) {
			if (random) {
			    Collections.shuffle(needIds);
			}
			ItemStack itemStack = chest.getLowerChestInventory().getStackInSlot(needIds.get(0));
			if (intelligent && InventoryUtil.compareItem(itemStack)) {
			    Minecraft.playerController.windowClick(chest.windowId, needIds.get(0), 0, 1,
				    Minecraft.thePlayer);
			} else if (!intelligent) {
			    Minecraft.playerController.windowClick(chest.windowId, needIds.get(0), 0, 1,
				    Minecraft.thePlayer);
			}
		    } else {
			System.out.println("BlockPos: " + currentChest);
			Minecraft.thePlayer.closeScreen();
			positions.add(currentChest);
			
		    }
		}
	    }
	}

    }

    @Override
    public void setup() {
	super.setup();
	final ArrayList<SettingsItem> items = new ArrayList<>();
	items.add(new SettingsItem("Delay", 0, 500, 10, ""));
	items.add(new SettingsItem("Random", false, ""));
	items.add(new SettingsItem("Intelligent", true, ""));
	Management.instance.settingsmgr.addSetting(new Setting(this, items));
	setting = Management.instance.settingsmgr.getSettingByName(getName());
    }

}
