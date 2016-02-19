package jus.aor.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Annuaire extends UnicastRemoteObject implements _Annuaire{

	protected Annuaire() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Numero get(String abonne) {
		// TODO Auto-generated method stub
		return null;
	}

}
