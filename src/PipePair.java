import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;

public class PipePair implements ActionListener {

    private double x;
    private double y;
    private int width = 40;
    private int gapY;
    private int gapHeight = 100;
    private int screenWidth;
    private int screenHeight;

    private double speed = 1;

    private Rectangle upperRect;
    private Rectangle lowerRect;

    private boolean passed;

    public PipePair(int screenWidth, int screenHeight, Timer timer) {
        timer.addActionListener(this);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        x = screenWidth;
        gapY = (int) (Math.random() * (screenHeight - gapHeight));
        passed = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        x -= speed;
        upperRect = new Rectangle((int)x, 0, width, gapY);
        lowerRect = new Rectangle((int)x, gapY + gapHeight, width, screenHeight - gapY - gapHeight);
    }

    public void draw(Graphics g) {
        g.setColor(Color.gray);

        try {
            g.fillRect(upperRect.x, upperRect.y, upperRect.width, upperRect.height);
            g.fillRect(lowerRect.x, lowerRect.y, lowerRect.width, lowerRect.height);
        } catch (Exception e) {

        }
    }

    public Rectangle getHitbox1() {
        return upperRect;
    }

    public Rectangle getHitbox2() {
        return lowerRect;
    }

    public double getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isPassed() {
        return passed;
    }
}
