package de.verschwiegener.atero.ui.guiingame;

import java.awt.Color;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.audio.Stream;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.module.ModuleManager;
import de.verschwiegener.atero.module.modules.combat.Killaura;
import de.verschwiegener.atero.ui.audio.AudioPanel;
import de.verschwiegener.atero.util.Util;
import de.verschwiegener.atero.util.render.RenderUtil;
import de.verschwiegener.atero.util.render.ShaderRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class CustomGUIIngame {

    private static Minecraft mc = Minecraft.getMinecraft();
    public static ShaderRenderer shader = new ShaderRenderer("tabGuiBlur.frag");

    public static void drawArrayList() {
	Fontrenderer fontRenderer = Management.instance.fontmgr.getFontByName("ArrayListFont").getFontrenderer();
	if (Management.instance.modulechange) {
	    Management.instance.modulechange = false;
	    Util.sortModuleList(Management.instance.modulemgr.modules,
		    Management.instance.fontmgr.getFontByName("ArrayListFont"));
	}
	int yoffset = 0;
	int xoffset = 10;
	ModuleManager mm = Management.instance.modulemgr;
	ScaledResolution sr = new ScaledResolution(mc);
	for (int i = 0; i < Management.instance.modulemgr.modules.size(); i++) {
	    if (Management.instance.modulemgr.modules.get(i).isEnabled()) {
		fontRenderer.drawString(mm.modules.get(i).getName(),
			((sr.getScaledWidth() * 2) - fontRenderer.getStringWidth(mm.modules.get(i).getName()))
				- xoffset,
			yoffset, Color.black.getRGB());
		yoffset += fontRenderer.getBaseStringHeight() * 2;

	    }
	}
    }

    public static void renderShader() {
	if (mc.currentScreen == null) {
	    // Render Tabgui Blur shader
	    try {
		ScaledResolution sr = new ScaledResolution(mc);
		shader.prepareRender();
		ARBShaderObjects.glUniform2fARB(shader.getUniformLocation("u_size"), 200.0F, 200.0F);
		shader.renderShader(sr);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public static void drawMusikTab() {
	Stream stream = Management.instance.currentStream;
	if (stream != null) {
	    ScaledResolution sr = new ScaledResolution(mc);
	    final int scaleFactor = sr.getScaleFactor();
	    Fontrenderer fontRenderer = Management.instance.fontrendererBold;
	    drawImage(sr.getScaledWidth() - 54, sr.getScaledHeight() - 54, 50, 50, stream);
	    fontRenderer.drawString(stream.getFulltitle(),
		    (sr.getScaledWidth() - 54) * 2 - (fontRenderer.getStringWidth(stream.getFulltitle())) - 5,
		    (sr.getScaledHeight() - 54) * 2, Color.BLACK.getRGB());
	    fontRenderer.drawString(stream.getArtist(),
		    (sr.getScaledWidth() - 54) * 2 - (fontRenderer.getStringWidth(stream.getArtist())) - 5,
		    (sr.getScaledHeight() - 54) * 2 + (fontRenderer.getBaseStringHeight() * 2), Color.BLACK.getRGB());
	}
    }

    private static void drawImage(final int xPos, final int yPos, final int width, final int height, Stream stream) {
	mc.getTextureManager().deleteTexture(stream.getLocation());
	stream.setTexture(new DynamicTexture(stream.getImage()));
	stream.setLocation(
		mc.getTextureManager().getDynamicTextureLocation(stream.getChannelName(), stream.getTexture()));
	stream.getTexture().updateDynamicTexture();
	mc.getTextureManager().bindTexture(stream.getLocation());
	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0.0F, 0.0F, width, height, width, height);
    }

    public static void renderTargetHud(ScaledResolution scaledResolution) {

	
	Fontrenderer fontRenderer = Management.instance.fontrenderer;
	
        if (Killaura.target != null && Killaura.target instanceof EntityPlayer
                || Killaura.target instanceof EntityAnimal) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(scaledResolution.getScaledWidth() / 2F, scaledResolution.getScaledHeight() / 1.8F, 0);
            RenderUtil.drawRect(0, 0, 149, 0.5F, Util.getColor(0, 0, 0, 75));
            RenderUtil.drawRect(0, 1, 0.5F, 59.5F, Util.getColor(0, 0, 0, 75));
            RenderUtil.drawRect(0, 59.5F, 149, 60, Util.getColor(0, 0, 0, 75));
            RenderUtil.drawRect(149.5F, 0, 150, 60, Util.getColor(0, 0, 0, 75));
            RenderUtil.drawRect(0, 0, 150, 60, Util.getColor(0, 0, 0, 160));

            fontRenderer.drawString(Killaura.target.getName(), 20, 3F, Util.getColor(255, 255, 255, 255));

            renderPlayer(25, 55, 23, Killaura.target);

            float healthProcent = Killaura.target.getHealth() / Killaura.target.getMaxHealth();
            RenderUtil.drawRect(55, 15, 55 + (90 * healthProcent), 25,
                    Color.HSBtoRGB(Math.min(-healthProcent + 0.3F, 0), 1, 1));
            fontRenderer.drawString(String.valueOf(Math.round(Killaura.target.getHealth())), 175, 6F,
                    Color.HSBtoRGB(Math.min(-healthProcent + 0.3F, 0), 1, 1));

            double winChance = 0;

            double TargetStrength = getWeaponStrength(Killaura.target.getHeldItem());
            winChance = getWeaponStrength(mc.thePlayer.getHeldItem()) - TargetStrength;
            winChance += getProtection(mc.thePlayer) - getProtection(Killaura.target);
            winChance += mc.thePlayer.getHealth() - (Killaura.target).getHealth();

            String message = winChance == 0 ? "You could win"
                    : winChance < 0 ? "You could lose" : "You are going to win";
            fontRenderer.drawString(message,
                    97.5F - fontRenderer.getStringWidth(message) + fontRenderer.getStringWidth(message) / 1F, 50F,
                    Util.getColor(255, 240, 0, 255));
            GlStateManager.popMatrix();
        }
    }

    private static double getProtection(EntityLivingBase target) {
	double protection = 0;

	for (int i = 0; i <= 3; i++) {
	    ItemStack stack = target.getCurrentArmor(i);

	    if (stack != null) {
		if (stack.getItem() instanceof ItemArmor) {
		    ItemArmor armor = (ItemArmor) stack.getItem();
		    protection += armor.damageReduceAmount;
		}

		protection += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.25;
	    }
	}

	return protection;
    }

    // schon mal an nen event daf�r gedacht - wo renderst du das was rendern?
    // die aary?

    private static double getWeaponStrength(ItemStack stack) {
	double damage = 0;

	if (stack != null) {
	    if (stack.getItem() instanceof ItemSword) {
		ItemSword sword = (ItemSword) stack.getItem();
		damage += sword.getDamageVsEntity();
	    }

	    if (stack.getItem() instanceof ItemTool) {
		ItemTool tool = (ItemTool) stack.getItem();
		damage += tool.getToolMaterial().getDamageVsEntity();
	    }

	    damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25;
	}

	return damage;
    }

    private static void renderPlayer(int posX, int posY, int scale, EntityLivingBase player) {
	GlStateManager.enableColorMaterial();
	GlStateManager.pushMatrix();
	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	GlStateManager.translate((float) posX, (float) posY, 50.0F);
	GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
	GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
	RenderHelper.enableStandardItemLighting();
	player.rotationYawHead = player.rotationYaw;
	player.prevRotationYawHead = player.rotationYaw;
	GlStateManager.translate(0.0F, 0.0F, 0.0F);
	RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
	rendermanager.setPlayerViewY(180.0F);
	rendermanager.setRenderShadow(false);
	rendermanager.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
	rendermanager.setRenderShadow(true);
	player.renderYawOffset = player.rotationYaw;
	player.prevRotationYawHead = player.rotationYaw;
	player.rotationYawHead = player.rotationYaw;
	GlStateManager.popMatrix();
	RenderHelper.disableStandardItemLighting();
	GlStateManager.disableRescaleNormal();
	GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	GlStateManager.disableTexture2D();
	GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

}