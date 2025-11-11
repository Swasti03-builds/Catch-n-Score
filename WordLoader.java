import java.io.*;
import java.util.*;

public class WordLoader {

    public static List<String> loadWords(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toUpperCase();
                if (!line.isEmpty()) words.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error loading words: " + e.getMessage());
        }
        return words;
    }
}

