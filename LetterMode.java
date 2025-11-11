import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Color;
import java.util.Collections;
import java.awt.Graphics2D;


public class LetterMode implements GameMode {

    private List<String> words = new ArrayList<>();
    private int currentWordIndex = 0;
    private String targetWord = "";
    private int charIndex = 0;

    public LetterMode(int level) {

        int numWords = 3 + (level - 1) * 2; // Level 1: 3 words, Level 2: 5 words, etc.

        List<String> pool = WordLoader.loadWords("words.txt");
        Collections.shuffle(pool);

        for (int i = 0; i < Math.min(numWords, pool.size()); i++) {
            words.add(pool.get(i).toUpperCase());
        }

        targetWord = words.get(0);
        charIndex = 0;
    }

    @Override
    public void spawnItem(List<FallingItem> items, int width, int level) {

        int x = ThreadLocalRandom.current().nextInt(20, width - 60);
        int speed = 3 + level;

        // 40% chance correct letter
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
        return (char)('A' + ThreadLocalRandom.current().nextInt(26));
    }

    @Override
    public boolean onCatch(FallingItem f, GamePanel panel) {

        LetterItem item = (LetterItem) f;
        char caught = item.letter;

        // ✅ CORRECT LETTER
        if (charIndex < targetWord.length() && caught == targetWord.charAt(charIndex)) {

            panel.score += 10;
            item.setCaughtColor();     // ✅ optional color change
            charIndex++;

            // ✅ WORD COMPLETED
            if (charIndex >= targetWord.length()) {

                currentWordIndex++;

                if (currentWordIndex >= words.size()) {
                    return true;       // ✅ level complete
                }

                // ✅ move to next word
                targetWord = words.get(currentWordIndex);
                charIndex = 0;
            }

            return false;
        }

        // ❌ WRONG LETTER
        panel.loseLife();
        return false;
    }

    @Override
    public void onMiss(FallingItem f, GamePanel panel) {
        // letters falling and missing DO NOT reduce lives
    }

    @Override
    public void drawHUD(Graphics2D g, GamePanel panel) {

        g.setColor(Color.WHITE);
        g.drawString("Word " + (currentWordIndex + 1) + "/" + words.size(), 
                      panel.width - 220, 30);

        g.drawString("Target: " + targetWord, 
                      panel.width - 220, 55);

        g.setColor(Color.YELLOW);
        g.drawString("Next Letter: " + targetWord.charAt(charIndex), 
                      panel.width - 220, 80);
    }
}
