package scrabble.dataservice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import scrabble.game.*;


public class GameServer extends Thread{
	private ServerSocket serverSocket;
	private Game game;

	/**
	 * @param port			the server port number
	 */
    public GameServer(int port) throws IOException{
    	serverSocket = new ServerSocket (port);
    	game = new Game();
    	serverSocket.setReuseAddress(true);
    	//System.out.println("Game server started successfully.");
    }
    
    public void finish()
    {
    	try{
    		serverSocket.close();
    	}catch (IOException e){
    		e.printStackTrace();
    	}
    }

    public void run()
    {
        while (true){
            try{
            	Thread.sleep(100);
	    		Socket skt = serverSocket.accept();
	    		System.out.println("Someone is coming in...");
	    		new ServerThread(game,skt).start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    	}
    }
}
