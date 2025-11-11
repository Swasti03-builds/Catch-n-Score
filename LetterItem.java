import java.awt.*;

public class LetterItem extends FallingItem {

    private char letter;
    private boolean caught = false;   // ✅ track caught state

    public LetterItem(int x, int y, int w, int h, int dy, char letter) {
        super(x, y, w, h, dy);
        this.letter = Character.toUpperCase(letter);
        this.pointValue = 10;
    }

    // ✅ Called when player catches this letter
    public void setCaughtColor() {
        caught = true;
    }

    public char getLetter() {
    return letter;
    }


    @Override
    public void draw(Graphics2D g) {

        // ✅ Color changes if caught
        if (caught) {
            g.setColor(new Color(0, 200, 0));   // green highlight
        } else {
            g.setColor(new Color(70, 130, 180)); // normal blue
        }

        g.fillOval(x, y, w, h);

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));

        FontMetrics fm = g.getFontMetrics();
        String s = String.valueOf(letter);
        int sw = fm.stringWidth(s);
        int sh = fm.getAscent();

        // ✅ Center letter inside circle
        g.drawString(s, x + (w - sw) / 2, y + (h + sh) / 2 - 4);
    }

    @Override
    public void onCatch() {
        setCaughtColor();   // ✅ change color when caught
    }

    @Override
    public void onMiss() {
        // Optional: animations or penalty
    }
}
