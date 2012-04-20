import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class IOFile
{
	public static File f;
	public static PrintStream out;
	public static BufferedReader in;
	
	public static void mkdir()
	{
		f.mkdir();
	}
	
	public static boolean exists()
	{
		return f.exists();
	}
	
	public static String getPath()
	{
		return f.getPath();
	}
	
	public static void setFile(String fileName)
	{
		f = new File(fileName);
	//	setOut();
	//	setIn();
	}
	
	public static void setOut()
	{
		try{
			out = new PrintStream(new FileOutputStream(f));
		}catch(FileNotFoundException fnfe){}
	}

	public static void setIn()
	{
		try{
			in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		}catch(IOException ioe){}	
	}	
	
	public static void println(String str)
	{
		out.println(str);
	}
	
	public static String readLine()
	{
		String str = "";
		
		try{
			str = in.readLine();
		}catch(IOException ioe){}
		
		return str;
	}
	
	public static String readAllLines()
	{
		String str = "";
		String tempStr = "";
		
		while(tempStr != null)
		{
			try{
				tempStr = in.readLine();
				if(tempStr != null)
					str+=tempStr+"\n";
			}catch(IOException ioe){}
		}	
		return str;
	}
	
	public static void rmdir()
	{
		f.delete();
	}
	
	public static int length()
	{
		int size = 0;
		String str = "";
		setIn();
		while(str != null)
		{
			try{
				str = in.readLine();
				if(str != null)
					size++;
			}catch(IOException ioe){}
		}
		
		return size;
	}
}