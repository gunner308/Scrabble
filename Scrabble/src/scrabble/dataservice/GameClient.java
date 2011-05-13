/* WORKING TODAY :
    Processing error codes;
    COmplete missing functions;
    Handling exceptions;
*/

/* GUI functions
 * repaint()
 * showMessage()
 *
 */
package scrabble.dataservice;


import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.io.IOException;
import java.io.InputStreamReader;


import scrabble.game.*;
import scrabble.gui.MainFrame;
import scrabble.*;

public class GameClient extends Thread {

        public static final String host = "127.0.0.1";
	public static final int port = 10003;
        public static final int size = 15;

        //public int timeOut;

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
        private boolean isMaster;

        private MainFrame GUI;

        /* Client Constructor */
	public GameClient(Socket _skt, String playerName, boolean _isMaster, MainFrame _GUI ) throws IOException
	{
            skt = _skt;
            turn = 0;
            player = new Player(playerName);
            board = new Board();
            playerList = new Vector<Player> ();
            GUI = _GUI;

            //timeOut = 0;
            setMaster(_isMaster);
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            inFromServer = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            outToServer = new DataOutputStream(skt.getOutputStream());

            String nameMessage = "NAME" + playerName + "\n";
            sendMessage(nameMessage);
            GUI.setInGameScreen(this);
        }
        
        

        public void setPlayer(Player _player)    {

            player = _player;

        }

        public void setBoard(Board _board)  {

            board = _board;
        }

        public Board getBoard() {

            return board;
        }

        public boolean isMaster()  {

            return isMaster;
        }

        public void setMaster(boolean _isMaster)    {

            isMaster = _isMaster;
        }

