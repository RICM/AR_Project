package jus.aor.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Chaine extends UnicastRemoteObject implements _Chaine {

	public Chaine() throws RemoteException {
		super();
		
	}

	@Override
	public List<Hotel> get(String localisation) {
		
	}

}
