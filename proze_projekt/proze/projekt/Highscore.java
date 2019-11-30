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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Klasa odpowiedzialna za obsługę pliku najlepszych wyników
 */
public class Highscore
{
	private String temp;
	
	private String[][] list = new String[6][2];
	
	private boolean isServerOn;
	
	/**
	 * Konstruktor, wczytuje plik
	 */
    public Highscore(boolean isServer) 
    {
		//System.out.println("DEBUG: konstruktor Highscore");
        loadFile(isServer);
        isServerOn=isServer;
    }
    /**
     * Funkcja odpowiedzialna za wczytywanie pliku
     */
    private void loadFile(boolean isServer)
    {
		String file;
		if(isServer)
		{
			Application.mainWindow.klient.downloadHighscoreFile();
			file="s_highscore.txt";
		}
		else
		file="bests.txt";
		
		try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) 
		{
			temp=r.readLine();
			for(int i=0 ; temp != null ; i++)
			 {
					list[i]=temp.split(" ");
					temp=r.readLine();
		     }

		} catch (FileNotFoundException fnfe) 
		{
			System.out.println("Nie znaleziono pliku bests.txt"); 
		} catch (IOException ioe) 
		{
			System.out.println("Wystąpil błąd odczytu pliku bests.txt");
		}
	}
	
	
	/**
	 * Funkcja zwracająca całą liste wyników w jednym stringu
	 * @return Wyniki oddzielone enterami
	 */
	public String exportAsString()
	{
		return "1. "+list[0][0]+" "+list[0][1]+"\n"+"2. "+list[1][0]+" "+list[1][1]+"\n"+"3. "+list[2][0]+" "+list[2][1]+"\n"+"4. "+list[3][0]+" "+list[3][1]+"\n"+"5. "+list[4][0]+" "+list[4][1];
	}
		
	/**
	 * Funkcja dodaje wynik do listy najlepszych wyników i ustawia na odpowiedniej pozycji
	 */	
	public void addToList(String name, int points)
	{
		list[5][0]=name;
		list[5][1]=""+points;
		String temp;
		
		for(int j = 0; j < 5; j++)
			for(int i = 0; i < 5; i++)
			if(Integer.parseInt(list[i][1]) > Integer.parseInt(list[i + 1][1])) 
			{
				temp=list[i][1];
				list[i][1]=list[i + 1][1];
				list[i + 1][1]=temp;
				
				temp=list[i][0];
				list[i][0]=list[i + 1][0];
				list[i + 1][0]=temp;
			}
		temp=null;
		saveHighscores(isServerOn);
	}
	
	/**
	 * Funkcja odpowiadająca za zapisanie wyników do pliku
	 */
	public void saveHighscores(boolean isServer)
	{
		if(isServer)
		{
			try(PrintWriter zapis = new PrintWriter("s_highscore.txt"))
			{
				for(int i=0; i< 5;i++)
					zapis.println(list[i][0]+" "+list[i][1]);
				zapis.close();
			}
			catch (FileNotFoundException fnfe) 
			{
				System.out.println("Nie znaleziono pliku bests.txt"); 
			}
			Application.mainWindow.klient.uploadHighscoreFile();
		}
		else
		{
			try(PrintWriter zapis = new PrintWriter("bests.txt"))
			{
				for(int i=0; i< 5;i++)
					zapis.println(list[i][0]+" "+list[i][1]);
				zapis.close();
			}
			catch (FileNotFoundException fnfe) 
			{
				System.out.println("Nie znaleziono pliku bests.txt"); 
			}
		}
	}
	
}
