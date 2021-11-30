package BrettDanSmith.CryptoManager;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class InfoDialog extends JDialog {
	private static final long serialVersionUID = 1175825476059716487L;
	
	private final JPanel contentPanel = new JPanel();

	public InfoDialog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(InfoDialog.class.getResource("/BrettDanSmith/CryptoManager/Info.png")));
		setResizable(false);
		setTitle("Info");
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("INFO");
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setBounds(10, 10, 415, 20);
			contentPanel.add(lblNewLabel);
		}

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				StringSelection stringSelection = new StringSelection("0x426a426f0c4115954b84e8f7b88d500161e650cf");
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setToolTipText("0x426a426f0c4115954b84e8f7b88d500161e650cf");
		lblNewLabel_1.setIcon(new ImageIcon(InfoDialog.class.getResource("/BrettDanSmith/CryptoManager/frame.png")));
		lblNewLabel_1.setBounds(10, 275, 125, 125);
		contentPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Made by Brett Smith (@BrettDanSmith)");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(10, 30, 415, 20);
		contentPanel.add(lblNewLabel_2);
	}

	public void setData(String string) {
	}

}
