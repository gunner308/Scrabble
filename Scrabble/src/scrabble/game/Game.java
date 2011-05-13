package scrabble.game;

import java.util.Vector;
import scrabble.Constants;
import scrabble.Player;

public class Game {
    private final int size = 10;
    private Board board;
    private Dictionary dictionary;
    private int turn;
    private Bag bag;
    private Vector<Player> playerList;
    private Vector<LetterMove> currentMove;
    private Vector<Position> startPos;
    Vector<String> words;
    private boolean isStarted;
    private int dir;
    
    public Game()
    {
    	board = new Board();
    	dictionary = new Dictionary();
    	bag = new Bag();
        isStarted = false;
        turn = -1;
    }
    
    public void startGame()
    {
    	isStarted = true;
    }

    public boolean endGame()
    {
        int count = 0;
        for (int i=0; i<playerList.size(); i++)
        {
            if (playerList.elementAt(i).resigned()) count++;
        }

        return (count>1);
    }

    public boolean checkWord()
    {
        dir = board.isLine(currentMove);
        if (dir == 0) return false;
        words = board.getWords(currentMove);

        for (int i=0; i<words.size(); i++)
        {
            if (!dictionary.checkWord(words.elementAt(i))) return false;
        }
        return true;
    }

    public boolean canStart()
    {
        return true;
    }

    public boolean isStarted()
    {
        return isStarted;
    }

    public void setGameStt (boolean _isStarted)
    {
        isStarted = _isStarted;
    }

    public void addPlayer(Player p)
    {
        playerList.add(p);
    }

    public Vector<Player> getPlayerList ()
    {
        return playerList;
    }

    public Vector<Tile> getNewTiles()
    {
        int size = playerList.elementAt(turn).getRack().size();
        bag.fillRack(playerList.elementAt(turn).getRack());
        Vector<Tile> newTiles = new Vector();
        Vector<Tile> rack = playerList.elementAt(turn).getRack();
        for (int i=size; i<rack.size(); i++)
        {
            newTiles.add(rack.elementAt(i));
        }

        return newTiles;
    }

    public String nextTurn ()
    {
        board.update(currentMove);
        currentMove.clear();
        if (turn == -1)
            turn = (int)(System.currentTimeMillis() * 257) % playerList.size();
        else
        {
            while (playerList.elementAt((turn++) % playerList.size()).resigned());
        }
        return playerList.elementAt(turn).getUsername();
    }

    // calculate score
    private int marking(String word, Position pos, int dir, int mod)
    {
        int ans = 0;
        Square cur;
        Position curPos = new Position(pos);
        int modifier = 1;

        for (int i=0; i<word.length(); i++)
        {
            cur = board.getSquare(curPos.x, curPos.y);
            switch (cur.type)
            {
                case (Constants.NORMAL):
                    ans += Constants.getPoint(word.charAt(i));
                    break;
                case (Constants.X2LETTER):
                    if (board.checkNewLetter(pos))
                        ans += Constants.getPoint(word.charAt(i));
                    ans += Constants.getPoint(word.charAt(i));
                    break;
                case (Constants.X3LETTER):
                    if (board.checkNewLetter(pos))
                        ans += (Constants.getPoint(word.charAt(i)) * 2);
                    ans += Constants.getPoint(word.charAt(i));
                    break;
                case (Constants.X2WORD):
                    if (board.checkNewLetter(pos))
                        modifier *= 2;
                    ans += Constants.getPoint(word.charAt(i));
                    break;
                case (Constants.X3WORD):
                    if (board.checkNewLetter(pos))
                        modifier *= 3;
                    ans += Constants.getPoint(word.charAt(i));
                    break;
                default: break;
            }
            if (dir == 1){
                curPos.y ++;
            }
            else curPos.x ++;
        }
        if (mod == 0) return ans;
        return ans * modifier;
    }
    
    // calculate score of current move
    public int calculateScore()
    {
        int point = 0;
        startPos = board.getPos(currentMove);
        point = marking(words.elementAt(0), startPos.elementAt(0), dir, 1);
        for (int i=1; i<words.size(); i++)
        {
            point += marking(words.elementAt(i), startPos.elementAt(0), 3-dir, 0);
        }
    	return point;
    }

    public Vector<Tile> exchangeRack()
    {
        bag.exchangeRack(playerList.elementAt(turn).getRack());
        return playerList.elementAt(turn).getRack();
    }

    public void updateMove(LetterMove move)
    {
        currentMove.add(move);
    }
}
