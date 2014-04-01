import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.PointerInfo; //gets mouse position
import java.awt.MouseInfo; //gets mouse position
import java.awt.Toolkit; //gets screen resolution
import javax.sound.sampled.*;
import java.io.File;

public class GameEngine
{
    static boolean mousePolling = false; //whether or not you're polling the mouse (used for scrolling)
    static double scx = 0; //last time you scrolled, in x and y
    static double scy = 0;
    static double scrollX = 0; //current amount you're scrolling by
    static double scrollY = 0;
    static boolean pauseTurn = false;
    static Timer t;
    static int count = 0;
    static ArrayList<ShipComponent> shipList;
    static ArrayList<DebrisComponent> debris;
    final ArrayList<Integer> teamOneInd = new ArrayList<Integer>(); //tells which ships are on which teams
    final ArrayList<Integer> teamTwoInd = new ArrayList<Integer>();
    GameEngine(ArrayList<ShipComponent> ships)
    {
        final JFrame frame = new JFrame();
        //frame.setSize(600,600);
        frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); //makes the frame sized to the screen resolution
        frame.setUndecorated(true); //removes window border (like title, close button)
        frame.setLocation(0,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //         ArrayList<Double> startState = new ArrayList<Double>(); //starting state for one ship. Passed into contructor of ship class. 
        //         startState.add(0.0); //x position
        //         startState.add(0.0); //y position
        //         startState.add(0.0); //x velocity (should be 0)
        //         startState.add(0.0); //y velocity
        //         startState.add(0.0); //x acceleration
        //         startState.add(0.0); //y acceleration
        //         startState.add(0.0); //'speed', current velocity overall (x and y)
        //         startState.add(0.0); //current direction, in radians. Starts at positive x, clockwise
        //         startState.add(0.0); //current turn speed, in radians per frame
        //         ArrayList<Double> startState2 = new ArrayList<Double>(); //starting state for one ship. Passed into contructor of ship class. 
        //         startState2.add(200.0); //x position
        //         startState2.add(600.0); //y position
        //         startState2.add(0.0); //x velocity (should be 0)
        //         startState2.add(0.0); //y velocity
        //         startState2.add(0.0); //x acceleration
        //         startState2.add(0.0); //y acceleration
        //         startState2.add(0.0); //'speed', current velocity overall (x and y)
        //         startState2.add(3.0); //current direction, in radians. Starts at positive x, clockwise
        //         startState2.add(0.0); //current turn speed, in radians per frame
        shipList = ships;
        debris = new ArrayList<DebrisComponent>();
        for(int i = 0; i < shipList.size(); i++)
        {
            if(shipList.get(i).getTeam()==1)
            {
                teamOneInd.add(i);
            }
            else 
            {
                teamTwoInd.add(i);
            }
        }

        //final ShipComponent a = new ShipComponent(startState, "Transporter", 1);
        //final ShipComponent b = new ShipComponent(startState2,"Cylon", 2);
        //shipList.add(a);
        //shipList.add(b);
        final HudComponent h = new HudComponent();
        int esl = 0;
        for(ShipComponent q : shipList)
        {
            if(q.getTeam()==2) //at the start, team 2 is the 'enemy'
            {
                esl++;
            }
        }
        h.updateEnemyShips(esl);
        final ShipComponent[] currentlySelected = new ShipComponent[1];
        currentlySelected[0] = shipList.get(0);
        final PathComponent p = new PathComponent(400,400, false);
        final PathComponent pTarget = new PathComponent(400,400, true);
        final ArrayList<ProjectileComponent> allProjectiles = new ArrayList<ProjectileComponent>(); //this holds all the bullets
        final SkyboxComponent sky = new SkyboxComponent();
        final int delay = 20; //delay in ms between frames. 

        ActionListener frameTimer = new ActionListener() {
                long lastFireProcessed = 0;
                double[] newHP = new double[shipList.size()];
                public void actionPerformed (ActionEvent e) {
                    if(checkGG() != 0)
                    {
                        GGComponent gg = new GGComponent(checkGG());
                        frame.add(gg,0);
                        frame.revalidate();
                        //                         System.out.println("G to the G!");
                    }
                    if(mousePolling) //scrolling. If you want other objects to scroll, you have to add them here and call 'setScroll' on them 
                    {
                        scrollX += MouseInfo.getPointerInfo().getLocation().getX()-scx;
                        scrollY += MouseInfo.getPointerInfo().getLocation().getY()-scy;
                        h.updateX((int) scrollX);
                        h.updateY((int) scrollY);
                        int thisX = (int) MouseInfo.getPointerInfo().getLocation().getX()-(int)scx;
                        int thisY = (int) MouseInfo.getPointerInfo().getLocation().getY()-(int)scy;
                        for(ShipComponent s: shipList)
                        {
                            s.setScroll((double) thisX, (double) thisY);
                        }
                        p.setScroll((double) thisX, (double) thisY);
                        pTarget.setScroll((double) thisX, (double) thisY);
                        sky.setScroll((double) thisX, (double) thisY);
                        for(ProjectileComponent pr : allProjectiles)
                        {
                            pr.setScroll((double) thisX, (double) thisY);
                        }

                        for(DebrisComponent de : debris)
                        {
                            de.setScroll((double) thisX, (double) thisY);
                        }

                        scx = MouseInfo.getPointerInfo().getLocation().getX(); 
                        scy = MouseInfo.getPointerInfo().getLocation().getY();

                    }

                    if(allProjectiles.size()>150) //garbage collection - nobody needs that many bullets
                    {
                        while(allProjectiles.size()>150)
                        {
                            allProjectiles.get(0).destroy();
                            allProjectiles.remove(0);
                        }
                    }

                    if(!pauseTurn)
                    {
                        for(DebrisComponent de : debris)
                        {
                            de.doVel();
                        }
                        h.updateTime(delay/1000.);
                        if(count%10==0)
                        {
                            h.updateShips((ArrayList<ShipComponent>)shipList.clone());
                        }
                        h.updatePlayer("None, running simulation");
                        //you can apply this state to any ship, so it's useful at the start/end of turns if you want to go back to how things were previously
                        //it's also probably necessary for collision detection later

                        //System.out.println("\f" + state.toString());
                        count++;
                        if(count > 100)
                        {

                            h.updatePlayer("One (Red)");
                            int enemyShipsLeft = 0;
                            for(ShipComponent q : shipList)
                            {
                                if(!q.isDestroyed() && q.getTeam() != 1)
                                {
                                    enemyShipsLeft++;
                                }
                            }
                            h.updateEnemyShips(enemyShipsLeft);
                            pauseTurn = true;
                            count = 0;
                        }
                        for(ShipComponent s: shipList)
                        {
                            if(!s.isDestroyed())
                            {
                                ArrayList<Double> state = s.getState(); //KEEP THIS IN

                                ArrayList<ProjectileComponent> newbullets = s.fire();
                                allProjectiles.addAll(newbullets); //add to array list
                                for(int i = newbullets.size(); i > 0; i--)
                                {
                                    frame.add(allProjectiles.get(allProjectiles.size()-i), 0); //add to frame (on top of all objects)
                                    frame.revalidate(); //better than repaint
                                }

                                if(p.isActive()) //if you have an active pathComponent, the ship will follow it
                                {
                                    s.followWaypoint();
                                }
                                //s.applyState(state);
                                s.doRotation(); //movement methods. Call on each ship
                                s.coast();
                                s.doVel();
                                s.doAccel();
                                s.doInertiaSlow();
                                //s.doFriction();
                            }
                        }
                        ArrayList<Integer> toRemove = new ArrayList<Integer>();
                        for(ProjectileComponent pr : allProjectiles)
                        {
                            pr.doVel();
                            for(ShipComponent s: shipList)
                            {
                                if(s.getTeam() != pr.getTeam() && !pr.getDestroyed())
                                {
                                    //                                     if(s.getHitBox().contains(pr.getPosition()))

                                    Area shiphit = s.getHitBox();
                                    Area proj = pr.getHitBox();
                                    Area combined = new Area(shiphit);
                                    combined.intersect(proj);
                                    if(!combined.isEmpty())
                                    {
                                        System.out.println("Ship " + shipList.indexOf(s) + " was hit!");
                                        if(s.hit(pr.getDamage()))
                                        {
                                            Point2D.Double sPos = s.getPosition();
                                            ArrayList<Double> debrisState = new ArrayList<Double>();
                                            debrisState.add(sPos.getX()+scrollX);
                                            debrisState.add(sPos.getY()+scrollY);
                                            debrisState.add(s.getTheta());
                                            debrisState.add(s.getXVel());
                                            debrisState.add(s.getYVel());
                                            debrisState.add(.005);
                                            for(int i = 1; i < 4; i++)
                                            {
                                                debris.add(new DebrisComponent(debrisState, System.currentTimeMillis(), s.getType(), s.getTeam(), i, i < 2));

                                                frame.add(debris.get(debris.size()-1),0);
                                                frame.revalidate();
                                                frame.repaint();
                                            }
                                            //System.out.println("Ship " + shipList.indexOf(s) + " was destroyed!");
                                        }
                                        pr.destroy();
                                        toRemove.add(allProjectiles.indexOf(pr));
                                    }
                                }
                            }
                        }
                        for(Integer i: toRemove)
                        {
                            allProjectiles.remove(i);
                        }

                        if(debris.size()>0)
                        {
                            while(debris.get(0).getTime()+5000<System.currentTimeMillis())
                            {
                                debris.get(0).destroy();
                                frame.remove(debris.get(0));
                                debris.remove(0);
                                if(debris.size()==0) break;
                            }
                        }
                    }

                    frame.repaint();
                }

            };

        class MouseTest implements MouseListener {

            public void mouseClicked(MouseEvent e)
            {
                if(pauseTurn)
                {
                    if(e.getButton()==1) //left click?
                    {
                        p.setPosition(e.getX(),e.getY()); //set the position of the pathComponent, activate it
                        p.setActive(true);

                        currentlySelected[0].setWaypoint(e.getX(), e.getY()); //set the waypoint for the ship (change this to currently selected ship)

                    }
                }
            }

            public void mouseEntered(MouseEvent e)
            {

            }

            public void mouseExited(MouseEvent e)
            {

            }

            public void mousePressed(MouseEvent e)
            {
                if(e.getButton()==3) //right click
                {
                    scx = MouseInfo.getPointerInfo().getLocation().getX();
                    scy = MouseInfo.getPointerInfo().getLocation().getY();

                    mousePolling = true;

                }
                if(e.getButton()==2) //middle click
                {
                    if(pauseTurn)
                    {

                        currentlySelected[0].setFiringTarget((int)(e.getX()-scrollX),(int)(e.getY()-scrollY));
                        currentlySelected[0].setFiring(true);
                        pTarget.setPosition(e.getX(), e.getY());

                    }

                }
            }

            public void mouseReleased(MouseEvent e)
            {

                //s.setFiring(false);

                mousePolling = false; //stops scrolling when mouse released
            }
        }

        class KeyTest implements KeyListener {
            private long lastPressProcessed = 0;
            private long lastSpaceProcessed = 0;
            private int player;
            private ArrayList<Integer> currentSelectable;
            private ArrayList<Integer> p1Selectable;
            private ArrayList<Integer> p2Selectable;
            public KeyTest(ArrayList<Integer> p1i, ArrayList<Integer> p2i)
            {
                player = 1;
                p1Selectable = p1i;
                p2Selectable = p2i;
                currentSelectable = p1i;
                lastSpaceProcessed = System.currentTimeMillis();
            }

            public void keyTyped(KeyEvent e){
                //                 if(e.getKeyChar()=='b')
                if(e.getKeyChar()==KeyEvent.VK_ESCAPE)
                {
                    System.exit(0);

                }
                //                 System.out.println(currentSelectable.toString());
                for(ShipComponent sh : shipList)
                {
                    sh.setSelected(false);
                }
                if(e.getKeyChar()==KeyEvent.VK_SPACE && lastSpaceProcessed < System.currentTimeMillis() - 2000)
                {
                    lastSpaceProcessed = System.currentTimeMillis();
                    //System.out.println(player);
                    if(player == 1)
                    {
                        playerSwitch();
                    }
                    else if (player==2)
                    {

                        //                         shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                        playerSwitch();
                        h.updateTurn();
                        pauseTurn = false;
                        count = 0;
                    }
                }
                else if(e.getKeyChar()=='1')
                {

                    //                     shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                    currentlySelected[0] = shipList.get(currentSelectable.get(0));
                    h.setSelected(0);
                    shipList.get(currentSelectable.get(0)).setSelected(true);

                }
                else if(e.getKeyChar()=='2' && currentSelectable.size()>1)
                {

                    //                     shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                    currentlySelected[0] = shipList.get(currentSelectable.get(1));
                    h.setSelected(1);
                    shipList.get(currentSelectable.get(1)).setSelected(true);

                }
                else if(e.getKeyChar()=='3' && currentSelectable.size()>2)
                {

                    //                     shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                    currentlySelected[0] = shipList.get(currentSelectable.get(2));
                    h.setSelected(2);
                    shipList.get(currentSelectable.get(2)).setSelected(true);

                }
                else if(e.getKeyChar()=='4' && currentSelectable.size()>3)
                {

                    //                     shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                    currentlySelected[0] = shipList.get(currentSelectable.get(3));
                    h.setSelected(3);
                    shipList.get(currentSelectable.get(3)).setSelected(true);

                }
                else if(e.getKeyChar()=='5' && currentSelectable.size()>4)
                {

                    //                     shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                    currentlySelected[0] = shipList.get(currentSelectable.get(4));
                    h.setSelected(4);
                    shipList.get(currentSelectable.get(4)).setSelected(true);

                }
                else if(e.getKeyChar()=='6' && currentSelectable.size()>5)
                {

                    //                     shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                    currentlySelected[0] = shipList.get(currentSelectable.get(5));
                    h.setSelected(5);
                    shipList.get(currentSelectable.get(5)).setSelected(true);

                }

                else if(e.getKeyChar()=='7' && currentSelectable.size()>6)
                {

                    //                     shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                    currentlySelected[0] = shipList.get(currentSelectable.get(6));
                    h.setSelected(6);
                    shipList.get(currentSelectable.get(6)).setSelected(true);

                }
                else if(e.getKeyChar()=='8' && currentSelectable.size()>7)
                {

                    //                     shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                    currentlySelected[0] = shipList.get(currentSelectable.get(7));
                    h.setSelected(7);
                    shipList.get(currentSelectable.get(7)).setSelected(true);

                }
            }

            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e){}

            public void playerSwitch()
            {
                //shipList.get(currentSelectable.get(h.getSelected())).setSelected(false);
                for(ShipComponent sh : shipList)
                {
                    sh.setSelected(false);
                }
                if(player == 1) 
                {
                    player = 2;
                    h.updatePlayer("Two (Blue)");
                    currentSelectable = p2Selectable;
                }
                else if(player == 2)
                {
                    player = 1;
                    h.updatePlayer("One (Red)");
                    currentSelectable = p1Selectable;
                }
                int enemyShipsLeft = 0;
                for(ShipComponent q : shipList)
                {
                    if(!q.isDestroyed() && q.getTeam() != player)
                    {
                        enemyShipsLeft++;
                    }
                }
                h.updateEnemyShips(enemyShipsLeft);
            }
        }
        t = new Timer(delay, frameTimer); 
        t.start();
        frame.addMouseListener(new MouseTest());
        frame.addKeyListener(new 
            KeyTest(teamOneInd, teamTwoInd));
        h.updateIndex(teamOneInd, teamTwoInd);
        frame.add(h);
        frame.setVisible(true);
        for(ShipComponent s: shipList)
        {
            frame.add(s);
            frame.setVisible(true);
        }
        frame.add(p);
        frame.setVisible(true);
        frame.add(pTarget);
        frame.setVisible(true);
        frame.add(sky);
        frame.setVisible(true);
    }

    public int checkGG()
    {
        boolean teamOneDefeat = true;
        for(Integer i: teamOneInd)
        {
            if(!shipList.get(i).isDestroyed())
            {
                teamOneDefeat = false;
            }
        }
        boolean teamTwoDefeat = true;
        for(Integer i: teamTwoInd)
        {
            if(!shipList.get(i).isDestroyed())
            {
                teamTwoDefeat = false;
            }
        }
        if(teamOneDefeat && teamTwoDefeat)//tie
        {
            return 3;
        }
        else if(teamOneDefeat)//team two wins
        {
            return 2;
        }
        else if(teamTwoDefeat)//team one wins
        {
            return 1;
        }
        else//no winner yet
        {
            return 0;
        }

    }

    public void playSound()
    {
        AudioInputStream music;
        AudioInputStream music2;
        try
        {
            music = AudioSystem.getAudioInputStream(new File("CrashSound.wav"));
            music2 = AudioSystem.getAudioInputStream(new File("CrashSound2.wav"));
            Clip clip2 = AudioSystem.getClip();
            Clip clip = AudioSystem.getClip();
            clip2.open(music2);
            clip.open(music);
            clip2.start();
            clip.start();
            //clip.loop(clip.LOOP_CONTINUOUSLY);
        }
        catch(Exception error)
        {
            // do nothing
        }
    }
}