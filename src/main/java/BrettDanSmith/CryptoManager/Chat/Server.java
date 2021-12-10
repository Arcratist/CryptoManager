package BrettDanSmith.CryptoManager.Chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public static ArrayList<PrintWriter> al = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		System.out.println("Server signing On");
		ServerSocket ss = new ServerSocket(9081);
		for (int i = 0; i < 8; i++) {
			Socket soc = ss.accept();
			Conversation c = new Conversation(soc);
			c.start();
		}
		System.out.println("Server signing Off");
	}

}

class Conversation extends Thread {

	Socket soc;
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

	public Conversation(Socket soc) {
		this.soc = soc;
	}

	@Override
	public void run() {
		System.out.println("Conversation thread  " + Thread.currentThread().getName() + "   signing On");
		try {

			BufferedReader nis = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			PrintWriter nos = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);
			Server.al.add(nos);
			String str = nis.readLine();
			while (!str.equals("End")) {
				System.out.println("Server Recieved  " + encrypt(str, 3));
				for (PrintWriter o : Server.al) {
					o.println(str);
				}
				str = nis.readLine();
			}
			nos.println("End");
		} catch (Exception e) {
			System.out.println("ChatClient Seems to have abruptly closed the connection");
		}
		System.out.println("Conversation thread  " + Thread.currentThread().getName() + "   signing Off");
	}

	public static String encrypt(String plainText, int shiftKey) {
		plainText = plainText.toLowerCase();
		String cipherText = "";
		for (int i = 0; i < plainText.length(); i++) {
			int charPosition = ALPHABET.indexOf(plainText.charAt(i));
			int keyVal = (shiftKey + charPosition) % 26;
			char replaceVal = ALPHABET.charAt(keyVal);
			cipherText += replaceVal;
		}
		return cipherText;
	}

	public static String decrypt(String cipherText, int shiftKey) {
		cipherText = cipherText.toLowerCase();
		String plainText = "";
		for (int i = 0; i < cipherText.length(); i++) {
			int charPosition = ALPHABET.indexOf(cipherText.charAt(i));
			int keyVal = (charPosition - shiftKey) % 26;
			if (keyVal < 0) {
				keyVal = ALPHABET.length() + keyVal;
			}
			char replaceVal = ALPHABET.charAt(keyVal);
			plainText += replaceVal;
		}
		return plainText;
	}
}