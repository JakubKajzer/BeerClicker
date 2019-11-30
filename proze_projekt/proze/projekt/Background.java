/**
 * @author Jakub Kajzer
 * @author Bartłomiej Traczyk
 * 
 */

package proze.projekt;

import java.io.File;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;



/**
 *Klasa odpowiedzialna za wysietlanie na niej obiektów
 * 
 */
public class Background extends JPanel 
{
	/**
	 * Przechowuje wczytany obraz obiektu
	 */
	private Image img; 
	
	/**
	 * Konstruktor,sprawdza czy ma stworzyć jednolity kolor czy wczytać obraz
	 */
	public Background()
	{
		//System.out.println("DEBUG: konstruktor Background");
		if(Application.props.getProperty("tlo").equals("plikGraficzny"))
		{
			loadImage();
		}
		if(Application.props.getProperty("tlo").equals("jednolite"))
		{
			loadColor();
		}
		
	}
	
	/**
	 * Wczytuje kolor z pliku konfiguracyjnego i ustawia go na tło
	 */
	private void loadColor()
	{
		
		int[] RGB=new int[3];
		StringTokenizer st = new StringTokenizer(Application.props.getProperty("kolorTla"));
		for(int i=0;st.hasMoreTokens();i++) 
		{
			RGB[i]=Integer.parseInt(st.nextToken());
		}
		
		setBackground(new Color(RGB[0],RGB[1],RGB[2]));
		setVisible(true);
		setLayout(new BorderLayout());
	}
	
	/**
	 * Wczytuje obraz i ustawia go na tło
	 */
	private void loadImage()
	{
		try
		{
			img = ImageIO.read(new File("proze/projekt/grafika/"+Application.props.getProperty("plikTla")));
		} 
		catch (FileNotFoundException fnfe) 
		{
			System.out.println("Nie znaleziono pliku graficznego"); 
		} 
		catch (IOException ioe) 
		{
			System.out.println("Wystapil blad odczytu pliku graficznego");
		}
		
		setLayout(new BorderLayout());  
	}
	
	
	
    @Override
    public void paintComponent(Graphics g) 
    {
		super.paintComponent(g);
        g.drawImage(img, 0, 0,Application.mainWindow.getGameWindowWidth(),
        Application.mainWindow.getGameWindowHeight(),null);
    }

}
