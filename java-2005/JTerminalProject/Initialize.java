import java.awt.Color;

public class Initialize
{
	// prints out the rules
	public void printRules()
	{
		System.out.println(".: RPG ASCII GAME :.");
		System.out.println("--------------------");
		System.out.println("To advance to the next level you have to defeat all the enemies\n");
		System.out.println("P\tPlayer");
		System.out.println("E\tEnemy");
		System.out.println("*\tWall");
		System.out.println("=\tDoor");
		System.out.println("$\tCoin");
		System.out.println("O\tPotion");
		System.out.println("K\tKey");
		System.out.println("L\tLevel Up");
		
		System.out.println("\nKEYBOARD SETTINGS");
		System.out.println("-------");
		System.out.println("   a   ");
		System.out.println(" s d f  \t- DIRECTIONAL KEYS");
		System.out.println("SPACEBAR\t- SAVE PROGRESS \\ SELECT COMMAND IN BATTLE");
		System.out.println("BACKSPACE\t- OPEN & CLOSE MENU");
	}
	
	// this method sets up a brand new board in the folder
	public void setBoards(String folderName)
	{
		for(int i=1;i<6;i++)
		{
			IOFile.setFile("Boards\\board_"+i+".txt");
			IOFile.setIn();
			IOFile.setFile(folderName+"\\"+"board_"+i+".txt");
			IOFile.setOut();
		
			String str = IOFile.readLine();
			
			while(str != null)
			{
				if(str != null)
				{
					IOFile.println(str);
					str = IOFile.readLine();
				}
			}
		}
		setDefaultInfo(folderName);
	}
	
	// this method sets up default player information
	public void setDefaultInfo(String folderName)
	{
		String name = Jin.readLine("Input player name: ");
		IOFile.setFile(folderName+"\\settings.txt");
		IOFile.setOut();
		IOFile.println("cb:1"); // board
		IOFile.println("ch:P"); // player character
		IOFile.println("xc:1"); // x coord
		IOFile.println("yc:1"); // y coord
		IOFile.println("nm:"+name); // player name
		IOFile.println("ky:0"); // keys
		IOFile.println("mo:0"); // money
		IOFile.println("po:2"); // potions
		IOFile.println("lv:1"); // level
		int hp = (int)(Math.random()*50+50);
		IOFile.println("hp:"+hp); // hp
		IOFile.println("mx:"+hp); // max hp
		IOFile.println("ex:0"); // experience
		IOFile.println("st:"+(int)(Math.random()*5+5)); // attack
		IOFile.println("df:"+(int)(Math.random()*5+5)); // defense
	}
	
	// saves all the information
	public void saveSettings(String folderName, String currentBoard, Player p)
	{
		IOFile.setFile(folderName+"\\settings.txt");
		IOFile.setOut();
		IOFile.println("cb:"+currentBoard.charAt(6));
		IOFile.println("ch:"+p.getPiece());
		IOFile.println("xc:"+p.getX());
		IOFile.println("yc:"+p.getY());
		IOFile.println("nm:"+p.getName());
		IOFile.println("ky:"+p.getKey());
		IOFile.println("mo:"+p.getMoney());
		IOFile.println("po:"+p.getPotions());
		IOFile.println("lv:"+p.getLevel());
		IOFile.println("hp:"+p.getHp());
		IOFile.println("mx:"+p.getMaxHp());
		IOFile.println("ex:"+p.getExp());
		IOFile.println("st:"+p.getStr());
		IOFile.println("df:"+p.getDef());
	}
	
	// loads player information
	public Player loadDefaultInfo(String folderName)
	{
		IOFile.setFile(folderName+"\\settings.txt");
		IOFile.setIn();
		String currentBoard = IOFile.readLine();
		char ch = IOFile.readLine().charAt(3);
		int xc = Integer.parseInt(IOFile.readLine().substring(3));
		int yc = Integer.parseInt(IOFile.readLine().substring(3));
		String nm = IOFile.readLine().substring(3);
		int ky = Integer.parseInt(IOFile.readLine().substring(3));
		int mo = Integer.parseInt(IOFile.readLine().substring(3));
		int po = Integer.parseInt(IOFile.readLine().substring(3));
		int lv = Integer.parseInt(IOFile.readLine().substring(3));
		int hp = Integer.parseInt(IOFile.readLine().substring(3));
		int mx = Integer.parseInt(IOFile.readLine().substring(3));
		int ex = Integer.parseInt(IOFile.readLine().substring(3));
		int st = Integer.parseInt(IOFile.readLine().substring(3));
		int df = Integer.parseInt(IOFile.readLine().substring(3));

		return new Player(ch,xc,yc,Color.red,true,nm,lv,hp,mx,ex,st,df,mo,ky,po);
	}
	
	// reads in the enemys information
	public Enemy readEnemy(int numb, int randomX, int randomY)
	{
		IOFile.setFile("enemy.txt");
		IOFile.setIn();
		String str = IOFile.readLine();
				
		while(Integer.parseInt(str.charAt(0)+"") != numb)
			str = IOFile.readLine();
		
		String[] tempArray = str.split("!");
		
		int level,hp,exp,strength,def,money;
		level = hp = exp = strength = def = money = 0;
		
		for(int i=1;i<tempArray.length;i++)
		{
			String tempStr = tempArray[i].substring(4);
			int bottomRange = Integer.parseInt(tempStr.substring(0,tempStr.indexOf("-")));
			int topRange = Integer.parseInt(tempStr.substring(tempStr.indexOf("-")+1));
			int range = (int)(Math.random()*(topRange-bottomRange)+bottomRange);
		
			switch(i)
			{
				case 1: level = range; break;
				case 2: hp = range; break;
				case 3: exp = range; break;
				case 4: strength = range; break;
				case 5: def = range; break;
				case 6: money = range; break;
			}
		}

		return new Enemy('E',randomX,randomY,Color.green,true,level,hp,exp,strength,def,money);
	}
}