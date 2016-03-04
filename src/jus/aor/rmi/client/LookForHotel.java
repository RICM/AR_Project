package jus.aor.rmi.client; /**
							* J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
							*/

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import java.util.regex.Pattern;

import jus.aor.rmi.common.Hotel;
import jus.aor.rmi.common.Numero;
import jus.aor.rmi.common._Annuaire;
import jus.aor.rmi.common._Chaine;
import jus.util.Calcul;

/**
 * Représente un client effectuant une requête lui permettant d'obtenir les
 * numéros de téléphone des hôtels répondant à son critère de choix.
 * 
 * @author Morat
 */
public class LookForHotel {

	/** le critère de localisaton choisi */
	private String localisation;

	// ...
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * 
	 * @param args
	 *            les arguments n'en comportant qu'un seul qui indique le
	 *            critère de localisation
	 */
	public LookForHotel(String... args) {
		localisation = args[0];

	}

	/**
	 * réalise une intérrogation
	 * 
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 */
	public long call() throws RemoteException {
		long start = System.nanoTime();
		List<Hotel> hotels = new ArrayList<>();
		Map<Hotel, Numero> ann = new HashMap<>();

		String[] chaines;

		try {
			// Découvrir toutes les chaines exposées par le registre et
			// interroger chacune.
			chaines = Naming.list("//localhost/");
			Pattern p = Pattern.compile("//localhost:1099/Chaine\\p{Digit}+");
			System.out.println("Recherche des chaines");
			for (String ch : chaines) {
				if (p.matcher(ch).matches()) {
					_Chaine c = (_Chaine) Naming.lookup(ch);
					List<Hotel> remoteHotels = c.get(localisation);
					hotels.addAll(remoteHotels);
				}
			}
			System.out.println("Total hotels in " + localisation + ": " + hotels.size());
			
			System.out.println("Recherche dans l'annuaire");
			_Annuaire annuaire = (_Annuaire) Naming.lookup("//localhost/Annuaire");
			for (Hotel hotel : hotels) {
				Numero num = annuaire.get(hotel.name);
				ann.put(hotel, num);
			}

		} catch (MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
		long end = System.nanoTime();

		return end - start;
	}

	// ...
}
