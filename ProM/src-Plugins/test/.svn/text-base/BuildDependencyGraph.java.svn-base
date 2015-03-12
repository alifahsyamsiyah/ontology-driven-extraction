package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BuildDependencyGraph {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		boolean includeLog = true;
		boolean includeProM = false;

		Map<String, String> map = new HashMap<String, String>();
		scanMainFolder(new File(".\\src\\"), map, true, null, null);
		if (includeProM) {
			scanFolder(new File(".\\src-Framework\\"), "Framework", map, true);
			scanFolder(new File(".\\src-Models\\"), "Models", map, true);
			scanFolder(new File(".\\src-Contexts\\"), "Contexts", map, true);
			scanFolder(new File(".\\src-Plugins\\"), "Plugins", map, true);
		}

		if (includeLog) {
			map.put("org.deckfour.xes.model.XLog", "Log");
			map.put("org.deckfour.xes.model.XTrace", "Log");
			map.put("org.deckfour.xes.model.XEvent", "Log");
			map.put("org.deckfour.xes.model.XAttribute", "Log");
		}

		// Now let's make a "core package"
		List<String> core = new ArrayList<String>();
		//		core.add("PetriNets");
		//		core.add("EPC");
		//		core.add("TransitionSystems");

		ProcessBuilder builder = new ProcessBuilder("C:\\Program Files (x86)\\Graphviz2.26.3\\bin\\dot.exe", "-Tjpg",
				"-oPackages.jpg");
		Process p = builder.start();
		OutputStream out = p.getOutputStream();
		out.write("digraph G{".getBytes());

		//		out.write("Core [shape=record,label=\"{Core|".getBytes());
		//		for (String s : core) {
		//			out.write("<".getBytes());
		//			out.write(s.getBytes());
		//			out.write("> ".getBytes());
		//			out.write(s.getBytes());
		//			out.write("|".getBytes());
		//		}
		//		out.write("}\"];\n".getBytes());

		scanMainFolder(new File(".\\src\\"), map, false, out, core);

		out.write("}".getBytes());
		out.close();

		//		buffer.append("}");
		//		System.out.println(buffer.toString());
	}

	private static void scanMainFolder(File folder, Map<String, String> map, boolean buildMap, OutputStream buffer,
			List<String> core) throws IOException {
		for (File f : folder.listFiles()) {
			if (f.isDirectory() && !f.getName().toLowerCase().endsWith(".svn")) {
				String n = f.getName();
				n = n.substring(0, n.length() - 4);
				Set<String> dependsOn = scanFolder(f, n, map, buildMap);
				dependsOn.remove(n);
				//				if (!dependsOn.isEmpty()) {
				//					System.out.println(n + " depends on:");
				//				}
				if (!buildMap && !core.contains(n)) {
					if (dependsOn.isEmpty()) {
						buffer.write(new String(n + ";\n").getBytes());
					}
					for (String s : dependsOn) {
						if (core.contains(s)) {
							s = "Core:" + s;
						}
						buffer.write(new String(s + " ->  " + n + ";\n").getBytes());
					}
				}
			}
		}
	}

	private static Set<String> scanFolder(File folder, String pack, Map<String, String> map, boolean buildMap)
			throws IOException {
		Set<String> dependsOn = new HashSet<String>();
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				if (!f.getName().toLowerCase().endsWith(".svn")) {
					if (buildMap) {
						scanFolder(f, pack, map, buildMap);
					} else {
						dependsOn.addAll(scanFolder(f, pack, map, buildMap));
					}
				}
			} else if (f.getName().toLowerCase().endsWith(".java")) {
				if (buildMap) {
					scanJavaFileForPackage(f, pack, map);
				} else {
					dependsOn.addAll(scanJavaFileForImport(f, pack, map));
				}
			}
		}
		return dependsOn;
	}

	private static String[] types = new String[] { " class ", " interface ", " @interface " };

	private static void scanJavaFileForPackage(File file, String pack, Map<String, String> map) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(file));

		String line = null; //not declared within while loop

		String prefix = "";
		while ((line = input.readLine()) != null) {
			line = line.trim();
			// line read
			if (line.startsWith("package")) {
				prefix = line.substring(8, line.length() - 1) + ".";
			} else
				for (String type : types) {
					if (line.contains(type)) {
						String classLine = line.substring(line.indexOf(type) + type.length());
						int i = Math.min(classLine.indexOf(" "), classLine.indexOf("{"));
						if (i > 0) {
							classLine = classLine.substring(0, i);
						}
						map.put(prefix + classLine, pack);
					}
				}
		}
		input.close();
		return;

	}

	private static Set<String> scanJavaFileForImport(File file, String pack, Map<String, String> map)
			throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(file));

		String line = null; //not declared within while loop
		Set<String> dependsOn = new HashSet<String>();

		while ((line = input.readLine()) != null) {
			line = line.trim();
			// line read
			if (line.startsWith("import ")) {
				String imported = line.substring(7, line.length() - 1);
				String importedPack = map.get(imported);
				if (importedPack != null) {
					dependsOn.add(importedPack);
				} else if (imported.startsWith("org.processmining")) {
					//					System.err.println(imported);
				}
			}
		}
		input.close();
		return dependsOn;
	}

}
