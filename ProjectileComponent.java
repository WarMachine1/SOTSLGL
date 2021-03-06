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
    boolean destroyed;

    double speed;
    int team;
    int weapon;

    Rectangle2D.Double rec;

    ImageIcon projpic;
    Image projimg;

    public ProjectileComponent(ArrayList<Double> start, double startDamage, int t, int w)
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
        destroyed = false;
        speed = Math.sqrt(Math.pow(xVel, 2) + Math.pow(yVel, 2));
        weapon = w;
        if(weapon == 1)
        {
            projpic = new ImageIcon("wep1.png");
        }
        else if(weapon == 2)
        {
            projpic = new ImageIcon("wep2.png");
        }
        else if (weapon == 3)
        {
            projpic = new ImageIcon("wep3.png");
        }
        else projpic = new ImageIcon("wep1.png");
        projimg = projpic.getImage();

    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
//         g2.setPaint(Color.GREEN);
        //         g2.fill(this.getHitBox());
        if(!destroyed)
        {
            g2.translate(xPos+scrollX, yPos+scrollY);
            g2.rotate(theta + (Math.PI/2.0));
            g2.setPaint(Color.GREEN);
            //g2.drawImage(rec);

            g2.drawImage(projimg, 0,0,projpic.getIconWidth(), projpic.getIconHeight(), null, null);
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

    public void destroy()
    {
        projpic=null;
        projimg=null;
        destroyed = true;
    }

    public boolean getDestroyed()
    {
        return destroyed;
    }

    public double getDamage()
    {
        return damage;
    }

    public Area getHitBox()
    {
        if(destroyed)
        {
            return new Area(new Polygon());
        }
        Rectangle2D.Double r = new Rectangle2D.Double(0, 0, (double) projpic.getIconWidth(), (double) projpic.getIconHeight());
        Area aR = new Area(r);
        AffineTransform aRRotate = new AffineTransform();
        aRRotate.translate(xPos+scrollX, yPos+scrollY);
        aRRotate.rotate((theta + (Math.PI)/2.0));
        aR.transform(aRRotate);
        return aR;
    }
}