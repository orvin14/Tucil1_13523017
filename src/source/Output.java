package Source;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class Output {
    private static final Map<Character, Color> blockColors = new HashMap<>();
    static {
        Color[] colors = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN,
            Color.ORANGE, Color.PURPLE, Color.TEAL, Color.DEEPPINK, Color.LIMEGREEN, 
            Color.DARKTURQUOISE, Color.DARKMAGENTA, Color.DARKGOLDENROD, Color.AQUA,
            Color.CRIMSON, Color.GOLD, Color.ORCHID
        };

        for (int i = 0; i < 26; i++) {
            blockColors.put((char) ('A' + i), colors[i % colors.length]);
        }
        blockColors.put('.', Color.BLACK);
    }
    public static void saveToFile(String filename, String solution) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(solution); 
            System.out.println("Hasil disimpan ke: " + filename);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan file: " + e.getMessage());
        }
    }
    public static TextFlow getColoredSolution(char[][] board) {
        TextFlow textFlow = new TextFlow();

        for (char[] row : board) {
            for (char cell : row) {
                Color color = blockColors.getOrDefault(cell, Color.BLACK);

                Text text = new Text(cell + " ");
                text.setFill(color);
                textFlow.getChildren().add(text);
            }
            textFlow.getChildren().add(new Text("\n"));
        }
        return textFlow;
    }
}
