package jus.aor.mobilagent.hostel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.mobilagent.kernel._Service;
import jus.aor.mobilagent.hostel.Hotel;

public class Chaine implements _Service<Collection<Hotel>> {

	private Collection<Hotel> hotels;

	public Chaine(Object... args) {
		String xmlFile = (String) args[0];
		hotels = loadHotels(xmlFile);
	}

	@Override
	public Collection<Hotel> call(Object... params) throws IllegalArgumentException {
		if (params.length != 1)
			throw new IllegalArgumentException();
		String localisation = (String) params[0];
		Collection<Hotel> hotelsInLocalisation = new ArrayList<Hotel>();
		for (Hotel h : hotels) {
			if(h.localisation.equals(localisation))
				hotelsInLocalisation.add(h);
		}
		return hotelsInLocalisation;
	}

	private Collection<Hotel> loadHotels(String file) {
		Collection<Hotel> hotels = new ArrayList<>();
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(new File(file));
			NodeList hotelNodes = doc.getElementsByTagName("Hotel");
			for (int i = 0; i < hotelNodes.getLength(); i++) {
				NamedNodeMap m = hotelNodes.item(i).getAttributes();
				String name = m.getNamedItem("name").getNodeValue();
				String localisation = m.getNamedItem("localisation").getNodeValue();
				hotels.add(new Hotel(name, localisation));
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return hotels;
	}

}
