package minesweeper_new_version;

import java.util.Scanner;

public class UserInterface {
    Scanner scanner = new Scanner(System.in);

    private final String ENTER_HEIGHT_FIELD_PROMPT = "Enter the height of the mine field.";
    private final String ENTER_WIDTH_FIELD_PROMPT = "Enter the width of the mine field.";
    private final String ENTER_AMOUNT_MINE_PROMPT = "How many mines do you want on the field? > ";
    private final String SET_MINE_OR_MARK_CELL_EMPTY_PROMPT = "Set/unset mines marks or claim a cell as free: > ";
    private final String THERE_NUMBER_MESSAGE = "There is a number here!";
    private final String YOU_FAILED_MESSAGE = "You stepped on a mine and failed!";
    private final String CONGRATULATION_MESSAGE = "Congratulations! You found all the mines!";
    public final String ERROR_WRONG_AMOUNT_NUMBERS_IN_INPUT = "Input must contain exactly two numbers.";
    public final String ERROR_WRONG_INTEGERS_IN_INPUT = "Input must contain valid integers.";
    public final String ERROR_WRONG_USER_COMMAND_INPUT = "Wrong input. You can input free to explore a cell " +
            "or mine to mark or unmark a cell.";
    public final String ERROR_UNKNOWN_VALUE = "Unknown value.";

    public UserInterface(){}

    public void exit()
    {
        scanner.close();
    }

    public String getUserInput(){
        return scanner.nextLine();
    }

    public void printEnterHeightPrompt(){
        System.out.print(ENTER_HEIGHT_FIELD_PROMPT);
    }

    public void printEnterWidthPrompt(){
        System.out.print(ENTER_WIDTH_FIELD_PROMPT);
    }

    public void printEnterAmountMinePrompt(){
        System.out.print(ENTER_AMOUNT_MINE_PROMPT);
    }

    public void printSetMineOrMarkCellEmptyPrompt(){
        System.out.print(SET_MINE_OR_MARK_CELL_EMPTY_PROMPT);
    }

    public void printThereNumberMessage(){
        System.out.println(THERE_NUMBER_MESSAGE);
    }

    public void printCongratulationMessage(){
        System.out.println(CONGRATULATION_MESSAGE);
    }

    public void printYouFailedMessage(){
        System.out.println(YOU_FAILED_MESSAGE);
    }

    public void printErrorWrongIntegersInInput(){
        System.out.println(ERROR_WRONG_INTEGERS_IN_INPUT);
    }

    public void printErrorWrongAmountNumbersInInput(){
        System.out.println(ERROR_WRONG_AMOUNT_NUMBERS_IN_INPUT);
    }

    public void printErrorUnknownValue(){
        System.out.println(ERROR_UNKNOWN_VALUE);
    }
}
