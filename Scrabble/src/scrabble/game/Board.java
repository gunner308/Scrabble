package scrabble.game;

import java.util.Vector;
import java.lang.Math;
import scrabble.Constants;
import sun.awt.VerticalBagLayout;

  class Position{
        public int x;
        public int y;
        public Position (int _x, int _y)
        {
            x = _x;
            y = _y;
        }

      public Position (Position pos)
      {
          x = pos.x;
          y = pos.y;
      }

    }

public class Board {
    private Square board[][];
    private static final int size = 15;
    private Vector <String> wordsToCheck;
    private Vector <Position> initPos;
   
    public Board()
    {
            board = new Square[size][size];
    }

    // get a square
    public Square getSquare(int i, int j)
    {
            return board[i][j];
    }

    // update a move to the board
    public void update(Vector<LetterMove> move)
    {
        for (int i=0; i<move.size(); i++)
        {
            board[move.elementAt(i).x][move.elementAt(i).y].tile = move.elementAt(i).tile;
        }
    }
    private int findMin (Vector <LetterMove> currentMove)
    {
        int min = 0;
        if (currentMove.elementAt(0).x == currentMove.elementAt(1).x)
        {
            min = currentMove.elementAt(0).y;
            for (int i=1; i< currentMove.size(); i++)
            {
                if (currentMove.elementAt(i).y < min) min = currentMove.elementAt(i).y;
            }
        }
        else
        {
            min = currentMove.elementAt(0).x;
            for (int i=1; i< currentMove.size(); i++)
            {
                if (currentMove.elementAt(i).x < min) min = currentMove.elementAt(i).x;
            }
        }
        return min;
    }
    private int findMax (Vector <LetterMove> currentMove)
    {
        int max = 0;
        if (currentMove.elementAt(0).x == currentMove.elementAt(1).x)
        {
            max = currentMove.elementAt(0).y;
            for (int i=1; i< currentMove.size(); i++)
            {
                if (currentMove.elementAt(i).y > max) max = currentMove.elementAt(i).y;
            }
        }
        else
        {
            max = currentMove.elementAt(0).x;
            for (int i=1; i< currentMove.size(); i++)
            {
                if (currentMove.elementAt(i).x > max) max = currentMove.elementAt(i).x;
            }
        }
        return max;
    }

    public int isLine(Vector<LetterMove> currentMove)
    {
        //move has only 1 tile
        if (currentMove.size() == 1) return 1;
        //move has 2 tiles
        else if (currentMove.size() == 2)
        {
            if (currentMove.elementAt(0).x == currentMove.elementAt(1).x 
                && Math.abs(currentMove.elementAt(0).y - currentMove.elementAt(1).y) ==1 ) 
            {
                return 1;
            }
            if (currentMove.elementAt(0).y == currentMove.elementAt(1).y
                && Math.abs(currentMove.elementAt(0).x - currentMove.elementAt(1).x) ==1)
            {
                return 2;
            }
            return 0;
        }
        //more than 2 tiles in a move
        if (currentMove.elementAt(0).x == currentMove.elementAt(1).x)
        {
            for (int i=2; i<currentMove.size(); i++)
            {
                if (currentMove.elementAt(i).x != currentMove.elementAt(0).x) return 0;
            }
            for (int i = findMin(currentMove); i<=findMax(currentMove); i++)
            {
                if (!board [currentMove.elementAt(0).x][i].isOccupied()) return 0;
            }
            return 1;
        }
        else if (currentMove.elementAt(0).y == currentMove.elementAt(1).y)
        {
            for (int i=2; i<currentMove.size(); i++)
            {
                if (currentMove.elementAt(i).y != currentMove.elementAt(0).y) return 0;
            }
            for (int i = findMin(currentMove); i<=findMax(currentMove); i++)
            {
                if (!board [i][currentMove.elementAt(0).y].isOccupied()) return 0;
            }
            return 2;
        }
        else return 0;
    }

