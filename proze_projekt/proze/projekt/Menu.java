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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Klasa odpowiedzialna za menu gry
 */
public class Menu extends JPanel implements ActionListener
{
	
	private JButton newGame;
	private JButton pause;
	private JButton options;
	private JButton	highscore;
	private JButton exit;
	private Highscore wyniki;
	/**
	 * Konstruktor ustawiający tło i układ menu
	 */
	public Menu()
	{
		//System.out.println("DEBUG: konstruktor Menu");
		setBackground(new Color(128,128,128));
		setLayout(new GridLayout(6,1,40,40));
		
		createTitle();
		createButtons();
		
		setVisible(true);
	}
	/**
	 * Tworzy napis tytułu gry w menu
	 */
	private void createTitle()
	{
		String GameName=Application.props.getProperty("nazwaGry");
		
		GameName=GameName.substring(0, GameName.indexOf('['));
		
		JLabel title=new JLabel(GameName,SwingConstants.CENTER);
		title.setSize(500,500);
		title.setFont(title.getFont().deriveFont(20.0f));
		
		add(title);
	}
	
	/**
	 * Tworzy przyciski w menu
	 */
	private void createButtons()
	{
		newGame=new JButton("New Game");
		pause=new JButton("Pause");
		options=new JButton("Options");
		highscore=new JButton("Highscore");
		exit=new JButton("Exit");
		
		newGame.addActionListener(this);
		pause.addActionListener(this);
		options.addActionListener(this);
		highscore.addActionListener(this);
		exit.addActionListener(this);
		
		add(newGame);
		add(pause);
		add(options);
		add(highscore);
		add(exit);
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();

		if(source == newGame)
		{
				Application.mainWindow.poziomy.removeAll();
				Application.mainWindow.poziomy.repaint();
				Application.mainWindow.isStarted=true;
				Application.mainWindow.liczniki.resetNumberOfSeconds();
				if(!Application.mainWindow.liczniki.isThreadGoing())
					Application.mainWindow.liczniki.startThread();
				Application.mainWindow.poziomy.startGame();
		}

		else if(source == pause)
		{
			Application.mainWindow.isPaused=!Application.mainWindow.isPaused;
			Application.mainWindow.liczniki.repaint();
		}
		
		else if(source == options)
		{
			if(!Application.mainWindow.isStarted)
				Application.mainWindow.poziomy.levelDifficulty = (String) JOptionPane.showInputDialog(Application.mainWindow, "The lowest, the easiest","Difficulty", JOptionPane.QUESTION_MESSAGE, null,Application.mainWindow.poziomy.choice,Application.mainWindow.poziomy.levelDifficulty);
		}
		
		else if(source == highscore)
			JOptionPane.showMessageDialog(Application.mainWindow, Application.mainWindow.wyniki.exportAsString(),"Highscore",JOptionPane.PLAIN_MESSAGE);
			
		else if(source == exit)
		{
			Application.mainWindow.setVisible(false);
			Application.mainWindow.dispose();
			Application.mainWindow.klient.close();
		}
	}
}
