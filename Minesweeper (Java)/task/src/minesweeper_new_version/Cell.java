package minesweeper_new_version;

public class Cell {
    private boolean hasMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int surroundingMineCount;

    public Cell() {
        this.hasMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.surroundingMineCount = 0;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public void placeMine() {
        this.hasMine = true;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void reveal() {
        this.isRevealed = true;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void flag() {
        this.isFlagged = true;
    }

    public void unflag() {
        this.isFlagged = false;
    }

    public int getSurroundingMineCount() {
        return surroundingMineCount;
    }

    public void setSurroundingMineCount(int count) {
        this.surroundingMineCount = count;
    }
}
