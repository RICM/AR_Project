/**
 * 
 */
package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jus.aor.mobilagent.broker._ServiceBroker;

public abstract class Agent implements _Agent {

	private static final long serialVersionUID = -5021567351232550115L;

	private Route route;

	protected transient AgentServer server;
	protected transient String serverName;
	private transient Socket socket;

	protected transient String loggerName;
	protected transient Logger logger;

	public Agent(Object... args) {
	}

	@Override
	public final void run() {
		if (route.hasNext()) {
			Etape etape = route.next();
			etape.action.execute();

			move();
		}
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		try {
			loggerName = "jus/aor/mobilagent/" + InetAddress.getLocalHost().getHostName() + "/" + serverName;
			logger = Logger.getLogger(loggerName);
		} catch (UnknownHostException e) {}
		
		this.server = agentServer;
		this.serverName = serverName;
		if (route == null)
			route = new Route(new Etape(agentServer.site(), this.retour()));

		// first action to perform on the initial server is empty
		route.add(new Etape(this.server.site(), _Action.NIHIL));
		
		discoverAgentServers();

		
	}

	@Override
	public void reInit(AgentServer server, String serverName) {
		this.server = server;
		this.serverName = serverName;

		try {
			loggerName = "jus/aor/mobilagent/" + InetAddress.getLocalHost().getHostName() + "/" + serverName;
			logger = Logger.getLogger(loggerName);
		} catch (UnknownHostException e) {
		}
	}

	@Override
	public final void addEtape(Etape etape) {
		route.add(etape);
	}

	protected _Service<?> getService(String name) {
		return server.getService(name);
	}

	protected String route() {
		return route.toString();
	}

	public final String toString() {
		return "Agent on server [" + serverName + "] with route: " + route.toString();

	}

	protected abstract _Action retour();

	protected void move(URI uri) {
		try {
			logger.log(Level.INFO, String.format("[Agent] moving to %s:%d", uri.getHost(), uri.getPort()));
			socket = new Socket(uri.getHost(), uri.getPort());
			BAMAgentClassLoader agentLoader = (BAMAgentClassLoader) this.getClass().getClassLoader();
			Jar repo = agentLoader.extractCode();
			OutputStream out = socket.getOutputStream();
			ObjectOutputStream outRepo = new ObjectOutputStream(out);
			ObjectOutputStream outAgent = new ObjectOutputStream(out);
			outRepo.writeObject(repo);
			outAgent.writeObject(this);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void move() {
		move(route.get().server);
	}

	/*
	 * Renvoie un tableau associatif: clé: nom de l'action valeur: nom du
	 * service
	 */
	protected abstract Map<String, String> actions();

	/*
	 * Demande pour chaque service requis par l'agent, une liste de serveurs le
	 * fournissant et ajoute ces serveurs à la feuille de route
	 */
	private void discoverAgentServers() {
		_ServiceBroker broker;
		try {
			broker = (_ServiceBroker) Naming.lookup("//localhost:1099/Broker");
			for (Entry<String, String> e : actions().entrySet()) {
				String action = e.getKey();
				String service = e.getValue();

				List<String> servers = broker.servers(service);

				//Class<?> agentClass = Class.forName(this.getClass().getCanonicalName());
				//Field f = agentClass.getDeclaredField(action);
				Field f = this.getClass().getDeclaredField(action);
				f.setAccessible(true);
				_Action actionInstance = (_Action) f.get(this);
				for (String server : servers) {
					addEtape(new Etape(new URI(server), actionInstance));
				}
			}

		} catch (RemoteException | NotBoundException | MalformedURLException
				| NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| URISyntaxException | NullPointerException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
