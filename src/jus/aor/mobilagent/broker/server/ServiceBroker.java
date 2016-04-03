package jus.aor.mobilagent.broker.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jus.aor.mobilagent.broker._ServiceBroker;

public class ServiceBroker implements _ServiceBroker {

	// maps a service to a list of servers providing it
	private Map<String, List<String>> directory;

	public ServiceBroker() {
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
	}

}
