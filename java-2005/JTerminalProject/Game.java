import java.util.ArrayList;
import java.awt.Color;

public class Game extends Initialize
{
	private GameBoard board;
	private GamePiece player;
	private GamePiece[][] wall;
	private ArrayList enemies;
	private String folderName, currentBoard;
	private int boardNumb, enemyCounter;
	private boolean boardTop;
	
	public Game()
	{
		folderName = Jin.readLine("What is your folder name? ");
		IOFile.setFile(folderName);
		
		if(!IOFile.exists())
		{
			IOFile.mkdir();
			setBoards(folderName);
		}
		init();
	}
	
	// this method initializes everything
	public void init()
	{
		// sets which board its at
		IOFile.setFile(folderName+"\\settings.txt");
		IOFile.setIn();
		boardNumb = Integer.parseInt(IOFile.readLine().substring(3));
		currentBoard = "board_" + boardNumb + ".txt";
		
		// sets other information
		player = loadDefaultInfo(folderName);
		printRules();
		boardTop = true; 
		board = new GameBoard();
		board.setScreenSize(60,30);
		board.setBoard(folderName+"\\"+currentBoard);
		initRandomLevelUp();
		enemies = new ArrayList();
	}
		
	public void run()	
	{
		board.setPiece(player);
		initEnemies();		
		board.displayBoard();
		int safePeriod = 0;	
		
		while(((Player)player).getHp() > 0)
		{
			if(boardTop)
			{
				if(board.kbhit())
				{
					int move = ((Player)player).move(board.getch());
					boolean isX = player.isX();
					movement(player,move,isX);
					
					if(board.getch() == GamePiece.SPACE)
					{
						saveSettings(folderName,currentBoard,(Player)player);
						board.saveBoard(folderName+"\\"+currentBoard);
					}
					else
					{
						if(board.getch() == GamePiece.BACKSPACE)
						{
							preSettings();
							displayMenu();
						}
					}
				}
				enemyMovement();
				
				if(safePeriod > 0)
					safePeriod--;
					
				// Checks if you run into the enemy
				if(safePeriod == 0)
				{
					for (int i=0;i<enemies.size();i++)
						if(player.getX() == ((GamePiece)enemies.get(i)).getX() && player.getY() == ((GamePiece)enemies.get(i)).getY())
						{
							preSettings();
							safePeriod = 750000;
							battle(i);
							
						}
				}
				
				// checks if there are any enemies left
				if(enemies.size() == 0)
				{
					if(boardNumb == 5)
						boardNumb = 1;
					else
						boardNumb++;
					
					currentBoard = "board_" + boardNumb + ".txt";
					board.setBoard(folderName+"\\"+currentBoard);
					initEnemies();		
					player.setXY(1,1);
					board.setPiece(player);
					board.displayBoard();
				}
			}
		}
		
		HighScore[] tempHS = saveHighScore();
		board.clrscr();
		board.setPrintColor(Color.white);
		board.drawString("GAMEOVER",board.getWidth()/2-35,75);

		for(int i=0;i<10;i++)
		{
			board.drawString((i+1)+") "+tempHS[i].getName(),50,120+20*i);
			board.drawString(tempHS[i].getScore()+"",350,120+20*i);
		}
	}
	
	public void preSettings()
	{
		board.saveBoard(folderName+"\\"+currentBoard);
		saveSettings(folderName,currentBoard,(Player)player);
		boardTop = false;	
	}
	
	public void displayMenu()
	{
		Fighter p = (Fighter)player;
		
		board.clrscr();
		board.setPrintColor(181,146,26);
		board.fillRect(10,350,(int)((double)p.getExp()/100*470),23);
		
		board.setPrintColor(Color.white);
		board.drawRect(10,35,470,350);
		board.drawRect(10,35,150,300);
		board.drawRect(10,35,150,150);
		board.drawRect(10,335,470,50);
		board.drawImage("player.gif",65,75);
	
		board.drawString("Name: ",200,80);
		board.drawString("Level: ",200,100);
		board.drawString("Hp: ",200,120);
		board.drawString("Str: ",200,140);
		board.drawString("Def: ",200,160);
		board.drawString("Money: ",25,230);
		board.drawString("Potions: ", 25, 260);
		board.drawString("Keys: ", 25, 290);
		board.drawString("Experience",15,365);
		
		board.drawString(((Player)p).getName(),250,80);
		board.drawString(p.getLevel()+"",250,100);
		board.drawString(p.getHp()+" / "+p.getMaxHp(),250,120);
		board.drawString(p.getStr()+"",250,140);
		board.drawString(p.getDef()+"",250,160);
		board.drawString(((Player)p).getMoney()+"",75,230);
		board.drawString(((Player)p).getPotions()+"",75,260);
		board.drawString(((Player)p).getKey()+"",75,290);
		
		while(!boardTop)
			if(board.kbhit() && board.getch() == GamePiece.BACKSPACE)
				runCommand();
	}
	
