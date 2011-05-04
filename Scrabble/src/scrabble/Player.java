package scrabble;

import java.util.Vector;
import scrabble.game.Tile;

public class Player {
    String username;
    int score;
    Vector<Tile> rack;
    int status;

    public Player(String _username)
    {
        username = _username;
        score = 0;
        status = Constants.NOT_READY;
    }

    public void setStatus(int i)    {

        status = i;
    }

    public String getUsername() {

    public String getUserame()
    {
        return username;
    }

    public void addScore(int _score) {

        score += _score;
    }

    
}
