/**
 * 
 */
package jus.aor.mobilagent.kernel;

import java.io.IOException;
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

	private BAMServerClassLoader serverLoader;

	public AgentServer(int p, String n) {
		port = p;
		name = n;
		services = new HashMap<String, _Service<?>>();

	}

	public void run() {
		ServerSocket servSoc = null;
		try {
			servSoc = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

		Socket clientSoc;
		BAMAgentClassLoader agentLoader = new BAMAgentClassLoader(this.getClass().getClassLoader());
		while (true) {
			try {
				clientSoc = servSoc.accept();

				// get the repository
				Jar jar = (Jar) ais.readObject();
				agentLoader.integrateCode(jar);
				// get the mobile agent
				_Agent agent = getAgent(clientSoc);
				agent.init(this, name);
				new Thread(agent).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			}
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
		ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
		Jar repo = (Jar) in.readObject();
		BAMAgentClassLoader agentLoader = new BAMAgentClassLoader(this.getClass().getClassLoader());
		agentLoader.integrateCode(repo);
		_Agent agent = null;
		try (AgentInputStream ais = new AgentInputStream(soc.getInputStream(), agentLoader) {
			agent = (_Agent) ais.readObject();
		}
		return agent;
	}

}
