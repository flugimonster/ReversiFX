/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversifx;

import java.io.IOException;
import java.io.PrintWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author flug
 */
public class SettingsController {

    ObservableList list = FXCollections.observableArrayList();

    @FXML
    protected Text message;

    @FXML
    private ChoiceBox<String> boardSize;

    @FXML
    private ChoiceBox<String> whoGoesFirst;

    @FXML
    ColorPicker playerOneColor;

    @FXML
    ColorPicker playerTwoColor;

    @FXML
    protected void saveSettings(ActionEvent event) throws IOException {
        String sizeOfBoard = boardSize.getValue();
        String goesFirst = whoGoesFirst.getValue();
        String p1Color = playerOneColor.getValue().toString();
        String p2Color = playerTwoColor.getValue().toString();

        PrintWriter fw;
        try {
            fw = new PrintWriter("gameSettings.txt", "UTF-8");
            fw.println(sizeOfBoard + "\n" + goesFirst + "\n" + p1Color + "\n"
                    + p2Color);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        Scene nextScene = new Scene(root, 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();
    }

}
