import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.util.ArrayList;

public class HudComponent extends JComponent
{
    Rectangle2D.Double rec;
    String player;
    int pnum;
    double currentPositionX;
    double currentPositionY;
    String currentPosition;
    int enemyShips;
    int turnNumber;
    double seconds;

    ArrayList<Integer> t1index;
    ArrayList<Integer> t2index;

    ArrayList<ShipComponent> allShips;
    ArrayList<Integer> currentDisplay;

    int selected;

    ImageIcon hudico;
    Image hudimg;

    public HudComponent()
    {
        pnum = 1;
        player = "One (Red)";
        rec = new Rectangle2D.Double(0,0,300,(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        allShips = new ArrayList<ShipComponent>();
        currentDisplay = new ArrayList<Integer>();

        selected = 0;

        hudico = new ImageIcon("failure.png");
        hudimg = hudico.getImage();
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

        currentDisplay = new ArrayList<Integer>();
        if(pnum == 1)
        {
            currentDisplay = t1index;
        }
        else if(pnum == 2)
        {
            currentDisplay = t2index;
        }
        else 
        {
            currentDisplay.clear();
            for(Integer e : t1index)
            {
                currentDisplay.add(e);
            }
            for(Integer e : t2index)
            {
                currentDisplay.add(e);
            }
        }
        int x = 0;
        if(allShips.size()>0)
        {
            for(Integer e : currentDisplay)
            {
                ShipComponent current = allShips.get(e);
                String teamColor = "";
                if(current.getTeam() == 1)
                {
                    teamColor = "Red";
                }
                else if(current.getTeam() == 2)
                {
                    teamColor = "Blue";
                }
                hudico = new ImageIcon("icons/" + current.getType() + teamColor + "Ico.png");
                hudimg = hudico.getImage();
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                Rectangle2D.Double shipBG = new Rectangle2D.Double(3, x+84, 300, 50);
                if(current.isDestroyed())
                {
                    g2.setPaint(new Color(100,50,50));
                }
                else if(pnum == 1 && t1index.size()>selected)
                {
                    if(t1index.get(selected) == e)
                    {
                        g2.setPaint(new Color(50, 100, 50));
                    } else g2.setPaint(new Color(20,20,20));
                }
                else if(pnum == 2 && t2index.size()>selected)
                {
                    if(t2index.get(selected) == e)
                    {
                        g2.setPaint(new Color(50, 100, 50));
                    } else g2.setPaint(new Color(20,20,20));
                }
                else g2.setPaint(new Color(20,20,20));
                g2.fill(shipBG);
                g2.setPaint(Color.WHITE);
                g2.draw(shipBG);
                g2.drawImage(hudimg, 3, x + 84, hudico.getIconWidth(), hudico.getIconHeight(),null,null);
                g2.drawString(current.getType(),100,x+104);
                g2.setPaint(new GradientPaint(100f, 0f, Color.RED, 250f, 0f, Color.GREEN));
                g2.fill(new Rectangle2D.Double(100,x+117,current.getHP()/current.getMaxHP() * 100.0,8));
                if(allShips.get(e).getTeam() == 1)
                {
                    g2.setPaint(Color.RED);
                }
                else g2.setPaint(new Color(50,50,255));
                g2.setFont(new Font("Arial Bold",Font.BOLD,24));
                g2.drawString(x/50 + 1 + "", 275, x+117);
                g2.setPaint(Color.WHITE);
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                x+=50;
            }
        }
        //g2.setPaint(Color.BLUE);
        //g2.fill(recb);

    }

    public void updatePlayer(String p)
    {

        player = p;
        if(player == "One (Red)")
        {
            pnum = 1;
        }
        else if (player == "Two (Blue)")
        {
            pnum = 2;
        }
        else pnum = 0;
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

    public void setSelected(int s)
    {
        selected = s;
    }

}