import java.net.*;  
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.io.*;
public class receiverudp extends JFrame implements ActionListener, MouseListener 
{
	public static int curr_score =0;
	public static String str;
	DatagramSocket dsh=null;
	DatagramPacket dph= null;
	private static final String START = "Play";
	private static final String EXIT = "Exit";
	public static final String REPLAY = "Play Again?";
	public static final String ADDHELP = "Add another player?";
	public static final String GUIDELINES = "How to play?";
	public int state = 0;
	public static String dresult;
	private static String dispclue;
	public static char word[];
	public static char clue[];
	public static char rec[];
	public static int wlen;
	public static int glen = 0;
	private static char[] Word;
	private static char[] guesses;
	public static int numBodyParts = 0;
	private static String numGuesses = "";
	public static String phrase;
	public static JPanel panel,playArea,buttonPanel,belowPanel,scoreArea;	
	public static boolean getnextword = true;
	public static int atmp =0;
	public static int countbutton =0;
	private char opponent;
	DatagramSocket ds;
	DatagramPacket dp ;
	byte[] buffer;				
	String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
	char[] alp=	{ 'a','b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z' };
	JButton[] buttons = new JButton[26];
	JLabel score = new JLabel("Score : 0",JLabel.RIGHT);
	
	public receiverudp(char player) 
	{
		super("Hangman");
		setSize(1000,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String str;
		
		opponent = player;
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));
		panel.setBackground(Color.PINK);

