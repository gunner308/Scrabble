package scrabble.game;

import scrabble.*;

public class Tile {


    char letter;
    int point;
    int id;
    
    public Tile(int _id)
    {
    	id = _id;
        letter = Constants.tileLetter[id];
        point = Constants.tilePoint[id];
        
    }
    
    public Tile(Tile t)
    {
    	letter = t.letter;
    }
    
    // set letter
    public void setLetter(char l)
    {
    	letter = l;
    }
    
    public String toString()
    {
    	return "" + letter;
    }
    public int getID ()
    {
        return id;
    }
}
