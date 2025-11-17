import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class GamePanel extends JPanel implements ActionListener, KeyListener, MouseMotionListener {

    final int width;            //panelsize
    final int height;

    private final javax.swing.Timer timer;
    private final int fps = 60;

    GameState state = GameState.MENU;

    private final Basket basket;
    final List<FallingItem> items = Collections.synchronizedList(new ArrayList<>());

    private GameMode mode;

    long lastSpawnTime = 0;
    int spawnIntervalMs = 1500;

    int score = 0;
    int lives = 5;
    int level = 1;

    public GamePanel(int w, int h) {

        this.width = w;
        this.height = h;

        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(30, 30, 40));
        setFocusable(true);

        addKeyListener(this);
        addMouseMotionListener(this);

        basket = new Basket(width / 2 - 50, height - 80, 100, 40, width);

        // default mode = fruits
        mode = new FruitMode(level);

        timer = new javax.swing.Timer(1000 / fps, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (state == GameState.RUNNING) {
            updateGame();
        }
        repaint();
    }

    private void updateGame() {
        long now = System.currentTimeMillis();

        // spawn
        if (now - lastSpawnTime > spawnIntervalMs) {
            mode.spawnItem(items, width, level);
            lastSpawnTime = now;
        }

        // move items
        synchronized (items) {
            Iterator<FallingItem> it = items.iterator();

            while (it.hasNext()) {
                FallingItem f = it.next();
                f.update();

                // if caught
                if (f.getBounds().intersects(basket.getBounds())) {
                    boolean levelComplete = mode.onCatch(f, this);

                    it.remove();
                    if (state == GameState.GAME_OVER) return;
                    if (levelComplete) {
                        state = GameState.LEVEL_COMPLETE;
                        SoundPlayer.play("sounds/level_up.wav");
                        timer.stop();
                    }
                    continue;
                }

                // if missed
                if (f.y > height) {
                    if (mode instanceof FruitMode) {
                    mode.onMiss(f, this);
                    }
                    it.remove();
                }
            }
        }
    }

    // painting everything
    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);

        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintBackground(g);
        basket.draw(g);

        synchronized (items) {
            for (FallingItem f : items) f.draw(g);
        }

        drawHUD(g);
        drawOverlay(g);
    }

    private void paintBackground(Graphics2D g) {
        g.setPaint(new GradientPaint(0, 0, new Color(25, 25, 40),
                                     0, height, new Color(10, 10, 20)));
        g.fillRect(0, 0, width, height);
    }

    private void drawHUD(Graphics2D g) {

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));

        g.drawString("Score: " + score, 20, 30);
        g.drawString("Lives: " + lives, 20, 55);
        g.drawString("Level: " + level, 20, 80);

        g.drawString("Mode: " + (mode instanceof FruitMode ? "FRUITS" : "LETTERS"), 20, 105);

        // mode-specific HUD
        mode.drawHUD(g, this);
    }

    private void drawOverlay(Graphics2D g) {

        if (state == GameState.RUNNING) return;

        String big = null;
        java.util.List<String> lines = new ArrayList<>();
// using switch case for state
        switch (state) {
    case MENU:
        big = "CATCH N SCORE";
        lines.add("Press ENTER to Start");
        lines.add("Press M to toggle mode");
        lines.add("Use LEFT/RIGHT or Mouse to move");
        break;

    case PAUSED:
        big = "PAUSED";
        lines.add("Press P to resume");
        break;

    case GAME_OVER:
        big = "GAME_OVER";
        lines.add("Final Score: " + score);
        lines.add("Press R to restart");
        break;

    case LEVEL_COMPLETE:
        big = "LEVEL " + level + " COMPLETE!";
        lines.add("Score: " + score);
        lines.add("Press ENTER for next level");
        break;
}


        // draw box
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(width / 4, height / 4, width / 2, height / 2);

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 36));
        FontMetrics fm = g.getFontMetrics();
        int bw = fm.stringWidth(big);
        g.drawString(big, width / 2 - bw / 2, height / 2 - 10);

        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        int y = height / 2 + 20;

        for (String s : lines) {
            int sw = g.getFontMetrics().stringWidth(s);
            g.drawString(s, width / 2 - sw / 2, y);
            y += 26;
        }
    }

    // all keys functions
    @Override
    public void keyPressed(KeyEvent e) {

        int k = e.getKeyCode();

        if (state == GameState.MENU) {
            if (k == KeyEvent.VK_ENTER) startGame();
            if (k == KeyEvent.VK_M) toggleMode();
            return;
        }

        if (state == GameState.RUNNING) {
            if (k == KeyEvent.VK_P) state = GameState.PAUSED;
            if (k == KeyEvent.VK_LEFT) basket.setVelocity(-8);
            if (k == KeyEvent.VK_RIGHT) basket.setVelocity(8);
            return;
        }

        if (state == GameState.PAUSED) {
            if (k == KeyEvent.VK_P) state = GameState.RUNNING;
            return;
        }

        if (state == GameState.GAME_OVER && k == KeyEvent.VK_R) {
            startGame();
            return;
        }

        if (state == GameState.LEVEL_COMPLETE && k == KeyEvent.VK_ENTER) {
            nextLevel();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT) {
            basket.setVelocity(0);
        }
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        basket.centerAt(e.getX());
    }

// starting game 
    private void startGame() {

        score = 0;
        lives = 5;
        items.clear();

        level = 1;

        // choose correct mode class
        if (mode instanceof FruitMode) mode = new FruitMode(level);
        else mode = new LetterMode(level);

        state = GameState.RUNNING;
        timer.start();
    }
// defines next level
    private void nextLevel() {

        level++;
        items.clear();
        lastSpawnTime = 0;
        if (mode instanceof FruitMode) mode = new FruitMode(level);
        else mode = new LetterMode(level);

        state = GameState.RUNNING;
        timer.start();
    }
// func to switch modes btwn letters and fruits
    private void toggleMode() {

    items.clear();
    level = 1;
    lastSpawnTime = 0;

    if (mode instanceof FruitMode) mode = new LetterMode(level);
    else mode = new FruitMode(level);
}

// function when u lose a life 
    public void loseLife() {
    lives--;
    if (lives <= 0) {
        lives = 0;
        state = GameState.GAME_OVER;
        SoundPlayer.play("sounds/game_over.wav");
        timer.stop();
    }
    }

}
