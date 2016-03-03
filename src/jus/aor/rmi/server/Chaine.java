package jus.aor.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import jus.aor.rmi.common.Hotel;
import jus.aor.rmi.common._Chaine;

public class Chaine extends UnicastRemoteObject implements _Chaine {

	private static final long serialVersionUID = -6907591845521765699L;
	List<Hotel> hotels;

	public Chaine(List<Hotel> hotels) throws RemoteException {
		this.hotels = hotels;
	}

	@Override
	public List<Hotel> get(String localisation) throws RemoteException {
		List<Hotel> list = new ArrayList<>();
		for (Hotel hotel : hotels) {
			if (hotel.localisation.equals(localisation))
				list.add(hotel);
		}
		return list;
	}
}
