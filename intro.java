import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
public class intro 
{
	JFrame intro_frame;
	JLabel title,name,regno,ack,logo;
	JButton startc,startp;
	intro()
	{
		intro_frame= new JFrame("Hangman");
		logo =new JLabel(new ImageIcon("D:\\JAVAPROGRAM\\CN project\\sastralogo.jpg"));
		logo.setBounds(10,10,450,150);
		intro_frame.add(logo);
		
		title = new JLabel("IMPLEMENTATION OF MULTI-PLAYER HANGMAN GAME USING UDP IN JAVA");
		title.setBounds(30,150,500,100);
		intro_frame.add(title);
		
		ack = new JLabel("Done by");
		intro_frame.add(ack);
		ack.setBounds(200,250,75,50);
		name = new JLabel("K NIRIKKSHA");
		name.setBounds(200,275,200,50);
		intro_frame.add(name);
		intro_frame.add(regno);
		
		startc = new JButton("Play with computer");
		//startc.addActionListener(this);
		startc.setBounds(150,350,200,50);
		intro_frame.add(startc);
		
		startp = new JButton("Play with human");
		//startp.addActionListener(this);
		startp.setBounds(150,450,200,50);
		intro_frame.add(startp);
			
		intro_frame.getContentPane().setBackground(Color.PINK);
		intro_frame.setSize(500,700);
		intro_frame.setLayout(null);
		intro_frame.setVisible(true);
		intro_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		startc.addActionListener(new ActionListener()
		{
		public void actionPerformed(ActionEvent e)
		{
			receiverudp game = new receiverudp('c');
			game.setVisible(true);
		}
		});
		startp.addActionListener(new ActionListener()
		{
		public void actionPerformed(ActionEvent e)
		{
			receiverudp game = new receiverudp('p');
			game.setVisible(true);
		}
		});
	}	

	public static void main(String args[])
	{
		intro i = new intro();
	}
}	