	public void battle(int enemyIndex)
	{
		Enemy e = (Enemy)enemies.get(enemyIndex);
		Player p = (Player)player;
		String bg = "bg_"+(int)(Math.random()*5)+".gif";
		String en = "enemy_"+(int)(Math.random()*3)+".gif";
		drawBattleScreen(e,bg,en);
		int playerDef = p.getDef(), enemyDef = e.getDef();
		int enemySpeed = 50000000, tempEnemySpeed = 0, playerSpeed = 100000000;
		p.setSpeed(0);
		
		while(!boardTop && p.alive() && e.alive())
		{
			if(tempEnemySpeed == enemySpeed)
			{
				e.enemyAction(p);
				tempEnemySpeed=0;
				drawBattleScreen(e,bg,en);
			}
			else
				tempEnemySpeed++;

			if(playerSpeed == p.getSpeed())
				drawCommandMenu(e,bg,en);
			else
				p.setSpeed(p.getSpeed()+1);
		}
		
		p.setDef(playerDef);
		e.setDef(enemyDef);
		
		if(!e.alive())
		{
			p.setMoney(p.getMoney()+e.getMoney());
			p.setExp(p.getExp()+e.getExp());
			p.checkExp();
			enemies.remove(enemyIndex);
		}
		runCommand();
	}
	
	public void drawCommandMenu(Enemy e, String bg, String en)
	{
		boolean actionSelected = false;
		int current = 0;
		printCursor(current);
		String[] commands = new String[4];
		commands[0] = "Attack";
		commands[1] = "Defend";
		commands[2] = "Use Potion";
		commands[3] = "Run";
		
		board.setPrintColor(Color.white);
		board.drawRect(130,295,180,90);
		board.drawString(commands[0],155,315);
		board.drawString(commands[1],155,335);
		board.drawString(commands[2],155,355);
		board.drawString(commands[3],155,375);
		
		while(!actionSelected)
		{
			if(board.kbhit())
			{
				if(board.getch() == GamePiece.UP)
					if(current == 0)
						current = 3;
					else
						current--;
				else
					if(board.getch() == GamePiece.DOWN)
					{
						if(current == 3)
							current = 0;
						else
							current++;
					}
				else
					if(board.getch() == GamePiece.SPACE)
						actionSelected = true;
								
				printCursor(current);
			}
		}
		
		performAction(current,e,bg,en);	
	}
	
	public void performAction(int current, Enemy e, String bg, String en)
	{
		((Player)player).setSpeed(0);
		switch(current)
		{
			case 0:
			attackCommand(e);
			drawBattleScreen(e,bg,en);
			break;
			
			case 1:
			((Fighter)player).defend();
			drawBattleScreen(e,bg,en);
			break;
			
			case 2:
			itemCommand();
			drawBattleScreen(e,bg,en);
			break;
			
			case 3:
			runCommand();
			break;
		}
	}	
	
	public void attackCommand(Enemy e)
	{
		board.setPrintColor(Color.black);
		board.fillRect(130,295,185,95);
		int dmg = ((Fighter)player).finalDamage(((Fighter)player).attack(),((Fighter)e).getDef());
		((Fighter)e).calculateHealth(dmg);
	}
	
	public void itemCommand()
	{
		Player p = (Player)player;
		if(p.getPotions() > 0)
			p.usePotion();
		else
			p.setHp(p.getHp()+1);
	}
	
	public void runCommand()
	{
		boardTop = true;
		board.clrscr();
		board.displayBoard();
	}

	public void printCursor(int current)
	{
		board.setPrintColor(Color.black);
		board.fillRect(135,297,17,88);
		board.setPrintColor(Color.white);
		board.drawString(">",145,315+current*20);
	}
	
