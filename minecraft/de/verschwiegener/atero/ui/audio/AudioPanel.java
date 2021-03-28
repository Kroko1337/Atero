package de.verschwiegener.atero.ui.audio;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.audio.Stream;
import de.verschwiegener.atero.audio.Stream.Provider;
import de.verschwiegener.atero.design.font.Font;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.ui.audio.channels.ChannelButton;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class AudioPanel extends GuiScreen {

    private int x, y, scrollY, maxScrollY;
    private final int width;
    private final int height;
    private final ArrayList<ProviderButton> providerButtons = new ArrayList<ProviderButton>();
    private final ArrayList<ChannelButton> channelbuttons = new ArrayList<ChannelButton>();
    private final Minecraft mc = Minecraft.getMinecraft();
    private Provider currentProvider = Provider.values()[0];
    private Stream currentStream;

    private final String Fontchars = "<>abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUWVXYZ0123456789§$%&?/{}()[].,;:-_|+*´`\"=";

    private final Fontrenderer titlefont;
    private final Fontrenderer songFont;
    double slidery;
    private int rotatevalue = 0;
    private String rotateString = "";

    public AudioPanel() {
	x = 100;
	y = 100;
	width = 500;
	height = 300;
	int buttonYOffset = 25;
	titlefont = new Font("AudioTitleFont", Fontrenderer.getFontByName("Inter-ExtraLight"), 6F, 6F, Fontchars)
		.getFontrenderer();
	songFont = new Font("SongFont", Fontrenderer.getFontByName("Inter-ExtraLight"), 4.5F, 4.5F, Fontchars)
		.getFontrenderer();
	for (final Provider provider : Provider.values()) {
	    providerButtons.add(new ProviderButton(provider.toString(), buttonYOffset));
	    buttonYOffset += 20;
	}
	scrollY = 0;
	createChannelButtons();
    }

    public void createChannelButtons() {
	channelbuttons.clear();
	final int maxX = width / 5 * 4;
	final int maxY = height - 30;
	final int widthOffset = width / 5 * 4 / 3;
	final int heightOffset = maxY / 2;
	int currentX = 0;
	int currentY = 0;
	for (final Stream stream : Management.instance.streamManager.getStreams()) {
	    if (stream.getProvider() == getCurrentProvider() && stream.getImage() != null) {
		channelbuttons.add(new ChannelButton(stream.getChannelName(), currentX, currentY, this, widthOffset,
			heightOffset));
		currentX += Math.round(widthOffset);
		if (currentX + 1 == maxX) {
		    currentX = 0;
		    currentY += maxY / 2;
		    maxScrollY = currentY - (maxY);
		}
	    }
	}
    }
    
    
    @Override
    public void handleMouseInput() throws IOException {
	int wheelD = (Mouse.getEventDWheel() / 10);
	int mouseY = -(Mouse.getEventY() - 1000) / 2;
	int mouseX = Mouse.getEventX() / 2;
	if(isMouseHovered(mouseX, mouseY)) {
	    scrollY += wheelD;
	    if(scrollY > 0) {
		scrollY = 0;
	    }
	    if(scrollY < -maxScrollY) {
		scrollY = -maxScrollY;
	    }
	    slidery =  (maxScrollY / (height - 30)) * scrollY / 9;
	    //System.out.println("Y: " + -slidery);
	    
	}
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, float partialTicks) {
	super.drawScreen(mouseX, mouseY, partialTicks);
	x = mc.displayWidth / 4 - width / 2;
	y = mc.displayHeight / 4 - height / 2;
	
	GL11.glEnable(GL11.GL_SCISSOR_TEST);
	final ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
	final int scaleFactor = scale.getScaleFactor();
	GL11.glScissor(x * scaleFactor, y * scaleFactor + 1, width * scaleFactor, height * scaleFactor);

	RenderUtil.fillRect(x, y, width, height, Management.instance.colorBlack);
	RenderUtil.fillRect(x, y, width / 5, height, Management.instance.colorGray);
	RenderUtil.fillRect(x, y + height - 30, width, 30, Management.instance.colorGray);
	titlefont.drawString("Musik", x * 2 + titlefont.getStringWidth2("Musik"), (y + 3) * 2, Color.WHITE.getRGB());

	providerButtons.forEach(button -> button.drawButton(this, x, y));
	channelbuttons.forEach(button -> button.drawButton(this, x + width / 5, y + scrollY));
	
	//DrawScrollSlider
	RenderUtil.fillRect(x + width - 3, this.y, 2, height, Management.instance.colorGray);
	RenderUtil.fillRect(x + width - 3, this.y + -(slidery) - 20, 2, 40, Management.instance.colorBlue);
	
	RenderUtil.fillRect(x, y + height - 30, width, 30, Management.instance.colorGray);
	
	RenderUtil.drawFullCircle(x + ((width - (width / 5)) / 2) + (width / 5), y + (height - 15), 14, true, Management.instance.colorBlack);
	
	RenderUtil.drawPlay(x + ((width - (width / 5)) / 2) + (width / 5) - 5, y + (height - 25),15,20, Management.instance.colorGray);
	if(currentStream != null) {
	    Management.instance.fontrenderer.drawString(currentStream.getFulltitle(), (x * 2) +  (width / 5) * 2, (y + (height)) * 2 - 40, Color.WHITE.getRGB());
	    
	   // rotateString(rotateString, Minecraft.getMinecraft().getSystemTime());
	    System.out.println("Rotate: " + rotateString);
	}
	
	GL11.glDisable(GL11.GL_SCISSOR_TEST);

    }
    private void rotateString(String input, float partialTicks) {
	if(partialTicks % 400 == 0) {
	    //Rotate String smoother machen
	    rotatevalue++;
	    if(rotatevalue > input.length()) {
		rotatevalue = 0;
	    }
	    //int i = rotatevalue % input.length();
	    rotateString =  input.substring(rotatevalue) + input.substring(0, rotatevalue);   
	}
    }
    public void setRotateString(String rotateString) {
	this.rotateString = rotateString;
    }

    public ProviderButton getButtonByPosition(final AudioPanel audiopanel, final int x, final int y) {
	return providerButtons.stream().filter(module -> x > audiopanel.x && x < audiopanel.x + audiopanel.width / 5
		&& y > audiopanel.y + module.y && y < audiopanel.y + module.y + 12).findFirst().orElse(null);
    }
    private boolean isMouseHovered(int mouseX, int mouseY) {
	return mouseX > (x + (width / 5)) && mouseX < (x + width) && mouseY > y && mouseY < (y + height - 30);
    }

    public Provider getCurrentProvider() {
	return currentProvider;
    }

    public int getHeight() {
	return height;
    }

    public Fontrenderer getSongFont() {
	return songFont;
    }

    public Fontrenderer getTitlefont() {
	return titlefont;
    }

    public int getWidth() {
	return width;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }
    public void setCurrentStream(Stream currentStream) {
	this.currentStream = currentStream;
	setRotateString(currentStream.getFulltitle() + " ---- ");
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
	super.mouseClicked(mouseX, mouseY, mouseButton);
	channelbuttons.forEach(button -> button.onMouseClicked(mouseX, mouseY, mouseButton));
	if (mouseButton == 0) {
	    final ProviderButton button = getButtonByPosition(this, mouseX, mouseY);
	    if (button != null) {
		currentProvider = Provider.valueOf(button.name);
		createChannelButtons();
	    }
	}
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
	super.mouseReleased(mouseX, mouseY, state);
    }
}
