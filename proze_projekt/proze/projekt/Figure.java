/**
 * @author Jakub Kajzer
 * @author Bartłomiej Traczyk
 * 
 */

package proze.projekt;

import java.awt.*;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.util.*;

/**
 * Klasa odpowiedzialna za generowanie obiektów
 */
public class Figure extends JComponent implements MouseListener
{
	
	private double maxSpeed;
	
	private double lastX_, lastY_;
	private double posX_, posY_;
	private double SpeedX, SpeedY;
	/**
	 * Zmienne okreslające kierunek ruchu obiektu
	 */
	private boolean dirX,dirY;
	
	/**
	 * Przechowuje grafikę obiektu
	 */
	private Image sprite;
	/**
	 * Zmienne określające rozmiar
	 */
	private int x_,y_;
	/**
	 * Zmienna pomocnicza do wybierania typu obiektu
	 */
	private String what_;
	/**
	 * Zmienna koloru figur, losowana z prawdopobieństwem 1% w każdej klatce
	 */
	Color kolorek;
	/**
	 *Konstruktor obiektu
	 * @param what Rodzaj obiektu
	 * @param x Zmienna określająca szerokość
	 * @param y Zmienna określająca wysokość
	 * @param speed Zmienna określająca prędkość
	 */
	public Figure(String what,int x, int y, int speed)
	{
		//System.out.println("DEBUG: konstruktor Figure");
		
		Random randomer = new Random();
		
		x_=x;
		y_=y;
		what_=what;
		maxSpeed=0.001*speed;
		SpeedX = maxSpeed;
        SpeedY = maxSpeed;
		dirX= randomer.nextBoolean();
		dirY= randomer.nextBoolean();
		lastX_ = Math.random()*0.9;
		lastY_ = Math.random()*0.9;
		setSize(x_,y_);
		
		setOpaque(false);
		setVisible(true);
		setDoubleBuffered(true);
		
		
			if(what_.equals("plikGraficzny"))
			try
            {
                sprite = ImageIO.read(new File("proze/projekt/grafika/"+Application.props.getProperty("plikObiektu")));
            }
            catch(Exception e){e.printStackTrace();}
		
		Thread animationThread = new Thread(new Runnable() 
        {
            public void run() 
            {
                while (true) 
                {
					if(!Application.mainWindow.isPaused)
						updatePosition();
                    repaint();
                    try 
                    {
						Thread.sleep(1000/60);
					} 
					catch (Exception ex) {}
                }
            }
        });

        animationThread.start();
        addMouseListener(this);
        
	}

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
		
		
			x_=(int)(Float.parseFloat(Application.props.getProperty("poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy"))*Application.mainWindow.getGameWindowWidth()/100);
			switch(what_)
			{
				case "kolka":
					drawCircle(g);
					break;
				case "prostokaty":
					drawRectangle(g);
					break;
				case "plikGraficzny":
					drawImage(g);
					break;
				case "trojkaty":
					drawTriangle(g);
					break;
				case "kwadraty":
					drawSquare(g);
					break;
				default:
					System.out.println("Nothing to draw! :(");
					break;
			}
			
			if(Math.random()<=0.01)
			kolorek =new Color((int)(Math.random() * 255),(int)(Math.random() * 255),(int)(Math.random() * 255));	//g.setClip((int)(lastX_*Application.mainWindow.getGameWindowWidth()),(int)(lastY_*Application.mainWindow.getGameWindowHeight()),x_,y_);
    }
	/**
	 * Metoda rysująca trójkąt równoboczny
	 */
    private void drawTriangle(Graphics g) 
    {
		g.setColor(kolorek);
		g.fillPolygon(new int[] {0, x_/2, x_}, new int[] {0, (int)(Math.round(0.5*x_*Math.sqrt(3))), 0}, 3);
		setSize(x_,x_);
    }
		/**
		 * Metoda rysująca obrazek. Na tę chwilę nazwa pliku jet ustawiona na stałe, w celu prezentacji wszystkich mozliwych obiektów gry.
		 */
       private void drawImage(Graphics g) 
    {
        g.drawImage(sprite,0,0,x_,x_,null);
        setSize(x_,x_);
    }
    
    /**
     * Metoda rysująca koło
     */
    private void drawCircle(Graphics g) 
    {
		
		g.setColor(kolorek);	
		g.fillOval(0,0,x_,x_);		
		setSize(x_,x_);
    }
    
    /**
     * Metoda rysująca prostokąt
     */
    private void drawRectangle(Graphics g) 
    {
		y_=x_;
		x_*=2;
		g.setColor(kolorek);
		g.fillRect(0,0,x_,y_);
		setSize(x_,y_);
    }
    /**
     * Metoda rysująca kwadrat, dodana w celu łatwiejszej pracy z plikiem konfiguracyjnym
     */
    private void drawSquare(Graphics g) 
    {
		g.setColor(kolorek);
		g.fillRect(0,0,x_,x_);
		setSize(x_,x_);
    }
    
	
    @Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		JComponent c = (JComponent) e.getSource();
		if(!Application.mainWindow.isPaused)
			Application.mainWindow.poziomy.removeObject(c);
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
	private void updatePosition()
	{
					if(dirX)
						posX_ = lastX_ + SpeedX;
					else
						posX_ = lastX_ - SpeedX;
						
					if(dirY)
						posY_ = lastY_ + SpeedY;
					else
						posY_ = lastY_ - SpeedY;

					if ((int)(posX_*Application.mainWindow.getGameWindowWidth()) > Application.mainWindow.getGameWindowWidth()-x_) 
					{
						dirX=false;
						SpeedX=(Math.random()*0.001);
					}
					if((int)(posX_*Application.mainWindow.getGameWindowWidth()) <= 1)
					{
						dirX=true;
						SpeedX=(Math.random()*0.001);
					}
					if ((int)(posY_*Application.mainWindow.getGameWindowHeight()) > Application.mainWindow.getGameWindowHeight()-2*y_) 
					{
						dirY=false;
						SpeedY=(Math.random()*0.001);
					}
					if((int)(posY_*Application.mainWindow.getGameWindowHeight()) <= 1)
					{
						dirY=true;
						SpeedY=(Math.random()*0.001);
					}
					
					
					lastX_ = posX_;
					lastY_ = posY_;
			
					setLocation((int)(lastX_*Application.mainWindow.getGameWindowWidth()),(int)(lastY_*Application.mainWindow.getGameWindowHeight()));
			
			
	}
	
	@Override
	public Dimension getPreferredSize() 
	{
		return new Dimension(x_, y_);
	}
}
