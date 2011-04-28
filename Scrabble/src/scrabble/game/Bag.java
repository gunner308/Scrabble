package scrabble.game;

import java.util.*;

public class Bag {
    private Vector<Tile> tileList;
    
    public Bag()
    {
    	
    }
    
    // return the number of tiles left
    public int getNumTileLeft()
    {
        return tileList.size();
    }
    
    // fill in a rack
    public void fillRack(Vector<Tile> rack)
    {

    }
    
    // exchange letters in rack
    public void exchangeRack(Vector<Tile> rack)
    {

    }
    
    // check whether the bag is empty
    public boolean isEmpty()
    {
    	return (getNumTileLeft() == 0);
    }
}
