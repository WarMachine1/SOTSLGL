import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import javax.sound.sampled.*;
import java.io.File;

public class ShipComponent extends JComponent
{
    Rectangle2D.Double rec;
    ArrayList<Double> state;
    double xPos;
    double yPos;
    double theta;

    double xVel;
    double yVel;
    double xAcc;
    double yAcc;
    double inSlow;
    double speed;
    double friction;
    double turnSpeed;
    double toTurn;

    double scrollX;
    double scrollY;

    boolean close;

    double targetX;
    double targetY;

    long lastPressProcessed;
    long lastAccelSlow;
    long lastFire;

    double maxSpeed;
    double maxTheta;

    double firingTargetX;
    double firingTargetY;

    int numWeapons;
    int[] weaponOffsetX;
    int[] weaponOffsetY;
    int[] weaponDirectionality;
    double[] weaponDamage;
    int[] weaponFireRate;

    long[] lastFired;
    //int fireRate;

    boolean isFiring;

    ImageIcon shippic;
    Image shipimg;

    String polyFileName;
    int numPoints;
    int[] xPa;
    int[] yPa;    

    int hp;
    int maxHP;
    boolean destroyed;
    int teamNumber;

    public ShipComponent(ArrayList<Double> stat, String shipType, int team) 
    {
        rec = new Rectangle2D.Double(-50,-50,100,100); //centered around origin of graphics
        xPos = stat.get(0); //sets all the starting stuff based on input.
        yPos = stat.get(1);
        theta = stat.get(7);
        xVel = stat.get(2);
        yVel = stat.get(3);
        xAcc = stat.get(4);
        yAcc = stat.get(5);
        speed = stat.get(6);
        turnSpeed = stat.get(8);
        teamNumber = team;
        maxSpeed = 0;
        maxTheta = 0;

        lastPressProcessed = 0;

        toTurn = 0;
        close = true;

        scrollX = 0;
        scrollY = 0;

        targetX = xPos;
        targetY = yPos;

        firingTargetX = 0;
        firingTargetY = 0;

        numWeapons = 0;
        weaponOffsetX = new int[0];
        weaponOffsetY = new int[0];
        weaponDirectionality = new int[0];
        weaponDamage = new double[0];
        weaponFireRate = new int[0];

        lastFired = new long[0];

        //fireRate = 200;
        lastFire = System.currentTimeMillis();

        isFiring = false;

        shippic = new ImageIcon("failure.png");
        shipimg = shippic.getImage();
        polyFileName = shipType;

        numPoints = 0;
        xPa = new int[0];
        yPa = new int[0];

        hp=0;
        destroyed = false;

        //now with individual weapon directionality!
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(polyFileName + ".poly"));
            inputStream.readLine();

            String imgFile = inputStream.readLine();
            if(team == 1){
                imgFile = imgFile.substring(0, imgFile.indexOf(".")) + "Red" + imgFile.substring(imgFile.indexOf("."), imgFile.length());
                //System.out.println(imgFile);
            }
            else if (team == 2)
            {
                imgFile = imgFile.substring(0, imgFile.indexOf(".")) + "Blue" + imgFile.substring(imgFile.indexOf("."), imgFile.length());
            }
            shippic = new ImageIcon(imgFile);
            shipimg = shippic.getImage();

            String defaultvars = inputStream.readLine();
            maxSpeed = Double.parseDouble(defaultvars.substring(0, defaultvars.indexOf(",")));
            maxTheta = Double.parseDouble(defaultvars.substring(defaultvars.indexOf(",")+1, nthIndexOf(defaultvars,',',2)));
            hp = Integer.parseInt(defaultvars.substring(nthIndexOf(defaultvars,',',2)+1, nthIndexOf(defaultvars,',',3)));
            maxHP = hp;
            //fireRate = Integer.parseInt(defaultvars.substring(nthIndexOf(defaultvars,',',3)+1,defaultvars.length()));

