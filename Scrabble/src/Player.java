/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Vector;
import Game.*;

/**
 *
 * @author Duong Minh Nguyen
 */
public class Player {
    private String username;
    private int score;
    private Vector<Tile> rack;
    private int status;



    public Player(String _username)
    {
        username = _username;
        score = 0;
        status = ''
    }
}
