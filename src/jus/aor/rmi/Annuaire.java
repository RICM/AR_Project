package jus.aor.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class Annuaire extends UnicastRemoteObject implements _Annuaire {

	Map<String, Numero> annuaire;

	protected Annuaire(Map<String, Numero> annuaire) throws RemoteException {
		this.annuaire = annuaire;
	}

	@Override
	public Numero get(String abonne) throws RemoteException {
		return annuaire.get(abonne);
	}

}
