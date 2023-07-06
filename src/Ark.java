/*
Ark.java
Savio Joseph Benher
Entry point for program.
 */

import javax.swing.*;
public class Ark extends JFrame{
    ArkPanel game = new ArkPanel();
    public final int WIDTH = 900;
    public final int HEIGHT = 720;

    public Ark(){
        super("Arkanoid");
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game = new ArkPanel();
        add(game);
        pack();
        setResizable(false);
        setVisible(true);
    }
    public static void main (String[] args) {
        Ark frame = new Ark();
    }
}