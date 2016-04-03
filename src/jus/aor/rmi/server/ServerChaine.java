package jus.aor.rmi.server;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.rmi.common.Hotel;

/**
 * Created by matthieu on 19/02/16.
 */
public class ServerChaine {

	public static void main(String args[]) {
		
		if(System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		if (args.length < 4) {
			System.out.println("params: <service-name> <data-file> <host> <port>");
			System.exit(1);
		}

		String serviceName = args[0];
		String host = args[2];
		int port = Integer.parseInt(args[3]);

		try {
			List<Hotel> hotels = new ArrayList<>();
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(new File(args[1]));
			NodeList hotelNodes = doc.getElementsByTagName("Hotel");
			for (int i = 0; i < hotelNodes.getLength(); i++) {
				NamedNodeMap m = hotelNodes.item(i).getAttributes();
				String name = m.getNamedItem("name").getNodeValue();
				String localisation = m.getNamedItem("localisation").getNodeValue();
				hotels.add(new Hotel(name, localisation));
			}
			System.out.println(hotels.size());
			Chaine ch = new Chaine(hotels);
			Registry r = LocateRegistry.getRegistry(host, port);
			r.rebind(serviceName, ch);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

}
