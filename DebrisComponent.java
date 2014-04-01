import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class DebrisComponent extends JComponent
{
    double xPos;
    double yPos;
    double theta;
    double xVel; 
    double yVel;
    double turnSpeed;

    double scrollX;
    double scrollY;
    boolean destroyed;

    boolean useExplosion;
    
    double speed;
    long time;
    ImageIcon explosionIcon;
    Image explosionImage;
    
    ImageIcon exGifIcon;
    Image exGifImage;

    public DebrisComponent(ArrayList<Double> start, long t, String fileName, int team, int num, boolean exploding)
    {
        Random r = new Random();

        xPos = start.get(0);
        yPos = start.get(1);
        //theta = r.nextInt(2)/10.0 - 0.05;
        theta = r.nextInt(628)/100.0;
        xVel = start.get(3);
        yVel = start.get(4);
        turnSpeed = start.get(5);
        System.out.println(start.toString());

        useExplosion = exploding;
        
        scrollX = 0;
        scrollY = 0;
        time = t;
        destroyed = false;
        speed = Math.sqrt(Math.pow(xVel, 2) + Math.pow(yVel, 2));
        String tColor = "";
        if(team == 1) tColor = "Red";
        else tColor = "Blue";
        
       
        explosionIcon = new ImageIcon("debris/" + fileName + tColor + num + ".png");
        explosionImage = explosionIcon.getImage();
        
        if(exploding)
        {
            exGifIcon = new ImageIcon("debris/explosion.gif");
            exGifImage = exGifIcon.getImage();
        }
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        if(!destroyed)
        {
            g2.translate(xPos+scrollX, yPos+scrollY);
            g2.rotate(theta + (Math.PI/2.0));

            //g2.setPaint(Color.GREEN);
            //g2.fill(new Rectangle2D.Double(0,0,100,100));
            //System.out.println("test");
            g2.drawImage(explosionImage, explosionIcon.getIconWidth()/4,explosionIcon.getIconHeight()/4,explosionIcon.getIconWidth()/2, explosionIcon.getIconHeight()/2, null, null);
            if(useExplosion && time + 400 < System.currentTimeMillis())
            {
                g2.drawImage(exGifImage, 0, 0, exGifIcon.getIconWidth(), exGifIcon.getIconHeight(), null, null);
            }
        }

    }

    public void setScroll(double x, double y)
    {
        scrollX += x;
        scrollY += y;
    }

    public void setPosition(double x, double y)
    {
        xPos = x-scrollX;
        yPos = y-scrollY;
    }

    public void addPosition(double x, double y) //adds position
    {
        xPos+=x;
        yPos+=y;
    }

    public void setVelocity(double x, double y) //sets velocity in x and y
    {
        xVel = x;
        yVel = y;

        speed = Math.sqrt(Math.pow(xVel, 2) + Math.pow(yVel, 2));
    }

    public void doVel() //moves the ship based on current x and y velocity
    {
        addPosition(3.0, 3.0);
        theta = theta + turnSpeed;
    }

    public Point2D.Double getPosition()
    {
        return new Point2D.Double(xPos, yPos);
    }

    public void destroy()
    {
        destroyed = true;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public long getTime()
    {
        return time;
    }

}