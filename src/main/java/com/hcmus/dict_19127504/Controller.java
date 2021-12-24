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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    slangWordList wordList = slangWordList.getInstance();

    @FXML
    private TableView<slangWord> findSlangWordTableView;

    @FXML
    private TableColumn<slangWord, String> wordColumnFind;

    @FXML
    private TableColumn<slangWord, String> meaningColumnFind;

    @FXML
    private TableView<slangWord> listSlangWord;

    @FXML
    private TableColumn<slangWord, String> wordColumn;

    @FXML
    private TableColumn<slangWord, String> meaningColumn;

    @FXML
    private TextField definitionInput;

    @FXML
    private Button meaningSeachBtn;

    @FXML
    private Button searchSlangWordBtn;

    @FXML
    private TextField slangWordInput;

    private ObservableList<slangWord> list = FXCollections.observableArrayList();
    private ObservableList<slangWord> listSearch = FXCollections.observableArrayList();

    public Controller() throws FileNotFoundException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchSlangWordBtn.setOnAction(event -> onClickSlangWordSearchBtn());
        meaningSeachBtn.setOnAction(event -> onClickDefinitionSearchBtn());
        addSlangWord();
    }

    private void setColumn() {
        wordColumnFind.setCellValueFactory(new PropertyValueFactory<>("word"));
        meaningColumnFind.setCellValueFactory(new PropertyValueFactory<>("meaning"));
    }

    private void onClickSlangWordSearchBtn() {
        setColumn();
        listSearch.clear();
        String word = slangWordInput.getText();
        if (wordList.findSlangWord(word) != null) {
            for (String meaning : wordList.findSlangWord(word)) {
                listSearch.add(new slangWord(word, meaning));
            }
        }
        else
            listSearch.add(new slangWord(word, "Không tìm thấy từ"));
        findSlangWordTableView.setItems(listSearch);
    }

    private void onClickDefinitionSearchBtn() {
        long startTime = System.currentTimeMillis();
        setColumn();
        String definition = definitionInput.getText();
        listSearch.clear();
        HashMap<String, ArrayList<String>> list = wordList.findDefinition(definition);
        if (list != null) {
            for (String word : list.keySet()) {
                for (String meaning : list.get(word)) {
                    listSearch.add(new slangWord(word, meaning));
                }
            }
        }
        findSlangWordTableView.setItems(listSearch);
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) + "ms");

    }

    private void addSlangWord() {
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        meaningColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        for (String key : wordList.getList().keySet()) {
            for (String meaning : wordList.getList().get(key)) {
                list.add(new slangWord(key, meaning));
            }
        }
        listSlangWord.setItems(list);
    }
}
