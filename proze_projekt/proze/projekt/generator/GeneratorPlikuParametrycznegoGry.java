/**
 * 
 */
package proze.projekt.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Properties;

/**
 * Program tworzy glowny plik parametryczny gry. 
 * 
 * @author kmi
 * @version 1.0.0  6 marca 2019 18:30
 */
public class GeneratorPlikuParametrycznegoGry {
	/**
	 * Pin generowanej gry.
	 */
	private final PIN pin = PIN.PIN;
	
	/**
	 * Pint generowanej gry, jako int.
	 */
	private final int Pint = pin.pin;
	
	/**
	 * "Maly pin" - suma cyfr pinu, obliczana tak dlugo, az bedzie mniejsza
	 * od 10.
	 */
	private int p = Pint; //tutaj wszystko jest okej
	
	final private String lsep = System.getProperty("line.separator");
	
	/**
	 * Przygotowuje obiekt do obliczania cech gry.
	 */
	GeneratorPlikuParametrycznegoGry() {
		do {
			p = sumaCyfr(p); 
		} while (p > 9);
	}
	
	final private String nazwyGier[] = {
			"Strzelanka", "Strzelanie do obiektow", "Zestrzel obiekty", "Strzelanie",
			"Gra na PROZE, Strzelanka", "Shooter", "ShootAndGo",
	};
	
	final private String nazwyPlikowPoziomow[] = {
			"Poziom", "poziom", "poziom#", "level", "Level", "PoziomNr", "PoziomGryNumer_",
	};
	
	final private String rozszerzenia[] = {
			"txt", "properties", "prop", "props", "text", "gra",
	};
	
	final private String figuryObiektow[] = {
			"kwadraty", "prostokaty", "trojkaty", "kolka",
	};
	
	private final String losowo(String...strings) {
		return strings[(int)(Math.random() * strings.length)];
	}
	
	private final double losowo(double min, double max) {
		assert min <= max : "losowo(min, max): minimum wieksze od maksimum?";
		return min + Math.random() * (max - min);
	}
	
	private final int losowo(int min, int max) {
		assert min <= max : "losowo(min, max): minimum wieksze od maksimum?";
		return min + (int) (Math.random() * (1L + max - min));
	}
	
	private final String wgPinu(String...strings) {
		return strings[Pint % strings.length];
	}
	
	private final int wgPinu(int min, int max) {
		assert min <= max : "wgPinu(min, max): minimum wieksze od maksimum?";
		return min + Pint % (max - min + 1);
	}
	
