import java.awt.Color;

public class Fighter extends GamePiece
{
	protected int money, level, hp, maxHp, exp, str, def, speed;
	
	public Fighter(){}
	
	public Fighter(char piece, int x, int y, Color colorPiece, boolean passable,int money, int level, int hp, int maxHp, int exp, int str, int def, int speed)
	{
		super(piece,x,y,colorPiece,passable);
		this.money = money;
		this.level = level;
		this.hp = hp;
		this.maxHp = maxHp;
		this.exp = exp;
		this.str = str;
		this.def = def;
		this.speed = speed;
	}
		
	public void checkExp()
	{
		if(exp>=100)
		{
			level++;
			exp%=100;
			maxHp+=(int)(Math.random()*5+1);
			hp = maxHp;
			str+=(int)(Math.random()*5+1);
			def+=(int)(Math.random()*5+1);
		}	
	}
	
	public int attack()
	{
		return (int)str + ((str + level/32)*(str*(level/32)));
	}

	public int defend()
	{
		int extraDef = (int)(def*.01)+1;
		def+=extraDef;
		return extraDef;
	}
	
	public int finalDamage(int dmg, int def)
	{
		int finalDamage = (int)(dmg * (512 - def)/512); // Calculates Damage
		finalDamage = (int)(Math.random()*(finalDamage/2)+(finalDamage/2)); // Randomizes the damage
		
		int randNum = (int)(Math.random()*100);
		
		// Miss
		if(randNum < 5)
			finalDamage = 0;
		// Perfect Hit
		if(randNum > 99)
			finalDamage = dmg;
		// If Final Damage is Negative, Damage is set to 1
		if(finalDamage < 0)
			finalDamage = 1;
		// If Final Damage is over 9999, then the damage is set to 9999
		if (finalDamage > 9999)
			finalDamage = 9999;
		
		// Returns the final damage
		return finalDamage;	
	}
	
	public void calculateHealth(int finaldmg)
	{
		hp-=finaldmg;
		
		if (hp<0)
			hp= 0;
	}
	
	public boolean alive()
	{
		boolean alive = true;
		if(hp == 0)
			alive = false;
		return alive;
	}
	
	public void setDef(int def){this.def = def;}
		
	public int getSpeed(){return speed;}
	public int getHp(){return hp;}
	public int getMaxHp(){return maxHp;}
	public int getLevel(){return level;}
	public int getExp(){return exp;}
	public int getStr(){return str;}
	public int getDef(){return def;}
	public int getMoney(){return money;}

}