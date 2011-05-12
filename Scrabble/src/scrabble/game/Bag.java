package scrabble.game;

import java.util.*;

public class Bag {
    private Vector<Tile> tileList = new Vector<Tile>();
    Random generator = new Random();
    private final char bag[] = {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'B', 
                                'B', 'C', 'C', 'D', 'D', 'D', 'D', 'E', 'E', 'E', 
                                'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'F',
                                'F', 'G', 'G', 'G', 'H', 'H', 'I', 'I', 'I', 'I', 
                                'I', 'I', 'I', 'I', 'I', 'J', 'K', 'L', 'L', 'L',
                                'L', 'M', 'M', 'N', 'N', 'N', 'N', 'N', 'N', 'O',
                                'O', 'O', 'O', 'O', 'O', 'O', 'O', 'P', 'P', 'Q',
                                'R', 'R', 'R', 'R', 'R', 'R', 'S', 'S', 'S', 'S',
                                'T', 'T', 'T', 'T', 'T', 'T', 'U', 'U', 'U', 'U',
                                'V', 'V', 'W', 'W', 'X', 'Y', 'Y', 'Z', ' ', ' '};
    
    public Bag()
    {
    	for (int i = 0; i < bag.length; i++){
                if ( bag[i] == ' ' )    {
                    tileList.add(new Tile(26));
                }
                else tileList.add(new Tile(bag[i] - 'A'));
    	}
    }
    
    // return the number of tiles left
    public int getNumTileLeft()
    {
        return tileList.size();
    }
    
    // fill in a rack
    public void fillRack(Vector<Tile> rack)
    {
    	int n;
    	while (!isEmpty() && rack.size() < 7){
    		n = generator.nextInt(getNumTileLeft());
    		rack.add(new Tile(tileList.elementAt(n)));
    		tileList.remove(n);
    	}
    }
    
    // exchange letters in rack
    public void exchangeRack(Vector<Tile> rack)
    {
    	for (int i = 0; i < rack.size(); i++){
    		tileList.add(new Tile(rack.elementAt(i)));
    	}
    	rack.clear();
    	fillRack(rack);
    }
    
    // check whether the bag is empty
    public boolean isEmpty()
    {
    	return (getNumTileLeft() == 0);
    }
}
