import java.net.*;
import java.io.*;
import java.net.*;  
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
public class helper implements ActionListener
{  
    private Socket socket   = null;
	private ServerSocket server   = null;
    private DataInputStream in =  null;
	JButton[] buttons = new JButton[26];
	JFrame help;
	JLabel guesses,word ;
	JPanel p1,buttonPanel;
    public helper(int port)
    {
        help = new JFrame("Hangman");
		p1 = new JPanel();
		p1.setBackground(Color.PINK);
		p1.setLayout(null);
		help.add(p1);
		word = new JLabel();
		guesses = new JLabel();
		String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
	
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 4));
		buttonPanel.setBackground(Color.GRAY);
		help.add(buttonPanel, BorderLayout.SOUTH);
		
		for (int i = 0; i < buttons.length; i++) 
		{
			if(i==21)
			{
				JLabel d = new JLabel("");
				buttonPanel.add(d);
			}	
			buttons[i] = new JButton(letters[i]);
			buttons[i].setSize(40, 40);
			buttons[i].setActionCommand(letters[i]);
			buttons[i].addActionListener(this);
			buttons[i].setBackground(Color.CYAN);
			Border border = BorderFactory.createLineBorder(Color.BLACK);
			buttons[i].setBorder(border);
			buttonPanel.add(buttons[i]);		
		}
		
		word = new JLabel("Waiting for request",JLabel.CENTER);
		word.setBounds(50,50,700,100);
		word.setFont(new Font("Arial", Font.BOLD, 24));
		word.setForeground(Color.BLUE);
		p1.add(word);
		p1.add(guesses);
		p1.setLayout(null);
		p1.setVisible(true);
		help.getContentPane().setBackground(Color.PINK);
		help.setSize(800,500);
		help.setLayout(new GridLayout(2,0));
		help.setVisible(true);
		
		help.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try
        {
            server = new ServerSocket(port);
            socket = server.accept();
            System.out.println("Client has been accepted");
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String line = "";
			while (!line.equals("End"))
			{
				try
				{
                   line = in.readUTF();
            	   word.setText(line);
				}
				catch(IOException i)
				{
					System.out.println(i);
				}
			}
		    System.exit(0);			
		}  
		catch(Exception i)
        {
            System.out.println(i);
        }
		try
		{
			in.close();
			socket.close();         
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
    }
	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
		{
			try
			{
			DatagramSocket ds1 = new DatagramSocket();
			InetAddress ip1 = InetAddress.getByName("localhost");
			DatagramPacket dp1 = new DatagramPacket(command.getBytes(),command.length(), ip1, 5100);  
			ds1.send(dp1);
			ds1.close();
			}
			catch(Exception i)
			{
				i.printStackTrace();
			}	
		}	
	}
    public static void main(String args[])
    {
        helper rec1 = new helper(5000);
    }
}