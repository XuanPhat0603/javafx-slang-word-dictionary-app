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

import java.io.FileNotFoundException;
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

    private ObservableList<slangWord> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addSlangWord();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        meaningColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        listSlangWord.setItems(list);
    }

    void addSlangWord() throws FileNotFoundException {
        for (String key : slangWordList.getInstance().getList().keySet()) {
            for (String meaning : slangWordList.getInstance().getList().get(key)) {
                list.add(new slangWord(key, meaning));
            }
        }
    }
}
