import java.net.*; 
import java.io.*;
import java.util.*;
import java.lang.Math; 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Sender
{  
	static int index;
	static String fdata="",strReq="";
	static String c="",w=""; 
	static void getvalues()
	{
		JFrame opponent = new JFrame("Opponent");
		JTextField word = new JTextField();
		JTextField clue = new JTextField();
		JButton submit = new JButton("Submit");
		JLabel ask_word = new JLabel("Enter a word to guess");
		JLabel ask_clue = new JLabel ("Enter a valid clue for given word");
		ask_word.setBounds(100,50,200,100);
		opponent.add(ask_word);
		word.setBounds(400,50,200,100);
		opponent.add(word);
		ask_clue.setBounds(100,200,200,100);
		opponent.add(ask_clue);
		clue.setBounds(400,200,200,100);
		opponent.add(clue);
		submit.setBounds(300,500,100,100);
		opponent.add(submit);
		
		submit.addActionListener(new ActionListener()
		{
		public void actionPerformed(ActionEvent e)
		{
		w = word.getText();
		c= clue.getText();
		if(w!="" && c!="")
		{
			fdata = w+","+c;
			System.out.println(w);
			System.out.println(c);
			System.out.println(fdata);
		}
		else
			JOptionPane.showMessageDialog(null,"Enter valid word and clue");	
		opponent.setVisible(false);
		}
		});
	
		opponent.setSize(700,700);
		opponent.setLayout(null);
		opponent.setVisible(true);
	}
	public static void main(String[] args) throws Exception 
	{    
		
		final int max=185;
		while(fdata!="bye")
		{
			try 
			{		
			DatagramSocket ds1 = new DatagramSocket(3100);  
			byte[] buf = new byte[1024];  
			DatagramPacket dp1 = new DatagramPacket(buf,1024);
			ds1.receive(dp1);  
			strReq = new String(dp1.getData(),0,dp1.getLength());
			ds1.close();
			DatagramSocket ds = new DatagramSocket();
			System.out.println(strReq);
			if(strReq.equals("c"))
			{
				File Obj = new File("word.txt");
				Random random = new Random();   
				int lno = random.nextInt(max+1);
				int thisline=-1;
				Scanner Reader1 = new Scanner(Obj);
				while (Reader1.hasNextLine()) 
				{
					String data = Reader1.nextLine();
					thisline+=1;
					if(thisline == lno)
						fdata = data;
				}
			}
			if(strReq.equals("p"))
			{
				getvalues();
			}
			
			InetAddress ip = InetAddress.getByName("localhost");  
			DatagramPacket dp = new DatagramPacket(fdata.getBytes(), fdata.length(), ip, 3000);  
			ds.send(dp);
			ds.close();		
		}
        catch (FileNotFoundException e) 
		{
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
	}
	}
}    