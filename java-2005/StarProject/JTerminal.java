import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class JTerminal extends Frame
    implements KeyListener, WindowListener, MouseListener, MouseMotionListener
{

    private final int charWidth = 10;
    private final int charHeight = 10;
    private final int DEFAULT_BUFFER = -1;
    private int xCoord;
    private int yCoord;
    private int mouseX;
    private int mouseY;
    private int xSpaces;
    private int ySpaces;
    private int gridX;
    private int gridY;
    private int buffer;
    private Color printColor;
    private boolean keyHit;
    private boolean leftHit;
    private boolean rightHit;

    public JTerminal()
    {
        xSpaces = 20;
        ySpaces = 20;
        setScreenSize(xSpaces, ySpaces);
        buffer = -1;
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
        return (i - 25) / 10;
    }

    public void setScreenSize(int i, int j)
    {
        xSpaces = i;
        ySpaces = j;
        setSize(10 * (xSpaces + 1) + 5, 10 * (ySpaces + 1) + 25);
    }

    public void gotoxy(int i, int j)
    {
        gridX = i;
        gridY = j;
        xCoord = i * 10 + 5;
        yCoord = (j + 1) * 10 + 25;
    }

    public int getch()
    {
        while(buffer == -1) ;
        int i = buffer;
        buffer = -1;
        return i;
    }

    public int getche()
    {
        while(buffer == -1) ;
        int i = getch();
        buffer = -1;
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
        Graphics g = getBufferStrategy().getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());
        repaint();
    }

    public void print(Object obj)
    {
        String s = obj.toString();
        gotoxy(gridX - 1, gridY);
        for(int i = 0; i < s.length(); i++)
        {
            gotoxy(gridX + 1, gridY);
            print(s.charAt(i));
        }

    }

    public void print(char c)
    {
        Graphics g = getBufferStrategy().getDrawGraphics();
        g.setFont(new Font("Courier New", 0, 12));
        g.setColor(printColor);
        g.clearRect(xCoord, yCoord - 10, 10, 10);
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

    public void paint(Graphics g)
    {
        update(g);
    }

    public void keyPressed(KeyEvent keyevent)
    {
        keyHit = true;
        buffer = keyevent.getKeyChar();
        if(buffer > 255)
        {
            buffer = -1;
        }
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
