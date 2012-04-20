import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JTerminal extends Frame
    implements KeyListener, WindowListener
{

    public JTerminal()
    {
        xSpaces = 40;
        ySpaces = 20;
        setScreenSize(xSpaces, ySpaces);
        gotoxy(0, 0);
        keyHit = false;
        addKeyListener(this);
        addWindowListener(this);
        setResizable(false);
        setTitle("JTerminal Window");
        setLocation(100, 25);
        setBackground(Color.black);
        printColor = Color.white;
        setVisible(true);
    }
	
    public int getXSpaces()
    {
        return xSpaces;
    }

    public int getYSpaces()
    {
        return ySpaces;
    }
	
    public void setScreenSize(int i, int j)
    {
        xSpaces = i;
        ySpaces = j;
        setSize(8 * (xSpaces + 1) + 5, 12 * (ySpaces + 1) + 25);
    }

    public void gotoxy(int i, int j)
    {
        xCoord = i * 8 + 5;
        yCoord = (j + 1) * 12 + 25;
    }

    public int getch()
    {
        int i = buffer;
        return i;
    }

    public int getche()
    {
        int i = getch();
        print((char)i);
        return i;
    }

    public boolean kbhit()
    {
        boolean flag = keyHit;
        keyHit = false;
        return flag;
    }

    public void setBackground(int i, int j, int k)
    {
        super.setBackground(new Color(i, j, k));
    }

    public void setBackground(Color color)
    {
        super.setBackground(color);
    }

    public void setPrintColor(int i, int j, int k)
    {
        printColor = new Color(i, j, k);
    }

    public void setPrintColor(Color color)
    {
        printColor = color;
    }

    public void clrscr()
    {
        getGraphics().clearRect(0, 0, getWidth(), getHeight());
    }

    public void print(Object obj)
    {
        String s = obj.toString();
        Graphics g = getGraphics();
        g.setFont(new Font("Courier", 0, 12));
        g.setColor(printColor);
        g.clearRect(xCoord, yCoord - 12, 8 * s.length(), 12);
        g.drawString(s, xCoord, yCoord);
    }
	
	// ADDED
	public void drawString(String s, int x, int y)
	{
		Graphics g = getGraphics();
		g.setColor(printColor);
		g.drawString(s,x,y);
	}
	// ADDED
	public void drawRect(int a, int b, int c, int d)
	{
		Graphics g = getGraphics();
		g.setColor(printColor);
		g.drawRect(a,b,c,d);
	}
	// ADDED
	public void fillRect(int a, int b, int c, int d)
	{
		Graphics g = getGraphics();
		g.setColor(printColor);
		g.fillRect(a,b,c,d);
	}
	// ADDED
	public void drawImage(String path, int x, int y)
	{
		ImageIcon playerImg = new ImageIcon("Images\\"+path);
		Graphics g = getGraphics();
		playerImg.paintIcon(this,g,x,y);
	}
	
    public void print(int i)
    {
        String s = i + "";
        print(s);
    }

    public void print(char c)
    {
        String s = c + "";
        print(s);
    }

    private void print(double d)
    {
        String s = d + "";
        print(s);
    }

    private void print(boolean flag)
    {
        String s;
        if(flag)
            s = "true";
        else
            s = "false";
        print(s);
    }

    public void paint(Graphics g)
    {
    }

    public void keyPressed(KeyEvent keyevent)
    {
        keyHit = true;
        buffer = keyevent.getKeyChar();
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void windowClosing(WindowEvent windowevent)
    {
        System.exit(0);
    }

    private final int charWidth = 8;
    private final int charHeight = 12;
    private final int WIDTH = 675;
    private final int HEIGHT = 835;
    private final int DEFAULT_BUFFER = -1;
    private int xCoord;
    private int yCoord;
    private int xSpaces;
    private int ySpaces;
    private int buffer;
    private Color printColor;
    private boolean keyHit;
}