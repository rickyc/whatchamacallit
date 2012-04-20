import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.lang.Thread;
import java.awt.event.MouseListener;

public class SimonScreen extends JFrame 
{
	private JButton[] buttons;
	
	public SimonScreen()
	{
		setSize(500,500);
		getContentPane().setLayout(null);
		initialize();
		getContentPane().setBackground(Color.white);
		setVisible(true);
	}
	
	public void initialize()
	{
		buttons = new JButton[4];
		buttons[0] = createButton(100,50,"red",Color.red);
		buttons[1] = createButton(150,100,"blue",Color.blue);
		buttons[2] = createButton(50,100,"yellow",Color.yellow);
		buttons[3] = createButton(100,150,"green",Color.green);	
		for(int i=0;i<buttons.length;i++)
			getContentPane().add(buttons[i]);
	}
	
	public JButton createButton(int x, int y, String str, Color c)
	{
		JButton b = new JButton(str);
		b.setBounds(x,y,50,50);
		b.setBackground(c);
		b.setForeground(c);
		return b;
	}
	
	public void addActionListeners(ActionListener a)
	{
		for(int i=0;i<buttons.length;i++)
			buttons[i].addActionListener(a);
	}
	
	public JButton getRandomButton()
	{
		return buttons[(int)(Math.random()*4)];
	}
	
	public void lightButton(JButton b, int patternDelay)
	{
		Color original = b.getBackground();
		b.setBackground(Color.white);
		try{Thread.sleep(patternDelay);}catch(InterruptedException e){};
		b.setBackground(original);
	}
}