            numWeapons = Integer.parseInt(inputStream.readLine());
            weaponOffsetX = new int[numWeapons];
            weaponOffsetY = new int[numWeapons];
            weaponDirectionality = new int[numWeapons];
            weaponDamage = new double[numWeapons];
            weaponFireRate = new int[numWeapons];
            lastFired = new long[numWeapons];
            for(long l: lastFired)
            {
                l = System.currentTimeMillis();
            }

            String test = "";
            for(int i = 0; i<numWeapons; i++)
            {
                test = inputStream.readLine();
                weaponOffsetX[i] = (int) Double.parseDouble(test.substring(0, test.indexOf(",")));
                weaponOffsetY[i] = (int) Double.parseDouble(test.substring(test.indexOf(",")+1,nthIndexOf(test,',',2)));
                weaponDirectionality[i] = (int) Double.parseDouble(test.substring(nthIndexOf(test,',',2)+1, nthIndexOf(test,',',3)));
                weaponDamage[i] = Double.parseDouble(test.substring(nthIndexOf(test,',',3)+1, nthIndexOf(test,',',4)));
                weaponFireRate[i] = (int) Double.parseDouble(test.substring(nthIndexOf(test,',',4)+1, test.length()));
            }

            numPoints = Integer.parseInt(inputStream.readLine());

            xPa = new int[numPoints];
            yPa = new int[numPoints];

            test = "";
            for(int i = 0; i<numPoints; i++)
            {
                test = inputStream.readLine();
                xPa[i] = (int) Double.parseDouble(test.substring(0, test.indexOf(",")));
                yPa[i] = (int) Double.parseDouble(test.substring(test.indexOf(",")+1, test.length()));
            }

