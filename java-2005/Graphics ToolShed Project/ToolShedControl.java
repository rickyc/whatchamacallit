import java.awt.Color;
import java.util.ArrayList;

public class ToolShedControl
{
	private Board board;
	private Conveyer conveyer;
	private GamePiece player;
	private Machine[] machine;
	private ArrayList dropTools;
	private	int conMoveInterval, conShiftInterval, toolDropInterval, fallingInterval;
	
	public ToolShedControl()
	{
		player = new Player(Player.PLAYER,14,17,Color.red,"player.gif");
		conveyer = new Conveyer();
		board = new Board();
		machine = new Machine[6];
		dropTools = new ArrayList();
		initializeMachines();
		printBelt();
		printConveyer();
	}
	
	public void quit()
	{
		for(int i=0;i<machine.length;i++)
			machine[i].setMachineBroken(true);
	}
	
	public boolean gameover()
	{
		for(int i=0;i<machine.length;i++)
		{
			machine[i].checkBroken();
			if(!machine[i].getMachineBroken())
				return false;
		}
		return true;
	}
	
	public void printMachine(int machineIndex)
	{
		Machine m = machine[machineIndex];

		for(int y=m.getY();y<m.getY()+4;y++)
			for(int x=m.getX();x<m.getX()+3;x++)
			{
				board.setPiece(m.getMachinePart(x,y));
				board.drawPiece(x,y);
			}
	}

	public void printBelt()
	{
		for(int x=0;x<5;x++)
			board.setBlank(x,24);
	
		for(int i=0;i<((Player)player).getBelt().getSize();i++)
			board.setPiece((GamePiece)(((Player)player).getBelt().next()),i,24);
	
		for(int i=0;i<5;i++)
			board.drawPiece(i,24);
	}

	public void printConveyer()
	{
		board.drawClearRect(0,40,375,50);
		
		for(int i=0;i<58;i++)
		{
			GamePiece g = (GamePiece)conveyer.next();
			
			if(i>=0 && i<=27)
			{
				g.setXY(28-i,2);
				board.setPiece(g);
				board.drawPiece(28-i,2);
			}
			if(i == 28)
			{
				g.setXY(0,3);
				board.setPiece(g);
				board.drawPiece(0,3);
			}
			if(i>=29 & i<=56) 
			{
				g.setXY(i-28,4);
				board.setPiece(g);
				board.drawPiece(i-28,4);
			}
			if(i == 57)
			{
				g.setXY(29,3);
				board.setPiece(g);
				board.drawPiece(29,3);
			}
		}	
	}
	
	public void initializeMachines()
	{
		for(int i=0;i<machine.length;i++)
		{
			int x=0,y = 13;
				
			switch(i)
			{
				case 1: x = 5; break;
				case 2: x = 10; break;
				case 3: x = 17; break;
				case 4: x = 22; break;
				case 5: x = 27; break;
			}
			
			machine[i] = new Machine(x,y);
			
			for(int z=machine[i].getY();z<machine[i].getY()+4;z++)
				for(int w=machine[i].getX();w<machine[i].getX()+3;w++)
					board.setPiece(machine[i].getMachinePart(w,z));
		}
	}
	
