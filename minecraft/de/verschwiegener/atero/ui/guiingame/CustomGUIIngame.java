package de.verschwiegener.atero.ui.guiingame;

import java.awt.Color;
import java.time.LocalDateTime;

import javax.swing.text.html.parser.Entity;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventRenderShader;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.audio.Stream;
import de.verschwiegener.atero.design.font.Font;
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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
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
		int xoffset = 3;
		ModuleManager mm = Management.instance.modulemgr;
		ScaledResolution sr = new ScaledResolution(mc);
		for (int i = 0; i < Management.instance.modulemgr.modules.size(); i++) {
			if (Management.instance.modulemgr.modules.get(i).isEnabled()) {
				RenderUtil.fillRect(((sr.getScaledWidth()) - mc.fontRendererObj.getStringWidth(mm.modules.get(i).getName())) - xoffset - 2, yoffset, xoffset + mc.fontRendererObj.getStringWidth(mm.modules.get(i).getName()), mc.fontRendererObj.FONT_HEIGHT + 4, new Color(0, 0, 0, 120));
				mc.fontRendererObj.drawStringWithShadow(mm.modules.get(i).getName(), ((sr.getScaledWidth()) - mc.fontRendererObj.getStringWidth(mm.modules.get(i).getName())) - xoffset, yoffset +2, Management.instance.colorBlue.getRGB());
				RenderUtil.fillRect(sr.getScaledWidth() - 2, yoffset, 2, mc.fontRendererObj.FONT_HEIGHT + 4, Management.instance.colorBlue);
				//fontRenderer.drawString(mm.modules.get(i).getName(),
				//((sr.getScaledWidth() * 2) - fontRenderer.getStringWidth(mm.modules.get(i).getName()))
				//- xoffset,
				//yoffset, Management.instance.settingsmgr.getSettingByName("ClickGui").getItemByName("TEST").getColor().getRGB());
				yoffset += mc.fontRendererObj.FONT_HEIGHT +4;

			}
		}
	}
	@EventTarget
	public static void renderShader(EventRenderShader event) {
		if (mc.currentScreen == null) {
			// Render Tabgui Blur shader
			try {
				//ScaledResolution sr = new ScaledResolution(mc);
				//shader.prepareRender();
				//ARBShaderObjects.glUniform2fARB(shader.getUniformLocation("u_size"), 200.0F, 200.0F);
				//shader.renderShader(sr);
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
			Font fontBold = Management.instance.fontBold;
			drawImage(sr.getScaledWidth() - 54, sr.getScaledHeight() - 54, 50, 50, stream);
			System.out.println("Width: " + fontBold.getStringWidth2(stream.getFulltitle()) * 2);
			fontBold.drawString(stream.getFulltitle(),
				(sr.getScaledWidth() - 54) - (fontBold.getStringWidth2(stream.getFulltitle()) * 2) - 5,
				(sr.getScaledHeight() - 53), Color.BLACK.getRGB());
			fontBold.drawString(stream.getArtist(),
				(sr.getScaledWidth() - 54) - (fontBold.getStringWidth2(stream.getArtist()) * 2) - 5,
				(sr.getScaledHeight() - 53) + (fontBold.getBaseStringHeight()),
				Color.BLACK.getRGB());
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

		Font font = Management.instance.font;
		EntityLivingBase target = Killaura.instance.getTarget();

		if (target != null && target instanceof EntityPlayer || target instanceof EntityAnimal || target instanceof EntityVillager || target instanceof EntityMob && Management.instance.modulemgr.getModuleByName("Killaura").isEnabled()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(scaledResolution.getScaledWidth() / 2F, scaledResolution.getScaledHeight() / 1.8F, 0);

			RenderUtil.fillRect(0, 0, 150, 60, Management.instance.colorGray);
			RenderUtil.fillRect(0, 59, 150, 1, Management.instance.colorBlue);

			font.drawString(target.getName(), 20, 3F, Management.instance.colorBlue.getRGB());

			renderPlayer(25, 55, 23, target);

			float healthProcent = target.getHealth() / target.getMaxHealth();
			RenderUtil.drawRect(55, 15, 55 + (90 * healthProcent), 25,
					Color.HSBtoRGB(Math.min(-healthProcent + 0.3F, 0), 1, 1));
			font.drawString(String.valueOf(Math.round(target.getHealth())), 175, 6F,
					Color.HSBtoRGB(Math.min(-healthProcent + 0.3F, 0), 1, 1));

			double winChance = 0;

			double TargetStrength = getWeaponStrength(target.getHeldItem());
			winChance = getWeaponStrength(mc.thePlayer.getHeldItem()) - TargetStrength;
			winChance += getProtection(mc.thePlayer) - getProtection(target);
			winChance += mc.thePlayer.getHealth() - (target).getHealth();

			String message = winChance == 0 ? "You could win"
					: winChance < 0 ? "You could lose" : "You are going to win";
			font.drawString(message, 97.5F - font.getStringWidth(message) + font.getStringWidth(message) / 1F, 50F,
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

	// schon mal an nen event dafï¿½r gedacht - wo renderst du das was rendern?
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

	public static void drawWatermark(ScaledResolution sr) {
		final Fontrenderer fontRenderer = Management.instance.fontmgr.getFontByName("WaterMarkFont").getFontrenderer();
		int xP = 15;
		int yP = 5;
		int widthP = (int) ((int) fontRenderer.getStringWidth("atero"));
		int heightP = (int) fontRenderer.getStringHeight("atero") / 2;
		LocalDateTime now = LocalDateTime.now();

		double seconds = now.getSecond();
		double minutes = now.getMinute();
		double hours = now.getHour();

		int width = sr.getScaledWidth() / 10;
		int height = sr.getScaledHeight() /45 ;

		int clockW = 30;
		int clockH = 30;

		fontRenderer.drawString("Atero", xP, yP, Management.instance.colorBlue.getRGB());
		mc.fontRendererObj.drawStringWithShadow("[" + now.getHour() + ":" + insertNulls(now.getMinute(), 2) + ":" +insertNulls(now.getSecond()   , 2) +"]",
				width , height  , Management.instance.colorBlue.getRGB());
//	drawImage((int) (xP), (int) (yP + fontRenderer.getStringHeight("atero")), widthP - 80, heightP,
		//	new ResourceLocation("atero/assets/arrow.png"));
	}

	private static void drawImage(int x, int y, int width, int height, ResourceLocation resourceLocation) {
		mc.getTextureManager().bindTexture(resourceLocation);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	public static String insertNulls(int val, int min_digits) {
		String s = String.valueOf(val);
		while(s.length() < min_digits) s = "0" + s;
		return s;
	}
}