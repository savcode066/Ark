/*
Blocks.java
Savio Joseph Benher
Blocks contains 2 levels, collision system where it find the blocks which the ball is colliding with, functions to check if the
block is silver or gold and a highscore function for keeping track of how many points gained from each block destroyed
 */
import javax.swing.*;
import java.awt.geom.Line2D;
import java.util.*;
import java.awt.*;
public class Blocks {
     int [][]level1 =
            {{100,400},{100,330},{100,260},{100,190},
            {200,190},{200,260},{200,330},{200,400},
            {300,540},{300,470},{300,400},{300,330},{300,260},{300,190},{300,120}, {300,50},
            {400,50},{400,120},{400,190},{400,260},{400,330},{400,400},{400,470},
            {500,50},{500,330},{500,400},
            {600,50},{600,330},{600,400},{600,470},
            {700,120},{700,50},{700,190},{700,260},{700,330},{700,400},{700,470}};

    int[][]level2 = {{8,0,50,3},{8,100,120,3},{8,200,190,3},{8,300,260,3},{8,400,330,3},{8,500,400,3},
            {0,0,120},{0,100,190},{0,200,260},{0,300,330},{0,400,400},
            {1,0,190},{1,100,260},{1,200,330},{1,300,400},
            {4,0,330},{4,100,400},
            {5,0,400},
            {6,0,260},{6,100,330},{6,200,400},
            {9,0,470},{9,100,470},{9,200,470},{9,300,470},{9,400,470},{9,500,470},{9,600,470}};


    Image yellow, white,violet,red,orange,green,cyan,blue,silver,gold;
    Image [] blockImgs = {yellow, white,violet,red,orange,green,cyan,blue,silver,gold};

    String [] blockNames = {"yellow", "white","violet","red","orange","green","cyan","blue","silver","gold"};
    ArrayList<Rectangle> blocks = new ArrayList();
    ArrayList rem = new ArrayList();
    ArrayList remSilver = new ArrayList();
    private int bx,by, bw = 100, bh = 70;
    int count = 0;
    int score = 0;
    private int isRed = 0;
    int []levelblocks = {49,21}; // number of blocks in each level. Used to check if the player won.
    int currLev = 0;
    boolean destroy = false;

    public Blocks() {
        for(int i = 0;i<blockImgs.length;i++){
            blockImgs[i] = new ImageIcon("assets//"+blockNames[i]+".png").getImage();
            blockImgs[i] = blockImgs[i].getScaledInstance(100,70,Image.SCALE_SMOOTH);
        }
    }
    public void levelOne(){
     for(int y = 50; y < 540; y += 70) {
            for (int x=100; x<800;x+=100) {
                blocks.add(new Rectangle(x, y, bw, bh));  // creates level 1 blocks
            }
        }
    }
    public void levelTwo(){
        for (int i = 0; i <level2.length; i++) {
                blocks.add(new Rectangle(level2[i][1], level2[i][2], bw, bh)); // creates level 2 blocks
            }
    }


