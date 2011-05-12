package scrabble.gui;

import javax.swing.JFrame;

import scrabble.dataservice.GameClient;

public final class MainFrame extends JFrame {
	// default sizes
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	// starting location
	private static int LOC_X = 100;
	private static int LOC_Y = 100;
	
	private StartGamePanel startGamePanel = null;
	private InGamePanel inGamePanel = null;
	GameClient client;
	
	
	public MainFrame()
	{
		setTitle("Scrabble game");
		setSize(WIDTH, HEIGHT);
		setLocation(LOC_X, LOC_Y);
		setResizable(false);
		
		// set start game screen
		setStartGameScreen();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);	
	}
	
	// change the screen into start game screen
	public void setStartGameScreen()
	{
		if (inGamePanel != null){
			this.remove(inGamePanel);
			inGamePanel = null;
		}
		
		if (startGamePanel == null)
			startGamePanel = new StartGamePanel(this);
		startGamePanel.setBounds(0, 0, WIDTH, HEIGHT);
		add(startGamePanel);
		redisplay();
	}
	
	// change the screen into in game screen
	public void setInGameScreen(GameClient _client)
	{
		if (startGamePanel != null){
			this.remove(startGamePanel);
		}
		client = _client;
		System.out.println(client.isMaster());
		inGamePanel = new InGamePanel(this, client);
		add(inGamePanel);
		redisplay();
	}
	
	// redisplay the screen
	public void redisplay()
	{
		this.paintAll(this.getGraphics());
	}
	
	/**
	 * End game
	 */
	public void endGame()
	{
		
	}
	/**
	 * Start game
	 */
	public void startGame()
	{
		
	}
	
	/**
	 * Display a message
	 */
	public void displayMessage(String s)
	{
		
	}
}
