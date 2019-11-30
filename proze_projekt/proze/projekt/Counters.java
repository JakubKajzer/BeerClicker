/**
 * @author Jakub Kajzer
 * @author Bartłomiej Traczyk
 * 
 */

package proze.projekt;

import java.awt.*;
import javax.swing.*;
import java.util.Properties;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Klasa odpowiadająca za wyświetlanie numeru poziomu i czasu
 */

public class Counters extends JPanel
{
	private String LevelNow;
	private String TimeNow;
	private JLabel LevelLabel;
	private JLabel TimeLabel;
	
	private int numberOfSeconds=0;
	
	private Thread TimeThread;
	
	
	/**
	 *Konstruktor tworzący panel i inicjujący wątek odliczania czasu 
	 */
	public Counters()
	{
		//System.out.println("DEBUG: konstruktor Counters");
		setLayout(new GridLayout(1,2));
		
		createLevelViewer();
		createTimeViewer();
		
		setVisible(true);
		
		
		
		TimeThread = new Thread(new Runnable() 
        {
            public void run() 
            {
                while (!Thread.currentThread().isInterrupted()) 
                {
					if(!Application.mainWindow.isPaused)
						numberOfSeconds++;
                    repaint();
                    try 
                    {
						Thread.sleep(1000);
					} 
					catch (Exception ex) {}
                }
            }
        });

       
		
	}
	/**
	 * Tworzy napis tytułu gry w menu
	 */
	private void createLevelViewer()
	{
		
		
		LevelLabel=new JLabel(LevelNow,SwingConstants.CENTER);
		LevelLabel.setSize(500,500);
		LevelLabel.setFont(LevelLabel.getFont().deriveFont(20.0f));
		
		add(LevelLabel);
	}


	/**
	 * Wypisuje upływający czas
	 */
	private void createTimeViewer()
	{
		
		TimeLabel=new JLabel("Click NEW GAME button",SwingConstants.CENTER);
		TimeLabel.setSize(500,500);
		TimeLabel.setFont(TimeLabel.getFont().deriveFont(20.0f));
		
		add(TimeLabel);
	}
	
	@Override
    public void paintComponent(Graphics g) 
    {
		super.paintComponent(g);
		LevelLabel.setText("Level: "+Application.LevelNumber);
	    if(Application.mainWindow.isStarted)
	    {		
			TimeLabel.setText(Integer.toString(numberOfSeconds));
			if(Application.mainWindow.isPaused)
				TimeLabel.setText("PAUSE");
		}
		else
			TimeLabel.setText("Click NEW GAME button");
    }	
	public int getNumberOfSeconds()
	{
		return numberOfSeconds;
	}
	public void resetNumberOfSeconds()
	{
		numberOfSeconds=0;
	}
	public void startThread()
	{
		TimeThread.start();
	}
	public void stopThread()
	{
		TimeThread.interrupt();
	}
	public boolean isThreadGoing()
	{
		return TimeThread.isAlive();
	}
}
