/**
 * 
 */
package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

public abstract class Agent implements _Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5021567351232550115L;
	protected transient AgentServer server;
	protected transient String serverName;
	private Route route;

	private transient Socket socket;
	
	public Agent(Object... args) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run() {
		if (route.hasNext()) {
			Etape etape = route.next();
			etape.action.execute();

			move();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jus.aor.mobilagent.kernel._Agent#init(jus.aor.mobilagent.kernel.
	 * AgentServer, java.lang.String)
	 */
	@Override
	public void init(AgentServer agentServer, String serverName) {
		this.server = agentServer;
		this.serverName = serverName;
		if (route == null)
			route = new Route(new Etape(agentServer.site(), _Action.NIHIL));
		
		route.add(new Etape(this.server.site(), _Action.NIHIL));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jus.aor.mobilagent.kernel._Agent#reInit(jus.aor.mobilagent.kernel.
	 * AgentServer, java.lang.String)
	 */
	@Override
	public void reInit(AgentServer server, String serverName) {
		this.server = server;
		this.serverName = serverName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jus.aor.mobilagent.kernel._Agent#addEtape(jus.aor.mobilagent.kernel.
	 * Etape)
	 */
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
		return "Agent on server " + serverName + " with remaining route: " + route();

	}

	protected abstract _Action retour();
	
	protected void move(URI uri) {
		try {
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
}
