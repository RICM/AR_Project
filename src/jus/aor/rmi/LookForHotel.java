package jus.aor.rmi; /**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Représente un client effectuant une requête lui permettant d'obtenir les numéros de téléphone des hôtels répondant à son critère de choix.
 * @author  Morat
 */
public class LookForHotel{

	/** le critère de localisaton choisi */
	private String localisation;
	

	// ...
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String... args){
		localisation = args[0];
	}

	/**
	 * réalise une intérrogation
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 */
	public long call() throws RemoteException {
		long start = System.nanoTime();
		List<Hotel> hotels = new ArrayList<>();
		Map<Hotel, Numero> ann = new HashMap<>();

		String[] chaines;
		
		try {
			// Découvrir toutes les chaines exposées par le registre et interroger chacune.
			chaines = Naming.list("//localhost/");
			for (String ch : chaines) {
				if(Pattern.matches("//*/Chaine*", ch)) {
					_Chaine c = (_Chaine) Naming.lookup(ch);
					hotels.addAll(c.get(localisation));
				}
			}
			
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
