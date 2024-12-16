package minesweeper_new_version;

import java.util.*;

public class GameBoard {
    private final int width;
    private final int height;
    private final Cell[][] board;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new Cell[width][height];

        initializeBoard();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    private void initializeBoard() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board[x][y] = new Cell();
            }
        }
    }

    public void printGameBoard() {
        System.out.println();
        //print the first line
        System.out.print(" |");
        for (int i = 1; i <= width; i++) {
            System.out.print(i);
        }
        System.out.println("|");

        //print the second line
        System.out.print("-|");
        for (int i = 1; i <= width; i++) {
            System.out.print("-");
        }
        System.out.println("|");

        //print the grid
        for (int i = 0; i < width; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < height; j++) {
                if (board[i][j].isRevealed()) {
                    if(board[i][j].hasMine()){
                        System.out.print("X");
                    }else if(board[i][j].getSurroundingMineCount() == 0){
                        System.out.print("/");
                    }else if(board[i][j].getSurroundingMineCount() > 0){
                        System.out.print(board[i][j].getSurroundingMineCount());
                    }
                }else {
                    if (board[i][j].isFlagged()){
                        System.out.print("*");
                    }else{
                        System.out.print(".");
                    }
                }
            }
            System.out.println("|");
        }

        //print the last line
        System.out.print("-|");
        for (int i = 1; i <= width; i++) {
            System.out.print("-");
        }
        System.out.println("|");
    }

    public void fillGameBoard(int[] userCoordinates, int amountMines) {
        //the first coordinate = userCoordinates
        setMines(userCoordinates, amountMines);
        setAdjacentMines();
    }

    public boolean hasMineCell(int[] userCoordinates) {
        return board[userCoordinates[0]][userCoordinates[1]].hasMine();
    }

    public boolean isRevealedCell(int[] userCoordinates) {
        return board[userCoordinates[0]][userCoordinates[1]].isRevealed();
    }

    public void revealCell(int[] userCoordinates) {
        int row = userCoordinates[0];
        int col = userCoordinates[1];

        // Validate coordinates
        if (!isValidCell(row, col)) {
            return;
        }

        Cell cell = board[row][col];

        // Do nothing if the cell is already revealed
        if (cell.isRevealed()){
            return;
        }

        // Do nothing if the cell is already flagged and had mine
        if (cell.isFlagged() && cell.hasMine()) {
            return;
        }

        // Reveal the current cell
        cell.reveal();

        // If the cell has no surrounding mines, recursively reveal its neighbors
        if (cell.getSurroundingMineCount() == 0 && !cell.hasMine()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) { // Skip the current cell
                        int newRow = row + i;
                        int newCol = col + j;
                        revealCell(new int[]{newRow, newCol});
                    }
                }
            }
        }
    }

    public boolean isFlaggedCell(int[] userCoordinates) {
        return board[userCoordinates[0]][userCoordinates[1]].isFlagged();
    }

    public void setFlagToCell(int[] userCoordinates) {
        board[userCoordinates[0]][userCoordinates[1]].flag();
    }

    public void unsetFlagCell(int[] userCoordinates) {
        board[userCoordinates[0]][userCoordinates[1]].unflag();
    }

    public void revealAllMines() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j].hasMine()) {
                    board[i][j].reveal();
                }
            }
        }
    }

    public boolean areAllCellsRevealed(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!board[i][j].isRevealed() && !board[i][j].hasMine()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setMines(int[] userCoordinates, int amountMines) {
        validateMineCount(amountMines);

        // Generate a shuffled list of all cell coordinatesi
        List<int[]> cellCoordinates = getShuffledCoordinates(userCoordinates);

        // Place mines on the field
        for (int i = 0; i < amountMines; ) {
            int[] coordinates = cellCoordinates.get(i);
            if (!Arrays.equals(userCoordinates, coordinates)) {
                board[coordinates[0]][coordinates[1]].placeMine();
                i++;
            }
        }
    }

    private void validateMineCount(int amountMines) {
        int totalCells = width * height;
        if (amountMines <= 0 || amountMines > totalCells) {
            throw new IllegalArgumentException("Invalid number of mines: " + amountMines);
        }
    }

    private List<int[]> getShuffledCoordinates(int[] userCoordinates) {
        List<int[]> coordinates = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] coordinate = new int[]{x, y};
                if (!Arrays.equals(userCoordinates, coordinate)) {
                    coordinates.add(coordinate);
                }
            }
        }

        Collections.shuffle(coordinates, new Random());
        return coordinates;
    }


    private void setAdjacentMines() {
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                if (!board[row][col].hasMine()) {
                    board[row][col].setSurroundingMineCount(countMinesAround(row, col));
                }
            }
        }
    }

    private int countMinesAround(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (isValidCell(newRow, newCol) && board[newRow][newCol].hasMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < width && col >= 0 && col < height;
    }
}
