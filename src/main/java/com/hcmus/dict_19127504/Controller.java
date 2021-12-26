package com.hcmus.dict_19127504;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements Initializable {

    slangWordList wordListInstance = slangWordList.getInstance();

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
    private TextField findInput;

    @FXML
    private Button meaningSeachBtn;

    @FXML
    private Button searchSlangWordBtn;

    @FXML
    private TextField slangWordInput;

    @FXML
    private TextField meaningInput;

    @FXML
    private Button addBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private TableView<history> listHistoryTableView;

    @FXML
    private TableColumn<history, String> timeHistoryColumn;

    @FXML
    private TableColumn<history, String> wordHistoryColumn;

    @FXML
    private Label random1SlangWordLabel;

    @FXML
    private Label random1SlangWordMeaningLabel;

    @FXML
    private Button random1SlangWordBtn;

    private ObservableList<slangWord> list = FXCollections.observableArrayList();
    private ObservableList<slangWord> listSearch = FXCollections.observableArrayList();
    private ObservableList<history> listHistory = FXCollections.observableArrayList();

    public Controller() throws FileNotFoundException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchSlangWordBtn.setOnAction(event -> onClickSlangWordSearchBtn());
        meaningSeachBtn.setOnAction(event -> onClickDefinitionSearchBtn());
        addBtn.setOnAction(event -> onClickAddBtn());
        deleteBtn.setOnAction(event -> onClickDeleteBtn());
        resetBtn.setOnAction(event -> {
            try {
                onClickResetBtn();
                showDialog("Thông báo", "Đã reset", Alert.AlertType.INFORMATION);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        editBtn.setOnAction(event -> onClickEditBtn());
        random1SlangWordBtn.setOnAction(event -> onClickRandom1SlangWordBtn());
        findSlangWordTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            slangWord selectedItem =  findSlangWordTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                slangWordInput.setText(selectedItem.getWord());
                meaningInput.setText(selectedItem.getMeaning());
            }
        });
        addSlangWordToTableView();
        addHistoryToTableView();
        random1SlangWord();
    }

    private void onClickRandom1SlangWordBtn() {
        random1SlangWord();
    }

    private void random1SlangWord() {
        Random random = new Random();
        int index = random.nextInt(list.size());
        random1SlangWordLabel.setText(list.get(index).getWord());
        random1SlangWordMeaningLabel.setText(list.get(index).getMeaning());
    }

    private void onClickResetBtn() throws FileNotFoundException {
        wordListInstance.reset();
        addSlangWordToTableView();
    }

    private void onClickDeleteBtn() {
        String slangWord = slangWordInput.getText().trim();
        String meaning = meaningInput.getText().trim();
        if (slangWord.isEmpty() || meaning.isEmpty()) {
            showDialog("Thông báo", "Hãy nhập từ lóng", Alert.AlertType.INFORMATION);
            return;
        }
        if (wordListInstance.getList().containsKey(slangWord)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa từ này không?");
            alert.setContentText("Từ: " + slangWord + "\nNghĩa: " + meaning);
            ButtonType confirm = new ButtonType("Có", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(confirm, cancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == confirm) {
                // overwrite
                wordListInstance.deleteSlangWord(slangWord, meaning);
                showDialog("Thông báo", "Xóa từ thành công", Alert.AlertType.INFORMATION);
                return;
            }
            if (result.get() == cancel) {
                return;
            }
        } else
            showDialog("Thông báo", "Không tìm thấy từ", Alert.AlertType.INFORMATION);
    }

    private void onClickEditBtn() {
        String slangWord = slangWordInput.getText().trim();
        String meaning = meaningInput.getText().trim();
        if (slangWord.isEmpty() || meaning.isEmpty()) {
            showDialog("Thông báo", "Hãy nhập từ lóng và nghĩa", Alert.AlertType.INFORMATION);
            return;
        }
        if (wordListInstance.getList().containsKey(slangWord)) {
            if (!wordListInstance.checkMeaning(slangWord, meaning)) {
                if (wordListInstance.editSlangWord(slangWord, meaning))
                    showDialog("Thông báo", "Sửa từ thành công", Alert.AlertType.INFORMATION);
            } else {
                showDialog("Thông báo", "Vui lòng chọn nghĩa khác", Alert.AlertType.INFORMATION);
            }
        }
        else
            showDialog("Thông báo", "Không tìm thấy từ", Alert.AlertType.INFORMATION);
    }

    private void showDialog(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void onClickAddBtn() {
        String slangWord = slangWordInput.getText().toUpperCase(Locale.ROOT).trim();
        String meaning = meaningInput.getText().trim();
        if (slangWord.equals("") || meaning.equals("")) {
            // show error
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ thông tin");
            alert.showAndWait();
            return;
        }
        if (wordListInstance.findSlangWord(slangWord) != null) {
            if (!wordListInstance.checkMeaning(slangWord, meaning)) {
                // show error
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Từ này đã tồn tại");
                alert.setContentText("Bạn muốn ???");
                // add option: overwrite, duplicate, cancel
                ButtonType duplicate = new ButtonType("Tạo bản sao");
                ButtonType overwrite = new ButtonType("Ghi đè");
                ButtonType cancel = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(overwrite, duplicate, cancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == overwrite) {
                    wordListInstance.setMeaningOverwrite(slangWord, meaning);
                    return;
                }
                if (result.get() == duplicate) {
                    wordListInstance.setMeaningDuplicate(slangWord, meaning);
                    return;

                }
                if (result.get() == cancel)
                    return;
            }
            else {
                showDialog("Thông báo", "Vui lòng chọn nghĩa khác", Alert.AlertType.INFORMATION);
            }
        }
        else {
            wordListInstance.setMeaningOverwrite(slangWord, meaning);
            showDialog("Thông báo", "Thêm từ thành công", Alert.AlertType.INFORMATION);
        }
    }

    private void setColumn() {
        wordColumnFind.setCellValueFactory(new PropertyValueFactory<>("word"));
        meaningColumnFind.setCellValueFactory(new PropertyValueFactory<>("meaning"));
    }

    private void onClickSlangWordSearchBtn() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        setColumn();
        listSearch.clear();
        String word = findInput.getText().trim();
        if (word.equals("")) {
            showDialog("Thông báo", "Vui lòng nhập từ cần tìm", Alert.AlertType.INFORMATION);
            return;
        }
        if (wordListInstance.findSlangWord(word) != null) {
            for (String meaning : wordListInstance.findSlangWord(word)) {
                listSearch.add(new slangWord(word.toUpperCase(Locale.ROOT), meaning));
            }
        }
        else {
            showDialog("Thông báo", "Không tìm thấy từ", Alert.AlertType.INFORMATION);
        }
        findSlangWordTableView.setItems(listSearch);
        wordListInstance.addHistory(word.toUpperCase(Locale.ROOT), dtf.format(now));
        addHistoryToTableView();
    }

    private void onClickDefinitionSearchBtn() {
        setColumn();
        String definition = findInput.getText().trim();
        if (definition.equals("")) {
            showDialog("Thông báo", "Vui lòng nhập nghĩa cần tìm", Alert.AlertType.INFORMATION);
            return;
        }
        listSearch.clear();
        HashMap<String, ArrayList<String>> list = wordListInstance.findFromDefinition(definition);
        if (list.size() == 0)
            showDialog("Thông báo", "Không tìm thấy từ", Alert.AlertType.INFORMATION);
        else {
            for (String key : list.keySet())
                for (String meaning : list.get(key)) {
                    listSearch.add(new slangWord(key, meaning));
                }
        }
        findSlangWordTableView.setItems(listSearch);
    }

    private void addSlangWordToTableView() {
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        meaningColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        list.clear();
        for (String key : wordListInstance.getList().keySet())
            for (String meaning : wordListInstance.getList().get(key))
                list.add(new slangWord(key, meaning));
        listSlangWord.setItems(list);
    }

    private void addHistoryToTableView() {
        wordHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        timeHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        listHistory.clear();
        if (wordListInstance.getHistory().size() > 0) {
            for (int i = wordListInstance.getHistory().size() - 1; i >= 0; i--) {
                listHistory.add(new history(wordListInstance.getHistory().get(i).getWord(), wordListInstance.getHistory().get(i).getTime()));
            listHistoryTableView.setItems(listHistory);
            }
        }
    }
}
