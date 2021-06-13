package de.verschwiegener.atero.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.util.account.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.awt.Color;
import java.net.Proxy;

public class LoginUtil {

    private static LoginUtil loginUtil;

    public static String status = "";
    public static Color statusColor;
    
    public static void reset() {
	status = "";
	statusColor = Color.WHITE;
    }
    
    public static void LoginAccount(Account account) {
	try {
	    TheAlteningAuthentication theAlteningAuthentication = TheAlteningAuthentication.mojang();
	    YggdrasilUserAuthentication service = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(
		    Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
	    service.setUsername(account.getEmail());
	    service.setPassword(account.getPassword());
	    service.logIn();
	    theAlteningAuthentication.updateService(AlteningServiceType.MOJANG);
	    Minecraft.getMinecraft().session = new Session(service.getSelectedProfile().getName(),
		    service.getSelectedProfile().getId().toString(), service.getAuthenticatedToken(), "LEGACY");
	    account.setLastLoginSuccess(true);
	    account.setUsername(service.getSelectedProfile().getName());
	    account.setUUID(service.getSelectedProfile().getId().toString());
	    status = "Logged in as: " + service.getSelectedProfile().getName();
	    statusColor = Color.GREEN;
	} catch (Exception e) {
	    account.setLastLoginSuccess(false);
	    status = "Logging in Failed";
	    statusColor = Color.RED;
	}
    }

    public static void login(String token) {
        if (token.contains("@alt")) {
            Thread loginToken = new Thread("loginToken") {
                public void run() {
                    try {
                        TheAlteningAuthentication theAlteningAuthentication = TheAlteningAuthentication.theAltening();
                        theAlteningAuthentication.updateService(AlteningServiceType.THEALTENING);
                        YggdrasilUserAuthentication service = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                        service.setUsername(token);
                        service.setPassword(Management.instance.CLIENT_NAME);

                        service.logIn();
                        Minecraft.getMinecraft().session = new Session(service.getSelectedProfile().getName(), service.getSelectedProfile().getId().toString(), service.getAuthenticatedToken(), "LEGACY");
                        status = "Logged in as: " + service.getSelectedProfile().getName();
            	    	statusColor = Color.GREEN;
                    } catch (Exception e) {
                	status = "Logging in Failed";
            	    	statusColor = Color.RED;
                    }
                }
            };
            loginToken.start();
        }
    }

    public static void loginCracked(String name) {
        Thread loginCracked = new Thread("loginCracked") {
            public void run() {
                TheAlteningAuthentication theAlteningAuthentication = TheAlteningAuthentication.mojang();
                theAlteningAuthentication.updateService(AlteningServiceType.MOJANG);
                status = "Logged into §e" + name + " §7(§cCracked§7)";
                Minecraft.getMinecraft().session = new Session(name, "", "", "LEGACY");
            }
        };
        loginCracked.start();
    }

    public static boolean login(String email, String password) {
	try {
	    TheAlteningAuthentication theAlteningAuthentication = TheAlteningAuthentication.mojang();
	    YggdrasilUserAuthentication service = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(
		    Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
	    service.setUsername(email);
	    service.setPassword(password);
	    service.logIn();
	    theAlteningAuthentication.updateService(AlteningServiceType.MOJANG);
	    Minecraft.getMinecraft().session = new Session(service.getSelectedProfile().getName(),
		    service.getSelectedProfile().getId().toString(), service.getAuthenticatedToken(), "LEGACY");
	    status = "Logged in as: " + service.getSelectedProfile().getName();
	    statusColor = Color.GREEN;
	    return true;
	} catch (Exception e) {
	    status = "Logging in Failed";
	    statusColor = Color.RED;
	    return false;
	}
    }

    public void generate(String apiToken) {

    }

    public static LoginUtil getInstance() {
        if(loginUtil == null) {
            loginUtil = new LoginUtil();
        }
        return loginUtil;
    }
}
