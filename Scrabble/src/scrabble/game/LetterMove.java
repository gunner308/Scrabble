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
        public LetterMove (String lt, String _x, String _y)
        {
            tile.id = ((int)lt.toCharArray()[0]-48)*10 + (int)lt.toCharArray()[1]-48;
            x = ((int)_x.toCharArray()[0]-48) * 10 + (int) _x.toCharArray()[1];
            y = ((int)_y.toCharArray()[0]-48) * 10 + (int) _y.toCharArray()[1];
        }

}
