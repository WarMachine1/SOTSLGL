import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;

public class LabelComponent extends JComponent
{
    JLabel label;
    String info;

    public LabelComponent(String title, String name)
    {
        label = new JLabel(title);
        info = name;
    }
    
    public String getTitle()
    {
        return label.getText();
    }
    
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle2D.Double selectBox = new Rectangle2D.Double(label.getX(), label.getY(), 400, 100);
        g2.setColor(Color.RED);
        g2.fill(selectBox);
        g2.draw(selectBox);
        g2.setColor(Color.BLACK);
        g2.drawString(info, label.getX()+20, label.getY()+20);
    }
}
