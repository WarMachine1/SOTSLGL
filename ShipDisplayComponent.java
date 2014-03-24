import javax.swing.*;
import java.awt.*;

public class ShipDisplayComponent extends JComponent
{
    ImageIcon shipico;
    Image shipimg;
    
    public ShipDisplayComponent(String filename)
    {
        shipico = new ImageIcon(filename);
        shipimg = shipico.getImage();
        this.setPreferredSize(new Dimension(shipico.getIconWidth(), shipico.getIconHeight()));
    }
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(shipimg, 0, 0, null);
    }
}