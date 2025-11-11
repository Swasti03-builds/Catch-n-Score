import java.util.concurrent.ThreadLocalRandom;

public enum FruitType {
    APPLE, MELON, ORANGE, GRAPE;

    public static FruitType random() {
        FruitType[] vals = values();
        return vals[ThreadLocalRandom.current().nextInt(vals.length)];
    }
}
