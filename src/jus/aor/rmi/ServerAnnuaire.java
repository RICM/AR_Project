/**
 * 
 */
package jus.aor.rmi;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Marwan Hallal
 *
 */
public class ServerAnnuaire {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
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
			Map<String, Numero> annuaire = new HashMap<>();
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(new File(args[1]));
			NodeList hotelNodes = doc.getElementsByTagName("Telephone");
			for (int i = 0; i < hotelNodes.getLength(); i++) {
				NamedNodeMap m = hotelNodes.item(i).getAttributes();
				String name = m.getNamedItem("name").getNodeValue();
				String numero = m.getNamedItem("numero").getNodeValue();
				annuaire.put(name, new Numero(numero));
			}
			_Annuaire an = new Annuaire(annuaire);
			System.out.println("Getting registry at " + host + ":" + port);
			Registry r = LocateRegistry.getRegistry(host, port);
			//Registry r = LocateRegistry.createRegistry(port);
			r.rebind(serviceName, an);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

}
