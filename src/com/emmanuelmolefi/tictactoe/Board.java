package com.emmanuelmolefi.tictactoe;

import java.util.*;

public class Board {

    public static final int NUM_TILES = 9;
    public static final int[][] WINNING_MOVES = {
            {0 , 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6} };

    private final Map<Integer, Token> boardConfig = new HashMap<>();
    private BoardStatus boardStatus;

    public Board() {
        createBoard();
        this.boardStatus = BoardStatus.PLAYING;
    }

    private void createBoard() {
        for (int i = 0; i < 9; i++) {
            this.boardConfig.put(i, Token.EMPTY);
        }
    }

    public void submitMove(final int index, final Token playerToken) {
        this.boardConfig.put(index, playerToken);
    }

    public Map<Integer, Token> getBoardConfig() {
        return boardConfig;
    }

    public BoardStatus getBoardStatus() {
        return boardStatus;
    }

    public void setBoardStatus(BoardStatus boardStatus) {
        this.boardStatus = boardStatus;
    }

    public boolean isMoveLegal(final int index) {
        if (index > -1 && index < 9)
            return Token.EMPTY.equals(this.boardConfig.get(index));
        return false;
    }

    public boolean isDraw() {
        return !this.boardConfig.containsValue(Token.EMPTY);
    }

    public boolean isDraw(final List<Token> boardConfig) {
        return !boardConfig.contains(Token.EMPTY);
    }

    public int generateComputerMove(final Map<Integer, Token> boardConfig, final Token playerToken) {
        int bestMove = -1;
        int bestVal = Integer.MIN_VALUE;

        List<Token> values = new ArrayList<>(boardConfig.values());
        // Traverse all cells, evaluate minimax function for all empty cells.
        // And return the cell with optimal value.
        for (int i = 0; i < values.size(); i++ ) {
            if (Token.EMPTY.toString().equals( values.get(i).toString() )) {
                // make the move
                values.set(i, playerToken);
                // compute evaluation function for this move
                int moveVal = minimax(values, 0, false);
                // undo the move
                values.set(i, Token.EMPTY);
                
                // if the value of the current move is more than the best value,
                // then update best
                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }
        return bestMove;
    }

    private int minimax(List<Token> board, int depth, boolean maximizingPlayer) {
        int score = evaluate(board);

        if (score == 10000) return score - depth;
        if (score == -10000) return score + depth;
        if (isDraw(board)) return 0;

        int best;

        if (maximizingPlayer) {
            best = Integer.MIN_VALUE;

            // traverse all cells
            for (int i = 0; i < board.size(); i++) {
                if (Token.EMPTY.toString().equals( board.get(i).toString() )) {
                    // make the move
                    board.set(i, Token.NOUGHT); // review this
                    // compute evaluation function for this move
                    best = Math.max(best, minimax(board, depth + 1, false));
                    // undo the move
                    board.set(i, Token.EMPTY);
                }
            }
        } else {
            best = Integer.MAX_VALUE;

            // traverse all cells
            for (int i = 0; i < board.size(); i++) {
                if (Token.EMPTY.toString().equals( board.get(i).toString() )) {
                    // make the move
                    board.set(i ,Token.CROSS);
                    // compute evaluation function for this move
                    best = Math.min(best, minimax(board, depth + 1, true));
                    // undo the move
                    board.set(i, Token.EMPTY);
                }
            }
        }
        return best;
    }

    private int evaluate(List<Token> board) {
        Map<Integer, Token> boardConfig = new HashMap<>();
        for (int i = 0; i < board.size(); i++) {
            boardConfig.put(i, board.get(i));
        }
        Token playerToken = Token.CROSS;
        if (isWin(playerToken, boardConfig)) {
            return -10000;
        }
        playerToken = Token.NOUGHT;
        if (isWin(playerToken, boardConfig)) {
            return 10000;
        }
        return 0;
    }

    public boolean isWin(final Token playerToken, final Map<Integer, Token> boardConfig) {
        List<Integer> plays = new ArrayList<>();
        for (Map.Entry<Integer, Token> entry : boardConfig.entrySet()) {
            if (playerToken.toString().equals(entry.getValue().toString())) {
                plays.add(entry.getKey());
            }
        }
        for (int[] winningMove : WINNING_MOVES) {
            if (plays.contains(winningMove[0])
                    && plays.contains(winningMove[1])
                    && plays.contains(winningMove[2]) ) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    private int generateComputerMoveAsNextAvailablePosition(final Map<Integer, Token> boardConfig) {
        for (Map.Entry<Integer, Token> entry : boardConfig.entrySet()) {
            if (Token.EMPTY.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }
}
