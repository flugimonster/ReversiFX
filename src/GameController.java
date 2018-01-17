/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversifx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class GameController implements Initializable {

    @FXML
    private HBox root;

    @FXML
    protected Text playerOneScore;

    @FXML
    protected Text playerTwoScore;

    @FXML
    protected Text currentPlayer;

    Board board;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        String boardSize;
        String playsFirst;
        String playerOneColor;
        String playerTwoColor;
        
        try (BufferedReader br = new BufferedReader(new FileReader("gameSettings.txt"))) {
            StringBuilder sb = new StringBuilder();
            boardSize = br.readLine(); // first line is the size of the board
            playsFirst = br.readLine(); // second line is who plays first
            playerOneColor = br.readLine(); // third line is the color of p1
            playerTwoColor = br.readLine(); // fourth line is the color of p2
            br.close();
        } catch (Exception e) {
            // if failed to read from the line for any reason - use the default
            // settings which are:
            boardSize = "8"; // an 8 by 8 board
            playsFirst = "player 1"; // player 1 goes first
            playerOneColor = "ffffff"; // player 1 is white
            playerTwoColor = "000000"; // player 2 is black
        }

        // create a new board using the chosen size
        board = new Board(Integer.parseInt(boardSize));
        
        BoardController bc = new BoardController(board, this, playsFirst,
                playerOneColor, playerTwoColor);
        bc.setPrefWidth(400);
        bc.setPrefHeight(400);
        root.getChildren().add(0, bc);
        bc.draw();

        root.widthProperty().addListener((observable, oldValue, newValue) -> {
            double boardNewWidth = newValue.doubleValue() - 200;
            bc.setPrefWidth(boardNewWidth);
            bc.draw();
        });
        root.heightProperty().addListener((observable, oldValue, newValue) -> {
            bc.setPrefHeight(newValue.doubleValue());
            bc.draw();
        });
    }

    public void updateStats(String cp, int p1Score, int p2Score) {
        this.currentPlayer.setText(cp);
        this.playerOneScore.setText(Integer.toString(p1Score));
        this.playerTwoScore.setText(Integer.toString(p2Score));
    }

}
