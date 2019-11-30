
package proze.projekt;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Klasa implementuje klienta sieciowego gry
 *
 */
public class Client 
{

	/**
	 * Pobiera plik konfiguracyjny
	 */
	public void downloadParamFile() 
	{
		
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
		String message;
            try(Socket socket = new Socket("127.0.0.1", 2137))
            {
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject("I_NEED_SOME_PARAMS");
				//read the server response message
				ois = new ObjectInputStream(socket.getInputStream());
				try(PrintWriter zapis = new PrintWriter("s_temp.txt"))
				{
					while(true)
					{
						message = (String) ois.readObject();
						if(message.equals("STOP")) break;
						zapis.println(message);
					}
					zapis.close();
				}
				catch (FileNotFoundException fnfe) 
				{
					System.out.println("Nie znaleziono pliku"); 
				}
				
				ois.close();
				oos.close();
			}
			catch(UnknownHostException a) {System.out.println("UnknownHostException");}
			catch(IOException a) {System.out.println("IOException");}
			catch(ClassNotFoundException a){System.out.println("ClassNotFoundException");}
	}
	/**
	 * Pobiera plik z najlepszymi wynikami
	 */
	public void downloadHighscoreFile() 
	{
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
		String message;
            try(Socket socket = new Socket("127.0.0.1", 2137))
            {
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject("I_BEG_YOU_FOR_HIGHSCORES");
				//read the server response message
				ois = new ObjectInputStream(socket.getInputStream());
				try(PrintWriter zapis = new PrintWriter("s_highscore.txt"))
				{
					while(true)
					{
						message = (String) ois.readObject();
						if(message.equals("STOP")) break;
						zapis.println(message);
					}
					zapis.close();
				}
				catch (FileNotFoundException fnfe) 
				{
					System.out.println("Nie znaleziono pliku"); 
				}
				
				ois.close();
				oos.close();
			}
			catch(UnknownHostException a) {System.out.println("UnknownHostException");}
			catch(IOException a) {System.out.println("IOException");}
			catch(ClassNotFoundException a){System.out.println("ClassNotFoundException");}
	}
	/**
	 * Pobiera plik opisu poziomów
	 */
	public void downloadLevelFile() 
	{
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
		String message;
            try(Socket socket = new Socket("127.0.0.1", 2137))
            {
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject("GIB_ME_LEVEL");
				//read the server response message
				ois = new ObjectInputStream(socket.getInputStream());
				try(PrintWriter zapis = new PrintWriter("s_temp.txt"))
				{
					while(true)
					{
						message = (String) ois.readObject();
						if(message.equals("STOP")) break;
						zapis.println(message);
					}
					zapis.close();
				}
				catch (FileNotFoundException fnfe) 
				{
					System.out.println("Nie znaleziono pliku"); 
				}
				
				ois.close();
				oos.close();
			}
			catch(UnknownHostException a) {System.out.println("UnknownHostException");}
			catch(IOException a) {System.out.println("IOException");}
			catch(ClassNotFoundException a){System.out.println("ClassNotFoundException");}
	}
    /**
	 * Sprawdza czy serwer jest aktywny
	 */
    public boolean checkServer()
    {
		String message="NULL";
		boolean avaiable=true;
		try (Socket s = new Socket("127.0.0.1", 2137)) 
		{
			ObjectOutputStream oos= new ObjectOutputStream(s.getOutputStream());
			oos.writeObject("GODMODE");
			try(ObjectInputStream ois = new ObjectInputStream(s.getInputStream()))
			{
				message = (String) ois.readObject();
				System.out.println("Server says: "+message);
				
			}
			catch(ClassNotFoundException a)
			{
				avaiable=false;
			}
		} 
		catch (IOException ex)
		{
			avaiable=false;
		}
		return avaiable;
	}
	/**
	 * Zamyka połączenie z serwerem
	 */
	public void close()
	{
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
		String message;
            try(Socket socket = new Socket("127.0.0.1", 2137))
            {
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject("IM_DONE_BRO");
				ois = new ObjectInputStream(socket.getInputStream());
				message = (String) ois.readObject();
				System.out.println("Server says: "+message);						
				ois.close();
				oos.close();
			}
			catch(UnknownHostException a) {System.out.println("UnknownHostException");}
			catch(IOException a) {System.out.println("IOException");}
			catch(ClassNotFoundException a){System.out.println("ClassNotFoundException");}
	}
	/**
	 * Aktualizuje plik najlepszych wyników na serwerze
	 */
	public void uploadHighscoreFile() 
	{
		
		String temp;
		
            try(Socket socket = new Socket("127.0.0.1", 2137))
            {
				try(ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()))
				{
					oos.writeObject("REFRESH_BEAUTIFUL_PEOPLE");
					try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("s_highscore.txt"), "UTF8"))) 
					{
						temp=r.readLine();
						for(int i=0 ; temp != null ; i++)
						 {
								oos.writeObject(temp);
								temp=r.readLine();
						 }
						 oos.writeObject("STOP");

					} 
					catch (FileNotFoundException fnfe) 
					{
						System.out.println("Nie znaleziono pliku "); 
					} 
					catch (IOException ioe) 
					{
						System.out.println("Wystąpil błąd odczytu pliku ");
					}
					oos.writeObject("STOP");
					oos.close();
				}
				catch(IOException a) {}
			}
			catch(UnknownHostException a){}
			catch(IOException a){}
	}
	
	
	
}
