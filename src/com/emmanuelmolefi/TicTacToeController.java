package com.emmanuelmolefi;

import com.emmanuelmolefi.tictactoe.Board;
import com.emmanuelmolefi.tictactoe.BoardStatus;
import com.emmanuelmolefi.tictactoe.Player;
import com.emmanuelmolefi.tictactoe.Token;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class TicTacToeController {

    @FXML
    private Label statusLabel;

    private Board tictactoeBoard;
    private Player crossPlayer;
    private Player noughtPlayer;
    private Player currentPlayer;

    private String gameOverMessage = "";

    private GridPane parent; // cache of parent

    public TicTacToeController() {
        initGame();
    }

    private void initGame() {
        this.tictactoeBoard = new Board();
        this.crossPlayer = new Player();
        this.noughtPlayer = new Player(false);
        this.currentPlayer = crossPlayer;
    }

    @FXML
    private void handleTileButtonAction(ActionEvent event) {

        if (!BoardStatus.PLAYING.equals(tictactoeBoard.getBoardStatus())) return;

        Button source = (Button) event.getSource();
        parent = (GridPane) source.getParent();
        ObservableList<Node> children = parent.getChildren();

        if (tictactoeBoard.isMoveLegal(children.indexOf(source)))
            tictactoeBoard.submitMove(children.indexOf(source), currentPlayer.getMarker());
        else return;

        gameUpdate(children);

        // let AI player
        if (!currentPlayer.isHuman() && BoardStatus.PLAYING.equals(tictactoeBoard.getBoardStatus())) {
            int cpuMove = tictactoeBoard.generateComputerMove(tictactoeBoard.getBoardConfig(), currentPlayer.getMarker());

            if (tictactoeBoard.isMoveLegal(cpuMove))
                tictactoeBoard.submitMove(cpuMove, currentPlayer.getMarker());
            else return;

            gameUpdate(children);
        }

        // update status label
        statusLabel.setText(gameOverMessage);
    }

    @FXML
    public void handleGameClose(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void handleAbout(ActionEvent event) {

    }

    @FXML
    public void handleNewGame(ActionEvent event) {
        initGame();

        Button tile;
        for (Node child : parent.getChildren()) {
            tile = (Button) child;
            tile.setText(Token.EMPTY.toString());
        }

        statusLabel.setText("New Game! You can start playing now.");
    }

    private void gameUpdate(ObservableList<Node> children) {
        Button tile;

        // updating UI
        for (int i = 0; i < children.size(); i++) {
            Token token = tictactoeBoard.getBoardConfig().get(i);
            tile = (Button) children.get(i);

            if (Token.EMPTY.toString().equals(token.toString())) {
                // set " "
                tile.setText(token.toString());
            } else if (Token.CROSS.toString().equals(token.toString())) {
                // set X
                tile.setText(token.toString());
            } else if (Token.NOUGHT.toString().equals(token.toString())) {
                // set O
                tile.setText(token.toString());
            }
        }

        // update game status
        if (tictactoeBoard.isWin(currentPlayer.getMarker(), tictactoeBoard.getBoardConfig())) {
            // then game over
            tictactoeBoard.setBoardStatus(
                    Token.CROSS.equals(currentPlayer.getMarker()) ?
                            BoardStatus.CROSS_WIN : BoardStatus.NOUGHT_WIN );
            gameOverMessage = currentPlayer.getMarker().toString() + " has won!!";
        }
        else if (tictactoeBoard.isDraw()) {
            tictactoeBoard.setBoardStatus(BoardStatus.DRAW);
            gameOverMessage = "Its a DRAW!";
        }

        if (!BoardStatus.PLAYING.equals(tictactoeBoard.getBoardStatus())) {
            // update status label
            statusLabel.setText(gameOverMessage);
        }

        // switch player
        currentPlayer = crossPlayer.equals(currentPlayer) ? noughtPlayer : crossPlayer;
    }
}
