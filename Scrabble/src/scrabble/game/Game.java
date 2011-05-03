package scrabble.game;

import java.util.Vector;

import scrabble.Player;

public class Game {
    private final int size = 10;
    private Square [][]board;
    private Dictionary dictionary;
    private int turn;
    private Bag bag;
    private Vector<Player> playerList;
    private Vector<LetterMove> currentMove;
    private boolean isStarted;
    
    public Game()
    {
    	board = new Square[size][size];
    	dictionary = new Dictionary();
    	bag = new Bag();
        isStarted = false;
    }
    //convert t
    public String toString (Vector <LetterMove> v)
    {
        String s = "";
        return s;
    }
    public void startGame()
    {
    	
    }

    public void finishGame()
    {
    	
    }

    public boolean canStart()
    {
        return true;
    }
    public boolean isStarted()
    {
        return isStarted;
    }
    public void setGameStt (boolean _isStarted)
    {
        isStarted = _isStarted;
    }
    public void addPlayer(Player p)
    {
        playerList.add(p);
    }
    public Vector<Player> getPlayerList ()
    {
        return playerList;
    }
    public int getTurn ()
    {
        return turn;
    }
    public boolean checkWord ()
    {
        return dictionary.checkWord(toString(currentMove));
    }
    // validate current move
    public boolean validateMove()
    {
    	return true;
    }
    
    // calculate score of current move
    public int calculateScore()
    {
    	return 0;
    }
}
