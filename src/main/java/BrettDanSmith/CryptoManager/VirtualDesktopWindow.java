package BrettDanSmith.CryptoManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import net.sourceforge.jdatepicker.JDatePanel;

public class VirtualDesktopWindow extends JFrame {
	private static final long serialVersionUID = -7993439526214392128L;
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

	private JPanel contentPane;

	private JDesktopPane desktopPane;

	private JInternalFrame startMenuFrame;
	private JTextField textField;

	private Box horizontalBox;
	private JTextField messengerFrame_MessageInputField;
	private JTextPane messengerFrame_textPane;
	private JInternalFrame messengerFrame;
	private JInternalFrame calendarFrame;

	/**
	 * Create the frame.
	 */
	public VirtualDesktopWindow() {
		setTitle("Virtual Desktop");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1280, 720);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		setLocationRelativeTo(null);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		JMenuItem mntmFile_Exit = new JMenuItem("Exit");
		mntmFile_Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mnFile.add(mntmFile_Exit);
		contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		URL iconURL = getClass().getResource("/BrettDanSmith/CryptoManager/bg.png");
		ImageIcon bgImage = new ImageIcon(iconURL);
		desktopPane = new JDesktopPane() {
			private static final long serialVersionUID = 9103410978359166994L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
		desktopPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (startMenuFrame != null)
					startMenuFrame.setVisible(false);

				if (calendarFrame != null)
					calendarFrame.setVisible(false);
			}
		});
		desktopPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (startMenuFrame != null)
					startMenuFrame.setVisible(false);

				if (calendarFrame != null)
					calendarFrame.setVisible(false);
			}
		});
		desktopPane.setDesktopManager(new DefaultDesktopManager() {
			private static final long serialVersionUID = 7352810128968165918L;

			@Override
			public void iconifyFrame(JInternalFrame f) {
				System.out.println("window");
				JButton button = new JButton(f.getTitle(), f.getFrameIcon()) {
					private static final long serialVersionUID = -8085985943218951348L;
				};
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							f.setIcon(false);
						} catch (PropertyVetoException e1) {
							e1.printStackTrace();
						}
						button.setVisible(false);
						horizontalBox.remove(button);
						horizontalBox.revalidate();
						f.setVisible(true);
						contentPane.revalidate();
					}
				});
				horizontalBox.add(button);
				f.setVisible(false);
			}

			@Override
			public void activateFrame(JInternalFrame f) {
				if (f.equals(startMenuFrame))
					startMenuFrame.setLocation(0, desktopPane.getSize().height - 450);
				else if (f.equals(calendarFrame))
					calendarFrame.setLocation(desktopPane.getSize().width - 250, desktopPane.getSize().height - 250);

				System.out.println(calendarFrame.getLocation().toString());

				super.activateFrame(f);
			}

			@Override
			public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
				boolean didResize = (f.getWidth() != newWidth || f.getHeight() != newHeight);
				if (!inBounds((JInternalFrame) f, newX, newY, newWidth, newHeight)) {
					Container parent = f.getParent();
					Dimension parentSize = parent.getSize();
					int boundedX = (int) Math.min(Math.max(0, newX), parentSize.getWidth() - newWidth);
					int boundedY = (int) Math.min(Math.max(0, newY), parentSize.getHeight() - newHeight);
					f.setBounds(boundedX, boundedY, newWidth, newHeight);
				} else {
					f.setBounds(newX, newY, newWidth, newHeight);
				}
				if (didResize) {
					f.validate();
				}
			}

			protected boolean inBounds(JInternalFrame f, int newX, int newY, int newWidth, int newHeight) {
				if (newX < 0 || newY < 0)
					return false;
				if (newX + newWidth > f.getDesktopPane().getWidth())
					return false;
				if (newY + newHeight > f.getDesktopPane().getHeight())
					return false;
				return true;
			}

			@Override
			public void dragFrame(JComponent component, int x, int y) {
				if (component instanceof JInternalFrame) {
					if (component.equals(startMenuFrame))
						return;
					if (component.equals(calendarFrame))
						return;
				}
				super.dragFrame(component, x, y);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, desktopPane, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, desktopPane, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, desktopPane, -36, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, desktopPane, 0, SpringLayout.EAST, contentPane);
		contentPane.add(desktopPane);

		startMenuFrame = new JInternalFrame("-------------------------- START MENU --------------------------");
		startMenuFrame.setLocation(0, 173);
		desktopPane.setLayer(startMenuFrame, 100, 000);
		startMenuFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		startMenuFrame.setBorder(null);
		startMenuFrame.setFrameIcon(null);
		startMenuFrame.setSize(350, 450);
		desktopPane.add(startMenuFrame);
		SpringLayout springLayout = new SpringLayout();
		startMenuFrame.getContentPane().setLayout(springLayout);

		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, -25, SpringLayout.SOUTH,
				startMenuFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, startMenuFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, textField, 0, SpringLayout.SOUTH,
				startMenuFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textField, -25, SpringLayout.EAST,
				startMenuFrame.getContentPane());
		startMenuFrame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnNewButton_1 = new JButton("");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton_1, 0, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_1, 0, SpringLayout.EAST, textField);
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_1, 0, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton_1, 0, SpringLayout.EAST,
				startMenuFrame.getContentPane());
		startMenuFrame.getContentPane().add(btnNewButton_1);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.7);
		springLayout.putConstraint(SpringLayout.NORTH, splitPane, 0, SpringLayout.NORTH,
				startMenuFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, splitPane, 0, SpringLayout.WEST, startMenuFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, splitPane, 0, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.EAST, splitPane, 0, SpringLayout.EAST, startMenuFrame.getContentPane());
		startMenuFrame.getContentPane().add(splitPane);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setMinimumSize(new Dimension((int) (350f * 0.7f), 0));
		splitPane.setLeftComponent(scrollPane);
		
		JLabel lblNewLabel_1 = new JLabel("Recent Applications/Files");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(lblNewLabel_1);

		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		JSeparator separator = new JSeparator();
		sl_panel.putConstraint(SpringLayout.NORTH, separator, 224, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, separator, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, separator, 226, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, separator, 0, SpringLayout.EAST, panel);
		panel.add(separator);

		Box horizontalBox_1 = Box.createHorizontalBox();
		sl_panel.putConstraint(SpringLayout.NORTH, horizontalBox_1, -36, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, horizontalBox_1, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, horizontalBox_1, 0, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, horizontalBox_1, 0, SpringLayout.EAST, panel);
		panel.add(horizontalBox_1);
		horizontalBox_1.setBorder(null);

		JButton btnNewButton_2 = new JButton("X");
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton_2.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnNewButton_2.setMinimumSize(new Dimension(30, 30));
		horizontalBox_1.add(btnNewButton_2);

		JButton btnNewButton = new JButton("X");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startMenuFrame.setLocation(0, desktopPane.getSize().height - 450);
				startMenuFrame.setVisible(!startMenuFrame.isVisible());
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton, 0, SpringLayout.SOUTH, desktopPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton, 0, SpringLayout.WEST, desktopPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton, 36, SpringLayout.WEST, contentPane);
		contentPane.add(btnNewButton);

		horizontalBox = Box.createHorizontalBox();
		sl_contentPane.putConstraint(SpringLayout.NORTH, horizontalBox, 0, SpringLayout.SOUTH, desktopPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, horizontalBox, 5, SpringLayout.EAST, btnNewButton);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, horizontalBox, 0, SpringLayout.SOUTH, contentPane);
		horizontalBox.setBorder(null);

		JInternalFrame desktopIcon_0 = new JInternalFrame("Files");
		desktopIcon_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				App.exit(0);
			}
		});
		desktopIcon_0.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		desktopPane.setLayer(desktopIcon_0, -1);
		desktopIcon_0.setFrameIcon(null);
		desktopIcon_0.setBorder(null);
		desktopIcon_0.setBounds(68, 10, 48, 64);
		desktopIcon_0.setVisible(true);
		desktopIcon_0.setOpaque(false);
		desktopIcon_0.setBackground(new Color(0, 0, 0, 0));
		desktopPane.add(desktopIcon_0);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(
				new ImageIcon(VirtualDesktopWindow.class.getResource("/BrettDanSmith/CryptoManager/folder.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		desktopIcon_0.getContentPane().add(lblNewLabel, BorderLayout.CENTER);
		{
			JInternalFrame desktopIcon_1 = new JInternalFrame(" Chat ");
			desktopIcon_1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					messengerFrame.setVisible(true);
				}
			});
			desktopIcon_1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			desktopPane.setLayer(desktopIcon_1, -1);
			desktopIcon_1.setFrameIcon(null);
			desktopIcon_1.setBorder(null);
			desktopIcon_1.setBounds(10, 10, 48, 64);
			desktopIcon_1.setVisible(true);
			desktopIcon_1.setOpaque(false);
			desktopIcon_1.setBackground(new Color(0, 0, 0, 0));
			desktopPane.add(desktopIcon_1);

			JLabel lblNewLabel1 = new JLabel("");
			lblNewLabel1.setIcon(
					new ImageIcon(VirtualDesktopWindow.class.getResource("/BrettDanSmith/CryptoManager/chat.png")));
			lblNewLabel1.setHorizontalAlignment(SwingConstants.CENTER);
			desktopIcon_1.getContentPane().add(lblNewLabel1, BorderLayout.CENTER);
		}

		messengerFrame = new JInternalFrame("Messenger");
		messengerFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		messengerFrame.setFrameIcon(new ImageIcon(
				new ImageIcon(VirtualDesktopWindow.class.getResource("/BrettDanSmith/CryptoManager/chat.png"))
						.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		messengerFrame.setClosable(true);
		messengerFrame.setIconifiable(true);
		messengerFrame.setMaximizable(true);
		messengerFrame.setResizable(true);
		messengerFrame.setBounds(383, 28, 462, 455);
		desktopPane.add(messengerFrame);
		SpringLayout springLayout_1 = new SpringLayout();
		messengerFrame.getContentPane().setLayout(springLayout_1);

		messengerFrame_MessageInputField = new JTextField();
		messengerFrame_MessageInputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sendMessage(messengerFrame_MessageInputField.getText().strip())) {
					messengerFrame_MessageInputField.setText("");
				}
			}
		});
		springLayout_1.putConstraint(SpringLayout.NORTH, messengerFrame_MessageInputField, -25, SpringLayout.SOUTH,
				messengerFrame.getContentPane());
		springLayout_1.putConstraint(SpringLayout.WEST, messengerFrame_MessageInputField, 0, SpringLayout.WEST,
				messengerFrame.getContentPane());
		springLayout_1.putConstraint(SpringLayout.SOUTH, messengerFrame_MessageInputField, 0, SpringLayout.SOUTH,
				messengerFrame.getContentPane());
		messengerFrame.getContentPane().add(messengerFrame_MessageInputField);
		messengerFrame_MessageInputField.setColumns(20);

		JButton messengerFrame_SendButton = new JButton("Send");
		messengerFrame_SendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sendMessage(messengerFrame_MessageInputField.getText().strip())) {
					messengerFrame_MessageInputField.setText("");
				}
			}
		});
		springLayout_1.putConstraint(SpringLayout.NORTH, messengerFrame_SendButton, 0, SpringLayout.NORTH,
				messengerFrame_MessageInputField);
		springLayout_1.putConstraint(SpringLayout.EAST, messengerFrame_MessageInputField, 0, SpringLayout.WEST,
				messengerFrame_SendButton);
		springLayout_1.putConstraint(SpringLayout.SOUTH, messengerFrame_SendButton, 0, SpringLayout.SOUTH,
				messengerFrame_MessageInputField);
		springLayout_1.putConstraint(SpringLayout.EAST, messengerFrame_SendButton, 0, SpringLayout.EAST,
				messengerFrame.getContentPane());
		messengerFrame.getContentPane().add(messengerFrame_SendButton);

		JScrollPane messengerFrame_scrollPane = new JScrollPane();
		springLayout_1.putConstraint(SpringLayout.SOUTH, messengerFrame_scrollPane, 0, SpringLayout.NORTH,
				messengerFrame_MessageInputField);
		springLayout_1.putConstraint(SpringLayout.EAST, messengerFrame_scrollPane, 0, SpringLayout.EAST,
				messengerFrame.getContentPane());
		messengerFrame_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		springLayout_1.putConstraint(SpringLayout.NORTH, messengerFrame_scrollPane, 0, SpringLayout.NORTH,
				messengerFrame.getContentPane());
		springLayout_1.putConstraint(SpringLayout.WEST, messengerFrame_scrollPane, 0, SpringLayout.WEST,
				messengerFrame.getContentPane());
		messengerFrame.getContentPane().add(messengerFrame_scrollPane);

		messengerFrame_textPane = new JTextPane();
		messengerFrame_textPane.setEditable(false);
		messengerFrame_scrollPane.setViewportView(messengerFrame_textPane);

		JMenuBar messengerFrame_menuBar = new JMenuBar();
		messengerFrame.setJMenuBar(messengerFrame_menuBar);

		JMenu messengerFrame_menuBar_File = new JMenu("File");
		messengerFrame_menuBar.add(messengerFrame_menuBar_File);

		JMenuItem messengerFrame_menuBar_File_Exit = new JMenuItem("Exit");
		messengerFrame_menuBar_File_Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messengerFrame.dispose();
			}
		});

		JMenuItem messengerFrame_menuBar_File_Connect = new JMenuItem("Connect");
		messengerFrame_menuBar_File_Connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		messengerFrame_menuBar_File.add(messengerFrame_menuBar_File_Connect);
		messengerFrame_menuBar_File.add(messengerFrame_menuBar_File_Exit);

		JMenu messengerFrame_menuBar_Profile = new JMenu("Profile");
		messengerFrame_menuBar.add(messengerFrame_menuBar_Profile);

		JMenuItem messengerFrame_menuBar_Profile_SetName = new JMenuItem("Set Name");
		messengerFrame_menuBar_Profile_SetName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(messengerFrame, "Enter Username: ",
						Config.getOrDefault("CHAT_NAME", "Unknown"));
				Config.set("CHAT_NAME", s);
				Config.save();
			}
		});
		messengerFrame_menuBar_Profile.add(messengerFrame_menuBar_Profile_SetName);

		JMenu messengerFrame_menuBar_Chat = new JMenu("Chat");
		messengerFrame_menuBar.add(messengerFrame_menuBar_Chat);

		JMenuItem messengerFrame_menuBar_Chat_ClearChat = new JMenuItem("Clear Chat");
		messengerFrame_menuBar_Chat_ClearChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messengerFrame_textPane.setText("");
			}
		});
		messengerFrame_menuBar_Chat.add(messengerFrame_menuBar_Chat_ClearChat);
		messengerFrame.setVisible(false);
		contentPane.add(horizontalBox);

		JLabel lblTime = new JLabel("00:00:00 AM");
		lblTime.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				calendarFrame.setVisible(!calendarFrame.isVisible());
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblTime, 0, SpringLayout.SOUTH, desktopPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblTime, -80, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblTime, -18, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblTime, 0, SpringLayout.EAST, contentPane);
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTime);

		JLabel lblDate = new JLabel("00/00/0000");
		lblDate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				calendarFrame.setVisible(!calendarFrame.isVisible());
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDate, 0, SpringLayout.SOUTH, lblTime);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDate, 0, SpringLayout.WEST, lblTime);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblDate, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblDate, 0, SpringLayout.EAST, contentPane);
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblDate);

		DateTimeFormatter dtf0 = DateTimeFormatter.ofPattern("HH:mm:ss");
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();

		lblTime.setText(dtf0.format(now));
		lblDate.setText(dtf1.format(now));

		JToggleButton btnMuteSound = new JToggleButton("");
		sl_contentPane.putConstraint(SpringLayout.EAST, horizontalBox, -6, SpringLayout.WEST, btnMuteSound);
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnMuteSound, 6, SpringLayout.SOUTH, desktopPane);

		calendarFrame = new JInternalFrame("Calendar");
		calendarFrame.setLocation(861, 349);
		desktopPane.setLayer(calendarFrame, 99);
		calendarFrame.setBorder(null);
		calendarFrame.setFrameIcon(null);
		JDatePanel datePanel = new JDatePanel();
		calendarFrame.getContentPane().add(datePanel, BorderLayout.CENTER);
		desktopPane.add(calendarFrame);
		calendarFrame.setSize(250, 250);

		sl_contentPane.putConstraint(SpringLayout.WEST, btnMuteSound, -29, SpringLayout.WEST, lblTime);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnMuteSound, -6, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnMuteSound, -5, SpringLayout.WEST, lblTime);
		contentPane.add(btnMuteSound);
	}

	protected boolean sendMessage(String string) {
		if (string.strip().isBlank())
			return false;
		messengerFrame_textPane.setText(messengerFrame_textPane.getText() + string + "\n");

		return true;
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

class ChatListener implements ActionListener {
	JTextField tf;
	PrintWriter nos;
	String uname;

	public ChatListener(JTextField tf, PrintWriter nos, String uname) {
		this.tf = tf;
		this.nos = nos;
		this.uname = uname;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String str = tf.getText();
		nos.println(uname + " : " + str);
		tf.setText("");
	}

}
