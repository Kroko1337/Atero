package de.verschwiegener.atero.util;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ReconnectUtil {

    public static void sendReconnect() {
	try {
	    String xmldata = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
		    + "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">"
		    + "<s:Body>" + "<u:ForceTermination xmlns:u=\"urn:schemas-upnp-org:service:WANIPConnection:1\" />"
		    + "</s:Body>" + "</s:Envelope>";

	    // Create socket
	    Socket socket = new Socket("fritz.box", 49000);

	    // Send header
	    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
	    wr.write("POST /upnp/control/WANIPConn1 HTTP/1.1");
	    wr.write("Host: fritz.box:49000" + "\r\n");
	    wr.write("SOAPACTION: \"urn:schemas-upnp-org:service:WANIPConnection:1#ForceTermination\"" + "\r\n");
	    wr.write("Content-Type: text/xml; charset=\"utf-8\"" + "\r\n");
	    wr.write("Content-Length: " + xmldata.length() + "\r\n");
	    wr.write("\r\n");
	    // Send data
	    wr.write(xmldata);
	    wr.flush();
	    socket.close();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
