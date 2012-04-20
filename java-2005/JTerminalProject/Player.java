import java.awt.Color;

public class Player extends Fighter
{
	private String name;
	private int keys,potions;
	
	public Player(char piece, int x, int y, Color colorPiece, boolean passable, String name, int level, int hp, int maxHp, int exp, int str, int def, int money, int keys, int potions)
	{
		super(piece,x,y,colorPiece,passable,money,level,hp,maxHp,exp,str,def,0);
		this.name = name;
		this.keys = keys;
		this.potions = potions;
	}
	
	public int getScore()
	{
		return (level*100)+exp+money+keys+potions+maxHp+str+def;
	}
	
	public void usePotion()
	{
		potions--;
		int tempHealth = hp + 50;
		
		if(tempHealth > maxHp)
			tempHealth = maxHp;
		
		hp = tempHealth;		
	}
	
	public int move(int direction)
	{
		switch(direction)
	   	{
	   		case GamePiece.UP:		isX = false; return y-1;
	   		case GamePiece.DOWN:	isX = false; return y+1;
	   		case GamePiece.LEFT:	isX = true; return x-1;
	   		case GamePiece.RIGHT:	isX = true; return x+1;
	   }
	   return -99;
	}
	
	public void setName(String name){this.name = name;}
	public void setHp(int hp){this.hp= hp;}
	public void setMoney(int money){this.money = money;}
	public void setKey(int keys){this.keys = keys;}
	public void setPotions(int potions){this.potions = potions;}
	public void setSpeed(int speed){this.speed = speed;}
	public void setExp(int exp){this.exp = exp;}
	
	public int getPotions(){return potions;}
	public int getMoney(){return money;}
	public int getKey(){return keys;}
	public String getName(){return name;}
}