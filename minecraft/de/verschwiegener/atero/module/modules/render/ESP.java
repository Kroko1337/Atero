package de.verschwiegener.atero.module.modules.render;

import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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

import java.awt.*;
import java.util.List;

public class ESP extends Module {
    private float oldBrightness;

    public ESP() {
	super("ESP", "ESP", Keyboard.KEY_NONE, Category.Render);
    }

    public void onUpdate() {
	if (this.isEnabled()) {
	    super.onUpdate();
	    mc.gameSettings.gammaSetting = oldBrightness;

	    mc.gameSettings.gammaSetting = 10F;
	}
    }

    @EventTarget
    public void onPostRender(EventPostRender render) {

    }

    public static void drawChestESP() {
	List<TileEntity> loadedTileEntityList = Minecraft.getMinecraft().theWorld.loadedTileEntityList;
	for (int i = 0, loadedTileEntityListSize = loadedTileEntityList.size(); i < loadedTileEntityListSize; i++) {
	    TileEntity tileEntity = loadedTileEntityList.get(i);
	    if (tileEntity instanceof TileEntityChest) {

		GlStateManager.disableTexture2D();
		GL11.glColor4f(0, 255, 255, 255);
		TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity,
			Minecraft.getMinecraft().timer.renderPartialTicks, 1);

		GlStateManager.enableTexture2D();

	    }
	}

    }

    public static void blockESPBox(BlockPos blockPos) {
	double x = blockPos.getX() - Minecraft.getMinecraft().getRenderManager().renderPosX + 5;
	double y = blockPos.getY() - Minecraft.getMinecraft().getRenderManager().renderPosY - 0.125;
	double z = blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().renderPosZ + 5;
	GL11.glBlendFunc(770, 771);
	GL11.glEnable(GL11.GL_BLEND);
	GL11.glLineWidth(1.0F);
	GL11.glDisable(GL11.GL_TEXTURE_2D);
	GL11.glDisable(GL11.GL_DEPTH_TEST);
	GL11.glDepthMask(false);
	int color = Color.cyan.getRGB();
	if ((color & -67108864) == 0) {
	    color |= -16777216;
	}
	float red;
	float green, blue, alpha;
	red = (float) (color >> 16 & 255) / 255.0F;
	blue = (float) (color >> 8 & 255) / 255.0F;
	green = (float) (color & 255) / 255.0F;
	alpha = (float) (color >> 24 & 255) / 255.0F;
	GL11.glColor4d(red, green, blue, alpha);
	RenderGlobal.func_181561_a(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
	GL11.glEnable(GL11.GL_TEXTURE_2D);
	GL11.glEnable(GL11.GL_DEPTH_TEST);
	GL11.glDepthMask(true);
	GL11.glDisable(GL11.GL_BLEND);
	GL11.glColor3f(1, 1, 1);

    }
}
