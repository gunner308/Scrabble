package scrabble.dataservice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import scrabble.game.*;


public class GameServer extends Thread{
	private ServerSocket serverSocket;
	private Game game;
	private boolean done = false;

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
    		done = true;
    		serverSocket.close();
    	}catch (IOException e){
    		e.printStackTrace();
    	}
    }
    @Override
    public void run()
    {
        while (!done){
            try{
            	Thread.sleep(1000);
	    		Socket skt = serverSocket.accept();
	    		System.out.println("Someone is coming in...");
	    		new ServerThread(game,skt).start();
	    		if (serverSocket.isClosed()){
            		break;
            	}
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    	}
    }
}
