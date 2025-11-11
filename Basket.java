import java.awt.*;

class Basket {
    int x, y, w, h;
    int maxX;
    int vx = 0;

    public Basket(int x, int y, int w, int h, int panelWidth) {
        this.x = x; this.y = y; this.w = w; this.h = h;
        this.maxX = panelWidth - w - 10;
    }

    public void setVelocity(int v) { this.vx = v; }

    public void update() {
        x += vx;
        if (x < 10) x = 10;
        if (x > maxX) x = maxX;
    }

    public void centerAt(int mouseX) {
        this.x = mouseX - w/2;
        if (x < 10) x = 10;
        if (x > maxX) x = maxX;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public void draw(Graphics2D g) {
        update(); // update basket position continuously
        // draw basket body
        g.setColor(new Color(200, 120, 40));
        g.fillRoundRect(x, y, w, h, 16, 14);
        // rim
        g.setColor(new Color(120, 70, 20));
        g.fillRect(x, y-6, w, 8);
        // highlight
        g.setColor(new Color(255, 255, 255, 60));
        g.fillRoundRect(x+8, y+8, w-16, h-12, 12, 12);
    }
}