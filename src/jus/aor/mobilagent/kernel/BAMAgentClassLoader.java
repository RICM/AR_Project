/**
 * 
 */
package jus.aor.mobilagent.kernel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarOutputStream;

/**
 * @author Marwan Hallal
 *
 */
public class BAMAgentClassLoader extends ClassLoader {

	Map<String, byte[]> classes;

	/**
	 * @param parent
	 * @throws IOException
	 * @throws JarException
	 */
	public BAMAgentClassLoader(String jarName, ClassLoader parent) throws JarException, IOException {
		super(parent);
		Jar jar = new Jar(jarName);
		for (Entry<String, byte[]> entry : jar) {
			classes.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 
	 */
	public BAMAgentClassLoader(ClassLoader parent) {
		super(parent);
	}

	/**
	 * @param jar
	 */
	public void integrateCode(Jar jar) {
		for (Entry<String, byte[]> entry : jar) {
			classes.put(entry.getKey(), entry.getValue());
		}
	}

	public Jar extractCode() throws JarException, IOException {
		File jarFile = File.createTempFile("extracted", "jar");
		try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile))) {
			for (String name : classes.keySet()) {
				JarEntry je = new JarEntry(name);
				jos.putNextEntry(je);
				jos.write(classes.get(name));
			}
		}
		return new Jar(jarFile.getPath());
	}

}