package de.verschwiegener.atero.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventReceivedPacket;
import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.module.modules.combat.Killaura;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.TimeUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InventoryManager extends Module {
    private static int[] bestArmorDamageReducement;
    private final TimeUtils timer = new TimeUtils();
    private final TimeUtils timer2 = new TimeUtils();
    private final List<Integer> trash = new ArrayList<>();
    private int[] bestArmorSlots;
    private float bestSwordDamage;
    private int bestSwordSlot;
    private boolean armor;
    private Setting setting, targetset;

    public InventoryManager() {
        super("InvManager", "InvManager", Keyboard.KEY_NONE, Category.Player);
    }

    public void onEnable() {
        armor = true;
        this.timer2.reset();
        this.timer.reset();
    }

    public void onDisable() {
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        searchForItems();

        if (((mc.currentScreen == null &&!Management.instance.modulemgr.getModuleByName("Scaffold").isEnabled()|| mc.currentScreen instanceof GuiInventory) && !setting.getItemByName("OpenInv").isState() &&!Management.instance.modulemgr.getModuleByName("Scaffold").isEnabled())
                || (setting.getItemByName("OpenInv").isState() && mc.currentScreen instanceof GuiInventory)) {

            float delay = setting.getItemByName("Delay").getCurrentValue();
            for (int i = 0; i < 4; i++) {
                if (bestArmorSlots[i] != -1) {
                    armor = true;
                    final int bestSlot = bestArmorSlots[i];
                    final ItemStack prevArmor = mc.thePlayer.inventory.armorItemInSlot(i);
                    // Armor
                    if (prevArmor != null && prevArmor.getItem() != null) {
                        if (timer.hasReached(delay)) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 8 - i, 0, 1, mc.thePlayer);

                            timer.reset();
                        }
                    }

                    // Armor
                    if (timer.hasReached((long) delay)) {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSlot < 9 ? bestSlot + 36 : bestSlot,
                                0, 1, mc.thePlayer);
                        timer.reset();
                    }
                } else {
                    armor = false;
                }
            }
            if (armor) return;

            searchForTrash();
            Collections.shuffle(trash);
            // Cleaning
            for (final Integer integer : trash) {
                if (timer2.hasReached((long) delay)) {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, integer < 9 ? integer + 36 : integer,
                            1, 4, mc.thePlayer); //mode 4 = drop all

                    timer2.reset();
                }
            }
            // Sorting
            if (trash.isEmpty() && bestSwordSlot != -1 && bestSwordDamage != -1) {
                if (timer.hasReached((long) delay)) {

                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSwordSlot < 9 ? bestSwordSlot + 36
                            : bestSwordSlot, 0, 2, mc.thePlayer); //mouse button clicked = slot of hotbar
                    timer.reset();
                }
            }
        }
    }


    private void searchForItems() {
        bestArmorDamageReducement = new int[4];
        bestArmorSlots = new int[4];
        bestSwordDamage = -1;
        bestSwordSlot = -1;

        Arrays.fill(bestArmorDamageReducement, -1);
        Arrays.fill(bestArmorSlots, -1);

        for (int i = 0; i < bestArmorSlots.length; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.armorItemInSlot(i);

            if (itemStack != null && itemStack.getItem() != null) {
                if (itemStack.getItem() instanceof ItemArmor) {
                    final ItemArmor armor = (ItemArmor) itemStack.getItem();
                    final int armorType = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
                    bestArmorDamageReducement[i] = armorType;
                }
            }
        }
        for (int i = 0; i < 36; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemStack == null || itemStack.getItem() == null) continue;

            if (itemStack.getItem() instanceof ItemArmor) {
                final ItemArmor armor = (ItemArmor) itemStack.getItem();

                final int armorType = 3 - armor.armorType;
                final int slotProtectionLevel = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
                if (bestArmorDamageReducement[armorType] < slotProtectionLevel) {
                    bestArmorDamageReducement[armorType] = slotProtectionLevel;
                    bestArmorSlots[armorType] = i;
                }
            }
            if (itemStack.getItem() instanceof ItemSword) {
                final ItemSword sword = (ItemSword) itemStack.getItem();

                if (bestSwordDamage < sword.getDamageVsEntity()) {
                    bestSwordDamage = sword.getDamageVsEntity();
                    bestSwordSlot = i;
                }
            }
        }
    }

    private void searchForTrash() {
        trash.clear();
        bestArmorDamageReducement = new int[4];
        bestArmorSlots = new int[4];
        bestSwordDamage = -1;
        bestSwordSlot = -1;

        Arrays.fill(bestArmorDamageReducement, -1);
        Arrays.fill(bestArmorSlots, -1);

        List<Integer>[] allItems = new List[4];

        List<Integer> allSwords = new ArrayList<>();

        for (int i = 0; i < bestArmorSlots.length; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.armorItemInSlot(i);

            allItems[i] = new ArrayList<>();

            if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemArmor) {
                bestArmorDamageReducement[i] = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
                bestArmorSlots[i] = 8 + i;
            }
        }
        for (int slot = 0; slot < 36; slot++) {
            final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);
            if (stack == null || stack.getItem() == null) continue;
            if (stack.getItem() instanceof ItemArmor) {
                final ItemArmor armor = (ItemArmor) stack.getItem();

                final int armorType = 3 - armor.armorType;

                allItems[armorType].add(slot);
            }
            if (stack.getItem() instanceof ItemSword) {
                float damageLevel = stack.getItem().getDamageVsEntity();

                allSwords.add(slot);

                if (bestSwordDamage < damageLevel) {
                    bestSwordDamage = damageLevel;
                    bestSwordSlot = slot;
                }
            }
            if (stack.getItem() instanceof ItemDye
                    || stack.getItem() instanceof ItemExpBottle
                    || stack.getItem() instanceof ItemEgg
                    || stack.getItem() instanceof ItemSnowball
                    || stack.getItem() instanceof ItemCoal
                    || stack.getItem() instanceof ItemDoor
                    || stack.getItem() instanceof ItemPiston
                    || stack.getItem() instanceof ItemSeeds
                    || stack.getItem() instanceof ItemAnvilBlock
                    || stack.getItem() instanceof ItemSaddle
                    || stack.getItem() instanceof ItemGlassBottle
                    || stack.getItem() instanceof ItemRedstone
                    || stack.getItem() instanceof ItemHoe
                    || stack.getItem() instanceof ItemShears
                    || stack.getItem() instanceof ItemTool
                    || isBadPotion(stack))
                trash.add(slot);
        }

        for (final List<Integer> allItem : allItems) trash.addAll(allItem);
        for (final Integer swordSlot : allSwords) if (bestSwordSlot != swordSlot) trash.add(swordSlot);
    }

    boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getItemDamage()))
            for (final PotionEffect potion : ((ItemPotion) stack.getItem()).getEffects(stack))
                if (potion.getPotionID() == Potion.blindness.id
                        || potion.getPotionID() == Potion.poison.id
                        || potion.getPotionID() == Potion.weakness.id
                        || potion.getPotionID() == Potion.moveSlowdown.id
                        || potion.getPotionID() == Potion.digSlowdown.id
                        || potion.getPotionID() == Potion.harm.id
                        || potion.getPotionID() == Potion.hunger.id)
                    return true;
        return false;
    }

    @Override
    public void setup() {
        super.setup();
        final ArrayList<SettingsItem> items = new ArrayList<>();
        items.add(new SettingsItem("Delay", 0, 500, 10, ""));
        items.add(new SettingsItem("OpenInv", false, ""));
        Management.instance.settingsmgr.addSetting(new Setting(this, items));
        setting = Management.instance.settingsmgr.getSettingByName(getName());
    }
    @EventTarget
    public void onUpdate(EventReceivedPacket ppe) {
        Packet p = ppe.getPacket();
     if(p instanceof C16PacketClientStatus) {
         final C16PacketClientStatus clientStatus = (C16PacketClientStatus) p;
         if (clientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
             ppe.setCancelled(true);
         }
     }
        }
}
