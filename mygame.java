import javax.swing.*;

public class mygame extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mygame frame = new mygame();
            frame.setTitle("Catch n Score");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new GamePanel(720, 600));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

