package com.emmanuelmolefi.tictactoe;

public class Player {

    private Token marker;
    private boolean isHuman;

    public Player() {
        this.marker = Token.CROSS;
        this.isHuman = true;
    }

    public Player(boolean isHuman) {
        this.isHuman = isHuman;
        this.marker = isHuman ? Token.CROSS : Token.NOUGHT;
    }

    public Player(Token marker, boolean isHuman) {
        this.marker = marker;
        this.isHuman = isHuman;
    }

    public Token getMarker() {
        return marker;
    }

    public boolean isHuman() {
        return isHuman;
    }
}
