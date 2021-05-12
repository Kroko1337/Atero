package de.verschwiegener.atero.util.files.config.handler;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class XMLHelper {

    public static void parse() throws ParserConfigurationException, SAXException {
	SAXParserFactory factory = SAXParserFactory.newInstance();
	factory.setValidating(true);
	SAXParser saxParser = factory.newSAXParser();
	File file = new File("test.xml");
	try {
	    saxParser.parse(file, new InfoHandler());
	} catch (SAXException | IOException e) {
	    e.printStackTrace();
	}
	//TODO Make XML Parser
    }

}
