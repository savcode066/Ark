/*
Menu.Java
Savio Joseph Benher
menu class uses fonts of different size to make intro screen, also writes the high score to the txt file and blits it
 */

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;

public class Menu {
    public static int high;
    Image i = new ImageIcon("assets/introbg.png").getImage();
    Image introbg = i.getScaledInstance(900, 720, Image.SCALE_SMOOTH);
    public static int s;
    public static Font fontSys=null, fontArk = null;

    public Menu(){
        high = 0;
        s = 0;
        fontSys = new Font("Engravers MT",Font.PLAIN,32);
        fontArk = new Font("Engravers MT",Font.PLAIN,64);
    }

    public void highScore(int score){
        try {
            Scanner inFile = new Scanner(new BufferedReader(new FileReader("src/highscore/scores.txt")));
            s = inFile.nextInt();
            high = Math.max(s, score); // finds which one is greatest and writes that to the txt
            inFile.close();

        }
        catch(IOException e){
            e.printStackTrace();
        }
        try{
            PrintWriter outFile = new PrintWriter(	new BufferedWriter (new FileWriter ("src/highscore/scores.txt")));
            outFile.print(high);
            outFile.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void drawIntro(Graphics g){
        g.drawImage(introbg, 0,0,null); // draws highscore and arkanoid for intro screen
        g.setFont(fontSys);
        g.setColor(Color.WHITE);
        g.drawString("High Score "+String.valueOf(high), 260 ,80);
        g.setFont(fontArk);
        g.setColor(Color.WHITE);
        g.drawString("Arkanoid",230,350);


    }
    public void draw(Graphics g, Blocks b){
        g.setFont(fontSys);
        g.setColor(Color.WHITE);
        g.drawString("High Score "+String.valueOf(b.score), 5 ,30);

    }
}
