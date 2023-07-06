/*
ArkPanel.java
Savio Joseph Benher
This class takes in mouse input from the user and acts as a place for other classes to interact and create
the functionality of the game.
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;




public class ArkPanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
    int mouseX;
    boolean startBall = true, aiming = false, firstTime = true;
    Menu menu;
    Slider vaus;
    Ball ball;
    Blocks blocks;
    Timer timer;
    PowerUps powerUps;
    Image i = new ImageIcon("assets//intro.png").getImage();
    Image intro = i.getScaledInstance(900, 720, Image.SCALE_SMOOTH);
    Image game = new ImageIcon("assets//game.png").getImage();
    //Image game = g.getScaledInstance(900,720,Image.SCALE_SMOOTH);
    Image l = new ImageIcon("assets//lose.png").getImage();
    Image lose = l.getScaledInstance(900, 720, Image.SCALE_SMOOTH);
    Image w = new ImageIcon("assets//win.png").getImage();
    Image win = w.getScaledInstance(900, 720, Image.SCALE_SMOOTH);
    Image li = new ImageIcon("assets//lives.png").getImage();
    Image live = li.getScaledInstance(20,30, Image.SCALE_SMOOTH);
    int INTRO = 0, GAME = 1, WON = 2, LOSE = 3;
    int screen = INTRO;
    boolean startLevel1 = true;
    int lives = 3;
    boolean gameOver = false;
    boolean playAgain = false;
    int prev;
    SoundEffect bounce;

    public ArkPanel() { //initializing everything
        setPreferredSize(new Dimension(900, 720));
        setFocusable(true);
        requestFocus();

        Toolkit tk = Toolkit.getDefaultToolkit();
        Image cursorImage = new ImageIcon("assets//cursor.png").getImage();
        Cursor c = tk.createCustomCursor( cursorImage, new Point( 15, 3 ), "My Cursor" );
        setCursor(c);

        bounce = new SoundEffect("src/sound/bounce.wav");

        addMouseListener(this);
        addMouseMotionListener(this);
        vaus = new Slider();
        ball = new Ball();
        blocks = new Blocks();
        powerUps = new PowerUps();
        menu = new Menu();

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(blocks.currLev == 0){ // if level one then make level 1 blocks. Don't want to do it muliple times so use a boolean and then set it to false
            //immediately.
            if (startLevel1){
                blocks.levelOne();
                startLevel1 = false;
            }
        }

        if(startBall){ // if your starting the level, ball and vaus are at the centre
            ball.start();
            vaus.startVaus();
        }

        else if(aiming){ // if you are aiming with the vaus then the ball should follow
            ball.aim(vaus);
        }

        else{ // if your not starting or aiming, then you are moving the ball
            ball.move(vaus);
        }

        if(!startBall) { // just to make sure the vaus doesn't move during the starting of a level
            vaus.move(mouseX);
        }

        if(ball.ballY>=720) {  // if ball falls through bottom of screen, you lose so -1 live
            lives -= 1;
            if (lives == 0) {  // if no lives then the game is over
                gameOver = true;
            }
            else {
                powerUps.restartPowerUps();  //makes sure that no powerUps are active
                startBall = true;  //its now back in starting a level state since it restarts
            }
        }

        if (gameOver) { //restarts everything, sets back to level 1
            screen = LOSE;
            restart();
            blocks.score = 0;
            startLevel1 = true;
            blocks.currLev = 0;
            gameOver = false;
            lives = 3;
        }

        if(blocks.win()){
            screen = WON;
        }

        if(ball.onVaus && powerUps.isCatch){ // if catch powerUp, revert back to aiming state
            aiming = true;
            powerUps.isCatch = false;
        }

        if(blocks.collide(ball)){ // if ball hits a block and its not silver or gold then a powerup can be created
            if(blocks.destroy) {
                powerUps.createPowerUps(blocks.getX(), blocks.getY(), blocks.getHeight());
                if(screen == GAME) {  // plays bounce sound whenever ball hits a block
                    bounce.stop();
                    bounce.play();
                }
            }
        }

        powerUps.collide(vaus,ball);
        if(blocks.destroy) { // if its not silver or gold then the laser should be also to destroy blocks
            powerUps.laserCollide(blocks,vaus);
        }
        repaint();
    }
    public void restart(){ // restarts everything for new levels and if the player wants to play again
        startBall = true;
        aiming = false;
        firstTime = true;
        ball = new Ball();
        powerUps = new PowerUps();
        vaus = new Slider();
        prev = blocks.score;
        blocks = new Blocks();
        blocks.score = prev;
    }

    @Override
    public void	mouseClicked(MouseEvent e){}
    @Override
    public void	mouseEntered(MouseEvent e){}
    @Override
    public void	mouseExited(MouseEvent e){}
    @Override
    public void mousePressed(MouseEvent e){
        if(screen == GAME){ // if you press the mouse then the ball should go into move state
                aiming = false;
            }
        else if(screen == INTRO){
            if(playAgain){ // if you won the game and are playing again then start level 1, make playagain false so that this only runs once
                //change block score to 0 as they are starting a new game.
                startLevel1 = true;
                playAgain = false;
                blocks.score = 0;
            }
            screen = GAME;
        }
        else if(screen == WON){
            if(blocks.currLev == 0){ // level 1
                restart(); //restart everything so that nothing carries on into level 2
                blocks.currLev = 1;  //change to level 2
                blocks.levelTwo(); // make second level blocks
                screen = GAME;
            }
            else if(blocks.currLev == 1){ // winning level 2
                restart();  // restart so that if you play again then nothing carries over
                screen = INTRO; // screen is intro because usually after you beat a video game, it brings you back to intro screen
                lives = 3;  //resets variables so that they don't carry over
                playAgain = true;
            }
        }
        else if(screen == LOSE){ //if you lose then you have to start all over at intro
            screen = INTRO;
        }

        }

    @Override
    public void	mouseReleased(MouseEvent e){}
    @Override
    public void mouseDragged(MouseEvent e){}
    @Override
    public void mouseMoved(MouseEvent e){
        mouseX = e.getX();

        if(screen == GAME) { // getting the mouse x position so that vaus and ball move whereever the mouse is
            if(startBall){ // if you move the mouse then you are trying to aim so startBalll = false and aiming is now true
                aiming = true;
                startBall = false;
            }


        }

    }

    @Override
    public void paint(Graphics g){

        if(screen == INTRO){
            //g.drawImage(intro,0,0,null);
            menu.drawIntro(g);
            menu.highScore(prev);
        }
        else if(screen == GAME) {
            g.drawImage(game, 0, 0, null);
            vaus.draw(g);
            blocks.draw(g);
            ball.draw(g);
            powerUps.draw(g, vaus, ball,blocks);
            powerUps.laserDraw(g, vaus);
            menu.highScore(blocks.score);
            menu.draw(g,blocks);
            for(int i=0;i<lives;i++){
                    g.drawImage(live, 750+50*i,15,null);
                }
        }
       else if(screen == WON){
            g.drawImage(win,0,0,null);
            menu.highScore(blocks.score);
            menu.draw(g,blocks);
        }
       else if(screen == LOSE){
            g.drawImage(lose,0,0,null);
            menu.highScore(blocks.score);
            menu.draw(g,blocks);
        }
    }
}
