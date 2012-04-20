import java.awt.Color;

public class Machine extends MachinePart
{
	private MachinePart[][] machineParts;
	private int startX, startY;
	private boolean machineBroken;
	
	public Machine(){}
	
	public Machine(int startX, int startY, int tool)
	{
		this.startX = startX;
		this.startY = startY;
		
		machineParts = new MachinePart[3][4];
		
		for(int y=startY;y<startY+4;y++)
			for(int x=startX;x<startX+3;x++)
				machineParts[x-startX][y-startY] = getTool(tool,x,y);
	}

	
	public Machine(int startX, int startY)
	{
		this.startX = startX;
		this.startY = startY;
		
		machineParts = new MachinePart[3][4];
		
		for(int y=startY;y<startY+4;y++)
			for(int x=startX;x<startX+3;x++)
				machineParts[x-startX][y-startY] = getTool((int)(Math.random()*6),x,y);
	}
	
	public static int getMachine(int x)
	{
		int machine = -99;
		
		if(x >= 0 && x <= 3)
			machine = 0;	
		else
			if(x >= 5 && x <= 7)
				machine = 1;
			else
				if(x >= 10 && x <= 12)
					machine = 2;
				else
					if(x >= 17 && x <= 19)
						machine = 3;
					else
						if(x >= 22 && x <= 24)
							machine = 4;
						else
							if(x >= 27 && x <= 29)
								machine = 5;
			
		return machine;
	}
	
	public void checkBroken()
	{
		boolean brokenPart = false;
		
		for(int x=0;x<3;x++)
			for(int y=0;y<4;y++)
				if(!machineParts[x][y].getBroken())
					brokenPart = true;

		if(!brokenPart)
			machineBroken = true;
	}
	
	public void breakPiece()
	{
		if(!machineBroken)
		{
			int x = 0, y = 0;
			
			do
			{
				x = (int)(Math.random()*3);
				y = (int)(Math.random()*4);	
			}while(machineParts[x][y].getBroken());
			
			machineParts[x][y].setBroken(true);
		}
		checkBroken();
	}
	
	
	public void fixPiece(int x, int y)
	{
		machineParts[x-startX][y-startY].setBroken(false);
		machineBroken = false;
	}
	
	public MachinePart getTool(int tool, int x, int y)
	{
		MachinePart mp = null;
		switch(tool)
		{
			case 0: mp = new MachinePart(Tool.SCREWDRIVER,x,y,Color.blue,"screwdriver.gif"); break;
			case 1: mp = new MachinePart(Tool.HAMMER,x,y,Color.white,"hammer.gif"); break;
			case 2: mp = new MachinePart(Tool.PICK,x,y,Color.cyan,"pick.gif"); break;
			case 3: mp = new MachinePart(Tool.WRENCH,x,y,Color.green,"wrench.gif"); break;
			case 4: mp = new MachinePart(Tool.LIGHTBULB,x,y,Color.yellow,"bulb.gif"); break;
			case 5: mp = new MachinePart(Tool.DRILL,x,y,Color.pink,"drill.gif"); break;
		}
		return mp;
	}
	
	public void setMachineBroken(boolean machineBroken)
	{
		this.machineBroken = machineBroken;
	}
	
	public boolean getMachineBroken(){return machineBroken;}
	public int getX(){return startX;}
	public int getY(){return startY;}
	public MachinePart getMachinePart(int x, int y){return machineParts[x-startX][y-startY];}
}