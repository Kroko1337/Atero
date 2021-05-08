package de.verschwiegener.atero.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import de.verschwiegener.atero.design.font.Font;
import de.verschwiegener.atero.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class Util {
    
    Minecraft mc = Minecraft.getMinecraft();

    /**
     * Sorts the Modules after the Name length
     * 
     * @param modules
     */
    public static void sortModuleList(ArrayList<Module> modules, final Font font) {
	Collections.sort(modules, new Comparator<Module>() {

	    @Override
	    public int compare(Module o1, Module o2) {
		if (font.getFontrenderer().getStringWidth(o1.getName()) > font.getFontrenderer()
			.getStringWidth(o2.getName())) {
		    return -1;
		}
		if (font.getFontrenderer().getStringWidth(o1.getName()) < font.getFontrenderer()
			.getStringWidth(o2.getName())) {
		    return 1;
		}
		return 0;
	    }
	});
    }

    /**
     * @throws IOException Gets the IP for Router Reconnect
     *                     https://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
     */
    public static String getIpv4() throws IOException {
	URL whatismyip = new URL("https://ipv4.icanhazip.com/");
	BufferedReader in = null;
	try {
	    in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
	    String ip = in.readLine();
	    return ip;
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    public static String getIpv6() throws IOException {
	URL whatismyip = new URL("https://ipv6.icanhazip.com/");
	BufferedReader in = null;
	try {
	    in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
	    String ip = in.readLine();
	    return ip;
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
    public static int getColor(int red, int green, int blue, int opacity) {
        int color = MathHelper.clamp_int(opacity, 0, 255) << 24;
        color |= MathHelper.clamp_int(red, 0, 255) << 16;
        color |= MathHelper.clamp_int(green, 0, 255) << 8;
        color |= MathHelper.clamp_int(blue, 0, 255);
        return color;
    }

}
