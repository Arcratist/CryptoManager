package BrettDanSmith.CryptoManager.Chat;


import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient{

    /**
     * @param args the command line arguments
     */
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static void main(String[] args) throws Exception  {
        Socket soc = new Socket("localhost",9081);
        Scanner sc = new Scanner(System.in);
        BufferedReader nis = new BufferedReader(
                                                    new InputStreamReader(
                                                           soc.getInputStream()
                                                    )
                                             );
        PrintWriter nos = new PrintWriter(
                                              new BufferedWriter(
                                                      new OutputStreamWriter(
                                                            soc.getOutputStream()
                                                      )
                                              ),true
                                         );
        JFrame frame = new JFrame("Get Username");
        String uname = new String();
        uname = JOptionPane.showInputDialog(frame, "Enter your Username :");
        if (uname == null) {
            uname = "Unknown";
        }
        JFrame f1 = new JFrame(uname);
        JButton b1 = new JButton("Send");
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        JTextField tf = new JTextField(20);
        JPanel p1 = new JPanel();
        p1.add(tf);
        p1.add(b1);
        f1.add(ta);
        f1.add(BorderLayout.SOUTH,p1);
        ChatListener l1 = new ChatListener(tf,nos,uname);
        b1.addActionListener(l1);
        tf.addActionListener(l1);
        f1.setSize(400,400);
        f1.setVisible(true);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String str = nis.readLine();
        while(!str.equals("End")){
            ta.append(str + "\n" );
            str = encrypt(str,3);
            str = nis.readLine();
        }
        ta.append("ChatClient Signing Off");
        Thread.sleep(1000);
        System.exit(0);
    }
     public static String encrypt(String plainText, int shiftKey)
    {
        plainText = plainText.toLowerCase();
        String cipherText = "";
        for (int i = 0; i < plainText.length(); i++)
        {
            int charPosition = ALPHABET.indexOf(plainText.charAt(i));
            int keyVal = (shiftKey + charPosition) % 26;
            char replaceVal = ALPHABET.charAt(keyVal);
            cipherText += replaceVal;
        }
        return cipherText;
    }
     public static String decrypt(String cipherText, int shiftKey)
    {
        cipherText = cipherText.toLowerCase();
        String plainText = "";
        for (int i = 0; i < cipherText.length(); i++)
        {
            int charPosition = ALPHABET.indexOf(cipherText.charAt(i));
            int keyVal = (charPosition - shiftKey) % 26;
            if (keyVal < 0)
            {
                keyVal = ALPHABET.length() + keyVal;
            }
            char replaceVal = ALPHABET.charAt(keyVal);
            plainText += replaceVal;
        }
        return plainText;
    }
    
}
class ChatListener implements ActionListener{
   JTextField tf ;
   PrintWriter nos;
   String uname;
    public ChatListener(JTextField tf,PrintWriter nos,String uname){
        this.tf = tf;
        this.nos = nos;
        this.uname = uname;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String str  = tf.getText();
        nos.println(uname+" : "+str);
        tf.setText("");
    }
    

}