package Source;

import java.util.*;

public class Solver {
    private int rows, cols, blockCount;
    private static char[][] board;
    private List<List<char[][]>> blocks;
    private List<Character> blockType;
    private static int attempt = 0;
    private static boolean found = false;

    public Solver(Input input) {
        this.rows = input.rows;
        this.cols = input.cols;
        this.blockCount = input.blockCount;
        this.blocks = input.blocks;
        this.blockType = input.blockType;
        this.board = new char[rows][cols];
        for (char[] row : board) Arrays.fill(row, '.');
    }

    public static char[][] getBoard() {
        return board;
    }

    public void solve() {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < blockCount; i++) order.add(i);
        List<List<Integer>> permutations = generatePermutations(order);
        for (List<Integer> perm : permutations) {
            if (placeBlock(perm)&&isBoardFull()) {
                found = true;
                return;
            }
        }
        System.out.println("Tidak ada solusi yang ditemukan.");
    }

    private boolean placeBlock(List<Integer> order) {
        char[][] tempBoard = new char[rows][cols];
        for (char[] row : tempBoard) Arrays.fill(row, '.');
        for (int index = 0; index < blockCount; index++) {
            boolean placed = false;
            char label = blockType.get(order.get(index));
            for (char[][] b : blocks.get(order.get(index))) {
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        attempt++;
                        if (canPlace(tempBoard, b, row, col)) {
                            setBlock(tempBoard, b, row, col, label);
                            placed = true;
                            break; 
                        }
                    }
                    if (placed) break;
                }
                if (placed) break;
            }
            if (!placed) return false;
        }
        board = tempBoard;
        return true;
    }

    private boolean canPlace(char[][] tempBoard, char[][] block, int row, int col) {
        int br = block.length;
        int bc = block[0].length;
        if (row + br > rows || col + bc > cols) return false;
        for (int i = 0; i < br; i++) {
            for (int j = 0; j < bc; j++) {
                if (block[i][j] != '.' && tempBoard[row + i][col + j] != '.') return false;
            }
        }
        return true;
    }

    private void setBlock(char[][] tempBoard, char[][] block, int row, int col, char character) {
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != '.') {
                    tempBoard[row + i][col + j] = character;
                }
            }
        }
    }

    private List<List<Integer>> generatePermutations(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        permute(list, 0, result);
        return result;
    }

    private void permute(List<Integer> arr, int l, List<List<Integer>> result) {
        if (l == arr.size()) {
            result.add(new ArrayList<>(arr));
            return;
        }
        for (int i = l; i < arr.size(); i++) {
            Collections.swap(arr, l, i);
            permute(arr, l + 1, result);
            Collections.swap(arr, l, i);
        }
    }
    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '.') {
                    return false; 
                }
            }
        }
        return true;
    }

    public static boolean isSolutionFound() {
        return found;
    }

    public static String getSolution() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            sb.append(new String(row)).append("\n");
        }
        return sb.toString();
    }

    public static int getAttempt() {
        return attempt;
    }
    public static void setAttempt(int value) {
        attempt = value;
    }
    public static void setFound(boolean value) {
    	found = value;
    }
    

}
