import java.util.List;
import java.awt.Graphics2D;

public interface GameMode {

    void spawnItem(List<FallingItem> items, int width, int level);

    boolean onCatch(FallingItem f, GamePanel panel);

    void onMiss(FallingItem f, GamePanel panel);

    void drawHUD(Graphics2D g, GamePanel panel);
}
