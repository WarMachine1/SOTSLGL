import javax.swing.*;
import java.awt.*;

public class SkyboxComponent extends JComponent
{
    ImageIcon skyico;
    Image skyimg;
    double xPos;
    double yPos;
    double scrollX;
    double scrollY;
    int xS;
    int yS;

    public SkyboxComponent()
    {
        skyico = new ImageIcon("sky.jpg");
        xPos = 0;
        yPos = 0;
        scrollX = 0;
        scrollY = 0;
        skyimg = skyico.getImage();
        xS = skyico.getIconWidth(); //x Size
        yS = skyico.getIconHeight(); //y Size
        
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(.5, .5);
        g2.translate(xPos+scrollX, yPos+scrollY);

        //g2.drawString("This way up!", -50, -50);
        //g2.setPaint(Color.RED);
        //g2.fill(rec);
        g2.drawImage(skyimg, (int) (-scrollX/xS) * xS,(int) (-scrollY/yS) * yS, xS, yS, new Color(0,0,0,0), null);
        g2.drawImage(skyimg, (int) (-scrollX/xS+1) * xS,(int) (-scrollY/yS) * yS, xS, yS, new Color(0,0,0,0), null);
        g2.drawImage(skyimg, (int) (-scrollX/xS-1) * xS,(int) (-scrollY/yS) * yS, xS, yS, new Color(0,0,0,0), null);
        g2.drawImage(skyimg, (int) (-scrollX/xS+1) * xS,(int) (-scrollY/yS+1) * yS, xS, yS, new Color(0,0,0,0), null);
        g2.drawImage(skyimg, (int) (-scrollX/xS-1) * xS,(int) (-scrollY/yS-1) * yS, xS, yS, new Color(0,0,0,0), null);
        g2.drawImage(skyimg, (int) (-scrollX/xS+1) * xS,(int) (-scrollY/yS-1) * yS, xS, yS, new Color(0,0,0,0), null);
        g2.drawImage(skyimg, (int) (-scrollX/xS-1) * xS,(int) (-scrollY/yS+1) * yS, xS, yS, new Color(0,0,0,0), null);
        g2.drawImage(skyimg, (int) (-scrollX/xS) * xS,(int) (-scrollY/yS+1) * yS, xS, yS, new Color(0,0,0,0), null);
        g2.drawImage(skyimg, (int) (-scrollX/xS) * xS,(int) (-scrollY/yS-1) * yS, xS, yS, new Color(0,0,0,0), null);

        
        //g2.setPaint(Color.BLUE);
        //g2.fill(recb);

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

}
