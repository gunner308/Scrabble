package scrabble.dataservice;

import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.io.IOException;
import java.io.InputStreamReader;


import scrabble.game.*;
import scrabble.*;

public class GameClient {

        public static final String host = "127.0.0.1";
	public static final int port = 10003;

        public int timeOut;

        private BufferedReader inFromUser;
	private BufferedReader inFromServer;
	private DataOutputStream outToServer;
	private Socket skt;
	private Player player;
	private Board board;
	private int turn;
        private Vector<Player> playerList;
        private Player currentPlayer;
        
	private Vector<LetterMove> currentMove;

        /* Client Constructor */
	public GameClient(String host, int port) throws IOException
	{
		skt = new Socket(host, port);
                turn = 0;
                
                timeOut = 0;

                inFromUser = new BufferedReader(new InputStreamReader(System.in));
                inFromServer = new BufferedReader(new InputStreamReader(skt.getInputStream()));
                outToServer = new DataOutputStream(skt.getOutputStream());
	}

        public void setPlayer(Player _player)    {

            player = _player;

        }

        public void setBoard(Board _board)  {

            board = _board;
        }

        public void updateBoard(String letter, int x, int y, String update)  {

            if ( update == "PLACE")    {

            
            }
            else if ( update == "REMOVE")  {

            }
            
        }

        public void setCurrentPlayer(String usernameSentByHost)  {
            
           for ( int i = 0; i < playerList.size(); i++ )    {

               if ( playerList.get(i).getUsername().compareTo(usernameSentByHost) == 0) {
                   currentPlayer = playerList.get(i);
               }
           }
        }

        
        


        public void addPlayerToList(Player newPlayer)  {

            playerList.add(newPlayer);
        }

        public void removePlayerFromList(String playerName) {

            for ( int i = 0; i < playerList.size(); i ++ )  {

                if ( playerList.get(i).getUsername().compareTo(playerName) == 0 )  {

                    playerList.remove(i);
                }
            }
        }

        
        /* Check if in turn or not */
        public boolean isTurn() {

            if ( currentPlayer.getUsername().compareTo(player.getUsername()) == 0 ) return true;
            return false;
            
            
        }


        public void writeServerMsg(String msg) throws IOException
	{
            try
            {
            // wait 1 seconds then send a msg
                Thread.sleep(1100L);
		outToServer.writeBytes(msg);
            } catch (InterruptedException e){
          	System.err.println(e);
            }
	}


        public String waitServerMsg(String inquery) throws IOException
	{
		String serverMsg = inFromServer.readLine();
		while (!serverMsg.startsWith(inquery))
		{
			System.out.println(serverMsg);
			serverMsg = inFromServer.readLine();
		}
		return serverMsg;
	}

        public void startNewTurn() {

            
        }

        public void quit()
	{
            try
            {
            	writeServerMsg("QUIT_GAME\n");
		inFromUser.close();
		inFromServer.close();
		outToServer.close();
            } catch (IOException e) {
		System.err.println(e);
            }
	}

        private void readErrorMessage() throws IOException
	{
		// error occurs
		System.out.println(inFromServer.readLine()); // "FAIL" message
		System.out.println(inFromServer.readLine()); // Error code
		System.out.println(inFromServer.readLine()); // Error message
	}

        public void preparation() throws IOException   {

            String playerName = inFromUser.readLine();
            setPlayer(new Player(playerName));

        }

        public void mainGame() throws IOException {

            String getCommand = "";

            while ( true )  {
                getCommand = inFromServer.readLine();
                if (getCommand.startsWith("TURN")) {

                    turn ++;
                    String currentPlayerName = getCommand.split(" ")[1];
                    setCurrentPlayer(currentPlayerName);
                    
                }
                else if(getCommand.startsWith("JOIN"))  {

                    String playerName = getCommand.split(" ")[1];
                    addPlayerToList(new Player(playerName));
                }
                else if(getCommand.startsWith("LEAVE"))  {

                    String playerName = getCommand.split(" ")[1];
                    removePlayerFromList(playerName);
                }
                else if(getCommand.startsWith("PLACE"))  {
                    String update = "PLACE";
                    String letter = getCommand.split(" ")[1];
                    int x = Integer.parseInt(getCommand.split(" ")[2]);
                    int y = Integer.parseInt(getCommand.split(" ")[3]);
                    updateBoard(letter,x,y,update);
                }
                else if(getCommand.startsWith("REMOVE"))  {
                    String update = "REMOVE";
                    String letter = " ";
                    int x = Integer.parseInt(getCommand.split(" ")[1]);
                    int y = Integer.parseInt(getCommand.split(" ")[2]);
                    updateBoard(letter,x,y,update);
                }
                else if(getCommand.startsWith("MESSAGE"))  {

                    String message = getCommand.split(" ")[1];
                    /* User Interface display the message */
                }
                else if(getCommand.startsWith("SET_SCORE"))  {

                    String wordScore = getCommand.split(" ")[1];
                    int score = Integer.parseInt(wordScore);
                    currentPlayer.addScore(score);
                    /* User Interface display the score accordingly  */
                }

                else if(getCommand.startsWith("END_GAME"))  {
                    
                    break;
                }
            }

        }
        
        public void play()  throws IOException  {

            preparation();
            mainGame();
            
        }

        /*
        public static void main(String[] args)
	{
            try {
            	GameClient client = new GameClient(host, port);
		client.play();
            } catch (IOException e)	{
		System.err.println(e);
            }
        }
        */

        
	
};
