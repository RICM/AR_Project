/**
 * 
 */
package jus.aor.mobilagent.kernel;

import java.net.Socket;
import java.net.URI;
import java.util.Map;

/**
 * @author Marwan Hallal
 *
 */
public class AgentServer {
	
	Map<String, _Service<?>> services;
	
	public AgentServer(int i, String s) {}
	
	public void run() {}
	
	public void addService(String name, _Service<?> service) {
		services.put(name, service);
	}
	
	public String toString() {return super.toString();};
	
	public _Service<?> getService(String name) {
		return services.get(name);
	}
	
	public URI site() {return null;}
	
	private _Agent getAgent(Socket s) {return null;}
	
	
	
	
	
	
	
	

}
