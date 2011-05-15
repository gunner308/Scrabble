package scrabble.game;

import java.util.Vector;
import java.lang.Math;
import scrabble.Constants;

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
    private Vector <String> wordsToCheck = new Vector <String>();
    private Vector <Position> initPos = new Vector <Position>();
    private static final int BOARD[][] = 
    	{{4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4,},
    	 {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0,},
    	 {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0,},
    	 {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1,},
    	 {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0,},
    	 {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0,},
    	 {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0,},
    	 {4, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 4,},
    	 {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0,},
    	 {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0,},
    	 {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0,},
    	 {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1,},
    	 {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0,},
    	 {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0,},
    	 {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4,}
    	};
    
    
    public Board()
    {
    	board = new Square[size][size];
    	for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = new Square(BOARD[i][j]);
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
    //find the min X if word is vertical, min Y if word is horizontal
    private int findMin (Vector <LetterMove> currentMove)
    {
        int min = 0;
        if (currentMove.size() >= 2)
        {
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
        }
        return min;
    }
    //find max min X if word is vertical, max Y if word is horizontal
    private int findMax (Vector <LetterMove> currentMove)
    {
        int max = 0;
        if (currentMove.size() >= 2)
        {
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
        }
        return max;
    }
    //check if the NEW tiles is in one row or one column and connect with each others
    private int isOneLine(Vector<LetterMove> currentMove)
    {
        if (currentMove.size() == 1) return 1;
        //more than 2 tiles in a move
        else
        {
            if (currentMove.elementAt(0).x == currentMove.elementAt(1).x)
            {
                System.out.println("board: check ngang");
                for (int i=2; i<currentMove.size(); i++)
                {
                    if (currentMove.elementAt(i).x != currentMove.elementAt(0).x) 
                    {       
                        return 0;
                    }
                }
                System.out.println("board: ngang");
                return 1;
            }
            else if (currentMove.elementAt(0).y == currentMove.elementAt(1).y)
            {
                for (int i=2; i<currentMove.size(); i++)
                {
                    if (currentMove.elementAt(i).y != currentMove.elementAt(0).y) 
                    {
                        return 0;
                    }
                }
                return 2;
            }
        }
        return 0;
    }
    private boolean preOccupied (Position p, Vector <LetterMove> m)
    {
        for (int i=0; i<m.size(); i++)
        {
            if (m.elementAt(i).x == p.x && m.elementAt(i).y == p.y) 
            {
                return true;
            }
        }
        return false;
    }
    private boolean firstWord ()
    {
        for (int i=0; i < size; i++)
        {
            for (int j=0; j<size; j++)
            {
                if (board[i][j].isOccupied()) return false;
            }
        }
        return true;
    }
    //check if the new tiles connect with old tiles
    public boolean checkConnected (Vector <LetterMove> m)
    {
        if (!board[7][7].isOccupied() && !preOccupied(new Position (7, 7), m)) return false;
        if (m.size() == 1)
        {
            if (m.elementAt(0).x > 0)
            {
                if (board[m.elementAt(0).x-1][m.elementAt(0).y].isOccupied()) return true;
            }
            if (m.elementAt(0).x < 14)
            {
                if (board[m.elementAt(0).x+1][m.elementAt(0).y].isOccupied()) return true;
            }
            if (m.elementAt(0).y > 0)
            {
                if (board[m.elementAt(0).x-1][m.elementAt(0).y-1].isOccupied()) return true;
            }
            if (m.elementAt(0).y < 14)
            {
                if (board[m.elementAt(0).x-1][m.elementAt(0).y+1].isOccupied()) return true;
            }
        }
        else
        {
            if (isOneLine(m) == 1)
            {
                int start, end, x = m.elementAt(0).x;
                start = findMin(m);
                end = findMax(m);

                boolean ans = false;
                if (start>0)
                    if (board[x][start-1].isOccupied()) ans = true;
                if (end<14)
                    if (board[x][end+1].isOccupied()) ans = true;
                
                System.out.println("board: start - " + start + " end - " + end);

                while (start <= end)
                {
                    System.out.println ("vao while ngang");
                    if (board[x][start].isOccupied()) ans = true;
                    if (x>0)
                        if (board[x-1][start].isOccupied()) ans = true;
                    if (x<14)
                        if (board[x+1][start].isOccupied()) ans = true;

                    if (!board[x][start].isOccupied()
                        && !preOccupied(new Position (x, start), m))
                        {
                            System.out.println ("board: false x " + x + "-" +start);
                            return false;
                        }
                    start ++;
                }
                if (ans) return true;
            }
            else
            {
               int start, end, y = m.elementAt(0).y;
                start = findMin(m);
                end = findMax(m);

                boolean ans = false;
                if (start>0)
                    if (board[start-1][y].isOccupied()) ans = true;
                if (end<14)
                    if (board[end+1][y].isOccupied()) ans = true;

                System.out.println("board: start - " + start + " end - " + end);

                while (start <= end)
                {
                    System.out.println ("vao while ngang");
                    if (board[start][y].isOccupied()) ans = true;
                    if (y>0)
                        if (board[start][y-1].isOccupied()) ans = true;
                    if (y<14)
                        if (board[start][y+1].isOccupied()) ans = true;

                    if (!board[start][y].isOccupied()
                        && !preOccupied(new Position (start,y), m))
                        {
                            System.out.println ("board: false y " + start + "-" + y);
                            return false;
                        }
                    start ++;
                }
                if (ans) return true;
            }
        }
        return true;
    }
    
    public int isLine (Vector <LetterMove> currentMove)
    {
        if (isOneLine(currentMove) == 0) 
        {
            System.out.println ("one line bang 0");
            return 0;
        }     
        else
        {
            System.out.println ("one line khac 0");
            if (checkConnected(currentMove)) 
            {
                System.out.println ("connected");
                int x = isOneLine(currentMove);
                System.out.println ("return: "+ x);
                return x;
            }//if connect
            else 
            {
                System.out.println ("not connected");
                return 0;
            }//not connect
        }
    }
    
    //make the word correspoding to the main direction 
    private void makeMainWord (Vector<LetterMove> currentMove)
    {

        if (currentMove.size() == 1)
        {
            String tmp = "";
            String s = "";
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

            wordsToCheck.add(s.toLowerCase());
        }
        else
        {
            if (isLine (currentMove) == 1)
            {
                String tmp = "";
                int it = findMin(currentMove);
                int x = currentMove.elementAt(0).x;
                while (it>0 && board[x][it-1].isOccupied()) it--;
                initPos.add(new Position(x, it));

                while (board[x][it].isOccupied() || preOccupied(new Position(x, it),currentMove))
                {
                    if (board[x][it].isOccupied()) tmp += board[x][it].getTile().letter;
                    else 
                    {
                        for (int i=0; i<currentMove.size(); i++)
                        {
                            if (currentMove.elementAt(i).x == x && currentMove.elementAt(i).y == it)
                            {
                                tmp += currentMove.elementAt(i).tile.letter;
                            }
                        }
                    }
                    it++;
                }

                wordsToCheck.add(tmp.toLowerCase());
            }
            else if (isLine(currentMove) == 2)
            {
                String tmp = "";
                int it = findMin(currentMove);
                int y = currentMove.elementAt(0).y;
                while (it>0 && board[it-1][y].isOccupied()) it--;
                initPos.add(new Position(it, y));

                while (board[it][y].isOccupied() || preOccupied(new Position(it, y),currentMove))
                {
                    if (board[it][y].isOccupied()) tmp += board[it][y].getTile().letter;
                    else
                    {
                        for (int i=0; i<currentMove.size(); i++)
                        {
                            if (currentMove.elementAt(i).y ==y && currentMove.elementAt(i).x == it)
                            {
                                tmp += currentMove.elementAt(i).tile.letter;
                            }
                        }
                    }
                    it++;
                }

                wordsToCheck.add(tmp.toLowerCase());
            }
        }
    }
    //add the secondary word to the vector
    private void makeSecondaryWord (Vector<LetterMove> m)
    {
        String s="";
        //horizontal
        if (isLine(m) == 1)
        {
            for (int i=0; i<m.size(); i++)
            {
                String tmp = "";
                int it = m.elementAt(i).x;
                int y = m.elementAt(i).y;
                while (it > 0 && board[it-1][y].isOccupied())
                {
                    it--;
                }
                initPos.add(new Position(it, y));
                while (it < 15 && board[it][y].isOccupied() || preOccupied(new Position(it, y), m))
                {
                    if (board[it][y].isOccupied()) tmp += board[it][y].getTile().letter;
                    else
                    {
                        for (int j=0; j<m.size(); j++)
                        {
                            if (m.elementAt(j).y ==y && m.elementAt(j).x == it)
                            {
                                tmp += m.elementAt(j).tile.letter;
                            }
                        }
                    }
                    it++;
                }
                if (tmp.length()>1)
                {
                    wordsToCheck.add(tmp.toLowerCase());
                }
            }
        }
        //vertical
        else if (isLine(m) == 2)
        {
            for (int i=0; i<m.size(); i++)
            {
                String tmp = "";
                int it = m.elementAt(i).y;
                int x = m.elementAt(i).x;
                while (it > 0 && board[x][it-1].isOccupied())
                {
                    it--;
                }
                initPos.add(new Position(x, it));
                while (it < 15 && board[x][it].isOccupied() || preOccupied(new Position(x, it), m))
                {
                    if (board[x][it].isOccupied()) tmp += board[x][it].getTile().letter;
                    else
                    {
                        for (int j=0; j<m.size(); j++)
                        {
                            if (m.elementAt(j).x == x && m.elementAt(j).y == it)
                            {
                                tmp += m.elementAt(j).tile.letter;
                            }
                        }
                    }
                    it++;
                }
                if (tmp.length()>1)
                {
                    wordsToCheck.add(tmp.toLowerCase());
                }
            }
        }
    }
    public Vector<String> getWords (Vector <LetterMove> currentMove)
    {
        wordsToCheck.clear();
        System.out.println ("Size vector words truoc: "+wordsToCheck.size());
        makeMainWord(currentMove); 
        makeSecondaryWord(currentMove);
        System.out.println ("Size vector words sau: "+wordsToCheck.size());
        return wordsToCheck;
    }
    public Vector <Position> getPos (Vector<LetterMove> currentMove)
    {
        Vector <Position> res = new Vector(initPos);
        System.out.println("board: initPos's size " + initPos.size());
        initPos.clear();
        return res;
    }
    public boolean checkNewLetter (Position p)
    {
        if (board[p.x][p.y].isOccupied()) return false;
        return true;
    }
    
}
