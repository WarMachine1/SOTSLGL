import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Engine
{
    static int itr = 0; //tutorial image iterator
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
        ImageIcon credImg = new ImageIcon("credits/credits.png");
        JLabel credLabel = new JLabel();
        credLabel.setIcon(credImg);
        credPanel.add(credLabel);
        
        JPanel mPanel = new JPanel(new GridLayout(2,1));
        JPanel tPanel = new JPanel();
        JLabel titleImage = new JLabel();
        titleImage.setIcon(new ImageIcon("title.gif"));
        tPanel.add(titleImage);
        JPanel tBPanel = new JPanel(new BorderLayout());
        JPanel bPanel = new JPanel(new GridLayout(2,2));

        JPanel sPanel1 = new JPanel();
        sPanel1.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight())/16));
        sPanel1.setOpaque(false);

        JPanel sPanel2 = new JPanel();
        sPanel2.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight())/16));
        sPanel2.setOpaque(false);

        JPanel sPanel3 = new JPanel();
        sPanel3.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4, ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        sPanel3.setOpaque(false);

        JPanel sPanel4 = new JPanel();
        sPanel4.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4, ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        sPanel4.setOpaque(false);

        //         JPanel lPanel = new JPanel(new GridLayout(2,1));
        //         JPanel rPanel = new JPanel(new GridLayout(2,1));

        final JButton play = new JButton();
        play.setBorderPainted(false);
        play.setIcon(new ImageIcon("buttons/play.png"));
        play.setRolloverIcon(new ImageIcon("buttons/playb.png"));
        play.setContentAreaFilled(false);
        final JButton cred = new JButton();
        cred.setIcon(new ImageIcon("buttons/credits.png"));
        cred.setRolloverIcon(new ImageIcon("buttons/creditsb.png"));
        cred.setBorderPainted(false);
        cred.setContentAreaFilled(false);
        final JButton tut = new JButton();
        tut.setIcon(new ImageIcon("buttons/tutorial.png"));
        tut.setRolloverIcon(new ImageIcon("buttons/tutorialb.png"));
        tut.setBorderPainted(false);
        tut.setContentAreaFilled(false);
        final JButton quit = new JButton();
        quit.setIcon(new ImageIcon("buttons/quit.png"));
        quit.setRolloverIcon(new ImageIcon("buttons/quitb.png"));
        quit.setBorderPainted(false);
        quit.setContentAreaFilled(false);
        final JButton tutQuit = new JButton("CLOSE");
        tutQuit.setPreferredSize(new Dimension(((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())/10, ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight())/10));
        final JButton credQuit = new JButton("CLOSE");
        credQuit.setPreferredSize(new Dimension(((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())/10, ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight())/10));

        //tutorial "powerpoint" buttons
        //im not adding image icons to the buttons cause i dont have them
        final JLabel tutImgLbl = new JLabel();
        final ArrayList<ImageIcon> tutImgs = new ArrayList<ImageIcon>();
        final JButton tutNext = new JButton("NEXT");
        tutNext.setPreferredSize(new Dimension(((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())/10, ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight())/10));
        final JButton tutBack = new JButton("BACK");
        tutBack.setPreferredSize(new Dimension(((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())/10, ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight())/10));
        ImageIcon tut1 = new ImageIcon("credits/tut1.png");
        ImageIcon tut2 = new ImageIcon("credits/tut2.png");
        ImageIcon tut3 = new ImageIcon("credits/tut3.png");
        ImageIcon tut4 = new ImageIcon("credits/tut4.png");
        tutImgs.add(tut1);
        tutImgs.add(tut2);
        tutImgs.add(tut3);
        tutImgs.add(tut4);

        class TitleTimer implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                menuFrame.repaint();
            }
        }
        final Timer t = new Timer(10,new TitleTimer());
        t.start();

        class BttnListener implements MouseListener
        {
            public void mouseClicked(MouseEvent e)
            {
                if(e.getSource() == play)
                {
                    boolean done = false;
                    t.stop();
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
                    itr = 0;
                }
                else if(e.getSource() == credQuit)
                {
                    credFrame.setVisible(false);
                }
                else if(e.getSource() == tutNext && itr<tutImgs.size()-1) //tutorial change
                {
                    itr++;
                    tutImgLbl.setIcon(tutImgs.get(itr));
                    tutFrame.repaint();
                }
                else if(e.getSource() == tutBack && itr>0) //tutorial change
                {
                    itr--;
                    tutImgLbl.setIcon(tutImgs.get(itr));
                    tutFrame.repaint();
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
        tutNext.addMouseListener(new BttnListener()); //tutorial change
        tutBack.addMouseListener(new BttnListener()); //tutorial change

        bPanel.add(play);
        bPanel.add(cred);
        bPanel.add(tut);
        bPanel.add(quit);

        //bPanel.add(lPanel);
        //bPanel.add(rPanel);

        tBPanel.add(bPanel, BorderLayout.CENTER);
        tBPanel.add(sPanel1, BorderLayout.NORTH);
        tBPanel.add(sPanel2, BorderLayout.SOUTH);
        tBPanel.add(sPanel3, BorderLayout.EAST);
        tBPanel.add(sPanel4, BorderLayout.WEST);

        tutImgLbl.setIcon(tutImgs.get(0)); //tutorial change
        tutPanel.add(tutImgLbl); //tutorial change
        tutFrame.repaint(); //tutorial change
        tutPanel.add(tutNext);
        tutPanel.add(tutBack);
        tutPanel.add(tutQuit);
        tutFrame.add(tutPanel);
        //tutFrame.add(tutImg);

        credPanel.add(credQuit);
        credFrame.add(credPanel);
//         credFrame.repaint();
//         final JLabel credLabel = new JLabel();
//         credLabel.setIcon(credImg);
//         credFrame.add(credLabel);
//         credFrame.repaint();
        mPanel.add(tPanel);
        mPanel.add(tBPanel);
        menuFrame.add(mPanel);
        //frame.add(backImg);
        bPanel.setBackground(new Color(0,0,0));
        tPanel.setBackground(new Color(0,0,0));
        tBPanel.setBackground(new Color(0,0,0));
        menuFrame.setBackground(new Color(0,0,0));

        menuFrame.setVisible(true);
    }

}
