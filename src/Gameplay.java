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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private int screenWidth = 900;
    private int screenHeigt = 600;
    private Timer timer = new Timer(5, this);

    private Bird bird;

    private int timeBetweenPipes = 300;
    private int pipeCountdown;
    private HashSet<PipePair> pipePairs = new HashSet<>();

    private boolean inStartMenu;
    private int characterSelected = 0;
    private Bird.CharacterType[] characters = {Bird.CharacterType.NICOLAIW, Bird.CharacterType.MALTE, Bird.CharacterType.MADS, Bird.CharacterType.JEPPE, Bird.CharacterType.NEAL, Bird.CharacterType.NICOLAIL};
    private boolean gameRunning;
    private int score;

    private BufferedImage bg_top;
    private BufferedImage bg_mid;
    private BufferedImage bg_bottom;
    private double bg_mid_x = 0;
    private double bg_bottom_x = 0;
    private Clip birdsounds;

    private int personalBest;

    private HashMap<Bird.CharacterType, BufferedImage> characterSprites;

    public Gameplay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer.start();

        try {
            bg_top = ImageIO.read(getClass().getResourceAsStream("bg_top.png"));
            bg_mid = ImageIO.read(getClass().getResourceAsStream("bg_mid.png"));
            bg_bottom = ImageIO.read(getClass().getResourceAsStream("bg_bottom.png"));

            characterSprites = new HashMap<>();
            characterSprites.put(Bird.CharacterType.NICOLAIW, ImageIO.read(getClass().getResourceAsStream("nicolai1_2.png")));
            characterSprites.put(Bird.CharacterType.NICOLAIL, ImageIO.read(getClass().getResourceAsStream("nicolail1.png")));
            characterSprites.put(Bird.CharacterType.MALTE, ImageIO.read(getClass().getResourceAsStream("malte1.png")));
            characterSprites.put(Bird.CharacterType.JEPPE, ImageIO.read(getClass().getResourceAsStream("jeppe1.png")));
            characterSprites.put(Bird.CharacterType.NEAL, ImageIO.read(getClass().getResourceAsStream("neal1.png")));
            characterSprites.put(Bird.CharacterType.MADS, ImageIO.read(getClass().getResourceAsStream("mads1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream audio = getClass().getResourceAsStream("sounds/birds.wav");
            InputStream audioBuffer = new BufferedInputStream(audio);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioBuffer);
            birdsounds = AudioSystem.getClip();
            birdsounds.open(audioIn);
        } catch (Exception e4) {
            e4.printStackTrace();
        }

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("personalbest.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        personalBest = scanner.nextInt();
        scanner.close();

        inStartMenu = true;
        //start();
    }

    public void paint(Graphics g) {
        //img background
        g.drawImage(bg_mid, (int)bg_mid_x, 0, null);
        g.drawImage(bg_mid, (int)bg_mid_x + bg_mid.getWidth(), 0, null);

        g.drawImage(bg_bottom, (int)bg_bottom_x, 0, null);
        g.drawImage(bg_bottom, (int)bg_bottom_x + bg_bottom.getWidth(), 0, null);

        g.drawImage(bg_top, 0, 0, null);

        if (!inStartMenu) {
            //bird
            bird.draw(g);

            //pipes
            for (PipePair pipePair : pipePairs) {
                pipePair.draw(g);
            }

            //score;
            g.setColor(Color.white);
            g.setFont(new Font("Retro Gaming", 0, 20));
            g.drawString("SCORE: " + score, 5, 20);
            g.drawString("BEST: " + personalBest, 5, 40);
        }

        if (inStartMenu) {
            g.setColor(new Color(255, 255, 255, 127));
            g.fillRect(100, 100, 700, 400);
            g.setFont(new Font("Retro Gaming", 0, 30));
            g.setColor(Color.black);
            g.drawString("CHOOSE YOUR CHARACTER", 225, 200);
            g.setFont(new Font("Retro Gaming", 0, 10));
            g.drawString("ENTER: START", 400, 400);
            g.drawString("SPACE: FLY", 407, 420);
            g.drawString("ESC: MENU", 412, 440);

            g.setColor(new Color(0, 0, 0, 127));

            g.fillRect(175 + (characterSelected * 100), 273, 60, 60);

            g.drawImage(characterSprites.get(Bird.CharacterType.NICOLAIW), 160, 250, null);
            g.drawImage(characterSprites.get(Bird.CharacterType.MALTE), 260, 250, null);
            g.drawImage(characterSprites.get(Bird.CharacterType.MADS), 360, 250, null);
            g.drawImage(characterSprites.get(Bird.CharacterType.JEPPE), 460, 250, null);
            g.drawImage(characterSprites.get(Bird.CharacterType.NEAL), 560, 250, null);
            g.drawImage(characterSprites.get(Bird.CharacterType.NICOLAIL), 660, 250, null);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameRunning)  {
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
                        playSound("point.wav");
                        pipePair.setPassed(true);
                    }
                    if (bird.getHitBox().intersects(pipePair.getHitbox1()) || bird.getHitBox().intersects(pipePair.getHitbox2())) {
                        playSound("hit.wav");
                        die();
                    }
                } catch (Exception e1) {

                }
                if (pipePair.getX() + pipePair.getWidth() < 0) newPipepairs.remove(pipePair);
            }
            pipePairs = newPipepairs;

            bg_mid_x -= 0.2;
            bg_bottom_x -= 0.6;
            if (bg_mid_x < -bg_mid.getWidth()) bg_mid_x += bg_mid.getWidth();
            if (bg_bottom_x < -bg_bottom.getWidth()) bg_bottom_x += bg_bottom.getWidth();

            if (score > personalBest) {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("personalbest.txt"), StandardCharsets.UTF_8))) {
                    writer.write(score + "");
                    personalBest = score;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        repaint();
    }

    private void die() {
        timer.stop();
        birdsounds.stop();
        gameRunning = false;
    }

    private void exit() {
        birdsounds.stop();
        gameRunning = false;
        inStartMenu = true;
    }

    private void start(Bird.CharacterType characterType) {
        bird = new Bird(screenHeigt, timer, characterType);
        pipeCountdown = timeBetweenPipes;
        pipePairs.clear();
        score = 0;
        gameRunning = true;
        timer.start();
        birdsounds.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (inStartMenu) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (characterSelected > 0) characterSelected--;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (characterSelected < 5) characterSelected++;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                inStartMenu = false;
                start(characters[characterSelected]);
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_SPACE && gameRunning) {
                playSound("flap2.wav");
                bird.flap();
                bird.setFlapping(true);
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameRunning || e.getKeyCode() == KeyEvent.VK_ENTER && !gameRunning ) {
                start(characters[characterSelected ]);
            } else if (gameRunning && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                exit();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameRunning) {
            bird.setFlapping(false);
        }
    }

    private void playSound(String fileName) {
        try {
            InputStream is = getClass().getResourceAsStream("/sounds/" + fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
