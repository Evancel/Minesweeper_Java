package minesweeper;

import java.util.*;

public class MineField {
    private int height;
    private int width;
    private int amountMines;
    private static final char EMPTY = '.';
    private static final char MINE = 'X';
    private static final char GUESS = '*';
    private static final char FREE = '/';
    private char[][] field;
    private final Set<int[]> cellsToPrint = new TreeSet<>((a, b) -> {
        if (a.length != b.length) { // Compare by length if different
            return Integer.compare(a.length, b.length);
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return Integer.compare(a[i], b[i]); // Compare elements
            }
        }
        return 0; // Arrays are equal
    });
    // TreeSet with a custom comparator for int[] based on array contents
    private final Set<int[]> minesPosition = new TreeSet<>((a, b) -> {
        if (a.length != b.length) { // Compare by length if different
            return Integer.compare(a.length, b.length);
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return Integer.compare(a[i], b[i]); // Compare elements
            }
        }
        return 0; // Arrays are equal
    });

    Set<int[]> emptyCellAndAroundCells = new TreeSet<>((a, b) -> {
        if (a.length != b.length) {
            return Integer.compare(a.length, b.length);
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return Integer.compare(a[i], b[i]);
            }
        }
        return 0;
    });

    public MineField() {
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getAmountMines() {
        return this.amountMines;
    }

    public void createEmptyField(int height, int width) {
        this.height = height;
        this.width = width;

        field = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = EMPTY;
            }
        }
    }

    public void printMineField() {
//        System.out.println("Debugging printMineField");
//        for(int[] userCoordinate : cellsToPrint){
//            System.out.print(Arrays.toString(userCoordinate));
//        }

        System.out.println();
        //print the first line
        System.out.print(" |");
        for (int i = 1; i <= field.length; i++) {
            System.out.print(i);
        }
        System.out.println("|");

        //print the second line
        System.out.print("-|");
        for (int i = 1; i <= field.length; i++) {
            System.out.print("-");
        }
        System.out.println("|");

        //print the grid
        for (int i = 0; i < field.length; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < field[i].length; j++) {
                if (cellsToPrint.contains(new int[]{i, j})) {
                    System.out.print(field[i][j]);
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("|");
        }

        //print the last line
        System.out.print("-|");
        for (int i = 1; i <= field.length; i++) {
            System.out.print("-");
        }
        System.out.println("|");
    }

    public void setMines(int amountMines) {
        validateMineCount(amountMines);

        // Generate a shuffled list of all cell coordinates
        List<int[]> cellCoordinates = getShuffledCoordinates();

        // Place mines on the field
        for (int i = 0; i < amountMines; i++) {
            int[] coordinates = cellCoordinates.get(i);
//            System.out.println("setting Mines: row = " + coordinates[0] + ", col = " + coordinates[1]);
            if (field[coordinates[0]][coordinates[1]] != GUESS) {
                field[coordinates[0]][coordinates[1]] = MINE;
            }

            minesPosition.add(new int[]{coordinates[0], coordinates[1]});
        }

        this.amountMines = amountMines;
    }

    private void validateMineCount(int amountMines) {
        int totalCells = field.length * field[0].length;
        if (amountMines <= 0 || amountMines > totalCells) {
            throw new IllegalArgumentException("Invalid number of mines: " + amountMines);
        }
    }

    private List<int[]> getShuffledCoordinates() {
        List<int[]> coordinates = new ArrayList<>();
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[0].length; y++) {
                int[] coordinate = new int[]{x, y};
                if (!emptyCellAndAroundCells.contains(coordinate)) {
                    coordinates.add(coordinate);
                }
            }
        }

        Collections.shuffle(coordinates, new Random());
        return coordinates;
    }


    public void mineAroundCells() {
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                if (!minesPosition.contains(new int[]{row, col})) {
                    int amountMinesAround = countMinesAround(row, col);
                    if (amountMinesAround > 0) {
                        field[row][col] = (char) (amountMinesAround + '0');
                    }
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
                if (isValidCell(newRow, newCol) && minesPosition.contains(new int[]{newRow, newCol})) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < field.length && col >= 0 && col < field[0].length;
    }

    public boolean isThereMine(int[] userCoordinates) {
        return minesPosition.contains(userCoordinates);
    }

    public boolean isThereNumber(int[] userCoordinates) {
        return Character.toString(field[userCoordinates[0]][userCoordinates[1]]).matches("\\d")
                && cellsToPrint.contains(userCoordinates);
    }

    public boolean isThereGuess(int[] userCoordinates) {
        return field[userCoordinates[0]][userCoordinates[1]] == GUESS;
    }

    public boolean isThereEmpty(int[] userCoordinates) {
        return field[userCoordinates[0]][userCoordinates[1]] == EMPTY;
    }

    public void setMineToCell(int[] userCoordinates) {
        field[userCoordinates[0]][userCoordinates[1]] = GUESS;
    }

    public void setFreeToCell(int[] userCoordinates) {
        field[userCoordinates[0]][userCoordinates[1]] = FREE;
    }

    public void deleteGuessFromCell(int[] userCoordinates) {
        if (cellsToPrint.remove(userCoordinates)) {
            int row = userCoordinates[0];
            int col = userCoordinates[1];
            if (minesPosition.contains(userCoordinates)) {
                field[row][col] = MINE;
            } else {
                int amountMinesAround = countMinesAround(row, col);
                if (amountMinesAround > 0) {
                    field[row][col] = (char) (amountMinesAround + '0');
                } else {
                    field[row][col] = EMPTY;
                }
            }
        }
        System.out.println(field[userCoordinates[0]][userCoordinates[1]]);
    }

    public boolean areAllMinesFound(Set<int[]> guessCounter) {
        if (guessCounter.size() != minesPosition.size()) {
            return false;
        }

        return minesPosition.containsAll(guessCounter) ||
                (cellsToPrint.size() + minesPosition.size()) == height * width;
    }

    public void fillFirst9Cells(int[] userCoordinates) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = userCoordinates[0] + i;
                int newCol = userCoordinates[1] + j;
                if (isValidCell(newRow, newCol)) {
//                    System.out.println("fillFirst9Cells: row = " + newRow + ", col = " + newCol);
                    emptyCellAndAroundCells.add(new int[]{newRow, newCol});
                }
            }
        }
    }

    public void exploreCell(int[] userCoordinates, MinesweeperGame.UserCommand userCommand) {
        if (userCommand == MinesweeperGame.UserCommand.FREE) {
            if (field[userCoordinates[0]][userCoordinates[1]] == EMPTY) {
                openCells(userCoordinates);
            } else if (Character.toString(field[userCoordinates[0]][userCoordinates[1]]).matches("\\d")) {
                cellsToPrint.add(userCoordinates);
            }

        } else {
            field[userCoordinates[0]][userCoordinates[1]] = GUESS;
            cellsToPrint.add(userCoordinates);
        }
    }

    private void openCells(int[] userCoordinates) {
//        System.out.println("Debugging openCells: userCoordinates[0] = " + userCoordinates[0] +
//                ", userCoordinates[1] = " + userCoordinates[1]);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = userCoordinates[0] + i;
                int newCol = userCoordinates[1] + j;
//                System.out.println("newRow = " + newRow + ", newCol = " + newCol);
                if (isValidCell(newRow, newCol)) {
                    //если ячейка пустая - открываем все возможные ячейки вокруг нее
                    //если ячейка = цифра - добавляем в сет для печати
                    //если ячейка = guess - анализируем исходное значение ячейки:
                    //                      если пустая - присваиваем значение / и открываем ячейки вокруг нее
                    //                      если цифра - добавляем в сет для печати

                    if (field[newRow][newCol] == EMPTY) {
                        field[newRow][newCol] = FREE;
                        openCells(new int[]{newRow, newCol});
                    }else if(field[newRow][newCol] == GUESS){
                        deleteGuessFromCell(new int[]{newRow, newCol});
                        openCells(new int[]{newRow, newCol});
                    }

                    if(!minesPosition.contains(new int[]{newRow, newCol})) {
                        cellsToPrint.add(new int[]{newRow, newCol});
                    }
                }
            }
        }
    }

    public void printAllMines() {
        cellsToPrint.addAll(minesPosition);
    }

    public void printMinePositions() {
        System.out.println("minePositions: ");
        for (int[] userCoordinate : minesPosition) {
            System.out.print(Arrays.toString(userCoordinate) + ", ");
        }
        System.out.println();
    }

    public void printCellsToPrintPositions() {
        System.out.println("cellsToPrint: ");
        for (int[] userCoordinate : cellsToPrint) {
            System.out.print(Arrays.toString(userCoordinate) + ", ");
        }
        System.out.println();
    }
}
