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
	private Vector<PlayerBoxPanel> panelList;
	Box box = Box.createHorizontalBox();
	
	public PlayerPanel(Vector<Player> _playerList)
	{
		playerList = _playerList;
		//System.out.println(playerList.size());
		setLayout(new BorderLayout());
		setOpaque(false);
		panelList = new Vector<PlayerBoxPanel>();
		for (int i = 0; i < 4; i++){
			PlayerBoxPanel p = new PlayerBoxPanel();
			panelList.add(p);
			box.add(p);
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
	
	public void redisplay()
	{
		System.out.println("So luong player:" + playerList.size());
		for (PlayerBoxPanel i: panelList){
			i.setVisible(false);
		}
		int count = 0;
		for (Player i : playerList){
			panelList.get(count).setPlayer(i);
			panelList.get(count).setVisible(true);
			count++;
			System.out.println(count);
		}
	}
}

class PlayerBoxPanel extends JPanel{
	private static int WIDTH = 150;
	private static int HEIGHT = 75;
	String username;
	JLabel nameLabel, scoreLabel;
	boolean isMaster, inTurn, isResigned;
	int score;
	Image masterImg, img;
	StatusPanel sttPanel;
	
	public PlayerBoxPanel()
	{
		this.setVisible(false);
		
		// nameLabel
		nameLabel = new JLabel("");
		nameLabel.setBounds(10, 5, 150, 30);
		nameLabel.setFont(new Font("Serif", Font.BOLD, 18));
		nameLabel.setForeground(Color.blue);
		add(nameLabel);
		// score label
		score = 0;
		scoreLabel = new JLabel("");
		scoreLabel.setBounds(100, 40, 50, 30);
		scoreLabel.setFont(new Font("Serif", Font.BOLD, 23));
		scoreLabel.setForeground(Color.white);
		add(scoreLabel);
		// status
		sttPanel = new StatusPanel("");
		sttPanel.setVisible(false);
		
	
		try{
			masterImg = ImageIO.read(new File("images/PLAYERMASTER.png"));
			img = ImageIO.read(new File("images/PLAYER.png"));
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
	
	public void setPlayer(Player p)
	{
		// nameLabel
		nameLabel.setText(p.getUsername());
		// score label
		score = 0;
		scoreLabel.setText(p.getScore()+"");
		// status
		if (p.resigned()){
			sttPanel.setText("RESIGN");
			sttPanel.setVisible(true);
		}else if (p.isInTurn()){
			sttPanel.setText("TURN");
			sttPanel.setVisible(true);
		}else{
			sttPanel.setVisible(false);
		}
		isMaster = p.isMaster();
	}
	
	public void paintComponent(Graphics g) 
	{
	    super.paintComponent(g);
	    //System.out.println("Paint o player");
	    if (isMaster)
	    	g.drawImage(masterImg, 0, 0, WIDTH, HEIGHT, null);
	    else
	    	g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
	}
	
	class StatusPanel extends JPanel{
		String text;
		Image img;
		JLabel stt;
		
		public StatusPanel(String t)
		{
			text = t;
			try{
				img = ImageIO.read(new File("images/STATUS.png"));
			}catch (IOException e){
				e.printStackTrace();
			}
			stt = new JLabel(text);
			stt.setBounds(5, 5, 50, 30);
			add(stt);
		}
		
		public void setText(String s)
		{
			stt.setText(s);
		}
		
		public void paintComponent(Graphics g) 
		{
		    super.paintComponent(g);
		    g.drawImage(img, 0, 0, null);
		}
	}
	
}
