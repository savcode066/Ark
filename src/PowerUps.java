/*
PowerUps.java
Savio Joseph Benher
Creates the powerup capsules and checks for collision with vaus to activate the powerups. The powerups are slow, enlarge, catch, and laser.
Slow slows the ball, enlarge makes the ball bigger, catch allows you to "catch" the ball instead of it bouncing back out, laser fires lasers to
destroy blocks. Unslow and Unenlarge undo slow and enlarge when neither powerup is active.
 */

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.geom.*;

public class PowerUps {
    private Random rand = new Random();
    Rectangle temp;
    private static int laserW = 10, laserH = 20;
    private static final Image l = new ImageIcon("assets//laser.png").getImage();
    private Image laser = l.getScaledInstance(laserW, laserH, Image.SCALE_SMOOTH);
    private static Image lasersCap, enlargeCap, catchhCap, slowCap;
    private static final Image[] powerImgs = {lasersCap, enlargeCap, catchhCap, slowCap};
    private static final String[] powerNames = {"laserCap", "enlargeCap", "slowCap", "catchCap"};

    ArrayList<Rectangle> laserArr = new ArrayList();
    ArrayList<int[]> drops = new ArrayList();
    ArrayList rem = new ArrayList();

    ArrayList remBlock = new ArrayList();
    ArrayList remLaser = new ArrayList();
    int[] capsuleDrops;
    boolean isCatch,isLaser;
    private static final int ch = 30, cw = 50;
    public PowerUps() {
        for (int i = 0; i < powerImgs.length; i++) {
            powerImgs[i] = new ImageIcon("assets//" + powerNames[i] + ".gif").getImage();
        }
    }
    public void restartPowerUps(){
        isLaser = false;
        isCatch = false;
        laserArr.removeAll(laserArr);
        drops.removeAll(drops);
    }
    public void createPowerUps(int powerX, int powerY, int powerHeight) {
        if (rand.nextInt(101) > 70) { // 30% chance for powerup spawns
            capsuleDrops = new int[]{rand.nextInt(4), powerX, powerY + powerHeight, cw, ch}; // creating random power capsule
            drops.add(capsuleDrops);
            remBlock.removeAll(remBlock);
        }
    }

    public void collide(Slider s, Ball b) {
        for (int i = 0; i < drops.size(); i++) {
            temp = new Rectangle(drops.get(i)[1], drops.get(i)[2], drops.get(i)[3], drops.get(i)[4]); // creates capsule rectangle for collision checking
            if(temp.intersects(s.getRect())) {
                if(drops.get(i)[0] == 0 ) { //0 for laser, 1 for enlarge, etc...
                    //when a powerUp is active, I turn off all the other powerUps
                    isLaser = true;
                    laserCreate(s);
                    unSlow(b);
                    unEnlarge(s);
                    isCatch = false;
                }
                else if(drops.get(i)[0] == 1 ) {
                    enlarge(s);
                    unSlow(b);
                    isCatch = false;
                    isLaser = false;
                }
               else if(drops.get(i)[0] == 2 ) {
                    slow(b);
                    unEnlarge(s);
                    isCatch = false;
                    isLaser = false;

                }
                else if(drops.get(i)[0] == 3){
                    unSlow(b);
                    unEnlarge(s);
                    isCatch = true;
                    isLaser = false;

                }
                rem.add(drops.get(i));
            }
        }
        drops.removeAll(rem); //they collided with the vaus so they should disappear


    }

    public void enlarge(Slider s) { // makes vaus bigger
        s.changeWidth(140);
        s.changeHeight(40);
    }
    public void unEnlarge(Slider s){ // changes it back to original size
        s.changeWidth(130);
        s.changeHeight(30);
    }

    public void slow(Ball b) {  //slows ball down
        b.changeBallVelocity(0.5);
    }
    public void unSlow(Ball b){   // undone's the slow
        b.changeBallVelocity(2);
    }

    public void draw(Graphics g, Slider s, Ball b,Blocks block) {
        for (int i = 0; i < drops.size(); i++) {
            if (drops.get(i)[2] < 720) { // if it goes past the top of the screen then it won't be drawn
                g.drawImage(powerImgs[drops.get(i)[0]], drops.get(i)[1], drops.get(i)[2],drops.get(i)[3],drops.get(i)[4], null);
                drops.get(i)[2] += 3; // adds to make the capsules fall down
            }
        }
    }
    public void laserCreate(Slider s) { // creates lasers
        if(laserArr.size() ==0 && isLaser) { // it has to be 0 because I create new lasers after the old ones are gone.
            laserArr.add(new Rectangle(s.getX()-(s.getWidth()/2), s.getY() - laserH, laserW, laserH));
            laserArr.add(new Rectangle(s.getX() + (s.getWidth()/2) - laserW, s.getY() - laserH, laserW, laserH));
        }
    }
    public void laserCollide(Blocks block, Slider s){
        for(int i=0;i<laserArr.size();i++) {
            //only checking the top not rect because it should not have side collision
            Line2D laserTop = new Line2D.Double(laserArr.get(i).getX(), laserArr.get(i).getY(), laserArr.get(i).getX()+laserArr.get(i).getWidth(), laserArr.get(i).getY());
            for(int j=0;j<block.getRects().size();j++){
                if(laserTop.intersects(block.getRects().get(j))){
                    //it's a normal block if it's not silver or gold
                   if(!block.isSilver((int)block.getRects().get(j).getX(),(int)block.getRects().get(j).getY()) && !block.isGold((int)block.getRects().get(j).getX(),(int)block.getRects().get(j).getY())){
                       block.levelblocks[block.currLev] -=1;
                       remBlock.add(block.getRects().get(j));
                       remLaser.add(laserArr.get(i));
                    }
                   // if the block is gold then when the laser hits it, it should disappear
                   else if(block.isGold((int)block.getRects().get(j).getX(),(int)block.getRects().get(j).getY())){
                       remLaser.add(laserArr.get(i));
                   }
                   else{
                       // if its normal then just get rid of laser
                       remLaser.add(laserArr.get(i));
                   }
                    // laser also contributes to score
                    block.highScore((int)block.getRects().get(j).getX(),(int)block.getRects().get(j).getY());
                }
            }
            if(laserArr.get(i).getY()<=0){ // checking if it's past the top of screen, if it is then it should be gone
                remLaser.add(laserArr.get(i));
            }
        }
        block.getRects().removeAll(remBlock);
        laserArr.removeAll(remLaser);
        remLaser.removeAll(remLaser); // making sure that old laser's coordinates don't make the program remove new laser's coordinates which are the same
        laserCreate(s); // create the new lasers after the old ones are gone

    }



    public void laserDraw(Graphics g, Slider s){
        for(int i =0; i<laserArr.size();i++){
            g.drawImage(laser,(int)laserArr.get(i).getX(),(int)laserArr.get(i).getY(),null);
            laserArr.get(i).y -=10; // to make the lasers go up
        }

    }

}
