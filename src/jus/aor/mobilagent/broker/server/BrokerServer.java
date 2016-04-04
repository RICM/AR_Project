package jus.aor.mobilagent.broker.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import jus.aor.mobilagent.broker._ServiceBroker;

public class BrokerServer {

	public static void main(String[] args) {
		Registry registry;
		try {
			registry = LocateRegistry.createRegistry(1099);
			_ServiceBroker broker = new ServiceBroker();
			registry.rebind("Broker", broker);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
