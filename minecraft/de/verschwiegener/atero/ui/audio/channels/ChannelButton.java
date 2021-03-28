package de.verschwiegener.atero.ui.audio.channels;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.audio.Stream;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.ui.audio.AudioPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class ChannelButton {

    private final int xOffset, yOffset, height, width;
    private final AudioPanel panel;
    private final String channel;
    private int x, y;

    public ChannelButton(final String channel, final int xOffset, final int yOffset, final AudioPanel panel,
	    final int width, final int height) {
	this.channel = channel;
	this.xOffset = xOffset;
	this.yOffset = yOffset;
	this.width = width;
	this.height = height;
	this.panel = panel;
    }

    public void drawButton(final AudioPanel panel, final int x, final int y) {
	final Fontrenderer fontrenderer = Management.instance.fontrenderer;
	final Stream stream = Management.instance.streamManager.getStreamByName(channel);
	
	this.x = x + xOffset;
	this.y = y + yOffset;
	
	if (stream != null && stream.getImage() != null) {
	    drawImage(this.x + width / 4, this.y + height / 8, width - width / 2, height - height / 2, stream.getLocation(), stream.getTexture());
	    final int stringWidth = fontrenderer.getStringWidth(channel);
	    fontrenderer.drawString(channel, this.x * 2 + width - stringWidth / 2, (this.y + height / 4 * 2.7F) * 2, Management.instance.colorBlue.getRGB());
	    panel.getSongFont().drawString(stream.getTitle(), this.x * 2 + width - panel.getSongFont().getStringWidth(stream.getTitle()) / 2, (this.y + height / 4 * 3.1F) * 2, Color.white.getRGB());
	    panel.getSongFont().drawString(stream.getArtist(), this.x * 2 + width - panel.getSongFont().getStringWidth(stream.getArtist()) / 2,(this.y + height / 4 * 3.5F) * 2, Color.WHITE.getRGB());
	}
    }
    
    public void onMouseClicked(final int mouseX, final int mouseY, final int mousebutton) {
	if(mousebutton == 0) {
	    if(isHovered(mouseX, mouseY)) {
		panel.setCurrentStream(Management.instance.streamManager.getStreamByName(channel));
	    }
	}
    }
    
    private boolean isHovered(final int mouseX, final int mouseY) {
	return mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
    }

    private void drawImage(final int xPos, final int yPos, final int width, final int height, final ResourceLocation location, final DynamicTexture texture) {
	texture.updateDynamicTexture();
	Minecraft.getMinecraft().getTextureManager().bindTexture(location);
	GL11.glEnable(GL11.GL_BLEND);
	GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
	GL11.glDisable(GL11.GL_ALPHA_TEST);
	Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0.0F, 0.0F, width, height, width, height);
	GL11.glEnable(GL11.GL_ALPHA_TEST);
	GL11.glDisable(GL11.GL_BLEND);
    }

}
