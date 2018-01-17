/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversifx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author flug
 */
public class BoardController extends GridPane {

    private Board board;
    private Player firstPlayer;
    private Player secondPlayer;
    private List<Circle> pieces;

    public BoardController(Board b, GameController gc, String playsFirst,
            String playerOneColor, String playerTwoColor) {

        this.board = b;

        pieces = new ArrayList();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "Board.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        // see who goes first
        if ("player 1".equals(playsFirst)) {
            // if player 1 goes first - set him as the first player and player 2
            // as the second player
            firstPlayer = new Player('O', playerOneColor, "player 1");
            secondPlayer = new Player('X', playerTwoColor, "player 2");
        } else {
            // if player 2 goes first - set him as the first player and player 1
            // as the second player
            firstPlayer = new Player('O', playerTwoColor, "player 2");
            secondPlayer = new Player('X', playerOneColor, "player 1");
        }

        // create a new gameLogic - this will run the game for us
        GameLogic gameLogic = new GameLogic(board, firstPlayer, secondPlayer);

        // send gameController the info it needs - who goes first and what the
        // current score is. game starts at 2 - 2
        gc.updateStats(playsFirst, 2, 2);
        try {
            fxmlLoader.load();
            this.setOnMousePressed(event -> {
                // if the moust was pressed
                int playerX = (int) event.getY();
                int playerY = (int) event.getX();
                int elementSize = (int) this.getWidth()
                        / this.board.getSize();
                // set the players move - the row is the x position of his click
                // divided by the size of the board, and the col is the y
                // position of his click divided by the size of the board
                // (the board is sqaured)
                Element move = new Element(playerX / elementSize, playerY / elementSize);
                if (gameLogic.playOneTurn(move)) {
                    // redraw if playOneTurn returned true, meaning a legal move
                    // was input by the user
                    redraw();
                }

                int firstPlayerScore = firstPlayer.getNumberOfPieces();
                int secondPlayerScore = secondPlayer.getNumberOfPieces();

                String currentPlayer = gameLogic.getCurrentPlayer();
                if ("player 1".equals(firstPlayer.getName())) {
                    // if player 1 is the first player
                    gc.updateStats(currentPlayer, firstPlayerScore,
                            secondPlayerScore);
                } else {
                    // if player 2 is the first player
                    gc.updateStats(currentPlayer, secondPlayerScore,
                            firstPlayerScore);
                }
                if (gameLogic.gameOver()) {
                    // if the game is over - throw a new alert at the user
                    // notifying him who won and telling him to press OK to
                    // return to the main menue
                    Alert alert = new Alert(AlertType.NONE,
                            gameLogic.declareWinner() + "\n" + "press \"OK\" "
                            + "to return to the main menu",
                            ButtonType.OK);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        try {
                            // if the player pressed OK - return to the menu
                            Parent root = FXMLLoader.load(getClass().
                                    getResource("Menu.fxml"));
                            Scene nextScene = new Scene(root, 800, 600);
                            Stage stage = (Stage) ((Node) event.getSource()).
                                    getScene().getWindow();
                            stage.setScene(nextScene);
                            stage.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void draw() {
        // clear the GridPane
        this.getChildren().clear();

        int height = (int) this.getPrefHeight();
        int width = (int) this.getPrefWidth();

        char[][] b = board.getBoard();

        int elementHeight = height / board.getSize();
        int elementWidth = width / board.getSize();

        // draw the board
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Rectangle rect = new Rectangle(elementWidth, elementHeight, Paint.valueOf("00bf00"));
                rect.setStroke(Paint.valueOf("228B22"));
                rect.setVisible(true);
                this.add(rect, j, i);
            }
        }
        
        // draw the intial pieces on the board
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (b[i][j] == firstPlayer.getToken()) {
                    Circle white = new Circle((elementHeight - 1) / 2, Paint.valueOf(firstPlayer.getColor()));
                    white.setVisible(true);
                    white.setStroke(Paint.valueOf("000000"));
                    pieces.add(white);
                    this.add(white, j, i);
                } else if (b[i][j] == secondPlayer.getToken()) {
                    Circle black = new Circle((elementHeight - 1) / 2, Paint.valueOf(secondPlayer.getColor()));
                    black.setVisible(true);
                    black.setStroke(Paint.valueOf("000000"));
                    this.add(black, j, i);
                    pieces.add(black);
                }
            }
        }
    }

    public void redraw() {
        int size = (int) this.getPrefHeight();

        char[][] b = board.getBoard();

        int elementSize = size / board.getSize();

        // remove the pieces from the board
        pieces.forEach((piece) -> {
            this.getChildren().remove(piece);
        });

        // draw the new pieces on the board
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (b[i][j] == firstPlayer.getToken()) {
                    Circle white = new Circle((elementSize - 1) / 2, Paint.valueOf(firstPlayer.getColor()));
                    white.setVisible(true);
                    white.setStroke(Paint.valueOf("000000"));
                    pieces.add(white);
                    this.add(white, j, i);
                } else if (b[i][j] == secondPlayer.getToken()) {
                    Circle black = new Circle((elementSize - 1) / 2, Paint.valueOf(secondPlayer.getColor()));
                    black.setVisible(true);
                    black.setStroke(Paint.valueOf("000000"));
                    this.add(black, j, i);
                    pieces.add(black);
                }
            }
        }
    }
}
