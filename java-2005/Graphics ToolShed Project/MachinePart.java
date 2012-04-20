import java.awt.Color;

public class MachinePart extends GamePiece
{
	private boolean broken;
	
	public MachinePart(){}
	
	public MachinePart(char piece, int x, int y, Color colorPiece, String imagePath)
	{
		super(piece,x,y,colorPiece,imagePath);
	}
	
	public void setBroken(boolean broken)
	{
		this.broken = broken;
	}
	
	public boolean getBroken(){return broken;}
}