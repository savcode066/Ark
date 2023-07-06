import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

class Sound extends JFrame implements ActionListener
{
    JButton btn = new JButton("Play Sound");
    SoundEffect sound;

    public Sound() {
        JPanel jp = new JPanel();
        btn.addActionListener(this);
        jp.add(btn);
        add(jp);
        pack();

        sound = new SoundEffect("bottle-open.wav");

    }

    public void actionPerformed(ActionEvent ae){
        sound.play();
    }

    public static void main(String args[]){
        new Sound().setVisible(true);
    }
}

class SoundEffect{
    private Clip c;
    public SoundEffect(String filename){
        setClip(filename);
    }
    public void setClip(String filename){
        try{
            File f = new File(filename);
            c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(f));
        }
        catch(Exception e){ System.out.println("error"); }
    }
    public void play(){
        c.setFramePosition(0);
        c.start();
    }
    public void stop(){
        c.stop();
    }
}