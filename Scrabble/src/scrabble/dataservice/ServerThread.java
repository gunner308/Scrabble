package scrabble.dataservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import scrabble.game.*;
import java.util.*;
import scrabble.Constants;
import scrabble.Player;
import scrabble.game.LetterMove;

public class ServerThread extends Thread {
    //public static final String host = "127.0.0.1";
    //public static final int port = 10003;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;
    private Socket skt;
    private static Game game = new Game();
    private boolean isMaster;
    private static boolean masterQuit = false;
    private String username;
    private static Vector<Socket> clientSocketList = new Vector<Socket>();
    private static boolean lockWrite = false;
    private boolean myTurn;
    public ServerThread(Game _game, Socket _skt) throws IOException
    {
        game = _game;
        skt = _skt;
        inFromClient = new BufferedReader(new InputStreamReader(skt.getInputStream()));
        outToClient = new DataOutputStream(skt.getOutputStream());
        myTurn = false;
    }
    
    /* DEBUGGING */
    /*public ServerThread (Game _game, Socket _skt) throws IOException
    { 
        skt = _skt;
        inFromClient = new BufferedReader(new InputStreamReader(skt.getInputStream()));
        outToClient = new DataOutputStream(skt.getOutputStream());
    }*/
    
    /*public void debugSending()   {
        String string = "";
        
        
        
    }*/
    /*public static void main (String args[]) throws Exception
    {
        ServerThread newThread = new ServerThread (host, port);
        //newThread.debugSending();
        //newThread
    }*/
    
    /* END DEBUGGING */

    public void outToAll (String s, int exception)
    {
        Socket dstSocket;
        for (int i = 0; i < clientSocketList.size(); i++)
        {
            if ((exception == -1) || (i!=exception))
            {
                dstSocket = clientSocketList.elementAt(i);
                while(lockWrite);
                lockWrite = true;
                try{
                    DataOutputStream out = new DataOutputStream(dstSocket.getOutputStream());
                    //while(lockWrite);
                    //lockWrite = true;
                    out.writeBytes(s);
                }
                catch (IOException ioe){
                    if (!isMaster)
                    {
                        quitHandler();
                        break;
                    }
                    else
                    {
                        masterQuit = true;
                        break;
                    }
                }
                lockWrite = false;
            }
        }
    }
    public void chat(String line) throws IOException
    {
        outToAll(line,clientSocketList.indexOf(username));
    }
    public void welcome () throws IOException
    {
        String line;
        while (true)
        {
            if ((clientSocketList.size() > 3) || game.isStarted())
            {
                while(lockWrite);
                lockWrite = true;
                outToClient.writeBytes("ERROR 1\n");
                lockWrite = false;
                continue;
            }
            line = inFromClient.readLine();
            String temp[];
            temp = line.split(" ");

            if (!line.startsWith("NAME"))
            {
                while(lockWrite);
                lockWrite = true;
                outToClient.writeBytes("ERROR 1\n");
                lockWrite = false;
                skt.close();
                continue; 
            }
            username = temp[1];
            for (int i=0; i < game.getPlayerList().size(); i++)
            {
                while(lockWrite);
                lockWrite = true;
                outToClient.writeBytes("JOIN " + game.getPlayerList().elementAt(i).getUsername() + "\n");
                lockWrite = false;
            }
            clientSocketList.add(skt);
            Player p = new Player(username,false);
            game.getPlayerList().add(p);
            outToAll ("JOIN " + username + "\n", -1);
            break;
        }
    }
    private void resignHandler()
    {
        game.getPlayerList().elementAt(clientSocketList.indexOf(skt)).setResign(true);
        outToAll ("SURRENDER" + username + "\n", clientSocketList.indexOf(skt));
        outToAll("TURN "+ game.nextTurn() + "\n", -1);
        //fixTurn (j);
    }
    private void quitHandler()
    {
        outToAll ("QUIT" + username + "\n", clientSocketList.indexOf(skt));
        game.getPlayerList().removeElementAt(clientSocketList.indexOf(skt));
        clientSocketList.remove(skt);
        outToAll("TURN "+ game.nextTurn() + "\n", -1);
    }
    
    public void controlInRoom () throws IOException
    {
        String line;
        //havent play yet mean still in room
        while (!game.isStarted())
        {            
            if (inFromClient.ready())
            {
                line = inFromClient.readLine();
                if(line.startsWith("QUIT"))
                {
                    if (!isMaster)
                    {
                        quitHandler();
                        break;
                    }
                    else
                    {
                        //MASTER QUIT
                        masterQuit = true;
                        break;
                    }
                }
                else
                {
                    if (line.startsWith("START"))
                    {
                        //timing = System.currentTimeMillis();
                        if (game.canStart())
                        {
//                            game.startGame();
                            //game.setGameStt(true);
                            String str = "START\n";
                            outToAll(str, -1);
                            /*
                            try
                            {
                                Thread.currentThread().sleep(1000);
                            }
                            catch (InterruptedException ie)
                            {
                                
                            }
                             * 
                             */
                            break;
                        }
                        else
                        {
                            outToClient.writeBytes("ERROR 3");
                        }
                    }
                    if (!line.startsWith("START"))
                    {
                        outToClient.writeBytes("ERROR -1");
                    }
                }
            }
        }
    }
    
