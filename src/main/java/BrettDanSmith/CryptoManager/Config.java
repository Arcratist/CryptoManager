/**
 * 
 */
package BrettDanSmith.CryptoManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author BrettDanSmith
 */
public class Config {

	private static HashMap<String, String> properties = new HashMap<String, String>();
	public static File configFile;

	public Config(String path) {
		configFile = new File(path);
		System.out.println("Loading config file: " + configFile.getAbsolutePath());
		if (!configFile.exists()) {
			generateConfigFile(configFile);
		} else {
			try {
				Scanner myReader = new Scanner(configFile);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					if (data.startsWith("#"))
						continue;
					String[] sData = data.split(";");
					properties.put(sData[0], sData[1]);
				}
				myReader.close();
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
		System.out.println("Loaded config file: " + configFile.getAbsolutePath());
	}

	private void generateConfigFile(File configFile) {
		System.out.println("Generating new config file: " + configFile.getAbsolutePath());
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		String defaultConfig = "#CryptoManager Config\r\n";
		save(defaultConfig);
		System.out.println("Generated new config file: " + configFile.getAbsolutePath());
	}

	public static String getOrDefault(String key, String defaultValue) {
		return properties.getOrDefault(key, defaultValue).strip();
	}

	public static void save() {
		System.out.println("Saving config...");
		String configString = "#CryptoManager Config\n";

		for (String key : properties.keySet()) {
			configString += (key + ";" + properties.get(key).toString() + "\n");
		}

		try (PrintStream out = new PrintStream(new FileOutputStream(configFile))) {
			out.print(configString);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Saved config!");
	}

	public static void save(String configString) {
		System.out.println("Saving config...");
		try (PrintStream out = new PrintStream(new FileOutputStream(configFile))) {
			out.print(configString);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Saved config!");
	}

	public static void set(String key, String value) {
		properties.put(key, value);
	}
}
