package scrabble.game;

import java.util.Vector;

public class Board {
	private Square board[][];
	private static final int size = 10;
	
	public Board()
	{
		board = new Square[size][size]; 
	}
	
	// get a square
	public Square getSquare(int i, int j)
	{
		return board[i][j];
	}
	
	// update a move to the board
	public void update(Vector<LetterMove> move)
	{
		
	}
}
