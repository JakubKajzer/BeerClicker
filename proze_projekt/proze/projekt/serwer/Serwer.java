
package proze.projekt.serwer;

import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.awt.*;

/**
 * Klasa implementująca serwer
 *
 */
public class Serwer {
    
    //static ServerSocket variable
    //socket server port on which it will listen
    private  int port = 2137;
    private Socket socket;
    private String temp;
    private ObjectInputStream ois;
    
    
    /**
     * Funkcja main serwera, inicjalizuje go
     */
    public static void main(String args[]) throws IOException, ClassNotFoundException
    {
		Serwer server = new Serwer();
    }
    /**
     * Konstruktor, pętla główna serwera
     */
    Serwer()
    {
		System.out.println("Ready!");
		 try(ServerSocket server = new ServerSocket(port);)
		 {
			while(true)
			{
				socket = server.accept();
				ois = new ObjectInputStream(socket.getInputStream());
				String message = (String) ois.readObject();
				
				if(message.equals("GODMODE"))
					sendMessage("EVERYTHING_GOOD");
				if(message.equals("GIB_ME_LEVEL"))
					sendFile("s_level.gra");
				if(message.equals("I_BEG_YOU_FOR_HIGHSCORES"))
					sendFile("s_bests.txt");
				if(message.equals("I_NEED_SOME_PARAMS"))
					sendFile("s_par.txt");
				if(message.equals("REFRESH_BEAUTIFUL_PEOPLE"))
					updateFile("s_bests.txt");
				if(message.equals("IM_DONE_BRO")) 
				{
					sendMessage("SURE_BRO");
					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException ie)
					{
						ie.printStackTrace();
					}
					break;
				}
				ois.close();
				socket.close();
				
				
			}
			server.close();
		}
		catch(IOException a){System.out.println("IOException");}
		catch(ClassNotFoundException a) {System.out.println("ClassNotFoundException");}
	}
    /**
     * Funkcja wysyłająca plik
     * @param what Nazwa wysyłanego pliku
     */
    private void sendFile(String what)
    {
		try(ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()))
		{
			try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("proze/projekt/serwer/"+what), "UTF8"))) 
			{
				temp=r.readLine();
				for(int i=0 ; temp != null ; i++)
				 {
						oos.writeObject(temp);
						temp=r.readLine();
				 }

			} catch (FileNotFoundException fnfe) 
			{
				System.out.println("Nie znaleziono pliku "+what); 
			} catch (IOException ioe) 
			{
				System.out.println("Wystąpil błąd odczytu pliku "+what);
			}
			oos.writeObject("STOP");
			oos.close();
		}
		catch(IOException a) {}
	}
	
	 /**
     * Funkcja wysyłająca wiadomość
     * @param what Treść wiadomości
     */
	private void sendMessage(String what)
    {
		try(ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()))
		{
			oos.writeObject(what);
			oos.close();
		}
		catch(IOException a) {}
	}
	 /**
     * Funkcja ściągająca plik z serwera
     * @param what Nazwa pobranego pliku
     */
    public void updateFile(String what) 
	{
		String message="NULL";
            
				System.out.println("Czekam na wiadomość");
				
				try(PrintWriter zapis = new PrintWriter("proze/projekt/serwer/"+what))
				{
					while(true)
					{
						try
						{
							message = (String) ois.readObject();
						}catch(IOException a) {System.out.println("IOException updateFile");}
						 catch(ClassNotFoundException a){System.out.println("ClassNotFoundException");}
						if(message.equals("STOP")) break;
						zapis.println(message);
					}
					zapis.close();
				}
				catch (FileNotFoundException fnfe) 
				{
					System.out.println("Nie znaleziono pliku"); 
				}
				
			
	}
}
