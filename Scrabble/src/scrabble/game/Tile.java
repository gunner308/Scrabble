package scrabble.game;


public class Tile {
    char letter;
    
    public Tile(char l)
    {
    	letter = l;
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
}
