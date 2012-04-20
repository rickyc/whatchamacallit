import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.lang.Thread;

public class SimonControl implements ActionListener
{
	private ArrayList pattern;
	private int patternDelay;
	private SimonScreen ss;
	private static int patternCtr;
	
	public SimonControl()
	{
		ss = new SimonScreen();
		ss.addActionListeners(this);
		pattern = new ArrayList();
		patternDelay = 2000;	
		updatePattern();
		showPattern();
		
		while(true){ 
			afterActionPerformed();
			try{Thread.sleep(20);}catch(InterruptedException e){};
		}
	}
	
	public void afterActionPerformed()
	{
		if(patternCtr == pattern.size())
		{
			updatePattern();
			showPattern();
		}
	}
	
	public void updatePattern()
	{
		pattern.add(ss.getRandomButton());	
	}
	
	public void showPattern()
	{
		patternCtr = 0;
		
		for(int i=0;i<pattern.size();i++)
		{
			JButton b = (JButton)pattern.get(i);
			System.out.print(b.getText() + ", ");
			ss.lightButton(b,patternDelay);
		}
		
		if(patternDelay > 30)
			patternDelay -= 10;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JButton b = (JButton)e.getSource();
		System.out.println(patternCtr);
		
		if(b == pattern.get(patternCtr))
		{
			patternCtr++;
		}
		else
		{
			pattern = new ArrayList();
			patternDelay = 2000;
			updatePattern();
			showPattern();
		}
	}	
}