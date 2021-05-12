package de.verschwiegener.atero.module.modules.combat;

import net.minecraft.client.Minecraft;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;

public class Antibots extends Module {
    TimeUtils timeUtils;

    public Antibots() {
        super("Antibots", "Antibots", Keyboard.KEY_NONE, Category.Combat);
    }

    public void onEnable() {

        super.onEnable();
    }

    public void onDisable() {

        super.onDisable();
    }

    public static boolean isInTablist(EntityPlayer player) {
        if (Minecraft.getMinecraft().isSingleplayer())
            return false;
        Minecraft.getMinecraft();
        for (NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName()))
                return true;
        }
        return false;
    }

    public static boolean isBot(EntityPlayer player) {
        if (player.isInvisible()) return true;
        if (!isInTablist(player)) return true;
        if (invalidName(player)) return true;
        return false;
    }

    public static boolean invalidName(Entity e) {
        if (e.getName().contains("-"))
            return true;
        if (e.getName().contains("/"))
            return true;
        if (e.getName().contains("|"))
            return true;
        if (e.getName().contains("<"))
            return true;
        if (e.getName().contains(">"))
            return true;
        if (e.getName().contains("ยง"))
            return true;
        return false;
    }


    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onUpdate() {
        if (this.isEnabled()) {
            super.onUpdate();
            for (Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if (((Entity) entity).isInvisible() && entity != Minecraft.thePlayer)
                    Killaura.bots.add((Entity) entity);
            }
            for (Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer && !isInTablist((EntityPlayer) entity) && entity != Minecraft.thePlayer)
                    Killaura.bots.add((Entity) entity);

            }
        }

    }
}
