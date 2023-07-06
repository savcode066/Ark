/*
Ball.java
Savio Joseph Benher
moves the ball using vectors, checks for collisions, starts the ball, puts it in aiming state, changes the ball velocity
changes ball's movement when it hits blocks.
 */

import javax.swing.*;
import java.awt.*;



public class Ball {
    private Image b = new ImageIcon("assets//ball.png").getImage();
    private Image ball = b.getScaledInstance(15,15,Image.SCALE_SMOOTH);

     int ballX,ballY;
    double velocity = 10,dir;
    private int ballW = 15, ballH = 15;

    double ballVX = 10, ballVY  = 10;
    boolean onVaus;

    public Ball(){
        ballX = 435;
        ballY = 610;

    }
    public void changeBallVelocity(double change){ // the general idea is change magintude but not sign
        if(change<1 && change>0) { // if a decimal less than 1 but greater then 0
            if (ballVY < 0) {
                ballVY = 10 * change * -1;  // if negative then it should keep being negative but change magnitude
            }
            else {
                ballVY = 10 * change;  //if postive keep being positive but change magnitude
            }

            velocity = 10 * change;
        }
        else{
            if (ballVY < 0) {
                ballVY = 5 * change * -1;
            }
            else {
                ballVY = 5 * change;
            }
            velocity = 5 * change;
        }
    }
    public void start(){ // starts ball at middle of the screen
        ballX = 435;
        ballY = 625;
    }
    public void aim(Slider s){  //subtracting ballY so that it is centred
            ballX = s.getX()-ballW;
            ballY = 625;

    }
    public void move(Slider s){
        ballX += ballVX;
        ballY += ballVY;

        onVaus = false;
            if (ballY <= 0) {  // if its on the top side then its going up so velocity is negative so it needs to change signs to change direction but
                //keep the same angle
               ballVY *=-1;
            }
            if(ballX<=0 || ballX>=900){
                ballX = ballX <= 0 ? 10 : 890;  // making sure the ball doesn't get stuck on the side
                ballVX*=-1; // changes direction

            }
            if (getRect().intersects(s.getRect())) {
                ballVY = Math.abs(ballVY) * -1 ; // makes the ballY negative so it goes up
                dir = (ballX-s.getX())+(s.getWidth()/2); // gets the angle for the ballX
                ballVX = velocity * Math.cos(Math.toRadians(dir))*-1;
                onVaus = true; // boolean for catch, checks if the ball is on vaus
            }

    }
    public void collide(boolean right, boolean left, boolean up,boolean down, int x, int y,int w, boolean gold){

        // block collision
        // changes the sign to change direction
        if(right){
            ballVX*=-1;
        }
        if(left){
            ballVX*=-1;

        }
        if(up){
            ballVY*=-1;

        }
        if(down){
            ballVY*=-1;

        }
    }

    public Rectangle getRect(){
        return new Rectangle(ballX,ballY,ballW,ballH);
    }
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.drawImage(ball,ballX, ballY,null);
    }

}
