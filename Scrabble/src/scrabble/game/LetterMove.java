package scrabble.game;

public class LetterMove {
	public int x, y;
	Tile tile;
	
	public LetterMove(int _x, int _y, Tile t)
	{
		x = _x;
		y = _y;
		tile = t;
	}
        public LetterMove(int _x, int _y, int tileID)   {

                x = _x;
                y = _y;
                tile = new Tile(tileID);
        }

}
