import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class ProjectileComponent extends JComponent
{
    double xPos;
    double yPos;
    double theta;
    double xVel; 
    double yVel;

    double damage;

    double scrollX;
    double scrollY;

    double speed;
    int team;

    Rectangle2D.Double rec;

    public ProjectileComponent(ArrayList<Double> start, double startDamage, int t)
    {
        xPos = start.get(0);
        yPos = start.get(1);
        theta = start.get(2);
        xVel = start.get(3);
        yVel = start.get(4);
        scrollX = 0;
        scrollY = 0;
        team = t;
        damage = startDamage;

        speed = Math.sqrt(Math.pow(xVel, 2) + Math.pow(yVel, 2));

        rec = new Rectangle2D.Double(-10, -10, 20, 20);
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        g2.translate(xPos+scrollX, yPos+scrollY);
        g2.rotate(theta + (Math.PI/2.0));
        g2.setPaint(Color.GREEN);
        g2.fill(rec);
        
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
        addPosition(speed * Math.cos(theta), speed * Math.sin(theta));
    }
    
    public Point2D.Double getPosition()
    {
        return new Point2D.Double(xPos, yPos);
    }
    
    public int getTeam()
    {
        return team;
    }
}