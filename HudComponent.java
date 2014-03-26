import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;

public class HudComponent extends JComponent
{
    Rectangle2D.Double rec;
    int player;
    double currentPositionX;
    double currentPositionY;
    String currentPosition;
    int enemyShips;
    int turnNumber;
    double seconds;
    String selected;
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
        g2.drawString("SIMHUD MK VII", 0, 12);
        g2.drawString("It is currently player " + player + "'s turn.", 0, 24);
        g2.drawString("Current Position: " + currentPositionX + "," + currentPositionY, 0, 36);
        g2.drawString("Enemy Ships Remaining: " + enemyShips, 0, 48);
        g2.drawString("Turn Number: " + turnNumber, 0 ,60);
        g2.drawString("Time: " + seconds, 0, 72);
        g2.drawString("Currently Selected Ship: " + selected, 0, 84);
        //g2.setPaint(Color.BLUE);
        //g2.fill(recb);
        
    }
    
    public void updatePlayer(int p)
    {
        player = p;
    }
    
    public void updateX(int x)
    {
        currentPositionX = x;
    }
    
    public void updateY(int y)
    {
        currentPositionY = y;
    }

    public void updateEnemyShips(int n)
    {
        enemyShips = n;
    }
    
    public void updateTurn(int t)
    {
        turnNumber = t;
    }
    
    public void updateTime(double t)
    {
        seconds = t;
    }
    
    public void updateSelected(String s)
    {
        selected = s;
    }
}
