package BrettDanSmith.CryptoManager;

public class App {

	public static LoadingDialog dialog;

	public static void main(String[] args) {
		INSTANCE = new CryptoManager();
		INSTANCE.initConfig("data/config.cfg");
		dialog = new LoadingDialog();
		dialog.setVisible(true);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				exit(0);
			}
		}, "Shutdown-thread"));
		new Thread(new Runnable() {

			@Override
			public void run() {
				INSTANCE.init();
			}
		}).start();
	}

	public static CryptoManager INSTANCE;

	public static void exit(int i) {
		System.out.println("Exiting Crypto Manager...");
		Config.save();
		Runtime.getRuntime().halt(i);
		System.exit(i);
	}
}
