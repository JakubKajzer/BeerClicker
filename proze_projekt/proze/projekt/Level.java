/**
 * @author Jakub Kajzer
 * @author Bartłomiej Traczyk
 * 
 */

package proze.projekt;


import javax.swing.*;
import java.util.Properties;
import java.io.*;
import java.awt.*;


/**
 * Klasa odpowiedzialna za generowanie poziomów
 */
public class Level extends JPanel
{
	
	private int rozmiar=(int)(Float.parseFloat(Application.props.getProperty("poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy"))*Integer.parseInt(Application.props.getProperty("poczatkowaSzerokoscPlanszy"))/100);
	
	/**
	 * Zmienna przechowująca wybrany poziom trudności
	 */
	public String levelDifficulty="1";
	/**
	 * Zmienna przechowująca dostępne poziomy trudności
	 */
	public String[] choice= new String[Integer.parseInt(Application.props.getProperty("liczbaStopniTrudnosci"))];
	
	private int howManyElements;
	private int howManyLeft;
	
	private int maxVelocity;
	
	private Properties config=new Properties();
	
	private Thread progressCheck;
	/**
	 * Konstruktor ustawiający wątek, wygląd i parametry obiektów
	 */
	public Level(boolean isServer)
	{
		//System.out.println("DEBUG: konstruktor Level");
		loadLevelFile(isServer);
		maxVelocity=Integer.parseInt(config.getProperty("maksymalnaPredkoscObiektow"));
		for(int i=0;i < Integer.parseInt(Application.props.getProperty("liczbaStopniTrudnosci"));i++)
			choice[i]=""+(i+1);
		
		
		setLayout(null);
		setVisible(true);
        setOpaque(false);
        setDoubleBuffered(true);
        
        progressCheck = new Thread(new Runnable() 
        {
			
            public void run() 
            {
				
                while (true) 
                {
					
				   if(Application.mainWindow.isStarted)
					if(howManyLeft==0)
					{
						howManyElements=(int)(howManyElements*(1+0.01*Integer.parseInt(Application.mainWindow.props.getProperty("zmianaStopniaTrudnosci"))));
						howManyLeft=howManyElements;
						if(Application.mainWindow.LevelNumber<=Integer.parseInt(Application.mainWindow.props.getProperty("liczbaPoziomow")))
						{
							Application.mainWindow.LevelNumber++;
							addElements(howManyElements);
						}
						else 
						{
							Application.mainWindow.isStarted=false;
							Application.mainWindow.liczniki.stopThread();
							
							String nazwa = (String) JOptionPane.showInputDialog(Application.mainWindow, "You won! Your score: "+Application.mainWindow.liczniki.getNumberOfSeconds()+"\nEnter your name:");
							if(nazwa.equals(""))
								Application.mainWindow.wyniki.addToList("noname",Application.mainWindow.liczniki.getNumberOfSeconds());
							else
								Application.mainWindow.wyniki.addToList(nazwa.replaceAll(" ",""),Application.mainWindow.liczniki.getNumberOfSeconds());
						}
					}
						
                    try 
                    {
						Thread.sleep(1000/60);
					} 
					catch (Exception ex) {}
                }
            }
        });
	}
	
	/**
	 * Funkcja usuwająca wskazany obiekt z planszy
	 * @param c Obiekt gry
	 */
	public void removeObject(JComponent c)
	{
		remove(c);
		revalidate();
		repaint();
		Application.mainWindow.liczniki.repaint();
		howManyLeft--;
	}
	
	/**
	 * Funkcja dodająca daną ilość elementów na plansze
	 * @param ile Ilość dodawanych elementów
	 */
	public void addElements(int ile)
	{
		for(int i=0; i<ile;i++)
		{
			if(Application.props.getProperty("obiektyGry").equals("figuryGeometryczne"))
				add(new Figure(Application.props.getProperty("figuraObiektuGry"),rozmiar,rozmiar,maxVelocity));
			else
				add(new Figure(Application.props.getProperty("obiektyGry"),rozmiar,rozmiar,maxVelocity));
		}
	}
	
	/**
	 * Funkcja odpowiadająca za zerowanie poziomu gry i dodaniu odpowiedniej ilości obiektów
	 */
	public void startGame()
	{
		if(Integer.parseInt(config.getProperty("liczbaElementow"))<=1)
			howManyElements=1;
		else
			howManyElements = Integer.parseInt(config.getProperty("liczbaElementow"))*Integer.parseInt(levelDifficulty);
		howManyLeft = howManyElements;
		
		Application.mainWindow.LevelNumber=Integer.parseInt(Application.mainWindow.props.getProperty("numeracjaPoziomowZaczynaSieOd"));
		addElements(howManyElements);
		if(!progressCheck.isAlive())
			progressCheck.start();
	}
	
	/**
	 * Funkcja wczytująca plik parametryzujący poziomy gry
	 */
	private void loadLevelFile(boolean isServer) 
	{
		String file;
		
		if(isServer)
		{
			Application.mainWindow.klient.downloadLevelFile();
			file="s_temp.txt";
		}
		else file=Application.mainWindow.props.getProperty("nazwaBazowaPlikuZOpisemPoziomu")+"."+Application.mainWindow.props.getProperty("rozszerzeniePlikuZOpisemPoziomu");
		try (Reader r = new BufferedReader(new FileReader(file)))
		{
			config.load(r);
		} catch (FileNotFoundException fnfe) 
		{
			System.out.println("Nie znaleziono pliku konfiguracji poziomow"); 
		} catch (IOException ioe) 
		{
			System.out.println("Wystąpil błąd odczytu pliku konfiguracji poziomow");
		}
		config.forEach( (nazwaParametru, wartoscParametru) -> 
		{
			System.out.println("[" + nazwaParametru + "]=[" + wartoscParametru + "]");
		});
	}
}
