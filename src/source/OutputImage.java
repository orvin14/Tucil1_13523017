package Source;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;


public class OutputImage{
    private static final int CELL_SIZE = 50; 
    private static final int PADDING = 10;
    private static final Map<Character, Color> blockColors = new HashMap<>();
    static {
        Color[] colors = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN,
            new Color(255, 165, 0),  // ORANGE
            new Color(128, 0, 128),  // PURPLE
            new Color(0, 128, 128),  // TEAL
            new Color(255, 20, 147), // DEEPPINK
            new Color(50, 205, 50),  // LIMEGREEN
            new Color(0, 206, 209),  // DARKTURQUOISE
            new Color(139, 0, 139),  // DARKMAGENTA
            new Color(184, 134, 11), // DARKGOLDENROD
            new Color(0, 255, 255),  // AQUA
            new Color(220, 20, 60),  // CRIMSON
            new Color(255, 215, 0),  // GOLD
            new Color(218, 112, 214) // ORCHID
        };

        for (int i = 0; i < 26; i++) {
            blockColors.put((char) ('A' + i), colors[i % colors.length]);
        }
        blockColors.put('.', Color.BLACK);
    }
	public static void saveSolutionAsImage(char[][] board, String filename) {
        int rows = board.length;
        int cols = board[0].length;

        int width = cols * CELL_SIZE + PADDING * 2;
        int height = rows * CELL_SIZE + PADDING * 2;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char block = board[r][c];
                g2d.setColor(blockColors.getOrDefault(block, Color.BLACK));
                g2d.fillRect(PADDING + c * CELL_SIZE, PADDING + r * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = PADDING + c * CELL_SIZE + (CELL_SIZE - fm.charWidth(block)) / 2;
                int textY = PADDING + r * CELL_SIZE + (CELL_SIZE + fm.getAscent()) / 2 - 5;
                g2d.drawString(String.valueOf(block), textX, textY);
            }
        }

        g2d.dispose();

        try {
            ImageIO.write(image, "png", new File(filename));
            System.out.println("Solusi disimpan sebagai " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}