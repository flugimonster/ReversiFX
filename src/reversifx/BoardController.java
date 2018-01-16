/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversifx;

import java.io.File;
import java.io.IOException;
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
    private static final char WHITE = 'O';
    private static final char BLACK = 'X';
    private Player firstPlayer;
    private Player secondPlayer;

    public BoardController(Board b, GameController gc, String playsFirst,
            String playerOneColor, String playerTwoColor) {

        this.board = b;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "Board.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        if ("player 1".equals(playsFirst)) {
            firstPlayer = new Player('O', playerOneColor, "player 1");
            secondPlayer = new Player('X', playerTwoColor, "player 2");
        } else {
            firstPlayer = new Player('O', playerTwoColor, "player 2");
            secondPlayer = new Player('X', playerOneColor, "player 1");
        }

        File f = new File("gameSettings");

        GameLogic gameLogic = new GameLogic(board, firstPlayer, secondPlayer);
        gc.updateStats(playsFirst, 2, 2);
        try {
            fxmlLoader.load();
            this.setOnMousePressed(event -> {
                int row = (int) event.getY();
                int col = (int) event.getX();
                int elementSize = (int) this.getWidth()
                        / this.board.getBoard().length;
                Element move = new Element(row / elementSize, col / elementSize);
                if (gameLogic.playOneTurn(move)) {
                    draw();
                }
                int firstPlayerScore = firstPlayer.getNumberOfPieces();
                int secondPlayerScore = secondPlayer.getNumberOfPieces();
                String currentPlayer = gameLogic.getCurrentPlayer();
                if ("player 1".equals(firstPlayer.getName())) {
                    gc.updateStats(currentPlayer, firstPlayerScore,
                            secondPlayerScore);
                } else {
                    gc.updateStats(currentPlayer, secondPlayerScore,
                            firstPlayerScore);
                }
                if (gameLogic.gameOver()) {
                    Alert alert = new Alert(AlertType.NONE,
                            gameLogic.declareWinner() + "\n" + "press \"OK\" "
                            + "to return to the main menu",
                            ButtonType.OK);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        try {
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

        this.getChildren().clear();
        int height = (int) this.getPrefHeight();
        int width = (int) this.getPrefWidth();

        char[][] b = board.getBoard();

        int cellHeight = height / board.getSize();
        int cellWidth = width / board.getSize();

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Rectangle rect = new Rectangle(cellWidth, cellHeight, Paint.valueOf("00bf00"));
                rect.setStroke(Paint.valueOf("228B22"));
                rect.setVisible(true);
                this.add(rect, j, i);
            }
        }
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                switch (b[i][j]) {
                    case WHITE:
                        Circle white = new Circle((cellHeight - 1) / 2, Paint.valueOf(firstPlayer.getColor()));
                        white.setVisible(true);
                        white.setStroke(Paint.valueOf("000000"));
                        this.add(white, j, i);
                        break;
                    case BLACK:
                        Circle black = new Circle((cellHeight - 1) / 2, Paint.valueOf(secondPlayer.getColor()));
                        black.setVisible(true);
                        black.setStroke(Paint.valueOf("000000"));
                        this.add(black, j, i);
                        break;
                }

            }
        }
    }

}
