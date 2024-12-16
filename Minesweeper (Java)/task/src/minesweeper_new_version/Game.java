package minesweeper_new_version;

public class Game {
    private GameBoard gameBoard;
    private final UserInterface userInterface = new UserInterface();
    private int amountMines;


    public Game() {
    }

    public void startGame() {
        createAndDisplayGameBoard();
        processUserMoves();
        userInterface.exit();
    }

    private void createAndDisplayGameBoard() {
//        userInterface.printEnterWidthPrompt();
//        int width = parseString(userInterface.getUserInput());
//        userInterface.printEnterHeightPrompt();
//        int height = parseString(userInterface.getUserInput());
        int defaultWidth = 9;
        int defaultHeight = 9;

        userInterface.printEnterAmountMinePrompt();
        amountMines = parseString(userInterface.getUserInput());

        gameBoard = new GameBoard(defaultWidth, defaultHeight);
        gameBoard.printGameBoard();
    }

    private int parseString(String userInput) {
        int value = 0;
        try {
            value = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            userInterface.printErrorWrongIntegersInInput();
        }
        return value;
    }

    private void processUserMoves() {
        int countUserMoves = 0;
        do {
            userInterface.printSetMineOrMarkCellEmptyPrompt();
            String userInput = userInterface.getUserInput();
            String[] inputParts = userInput.trim().split("\\s+");

            // Validate user input
            if (inputParts.length != 3) {
                userInterface.printErrorWrongAmountNumbersInInput();
                continue;
            }

            int[] userCoordinates;
            try {
                userCoordinates = parseUserCoordinates(inputParts);
            } catch (IllegalArgumentException e) {
                userInterface.printErrorWrongIntegersInInput();
                continue;
            }

            UserCommand userCommand;
            try {
                userCommand = parseUserCommand(inputParts);
            } catch (IllegalArgumentException e) {
                userInterface.printErrorWrongUserCommandInput();
                continue;
            }

            // Process user command
            if (userCommand == UserCommand.FREE) {
                // Handle first move and ensure no mines are in the starting position
                if (countUserMoves == 0) {
                    try {
                        gameBoard.fillGameBoard(userCoordinates, amountMines);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    ++countUserMoves;
                }

                if (gameBoard.isRevealedCell(userCoordinates)) {
                    userInterface.printThereNumberMessage();
                } else if (gameBoard.hasMineCell(userCoordinates)) {
                    gameBoard.revealAllMines();
                    gameBoard.printGameBoard();
                    userInterface.printYouFailedMessage();
                    return;
                } else {
                    gameBoard.revealCell(userCoordinates);
                }

            } else if (userCommand == UserCommand.MINE) {
                if (gameBoard.isFlaggedCell(userCoordinates)) {
                    gameBoard.unsetFlagCell(userCoordinates);
                } else {
                    gameBoard.setFlagToCell(userCoordinates);
                }
            }

            // Print the updated game board after each move
            gameBoard.printGameBoard();
        } while (!gameBoard.areAllCellsRevealed());
        userInterface.printCongratulationMessage();
    }

    private int[] parseUserCoordinates(String[] inputParts) {
        int[] userCoordinates = new int[]{0, 0};
        try {
            userCoordinates = new int[]{
                    Integer.parseInt(inputParts[1]) - 1,
                    Integer.parseInt(inputParts[0]) - 1};
        } catch (NumberFormatException e) {
            userInterface.printErrorWrongIntegersInInput();
        }

        if (userCoordinates[0] < 0 || userCoordinates[0] > gameBoard.getWidth() - 1 ||
                userCoordinates[1] < 0 || userCoordinates[1] > gameBoard.getHeight() - 1) {
            userInterface.printErrorWrongIntegersInInput();
        }

        return userCoordinates;
    }

    private UserCommand parseUserCommand(String[] inputParts) {
        UserCommand currUserCommand = UserCommand.fromValue(inputParts[2]);
        if (currUserCommand == null) {
            userInterface.printErrorUnknownValue();
        }
        return currUserCommand;
    }
}
