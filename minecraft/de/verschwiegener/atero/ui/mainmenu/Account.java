package de.verschwiegener.atero.ui.mainmenu;

import java.util.Arrays;

public class Account {

    private String email;
    private String password;
    private String username;
    private boolean lastLoginSuccess;
    private String[] bannedServer;

    public Account(String email, String password, String username, boolean lastLoginSuccess, String[] bannedServer) {
	this.email = email;
	this.password = password;
	this.username = username;
	this.lastLoginSuccess = lastLoginSuccess;
	this.bannedServer = bannedServer;
    }

    public Account(String email, String password) {
	this.email = email;
	this.password = password;
	this.username = "";
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

}