    public ArrayList<Rectangle> getRects(){
        return blocks;
    }
    public boolean collide(Ball b){  // finds which block the ball collided and uses ball to check which side it hit
        for(int i=0;i<blocks.size();i++){
            if(b.getRect().intersects(blocks.get(i))){
                count++;
                bx = (int)blocks.get(i).getX();
                by = (int) blocks.get(i).getY();

                Line2D blockLeft = new Line2D.Double(blocks.get(i).getX() + 0, blocks.get(i).getY(), blocks.get(i).getX(), blocks.get(i).getY() +blocks.get(i).getHeight());
                Line2D blockRight = new Line2D.Double(blocks.get(i).getX() + blocks.get(i).getWidth(), blocks.get(i).getY(), blocks.get(i).getX()+blocks.get(i).getWidth(), blocks.get(i).getY() +blocks.get(i).getHeight());
                Line2D blockTop = new Line2D.Double(blocks.get(i).getX() + 0, blocks.get(i).getY(), blocks.get(i).getX() + blocks.get(i).getWidth(), blocks.get(i).getY());
                Line2D blockBottom = new Line2D.Double(blocks.get(i).getX(), blocks.get(i).getY() + blocks.get(i).getHeight(), blocks.get(i).getX()+blocks.get(i).getWidth(), blocks.get(i).getY() +blocks.get(i).getHeight());

                highScore((int)blocks.get(i).getX(),(int)blocks.get(i).getY());

                b.collide(blockLeft.intersects(b.getRect()),blockRight.intersects(b.getRect()),
                        blockTop.intersects(b.getRect()),blockBottom.intersects(b.getRect()),(int)blocks.get(i).getX(),(int)blocks.get(i).getY(),(int)blocks.get(i).getWidth(), isGold((int)blocks.get(i).getX(),(int)blocks.get(i).getY()));

                // if its silver or gold then it shouldn't get removed (silver gets removed after 3 hits)
                if(!isSilver((int)blocks.get(i).getX(),(int)blocks.get(i).getY()) && !isGold((int)blocks.get(i).getX(),(int)blocks.get(i).getY())){
                    levelblocks[currLev] -= 1;
                    rem.add(blocks.get(i));
                    blocks.removeAll(rem);
                    destroy = true;
                }
                else{
                    destroy = false;
                    return false;
                }
                break;
            }
        }
        //used to see if there was a collision or not
        if(count!=0){
            count = 0;
            return true;
        }
        else{
            return false;
        }
    }

    public void highScore(int x, int y){
                if (currLev == 1) { // checks if silver has been hit 3 times and then adds score
                    for (int[] col : level2) {
                        if(x == col[1] && y== col[2] && col[0] == 8){
                            if(col[3] == 1){
                                score+=80;
                            }
                        }
                        // adds score based on type of color
                        else if (x == col[1] && y== col[2] && col[0] != 9 && col[0] != 8) {
                            score += col[0] * 10;
                            break;
                        }
                    }
                }
                //score for first level
                else if (currLev == 0) {
                    isRed = 0;
                    for (int[] col : level1) {
                        if (x == col[0] && y == col[1]) {
                            score += 40;
                            isRed++;
                            break;
                        }
                    }
                    if (isRed == 0) {
                        score += 30;
                    }
                }
    }

    public boolean isGold(int x, int y){ // checking if the block at the x y position is gold
        if(currLev == 1) {
            for (int[] col : level2) {
                if (x == col[1] && y == col[2]) {
                    if (col[0] == 9) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean isSilver(int x, int y){ // checking if the block at x y position is silver
        int countSilver = 0;
        if(currLev == 1) {
            for (int i=0;i<level2.length;i++) {
                if (x == level2[i][1] && y == level2[i][2] && 8 == level2[i][0]) {
                    countSilver++;
                    level2[i][3] -= 1;
                    if(level2[i][3] == 0){
                        remSilver.add(new Rectangle(x,y,bw,bh));
                        levelblocks[currLev] -=1; // deals with getting rid of silver blocks here
                    }
                }
            }
        }

        blocks.removeAll(remSilver);
        if(countSilver != 0){
            return true;
        }
        else{
            return false;
        }

    }
    public boolean win() { // check if there are no more blocks
        if(levelblocks[currLev] == 0) {

            return true;
        }
        return false;

    }
    public int getX(){return bx;}
    public int getY(){return by;}
    public int getWidth(){return bw;}
    public int getHeight(){return bh;}

    public void draw(Graphics g) {
        if(currLev == 0){ // draws based on  the hard coded levels
            for (int i = 0; i < blocks.size(); i++) {
                g.drawImage(blockImgs[2], (int) blocks.get(i).getX(), (int) blocks.get(i).getY(), null);
                for (int[] block : level1) {
                    if ((int) blocks.get(i).getX() == block[0] && (int) blocks.get(i).getY() == block[1]) {
                        g.drawImage(blockImgs[3], (int) blocks.get(i).getX(), (int) blocks.get(i).getY(), null);
                    }
                }
            }
        }
        else if(currLev == 1) {
            for (int i = 0; i < blocks.size(); i++) {
                for (int[] col : level2) {
                    if ((int) blocks.get(i).getX() == col[1] && (int) blocks.get(i).getY() == col[2])
                        g.drawImage(blockImgs[col[0]], col[1], col[2], null);
                }
            }
        }
    }
}
