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
        public LetterMove (char lt, String _x, String _y)
        {
            tile.letter = lt;
            x = ((int)_x.toCharArray()[0]-48) * 10 + (int) _x.toCharArray()[1];
            y = ((int)_y.toCharArray()[0]-48) * 10 + (int) _y.toCharArray()[1];
        }

}
