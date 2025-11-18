import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List; 

import java.awt.Color;
import java.awt.Graphics2D;


public class LetterMode implements GameMode {

    private List<String> words = new ArrayList<>();
    private int currentWordIndex = 0;
    private String targetWord = "";
    private int charIndex = 0;
    private boolean levelComplete = false;
    private int displayWordIndex = 0;


    public LetterMode(int level) {

        int numWords = 1 + (level - 1) * 2;

        List<String> pool = WordLoader.loadWords("words.txt");
        Collections.shuffle(pool);

        for (int i = 0; i < Math.min(numWords, pool.size()); i++) {
            words.add(pool.get(i).toUpperCase());
        }

        if (words.isEmpty()) {
            words.add("JAVA");
        }

        targetWord = words.get(0);
        charIndex = 0;
    }

    @Override
    public void spawnItem(List<FallingItem> items, int width, int level) {

        if (levelComplete) return;

        int x = ThreadLocalRandom.current().nextInt(20, width - 60);
        int speed = 3 + level;

        boolean dropCorrect = ThreadLocalRandom.current().nextInt(100) < 40;

        char letter;

        if (dropCorrect && charIndex < targetWord.length()) {
            letter = targetWord.charAt(charIndex);
        } else {
            letter = randomLetter();
        }

        items.add(new LetterItem(x, -30, 30, 30, speed, letter));
    }

    private char randomLetter() {
        return (char) ('A' + ThreadLocalRandom.current().nextInt(26));
    }

    @Override
    public boolean onCatch(FallingItem f, GamePanel panel) {

        if (!(f instanceof LetterItem)) return false;
        LetterItem item = (LetterItem) f;

        char caught = item.getLetter();

        // if correct
        if (charIndex < targetWord.length() && caught == targetWord.charAt(charIndex)) {

            SoundPlayer.play("sounds/catch.wav");

            panel.score += 10;
            item.setCaughtColor();
            charIndex++;

            // Word completed
            if (charIndex >= targetWord.length()) {
                displayWordIndex++;
                currentWordIndex++;

                if (currentWordIndex >= words.size()) {
                    levelComplete = true;
                    return true;
                }

                targetWord = words.get(currentWordIndex);
                charIndex = 0;
            }

            return false;
        }

        // if wrong
        SoundPlayer.play("sounds/miss.wav");
        panel.loseLife();
        return false;
    }

    @Override
    public void onMiss(FallingItem f, GamePanel panel) {
        // missed letter no effect
    }

    @Override
    public void drawHUD(Graphics2D g, GamePanel panel) {

        g.setColor(Color.WHITE);
        g.drawString("Words: " + displayWordIndex + "/" + words.size(),
                     panel.width - 260, 30);

        g.drawString("Target: " + targetWord,
                     panel.width - 260, 55);

        if (charIndex < targetWord.length()) {
            g.setColor(Color.YELLOW);
            g.drawString("Next: " + targetWord.charAt(charIndex),
                         panel.width - 260, 80);
        }
    }
}
