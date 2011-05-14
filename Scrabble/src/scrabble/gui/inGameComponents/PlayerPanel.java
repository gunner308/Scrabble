package scrabble.gui.inGameComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scrabble.Player;

public class PlayerPanel extends JPanel{
	private Vector<PlayerBoxPanel> players;
	private Vector<Player> playerList;
	Box box = Box.createHorizontalBox();
	
	public PlayerPanel(Vector<Player> _playerList)
	{
		playerList = _playerList;
		System.out.println(playerList.size());
		setLayout(new BorderLayout());
		setOpaque(false);
		for (Player i: playerList){
			box.add(new PlayerBoxPanel(i));
			box.add(Box.createHorizontalStrut(10));
		}
		add(box, BorderLayout.WEST);
	}
	
	/*// add a new player to panel
	public void addPlayer(String username, boolean isMaster)
	{
		PlayerBoxPanel temp = new PlayerBoxPanel(username, isMaster); 
		players.add(temp);
		redisplay();
	}
	
	public void removePlayer(String username)
	{
		for (PlayerBoxPanel i:players ){
			if (i.getUsername().equals(username)){
				players.removeElement(i);
				break;
			}
		}
		redisplay();
	}*/
	
	//update score of a player
	/*public void updateScore(String username, int score)
	{
		for (PlayerBoxPanel i:players ){
			if (i.getUsername().equals(username)){
				i.updateScore(score);
				break;
			}
		}
		redisplay();
	}*/
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		System.out.println("Painting");
		System.out.println(playerList.size());
		box.removeAll();
		for (Player i: playerList){
			box.add(new PlayerBoxPanel(i));
			box.add(Box.createHorizontalStrut(10));
		}
		//this.paintAll(g);
	}
}

class PlayerBoxPanel extends JPanel{
	private static int WIDTH = 150;
	private static int HEIGHT = 75;
	String username;
	JLabel nameLabel, scoreLabel;
	boolean isMaster, inTurn, isResigned;
	int score;
	Image img; 
	
	public PlayerBoxPanel(Player p)
	{
		username = p.getUsername();
		isMaster = p.isMaster();
		inTurn = p.isInTurn();
		isResigned = p.resigned();
		
		Color c;
		if (isMaster){
			c = Color.blue;
		}else{
			c = Color.BLACK;
		}
		// nameLabel
		nameLabel = new JLabel(username);
		nameLabel.setBounds(10, 5, 150, 30);
		nameLabel.setFont(new Font("Serif", Font.BOLD, 18));
		nameLabel.setForeground(c);
		add(nameLabel);
		// score label
		score = 0;
		scoreLabel = new JLabel(score + "");
		scoreLabel.setBounds(100, 40, 50, 30);
		scoreLabel.setFont(new Font("Serif", Font.BOLD, 23));
		scoreLabel.setForeground(Color.white);
		add(scoreLabel);
		// status
		if (isResigned){
			add(new StatusPanel("RESIGN"));
		}else if (inTurn){
			add(new StatusPanel("TURN"));
		}
		
		
		String location;
		if (isMaster){
			location = "images/PLAYERMASTER.png";
		}else{
			location = "images/PLAYER.png";
		}
		try{
			img = ImageIO.read(new File(location));
		}catch (IOException e){
			e.printStackTrace();
		}
		Dimension size = new Dimension(WIDTH, HEIGHT);
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	}
	
	public void paintComponent(Graphics g) 
	{
	    super.paintComponent(g);
	    //System.out.println("Paint o player");
	    g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
	}
	
	class StatusPanel extends JPanel{
		String text;
		Image img;
		
		public StatusPanel(String t)
		{
			text = t;
			try{
				img = ImageIO.read(new File("images/STATUS.png"));
			}catch (IOException e){
				e.printStackTrace();
			}
			JLabel l = new JLabel(text);
			l.setBounds(5, 5, 50, 30);
			add(l);
		}
		
		public void paintComponent(Graphics g) 
		{
		    super.paintComponent(g);
		    g.drawImage(img, 0, 0, null);
		}
	}
	
}