    private String makeMainWord (Vector<LetterMove> currentMove)
    {
        String s = "";
        if (currentMove.size() == 1)
        {
            String tmp = "";
            int i = currentMove.elementAt(0).y - 1;
            while (i >= 0 && board[currentMove.elementAt(0).x][i].isOccupied())
            {
                tmp += board[currentMove.elementAt(0).x][i].tile.letter;
                if ((i>0 && 
                    !board[currentMove.elementAt(0).x][i-1].isOccupied()) || i==0) 
                {
                    Position p = new Position (currentMove.elementAt(0).x, i);
                    initPos.add(p);
                }
                i--;
            }
            for (int j=tmp.length()-1; j>=0; j--)
            {
                s += tmp.charAt(j);
            }
            int j = currentMove.elementAt(0).x;
            while (j<size && board[currentMove.elementAt(0).x][j].isOccupied())
            {
                s += board[currentMove.elementAt(0).x][j].tile.letter;
                j++;
            }
        }
        else
        {
            if (currentMove.elementAt(0).x == currentMove.elementAt(1).x)
            {
                String tmp = "";
                int it = findMin(currentMove) - 1;
                while (it >= 0 && board[currentMove.elementAt(0).x][it].isOccupied())
                {
                    tmp += board[currentMove.elementAt(0).x][it].tile.letter;
                    if ((it>0 && 
                    !board[currentMove.elementAt(0).y][it-1].isOccupied()) || it==0) 
                    {
                        Position p = new Position (currentMove.elementAt(0).x, it);
                        initPos.add(p);
                    }
                    it--;
                }
                for (int j=tmp.length()-1; j>=0; j--)
                {
                    s += tmp.charAt(j);
                }
                for (int i= findMin(currentMove); i<=findMax(currentMove); i++)
                {
                    s += board[currentMove.elementAt(0).x][i].tile.letter;
                }
                int j = currentMove.elementAt(0).y+1;
                while (j<size && board[j][currentMove.elementAt(0).y].isOccupied())
                {
                    s += board[currentMove.elementAt(0).x][j].tile.letter;
                    j++;
                }
            }
            else
            {
                String tmp = "";
                int it = findMin(currentMove) - 1;
                while (it >= 0 && board[it][currentMove.elementAt(0).y].isOccupied())
                {
                    tmp += board[it][currentMove.elementAt(0).y].tile.letter;
                    if ((it>0 && 
                        !board[it-1][currentMove.elementAt(0).y].isOccupied()) || it==0) 
                    {
                        Position p = new Position (it, currentMove.elementAt(0).y);
                        initPos.add(p);
                    }
                    it--;
                }
                for (int j=tmp.length()-1; j>=0; j--)
                {
                    s += tmp.charAt(j);
                }
                for (int i= findMin(currentMove); i<=findMax(currentMove); i++)
                {
                    s += board[i][currentMove.elementAt(0).y].tile.letter;
                }
                int j = currentMove.elementAt(0).x+1;
                while (j<size && board[j][currentMove.elementAt(0).y].isOccupied())
                {
                    s += board[j][currentMove.elementAt(0).y].tile.letter;
                    j++;
                }
            }
        }
        return s;
    }
    private void makeSecondaryWord (Vector<LetterMove> m)
    {
        String s="";
        if (isLine(m) == 1)
        {
            for (int i=0; i<m.size(); i++)
            {
                String tmp = "";
                int it = m.elementAt(0).x - 1;
                while (it >= 0 && board[it][m.elementAt(i).y].isOccupied())
                {
                    tmp += board[it][m.elementAt(i).y].tile.letter;
                    if ((it>0 && 
                        !board[it-1][m.elementAt(i).y].isOccupied()) || it==0) 
                    {
                        Position p = new Position (it, m.elementAt(i).y);
                        initPos.add(p);
                    }
                    it--;
                }
                for (int j=tmp.length()-1; j>=0; j--)
                {
                    s += tmp.charAt(j);
                }
                int j = m.elementAt(i).y;
                while (j<size && board[m.elementAt(i).x][j].isOccupied())
                {
                    s += board[j][m.elementAt(i).y].tile.letter;
                    j++;
                }
            }
            wordsToCheck.add(s);
        }
        else if (isLine(m) == 2)
        {
            for (int i=0; i<m.size(); i++)
            {
                String tmp = "";
                int it = m.elementAt(i).y - 1;
                while (i >= 0 && board[m.elementAt(i).x][it].isOccupied())
                {
                    tmp += board[m.elementAt(i).x][it].tile.letter;
                    if ((it>0 && 
                        !board[m.elementAt(i).x][it-1].isOccupied()) || it==0) 
                    {
                        Position p = new Position (m.elementAt(i).x, it);
                        initPos.add(p);
                    }
                    it--;
                }
                for (int j=tmp.length()-1; j>=0; j--)
                {
                    s += tmp.charAt(j);
                }
                int j = m.elementAt(0).y;
                while (j<size && board[m.elementAt(i).x][j].isOccupied())
                {
                    s += board[m.elementAt(i).x][j].tile.letter;
                    j++;
                }
            }
            wordsToCheck.add(s);
        }
    }
    public Vector<String> getWords (Vector <LetterMove> currentMove)
    {
        wordsToCheck.add(makeMainWord(currentMove)); 
        makeSecondaryWord(currentMove);
        return wordsToCheck;
    }
    public Vector <Position> getPos (Vector<LetterMove> currentMove)
    {
        return initPos;
    }
    public boolean checkNewLetter (Position p)
    {
        if (board[p.x][p.y].isOccupied()) return false;
        return true;
    }
}
