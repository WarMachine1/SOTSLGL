import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;

public class BackgroundComponent extends JComponent
{

    ImageIcon bgico;
    Image bgimg;
    public BackgroundComponent()
    {

        bgico = new ImageIcon("menuBG.gif");
        bgimg = bgico.getImage();
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(bgimg, 0, 0, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(), null, null);
    }
}