	public void drawBattleScreen(Enemy e, String bg, String en)
	{
		board.clrscr();
		board.drawImage(bg,0,0);
		board.setPrintColor(Color.black);
		board.fillRect(5,290,482,100);
		board.setPrintColor(Color.white);
		board.drawRect(5,290,482,100);
			
		board.drawString(((Player)player).getName(),70,310);
		board.drawString(((Player)player).getLevel()+"",70,327);
		board.drawString(((Player)player).getHp()+"",70,344);
		board.drawString(((Player)player).getStr()+"",70,361);
		board.drawString(((Player)player).getDef()+"",70,378);
		board.drawString("Name:",15,310);
		board.drawString("Level:",15,327);
		board.drawString("HP:",15,344);
		board.drawString("Strength:",15,361);
		board.drawString("Defense:",15,378);
		board.drawImage("player.gif",80,150);
		
		board.drawString("Name:",320,310);
		board.drawString("Level:",320,327);
		board.drawString("HP:",320,344);
		board.drawString("Strength:",320,361);
		board.drawString("Defense:",320,378);
		board.drawString("Enemy",375,310);
		board.drawString(e.getLevel()+"",375,327);
		board.drawString(e.getHp()+"",375,344);
		board.drawString(e.getStr()+"",375,361);
		board.drawString(e.getDef()+"",375,378);
		board.drawImage(en,300,150);
		
		board.setPrintColor(Color.red);
		board.fillRect(15,40,(int)((double)((Player)player).getHp()/((Player)player).getMaxHp()*100),10);
		board.fillRect(375,40,(int)((double)e.getHp()/e.getMaxHp()*100),10);
		board.setPrintColor(Color.black);
		board.drawRect(15,40,100,10);
		board.drawRect(375,40,100,10);
	}
	
	public void initEnemies()
	{
		int tempCounter = 0;
		enemyCounter+=5;
		
		while(tempCounter < enemyCounter)
		{
			int randomX = (int)(Math.random()*board.getXSpaces());
			int randomY = (int)(Math.random()*board.getYSpaces());
			
			if(board.isValid(new Enemy(),randomX,randomY))
			{
				enemies.add(readEnemy(randomEnemyRange(),randomX,randomY));
				tempCounter++;	
			}
		}
		
		for(int i=0;i<enemies.size();i++)
		{
			Enemy e = (Enemy)(enemies.get(i));
			board.printPiece(e.getX(),e.getY());
			board.setPiece(e);
		}
	}
	
	public int randomEnemyRange()
	{
		int range = 0;
		Fighter p = (Fighter)player;
		int level = ((Fighter)player).getLevel();
		
		switch(level)
		{
			case 1:	case 2:	case 3: break;
			case 4: case 5: range = 1;break;
			case 6: case 7:	case 8: range = 2;break;
			case 9: case 10: range = 3; break;
			case 11: case 12: case 13: range = 4; break;
		}
	
		return range;
	}

	public void enemyMovement()
	{
		for(int i=0;i<enemies.size();i++)
		{
			GamePiece enemy = ((GamePiece)(enemies.get(i)));
			if(((Enemy)enemy).getTempSpeed() == ((Enemy)enemy).getSpeed())
			{
				int rand = (int)(Math.random()*5+1);
				int movement = ((Enemy)enemy).move(rand);
				boolean isX = enemy.isX();
				movement(enemy,movement,isX);
				((Enemy)enemy).setTempSpeed(0);
			}
			((Enemy)enemy).setTempSpeed(((Enemy)enemy).getTempSpeed()+1);			
		}
	}
	
	public void getAction(GamePiece g, int x, int y)
	{
		if(g instanceof Player)
		{
			switch(board.getGamePiece(x,y).getPiece())
			{
				case '$': ((Player)player).setMoney(((Player)player).getMoney()+100); break;	
				case '=': 
				if(((Player)player).getKey() > 0)
				{
					((Player)player).setKey(((Player)player).getKey()-1);
					board.getGamePiece(x,y).setPassable(true);
				}
				break;
				case 'K': ((Player)player).setKey(((Player)player).getKey()+1);	break;
				case 'O': ((Player)player).setPotions(((Player)player).getPotions()+1); break;
				case 'L': 
				{
					((Player)player).setExp(((Fighter)player).getExp()+100);
					((Fighter)player).checkExp();
				}
			}
		}
	}
	
	public void movement(GamePiece g, int move, boolean isX)
	{
		if(move != -99)
		{
			if(isX)
			{
				getAction(g,move,g.getY());
				if(board.isValid(g,move,g.getY()))
				{					
					board.setBlank(g.getX(),g.getY());
					board.printPiece(g.getX(),g.getY());
					g.setXY(move,g.getY());
					board.setPiece(g);
					board.printPiece(g.getX(),g.getY());
				}
			}
			else
			{
				getAction(g,g.getX(),move);
				if(board.isValid(g,g.getX(),move))
				{
					
					board.setBlank(g.getX(),g.getY());
					board.printPiece(g.getX(),g.getY());
					g.setXY(g.getX(),move);
					board.setPiece(g);
					board.printPiece(g.getX(),g.getY());
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
	
	public void initRandomLevelUp()
	{
		int randomLevelUp = (int)(Math.random()*100), randomX = 0, randomY = 0;

		if(randomLevelUp <= 5)
		{
			while(!board.isValid(null,randomX,randomY))
			{
				randomX = (int)(Math.random()*board.getXSpaces());
				randomY = (int)(Math.random()*board.getYSpaces());				
			}
			
			Color c = new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
			board.setPiece(new GamePiece('L',randomX,randomY,c,true));
		}
	}
}