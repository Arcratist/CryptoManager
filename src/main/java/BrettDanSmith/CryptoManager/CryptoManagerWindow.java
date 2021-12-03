package BrettDanSmith.CryptoManager;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;

import com.eleet.dragonconsole.CommandProcessor;
import com.eleet.dragonconsole.DragonConsole;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.StartDownloadCallback;
import com.teamdev.jxbrowser.download.Download;
import com.teamdev.jxbrowser.download.DownloadTarget;
import com.teamdev.jxbrowser.download.event.DownloadFinished;
import com.teamdev.jxbrowser.download.event.DownloadUpdated;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.navigation.event.LoadFinished;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class CryptoManagerWindow extends JFrame {
	private static final long serialVersionUID = -6986453964668526169L;
	private JPanel contentPane;
	private InfoDialog infoDialog;
	private JLabel lblTitle, lblUnpaidBalance, lblStats;
	private JProgressBar progressBar;
	private DragonConsole txtLog;
	private StartMinerDialog minerDialog;

	/**
	 * Create the frame.
	 * 
	 * @param string
	 */
	public CryptoManagerWindow() {
		URL iconURL = getClass().getResource("/BrettDanSmith/CryptoManager/icon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());
		infoDialog = new InfoDialog();
		minerDialog = new StartMinerDialog();
		new OptionsWindow(this);

		String string = CryptoManager.executePost("https://api.ethermine.org/miner/'"
				+ Config.getOrDefault("WALLET", "d092B1c6eAC5523d9E3C8d99033b3B62789A1Fc2") + "'/currentStats");

		System.setProperty("jxbrowser.license.key",
				"6P835FT5HAPTB03TPIEFPGU5ECGJN8GMGDD79MD7Y52NVP0K0IV6FHYZVQI25H0MLGI2");
		Engine engine = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.OFF_SCREEN)
				.userDataDir(Paths.get(new File("data/.jxbrowser/").getAbsolutePath())).build());
		Browser browser = engine.newBrowser();
		browser.set(StartDownloadCallback.class, (params, tell) -> {
			Download download = params.download();
			// Download target details.
			DownloadTarget downloadTarget = download.target();

			System.out.println("Attempting to download: " + downloadTarget.url());
//			infoDialog.setVisible(true);
			// Tell the engine to download and save the file.
			tell.download(Paths.get("data/downloads/" + downloadTarget.suggestedFileName()));

			download.on(DownloadFinished.class, event -> {
				System.out.println("Finished downloading: " + downloadTarget.url());
//				infoDialog.setVisible(false);
			});

			download.on(DownloadUpdated.class, event -> {
				// Print download progress in percents.
				event.progress().ifPresent(progress -> System.out.println((int) (progress.value()) + "%"));
//				event.progress().ifPresent(progress -> infoDialog.setData((int) (progress.value()) + "%"));

				// The current download speed estimate in bytes/second.
				long currentSpeed = event.currentSpeed();
				// The total size of a file in bytes.
				long totalBytes = event.totalBytes();
				// The number or received (downloaded) bytes.
				long receivedBytes = event.receivedBytes();
			});

		});

		setTitle("Crypto Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(200, 400));
		setPreferredSize(new Dimension(1280, 720));
		setSize(new Dimension(1280, 720));
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		JMenuItem mntm_File_Options = new JMenuItem("Options");
		mntm_File_Options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OptionsWindow.Open();
			}
		});
		mnFile.add(mntm_File_Options);

		JMenuItem mntm_File_Exit = new JMenuItem("Exit");
		mntm_File_Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				App.exit(0);
			}
		});
		mnFile.add(mntm_File_Exit);

		JMenu mnBrowser = new JMenu("Browser");
		mnBrowser.setMnemonic('B');
		menuBar.add(mnBrowser);

		JMenu mnBrowser_GoTo = new JMenu("Go To");
		mnBrowser_GoTo.setMnemonic('G');
		mnBrowser.add(mnBrowser_GoTo);

		JMenuItem mntmBrowser_GoTo_Home = new JMenuItem("Home");
		mntmBrowser_GoTo_Home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browser.navigation().loadUrl(Config.getOrDefault("HOME_URL", "https://google.com.au"));
			}
		});
		mnBrowser_GoTo.add(mntmBrowser_GoTo_Home);

		JMenuItem mntmBrowser_GoTo_Ethermine = new JMenuItem("Ethermine");
		mntmBrowser_GoTo_Ethermine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browser.navigation().loadUrl("https://ethermine.org/miners/"
						+ Config.getOrDefault("WALLET", "d092B1c6eAC5523d9E3C8d99033b3B62789A1Fc2") + "/dashboard");
			}
		});
		mnBrowser_GoTo.add(mntmBrowser_GoTo_Ethermine);

		JMenuItem mntmBrowser_GoTo_OpenSea = new JMenuItem("OpenSea");
		mntmBrowser_GoTo_OpenSea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browser.navigation().loadUrl("https://opensea.io");
			}
		});
		mnBrowser_GoTo.add(mntmBrowser_GoTo_OpenSea);

		JMenuItem mntmBrowser_GoTo_Polygon = new JMenuItem("Polygon");
		mntmBrowser_GoTo_Polygon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browser.navigation().loadUrl("https://polygon.technology");
			}
		});
		mnBrowser_GoTo.add(mntmBrowser_GoTo_Polygon);

		JMenuItem mntmBrowser_GoTo_Coinspot = new JMenuItem("Coinspot");
		mntmBrowser_GoTo_Coinspot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browser.navigation().loadUrl("https://www.coinspot.com.au/");
			}
		});
		mnBrowser_GoTo.add(mntmBrowser_GoTo_Coinspot);

		JMenuItem mntmBrowser_GoTo_CMC = new JMenuItem("CoinMarketCap");
		mntmBrowser_GoTo_CMC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browser.navigation().loadUrl("https://coinmarketcap.com/");
			}
		});
		mnBrowser_GoTo.add(mntmBrowser_GoTo_CMC);

		JMenu mnMiner = new JMenu("Miner");
		mnMiner.setMnemonic('M');
		menuBar.add(mnMiner);

		JMenuItem mntmMiner_Start = new JMenuItem("Start Miner (Separate Process)");
		mntmMiner_Start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				minerDialog.setVisible(true);
			}
		});

		JMenuItem mntmMiner_Download = new JMenuItem("Download Miner");
		mntmMiner_Download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileUtils.copyURLToFile(
							new URL("https://github.com/Arcratist/CryptoManager/blob/master/PhoenixMiner.zip?raw=true"),
							new File("data/tmp/pmdownload.zip"));
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				}
				ZipFile zipFile = new ZipFile(new File("data/tmp/pmdownload.zip"));
				try {
					zipFile.extractAll(new File("data/").getAbsolutePath());
				} catch (ZipException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnMiner.add(mntmMiner_Download);
		mntmMiner_Start.setToolTipText(
				"Starts PhoenixMiner in a separate process than the Crypto Manager.\nClosing the Crypto Manager will not close PhoenixMiner and vice versa.");
		mnMiner.add(mntmMiner_Start);

		JMenu mnAbout = new JMenu("About");
		mnAbout.setMnemonic('A');
		menuBar.add(mnAbout);

		JMenuItem mntmNewMenuItem = new JMenuItem("Info");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				infoDialog.setVisible(true);
			}
		});
		mnAbout.add(mntmNewMenuItem);

		browser.navigation().on(LoadFinished.class, event -> {
			Config.set("LAST_URL", browser.url().strip());
		});
		contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(0.6);
		sl_contentPane.putConstraint(SpringLayout.NORTH, splitPane, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, splitPane, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, splitPane, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, splitPane, 0, SpringLayout.EAST, contentPane);
		contentPane.add(splitPane);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setContinuousLayout(true);
		splitPane_1.setResizeWeight(0.8);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(splitPane_1);

		JPanel panel = new JPanel();

		splitPane_1.setLeftComponent(BrowserView.newInstance(browser));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane_1.setRightComponent(tabbedPane);
		tabbedPane.setMinimumSize(new Dimension(0, 0));

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Economy", null, panel_3, null);
		SpringLayout sl_panel_3 = new SpringLayout();
		panel_3.setLayout(sl_panel_3);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Notes", null, panel_1, null);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sl_panel_1.putConstraint(SpringLayout.NORTH, scrollPane_1, 0, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, scrollPane_1, 0, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, scrollPane_1, 0, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, scrollPane_1, 0, SpringLayout.EAST, panel_1);
		panel_1.add(scrollPane_1);

		JTextPane textPane = new JTextPane();
		scrollPane_1.setViewportView(textPane);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Console", null, panel_2, null);
		SpringLayout sl_panel_2 = new SpringLayout();
		panel_2.setLayout(sl_panel_2);

		JScrollPane scrollPane_2 = new JScrollPane();
		sl_panel_2.putConstraint(SpringLayout.NORTH, scrollPane_2, 0, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, scrollPane_2, 0, SpringLayout.SOUTH, panel_2);
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sl_panel_2.putConstraint(SpringLayout.WEST, scrollPane_2, 0, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, scrollPane_2, 0, SpringLayout.EAST, panel_2);
		panel_2.add(scrollPane_2);

		browser.navigation().loadUrl(Config.getOrDefault("LAST_URL", "https://google.com.au"));
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		txtLog = new DragonConsole(false, false);
		txtLog.setAutoscrolls(true);
		txtLog.setPrompt("Crypto Manager>>");
		txtLog.setCommandProcessor(new CommandProcessor() {
			
			@Override
		    public void processCommand(String input) {
		        super.processCommand("Crypto Manager>>" + input);
		        if (input.startsWith("/")) {
		        	String originalCommand = input.substring(1);
		        	String[] command = originalCommand.split(" ");
		        	
		        	if (command[0].toLowerCase().equals("help")) {
		        		txtLog.append("======================== HELP ========================\n");
		        		txtLog.append(" /help - Displays this message.\n");
		        		txtLog.append(" /exit - Exits the application.\n");
		        		txtLog.append(" /killminer - Kills any running miner instances.\n");
		        		txtLog.append(" /save - Saves the configuration files.\n");
		        		txtLog.append(" //cleardata - WARNING! Clears the data directory.\n");
		        		txtLog.append("======================================================\n");
		        	}
		        	
		        	if (command[0].toLowerCase().equals("exit")) {
		        		App.exit(0);
		        	}
		        	
		        	if (command[0].toLowerCase().equals("save")) {
		        		Config.save();
		        	}
		        	
		        	if (command[0].toLowerCase().equals("/cleardata")) {
		        		txtLog.append("DELETING DATA DIRECTORY!\n");
		        	}
		        }
		    }
			
		});
		scrollPane_2.setViewportView(txtLog);

		JScrollPane scrollPane = new JScrollPane();
		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 100, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, panel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane);
		splitPane.setRightComponent(panel);

		lblTitle = new JLabel("Ethermine Stats");
		sl_panel.putConstraint(SpringLayout.WEST, lblTitle, 120, SpringLayout.WEST, scrollPane);
		sl_panel.putConstraint(SpringLayout.EAST, lblTitle, -120, SpringLayout.EAST, scrollPane);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panel.putConstraint(SpringLayout.NORTH, lblTitle, 5, SpringLayout.NORTH, panel);
		panel.add(lblTitle);

		progressBar = new JProgressBar();
		progressBar.setMaximum(500);
		progressBar.setStringPainted(true);
		progressBar.setToolTipText("");
		sl_panel.putConstraint(SpringLayout.WEST, progressBar, 5, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, progressBar, -5, SpringLayout.EAST, panel);
		panel.add(progressBar);

		lblUnpaidBalance = new JLabel("");
		sl_panel.putConstraint(SpringLayout.WEST, lblUnpaidBalance, -50, SpringLayout.WEST, lblTitle);
		sl_panel.putConstraint(SpringLayout.EAST, lblUnpaidBalance, 50, SpringLayout.EAST, lblTitle);
		sl_panel.putConstraint(SpringLayout.NORTH, progressBar, 5, SpringLayout.SOUTH, lblUnpaidBalance);
		lblUnpaidBalance.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panel.putConstraint(SpringLayout.NORTH, lblUnpaidBalance, 5, SpringLayout.SOUTH, lblTitle);

		panel.add(lblUnpaidBalance);

		lblStats = new JLabel("");
		sl_panel.putConstraint(SpringLayout.SOUTH, lblStats, -5, SpringLayout.NORTH, scrollPane);

		Box verticalBox = Box.createVerticalBox();
		scrollPane.setViewportView(verticalBox);
		lblStats.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panel.putConstraint(SpringLayout.NORTH, lblStats, 5, SpringLayout.SOUTH, progressBar);
		sl_panel.putConstraint(SpringLayout.WEST, lblStats, 5, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, lblStats, -5, SpringLayout.EAST, panel);
		panel.add(lblStats);

		JButton btn_Refresh = new JButton("");
		Image img = new ImageIcon(CryptoManagerWindow.class.getResource("/BrettDanSmith/CryptoManager/refresh.png"))
				.getImage();
		btn_Refresh.setIcon(new ImageIcon(img.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		btn_Refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshData(CryptoManager.executePost("https://api.ethermine.org/miner/'"
						+ Config.getOrDefault("WALLET", "d092B1c6eAC5523d9E3C8d99033b3B62789A1Fc2")
						+ "'/currentStats"));
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btn_Refresh, 5, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, btn_Refresh, -25, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btn_Refresh, 25, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btn_Refresh, -5, SpringLayout.EAST, panel);
		panel.add(btn_Refresh);

		JCheckBox radioBtnAutoRefresh = new JCheckBox("Auto Refresh");
		radioBtnAutoRefresh.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Config.set("AUTO_REFRESH", Boolean.valueOf(radioBtnAutoRefresh.isSelected()).toString());
				radioBtnAutoRefresh.revalidate();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, radioBtnAutoRefresh, 5, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, radioBtnAutoRefresh, 25, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, radioBtnAutoRefresh, -5, SpringLayout.WEST, btn_Refresh);
		panel.add(radioBtnAutoRefresh);
		radioBtnAutoRefresh.setSelected(Boolean.valueOf(Config.getOrDefault("AUTO_REFRESH", "false")));
		radioBtnAutoRefresh.revalidate();
		refreshData(string);
		setVisible(true);
		App.dialog.setVisible(false);
	}

	private void refreshData(String string) {
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(string);
		JsonObject obj = el.getAsJsonObject();
		JsonPrimitive data0 = obj.getAsJsonPrimitive("unpaid");
		JsonPrimitive data1 = obj.getAsJsonPrimitive("activeWorkers");
		JsonPrimitive data2 = obj.getAsJsonPrimitive("validShares");
		JsonPrimitive data3 = obj.getAsJsonPrimitive("currentHashrate");
		JsonPrimitive data4 = obj.getAsJsonPrimitive("reportedHashrate");
		JsonPrimitive data5 = obj.getAsJsonPrimitive("usdPerMin");

		float ff = Float.parseFloat(data0.toString());
		int ff1 = Integer.parseInt(data1.toString());
		int ff2 = Integer.parseInt(data2.toString());
		float ff3 = Float.parseFloat(data3.toString()) / 1000000f;
		float ff4 = Float.parseFloat(data4.toString()) / 1000000f;
		float uPB = (ff / 1000000000000000000f);
		float ff5 = (((Float.parseFloat(data5.toString())) * 60.00f) * 24.00f) * 1.430f;

		lblUnpaidBalance.setText("Unpaid Balance: " + uPB + " / 0.00500 ETH");
		progressBar.setValue((int) (uPB * 100000f));
		lblStats.setText("Workers: " + ff1 + "  |  Shares: " + ff2 + "  |  Current Hashrate: "
				+ ((double) Math.round(ff3 * 100) / 100) + "MH/s  |  Reported Hashrate: "
				+ ((double) Math.round(ff4 * 100) / 100) + "MH/s  |  $" + ((double) Math.round(ff5 * 1000) / 1000) + "/d");
	}
}
