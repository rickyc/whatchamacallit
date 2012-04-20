import java.awt.Color;

public class Enemy extends Fighter
{
	private int tempSpeed;
	
	public Enemy(){}
	
	public Enemy(char piece, int x, int y, Color colorPiece, boolean passable, int level, int hp, int exp, int str, int def, int money)
	{
		super(piece,x,y,colorPiece,passable,money,level,hp,hp,exp,str,def,500000);
	}
	
	public void enemyAction(Fighter p)
	{
		int action = (int)(Math.random()*2);
		
		if(action == 0)
		{
			int dmg = finalDamage(attack(),p.getDef());
			p.calculateHealth(dmg);
		}
		else
			defend();
	}
		
	public int move(int randomNumber)
	{
		switch(randomNumber)
	   	{
	   		case 1:	isX = false; return y-1;
	   		case 2:	isX = false; return y+1;
	   		case 3:	isX = true; return x-1;
	   		case 4:	isX = true; return x+1;
	   }
	   return -99;
	}
	
	public void setTempSpeed(int tempSpeed){this.tempSpeed = tempSpeed;}
	
	public int getTempSpeed(){return tempSpeed;}
}