	public void run()	
	{
		board.setPiece(player);
		board.displayBoard();
		long timer = System.currentTimeMillis();
		long conMoveTimer = timer + conMoveInterval;
		long conShiftTimer = timer + conShiftInterval;
		long toolDropTimer = timer + toolDropInterval;
		
		while(!gameover())
		{
			setIntervals();
			if(board.kbhit())
			{
				int move = ((Player)player).move(board.getch());
				boolean isX = player.isX();
				movement(player,move,isX);
				
				if(board.getch() == GamePiece.ENTER && !((Player)player).getBelt().isEmpty())
				{
					((Player)player).getBelt().next();
					printBelt();
				}
				
				if(board.getch() == GamePiece.SPACE)
				{
					if(Machine.getMachine(((Player)player).getX()) != -99)
						fixMachine();
				}
				
				if(board.getch() == GamePiece.Q)
				{
					quit();
				}
			}
			
			if(System.currentTimeMillis() >= conMoveTimer)
			{
				conveyer.next();
				((Player)player).setScore(((Player)player).getScore()+1);
				conMoveTimer+=conMoveInterval;			

				printConveyer();
			}
			if(System.currentTimeMillis() >= conShiftTimer)
			{
				conveyer.shift();
				conShiftTimer+=conShiftInterval;
			}
			if(System.currentTimeMillis() >= toolDropTimer)
			{
				Tool t = (Tool)conveyer.remove();
				t.setXY(t.getX(),4);
				t.setTimer(System.currentTimeMillis()+500);
				dropTools.add(t);
				printConveyer();
				toolDropTimer +=toolDropInterval;
			}
			
			for(int i=0;i<dropTools.size();i++)
			{
				Tool t = (Tool)dropTools.get(i);
				
				if(System.currentTimeMillis() >= t.getTimer())
				{
					board.setBlank(t.getX(),t.getY());
					t.setXY(t.getX(),t.getY()+1);
					
					if(board.getPiece(t.getX(),t.getY()).getPiece() == Player.PLAYER)
					{
						((Player)player).getBelt().add(t);
						dropTools.remove(i);
						printBelt();
					}
					else
					{
						if(board.getPiece(t.getX(),t.getY()).getPiece() != ' ')
						{
							if(Machine.getMachine(t.getX()) != -99)
							{
								machine[Machine.getMachine(t.getX())].breakPiece();	
								dropTools.remove(i);
								printMachine(Machine.getMachine(t.getX()));
							}
						}
						else
						{
							board.setPiece(t);
							board.drawPiece(t.getX(),t.getY());
							t.setTimer(System.currentTimeMillis()+fallingInterval);
						}
					}
					
					if(t.getY() > 23)
					{
						board.setBlank(t.getX(),t.getY());
						dropTools.remove(i);
						printBelt();
					}
				}
			}
		}
		
		board.add();
		while(!board.getContinueOn()){}
		((Player)player).setName(board.getName());
 
		HighScore[] tempHS = saveHighScore();
		board.clrscr();
		board.setPrintColor(Color.white);
		board.drawString("GAMEOVER",board.getWidth()/2-35,75);

		for(int i=0;i<10;i++)
		{
			board.drawString((i+1)+") "+tempHS[i].getName(),20,120+20*i);
			board.drawString(tempHS[i].getScore()+"",150,120+20*i);
		}
	}
	
	public void setIntervals()
	{	
		if(((Player)player).getScore() > 10000)
		{
			conMoveInterval = 5;
			conShiftInterval = 5;
			toolDropInterval = 25;
			fallingInterval = 25;
		}
		else
		{
			if(((Player)player).getScore() > 7500)
			{
				conMoveInterval = 50;
				conShiftInterval = 10;
				toolDropInterval = 100;
				fallingInterval = 50;
			}
			else
			{
				if(((Player)player).getScore() > 5000)
				{
					conMoveInterval = 100;
					conShiftInterval = 25;
					toolDropInterval = 200;
					fallingInterval = 100;
				}
				else
				{
					if(((Player)player).getScore() > 2500)
					{
						conMoveInterval = 250;
						conShiftInterval = 50;
						toolDropInterval = 500;
						fallingInterval = 250;
					}
					else
					{
						if(((Player)player).getScore() > 1000)
						{
							conMoveInterval = 750;
							conShiftInterval = 250;
							toolDropInterval = 1250;
							fallingInterval = 300;
						}
						else
						{
							if(((Player)player).getScore() > 500)
							{
								conMoveInterval = 1500;
								conShiftInterval = 500;
								toolDropInterval = 2500;
								fallingInterval = 350;
							}
							else
							{
								if(((Player)player).getScore() > 500)
								{
									conMoveInterval = 800;
									conShiftInterval = 1000;
									toolDropInterval = 3500;
									fallingInterval = 400;
								}
								else
								{
									if(((Player)player).getScore() > 100)
									{
										conMoveInterval = 900;
										conShiftInterval = 4000;
										toolDropInterval = 5000;
										fallingInterval = 450;
									}
									else
									{
										conMoveInterval = 1000;
										conShiftInterval = 5000;
										toolDropInterval = 10000;
										fallingInterval = 500;
									}
								}
							}
						}
					}				
				}
			}
		}
	}
	
