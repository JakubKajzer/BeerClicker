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
 * Klasa główna całej aplikacji, odpowiada za szkielet interfejsu
 */
public class Application extends JFrame 
{
	/**
	 * Okno główne aplikacji
	 */
	 public static Application mainWindow;
    /**
     * Przechowuje ustawienia pliku konfiguracyjnego
     */
    public static Properties props=new Properties();
    
    
    /**
     * Panel menu
     */
    private Menu menuPanel;
    /**
     * Tło planszy
     */
	private Background tlo;
	/** 
	 * Wyświetlanie poziomu i czasu
	 */
	public Counters liczniki;
	/**
	 * Wyświetlanie obiektów
	 */
	public  Level poziomy;
	/**
	 *Zmienna przechowująca aktualny poziom gry
	 */
	public static int LevelNumber;
	/**
	 * Zmienna ustawiająca flagę pauzy w grze
	 */
	public boolean isPaused=false;
	/**
	 * Zmienna ustawiająca flagę startu gry
	 */ 
	public boolean isStarted=false;
    
    /**
     * Klient sieciowy gry
     */
    public static Client klient= new Client();
    
    /**
     * Zmienna przechowująca stan serwera
     */
    private boolean isServerOnline=false;
    /**
	 * Obsługa najlepszych wyników
	 */
	public Highscore wyniki;
    /**
     * Konstruktor wywołujący funkcje inicjacji UI
     */
     
    public Application() 
    {
		//System.out.println("DEBUG: konstruktor Application");
		isServerOnline=klient.checkServer();
		LoadConfigFile();
        initUI();
        wyniki=new Highscore(isServerOnline);
    }

	  /**
	 * Metoda wczytująca plik konifguracyjny
	 */
	private void LoadConfigFile() 
	{
		String file;
		
		if(isServerOnline)
		{
			klient.downloadParamFile();
			file="s_temp.txt";
		}
		else file="par.txt";
		
		
		
		
		try (Reader r = new BufferedReader(new FileReader(file))) 
		{
			props.load(r);
		} catch (FileNotFoundException fnfe) 
		{
			System.out.println("Nie znaleziono pliku parametrycznego par.txt"); 
		} catch (IOException ioe) 
		{
			System.out.println("Wystąpil błąd odczytu pliku parametrycznego par.txt");
		}
		props.forEach( (nazwaParametru, wartoscParametru) -> 
		{
			System.out.println("[" + nazwaParametru + "]=[" + wartoscParametru + "]");
		});
	}
	
	    /**
	 * Inicjuje interfejs, ustawia wymiary okna i tytuł okna
	 */
    private void initUI() 
    {
    
    
		LevelNumber=Integer.parseInt(props.getProperty("numeracjaPoziomowZaczynaSieOd"));
		
		int x=Integer.parseInt(props.getProperty("poczatkowaSzerokoscPlanszy"));
		int y=Integer.parseInt(props.getProperty("poczatkowaWysokoscPlanszy"));
		
        setSize(x, y);

        setTitle(props.getProperty("nazwaGry"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("proze/projekt/grafika/kursor.png").getImage(),new Point(0,0),"custom cursor"));
		setMinimumSize(new Dimension(500,350));
		
		
		menuPanel = new Menu();
		add(menuPanel,BorderLayout.EAST);
		
		tlo= new Background();
		add(tlo);
		
		liczniki= new Counters();
		tlo.add(liczniki,BorderLayout.SOUTH);
		
		
		poziomy=new Level(isServerOnline);
		tlo.add(poziomy,BorderLayout.CENTER);
		
		
    }    
    /**
     * Metoda zwracająca szerokość okna gry
     */
    public int getGameWindowWidth()
    {
		return tlo.getWidth();
	}
	/**
     * Metoda zwracająca wysokość okna gry
     */
	public int getGameWindowHeight()
    {
		return tlo.getHeight();
	}
    
    /**
	 * Tworzy główne okno aplikacji
	 * @param args nieuzywane.
	 */
    public static void main(String[] args) 
    {
        EventQueue.invokeLater(() -> 
        {
            mainWindow = new Application();
            mainWindow.setVisible(true);
        });
    }
    
    
    
}
