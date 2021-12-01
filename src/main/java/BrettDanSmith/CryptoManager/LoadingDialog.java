package BrettDanSmith.CryptoManager;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;

public class LoadingDialog extends JDialog {
	private static final long serialVersionUID = -8703270036365780992L;
	
	
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public LoadingDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setUndecorated(true);
		setBounds(100, 100, 450, 150);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBounds(5, 125, 440, 20);
		progressBar.setStringPainted(true);
		contentPanel.add(progressBar);
		progressBar.setString("Loading please wait...");
		
		JLabel lblNewLabel = new JLabel("Crypto Manager v0.02a By @BrettDanSmith");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(5, 100, 440, 20);
		contentPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(LoadingDialog.class.getResource("/BrettDanSmith/CryptoManager/icon.png")));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(0, 0, 450, 115);
		contentPanel.add(lblNewLabel_2);
	}
}
