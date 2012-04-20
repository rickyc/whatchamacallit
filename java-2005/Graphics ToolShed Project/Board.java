// ------------------------------------------------
// | Board Class |
// ---------------
// This class handles the displaying of the pieces
// ------------------------------------------------

import java.awt.Color;

public class Board extends JTerminal
{
	private GamePiece board[][];
	
	// default constructor
	public Board()
	{
		toFront();
		setTitle(".:Tool Shed:.");
		board = new GamePiece[getXSpaces()][getYSpaces()];
		
		for(int x=0;x<getXSpaces();x++)
			for(int y=0;y<getYSpaces();y++)
				setBlank(x,y);		
	}
	
	// this method displays all the pieces of the board
	public void displayBoard()
	{
		for(int y=0;y<getYSpaces();y++)
			for(int x=0;x<getXSpaces();x++)
			//	printPiece(x,y);
				drawPiece(x,y);
	}
	
	// draws one piece onto the board (images)
	public void drawPiece(int x, int y)
	{
		drawImage(board[x][y].getImagePath(),x,y);

		if(getPiece(x,y) instanceof MachinePart)
			if(((MachinePart)getPiece(x,y)).getBroken())
				drawImage("broken_"+board[x][y].getImagePath(),x,y);
	}
	
	// prints one piece onto the board (chars)
	public void printPiece(int x, int y)
	{
		gotoxy(x,y);
		setPrintColor(board[x][y].getColor());
		
		if(getPiece(x,y) instanceof MachinePart)
			if(((MachinePart)getPiece(x,y)).getBroken())
				setPrintColor(Color.gray);
			
		print(board[x][y].getPiece());
	}
	
	// sets a blank into the board
	public void setBlank(int x, int y)
	{
		board[x][y] = new GamePiece(' ',x,y,Color.black,"");
		drawImage("blank.gif",x,y);
	//	printPiece(x,y);
	}
	
	// checks if the piece is valid or not
	public boolean isValid(GamePiece g, int x, int y)
	{
		boolean valid = true;

		if(x<0 || x>getXSpaces()-1 || y<17 || y>23)
			valid = false;

		return valid;
	}
	
	// sets a piece on to the board
	public void setPiece(GamePiece gp)
	{
		setPrintColor(gp.getColor());
		board[gp.getX()][gp.getY()] = gp;
	}
	
	// another method to set the piece onto the board
	public void setPiece(GamePiece gp, int x, int y)
	{
		setPrintColor(gp.getColor());
		board[x][y] = gp;
	}
	
	// accessors
	public GamePiece getPiece(int x, int y){return board[x][y];}
}