/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Game;
import java.util.*;
import Player;

/**
 *
 * @author Duong Minh Nguyen
 */
public class Game {
    private final int size = 10;
    private Square [][]board;
    private Dictionary dictionary;
    private ScoreBoard scoreBoard;
    private int turn;
    private Bag bag;
    private Vector<Player> playerList;

    public void startGame()
    {

    }

    public void finishGame()
    {


    }

    public boolean canStart()
    {
        return true;
    }
}
