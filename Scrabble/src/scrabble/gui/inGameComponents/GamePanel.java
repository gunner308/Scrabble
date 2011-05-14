package scrabble.gui.inGameComponents;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scrabble.Player;
import scrabble.dataservice.GameClient;
import scrabble.game.Board;
import scrabble.game.LetterMove;
import scrabble.game.Square;
import scrabble.game.Tile;
import scrabble.gui.MyButton;

public class GamePanel extends JPanel{
	private static final int SQUARE_SIZE = 28;
	private static final int TILE_SIZE = 35;
	
	GameClient client;
	private Board board;
	private Player player;
	private BoardPanel boardPanel;
	private TilePanel tilePanel;
	private TileLeftPanel tileLeftPanel;
	
	private Tile currentTile;
	private TileButton sourceButton;
	private TileButton currentButton;
	private Toolkit toolkit;
	private Vector<LetterMove> currentMove = new Vector<LetterMove>();
	
	public GamePanel(){}
	
	public GamePanel(GameClient _client)
	{
		client = _client;
		player = client.getPlayer();
		board = client.getBoard();
		setLayout(null);
		
		toolkit = Toolkit.getDefaultToolkit();
		
		addBoardPanel();
		addTilePanel();
		addTileLeftPanel();
		addPassButton();
		addExchangeButton();
		addSubmitButton();
		setOpaque(false);
	}
	
	// panel containing the number of tiles left
	private void addTileLeftPanel()
	{
		int w = 70;
		int h = 90;
		tileLeftPanel = new TileLeftPanel(client, w, h);
		tileLeftPanel.setBounds(10, 330, w, h);
		add(tileLeftPanel);
	}
	
	// add panel containing board
	private void addBoardPanel()
	{
		boardPanel = new BoardPanel();
		boardPanel.setBounds(90, 20, SQUARE_SIZE*15, SQUARE_SIZE*15);
		add(boardPanel);
	}
	
	// add panel containing rack
	private void addTilePanel()
	{
		tilePanel = new TilePanel();
		tilePanel.setBounds(170, 450, TILE_SIZE*7, TILE_SIZE);
		add(tilePanel);
	}
	/**
	 * Button for player to pass turn
	 */
	private void addPassButton()
	{
		JButton b = new MyButton("Pass", 70, 30, 10, 450, null);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if (client.isTurn()){
					client.requestPass();
				}
			}
		});
		add(b);
	}
	
	/**
	 * Button for player to exchange tiles
	 */
	private void addExchangeButton()
	{
		JButton b = new MyButton("Swap", 70, 30, 90, 450, null);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if (client.isTurn() && client.countTile() >= 7){
					client.requestExchange();
				}
			}
		});
		add(b);
	}
	
	/**
	 * Button for player to submit
	 */
	private void addSubmitButton()
	{
		JButton b = new MyButton("Submit", 80, 30, 430, 450, null);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if (client.isTurn() && client.countTile() >= 7){
					client.requestExchange();
				}
			}
		});
		add(b);
	}
	
	// check whether this is a placed tile from previous rounds
	private boolean isOldTile(int i, int j)
	{
		for(LetterMove m:currentMove){
			if (m.x == i && m.y == j){
				return false;
			}
		}
		return true;
	}
	
	// Panel contains player's tiles
	class TilePanel extends JPanel{
		
		public TilePanel()
		{
			setLayout(new GridLayout(1, 7));
			try{
				Image img = ImageIO.read(new File("images/0.png"));
				for (int i = 0; i < 7; i++){
					TileButton b = new TileButton(0, i, 0, img, TILE_SIZE, false);
					b.setTile(new Tile(i));
					add(b);
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		
		public void updateRack()
		{
			Vector<Tile> rack = player.getRack();
			
		}
	}
	
	// panel contains board
	class BoardPanel extends JPanel{
		
		public BoardPanel()
		{
			setLayout(new GridLayout(15, 15));
			for (int i = 0; i < 15; i++){
				for (int j = 0; j < 15; j++){
					addButton(board.getSquare(i, j), i, j);
				}
			}
		}
		
		private void addButton(Square s, int i, int j)
		{
			try{
				Image img;
				if (i == 7 && j == 7){
					img = ImageIO.read(new File("images/STAR.png"));
				}else{
					img = ImageIO.read(new File("images/"+s.getType()+".png"));
				}
				TileButton b = new TileButton(i, j, s.getType(), img, SQUARE_SIZE, true);
				this.add(b);
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	// Button on board
	class TileButton extends JButton{
		int type;
		Tile tile;
		Image orgImg;
		int SIZE;
		boolean inBoard;
		int x, y;
		
		public TileButton(){}
		
		public TileButton(int i, int j, int _type, Image _img, int size, boolean _inBoard)
		{
			x = i;
			y = j;
			inBoard = _inBoard;
			SIZE = size;
			orgImg = _img;
			orgImg = orgImg.getScaledInstance(SIZE, SIZE, Image.SCALE_AREA_AVERAGING);
			type = _type;
			tile = null;
			
			this.setPreferredSize(new Dimension(SIZE,SIZE));
			this.setIcon(new ImageIcon(orgImg));
			this.addMouseListener(new MyMouseListener(this));
		}
		
		public void painComponent(Graphics g)
		{
			super.paintComponent(g);	
		}
		
		public void setTile(Tile _tile)
		{
			tile = _tile;
			try{
				String s = "images/"+tile.getLetter()+".png";
				System.out.println(s);
				Image img = ImageIO.read(new File("images/letter/"+tile.getLetter()+".png"));
				img = img.getScaledInstance(SIZE, SIZE, Image.SCALE_AREA_AVERAGING);
				setIcon(new ImageIcon(img));
				this.paintAll(this.getGraphics());
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		
		public void removeTile()
		{
			tile = null;
			this.setIcon(new ImageIcon(orgImg));
			this.paintAll(this.getGraphics());
		}
	}
	
	// mouselistener for TileButton
	class MyMouseListener extends MouseAdapter {
		TileButton b;
		
		public MyMouseListener(TileButton _b)
		{
			b = _b;
		}
		
		public void mousePressed(MouseEvent e) 
		{
			//if (b.tile == null && !isOldTile(b.x, b.y)){
			if (b.tile != null){
				currentTile = b.tile;
				sourceButton = b;
				Image cursorImage = toolkit.getImage("images/letter/" + b.tile.getLetter() +".png");
				cursorImage = cursorImage.getScaledInstance(30, 30, Image.SCALE_AREA_AVERAGING);
				Point cursorHotSpot = new Point(0,0);
				Cursor customCursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot, "");
				setCursor(customCursor);
				b.removeTile();
			}
		}
		public void mouseReleased(MouseEvent e) 
		{
			System.out.printf("%d %d\n", b.x, b.y);
		    if (currentButton.tile == null){
		    	if (currentTile != null){
		    		currentButton.setTile(currentTile);
		    	}
		    	sourceButton = null;
		    }else{
		    	sourceButton.setTile(currentTile);
		    }
		    currentTile = null;
		    currentButton = null;
		    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
		public void mouseEntered(MouseEvent e) 
		{
			currentButton = b;
		}
		
	}
}


