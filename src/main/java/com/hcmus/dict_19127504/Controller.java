package com.hcmus.dict_19127504;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class Controller {
    @FXML
    private TableView<slangWord> tableView = new TableView<>();

    @FXML
    private TextField search;

    @FXML
    private Button searchSlangWord;

    @FXML
    private TableColumn<slangWord, String> wordColumn;

    @FXML
    private TableColumn<slangWord, String> meaningColumn;

    private ObservableList<slangWord> data;
    @FXML
    public void initialize() {
        data = FXCollections.observableArrayList(
            new slangWord("hello", "xin chào"),
            new slangWord("goodbye", "tạm biệt"),
            new slangWord("thank you", "cảm ơn")
        );
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        meaningColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        tableView.setItems(data);
    }
}