        public void updateBoard(String letter, int x, int y, String update)  {

            if ( update == "PLACE")    {
                // GUI displaying placing
            
            }
            else if ( update == "REMOVE")  {
                // GUI displayer removing
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

        
        /* Check if "my" player is in turn or not */
        public boolean isTurn() {

            if ( currentPlayer.getUsername().compareTo(player.getUsername()) == 0 ) return true;
            return false;
            
            
        }


        public void sendMessage(String msg) throws IOException
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

        public void receiveLetter(int tileID) {

            Tile newTile = new Tile(tileID);
            player.addTile(newTile);

            // GUI.receiveLetter(int tileID);
        }

        
        
        public void checkWordResult(String getCommand)   {

            
            if ( getCommand.startsWith("ACCEPT"))   {
                board.update(currentMove);
                GUI.redisplay();

            }

            else if ( getCommand.startsWith("REFUSE")) {
               currentMove.clear();
               GUI.redisplay();
            }
            
        }

        

        public void requestExchange() throws IOException  {

            String requestMsg = "EXCHANGE" + "\n";
            sendMessage(requestMsg);
        }
        /* Move Processing : place letter, remove letter from board, submit Word */
        public void placeLetter(int tileID, int x, int y) throws IOException {

            LetterMove tempMove = new LetterMove(x, y, tileID);
            currentMove.add(tempMove);
            String message = "PLACE" + tileID + x + y + "\n";
            sendMessage(message);
            GUI.redisplay();
        }

        public void removeLetter(int x, int y)  throws IOException  {

            String message = "REMOVE" + x + y + "\n";
            for (int i =0; i < currentMove.size(); i ++)    {

                if ( currentMove.get(i).x == x && currentMove.get(i).y == y )   {

                    currentMove.remove(i);
                }
            }
            sendMessage(message);
            GUI.redisplay();
            
        }

        public void submitWord() throws IOException   {

            String requestMsg = "SUBMIT" + "\n";
            sendMessage(requestMsg);
        }

        /* End move processing */

        public void quit()
	{
            try
            {
            	sendMessage("QUIT" + player.getUsername() + "\n");
                
		inFromUser.close();
		inFromServer.close();
		outToServer.close();
                skt.close();
            } catch (IOException e) {
		System.err.println(e);
            }
	}


        public boolean canStart()   {

            if ( isMaster() && playerList.size() >= 2 )
            return true;
            else return false;
        }

        public void callStartGame() throws IOException {

            String startMessage = "START_GAME" + "\n";
            sendMessage(startMessage);
            
        }

        public void resign()    {

            try
            {
                sendMessage("SURRENDER " + player.getUsername() + "\n");
                player.setResign(true);
            }
            catch ( IOException e)  {
                e.printStackTrace();
            }
        }

        public void requestPass()   {

            currentMove.clear();
            GUI.redisplay();
            String passMessage = "PASS" + " " + player.getUsername() + "\n";
            try {

                sendMessage(passMessage);
            }
            catch ( IOException e)  {
                e.printStackTrace();
            }

            
        }

        public int countTile()  {

            int tileOnBoard = 0;
            for (int i = 0; i < size; i ++) {

                for ( int j = 0; j < size; j ++)    {

                    Square thisSquare = board.getSquare(i, j);
                    if ( thisSquare.isOccupied() ) tileOnBoard ++;
                }
            }
            int tileOnHands = 0;
            for ( int i = 0; i < playerList.size(); i ++ )  {

                tileOnHands += playerList.get(i).getRack().size();
                
            }
            System.out.println(tileOnBoard);
            System.out.println(tileOnHands);
            int tileOnBag = 100 - tileOnBoard - tileOnHands;
            if (tileOnBag < 0){
            	tileOnBag = 0;
            }
            return tileOnBag;
            
        }

        public void endGame()   {
            GUI.endGame();
        }

        public void closeSocket() throws IOException   {

            inFromUser.close();
            inFromServer.close();
            outToServer.close();
            skt.close();
            
        }

        public boolean preparation() throws IOException   {

            //String playerName = inFromUser.readLine();
            //setPlayer(new Player(playerName));
            String getCommand = "";
            while ( true ) {
                
                try {
                    getCommand = inFromServer.readLine();

                    if ( getCommand.startsWith("START")) {

                        GUI.startGame();
                        return true;
                    }
                    else if(getCommand.startsWith("JOIN"))  {

                        String playerName = getCommand.split(" ")[1];
                        addPlayerToList(new Player(playerName));
                        // GUI display joining player
                        GUI.redisplay();
                    }
                    else if(getCommand.startsWith("LEAVE"))  {

                        String playerName = getCommand.split(" ")[1];
                        removePlayerFromList(playerName);
                        // GUI display removing player
                        GUI.redisplay();
                    }
                    else if(getCommand.startsWith("ERROR"))  {

                        String errorMessage = "Room is already full";
                        GUI.displayMessage(errorMessage);
                        this.closeSocket();
                        return false;
                    }
                    
                }
                catch (Exception e) {
                    e.printStackTrace();;
                }
            }

        }

        public void mainGame() throws IOException {

            String getCommand = "";

            while ( true )  {
                try {
                    getCommand = inFromServer.readLine();
                    if (getCommand.startsWith("TURN")) {
                        //turn ++;
                        String currentPlayerName = getCommand.split(" ")[1];
                        setCurrentPlayer(currentPlayerName);
                        GUI.redisplay();
                        GUI.startTurn(currentPlayerName);
                    }

                    else if(getCommand.startsWith("PLACE"))  {

                        int tileID = Integer.parseInt(getCommand.split(" ")[1]);
                        int x = Integer.parseInt(getCommand.split(" ")[2]);
                        int y = Integer.parseInt(getCommand.split(" ")[3]);
                        placeLetter(tileID, x, y);
                    }
                    else if(getCommand.startsWith("REMOVE"))  {
                        int x = Integer.parseInt(getCommand.split(" ")[1]);
                        int y = Integer.parseInt(getCommand.split(" ")[2]);
                        removeLetter(x, y);
                    }
                    else if(getCommand.startsWith("MESSAGE"))  {

                        String username = getCommand.split(" ")[1];
                        String message = getCommand.split(" ")[2];
                        /* User Interface display the message */
                    }
                    else if(getCommand.startsWith("TILE"))  {

                        String strTileID = getCommand.split(" ")[1];
                        int tileID = Integer.parseInt(strTileID);
                        receiveLetter(tileID);
                        /* User Interface display the message */
                    }
                    else if(getCommand.startsWith("SET_SCORE"))  {

                        String wordScore = getCommand.split(" ")[1];
                        int score = Integer.parseInt(wordScore);
                        currentPlayer.addScore(score);
                        /* User Interface display the score accordingly  */
                        GUI.redisplay();
                    }
                    else if(getCommand.startsWith("PASS"))  {

                        String username = getCommand.split(" ")[1];
                        GUI.displayMessage("Player " + username + " has passed his turn");
                        currentMove.clear();
                        GUI.redisplay();
                    }

                    else if(getCommand.startsWith("END_GAME"))  {

                        endGame();

                        break;
                    }
                    else if(getCommand.startsWith("QUIT"))  {

                        String username = getCommand.split(" ")[1];

                        /* Remove the quitted player from list */
                        for ( int i = 0; i < playerList.size(); i ++ )  {
                            if ( playerList.get(i).getUsername() == username)   {
                                playerList.remove(i);
                            }
                        }
                        String noticeMessage = username + " has left the game";
                        GUI.displayMessage(noticeMessage);
                        GUI.redisplay();
                    }
                }
                catch(Exception e)  {
                    e.printStackTrace();
                }

            }

        }
        
        public Player getPlayer()
        {
        	return player;
        }
        
        public void run()    {

            while ( true )  {

                try {
                    if (preparation())
                    mainGame();
                    else break;
                }
                catch ( IOException e)  {
                    e.printStackTrace();
                }
            }
            
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
