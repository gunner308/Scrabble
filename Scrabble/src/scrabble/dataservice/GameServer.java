package scrabble.dataservice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import scrabble.game.*;


public class GameServer {
	private ServerSocket serverSocket;
	private Game game;

	/**
	 * @param port			the server port number
	 */
    public GameServer(int port) throws IOException{
    	serverSocket = new ServerSocket (port);
    	game = new Game();
    	serverSocket.setReuseAddress(true);
    	System.out.println("Game server started successfully.");
    }

    /**
     * Listen to the specified port number, wait for clients to connect.
     * @throws IOException
     * @throws InterruptedException
     */
    public void listen()throws IOException, InterruptedException{
    	while (true){
    		// avoid resources getting exhausted
    		Thread.sleep(100);
    		
    		// keep listening for client messages
    		Socket skt = serverSocket.accept();
    		System.out.println("Someone is coming in...");
    		new ServerThread(game, skt).start();
    	}
    }

    public static void main(String args[]) throws IOException, InterruptedException{
   		new GameServer(1234).listen();
    }
}
