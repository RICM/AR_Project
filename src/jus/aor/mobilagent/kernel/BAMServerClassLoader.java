/**
 * 
 */
package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Marwan Hallal
 *
 */
public class BAMServerClassLoader extends URLClassLoader {

	/**
	 * @param urls
	 * @param parent
	 */
	public BAMServerClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
		
	}
	
	public void addURL(URL url) {
		super.addURL(url);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
