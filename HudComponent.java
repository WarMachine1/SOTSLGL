import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;

public class HudComponent extends JComponent
{
    Rectangle2D.Double rec;

    public HudComponent()
    {
        rec = new Rectangle2D.Double(0,0,300,(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setPaint(Color.BLACK);
        g2.fill(rec);
        g2.setPaint(Color.WHITE);
        g2.drawString("Yo, check it out!", 0, 12);
        //g2.setPaint(Color.BLUE);
        //g2.fill(recb);

    }

}
