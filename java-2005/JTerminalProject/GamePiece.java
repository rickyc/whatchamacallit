import java.awt.Color;

public class GamePiece
{
	public static final int UP = 'w', DOWN = 's', LEFT = 'a', RIGHT = 'd', SPACE = 32, BACKSPACE = 8;
	
	protected char piece;
	protected int x,y;
	protected Color colorPiece;
	protected boolean isX, passable;
	
	public GamePiece(){}
	
	public GamePiece(char piece, int x, int y, Color colorPiece, boolean passable)
	{
		this.piece = piece;
		this.x = x;
		this.y = y;
		this.colorPiece = colorPiece;
		this.passable = passable;
	}
			
	public void setXY(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setPassable(boolean passable)
	{
		this.passable = passable;
	}
	
	public boolean isX(){return isX;}
	public int getX(){return x;}
	public int getY(){return y;}
	public char getPiece(){return piece;}
	public Color getColor(){return colorPiece;}
	public String toString(){return piece + "";}
	public boolean isPassable(){return passable;}
}