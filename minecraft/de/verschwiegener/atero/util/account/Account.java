package de.verschwiegener.atero.util.account;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

import de.verschwiegener.atero.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class Account {

    private String email;
    private String password;
    private String username;
    private String UUID;
    private boolean lastLoginSuccess;
    private String[] bannedServer;
    private ResourceLocation location;

    public Account(String email, String password, String username, String UUID, boolean lastLoginSuccess, String[] bannedServer) {
	this.email = email;
	this.password = password;
	this.username = username;
	this.UUID = UUID;
	this.lastLoginSuccess = lastLoginSuccess;
	this.bannedServer = bannedServer;
	location = Util.getSkin(username);
	/*if(this.UUID != null && !this.UUID.isEmpty()) {
	    try {
		image = ImageIO.read(new URL("https://crafatar.com/avatars/" + this.UUID).openStream());
		location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(username, new DynamicTexture(image));
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}*/
    }

    public Account(String email, String password) {
	this.email = email;
	this.password = password;
	this.username = "";
	this.UUID = "";
	this.lastLoginSuccess = false;
	this.bannedServer = new String[] {};
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public boolean isLastLoginSuccess() {
	return lastLoginSuccess;
    }

    public void setLastLoginSuccess(boolean lastLoginSuccess) {
	this.lastLoginSuccess = lastLoginSuccess;
    }

    public String[] getBannedServer() {
	return bannedServer;
    }

    public void setBannedServer(String[] bannedServer) {
	this.bannedServer = bannedServer;
    }
    public void addBannedServer(String ServerIP) {
	bannedServer = Arrays.copyOf(bannedServer, bannedServer.length + 1);
	bannedServer[bannedServer.length - 1] = ServerIP;
    }
    public String getUUID() {
	return UUID;
    }
    public ResourceLocation getLocation() {
	return location;
    }

}
