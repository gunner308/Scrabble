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
    public int getStatus ()
    {
        return status;
    }
    public void setStatus(int stt)
    {
        status = stt;
    }
    public String getUserame()
    {
        return username;
    }
}
