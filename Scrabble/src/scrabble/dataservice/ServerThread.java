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

public class ServerThread extends Thread {
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;
    private Socket skt;
    private static Game game = new Game();
    private boolean isMaster;
    private static boolean masterQuit = false;
    private String username;
    private static Vector<Socket> clientSocketList = new Vector<Socket>();
    private static boolean lockWrite;

    public ServerThread(Game _game, Socket _skt) throws IOException
    {
        game = _game;
        skt = _skt;
        inFromClient = new BufferedReader(new InputStreamReader(skt.getInputStream()));
        outToClient = new DataOutputStream(skt.getOutputStream());
    }

    public void outToAll (String s, int exception)
    {
        Socket dstSocket;
        for (int i = 0; i < clientSocketList.size(); i++)
        {
            if ((exception == -1) || (i!=exception))
            {
                dstSocket = clientSocketList.elementAt(i);
                try{
                    DataOutputStream out = new DataOutputStream(dstSocket.getOutputStream());
                    while(lockWrite);
                    lockWrite = true;
                    out.writeBytes(s);
                }
                catch (IOException ioe){

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
        String line = inFromClient.readLine();
        while (true)
        {
            if ((clientSocketList.size() > 3) || game.isStarted())
            {
                while(lockWrite);
                lockWrite = true;
                outToClient.writeBytes("ERROR 1"+'\0');
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
                outToClient.writeBytes("ERROR 1" + '\0');
                lockWrite = false;
                continue;
            }
            username = temp[1];
            clientSocketList.add(skt);
            Player p = new Player(username);
            game.getPlayerList().add(p);
            outToAll ("JOIN" + '\0', -1);
            break;
        }
    }
    public void resignHandler()
    {
        game.getPlayerList().elementAt(clientSocketList.indexOf(skt)).setResign(true);
        outToAll ("SURRENDER" + username + '\0', clientSocketList.indexOf(skt));
        //fixTurn (j);
    }
    public void quitHandler()
    {
        outToAll ("QUIT" + username + '\0', clientSocketList.indexOf(skt));
        game.getPlayerList().removeElementAt(clientSocketList.indexOf(skt));
        clientSocketList.remove(skt);
    }
    public void controlInRoom () throws IOException
    {
        String line;
        while (true)
        {
            if (game.isStarted()) break;
            if (inFromClient.ready())
            {
                line = inFromClient.readLine();
                if (line.startsWith("MESSAGE")) {
                    chat(line);
                    //timing = System.currentTimeMillis();
                }
                else if(line.startsWith("QUIT"))
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
                            game.startGame();
                            game.setGameStt(true);
                            String str = "START";
                            outToAll(str, -1);
                            break;
                        }
                        else
                        {
                            outToClient.writeBytes("ERROR 3");
                        }
                    }
                    if (!line.startsWith("START") && !line.startsWith("CHAT")
                            && !line.startsWith("NOTREADY") && !line.startsWith("READY"))
                    {
                        outToClient.writeBytes("ERROR -1");
                    }
                }
            }
            /*
            if (System.currentTimeMillis() - timing > 180000){
                quited = true;
                quit();
                break;
            }*/
        }
    }
    public void controlInPlay () throws IOException
    {
        String line;
        while (true)
        {
            if (game.getPlayerList().elementAt(clientSocketList.indexOf(skt)).resigned() == true) break;
            if (clientSocketList.indexOf(skt) + 1 == game.getTurn())
            {
                outToAll ("TURN" + game.getPlayerList().elementAt(game.getTurn()-1).getUsername() + '\0', -1);
            }
            if (inFromClient.ready())
            {
                line = inFromClient.readLine();
                if (line.startsWith("PLACE"))
                {
                    if (game.validateMove())
                    outToAll (line + '\0', clientSocketList.indexOf(skt));
                }
                if (line.startsWith("REMOVE"))
                {
                    outToAll (line + '\0', clientSocketList.indexOf(skt));
                }
                if (line.startsWith("SUBMIT"))
                {
                    if (game.checkWord())
                    {
                        outToClient.writeBytes("ACCEPT" + '\0');
                        outToClient.writeBytes("SET_SCORE" + game.calculateScore());
                    }
                    else outToClient.writeBytes("REFUSE" +'\0');
                }
                if (line.startsWith("EXCHANGE"))
                {

                }
                if (line.startsWith("PASS"))
                {

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
        }
    }
    public void run()
    {
        try
        {
            while (true)
            {
                welcome();
                while (!masterQuit)
                {
                    controlInRoom();
                    //if (quited) break;
                    controlInPlay();
                    //if (quited) break;
                }
                if (!masterQuit) break;
            }
        }
        /*catch (SocketException se){
            System.out.println("SocketException!!!");
            quit();
        }*/
        catch (IOException ioe)
        {
            //System.out.println(ioe);
            //quit();
    	}

    }
}
