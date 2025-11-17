import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Graphics2D;
import java.awt.Color;

public class FruitMode implements GameMode {

    private int fruitsCaught = 0;
    private int fruitsGoal;

    public FruitMode(int level) {
        this.fruitsGoal = 10 + level * 5;
    }

    @Override
    public void spawnItem(List<FallingItem> items, int width, int level) {
        int x = ThreadLocalRandom.current().nextInt(20, width - 60);
        int speed = 2 + level;

        items.add(new FruitItem(x, -30, 30, 30, speed, FruitType.random()));
    }

    @Override
    public boolean onCatch(FallingItem f, GamePanel panel) {
        SoundPlayer.play("sounds/catch.wav");
        if (panel.state == GameState.GAME_OVER) return false;
        panel.score += f.pointValue;
        fruitsCaught++;
        return fruitsCaught >= fruitsGoal;  // level complete
    }

    @Override
    public void onMiss(FallingItem f, GamePanel panel) {
        SoundPlayer.play("sounds/miss.wav");
        panel.loseLife();
    }

    @Override
    public void drawHUD(Graphics2D g, GamePanel panel) {
        g.setColor(Color.WHITE);
        g.drawString("Fruits: " + fruitsCaught + "/" + fruitsGoal,
                     panel.width - 220, 30);
    }
}
