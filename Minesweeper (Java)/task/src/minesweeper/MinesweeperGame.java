package minesweeper;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.Set;

public class MinesweeperGame {
    private final UserInterface userInterface = new UserInterface();
    private final MineField field = new MineField();

    private final int heightField = 9;
    private final int widthField = 9;
    private int amountMines;

    enum UserCommand {
        FREE("free"),
        MINE("mine");

        String value;

        UserCommand(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        // Static method to get UserCommand by value
        public static UserCommand fromValue(String value) {
            for (UserCommand command : UserCommand.values()) {
                if (command.value.equals(value)) {
                    return command;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public void play() {
        createFieldProcess();
        userMoves();
        field.printMineField();
    }

    private void createFieldProcess() {
        userInterface.printEnterAmountMinePrompt();
        try {
            amountMines = Integer.parseInt(userInterface.getUserInput().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(userInterface.ERROR_WRONG_NUMBERS_IN_INPUT);
        }

        field.createEmptyField(heightField, widthField);
        field.printMineField();
    }

    private void userMoves() {
        Set<int[]> guessPositions = new TreeSet<>((a, b) -> {
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
        int countUserMoves = 0;

        do {
            userInterface.printSetMineOrMarkCellEmptyPrompt();
            String userInput = userInterface.getUserInput();
            String[] inputParts = userInput.trim().split("\\s+");
            if (inputParts.length != 3) {
                throw new IllegalArgumentException(userInterface.ERROR_WRONG_AMOUNT_NUMBERS_IN_INPUT);
            }
            int[] userCoordinates = parseUserCoordinates(inputParts);
            UserCommand userCommand = parseUserCommand(inputParts);

            if (countUserMoves == 0 && userCommand == UserCommand.FREE) {
                field.fillFirst9Cells(userCoordinates);
                field.setMines(amountMines);
                field.mineAroundCells();
                ++countUserMoves;
            }

            if (field.isThereNumber(userCoordinates)) {
                System.out.println(Arrays.toString(userCoordinates));
                userInterface.printThereNumberMessage();
            } else if (field.isThereGuess(userCoordinates)) {
                if (userCommand == UserCommand.MINE && guessPositions.remove(userCoordinates)) {
                    field.deleteGuessFromCell(userCoordinates);
                    field.printMineField();
                }
            } else if(field.isThereMine(userCoordinates) && userCommand == UserCommand.FREE){
                field.printAllMines();
                userInterface.printYouFailedMessage();
                return;
            } else {
//                field.setMineToCell(userCoordinates);
                field.exploreCell(userCoordinates, userCommand);
                if(userCommand == UserCommand.MINE){
                    guessPositions.add(userCoordinates);
                }
                field.printMineField();
            }

            System.out.println("guessPositions: ");
            for(int[] userCoordinate : guessPositions){
                System.out.print(Arrays.toString(userCoordinate) + ", ");
            }
            System.out.println();

            field.printMinePositions();
            field.printCellsToPrintPositions();

        } while (!field.areAllMinesFound(guessPositions));
        userInterface.printCongratulationMessage();

    }

    private int[] parseUserCoordinates(String[] inputParts) {
        int[] userCoordinates;
        try {
            userCoordinates = new int[]{
                    Integer.parseInt(inputParts[1]) - 1,
                    Integer.parseInt(inputParts[0]) - 1};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(userInterface.ERROR_WRONG_NUMBERS_IN_INPUT);
        }

        if (userCoordinates[0] < 0 || userCoordinates[0] > field.getHeight() - 1 ||
                userCoordinates[1] < 0 || userCoordinates[1] > field.getWidth() - 1) {
            throw new IllegalArgumentException(userInterface.ERROR_WRONG_NUMBERS_IN_INPUT);
        }

        return userCoordinates;
    }

    private UserCommand parseUserCommand(String[] inputParts) {
        UserCommand currCommand = UserCommand.fromValue(inputParts[2]);
        if (currCommand == null) {
            throw new IllegalArgumentException(userInterface.ERROR_WRONG_USER_COMMAND_INPUT);
        }
        return currCommand;
    }
}
