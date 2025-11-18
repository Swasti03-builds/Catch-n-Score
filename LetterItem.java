import java.awt.*;

public class LetterItem extends FallingItem {

    private char letter;
    private boolean caught = false;  

    public LetterItem(int x, int y, int w, int h, int dy, char letter) {
        super(x, y, w, h, dy);
        this.letter = Character.toUpperCase(letter);
        this.pointValue = 10;
    }

    public void setCaughtColor() {
        caught = true;
    }

    public char getLetter() {
    return letter;
    }


    @Override
    public void draw(Graphics2D g) {

        if (caught) {
            g.setColor(new Color(0, 200, 0));   
        } else {
            g.setColor(new Color(70, 130, 180)); 
        }

        g.fillOval(x, y, w, h);

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));

        FontMetrics fm = g.getFontMetrics();
        String s = String.valueOf(letter);
        int sw = fm.stringWidth(s);
        int sh = fm.getAscent();

        // Center letter circle ka
        g.drawString(s, x + (w - sw) / 2, y + (h + sh) / 2 - 4);
    }

    @Override
    public void onCatch() {
        setCaughtColor();   // change color agar caught
    }

    @Override
    public void onMiss() {}
}
