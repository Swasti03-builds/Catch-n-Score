import java.awt.*;
import java.awt.GradientPaint;
import java.util.concurrent.ThreadLocalRandom;

class FruitItem extends FallingItem {
    private final FruitType type;
    public FruitItem(int x, int y, int w, int h, int dy, FruitType type) {
        super(x, y, w, h, dy);
        this.type = type;
        switch (type) {
            case APPLE -> { pointValue = 15; }
            case MELON -> { pointValue = 10; }
            case ORANGE -> { pointValue = 12; }
            case GRAPE -> { pointValue = 18; }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // Draw a simple fruit shape depending on type
        switch (type) {
            case APPLE -> drawApple(g);
            case MELON -> drawMelon(g);
            case ORANGE -> drawOrange(g);
            case GRAPE -> drawGrapes(g);
        }
    }
    @Override
    public void onCatch() {}

    @Override
    public void onMiss() {}

    private void drawApple(Graphics2D g) {
        g.setColor(new Color(220, 40, 40));
        g.fillOval(x, y, w, h);
        g.setColor(new Color(90, 40, 20));
        g.fillRect(x + w/2 - 2, y - 8, 4, 8); // stem
        g.setColor(new Color(255,255,255,60));
        g.fillOval(x + 6, y + 6, 8, 6);
    }

    private void drawMelon(Graphics2D g) {
        g.setColor(new Color(30, 150, 50));
        g.fillArc(x, y, w, h+6, 0, 180);
        g.setColor(new Color(240, 50, 60));
        g.fillArc(x+4, y+4, w-8, h-4, 0, 180);
    }

    private void drawOrange(Graphics2D g) {
        g.setColor(new Color(255, 140, 0));
        g.fillOval(x, y, w, h);
        g.setColor(new Color(255, 200, 120, 50));
        g.fillOval(x + 6, y + 6, 8, 6);
    }

    private void drawGrapes(Graphics2D g) {

    int r = (int)(w / 2.5);  // grape size
    int step = r - 4; // overlap amount for compact cluster

    // Define how many grapes in each row (bottom → top)
    int[] rows = {3, 2, 1};
    int startY = y;

    // Draw rows
    for (int row = 0; row < rows.length; row++) {
        int count = rows[row];
        // Center grapes horizontally
        int startX = x + (w - (count * step + (r - step))) / 2;

        for (int i = 0; i < count; i++) {
            int gx = startX + i * step;
            int gy = startY + row * step;

            // Slight random variation in purple shade
            Color base = new Color(120 + (int)(Math.random() * 30), 30, 140);
            g.setColor(base);
            g.fillOval(gx, gy, r, r);

            // Shading (darker gradient for depth)
            GradientPaint shade = new GradientPaint(
                gx, gy, new Color(150, 50, 180),
                gx + r, gy + r, new Color(70, 20, 90)
            );
            g.setPaint(shade);
            g.fillOval(gx, gy, r, r);

            // Small glossy highlight (top-left)
            g.setColor(new Color(255, 255, 255, 80));
            g.fillOval(gx + r/4, gy + r/5, r/4, r/5);
        }
    }

    // Draw stem at top center
    g.setColor(new Color(90, 40, 20));
    g.fillRect(x + w / 2 - 2, y - 8, 4, 8);

    // Optional tiny green leaf near stem
    g.setColor(new Color(60, 160, 60));
    int[] lx = {x + w / 2 + 3, x + w / 2 + 13, x + w / 2 + 5};
    int[] ly = {y - 5, y + 5, y + 8};
    g.fillPolygon(lx, ly, 3);
}

}
