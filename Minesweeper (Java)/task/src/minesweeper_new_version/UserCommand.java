package minesweeper_new_version;

public enum UserCommand {
    FREE("free"),
    MINE("mine");

    final String value;

    UserCommand(String value) {
        this.value = value;
    }

    // Static method to get UserCommand by value
    public static UserCommand fromValue(String value) {
        for (UserCommand command : UserCommand.values()) {
            if (command.value.equals(value)) {
                return command;
            }
        }
        return null;
    }
}