		playArea = new JPanel();
		playArea.setBackground(Color.PINK);
		playArea.setLayout(null);
		panel.add(playArea);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 4));
		buttonPanel.setBackground(Color.GRAY);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		score.setBounds(850,0,100,100);
		score.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		playArea.add(score);
		
		belowPanel = new JPanel();
		
		add(panel);
		add(belowPanel, BorderLayout.AFTER_LAST_LINE);
		belowPanel.setLayout(new GridLayout(0, 3));
		belowPanel.setVisible(true);
		
		frame_design();
	}	
	void frame_design()
	{
		//To create menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu file = new JMenu("File");
		menuBar.add(file);
		
		//Add menu items
		addItem(file, START);
		addItem(file, EXIT);
		
		//Help menu item
		JMenu Help = new JMenu("Help");
		addItem(Help, GUIDELINES);
		menuBar.add(Help);
		
		//To add buttons to the panel
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
		
		//playagain and exit button in the next panel
		JButton playAgain = new JButton(REPLAY);
		JButton dummybutton = new JButton("");
		playAgain.setSize(80, 80);
		playAgain.setActionCommand(REPLAY);
		playAgain.addActionListener(this);
		
		JButton addhelp = new JButton(ADDHELP);

		addhelp.setSize(80, 80);
		addhelp.setActionCommand(ADDHELP);
		addhelp.addActionListener(this);
		
		JButton exit = new JButton(EXIT);
		exit.setActionCommand(EXIT);
		exit.addActionListener(this);
		exit.setSize(80, 80);
		belowPanel.add(playAgain);
		belowPanel.add(addhelp);
		belowPanel.add(exit);
		
	}
	
	public void addItem(JMenu menu, String itemName) 
	{
		JMenuItem Item = new JMenuItem(itemName);
		Item.addActionListener(this);
		menu.add(Item);
	}
	
	public void paint(Graphics g) 
	{
		super.paint(g);
		Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 24);
		g.setFont(font);
		g.setColor(Color.BLUE);
		if (state== 1 || state ==3) 
		{	
			gameMessages(g);
			dresult = "";
			for (int i = 0; i < guesses.length; i++) 
			{
				dresult += guesses[i] + " ";	
			}
			g.drawString(dresult, 300, 175);
			g.drawString("GUESSES", 300, 300);
			g.drawString(dispclue,300,100);
			g.drawString(numGuesses, 300, 350);
			System.out.println(Word);
			drawStickMan(g);
		}
	}
	
	private void drawStickMan(Graphics g) 
	{
		if (numBodyParts >= 1) 
		{
			g.setColor(Color.BLACK);
			g.fillOval(735, 120, 70, 60);
			g.fillRect(700,80,100,10);
			g.fillRect(700,80,10,300);
			//g.fillRect(765,90,10,40);
		}
		if (numBodyParts >= 2) 
		{
			g.setColor(Color.BLACK);
			g.fillRect(760, 180, 20, 80);
		}
		if (numBodyParts >= 3) 
		{
			g.setColor(Color.BLACK);
			g.fillRect(725, 200, 45, 15);
			g.fillRect(715, 200, 10, 15);
		}
		if (numBodyParts >= 4) 
		{
			g.setColor(Color.BLACK);
			g.fillRect(780, 200, 55, 15);
		}
		if (numBodyParts >= 5) 
		{
			g.setColor(Color.BLACK);
			g.fillRect(735, 260, 30, 15);
		}
		if (numBodyParts >= 6) 
		{
			g.setColor(Color.BLACK);
			g.fillRect(770, 260, 30, 15);
			g.drawString("You Lost!!", 25, 80);
			g.drawString(str,300,195);
			g.fillRect(765,90,10,40);
			repaint();
			buttonPanel.setVisible(false);
			belowPanel.setVisible(true);
				
		}
	}
	
	private void gameMessages(Graphics g) 
	{
		if(winner() && state==3)
		{	
			try
			{
				System.out.println("Closing connection helper ");
				dsh.close();
				ds.close();
			}
			catch(Exception i)
			{
				System.out.println(i);
			}
			state =1;
		}
		if(!winner() && state ==3 && numBodyParts>=6)
		{	
			try
			{
//				JOptionPane.showMessageDialog(null,"Closing helper's connection");
				System.out.println("Closing connection helper ");
				dsh.close();
				ds.close();
			}
			catch(Exception i)
			{
				System.out.println(i);
			}
			state =1;
		}	
		if (!winner() && numBodyParts < 6) 
		{
			g.drawString("Let's Play ", 25, 80);
		} 
		else if (winner() && numBodyParts < 6) 
		{
			JOptionPane.showMessageDialog(null,"You saved me!!...");
			System.out.println("i hit here");
			g.drawString("You Won!!", 50, 80);
			curr_score+=10;
			String disp_score = new String("Score:"+Integer.toString(curr_score));
			score.setText(disp_score);
			System.out.println(disp_score);
			buttonPanel.setVisible(false);
			belowPanel.setVisible(true);
			getnextword = true;
		}
		else if(numBodyParts == 6) 
		{
			g.drawString("You Lost!!", 25, 80);
			numBodyParts = 0;
			numGuesses = "";
			buttonPanel.setVisible(true);
			state = 1;
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < Word.length; i++) 
			{
				if(i==0)
					result.append("Word is ");
			result.append(str.charAt(i));
			}
			
			String strdisp = new String(result);
			g.setColor(Color.BLACK);
			g.drawString(strdisp,245,250);
			g.setColor(Color.BLUE);
			
			for(int i=0;i<26;i++)
			{
				buttons[i].setBackground(Color.CYAN);
				Border border = BorderFactory.createLineBorder(Color.BLUE);
				buttons[i].setBorder(border);
			}
			buttonPanel.setVisible(false);
			belowPanel.setVisible(true);
				
		}
	}
	public String getword() 
	{
		try
		{
			
			DatagramSocket ds1 = new DatagramSocket();
			InetAddress ip1 = InetAddress.getByName("localhost");
			String req = ""+opponent;
			DatagramPacket dp1 = new DatagramPacket(req.getBytes(),req.length(), ip1, 3100);  
			ds1.send(dp1);
			ds1.close();
			
			DatagramSocket ds2 = new DatagramSocket(3000);  
			byte[] buf = new byte[1024];  
			DatagramPacket dp2 = new DatagramPacket(buf,1024);
			ds2.receive(dp2);  
			str = new String(dp2.getData(),0,dp2.getLength());
			ds2.close();
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
		return str;
	}	
	public boolean winner() 
	{
		if (Arrays.equals(guesses, Word)) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}
	public void computedresult()
	{
		dresult = "";
			for (int i = 0; i < guesses.length; i++) 
			{
				dresult += guesses[i] + " ";				
			}
		repaint();	
		if(winner())
		try
		{	
			InetAddress ipe = InetAddress.getByName("localhost");
			String lineend = "End";
			dph = new DatagramPacket(lineend.getBytes(),lineend.length(), ipe, 5000);
			dsh.send(dph);	
			ds.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
	    if (command.equals(START)) 
	    {
			state = 1;
			play();
			repaint();
		}
	    else if (command.length() == 1 && state !=2) 
		{
			letters(command);
		}
		else if(command.equals(REPLAY)) 
		{
			numBodyParts = 0;
			numGuesses = "";
			buttonPanel.setVisible(true);
			state = 1;
			play();
			repaint();
			for(int i=0;i<26;i++)
			{
				buttons[i].setBackground(Color.CYAN);
				Border border = BorderFactory.createLineBorder(Color.BLUE);
				buttons[i].setBorder(border);
			}	
		}	
		else if (command.equals(EXIT)) 
		{
			state = 2;
			System.exit(-1);
		}
		else if (command.equals(ADDHELP)) 
		{
			state =3;
			countbutton=1;
			atmp=1;
			System.out.println(state+ADDHELP);
			letters(" ");
			try
			{
				ds = new DatagramSocket(5100);  
				buffer = new byte[1024];  
				dp = new DatagramPacket(buffer,1024);
				/*DatagramSocket ds1 = new DatagramSocket();
				InetAddress ip1 = InetAddress.getByName("localhost");
				String req = "XX";
				DatagramPacket dp1 = new DatagramPacket(req.getBytes(),req.length(), ip1, 5100);  
				ds1.send(dp1);
				ds1.close();
				*/
				//System.out.println("Writing into socket");
				dsh = new DatagramSocket();
				InetAddress iph = InetAddress.getByName("localhost");
				String line = "Word:"+dresult+"Guesses:"+numGuesses;
				DatagramPacket dph = new DatagramPacket(line.getBytes(),line.length(), iph, 5000);  
				dsh.send(dph);
				repaint();
				computedresult();
				System.out.println(line);
				
			}
			catch(Exception u)
			{
				System.out.println(u);
			}
		}	
		else if (command.equals(GUIDELINES))
		{
			JFrame playguide = new JFrame("How to play?");
			String d = "It\'s a word guessing game. Initially a word will be picked up and the \ndash will be generated depending on the word’s length.\nThe word has to be guessed letter by letter. If a correct letter is \nguessed then the place at which the letter occurs is filled up and the button \nturns to green. Else if the letter guessed is not present in the word,\nthen a part of stickman would be displayed and number of chances left is \ndecreased and the button turns to red. Each incorrect guess increases\nthe probability of being hanged.\n\nFor more details Contact us at:\nHM office,\n789,North Bazaar,\nThanjavur - 613401";
			final JTextArea instruction = new JTextArea(d);
			Font font = new Font("Times new roman", Font.ITALIC, 24);
			instruction.setFont(font);
			playguide.add(instruction);
			playguide.setSize(700,600);
			playguide.setVisible(true);
		}		
	}
	public void letters(String command) 
	{
		int index=0;
		System.out.println(command);
		for(int j=0;j<26;j++)
		{	
			if (command.toLowerCase().charAt(0) == alp[j] ) 
			{
				index=j;
				break;
			}
		}
		if (phrase.contains(command.toLowerCase())) 
		{
			for (int i = 0; i < Word.length; i++) 
			{
				if (command.toLowerCase().charAt(0) == Word[i]) 
				{
					guesses[i] = command.toLowerCase().charAt(0);
					buttons[index].setBackground(Color.GREEN);
				}
			}
		}
		else if(command == " ")
		{}	
		else if (!phrase.contains(command.toLowerCase())) 
		{
			JOptionPane.showMessageDialog(null, "Sorry " + command+ " is not part of the word");
			numBodyParts++;
			buttons[index].setBackground(Color.RED);
		}
		numGuesses += command;
		if (numBodyParts < 6 && !winner()) 
		{
			numGuesses += ",";
		}
		repaint();
		if(state ==3)	
		{
				computedresult();
				String line = "Word:"+dresult+"Guesses:"+numGuesses;
				try
				{
					System.out.println("Writing into socket");
					dsh = new DatagramSocket();
					InetAddress iph = InetAddress.getByName("localhost");
					DatagramPacket dph = new DatagramPacket(line.getBytes(),line.length(), iph, 5000);  
					dsh.send(dph);
					System.out.println(line);
					atmp++;
				}
				catch(Exception i)
				{
					System.out.println(i);
				}
			countbutton+=1;	
			//System.out.println("atmp ="+atmp);
			if(atmp >0 && countbutton%2==1 && numBodyParts<=6)	
			{	
				System.out.println("Checking helper");
			//if(atmp==1)
			//System.out.println("Atmp = 1");
				checkhelper();	
			}
		}	
		repaint();
	}
	public void checkhelper()
	{
		try
		{


			ds.receive(dp);  
			String let = new String(dp.getData(),0,dp.getLength());
			System.out.println(let);
			if((atmp==2||atmp==3) && let.length()!=1)
			{
				ds.close();
				JOptionPane.showMessageDialog(null,"Helper not available");
				state=1;
				return;
			}	
			else
			{
				JOptionPane.showMessageDialog(null,"Helper has sent "+let);
				letters(let);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
	private void play() 
	{
		phrase = getword();
		rec = phrase.toCharArray();
		for(int i=0;i<rec.length;i++)
		{
			if(rec[i] ==',')
				wlen =i;
		}	
		Word = new char[wlen];
		clue = new char[rec.length-wlen-1];
		System.out.println(wlen);
		for(int i=0;i<wlen;i++)
		{
			Word[i]=rec[i];
		}	
		for(int j=0,i=wlen+1;i<rec.length;j++,i++)
		{
			clue[j]=rec[i];
		}
		guesses = new char[Word.length];
		for (int i = 0; i < guesses.length; i++) 
		{
			if(Word[i] !=' ')
				guesses[i] = '_';
			else
				guesses[i] = ' ';
		}
		dispclue = new String(clue);
		phrase = new String(Word);
	}		
	public void mouseClicked(MouseEvent e) 
	{}
	public void mouseEntered(MouseEvent e) 
	{}
	public void mouseExited(MouseEvent e) 
	{}
	public void mousePressed(MouseEvent e) 
	{}
	public void mouseReleased(MouseEvent e) 
	{}	
	public static void main(String args[])
	{
		receiverudp game = new receiverudp();
		game.setVisible(true);
	}
}