    public void controlInPlay () throws IOException
    {
        //System.out.println ("in game");
        if (!game.isStarted())
        {
            for (int i = 0; i < clientSocketList.size(); i++)
            {
                System.out.println ("server: in for number" + i);
                Socket dstSocket;
                dstSocket = clientSocketList.elementAt(i);
                try{
                    DataOutputStream out = new DataOutputStream(dstSocket.getOutputStream());
                    Vector <Tile> tiles = game.getTiles(game.getPlayerList().elementAt(i).getUsername());
                    for (int j=0; j< tiles.size(); j++)
                    {
                        String s = "TILE " + tiles.elementAt(j).getID() +"\n";
                        System.out.println("server: send " + s);
                        while(lockWrite);
                        lockWrite = true;
                        out.writeBytes(s);
                        lockWrite = false;
                    }
                    tiles.clear();
                }
                catch (IOException ioe)
                {
                }
            }
            
        // testing
            username = game.nextTurn();
        
            String s = "TURN " + username + "\n";
            System.out.println ("server: " + s);
            outToAll (s, -1);

            game.startGame();

            System.out.println("server: finish preparation");
        }

        

        long timing=0;
        String line;
        
        while (true)
        {
            if (game.getPlayerList().elementAt(clientSocketList.indexOf(skt)).resigned() == true
                && username == game.getTurn())
                outToAll("TURN " + game.nextTurn() + "\n", -1);
            timing = System.currentTimeMillis();
            if (inFromClient.ready()
                && !game.getPlayerList().elementAt(clientSocketList.indexOf(skt)).resigned())
            {
                line = inFromClient.readLine();
                System.out.println("server: receive message " + line);
                if (username == game.getTurn())
                {
                    timing = System.currentTimeMillis();
                    if (line.startsWith("PLACE"))
                    {
                        System.out.println ("server: NHAN PLACE");
                        String []s = line.split(" ");
                        LetterMove letterMove = new LetterMove (s[1], s[2], s[3]);
                        game.updateMove(letterMove);
                        System.out.println ("server: SEND PLACE");
                        outToAll (line + "\n", clientSocketList.indexOf(skt));
                    }
                    if (line.startsWith("REMOVE"))
                    {
                        outToAll (line + "\n", clientSocketList.indexOf(skt));
                    }
                    if (line.startsWith("SUBMIT"))
                    {
                        System.out.println("server: going to check word");
                        if (game.checkWord())
                        {
                            System.out.println("server: this is a correct move");
                            outToAll("ACCEPT\n", -1);
                            outToClient.writeBytes("SET_SCORE" + game.calculateScore());
                            outToAll("TURN "+ game.nextTurn() + "\n", -1);
                        }
                        else 
                        {
                            System.out.println("server: this is a wrong move");
                            outToClient.writeBytes("REFUSE\n");
                        }
                    }
                    if (line.startsWith("EXCHANGE"))
                    {
                        Vector <Tile> exchange = game.exchangeRack();
                        for (int i=0; i< exchange.size(); i++)
                        {
                            outToClient.writeBytes("TILE" + exchange.elementAt(i).getID() +"\n");
                        }
                        outToAll("TURN "+ game.nextTurn() + "\n", -1);
                    }
                    if (line.startsWith("PASS"))
                    {
                        outToAll("TURN " + game.nextTurn() + "\n", -1);
                    }
                    if (line.startsWith("SURRENDER"))
                    {
                        resignHandler();
                        break;
                    }
                    if (line.startsWith("QUIT"))
                    {
                        if (!isMaster)
                        {
                            quitHandler();
                            break;
                        }
                        else
                        {
                            masterQuit = true;
                            break;
                        }
                    }
                }
                else
                {
                    if (line.startsWith("SURRENDER"))
                    {
                        resignHandler();
                    }
                    if (line.startsWith("QUIT"))
                    {
                        if (!isMaster)
                        {
                            quitHandler();
                        }
                        else
                        {
                            masterQuit = true;
                            break;
                        }
                    }
                }
            }

            if (username == game.getTurn())
            {
                Vector <Tile> tiles = game.getNewTiles();
                for (int i=0; i< tiles.size(); i++)
                {
                    String s = "TILE " + tiles.elementAt(i).getID() +"\n";
                    outToClient.writeBytes(s);
                    System.out.println("server: update " + s);
                }
                if (System.currentTimeMillis() - timing >= 120000)
                {
                    outToAll ("TURN " + game.nextTurn() + "\n", -1);
                }
                if (game.endGame())
                {
                    outToAll("END_GAME\n", -1);
                }
            }

            try
            {
                this.sleep(100);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void run()
    {
        try
        {
            //while (true)
            //{
                welcome();
                while (!masterQuit)
                {
                    controlInRoom();
                    //if (quited) break;
                    controlInPlay();
                    //if (quited) break;
                }
                //if (!masterQuit) break;
            //}
        }
        /*catch (SocketException se){
            System.out.println("SocketException!!!");
            quit();
        }*/
        catch (IOException ioe)
        {
            
    	}

    }
}
