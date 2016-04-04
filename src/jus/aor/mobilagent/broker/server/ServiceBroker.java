package jus.aor.mobilagent.broker.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jus.aor.mobilagent.broker._ServiceBroker;

public class ServiceBroker extends UnicastRemoteObject implements _ServiceBroker {

	private static final long serialVersionUID = -2266589104664281904L;
	
	// maps a service to a list of servers providing it
	private Map<String, List<String>> directory;

	public ServiceBroker() throws RemoteException {
		this.directory = new HashMap<>();
	}

	@Override
	public List<String> servers(String service) throws RemoteException {
		return this.directory.get(service);
	}

	@Override
	public void announce(String server, String service) throws RemoteException {
		if (this.directory.containsKey(service)) {
			List<String> servers = this.directory.get(service);
			servers.add(server);
		} else {
			List<String> l = new ArrayList<>();
			l.add(server);
			this.directory.put(service, l);
		}
		System.out.println("Server " + server + " announced service " + service);
	}

}
