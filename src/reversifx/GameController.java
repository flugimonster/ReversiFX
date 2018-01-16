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
    protected Text p1Score;

    @FXML
    protected Text p2Score;

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
            boardSize = br.readLine();
            playsFirst = br.readLine();
            playerOneColor = br.readLine();
            playerTwoColor = br.readLine();
            br.close();
        } catch (Exception e) {
            boardSize = "8";
            playsFirst = "player 1";
            playerOneColor = "ffffff";
            playerTwoColor = "000000";
        }

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

    public void updateStats(String cp, int p1, int p2) {
        String p1s = Integer.toString(p1);
        String p2s = Integer.toString(p2);
        currentPlayer.setText(cp);
        p1Score.setText(p1s);
        p2Score.setText(p2s);
    }

}
