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

public class PlayerPanel extends JPanel{
	private Vector<PlayerBoxPanel> players; 
	Box box = Box.createHorizontalBox();
	
	public PlayerPanel()
	{
		players = new Vector<PlayerBoxPanel>();
		setLayout(new BorderLayout());
		setOpaque(false);
		add(box, BorderLayout.WEST);
		//this.setSize(800, 100);
		addPlayer("Player1", true);
		addPlayer("Player2", false);
		addPlayer("Player3", false);
		addPlayer("Player4", false);
		redisplay();
	}
	
	// add a new player to panel
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
	}
	
	//update score of a player
	public void updateScore(String username, int score)
	{
		for (PlayerBoxPanel i:players ){
			if (i.getUsername().equals(username)){
				i.updateScore(score);
				break;
			}
		}
		redisplay();
	}
	
	private void redisplay()
	{
		box.removeAll();
		for (PlayerBoxPanel i:players ){
			box.add(i);
			box.add(Box.createHorizontalStrut(10));
		}
		this.paintAll(this.getGraphics());
	}
}

class PlayerBoxPanel extends JPanel{
	private static int WIDTH = 150;
	private static int HEIGHT = 75;
	String username;
	JLabel nameLabel, scoreLabel;
	boolean isMaster;
	int score;
	Image img; 
	
	public PlayerBoxPanel(String _username, boolean _isMaster)
	{
		username = _username;
		isMaster = _isMaster;
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
	    g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
	}
	
	
	/**
	 * return username
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * update new score
	 */
	public void updateScore(int _score)
	{
		score = _score;
	}
	
}
