package de.verschwiegener.atero.util.files.config.handler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.util.files.config.Config;
import de.verschwiegener.atero.util.files.config.ConfigItem;

import org.w3c.dom.*;

public class XMLHelper {

    private String role1 = null;
    private String role2 = null;
    private String role3 = null;
    private String role4 = null;
    private ArrayList<String> rolev;

    public static void parse(File file) {
	try {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document doc = db.parse(file);
	    doc.getDocumentElement().normalize();

	    NodeList infoList = doc.getElementsByTagName("info");
	    Config config = new Config(null, null, null, null);
	    for (int i = 0; i < infoList.getLength(); i++) {
		Node node = infoList.item(i);
		if (node.getNodeType() == Node.ELEMENT_NODE) {
		    Element eElement = (Element) node;
		    config.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
		    config.setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
		    config.setRecommendedServerIP(eElement.getElementsByTagName("server").item(0).getTextContent());
		}
	    }

	    ArrayList<ConfigItem> items = new ArrayList<>();
	    NodeList valueList = doc.getElementsByTagName("values");
	    for (int i = 0; i < valueList.getLength(); i++) {
		Node node = valueList.item(i);
		for (int e = 0; e < node.getChildNodes().getLength(); e++) {
		    Node value = node.getChildNodes().item(e);
		    if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			items.add(new ConfigItem(
				element.getElementsByTagName("value").item(0).getTextContent().split(" ")));
		    }
		}
	    }
	    config.setItems(items);
	    Management.instance.configmgr.configs.add(config);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void write(Config config) {
	try {

	    Document doc = createDoc(config);

	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transf = transformerFactory.newTransformer();

	    transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transf.setOutputProperty(OutputKeys.INDENT, "yes");
	    transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

	    DOMSource source = new DOMSource(doc);
	    String path = Management.instance.CLIENT_DIRECTORY.getAbsolutePath() + File.separator + "Configs";
	    File myFile = new File(path, config.getName() + ".config");
	    createConfigEnvironment(new File(path));

	    StreamResult file = new StreamResult(myFile);

	    transf.transform(source, file);

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static String createString(Config config) {
	try {
	    Document doc = createDoc(config);

	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transf = transformerFactory.newTransformer();

	    transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transf.setOutputProperty(OutputKeys.INDENT, "yes");
	    transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

	    DOMSource source = new DOMSource(doc);

	    StringWriter writer = new StringWriter();
	    transf.transform(source, new StreamResult(writer));
	    return writer.getBuffer().toString();

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    private static Document createDoc(Config config) {
	try {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = dbf.newDocumentBuilder();
	    Document doc = builder.newDocument();
	    Element rootElement = doc.createElement("config");
	    doc.appendChild(rootElement);

	    Element infoElement = doc.createElement("info");
	    rootElement.appendChild(infoElement);

	    Element name = doc.createElement("name");
	    name.appendChild(doc.createTextNode(config.getName()));
	    infoElement.appendChild(name);

	    Element desc = doc.createElement("description");
	    desc.appendChild(doc.createTextNode(config.getDescription()));
	    infoElement.appendChild(desc);

	    Element server = doc.createElement("server");
	    server.appendChild(doc.createTextNode(config.getRecommendedServerIP()));
	    infoElement.appendChild(server);

	    Element valueElement = doc.createElement("values");
	    rootElement.appendChild(valueElement);

	    for (ConfigItem item : config.getItems()) {
		valueElement.appendChild(createNode(item, doc));
	    }
	    return doc;
	} catch (Exception e) {

	}
	return null;
    }

    private static void createConfigEnvironment(File directory) {
	if (!directory.exists()) {
	    directory.mkdir();
	}
    }

    private static Node createNode(ConfigItem item, Document doc) {
	StringBuilder builder = new StringBuilder();
	for (int i = 0; i < item.getArgs().length; i++) {
	    builder.append(item.getArgs()[i]);
	    if (i != item.getArgs().length - 1) {
		builder.append(" ");
	    }
	}

	Element node = doc.createElement("value");
	node.appendChild(doc.createTextNode(builder.toString()));
	return node;
    }

}
