/**
 * 
 */
package proze.projekt.generator;

import java.io.FileReader;
import java.util.Properties;

/**
 * Klasa przechowujaca glowny parametr gry. Singleton. Nie wolno samodzielnie modyfikowac
 * tej klasy poza zmiana:
 * <ol>
 * <li>kodowania,</li>
 * <li>zdania pakietyzacji,</li>
 * <li>komentarzy,</li>
 * <li>sciezki do pliku {@code ./pin.txt}.</li>
 * </ol>
 * @author kmi
 * @version 1.0.0  6 marca 2019 04:11
 */
public enum PIN {
	/**
	 * Singleton tej klasy.
	 */
	PIN;
	
	/**
	 * Pin gry.
	 */
	 public final int pin;
	
	/**
	 * Autorzy calego projektu.
	 */
	 public final String autorzy; 
	
	/**
	 * Wlasnosci wczytane z pliku pin.txt. 
	 */
	private Properties wlasnosci = null;
	
	/**
	 * Konstruktor wczytujacy pin. Od razu uruchamia leniwa inicjacje.
	 * Dlaczego w nim jest | a nie || ?!  
	 * Poniewaz operatory '<' i '>' zwracaja 0 lub 1, wiec operator bitowy daje rade
	 */
	private PIN() {
		initializeLazy();
		String pin$ = null;
		try {
			pin$ = wlasnosci.getProperty("pin").trim();
			Integer.parseInt(pin$);
		} catch (Throwable thr) {
			wypiszKomunikatyIZakoncz("Blad parsowania pinu", "pin=" + pin$, thr);
		}
		pin = Integer.parseInt(pin$);
		if (pin < 1000 | pin > 9999) {
			wypiszKomunikatyIZakoncz("Pin poza dopuszczalnym zakresem", pin);
		}
		autorzy = wlasnosci.getProperty("autorzy").trim();
	}
	
	/**
	 * Leniwe ladowanie pliku wlasnosci. Wzorzec leniwej inicjacji. Metoda powinna
	 * byc uzyta dopiero w momencie potrzeby pierwszego skorzystania z wlasnosci
	 * tu przechowywanych, poniewaz jednak projekt nie moze dzialac bez pinu - metoda
	 * jest wywolywana od razu w konstruktorze. Nasladuj leniwa inicjacje - inicjuj
	 * obiekty dopiero wtedy, gdy to jest potrzebne. Inicjacja rozumiana jest tu jako
	 * odwolanie sie do glebszych warstw np. systemu operacyjnego, systemu plikow, bazy
	 * danych itp. W tym przypadku jest to odczyt pliku.
	 */
	private void initializeLazy() {
		if (wlasnosci == null) {
			try (FileReader fr = new FileReader("./pin.txt")) {
				(wlasnosci = new Properties()).load(fr);
			} catch (Throwable thr) {
				wypiszKomunikatyIZakoncz("Nie udalo sie wczytac pinu gry.", thr);
			}
		}
	}
	
	/**
	 * Wypisuje komunikaty i konczy dzialanie programu przez {@link java.lang.System#exit}.
	 * Dodatkowo, jesli komunikat jest wyjatkiem zostanie wywolana metoda
	 * {@link java.lang.Throwable#printStackTrace} tego wyjatku.
	 * @param tablicaKomunikatow
	 */
	void wypiszKomunikatyIZakoncz(Object... tablicaKomunikatow) {
		for (int i = 0; i < tablicaKomunikatow.length; i++) {
			System.err.print((i+1) + ": " );
			if (tablicaKomunikatow[i] instanceof Throwable) {
				((Throwable)tablicaKomunikatow[i]).printStackTrace();
			} else {
				System.err.println(tablicaKomunikatow[i].toString());
			}
		}
		System.exit(-1);
	}
	
	public String toString() {
		return "pin: " + pin + ", autorzy: " + autorzy; 
	}
	
	/**
	 * Test klasy. Argumenty nieuzywane.
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println(java.nio.file.Paths.get(".").toAbsolutePath());
		System.out.println(PIN);
	}
}
