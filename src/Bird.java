import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bird implements ActionListener {

    private double bird_pos_x;
    private double bird_pos_y;
    private int size = 20;

    private int screenheight;

    private double velY;
    private double acceleration = 1.2;
    private double gravity = 0.02;

    private Rectangle hitBox;
    private boolean flapping = false;

    private CharacterType characterType;
    BufferedImage sprite;
    BufferedImage sprite2;

    public Bird(int screenheight, Timer timer, CharacterType characterType) {
        this.characterType = characterType;
        bird_pos_x = 150;
        bird_pos_y = 20;
        hitBox = new Rectangle((int)bird_pos_x, (int)bird_pos_y, size, size);
        velY = 0;
        this.screenheight = screenheight;
        timer.addActionListener(this);

        try {
            switch (characterType) {
                case NICOLAIW:
                    sprite = ImageIO.read(getClass().getResourceAsStream("nicolai1_2.png"));
                    sprite2 = ImageIO.read(getClass().getResourceAsStream("nicolai2_2.png"));
                    break;
                case NICOLAIL:
                    sprite = ImageIO.read(getClass().getResourceAsStream("nicolail1.png"));
                    sprite2 = ImageIO.read(getClass().getResourceAsStream("nicolail2.png"));
                    break;
                case MALTE:
                    sprite = ImageIO.read(getClass().getResourceAsStream("malte1.png"));
                    sprite2 = ImageIO.read(getClass().getResourceAsStream("malte2.png"));
                    break;
                case MADS:
                    sprite = ImageIO.read(getClass().getResourceAsStream("mads1.png"));
                    sprite2 = ImageIO.read(getClass().getResourceAsStream("mads2.png"));
                    break;
                case JEPPE:
                    sprite = ImageIO.read(getClass().getResourceAsStream("jeppe1.png"));
                    sprite2 = ImageIO.read(getClass().getResourceAsStream("jeppe2.png"));
                    break;
                case NEAL:
                    sprite = ImageIO.read(getClass().getResourceAsStream("neal1.png"));
                    sprite2 = ImageIO.read(getClass().getResourceAsStream("neal2.png"));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //gravity
        if (bird_pos_y < screenheight - 2*size) {
            velY += gravity;
        }

        //take care of borders
        if (bird_pos_y + velY > screenheight - 2*size || bird_pos_y + velY < 0) {
            velY = 0;
        }

        bird_pos_y += velY;
        hitBox = new Rectangle((int)bird_pos_x, (int)bird_pos_y, size, size);
    }

    public void draw(Graphics g) {
        //g.setColor(Color.yellow);
        //g.fillOval((int)bird_pos_x, (int)bird_pos_y, size, size);

        if (!flapping) {
            g.drawImage(rotateImageByDegrees(sprite, velY*12), (int)bird_pos_x - 50, (int)bird_pos_y - 50, null);
        } else {
            g.drawImage(rotateImageByDegrees(sprite2, velY*12), (int)bird_pos_x - 50, (int)bird_pos_y - 50, null);
        }

    }

    public void flap() {
        velY -= acceleration;
    }

    public double getBird_pos_x() {
        return bird_pos_x;
    }

    public double getBird_pos_y() {
        return bird_pos_y;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setFlapping(boolean flapping) {
        this.flapping = flapping;
    }

    private BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        //g2d.setColor(Color.RED);
        //g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
        g2d.dispose();

        return rotated;
    }

    public enum CharacterType {
        NICOLAIW,
        NICOLAIL,
        MALTE,
        MADS,
        JEPPE,
        NEAL
    }
}
