public class Controlss extends JTerminal
{
	private String str = "It's Mr.Carabano!";
	private char [] hiddenArray;
	private char [] string;
	
	public Controlss()
	{
		hiddenArray = new char[str.length()];
		string = new char[str.length()];
	
		for(int i=0;i<str.length();i++)
		{
			string[i] = str.charAt(i);	
			hiddenArray[i] = '_';
		}
	}
	
	public void run()
	{
		displayOnScreen();
		while(true)
		{
			if(kbhit())
			{
				char key = (char)getch();
				checkStr(key);
				displayStr();
				displayOnScreen();
			}
		}
	}
	
	public void checkStr(char ch)
	{
		for(int i=0;i<string.length;i++)
			if(string[i] == ch)
				hiddenArray[i] = ch;	
	}
	
	public void displayStr()
	{
		String str = "";
		
		for(int i=0;i<string.length;i++)
			str += hiddenArray[i];
	}
	
	public void displayOnScreen()
	{
		for(int x=0;x<string.length;x++)
		{
			gotoxy(3+x*2,10);
			print(hiddenArray[x]);			
		}
	}
}