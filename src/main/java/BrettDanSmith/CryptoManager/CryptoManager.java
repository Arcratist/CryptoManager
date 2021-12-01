/**
 * 
 */
package BrettDanSmith.CryptoManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author BrettDanSmith
 *
 */
public class CryptoManager {

	// https://api.ethermine.org/miner/'d092B1c6eAC5523d9E3C8d99033b3B62789A1Fc2'/dashboard

	private CryptoManagerWindow mainWindow;

	public void init() {
		mainWindow = new CryptoManagerWindow();
	}

	public static String executePost(String targetURL) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(targetURL)).build();

		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		String jsonString = response.body();

		JsonParser parser = new JsonParser();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement el = parser.parse(jsonString);
		jsonString = gson.toJson(el); // done
		JsonObject obj = el.getAsJsonObject();
		JsonObject data = obj.getAsJsonObject("data");

		jsonString = data.toString();

		JsonElement el1 = parser.parse(jsonString);
		jsonString = gson.toJson(el1); // done

		return jsonString;
	}

	public void initConfig(String path) {
		new Config(path);
		try {
			UIManager.setLookAndFeel(
					Config.getOrDefault("LAF", "dark").equals("light") ? new FlatLightLaf() : new FlatDarkLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
	}

}
