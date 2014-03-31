import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Engine
{
    public static void main(String[] args)
    {
        final JFrame menuFrame = new JFrame();
        menuFrame.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        menuFrame.setTitle("Main Menu");
        menuFrame.setUndecorated(true);

        final JFrame tutFrame = new JFrame();
        tutFrame.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        tutFrame.setTitle("Tutorial");
        tutFrame.setUndecorated(true);
        JPanel tutPanel = new JPanel();

        final JFrame credFrame = new JFrame();
        credFrame.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        credFrame.setTitle("Credits");
        credFrame.setUndecorated(true);
        JPanel credPanel = new JPanel();

        //ImageIcon backImg = new ImageIcon("background.png");
        //ImageIcon tutImg = new ImageIcon("tutorial.png");
        //ImageIcon credImg = new ImageIcon("credits.png");

        JPanel mPanel = new JPanel(new GridLayout(2,1));
        JPanel tPanel = new JPanel();
        JPanel tBPanel = new JPanel(new BorderLayout());
        JPanel bPanel = new JPanel(new GridLayout(1,2));

        JPanel sPanel1 = new JPanel();
        sPanel1.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight())/8));
        sPanel1.setOpaque(false);

        JPanel sPanel2 = new JPanel();
        sPanel2.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight())/8));
        sPanel2.setOpaque(false);

        JPanel sPanel3 = new JPanel();
        sPanel3.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4, ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        sPanel3.setOpaque(false);

        JPanel sPanel4 = new JPanel();
        sPanel4.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4, ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        sPanel4.setOpaque(false);

        JPanel lPanel = new JPanel(new GridLayout(2,1));
        JPanel rPanel = new JPanel(new GridLayout(2,1));

        final JButton play = new JButton();
        play.setIcon(new ImageIcon("buttons/play.png"));
        play.setRolloverIcon(new ImageIcon("buttons/playb.png"));
        final JButton cred = new JButton();
                cred.setIcon(new ImageIcon("buttons/credits.png"));
        cred.setRolloverIcon(new ImageIcon("buttons/creditsb.png"));
        final JButton tut = new JButton();
                tut.setIcon(new ImageIcon("buttons/tutorial.png"));
        tut.setRolloverIcon(new ImageIcon("buttons/tutorialb.png"));
        final JButton quit = new JButton();
                quit.setIcon(new ImageIcon("buttons/quit.png"));
        quit.setRolloverIcon(new ImageIcon("buttons/quitb.png"));
        final JButton tutQuit = new JButton("CLOSE");
        tutQuit.setPreferredSize(new Dimension(((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())/10, ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight())/10));
        final JButton credQuit = new JButton("CLOSE");
        credQuit.setPreferredSize(new Dimension(((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())/10, ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight())/10));

        class BttnListener implements MouseListener
        {
            public void mouseClicked(MouseEvent e)
            {
                if(e.getSource() == play)
                {
                    boolean done = false;
                    menuFrame.setVisible(false);
                    ShipSelectMenu.start();


                }
                else if(e.getSource() == cred)
                {
                    credFrame.setVisible(true);
                }
                else if(e.getSource() == tut)
                {
                    tutFrame.setVisible(true);
                }
                else if(e.getSource() == quit)
                {
                    menuFrame.setVisible(false);
                    System.exit(0);
                }
                else if(e.getSource() == tutQuit)
                {
                    tutFrame.setVisible(false);
                }
                else if(e.getSource() == credQuit)
                {
                    credFrame.setVisible(false);
                }
            }

            public void mouseEntered(MouseEvent e){}

            public void mouseExited(MouseEvent e){}

            public void mousePressed(MouseEvent e){}

            public void mouseReleased(MouseEvent e){}
        }

        play.addMouseListener(new BttnListener());
        cred.addMouseListener(new BttnListener());
        tut.addMouseListener(new BttnListener());
        quit.addMouseListener(new BttnListener());
        tutQuit.addMouseListener(new BttnListener());
        credQuit.addMouseListener(new BttnListener());

        lPanel.add(play);
        lPanel.add(cred);
        rPanel.add(tut);
        rPanel.add(quit);

        bPanel.add(lPanel);
        bPanel.add(rPanel);

        tBPanel.add(bPanel, BorderLayout.CENTER);
        tBPanel.add(sPanel1, BorderLayout.NORTH);
        tBPanel.add(sPanel2, BorderLayout.SOUTH);
        tBPanel.add(sPanel3, BorderLayout.EAST);
        tBPanel.add(sPanel4, BorderLayout.WEST);

        tutPanel.add(tutQuit);
        tutFrame.add(tutPanel);
        //tutFrame.add(tutImg);

        credPanel.add(credQuit);
        credFrame.add(credPanel);
        //credFrame.add(credImg);

        mPanel.add(tPanel);
        mPanel.add(tBPanel);
        menuFrame.add(mPanel);
        //frame.add(backImg);

        menuFrame.setVisible(true);
    }

}
