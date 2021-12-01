package BrettDanSmith.CryptoManager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class StartMinerDialog extends JDialog {
	private static final long serialVersionUID = 5432324306626720764L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldWallet;
	private JLabel lblWallet;
	private JTextField textFieldName;

	/**
	 * Create the dialog.
	 */
	public StartMinerDialog() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Start Miner");
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);

		JLabel lblCoinType = new JLabel("Coin: ");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblCoinType, 5, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblCoinType, 5, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblCoinType, 25, SpringLayout.NORTH, contentPanel);
		contentPanel.add(lblCoinType);

		JComboBox<String> coinType = new JComboBox<String>();
		sl_contentPanel.putConstraint(SpringLayout.WEST, coinType, 15, SpringLayout.EAST, lblCoinType);
		coinType.setModel(new DefaultComboBoxModel<String>(new String[] { "ETH", "ETC", "MATIC" }));
		sl_contentPanel.putConstraint(SpringLayout.NORTH, coinType, 5, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, coinType, 25, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, coinType, -5, SpringLayout.EAST, contentPanel);
		coinType.setSelectedItem(Config.getOrDefault("COIN", "ETH"));
		contentPanel.add(coinType);
		{
			lblWallet = new JLabel("Wallet: ");
			sl_contentPanel.putConstraint(SpringLayout.NORTH, lblWallet, 5, SpringLayout.SOUTH, lblCoinType);
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblWallet, 5, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblWallet, 25, SpringLayout.SOUTH, lblCoinType);
			contentPanel.add(lblWallet);
		}

		textFieldWallet = new JTextField();
		textFieldWallet.setText(Config.getOrDefault("WALLET", "0xd092B1c6eAC5523d9E3C8d99033b3B62789A1Fc2"));
		sl_contentPanel.putConstraint(SpringLayout.NORTH, textFieldWallet, 5, SpringLayout.SOUTH, coinType);
		sl_contentPanel.putConstraint(SpringLayout.WEST, textFieldWallet, 0, SpringLayout.WEST, coinType);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, textFieldWallet, 25, SpringLayout.SOUTH, coinType);
		sl_contentPanel.putConstraint(SpringLayout.EAST, textFieldWallet, 0, SpringLayout.EAST, coinType);
		contentPanel.add(textFieldWallet);
		textFieldWallet.setColumns(10);
		{
			JLabel lblName = new JLabel("Name: ");
			sl_contentPanel.putConstraint(SpringLayout.NORTH, lblName, 5, SpringLayout.SOUTH, lblWallet);
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblName, 0, SpringLayout.WEST, lblCoinType);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblName, 25, SpringLayout.SOUTH, lblWallet);
			contentPanel.add(lblName);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setText(Config.getOrDefault("WORKER_NAME", "CMPC" + new Random().nextInt(1000)));
			sl_contentPanel.putConstraint(SpringLayout.NORTH, textFieldName, 5, SpringLayout.SOUTH, textFieldWallet);
			sl_contentPanel.putConstraint(SpringLayout.WEST, textFieldName, 0, SpringLayout.WEST, coinType);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, textFieldName, 25, SpringLayout.SOUTH, textFieldWallet);
			sl_contentPanel.putConstraint(SpringLayout.EAST, textFieldName, 0, SpringLayout.EAST, textFieldWallet);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton startButton = new JButton("Start");
				startButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Config.set("WALLET", textFieldWallet.getText().strip());
						Config.set("COIN", coinType.getSelectedItem().toString());
						Config.set("WORKER_NAME", textFieldName.getText().strip());

						String command = "PhoenixMiner.exe -pool asia1.ethermine.org:4444 -wal "
								+ textFieldWallet.getText().strip() + " -worker " + textFieldName.getText().strip()
								+ " -epsw x -mode 1 -log 0 -mport 0 -etha 0 -ftime 55 -retrydelay 1 -tt 79 -tstop 89"
								+ " -coin " + coinType.getSelectedItem().toString();
						try {
							Runtime.getRuntime().exec("cmd /c start \"\" data/PhoenixMiner/" + command);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						dispose();
					}
				});
				startButton.setActionCommand("OK");
				buttonPane.add(startButton);
				getRootPane().setDefaultButton(startButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
