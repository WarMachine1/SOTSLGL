import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;

public class PathComponent extends JComponent
{
    Rectangle2D.Double rec;
    double xPos;
    double yPos;
    double scrollX;
    double scrollY;

    boolean active;
    
    ImageIcon reticlePic;
    Image reticleImg;
    public PathComponent(double x, double y, boolean target)
    {
        rec = new Rectangle2D.Double(-20,-20,40,40);
        xPos = x;
        yPos = y;
        scrollX = 0;
        scrollY = 0;
        active = false;
        reticlePic = new ImageIcon("failure.png");
        if(target)
        {
            reticlePic = new ImageIcon("reticleRed.png");
        }
        else reticlePic = new ImageIcon("reticleBlue.png");
        reticleImg = reticlePic.getImage();
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        g2.translate(xPos + scrollX, yPos + scrollY);
        g2.setPaint(Color.BLUE);
        //g2.fill(rec);
        g2.drawImage(reticleImg, -reticlePic.getIconWidth()/4,-reticlePic.getIconHeight()/4, reticlePic.getIconWidth()/2, reticlePic.getIconHeight()/2, null, null);

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

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean a)
    {
        active = a;
    }
}
