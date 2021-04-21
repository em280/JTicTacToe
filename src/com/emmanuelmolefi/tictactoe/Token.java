package com.emmanuelmolefi.tictactoe;

public enum Token {
    CROSS {
        @Override
        public String toString() {
            return "X";
        }
    },
    NOUGHT {
        @Override
        public String toString() {
            return "O";
        }
    },
    EMPTY {
        @Override
        public String toString() {
            return " ";
        }
    }
}
