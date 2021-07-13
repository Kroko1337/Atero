package de.verschwiegener.atero.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventReceivedPacket;
import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.inventory.InventoryUtil;
import god.buddy.aot.BCompiler;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collections;

public class Cheststealer extends Module {
    public static Cheststealer instance;
    public BlockPos currentChest;
    private TimeUtils timer = new TimeUtils();
    private Setting setting;
    private ArrayList<BlockPos> positions = new ArrayList<>();

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
                    //System.out.println("Need: " + needIds);
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
    @EventTarget
    public void onUpdate(EventReceivedPacket ppe) {
        Packet p = ppe.getPacket();
        if (p instanceof C16PacketClientStatus) {
            final C16PacketClientStatus clientStatus = (C16PacketClientStatus) p;
            if (clientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                ppe.setCancelled(true);
            }
        }
    }
}
