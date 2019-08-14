import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private int screenWidth = 900;
    private int screenHeigt = 600;
    private Timer timer = new Timer(5, this);

    private Bird bird;

    private int timeBetweenPipes = 300;
    private int pipeCountdown;
    private HashSet<PipePair> pipePairs = new HashSet<>();

    private boolean gameRunning;
    private int score;

    private BufferedImage bg;
    private Clip music;

    public Gameplay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        try {
            bg = ImageIO.read(getClass().getResourceAsStream("bg-resized.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
/*
        try {
            InputStream audio = getClass().getResourceAsStream("sounds/birdssing.wav");
            InputStream audioBuffer = new BufferedInputStream(audio);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioBuffer);
            music = AudioSystem.getClip();
            music.open(audioIn);
        } catch (Exception e4) {

        }*/

        start();
    }

    public void paint(Graphics g) {
        //backround
        g.setColor(Color.black);
        g.fillRect(0, 0, screenWidth, screenHeigt);

        //img background
        g.drawImage(bg, 0, 0, null);

        //bird
        bird.draw(g);

        //pipes
        for (PipePair pipePair : pipePairs) {
            pipePair.draw(g);
        }

        //score;
        g.setColor(Color.yellow);
        g.drawString("Score: " + score, 5, 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pipeCountdown--;
        if (pipeCountdown == 0) {
            pipePairs.add(new PipePair(screenWidth, screenHeigt, timer));
            pipeCountdown = timeBetweenPipes;
        }

        HashSet<PipePair> newPipepairs = new HashSet<>(pipePairs);
        for (PipePair pipePair : pipePairs) {
            try {
                if (bird.getBird_pos_x() > pipePair.getX() + pipePair.getWidth() && !pipePair.isPassed()) {
                    score++;
                    pipePair.setPassed(true);
                }
                if (bird.getHitBox().intersects(pipePair.getHitbox1()) || bird.getHitBox().intersects(pipePair.getHitbox2())) {
                    die();
                }
            } catch (Exception e1) {

            }

            if (pipePair.getX() + pipePair.getWidth() < 0) newPipepairs.remove(pipePair);
        }
        pipePairs = newPipepairs;
        repaint();
    }

    private void die() {
        timer.stop();
        //music.stop();
        gameRunning = false;
    }

    private void start() {
        bird = new Bird(screenHeigt, timer);
        pipeCountdown = timeBetweenPipes;
        pipePairs.clear();
        score = 0;
        gameRunning = true;
        //music.loop(Clip.LOOP_CONTINUOUSLY);
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameRunning) {
            //flapping = true;
            playSound("flap.wav");
            bird.flap();
            bird.setFlapping(true);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameRunning) {
            start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            //flapping = false;
            bird.setFlapping(false);
        }
    }

    private void playSound(String fileName) {
        try {
            InputStream audio = getClass().getResourceAsStream("sounds/" + fileName);
            InputStream audioBuffer = new BufferedInputStream(audio);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioBuffer);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
