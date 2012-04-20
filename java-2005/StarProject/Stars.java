import java.awt.Color;

public class Stars extends JTerminal
{
	public void run()
	{
		setPrintColor(Color.white);
		drawDiag(0,0);
	}
	

	public void drawDiag(int x, int y)
	{
		if(x != getXSpaces() && y != getYSpaces())
		{
			gotoxy(x,y);
			print("*");
			gotoxy(getXSpaces()-x,y);
			print("*");
			drawDiag(x+1,y+1);
		}
	}
}