            inputStream.close();
        } catch(IOException|NumberFormatException i) {
            System.out.println("Error parsing polygon file " + polyFileName);

        }
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        //g2.fill(this.getHitBox());
        
        g2.translate(xPos+scrollX, yPos+scrollY);
        if(!destroyed)
        {
            double hudY = -(shippic.getIconHeight()/2 + 20);
            double hudX = 0;
            double hudDiameter = 30;double healthWidth = .175;
            Arc2D pie = new Arc2D.Double(hudX - (hudDiameter/2 + hudDiameter*healthWidth/2), hudY - (hudDiameter/2 + hudDiameter*healthWidth/2), hudDiameter * (1 + healthWidth), hudDiameter * (1 + healthWidth), -90, 180 * getHP()/getMaxHP(), Arc2D.PIE);            
            g2.setPaint(Color.GREEN);
            g2.fill(pie);
            Ellipse2D.Double mainHud = new Ellipse2D.Double(hudX - hudDiameter/2, hudY - hudDiameter/2, hudDiameter, hudDiameter);
            if(teamNumber == 1)
            {
                g2.setPaint(Color.RED);
            }
            else
            {
                g2.setPaint(Color.BLUE);
            }
            g2.fill(mainHud);

        }
        g2.rotate(theta + Math.PI); //+ (Math.PI/2.0));
        // g2.drawString("This way up!", -50, -50);
        
        //g2.fill(rec);

        if(!destroyed)
        {
            g2.drawImage(shipimg, -shippic.getIconWidth() / 4, -shippic.getIconHeight() / 4, shippic.getIconWidth()/2, shippic.getIconHeight()/2, null, null);
        }
        //g2.setPaint(Color.BLUE);
        //g2.fill(recb);

    }

    public void setScroll(double x, double y)
    {
        scrollX += x;
        scrollY += y;
        targetX += x;
        targetY += y;
    }

    public void setPosition(double x, double y) //set position, in x and y. 
    {
        xPos = x;
        yPos = y;
    }

    public void addPosition(double x, double y) //adds position
    {
        xPos+=x;
        yPos+=y;
    }

    public void setVelocity(double x, double y) //sets velocity in x and y
    {
        xVel = x;
        yVel = y;
    }

    public void addVelocity(double x, double y) //adds velocity
    {
        xVel += x;
        yVel += y;
    }

    public void setAccel(double x, double y) //acceleration
    {
        xAcc=x;
        yAcc=y;
    }

    public void addAccel(double s) //acceleration, but preserves direction
    {
        setAccel(Math.sin(state.get(7)) * s, -Math.cos(state.get(7)) * s);
    }

    public void addAccel(double x, double y)
    {
        xAcc += x;
        yAcc += y;
    }

    public void setTurn(double t) //angular velocity
    {
        turnSpeed=t;
    }

    //those above this line can be called when needed. 
    //all below this line are applied automatically every frame, don't call them unless you really have to

    public void doVel() //moves the ship based on current x and y velocity
    {
        addPosition(state.get(2), state.get(3));
    }

    public void doAccel() //changes velocity based on acceleration
    {

        xVel += state.get(4);

        yVel += state.get(5);
    }

    public void coast() //goes in the direction of speed vector (applied before doAccel)
    {

        xVel = speed * Math.cos(state.get(7));

        yVel = speed * Math.sin(state.get(7));
    }

    public void doInertiaSlow() //reduces acceleration to 0. 
    {
        setAccel(0.0,0.0);
    }

    public void doRotation() //happens first. Calculates speed vector, then changes direction based on turn speed
    {
        if(speed == state.get(6))     
        {
            speed = Math.sqrt(state.get(2) * state.get(2) + state.get(3) * state.get(3));
        }
        theta+=state.get(8);

    }

    public void doFriction() //old code, don't use. 
    {

        if(state.get(2)>0)
        {
            xVel -= Math.sin(state.get(7)) * friction;
            if(xVel<0) xVel = 0;
        }
        else if(state.get(2)<0)
        {
            xVel -= Math.sin(state.get(7)) *friction;
            if(xVel > 0) xVel = 0;
        }

        if(state.get(3)>0)
        {
            //yVel-=inSlow * 5 * 10;
            //yAcc  = -Math.sin(theta/4.0) * (speed * friction);
            yVel += Math.cos(state.get(7)) * friction;
            if(yVel<0) yVel = 0;
        }
        else if (state.get(3) <0)
        {
            yVel += Math.cos(state.get(7)) * friction;
            if(yVel > 0) yVel = 0;
        }

        if(state.get(8) > 0.003) 
        {
            turnSpeed -= .001;
        }
        else if(state.get(8) < -0.003) turnSpeed +=.001;
        else turnSpeed = 0;

        //         if(System.currentTimeMillis()-lastPressProcessed>100)
        //         {
        //             System.out.println("\f" + xVel + "\n" + xAcc);
        //             System.out.println(yVel + "\n" + yAcc);
        //             lastPressProcessed = System.currentTimeMillis();
        //         }
    }

    public ArrayList<Double> getState() //takes every useful variable from the current frame (use for collision detection)
    {
        state = new ArrayList<Double>();
        state.add(xPos);
        state.add(yPos);
        state.add(xVel);
        state.add(yVel);
        state.add(xAcc);
        state.add(yAcc);

        state.add(speed);
        state.add(theta);
        state.add(turnSpeed);
        state.add(scrollX);
        state.add(scrollY);
        applyState(state); //applies this state
        return state;
    }

    public void applyState(ArrayList<Double> st) 
    {

        xPos = st.get(0);
        yPos = st.get(1);
        xVel = st.get(2);
        yVel = st.get(3);
        xAcc = st.get(4);
        yAcc = st.get(5);
        speed = st.get(6);
        theta = st.get(7);
        turnSpeed = st.get(8);
        scrollX = st.get(9);
        scrollY = st.get(10);
    }

    public void setWaypoint(double x, double y)
    {
        targetX = x;
        targetY = y;

        double myTheta = state.get(7) % (Math.PI * 2); //my theta, between 0 and 6.28
        if(myTheta < 0) myTheta+= Math.PI * 2.0;
        double yDist = (state.get(1)+scrollY) - targetY; //y and x distance to the waypoint
        double xDist = (state.get(0)+scrollX) - targetX;
        //System.out.print((Math.sqrt((yDist * yDist) + (xDist * xDist))) + " "); 
        if((Math.sqrt((yDist * yDist) + (xDist * xDist)))<30) close = true; //if it hit, stop moving
        else close = false;
        double TTotal = Math.asin(yDist / (Math.sqrt((yDist * yDist) + (xDist * xDist)))); //waypoint's theta relative to origin of ship
        if(xPos+scrollX>x)
        {
            if(TTotal > 0)
            {
                TTotal = -TTotal + Math.PI;
            }
            else TTotal = -TTotal - Math.PI;
        }
        TTotal = -TTotal;
        if(TTotal < 0) TTotal += Math.PI * 2.0;

        toTurn = TTotal - myTheta;
        if(toTurn > Math.PI) 
        {
            toTurn = -(Math.PI * 2.0 - toTurn);

        }
        else if (toTurn < -Math.PI)
        {
            toTurn = (Math.PI * 2.0 + toTurn);
        }

        //speed = Math.min(maxSpeed, maxSpeed * (1-(Math.abs(toTurn) / Math.PI)) * (1-(Math.abs(toTurn) / Math.PI)) * (1-(Math.abs(toTurn) / Math.PI)));
        speed = Math.min(Math.min(maxSpeed, Math.sqrt((yDist * yDist) + (xDist * xDist))/40.0 * (1-(Math.abs(toTurn) / Math.PI)) * (1-(Math.abs(toTurn) / Math.PI)) * (1-(Math.abs(toTurn) / Math.PI))), state.get(6)+0.5);
        //yVel = Math.min(20, Math.sqrt((yDist * yDist) + (xDist * xDist)) * (1-(Math.abs(toTurn) / Math.PI)));

        //System.out.println(speed);
        //System.out.println(targetX + " " + targetY);
        //System.out.println(scrollX + " " + scrollY + " " + xPos + " " + yPos); //(toTurn) / Math.PI + " "+ TTotal / Math.PI + " " + myTheta/Math.PI);
    }

    public void followWaypoint()
    {

        if(!close)
        {
            setWaypoint(targetX + (scrollX - state.get(9)), targetY + (scrollY - state.get(10))); //calculate speed and direction to waypoint (same as last turn)
            double eachTurn; //turn this frame is a fraction of the total turn
            if(toTurn>0)
            {
                eachTurn = Math.min(toTurn, maxTheta);
            }
            else eachTurn = Math.max(-maxTheta, toTurn); //limit by maximum theta
            if(state.get(8)>=0 && eachTurn<=0 || state.get(8)<=0 && eachTurn>=0)
            {
                setTurn(eachTurn);
            }
            else setTurn(0);

        }
        else 
        {
            state.set(2, 0.0);
            state.set(3, 0.0);
            setTurn(0);
        }
    }

    //below this line is stuff for firing

    public void setFiringTarget(int x, int y)
    {
        firingTargetX = x;
        firingTargetY = y;
        //System.out.println(x + " " + y);
    }

    public double getTargetTheta()
    {
        double xD = (state.get(0)) - (firingTargetX);
        double yD = (state.get(1)) - (firingTargetY); //y and x distance to the waypoint

        double targetT = Math.asin(yD / (Math.sqrt((yD * yD) + (xD * xD)))); //waypoint's theta relative to origin of ship
        if(xPos>firingTargetX)
        {
            if(targetT > 0)
            {
                targetT = -targetT + Math.PI;
            }
            else targetT = -targetT - Math.PI;
        }

        targetT = -targetT;
        if(targetT < 0) targetT += Math.PI * 2.0;

        return targetT;
    }

    public void setFiring(boolean iF)
    {
        isFiring = iF;
    }

    public boolean isFiring()
    {
        return isFiring;
    }

    public ArrayList<ProjectileComponent> fire()
    {
        ArrayList<ProjectileComponent> toFire = new ArrayList<ProjectileComponent>();
        for(int i = 0; i < numWeapons; i++)
        {
            if(isFiring && lastFired[i] + weaponFireRate[i] < System.currentTimeMillis())
            {
                ArrayList<Double> newBullet = new ArrayList<Double>();
                int thisOffsetX = weaponOffsetX[i] - shippic.getIconWidth()/2; //not rotated...
                int thisOffsetY = weaponOffsetY[i] - shippic.getIconHeight()/2;
                double distance = Math.sqrt(Math.pow(thisOffsetX,2)+Math.pow(thisOffsetY,2));
                double weaponTheta = Math.atan(thisOffsetY/thisOffsetX);
                double totalTheta = weaponTheta + state.get(7);
                double xOff = (distance * Math.cos(totalTheta))/2;
                double yOff = (distance * Math.sin(totalTheta))/2;
                newBullet.add(state.get(0)+scrollX+xOff); //position(adjusted for scrolling)
                newBullet.add(state.get(1)+scrollY+yOff);
                if(weaponDirectionality[i]==1) 
                {
                    newBullet.add(getTargetTheta()); //direction to target, gotten from ship
                }
                else newBullet.add(theta);

                newBullet.add(25.0); //speed of bullet in x and y
                newBullet.add(25.0);
                int w = 1;
                toFire.add(new ProjectileComponent(newBullet, weaponDamage[i], teamNumber, 0));
                lastFired[i] = System.currentTimeMillis();
                System.out.println("." + i);
                playFireSound();
            }
        }

        return toFire;
    }

    public int getTeam()
    {
        return teamNumber;
    }

    //     public boolean readyToFire()
    //     {
    //         if(isFiring() && System.currentTimeMillis()-lastFire>fireRate)
    //         {
    //             return true;
    //         }
    //         else 
    //         {
    //             return false;
    //         }
    //     }

    public Shape getHitBox()
    {

        if(destroyed)
        {
            return new Area(new Polygon());
        }
        Polygon p = new Polygon(xPa, yPa, numPoints);
        Area pArea = new Area(p);
        if(polyFileName == "Enterprise")
        {
            Ellipse2D.Double disc = new Ellipse2D.Double();
            disc.setFrameFromDiagonal(40,40,266,266);
            pArea.add(new Area(disc));
        }

        AffineTransform at = new AffineTransform();

        at.translate(xPos+scrollX+shippic.getIconWidth() / 4-shippic.getIconWidth()/2, yPos+scrollY-shippic.getIconHeight() / 4);

        at.scale(.5, .5);
        //at.rotate(theta + Math.PI, (xPos-shippic.getIconWidth()), (yPos+shippic.getIconHeight()));
        at.rotate(theta+Math.PI,shippic.getIconWidth()/2.0,shippic.getIconHeight()/2.0);
        pArea.transform(at);
        return pArea;
    }

    public static int nthIndexOf(String text, char tofind, int n)
    {
        for (int i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) == tofind)
            {
                n--;
                if (n == 0)
                {
                    return i;
                }
            }
        }
        return -1;
    }

    public double getHP()
    {
        return hp;
    }

    public double getMaxHP()
    {
        return maxHP;
    }

    public boolean hit(double damage)
    {
        hp-=damage;

        if(hp<0)
        {
            destroyed = true;
            return true;
        }
        else return false; 
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public void playFireSound()
    {
        AudioInputStream music;
        try
        {
            music = AudioSystem.getAudioInputStream(new File("CylonProjectile.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(music);
            clip.start();
            //clip.loop(clip.LOOP_CONTINUOUSLY);
        }
        catch(Exception error)
        {
            // do nothing
        }
    }

    public String getType()
    {
        return polyFileName;
    }

}