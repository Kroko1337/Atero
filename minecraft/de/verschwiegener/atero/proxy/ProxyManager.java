package de.verschwiegener.atero.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ProxyManager {

    private ArrayList<Proxy> proxys = new ArrayList<>();

    private Proxy currentProxy;
    private boolean useProxy;

    public ArrayList<Proxy> getProxys() {
	return proxys;
    }

    public void setProxys(ArrayList<Proxy> proxys) {
	this.proxys = proxys;
    }

    public Proxy getCurrentProxy() {
	return currentProxy;
    }

    public void setCurrentProxy(Proxy currentProxy) {
	this.currentProxy = currentProxy;
    }

    public boolean isUseProxy() {
	return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
	this.useProxy = useProxy;
    }
    
    public static boolean pingHost(Proxy proxy) {
	    try (Socket socket = new Socket()) {
	        socket.connect(new InetSocketAddress(proxy.getIP(), proxy.getPort()), 3000);
	        return true;
	    } catch (IOException e) {
	        return false; // Either timeout or unreachable or failed DNS lookup.
	    }
	}

}
