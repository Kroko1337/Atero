package de.verschwiegener.atero.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import de.verschwiegener.atero.Management;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

public class LoginUtil {

    private static LoginUtil loginUtil;

    public String status = "waiting...";

    public void login(String token) {
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
                        status = "Logged into §e" + service.getSelectedProfile().getName();
                        Minecraft.getMinecraft().session = new Session(service.getSelectedProfile().getName(), service.getSelectedProfile().getId().toString(), service.getAuthenticatedToken(), "LEGACY");
                        this.stop();
                    } catch (Exception e) {
                        status = "§c§lError: §cAccount isn't working";
                    }
                }
            };
            loginToken.start();
        }
    }

    public void loginCracked(String name) {
        Thread loginCracked = new Thread("loginCracked") {
            public void run() {
                TheAlteningAuthentication theAlteningAuthentication = TheAlteningAuthentication.mojang();
                theAlteningAuthentication.updateService(AlteningServiceType.MOJANG);
                status = "Logged into §e" + name + " §7(§cCracked§7)";
                Minecraft.getMinecraft().session = new Session(name, "", "", "LEGACY");
                this.stop();
            }
        };
        loginCracked.start();
    }

    public void login(String email, String password) {
        Thread login = new Thread("login") {
            public void run() {
                try {
                    TheAlteningAuthentication theAlteningAuthentication = TheAlteningAuthentication.mojang();
                    YggdrasilUserAuthentication service = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                    service.setUsername(email);
                    service.setPassword(password);
                    service.logIn();
                    theAlteningAuthentication.updateService(AlteningServiceType.MOJANG);
                    status = "Logged into §e" + service.getSelectedProfile().getName();
                    Minecraft.getMinecraft().session = new Session(service.getSelectedProfile().getName(), service.getSelectedProfile().getId().toString(), service.getAuthenticatedToken(), "LEGACY");
                    this.stop();
                } catch (Exception e) {
                    status = "§c§lError: §cAccount isn't working";
                    this.stop();
                }
            }
        };
        login.start();
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
