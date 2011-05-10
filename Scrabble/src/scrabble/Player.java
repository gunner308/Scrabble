package scrabble;

import java.util.Vector;
import scrabble.game.Tile;

public class Player {
    String username;
    int score;
    Vector<Tile> rack;
    boolean resign;
    

    public Player(String _username)
    {
        username = _username;
        score = 0;
        
    }

    public String getUsername()
    {
        return username;
    }

    public void addScore(int _score) {

        score += _score;
    }

    public boolean resigned()   {

        return resign;
    }

    public void setResign(boolean _resign){

        resign = _resign;
        
    }

    public void addTile(Tile tile)   {

        rack.add(tile);
    }

    public Vector<Tile> getRack()   {

        return rack;
    }


}
