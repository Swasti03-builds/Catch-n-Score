import java.awt.*;

public abstract class FallingItem implements Catchable {
    int x, y, w, h;
    int dy;
    int pointValue = 10;

    public FallingItem(int x, int y, int w, int h, int dy) {
        this.x = x; this.y = y; this.w = w; this.h = h; this.dy = dy;
    }

    public void update() {
        y += dy;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public abstract void draw(Graphics2D g);
}
