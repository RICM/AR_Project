package jus.aor.mobilagent.broker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface _ServiceBroker extends Remote {
	
	public abstract List<String> servers(String service) throws RemoteException;
	
	public abstract void announce(String server, String service) throws RemoteException;

}
