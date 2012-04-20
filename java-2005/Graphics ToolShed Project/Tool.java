import java.awt.Color;

public class Tool extends GamePiece
{
	public static final char SCREWDRIVER = 'S', HAMMER = 'H', PICK = 'P', WRENCH = 'W', LIGHTBULB = 'L', DRILL = 'D';
	private long timer;
	
	public Tool(){}
	
	public Tool(char piece, Color colorPiece, String imagePath)
	{
		this.piece = piece;
		this.colorPiece = colorPiece;
		this.imagePath = imagePath;
	}
	
	public Tool(char piece, int x, int y, Color colorPiece, String imagePath)
	{
		super(piece,x,y,colorPiece,imagePath);
	}
	
	public void setTimer(long timer)
	{
		this.timer = timer;
	}
	
	public long getTimer(){return timer;}
	
	public static Tool getTool(int tool)
	{
		Tool t = null;
		switch(tool)
		{
			case 0: t = new Tool(SCREWDRIVER,Color.blue,"screwdriver.gif"); break;
			case 1: t = new Tool(HAMMER,Color.white,"hammer.gif"); break;
			case 2: t = new Tool(PICK,Color.cyan,"pick.gif"); break;
			case 3: t = new Tool(WRENCH,Color.green,"wrench.gif"); break;
			case 4: t = new Tool(LIGHTBULB,Color.yellow,"bulb.gif"); break;
			case 5: t = new Tool(DRILL,Color.pink,"drill.gif"); break;
		}
		return t;
	}
}