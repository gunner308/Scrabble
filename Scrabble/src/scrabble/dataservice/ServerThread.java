package scrabble.dataservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import scrabble.game.*;

public class ServerThread extends Thread {
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private Socket skt;
	private Game game;
	
	private String username;
	
	public ServerThread(Game _game, Socket _skt) throws IOException
	{
		game = _game;
		skt = _skt;
		inFromClient = new BufferedReader(new InputStreamReader(skt.getInputStream()));
    	outToClient = new DataOutputStream(skt.getOutputStream());
	}
	
}
