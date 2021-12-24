package com.hcmus.dict_19127504;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField search;

    @FXML
    private Button searchSlangWord;

    @FXML
    private TableColumn<slangWord, String> wordColumn;

    @FXML
    private TableColumn<slangWord, String> meaningColumn;

    @FXML
    private TableView<slangWord> listSlangWord;

    private ObservableList<slangWord> list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = FXCollections.observableArrayList(
                new slangWord("hello", "lô lô"),
                new slangWord("goodbye", "Tạm biệt"),
                new slangWord("thank you", "Cảm ơn")
        );
        wordColumn.setCellValueFactory(new PropertyValueFactory<slangWord, String>("word"));
        meaningColumn.setCellValueFactory(new PropertyValueFactory<slangWord, String>("meaning"));
        listSlangWord.setItems(list);
    }

}
