/**
 * 
 */
package jus.aor.mobilagent.kernel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarOutputStream;

public class BAMAgentClassLoader extends ClassLoader {

	Map<String, byte[]> classes;

	/**
	 * @param parent
	 * @throws IOException
	 * @throws JarException
	 */
	public BAMAgentClassLoader(String jarName, ClassLoader parent) throws JarException, IOException {
		super(parent);
		classes = new HashMap<>();
		Jar jar = new Jar(jarName);
		integrateCode(jar);
	}

	public BAMAgentClassLoader(ClassLoader parent) {
		super(parent);
		classes = new HashMap<>();
	}

	/**
	 * @param jar
	 * @throws ClassNotFoundException 
	 */
	public void integrateCode(Jar jar) {
		for (Entry<String, byte[]> entry : jar) {
			String className = className(entry.getKey());
			classes.put(className, entry.getValue());
			Class<?> c = defineClass(className, entry.getValue(), 0, entry.getValue().length);
			// class must be resolved before it can be used
			try {
				loadClass(className);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	private String className(String s) {
		return s.replace("/", ".").replace(".class", "");
	}

}