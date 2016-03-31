/**
 * 
 */
package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class AgentServer implements Runnable {

	private Map<String, _Service<?>> services;
	private String name;
	private int port;

	public AgentServer(int p, String n) {
		port = p;
		name = n;
		services = new HashMap<String, _Service<?>>();
	}

	public void run() {
		try (ServerSocket servSoc = new ServerSocket(port)) {
			while (true) {
				try {
					// wait for incoming connections from mobile agents
					Socket clientSoc = servSoc.accept();
					// load the repository and the agent
					_Agent agent = getAgent(clientSoc);
					// launch the agent
					agent.reInit(this, name);
					new Thread(agent).start();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addService(String name, _Service<?> service) {
		services.put(name, service);
	}

	public String toString() {
		return name + ":" + port;
	};

	public _Service<?> getService(String name) {
		return services.get(name);
	}

	public URI site() {
		URI uri = null;
		try {
			uri = new URI("mobilagent", null, InetAddress.getLocalHost().getHostName(), port, null, null, null);
		} catch (URISyntaxException | UnknownHostException e) {
		}
		return uri;
	}

	private _Agent getAgent(Socket soc) throws IOException, ClassNotFoundException {
		BAMAgentClassLoader agentLoader = new BAMAgentClassLoader(this.getClass().getClassLoader());

		InputStream in = soc.getInputStream();
		ObjectInputStream inRepo = new ObjectInputStream(in);
		AgentInputStream ais = new AgentInputStream(in, agentLoader);

		Jar repo = (Jar) inRepo.readObject();

		agentLoader.integrateCode(repo);

		_Agent agent = null;
		agent = (_Agent) ais.readObject();
		ais.close();
		return agent;
	}

}
