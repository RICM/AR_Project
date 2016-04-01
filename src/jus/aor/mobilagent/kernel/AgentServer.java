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
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentServer implements Runnable {

	private Map<String, _Service<?>> services;
	private String name;
	private int port;
	
	private Logger logger;
	private String loggerName;

	public AgentServer(int p, String n) {
		port = p;
		name = n;
		services = new HashMap<String, _Service<?>>();
		
		try {
			loggerName = "jus/aor/mobilagent/" + InetAddress.getLocalHost().getHostName() + "/" +this.name;
			logger = Logger.getLogger(loggerName);
		} catch (UnknownHostException e) {}
	}

	public void run() {
		try (ServerSocket servSoc = new ServerSocket(port)) {
			while (true) {
				try {
					// wait for incoming connections from mobile agents
					Socket clientSoc = servSoc.accept();
					// load the repository and the agent
					_Agent agent = getAgent(clientSoc);
					assert agent != null;
					logger.log(Level.INFO, String.format("[%s:%d] received agent %s", name, port, agent.getClass().getName()));
					// launch the agent
					logger.log(Level.INFO, String.format("[%s:%d] reinitializing agent...", name, port));
					agent.reInit(this, name);
					logger.log(Level.INFO, String.format("[%s:%d] starting agent...", name, port));
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
