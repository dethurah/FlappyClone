import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 900, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("dumt spil");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Gameplay gameplay = new Gameplay();
        frame.add(gameplay);

        frame.setVisible(true);
    }

}
