package scrabble.game;


public class Tile {
    char letter;
    int point;
    
    public Tile(char l, int p)
    {
    	letter = l;
    	point = p;
    }
    
    // set letter
    public void setLetter(char l)
    {
    	letter = l;
    }
}