	public HighScore[] saveHighScore()
	{
		HighScore[] tempHighScore = printHighScore();
		IOFile.setFile("highscore.txt");
		IOFile.setOut();
		
		for(int i=0;i<10;i++)
			IOFile.println(i+"!"+tempHighScore[i].getName()+"!"+tempHighScore[i].getScore()+"!");
	
		return tempHighScore;
	}	
	
	public HighScore[] printHighScore()
	{
		IOFile.setFile("highscore.txt");
		HighScore[] tempHighscore = new HighScore[11];
		IOFile.setIn();
		String str = IOFile.readLine();
		int index = 0;
		
		while(str != null)
		{
			String[] tempArray = str.split("!");
			tempHighscore[index] = new HighScore(tempArray[1],Integer.parseInt(tempArray[2]));
			index++;
			str = IOFile.readLine();
		}
		
		tempHighscore[10] = new HighScore(((Player)player).getName(),((Player)player).getScore());
		Sort.bubbleSort(tempHighscore);
		
		return tempHighscore;
	}
	
	public void movement(GamePiece g, int move, boolean isX)
	{
		if(move != -99)
		{
			if(isX)
			{
				if(board.isValid(g,move,g.getY()))
				{					
					board.setBlank(g.getX(),g.getY());
//					board.drawPiece(g.getX(),g.getY());
					g.setXY(move,g.getY());
//
					checkPiece(move,g.getY());
					board.setPiece(g);
					board.drawPiece(g.getX(),g.getY());
				}
			}
			else
			{
				if(board.isValid(g,g.getX(),move))
				{
					
					board.setBlank(g.getX(),g.getY());
//					board.drawPiece(g.getX(),g.getY());
					g.setXY(g.getX(),move);
//
					checkPiece(g.getX(),move);
					board.setPiece(g);
					board.drawPiece(g.getX(),g.getY());
				}
			}
		}
	}
	
	public void fixMachine()
	{
		boolean valid = false, xySet = false;
		int xFix = 0, yFix = 0;
		int machineIndex = Machine.getMachine(player.getX());
		char firstTool = ((Player)player).getFirstPiece();
		Machine m = machine[machineIndex];
		
		for(int y=m.getY();y<m.getY()+4;y++)
			for(int x=m.getX();x<m.getX()+3;x++)
				if(!xySet)
					if(!m.getMachineBroken())
						if(m.getMachinePart(x,y).getBroken() && m.getMachinePart(x,y).getPiece() == firstTool && ((Player)player).getY() == 17)
						{
							valid = true;
							xySet = true;
							xFix = x;
							yFix = y;				
						}
		
		if(valid)
		{
			((Player)player).setScore(((Player)player).getScore()+10);
			((Player)player).getBelt().remove();
			m.fixPiece(xFix,yFix);
			printBelt();
			printMachine(machineIndex);
		}
	}
	
	public void checkPiece(int x, int y)
	{
		if(board.getPiece(x,y).getPiece() != ' ')
		{
			((Player)player).getBelt().add((Tool)board.getPiece(x,y));
			((Player)player).setScore(((Player)player).getScore()+5);
			board.setBlank(x,y);
			printBelt();
		
			for(int i=0;i<dropTools.size();i++)
			{
				GamePiece g = (GamePiece)dropTools.get(i);
				if(g.getX() == x && g.getY() == y)
					dropTools.remove(i);
			}
		}
	}
}	