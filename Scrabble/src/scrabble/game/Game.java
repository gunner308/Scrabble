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
    
    public Game()
    {
    	board = new Square[size][size];
    	dictionary = new Dictionary();
    	bag = new Bag();
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
