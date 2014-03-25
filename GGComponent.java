import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class GGComponent extends JComponent
{
    int outcome = 0;
    public GGComponent(int i)
    {
        outcome = i;
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        String result = "";
        if(outcome == 1)
        {
            result = "Player 1 Wins!";
            g2.setColor(Color.RED);
        }
        else if(outcome == 2)
        {
            result = "Player 2 Wins!";
            g2.setColor(Color.BLUE);
        }
        else if(outcome == 3)
        {
            result = "Mutually assured destruction!  No lifeforms remain.  Galaxy is now cleansed!";
            g2.setColor(Color.GREEN);
        }
        
        g2.setFont(new Font("Arial", Font.BOLD, 100));
        FontMetrics fm = g2.getFontMetrics();//used for centering the text
        int xf = (getWidth()/2) - (fm.stringWidth(result)/2);
        int yf = (getHeight()/2);
        g2.drawString(result, xf, yf);
    }
}