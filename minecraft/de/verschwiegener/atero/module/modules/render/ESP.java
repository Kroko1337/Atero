package de.verschwiegener.atero.module.modules.render;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.settings.Setting;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPostRender;
import com.darkmagician6.eventapi.events.callables.EventRender2D;

import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ESP extends Module {
    private float oldBrightness;
    public static ESP instance;
    public static Setting setting;
    public ESP() {
	super("ESP", "ESP", Keyboard.KEY_NONE, Category.Render);
	instance = this;
    }

    public void onUpdate() {
	if (this.isEnabled()) {
	    super.onUpdate();
	    mc.gameSettings.gammaSetting = oldBrightness;

	    mc.gameSettings.gammaSetting = 10F;
	}
    }

    @EventTarget
    public void onRender() {
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            int scaleFactor = scaledResolution.getScaleFactor();
            double scaling = scaleFactor / Math.pow(scaleFactor, 2);
            GL11.glScaled(scaling, scaling, scaling);
            for (EntityPlayer player : Minecraft.theWorld.playerEntities) {
                if (player != Minecraft.thePlayer || mc.gameSettings.thirdPersonView != 0) {
                    if(player.isInvisible())
                        continue;

                    double x = RenderUtil.interpolate(player.posX, player.lastTickPosX, mc.timer.renderPartialTicks);
                    double y = RenderUtil.interpolate(player.posY, player.lastTickPosY, mc.timer.renderPartialTicks);
                    double z = RenderUtil.interpolate(player.posZ, player.lastTickPosZ, mc.timer.renderPartialTicks);
                    double width = player.width / 1.5;
                    double height = player.height + 0.2;
                    AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                    mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);

                    Vector4d position = null;
                    for (Vector3d vec : vectors) {
                        vec = RenderUtil.project2D(scaleFactor, vec.x - mc.getRenderManager().viewerPosX, vec.y - mc.getRenderManager().viewerPosY, vec.z - mc.getRenderManager().viewerPosZ);
                        if (vec != null && vec.z >= 0 && vec.z < 1) {
                            if (position == null)
                                position = new Vector4d(vec.x, vec.y, vec.z, 0);

                            position.x = Math.min(vec.x, position.x);
                            position.y = Math.min(vec.y, position.y);
                            position.z = Math.max(vec.x, position.z);
                            position.w = Math.max(vec.y, position.w);
                        }
                    }
                    mc.entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        double posX = position.x;
                        double posY = position.y;
                        double endPosX = position.z;
                        double endPosY = position.w;
                        // RECTANGLE
                        RenderUtil.drawRect(posX, posY, endPosX, endPosY, new Color(205, 255, 255, 50).getRGB());
                        // HEALTH BAR
                        float health = player.getHealth();
                        float maxHealth = player.getMaxHealth();
                        //new Color(0, 255, 0, 255).getRGB()
                        RenderUtil.drawRect(posX - 2, endPosY - (health / maxHealth) * (endPosY - posY), posX - 0.5, endPosY, Management.instance.colorBlue.getRGB());
                    }
                }
            }
        }
        // }





}
