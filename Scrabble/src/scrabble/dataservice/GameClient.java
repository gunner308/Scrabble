package scrabble.dataservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;

import scrabble.game.*;
import scrabble.*;

public class GameClient {
	private BufferedReader inFromServer;
	private DataOutputStream outToServer;
	private Socket skt;
	private Player player;
	private Board board;
	private int turn;
	private Vector<LetterMove> currentMove;
	
	public GameClient(Socket _skt, String username)
	{
		skt = _skt;
		board = new Board();
		player = new Player(username);
	}
	
}
