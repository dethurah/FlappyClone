import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private int screenWidth = 900;
    private int screenHeigt = 600;
    private Timer timer = new Timer(5, this);

    private Bird bird;
    private boolean flapping = false;

    private int timeBetweenPipes = 500;
    private int pipeCountdown;
    private HashSet<PipePair> pipePairs = new HashSet<>();

    private boolean gameRunning;
    private int score;

    public Gameplay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        start();
    }

    public void paint(Graphics g) {
        //backround
        g.setColor(Color.black);
        g.fillRect(0, 0, screenWidth, screenHeigt);

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
        gameRunning = false;
    }

    private void start() {
        bird = new Bird(screenHeigt, timer);
        pipeCountdown = timeBetweenPipes;
        pipePairs.clear();
        score = 0;
        gameRunning = true;
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameRunning) {
            flapping = true;
            bird.flap();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameRunning) {
            start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            flapping = false;
        }
    }
}
