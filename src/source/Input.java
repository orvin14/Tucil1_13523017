package Source;

import java.io.*;
import java.util.*;

public class Input {
    public int rows, cols, blockCount;
    private char[][] board;
    public List<List<char[][]>> blocks;
    public List<Character> blockType;

    public Input(String filename) throws IOException {
        readInput(filename);
        board = new char[rows][cols];
        for (char[] row : board) Arrays.fill(row, '.');
    }

    private void readInput(String filename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    String line = br.readLine();
    
    if (line == null) {
        br.close();
        throw new IOException("Empty input file");
    }
    StringTokenizer st = new StringTokenizer(line);
    rows = Integer.parseInt(st.nextToken());
    cols = Integer.parseInt(st.nextToken());
    blockCount = Integer.parseInt(st.nextToken());
    blocks = new ArrayList<>();
    blockType = new ArrayList<>();

    Map<Character, List<String>> blockShapes = new LinkedHashMap<>();

    line = br.readLine();
    while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) continue;
        char blockChar = line.charAt(0);
        blockShapes.putIfAbsent(blockChar, new ArrayList<>());
        blockShapes.get(blockChar).add(line);
    }
    br.close();

    for (Map.Entry<Character, List<String>> entry : blockShapes.entrySet()) {
        char blockChar = entry.getKey();
        List<String> shapeLines = entry.getValue();
        blockType.add(blockChar);
        blocks.add(blockAlternatives(parseBlock(shapeLines, blockChar)));
    }
    if (blockType.size() != blockCount) {
        System.out.println("Error: Expected " + blockCount + " blocks, but found " + blockType.size());
        System.exit(1);
    }
}

    private char[][] parseBlock(List<String> shapeLines, char blockChar) {
    int minRow = Integer.MAX_VALUE, maxRow = Integer.MIN_VALUE;
    int minCol = Integer.MAX_VALUE, maxCol = Integer.MIN_VALUE;

    for (int r = 0; r < shapeLines.size(); r++) {
        String row = shapeLines.get(r);
        for (int c = 0; c < row.length(); c++) {
            if (row.charAt(c) == blockChar) {
                minRow = Math.min(minRow, r);
                maxRow = Math.max(maxRow, r);
                minCol = Math.min(minCol, c);
                maxCol = Math.max(maxCol, c);
            }
        }
    }

    if (minRow == Integer.MAX_VALUE) {
        System.out.println("Error: Block " + blockChar + " not found.");
        return new char[0][0];
    }
    int blockRows = maxRow - minRow + 1;
    int blockCols = maxCol - minCol + 1;
    char[][] block = new char[blockRows][blockCols];

    for (char[] row : block) Arrays.fill(row, '.');
    for (int r = minRow; r <= maxRow; r++) {
        String row = shapeLines.get(r);
        for (int c = minCol; c <= Math.min(maxCol, row.length() - 1); c++) {
            if (row.charAt(c) == blockChar) {
                block[r - minRow][c - minCol] = blockChar;
            }
        }
    }

    return block;
}

    private List<char[][]> blockAlternatives(char[][] block) {
        Set<String> seen = new HashSet<>();
        List<char[][]> alternatives = new ArrayList<>();
        
        String original = toString(block);
        seen.add(original);
        alternatives.add(copy(block));

        for (int i = 0; i < 3; i++) {
            block = rotate(block);
            String rotated = toString(block);
            if (!seen.contains(rotated)) {
                seen.add(rotated);
                alternatives.add(copy(block));
            }
            char[][] mirrored = mirror(block);
            String mirroredBlock = toString(mirrored);
            if (!seen.contains(mirroredBlock)) {
                seen.add(mirroredBlock);
                alternatives.add(copy(mirrored));
            }
        }
        return alternatives;
    }

    private char[][] rotate(char[][] block) {
        int r = block.length, c = block[0].length;
        char[][] rotated = new char[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                rotated[j][r - 1 - i] = block[i][j];
            }
        }
        return rotated;
    }

    private char[][] mirror(char[][] block) {
        int r = block.length, c = block[0].length;
        char[][] mirrored = new char[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                mirrored[i][c - 1 - j] = block[i][j];
            }
        }
        return mirrored;
    }

    private String toString(char[][] block) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : block) sb.append(new String(row)).append("|");
        return sb.toString();
    }

    private char[][] copy(char[][] block) {
        char[][] newBlock = new char[block.length][block[0].length];
        for (int i = 0; i < block.length; i++) {
            System.arraycopy(block[i], 0, newBlock[i], 0, block[i].length);
        }
        return newBlock;
    }
}
