package BrettDanSmith.CryptoManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.net.URL;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class VirtualDesktopWindow extends JFrame {
	private static final long serialVersionUID = -7993439526214392128L;

	private JPanel contentPane;

	private JDesktopPane desktopPane;

	private JInternalFrame startMenuFrame;
	private JTextField textField;

	private Box horizontalBox;

	private JInternalFrame internalFrame_1;

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
		desktopPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (startMenuFrame != null)
					startMenuFrame.setVisible(false);
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
				f.setLocation(0, f.getDesktopPane().getSize().height - 450);
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
				if (component instanceof JInternalFrame)
					if (((JInternalFrame) component).getTitle().equals("   "))
						return;
				super.dragFrame(component, x, y);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, desktopPane, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, desktopPane, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, desktopPane, -36, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, desktopPane, 0, SpringLayout.EAST, contentPane);
		contentPane.add(desktopPane);

		startMenuFrame = new JInternalFrame("   ");
		desktopPane.setLayer(startMenuFrame, 10, 000);
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
		splitPane.setResizeWeight(0.8);
		springLayout.putConstraint(SpringLayout.NORTH, splitPane, 0, SpringLayout.NORTH,
				startMenuFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, splitPane, 0, SpringLayout.WEST, startMenuFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, splitPane, 0, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.EAST, splitPane, 0, SpringLayout.EAST, startMenuFrame.getContentPane());
		startMenuFrame.getContentPane().add(splitPane);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

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
		sl_contentPane.putConstraint(SpringLayout.WEST, horizontalBox, 5, SpringLayout.EAST, btnNewButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, horizontalBox, -5, SpringLayout.EAST, contentPane);
		horizontalBox.setBorder(null);
		sl_contentPane.putConstraint(SpringLayout.NORTH, horizontalBox, 0, SpringLayout.SOUTH, desktopPane);

		JInternalFrame internalFrame = new JInternalFrame("Test");
		internalFrame.setClosable(true);
		internalFrame.setIconifiable(true);
		internalFrame.setResizable(true);
		internalFrame.setMaximizable(true);
		internalFrame.setBounds(105, 0, 400, 400);
		desktopPane.add(internalFrame);

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
		desktopIcon_0.setBounds(886, 166, 48, 64);
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
		JInternalFrame desktopIcon_1 = new JInternalFrame("Chat");
		desktopIcon_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				internalFrame_1.setVisible(true);
			}
		});
		desktopIcon_1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		desktopPane.setLayer(desktopIcon_1, -1);
		desktopIcon_1.setFrameIcon(null);
		desktopIcon_1.setBorder(null);
		desktopIcon_1.setBounds(780, 166, 48, 64);
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
		
		internalFrame_1 = new JInternalFrame("Chat");
		internalFrame_1.setClosable(true);
		internalFrame_1.setIconifiable(true);
		internalFrame_1.setResizable(true);
		internalFrame_1.setMaximizable(true);
		internalFrame_1.setFrameIcon(new ImageIcon(VirtualDesktopWindow.class.getResource("/BrettDanSmith/CryptoManager/chat.png")));
		internalFrame_1.setBounds(629, 282, 189, 122);
		desktopPane.add(internalFrame_1);
		internalFrame.setVisible(true);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, horizontalBox, 0, SpringLayout.SOUTH, contentPane);
		contentPane.add(horizontalBox);
	}
}
