import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {

    public static void play(String filename) {
        try {
            File soundFile = new File(filename);
            if (!soundFile.exists()) {
                System.out.println("Sound file not found: " + filename);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }
}
