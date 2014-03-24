import javax.swing.*;
import java.awt.event.*;
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
        final ArrayList<ShipComponent> shipList = ships;
        final ArrayList<Integer> teamOneInd = new ArrayList<Integer>(); //tells which ships are on which teams
        final ArrayList<Integer> teamTwoInd = new ArrayList<Integer>();
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
        final ShipComponent[] currentlySelected = new ShipComponent[1];
        currentlySelected[0] = shipList.get(0);
        final PathComponent p = new PathComponent(400,400);
        final PathComponent pTarget = new PathComponent(400,400);
        final ArrayList<ProjectileComponent> allProjectiles = new ArrayList<ProjectileComponent>(); //this holds all the bullets
        final SkyboxComponent sky = new SkyboxComponent();
        int delay = 20; //delay in ms between frames. 

        ActionListener frameTimer = new ActionListener() {
                long lastFireProcessed = 0;
                public void actionPerformed (ActionEvent e) {
                    if(mousePolling) //scrolling. If you want other objects to scroll, you have to add them here and call 'setScroll' on them 
                    {
                        scrollX += MouseInfo.getPointerInfo().getLocation().getX()-scx;
                        scrollY += MouseInfo.getPointerInfo().getLocation().getY()-scy;
                        for(ShipComponent s: shipList)
                        {
                            s.setScroll((double) MouseInfo.getPointerInfo().getLocation().getX()-scx, (double) MouseInfo.getPointerInfo().getLocation().getY()-scy);
                        }
                        p.setScroll((double) MouseInfo.getPointerInfo().getLocation().getX()-scx, (double) MouseInfo.getPointerInfo().getLocation().getY()-scy);
                        pTarget.setScroll((double) MouseInfo.getPointerInfo().getLocation().getX()-scx, (double) MouseInfo.getPointerInfo().getLocation().getY()-scy);
                        sky.setScroll((double) MouseInfo.getPointerInfo().getLocation().getX()-scx, (double) MouseInfo.getPointerInfo().getLocation().getY()-scy);
                        for(ProjectileComponent pr : allProjectiles)
                        {
                            pr.setScroll((double) MouseInfo.getPointerInfo().getLocation().getX()-scx, (double) MouseInfo.getPointerInfo().getLocation().getY()-scy);
                        }

                        scx = MouseInfo.getPointerInfo().getLocation().getX(); 
                        scy = MouseInfo.getPointerInfo().getLocation().getY();

                    }
                    if(!pauseTurn)
                    {

                        //you can apply this state to any ship, so it's useful at the start/end of turns if you want to go back to how things were previously
                        //it's also probably necessary for collision detection later

                        //System.out.println("\f" + state.toString());
                        count++;
                        if(count > 250)
                        {
                            pauseTurn = true;
                            count = 0;
                        }
                        for(ShipComponent s: shipList)
                        {
                            ArrayList<Double> state = s.getState(); //just used for debug, you can get all the values

                            if(s.readyToFire())
                            {
                                allProjectiles.add(s.fire()); //add to array list
                                frame.add(allProjectiles.get(allProjectiles.size()-1), 0); //add to frame (on top of all objects)
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
                        ArrayList<Integer> toRemove = new ArrayList<Integer>();
                        for(ProjectileComponent pr : allProjectiles)
                        {
                            pr.doVel();
                            for(ShipComponent s: shipList)
                            {
                                if(s.getTeam() != pr.getTeam())
                                {
                                    if(s.getHitBox().contains(pr.getPosition()) && !pr.getDestroyed())
                                    {
                                        System.out.println("Ship " + shipList.indexOf(s) + " got hit!");
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
            }

            public void keyTyped(KeyEvent e){
                if(e.getKeyChar()=='b')
                {
                    
                    System.out.println(player);
                    if(player == 1)
                    {
                        playerSwitch();
                    }
                    else if (player==2)
                    {
                        playerSwitch();
                        pauseTurn = false;
                        count = 0;
                    }
                }
                else if(e.getKeyChar()=='1')
                {
                    currentlySelected[0] = shipList.get(currentSelectable.get(0));
                }
                else if(e.getKeyChar()=='2' && currentSelectable.size()>1)
                {
                    currentlySelected[0] = shipList.get(currentSelectable.get(1));
                }
                else if(e.getKeyChar()=='3' && currentSelectable.size()>2)
                {
                    currentlySelected[0] = shipList.get(currentSelectable.get(2));
                }
                else if(e.getKeyChar()=='4' && currentSelectable.size()>3)
                {
                    currentlySelected[0] = shipList.get(currentSelectable.get(3));
                }
                else if(e.getKeyChar()=='5' && currentSelectable.size()>4)
                {
                    currentlySelected[0] = shipList.get(currentSelectable.get(4));
                }
                else if(e.getKeyChar()=='6' && currentSelectable.size()>5)
                {
                    currentlySelected[0] = shipList.get(currentSelectable.get(5));
                }
                else if(e.getKeyChar()=='7' && currentSelectable.size()>6)
                {
                    currentlySelected[0] = shipList.get(currentSelectable.get(6));
                }
                else if(e.getKeyChar()=='8' && currentSelectable.size()>7)
                {
                    currentlySelected[0] = shipList.get(currentSelectable.get(7));
                }
            }

            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e){}

            public void playerSwitch()
            {
                if(player == 1) 
                {
                    player = 2;

                    currentSelectable = p2Selectable;
                }
                else if(player == 2)
                {
                    player = 1;
                    currentSelectable = p1Selectable;
                }

            }
        }
        t = new Timer(delay, frameTimer); 
        t.start();
        frame.addMouseListener(new MouseTest());
        frame.addKeyListener(new KeyTest(teamOneInd, teamTwoInd));
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