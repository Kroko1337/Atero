package de.verschwiegener.atero.module.modules.world;



import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;
import java.util.ArrayList;

public class Cheststealer extends Module {
    TimeUtils timeUtils;
    public Cheststealer() {
        super("ChestStealer", "ChestStealer", Keyboard.KEY_NONE, Category.World);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
        if (this.isEnabled()) {
            super.onUpdate();


            if (Minecraft.thePlayer.openContainer != null
                    && Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                ContainerChest container = (ContainerChest) Minecraft.thePlayer.openContainer;
                int i = 0;
                while (i < container.getLowerChestInventory().getSizeInventory()) {
                    if (container.getLowerChestInventory().getStackInSlot(i) != null && TimeUtils.hasReached((long) 200F)){



                        Minecraft.playerController.windowClick(container.windowId, i, 0, 1,
                                Minecraft.thePlayer);
                        TimeUtils.reset();
                    }
                    ++i;
                }
                GuiChest chest = (GuiChest) Minecraft.currentScreen;
                if (this.isChestEmpty(chest) || this.isInventoryFull()) {
                    Minecraft.thePlayer.closeScreen();

                }
            }
        }

    }


    private boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemSword
                || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood
                || itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock;
    }

    private boolean isChestEmpty(GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null && this.isValidItem(stack)) {
                return false;
            }
        }

        return true;
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }

        return true;


    }

}
