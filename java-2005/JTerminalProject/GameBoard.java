import java.awt.Color;

public class GameBoard extends JTerminal
{
	private GamePiece board[][];
	
	public GameBoard()
	{
		toFront();
		setTitle(".:RPG ASCII GAME:.");
		board = new GamePiece[getXSpaces()][getYSpaces()];
		
		for(int x=0;x<getXSpaces();x++)
			for(int y=0;y<getYSpaces();y++)
				setBlank(x,y);		
	}

	public void setBoard(String fileName)
	{
		IOFile.setFile(fileName);
		IOFile.setIn();
		
		for(int y=0;y<getYSpaces();y++)
		{
			String line = IOFile.readLine();
			char lineArray[] = new char[line.length()];
			
			for(int i=0;i<lineArray.length;i++)
				lineArray[i] = line.charAt(i);
			
			for(int x=0;x<getXSpaces();x++)
			{
				GamePiece g = swapPiece(lineArray[x],x,y);
				board[x][y]	= g;
			}
		}			
	}
	
	public GamePiece swapPiece(char c,int x,int y)
	{
		GamePiece g = new GamePiece();

		switch(c)
		{
			case 'W': g = new GamePiece('*',x,y,Color.white,false); break;
			case 'D': g = new GamePiece('=',x,y,Color.magenta,false); break;
			case 'K': g = new GamePiece('K',x,y,Color.pink,true); break;
			case 'C': g = new GamePiece('$',x,y,Color.yellow,true); break;
			case 'P': g = new GamePiece('O',x,y,Color.cyan,true); break;
			case ' ': g = new GamePiece(' ',x,y,Color.black,true); break;
		}
		
		return g;
	}
	
	public void setBlank(int x, int y)
	{
		board[x][y] = new GamePiece(' ',x,y,Color.black,true);
	}
	
	public void displayBoard()
	{
		for(int y=0;y<getYSpaces();y++)
			for(int x=0;x<getXSpaces();x++)
				printPiece(x,y);
	}
	
	public void printPiece(int x, int y)
	{
		gotoxy(x,y);
		setPrintColor(board[x][y].getColor());
		print(board[x][y].getPiece());
	}
	
	public void saveBoard(String path)
	{
		IOFile.setFile(path);
		IOFile.setOut();
		
		for(int y=0;y<getYSpaces();y++)
		{
			String test = "";
			for(int x=0;x<getXSpaces();x++)
				test+=convertToChar(board[x][y].getPiece());
			IOFile.println(test);	
		}
	}
	
	public char convertToChar(char p)
	{
		char c = ' ';
	
		switch(p)
		{
			case '*':  c = 'W'; break;
			case '=':  c = 'D'; break;
			case 'K':  c = 'K'; break;
			case '$':  c = 'C'; break;
			case 'O':  c = 'P'; break;
			case ' ':  c = ' '; break;
		}
		
		return c;
	}
	
	public boolean isValid(GamePiece g, int x, int y)
	{
		boolean valid = true;

		if(x<0 || x>getXSpaces()-1 || y<0 || y>getYSpaces()-1)
			valid = false;
		else
			if(!board[x][y].isPassable())
				valid = false;
		
		if(g != null)
			if(board[g.getX()][g.getY()] instanceof Enemy)
				if(board[x][y].getPiece() == 'K' || board[x][y].getPiece() == '$')
					valid = false;
	
		return valid;
	}
	
	public void setPiece(GamePiece gp)
	{
		setPrintColor(gp.getColor());
		board[gp.getX()][gp.getY()] = gp;
	}
	
	public GamePiece getGamePiece(int x, int y){return board[x][y];}
}