/*
Slider.Java
Savio Joseph Benher
Moves slider x to the mouse position, startvaus to start it off at the middle
 */

import java.awt.*;
import javax.swing.*;

public class Slider {
    int sx, sy = 630, sw = 130, sh = 30;
    boolean start = true;
    Image vaus = new ImageIcon("assets//vaus.gif").getImage();

    public Slider(){
        sx = 450;
    }
    public void changeWidth(int change){ // used for enlarge powerup
        sw = change;
    }
    public void changeHeight(int change){ // used for UnEnlarge powerup
        sh = change;
    }

    public int getX(){return sx;}
    public int getY(){return sy;}
    public int getWidth(){return sw;}
    public void startVaus(){
        sx = 450;
    } // starts vaus at middle
    public void move(int moveX){
        if (moveX >= 12 && moveX <= 888){ //moves the vaus according to mouse position, restricts movements so that user can't drag vaus off the screen completely
            sx = moveX;
        }
    }

    public Rectangle getRect(){
        return new Rectangle(sx-(sw/2),sy,sw,sh);
    }
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.drawImage(vaus,sx-(sw/2), sy,sw, sh,null);
    }

}
