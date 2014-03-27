import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.util.ArrayList;

public class HudComponent extends JComponent
{
    Rectangle2D.Double rec;
    String player;
    double currentPositionX;
    double currentPositionY;
    String currentPosition;
    int enemyShips;
    int turnNumber;
    double seconds;

    ArrayList<Integer> t1index;
    ArrayList<Integer> t2index;

    ArrayList<ShipComponent> allShips;

    public HudComponent()
    {
        player = "One (Red)";
        rec = new Rectangle2D.Double(0,0,300,(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());

    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(Color.BLACK);
        g2.fill(rec);
        g2.setPaint(Color.WHITE);
        g2.drawString("Game Info", 0, 12);
        if(player == "One (Red)")
        {
            g2.setPaint(new Color(255,100,100));
        }
        else if(player == "Two (Blue)")
        {
            g2.setPaint(new Color(100,100,255));
        }
        else g2.setPaint(new Color(0,0,0));
        g2.drawString("It is currently player " + player + "'s turn.", 0, 24);
        g2.setPaint(Color.WHITE);
        g2.drawString("Current Position: " + currentPositionX + "," + currentPositionY, 0, 36);
        g2.drawString("Enemy Ships Remaining: " + enemyShips, 0, 48);
        g2.drawString("Turn Number: " + turnNumber, 0 ,60);
        g2.drawString("Time: " + seconds, 0, 72);

        ArrayList<Integer> currentDisplay;
        if(player == "One (Red)")
        {
            currentDisplay = t1index;
        }
        else 
        {
            currentDisplay = t2index;
        }
        
        int x = 0;
        for(Integer e : currentDisplay)
        {
            g2.drawString(allShips.get(e).getType(),0,x+84);
            g2.fill(new Rectangle2D.Double(0,x+85,allShips.get(e).getHP()/allShips.get(e).getMaxHP() * 250.0,8));
            g2.setFont(new Font("Arial Bold",Font.BOLD,24));
            g2.drawString(x/24 + 1 + "", 275, x+90);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            x+=24;
        }
        //g2.setPaint(Color.BLUE);
        //g2.fill(recb);

    }

    public void updatePlayer(String p)
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

    public void updateTurn()
    {
        turnNumber++;
    }

    public void updateTime(double t)
    {
        seconds+= Math.floor(t * 100) / 100;

        seconds = Math.ceil(seconds * 100) / 100;
    }

    public void updateIndex(ArrayList<Integer> t1ind, ArrayList<Integer> t2ind)
    {
        t1index = t1ind;
        t2index = t2ind;
    }

    public void updateShips(ArrayList<ShipComponent> currentShips)
    {
        allShips = currentShips;
    }

    
}