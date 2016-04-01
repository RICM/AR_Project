package jus.aor.mobilagent.hostel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.mobilagent.kernel._Service;

public class Annuaire implements _Service<Numero> {

	private Map<String, Numero> annuaire;

	public Annuaire(Object... args) {
		String xmlFile = (String) args[0];
		annuaire = loadDirectory(xmlFile);
	}

	@Override
	public Numero call(Object... params) throws IllegalArgumentException {
		if (params.length != 1)
			throw new IllegalArgumentException();
		String abonne = (String) params[0];
		return annuaire.get(abonne);
	}

	private Map<String, Numero> loadDirectory(String file) {
		Map<String, Numero> annuaire = new HashMap<>();
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			NodeList hotelNodes = doc.getElementsByTagName("Telephone");
			for (int i = 0; i < hotelNodes.getLength(); i++) {
				NamedNodeMap m = hotelNodes.item(i).getAttributes();
				String name = m.getNamedItem("name").getNodeValue();
				String numero = m.getNamedItem("numero").getNodeValue();
				annuaire.put(name, new Numero(numero));
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return annuaire;
	}

}
