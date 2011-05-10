package scrabble.gui;

import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class StartGame {
	private StartGameFrame startFrame;
	
	public StartGame()
	{
		startFrame = new StartGameFrame();
	}
}

class StartGameFrame extends JFrame{
	private static int WIDTH = 640;
	private static int HEIGHT = 480;
	private static int CELL_WIDTH = 20;
	private static int CELL_HEIGHT = 5;
	private PlayerNamePanel playerNamePanel;
	private HostPanel hostPanel;
	private JButton joinGameButton,
					createGameButton,
					cancelButton,
					showGameButton;
	
	public StartGameFrame()
	{
		setTitle("Scrabble game");
		setSize(WIDTH, HEIGHT);
		setLocation(100, 100);
		//setResizable(false);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		addPlayerNameBox();
		addHostBox();
		addJoinGameButton();
		addCancelButton();
		addCreateGameButton();
		addShowGameButton();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pack();
		setVisible(true);	
	}
	
	private void addPlayerNameBox()
	{
		playerNamePanel = new PlayerNamePanel();
		add(playerNamePanel, 
				new GBC(1, 0, 2, 2).setAnchor(GBC.FIRST_LINE_START).setInsets(20, 10, 0, 10));
	}
	
	private void addHostBox()
	{
		JLabel hostLabel = new JLabel("Games");
		hostLabel.setFont(new Font("Serif", Font.BOLD, 18));
		add(hostLabel, new GBC(1, 2, 2, 5).setAnchor(GBC.FIRST_LINE_START).setInsets(20, 20, 20, 10));
		hostPanel = new HostPanel();
		add(hostPanel, 
				new GBC(1, 3, 2, 5).setAnchor(GBC.FIRST_LINE_START).setWeight(CELL_WIDTH, CELL_HEIGHT).setInsets(20, 15, 30, 10).setFill(GBC.BOTH));
	}
	
	private JButton newButton(String name)
	{
		JButton b = new JButton(name);
		b.setPreferredSize(new Dimension(150, 30));
		return b;
	}
	
	private void addJoinGameButton()
	{
		joinGameButton = newButton("Join game");
		add(joinGameButton, 
			new GBC(3, 1, 1, 1).setAnchor(GBC.CENTER).setInsets(10, 10, 5, 10));
	}
	
	private void addCancelButton()
	{
		cancelButton = newButton("Cancel");
		add(cancelButton, 
			new GBC(3, 2, 1, 1).setAnchor(GBC.CENTER));
	}
	
	private void addCreateGameButton()
	{
		createGameButton = newButton("Create game");
		add(createGameButton, 
			new GBC(2, 8, 1, 1).setAnchor(GBC.CENTER).setInsets(10, 20, 20, 10));
	}
	
	private void addShowGameButton()
	{
		showGameButton = newButton("Show games");
		add(showGameButton, 
			new GBC(1, 8, 1, 1).setAnchor(GBC.CENTER).setInsets(10, 20, 20, 10));
	}
	
	// display this frame
	public void display()
	{
		this.repaint();
	}
	
	// close StartGame frame
	public void close()
	{
		setVisible(false);
		dispose();
	}
}

class PlayerNamePanel extends JPanel{
	Box playerNameBox;
	public PlayerNamePanel()
	{
		int strut = 10;
		playerNameBox = Box.createVerticalBox();
		JLabel nameLabel = new JLabel("Player name");
		nameLabel.setFont(new Font("Serif", Font.BOLD, 18));
		playerNameBox.add(nameLabel);
		playerNameBox.add(Box.createVerticalStrut(strut));
		JTextField textField = new JTextField(30);
		
		playerNameBox.add(textField);
		add(playerNameBox);
	}
}

class HostPanel extends JPanel{
	Box hostBox;
	Set<String> gameList = new HashSet();
	
	public HostPanel()
	{
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		hostBox = Box.createVerticalBox();
		gameList.add("thanh");
		gameList.add("abc");
		
		Iterator it = gameList.iterator();
	    while(it.hasNext()){
	    	String value=(String)it.next();
	    	JButton b = new JButton(value);
	    	b.setContentAreaFilled(false);
	    	//b.setPreferredSize(new Dimension(400, 30));
	    	b.setBorderPainted(false);
	    	b.addMouseListener(new MouseAdapter(){
	    		public void mouseClicked(MouseEvent e){
	    			System.out.println("connect");
	    		}
	    	});
	    	hostBox.add(b);
	    } 
	    add(hostBox);
	}
	
	public void updateGameList(Set<String> ls)
	{
		gameList = ls;
	}
	
}

class TransparentButton extends JButton {
	private float a = 0.5f;
	public TransparentButton(String text) { 
	    super(text);
	    setOpaque(false); 
	} 
	    
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g.create(); 
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a)); 
	    super.paintComponent(g2); 
	    g2.dispose(); 
	} 
}
