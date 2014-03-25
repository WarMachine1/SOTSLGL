import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

public class ShipSelectMenu
{
    static ArrayList<ShipComponent> gameShips = new ArrayList<ShipComponent>();
    static double[][] gameStates = new double[16][10];
    static int teamOne = 0;
    static int teamTwo = 0;
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel = new JPanel(new GridLayout(1,2));
        JPanel p1ReadyPanel = new JPanel(new GridLayout(1,2));
        JPanel p2ReadyPanel = new JPanel(new GridLayout(1,2));
        final JPanel p1 = new JPanel(new BorderLayout());
        final JPanel p2 = new JPanel(new BorderLayout());
        //         frame.setSize(1000, 500);
        frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); //makes the frame sized to the screen resolution
        frame.setUndecorated(true);
        final JButton p1ReadyButton = new JButton("P1 Ready");
        final JButton p2ReadyButton = new JButton("P2 Ready");
        final JLabel p1ReadyText = new JLabel("P1 Ready?");
        final JLabel p2ReadyText = new JLabel("P2 Ready?");

        class ReadyListener implements ActionListener 
        {
            public void actionPerformed(ActionEvent e)
            {
                if(e.getSource() == p1ReadyButton)
                {
                    p1ReadyText.setText("READY");
                    p1.remove(p1ReadyButton);
                    p1.repaint();
                    p1.revalidate();
                }
                else if(e.getSource() == p2ReadyButton)
                {
                    p2ReadyText.setText("READY");
                    p2.remove(p2ReadyButton);
                    p2.repaint();
                    p2.revalidate();
                }
                
                panel.repaint();
                panel.revalidate();
            }
        }
        p1ReadyButton.addActionListener(new ReadyListener());
        p2ReadyButton.addActionListener(new ReadyListener());

        p1ReadyPanel.add(p1ReadyText);
        p1ReadyPanel.add(p1ReadyButton);
        p2ReadyPanel.add(p2ReadyText);
        p2ReadyPanel.add(p2ReadyButton);

        p1.add(getPanel(1), BorderLayout.CENTER);
        p1.add(p1ReadyPanel, BorderLayout.SOUTH);
        p2.add(getPanel(2), BorderLayout.CENTER);
        p2.add(p2ReadyPanel, BorderLayout.SOUTH);

        panel.add(p1);
        panel.add(p2);
        frame.add(panel);
        frame.setVisible(true);

        String stateFileName = "StartStates";
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(stateFileName + ".states"));
            String line = "";
            for(int i = 0; i < 16; i++)
            {
                line = inputStream.readLine();
                gameStates[i] = parseState(line);
            }
            inputStream.close();
        } catch(IOException i) {
            System.out.println("Error parsing state file " + stateFileName);
        }

        while(true)
        {
            if(p1ReadyText.getText().equalsIgnoreCase("ready") && p2ReadyText.getText().equalsIgnoreCase("ready"))
            {
                frame.setVisible(false);
                System.out.println("Both players ready, STARTING GAME...");
                GameEngine g = new GameEngine(gameShips);
                break;
            }
        }
    }

    public static JPanel getPanel(int player)
    {
        final int team = player;
        final JPanel frame = new JPanel();
        frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, 500);
        final String[] ships = new String[8];
        final ArrayList<LabelComponent> shipLabels = new ArrayList<LabelComponent>();
        final ArrayList<JPanel> shipLabelPanels = new ArrayList<JPanel>();
        ships[0] = "[Select Ship]                                     ";
        ships[1] = "Enterprise";
        ships[2] = "BirdofPrey";
        ships[3] = "Transporter";
        ships[4] = "Crusader";
        ships[5] = "Cylon";
        ships[6] = "Krynn";
        ships[7] = "Bomber";

        final JComboBox shipSelect = new JComboBox(ships);
        final JPanel totPanel = new JPanel(new BorderLayout());
        final JPanel labels = new JPanel(new GridLayout(9, 1));
        final JPanel shipPanel = new JPanel();
        final JLabel shipName = new JLabel();

        class LabelListener implements MouseListener
        {
            public void mouseClicked(MouseEvent e)
            {
                //LabelComponent l = (LabelComponent) e.getSource();
                //shipName.setText(l.getTitle());
                totPanel.remove(1);
                totPanel.add(new ShipDisplayComponent("testignore.png"));
                totPanel.revalidate();
                frame.revalidate();
            }

            public void mouseEntered(MouseEvent e) {}

            public void mouseExited(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {}

            public void mouseReleased(MouseEvent e) {}
        }

        class XListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                JButton j = (JButton) e.getSource();
                int num = 0;
                for(int a = 0; a<shipLabelPanels.size(); a++)
                {
                    if(shipLabelPanels.get(a).getComponent(1).equals(j))
                    {
                        num = a;
                    }
                }
                if(team == 1)
                {
                    gameShips.remove(num);
                    teamOne--;
                }
                else if(team == 2)
                {
                    gameShips.remove(num + gameShips.size() - shipLabelPanels.size());
                    teamTwo--;
                }
                shipLabelPanels.get(num).remove(0);
                shipLabelPanels.get(num).remove(0);
                shipLabelPanels.remove(num);
                labels.remove(num+1);
                labels.revalidate();
                labels.repaint();
            }
        }

        class ButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                if(e.getSource() == shipSelect)
                {
                    for(int i=1; i<ships.length; i++)
                    {
                        if(ships[i].equalsIgnoreCase((String) shipSelect.getSelectedItem()))
                        {
                            ArrayList<Double> startState = new ArrayList<Double>(); //starting state for one ship. Passed into contructor of ship class. 
                            //                             startState.add(0.0); //x position
                            //                             startState.add(0.0); //y position
                            //                             startState.add(0.0); //x velocity (should be 0)
                            //                             startState.add(0.0); //y velocity
                            //                             startState.add(0.0); //x acceleration
                            //                             startState.add(0.0); //y acceleration
                            //                             startState.add(0.0); //'speed', current velocity overall (x and y)
                            //                             startState.add(0.0); //current direction, in radians. Starts at positive x, clockwise
                            //                             startState.add(0.0); //current turn speed, in radians per frame
                            if(team == 1)
                            {
                                for(int x = 0; x < 9; x++)
                                {
                                    startState.add(gameStates[teamOne][x]);
                                }
                                teamOne++;
                            }
                            else{
                                for(int x = 0; x < 9; x++)
                                {
                                    startState.add(gameStates[teamTwo+8][x]);
                                }
                                teamTwo++;
                            }
                            gameShips.add(new ShipComponent(startState, ships[i], team));
                            shipLabels.add(new LabelComponent(ships[i], ships[i] + " " + shipLabels.size()));
                            shipLabels.get((shipLabels.size()-1)).addMouseListener(new LabelListener());
                            shipLabelPanels.add(new JPanel(new BorderLayout()));
                            shipLabelPanels.get((shipLabelPanels.size()-1)).add(shipLabels.get(shipLabels.size()-1), BorderLayout.CENTER);
                            shipLabelPanels.get((shipLabelPanels.size()-1)).add(new JButton("X"), BorderLayout.EAST);
                            JButton jb = (JButton) shipLabelPanels.get((shipLabelPanels.size()-1)).getComponent(1);
                            jb.addActionListener(new XListener());
                            labels.add(shipLabelPanels.get(shipLabelPanels.size()-1));
                        }
                    }
                }

                frame.revalidate();
            }
        }

        shipSelect.addActionListener(new ButtonListener());

        for(int i = 0; i<shipLabels.size(); i++)
        {
            shipLabels.get(i).addMouseListener(new LabelListener());
        }

        shipPanel.add(shipName);
        labels.add(shipSelect);
        totPanel.add(labels, BorderLayout.WEST);
        totPanel.add(new ShipDisplayComponent("testignore.png"), BorderLayout.CENTER);
        frame.add(totPanel);
        return frame;
    }

    public static double[] parseState(String s) {

        double[] output = new double[9];
        String listString = s.substring(1, s.length() - 1); // chop off brackets
        StringTokenizer st = new StringTokenizer(listString, ",");
        for (int i = 0; i<9; i++) {
            output[i] = Double.parseDouble(st.nextToken());
        }
        return output;
    }

}
