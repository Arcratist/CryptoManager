package BrettDanSmith.CryptoManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;

public class OptionsWindow extends JFrame {
	private static final long serialVersionUID = 7797674010693776442L;
	private JPanel contentPane;
	public static OptionsWindow INSTANCE;
	private JTextField textField;
	private JTextField textField_1;

	public OptionsWindow(CryptoManagerWindow cryptoManagerWindow) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(OptionsWindow.class.getResource("/BrettDanSmith/CryptoManager/options.png")));
		setAlwaysOnTop(true);
		setTitle("Crypto Manager - Options");
		setResizable(false);
		INSTANCE = this;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 612, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		JLabel lblNewLabel = new JLabel("Theme: ");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel, 5, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel, 5, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel, 25, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel, 55, SpringLayout.WEST, contentPane);
		contentPane.add(lblNewLabel);

		JSpinner spinner = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.WEST, spinner, 10, SpringLayout.EAST, lblNewLabel);
		spinner.setModel(new SpinnerListModel(new String[] { "dark", "light" }));
		sl_contentPane.putConstraint(SpringLayout.NORTH, spinner, 5, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, spinner, 25, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, spinner, -5, SpringLayout.EAST, contentPane);
		contentPane.add(spinner);
		spinner.setValue(Config.getOrDefault("LAF", "dark"));

		JButton btnNewButton = new JButton("Save and Close");

		btnNewButton.setMnemonic('S');
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton, -25, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton, -5, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton, -5, SpringLayout.EAST, contentPane);
		contentPane.add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("Home URL: ");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 5, SpringLayout.SOUTH, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_1, 0, SpringLayout.WEST, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_1, 25, SpringLayout.SOUTH, lblNewLabel);
		contentPane.add(lblNewLabel_1);

		textField = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, textField, 5, SpringLayout.SOUTH, spinner);
		sl_contentPane.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, spinner);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textField, 25, SpringLayout.SOUTH, spinner);
		sl_contentPane.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, spinner);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.setText(Config.getOrDefault("HOME_URL", "https://google.com.au/"));
		
		JLabel lblNewLabel_2 = new JLabel("Wallet ADD:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_2, 5, SpringLayout.SOUTH, lblNewLabel_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_2, 0, SpringLayout.WEST, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_2, 25, SpringLayout.SOUTH, lblNewLabel_1);
		contentPane.add(lblNewLabel_2);
		
		textField_1 = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, textField_1, 5, SpringLayout.SOUTH, textField);
		sl_contentPane.putConstraint(SpringLayout.WEST, textField_1, 0, SpringLayout.WEST, spinner);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textField_1, 25, SpringLayout.SOUTH, textField);
		sl_contentPane.putConstraint(SpringLayout.EAST, textField_1, 0, SpringLayout.EAST, spinner);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		textField_1.setText(Config.getOrDefault("WALLET", "d092B1c6eAC5523d9E3C8d99033b3B62789A1Fc2"));

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Config.set("LAF", spinner.getValue().toString());
				Config.set("HOME_URL", textField.getText().strip());
				Config.set("WALLET", textField_1.getText().strip());
				Config.save();
				setVisible(false);
			}
		});
	}

	public static void Open() {
		INSTANCE.setVisible(true);
	}
}
