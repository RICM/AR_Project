package jus.aor.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import jus.aor.rmi.common.Numero;
import jus.aor.rmi.common._Annuaire;

public class Annuaire extends UnicastRemoteObject implements _Annuaire {

	private static final long serialVersionUID = -6861764277393976018L;
	Map<String, Numero> annuaire;

	protected Annuaire(Map<String, Numero> annuaire) throws RemoteException {
		this.annuaire = annuaire;
	}

	@Override
	public Numero get(String abonne) throws RemoteException {
		return annuaire.get(abonne);
	}

}
