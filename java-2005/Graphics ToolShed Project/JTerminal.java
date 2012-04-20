import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import javax.swing.*;

public class JTerminal extends Frame
    implements KeyListener, WindowListener, MouseListener, MouseMotionListener
{
	private TextField txtField = new TextField();
	private Button btn = new Button("Continue");
	private boolean continueOn = false;
	private String name;

    private final int charWidth = 10;
    private final int charHeight = 12;
    private final int DEFAULT_BUFFER = -1;
    private int xCoord;
    private int yCoord;
    private int mouseX;
    private int mouseY;
    private int xSpaces;
    private int ySpaces;
    private int buffer;
    private Color printColor;
    private boolean keyHit;
    private boolean leftHit;
    private boolean rightHit;

    public JTerminal()
    {
		xSpaces = 30;
        ySpaces = 25;
        setScreenSize(xSpaces, ySpaces);
        gotoxy(0, 0);
        keyHit = false;
        leftHit = false;
        rightHit = false;
        addKeyListener(this);
        addWindowListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setLayout(null);
        setResizable(false);
        setTitle("JTerminal Window");
        setLocation(100, 25);
        setBackground(Color.black);
        printColor = Color.white;
        setVisible(true);
        createBufferStrategy(2);
    }

	// START ADDED
	public void drawString(String s, int x, int y)
	{
		Graphics g = getGraphics();
		g.setColor(printColor);
		g.drawString(s,x,y);
	}

	public void drawClearRect(int a, int b, int c, int d)
	{
		Graphics g = getBufferStrategy().getDrawGraphics();
        g.setColor(Color.black);
		g.fillRect(a,b,c,d);
		repaint();
	}

	public void fillRect(int a, int b, int c, int d)
	{
		Graphics g = getGraphics();
		g.setColor(printColor);
		g.fillRect(a,b,c,d);
	}

	public void drawImage(String path, int x, int y)
	{
		Graphics g = getBufferStrategy().getDrawGraphics();
        ImageIcon playerImg = new ImageIcon("Images\\"+path);
		playerImg.paintIcon(this,g,x*8+5,12*(y+1)+15);
		repaint();
	}
	
	public void add()
	{
		clrscr();
		txtField.setBounds(75,125,100,25);
		btn.setBounds(75,175,100,25);
		btn.addActionListener(new Listener());
		add(txtField);
		add(btn);
	}
	
	class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			name = txtField.getText();
			continueOn = true;
			remove(txtField);
			remove(btn);
		}
	}
	
	
	public String getName(){return name;}
	public boolean getContinueOn(){return continueOn;}
	
	// END ADDED
    public int getXSpaces()
    {
        return xSpaces;
    }

    public int getYSpaces()
    {
        return ySpaces;
    }

    public int getMouseX()
    {
        return mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }

    private int getXSpace(int i)
    {
        return (i - 5) / 10;
    }

    private int getYSpace(int i)
    {
        return (i - 25) / 12;
    }

    public void setScreenSize(int i, int j)
    {
        xSpaces = i;
        ySpaces = j;
        setSize(8 * (xSpaces + 1) + 5, 12 * (ySpaces + 1) + 25);
    }

    public void gotoxy(int i, int j)
    {
        if(i < 0)
        {
            i = 0;
        }
        if(j < 0)
        {
            j = 0;
        }
        if(i >= xSpaces)
        {
            i = xSpaces - 1;
        }
        if(j >= ySpaces)
        {
            j = ySpaces - 1;
        }
        xCoord = i * 10 + 5;
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

    public boolean mlbhit()
    {
        boolean flag = leftHit;
        leftHit = false;
        return flag;
    }

    public boolean mrbhit()
    {
        boolean flag = rightHit;
        rightHit = false;
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
        gotoxy(getXSpace(xCoord) - 1, getYSpace(yCoord));
        for(int i = 0; i < s.length(); i++)
        {
            gotoxy(getXSpace(xCoord) + 1, getYSpace(yCoord));
            print(s.charAt(i));
        }

    }

    public void print(char c)
    {
        Graphics g = getBufferStrategy().getDrawGraphics();
        g.setFont(new Font("Courier New", 0, 12));
        g.setColor(printColor);
        g.clearRect(xCoord, yCoord - 12, 10, 12);
        g.drawString(c + "", xCoord, yCoord);
        repaint();
    }

    public void print(int i)
    {
        String s = i + "";
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
        {
            s = "true";
        } else
        {
            s = "false";
        }
        print(s);
    }

    public void update(Graphics g)
    {
        getBufferStrategy().show();
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

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(mouseevent.getButton() == 1)
        {
            leftHit = true;
        }
        if(mouseevent.getButton() == 3)
        {
            rightHit = true;
        }
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        mouseX = getXSpace(mouseevent.getX());
        mouseY = getYSpace(mouseevent.getY());
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
}
