import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bird implements ActionListener {

    private double bird_pos_x;
    private double bird_pos_y;
    private int size = 20;

    private int screenheight;

    private double velY;
    private double acceleration = 2;
    private double gravity = 0.02;

    private Rectangle hitBox;

    public Bird(int screenheight, Timer timer) {
        bird_pos_x = 50;
        bird_pos_y = 400;
        hitBox = new Rectangle((int)bird_pos_x, (int)bird_pos_y, size, size);
        velY = 0;
        this.screenheight = screenheight;
        timer.addActionListener(this);
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
        g.setColor(Color.yellow);
        g.fillOval((int)bird_pos_x, (int)bird_pos_y, size, size);
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
}