	void zapiszPlikParametryczny(String nazwaPliku) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(nazwaPliku)));
		} catch (IOException ioe) {
			pin.wypiszKomunikatyIZakoncz("Nie udalo sie otworzyc pliku parametrycznego " +
					"do zapisu", nazwaPliku, ioe);
		}
		pw.println("# Plik parametryczny gry." + lsep
				+ "# Komentarz zaczyna sie od znaku # i obowiazuje do konca linii." + lsep
				+ "# Parametry sa zapisane w postaci par {nazwa}={wartosc}." + lsep
				+ "# Plik nalezy wczytac do obiektu java.util.Properties metoda load() tej klasy" + lsep
				+ "# wg wzoru w programie generatora plikow parametrycznych." + lsep
				+ "#" + lsep
				+ "nazwaGry=" + losowo(nazwyGier) + " [" + pin + "]" + lsep 
				+ "# Nazwa gry powinna wyswietlac sie gdzies na planszy albo w tytule okna." + lsep
				+ "#" + lsep
				+ "# 1. Poziomy gry i pliki opisujace poziom gry. Zawartosc pliku opisujacego poziom" +lsep
				+ "#    trzeba opracowac i przygotowac samodzielnie wg metody stosowanej w tym pliku." + lsep
				+ "#" + lsep 
				+ "liczbaPoziomow=" + (3 + (p % 3)) + lsep
				+ "# dodatnia liczba calkowita" + lsep
				+ "nazwaBazowaPlikuZOpisemPoziomu=" + wgPinu(nazwyPlikowPoziomow) + lsep
				+ "numeracjaPoziomowZaczynaSieOd=" + wgPinu(0, 1) + lsep
				+ "# liczba 0 lub 1" + lsep
				+ "rozszerzeniePlikuZOpisemPoziomu=" + wgPinu(rozszerzenia) + lsep
				+ "#" + lsep
				+ "# 2. Stopnie trudnosci" + lsep 
				+ "#" + lsep
				+ "liczbaStopniTrudnosci=" + losowo(2, 5) + lsep
				+ "# dodatnia liczba calkowita" + lsep
				+ "zmianaStopniaTrudnosci=" + losowo(20, 45) + lsep
				+ "# liczba calkowita w zakresie 20..45 - o tyle procent zwiekszaja sie " 
				+ "i/lub zmniejszaja" + lsep + "# sie parametry wplywajace na stopien trudnosci"
				+ " np. predkosc, czas, liczba punktow do zdobycia itp." + lsep
				+ "#" + lsep
				+ "# 3. Rozne wymiary" + lsep
				+ "#" + lsep
				+ "poczatkowaSzerokoscPlanszy=" + losowo(400, 1000) + lsep
				+ "poczatkowaWysokoscPlanszy=" + losowo(250, 700) + lsep
				+ "poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy=" 
				+ (float) losowo(5d, 9d) + lsep
				+ "#" + lsep
				+ "# 4. Rysowanie tla i obiektow, do ktorych sie strzela" + lsep
				+ "#");
		if (losowo(0, 1) == 0) { // tlo jednolite
			pw.println("tlo=jednolite" + lsep
					+ "kolorTla=" + losowo(0, 255) + " " + losowo(0, 255) + " " + losowo(0, 255) + lsep
					+ "# skladowe R G B koloru tla, kazda w zakresie 0..255");
		} else { // tlo graficzne
			pw.println("tlo=plikGraficzny" + lsep
					+ "plikTla=plik.jpeg" + lsep
					+ "# w ogolnosci tu mozna wstawic (wzgledna!) sciezke do wlasnego pliku tla");
		}
		if (losowo(0, 1) == 0) { // obiekty rysowane jako ksztalty
			pw.println("obiektyGry=figuryGeometryczne" + lsep
					+ "figuraObiektuGry=" + losowo(figuryObiektow) + lsep
					+ "# jedno z: kwadraty prostokaty trojkaty kolka");
		} else { // obiekty rysowane jako pliki graficzne
			pw.println("obiektyGry=plikGraficzny" + lsep
					+ "plikObiektu=plikObiektu.jpeg" + lsep
					+ "# w ogolnosci tu mozna wstawic (wzgledna!) sciezke do wlasnego pliku z obiektem graficznym");
		}
		pw.println("#" + lsep
				+ "# Jesli uwazasz, ze warto dodac jeszcze jakies parametry dodaj je w dodatkowym" + lsep
				+ "# pliku konfiguracyjnym, obslugiwanym niezaleznie od tego pliku parametrycznego."
				);
		pw.close();
	}
	
	void wczytajIWypiszPlikParametryczny(String nazwaPliku) {
		Properties props = new Properties();
		try (Reader r = new BufferedReader(new FileReader("par.txt"))) 
		{
			props.load(r);
		} catch (FileNotFoundException fnfe) 
		{
			pin.wypiszKomunikatyIZakoncz("Nie znaleziono pliku parametrycznego",nazwaPliku, fnfe); 
		} catch (IOException ioe) 
		{
			pin.wypiszKomunikatyIZakoncz("Wystapil blad odczytu pliku parametrycznego",nazwaPliku, ioe);
		}
		props.forEach( (nazwaParametru, wartoscParametru) -> 
		{
			System.out.println("[" + nazwaParametru + "]=[" + wartoscParametru + "]");
		});
	}

	
	/**
	 * Oblicza sume cyfr podanej liczby na podstawie dzielenia i dzielenia z resztą
	 * @param a
	 * @return 
	 */
	private int sumaCyfr(int a) 
	{
	//	String s = "" + a;
	//	int sumaCyfr = 0;
	//	for (int i = 0; i < s.length(); i++) {
	//		sumaCyfr += (s.charAt(i)) - '0';
	//	}
		int sumaCyfr=a/1000+((a/100)%10)+((a%100)/10)+(a%10);
		return sumaCyfr;
	}
	
	/**
	 * Tworzy plik parametryczny (.\par.txt) gry na podstawie pinu zawartego
	 * w pliku .\pin.txt.
	 * @param args nieuzywane.
	 */
	public static void main(String... args) {
		System.out.println(PIN.PIN);
		GeneratorPlikuParametrycznegoGry cg = new GeneratorPlikuParametrycznegoGry();
		System.out.println("p: " + cg.p);
		String nazwaPlikuParametrycznego = "par.txt";
		cg.zapiszPlikParametryczny(nazwaPlikuParametrycznego);
		cg.wczytajIWypiszPlikParametryczny(nazwaPlikuParametrycznego);
	}
}
