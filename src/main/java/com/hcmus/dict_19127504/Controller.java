package com.hcmus.dict_19127504;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements Initializable {

    slangWordList wordListInstance = slangWordList.getInstance();
    Boolean isFresh = true;

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

    @FXML
    private Label quizSlangWordLabel;

    @FXML
    private Button quizSlangWordRefreshBtn;

    @FXML
    private Button ABtn;

    @FXML
    private Button BBtn;

    @FXML
    private Button CBtn;

    @FXML
    private Button DBtn;

    @FXML
    private Button _ABtn;

    @FXML
    private Button _BBtn;

    @FXML
    private Button _CBtn;

    @FXML
    private Button _DBtn;

    @FXML
    private Label quizDefinitionLabel;

    @FXML
    private Button quizDefinitionRefreshBtn;

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
                showDialog("Đã reset");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        editBtn.setOnAction(event -> onClickEditBtn());
        random1SlangWordBtn.setOnAction(event -> onClickRandom1SlangWordBtn());
        quizSlangWordRefreshBtn.setOnAction(event -> onClickQuizSlangWordRefreshBtn());
        quizDefinitionRefreshBtn.setOnAction(event -> onClickQuizDefinitionRefreshBtn());
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
        quizSlangWord();
        quizDefinition();
    }

    private void onClickQuizDefinitionRefreshBtn() {
        quizDefinition();
    }

    private void quizDefinition() {
        isFresh = true;
        resetBtn(_ABtn, _BBtn, _CBtn, _DBtn);
        Random random = new Random();
        int index = random.nextInt(list.size());
        slangWord slangWord = list.get(index);
        String definition = slangWord.getMeaning();
        quizDefinitionLabel.setText(definition);
        quizDefinitionLabel.setWrapText(true);
        quizDefinitionLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 45));
        // random answer
        ArrayList<String> listAnswer = new ArrayList<>();
        listAnswer.add(slangWord.getWord());
        int i = 0;
        while (i >= 0) {
            if (i == 3) {
                break;
            }
            int rand = random.nextInt(list.size());
            if (!list.get(rand).getMeaning().equals(slangWord.getMeaning())) {
                listAnswer.add(list.get(rand).getWord());
                i++;
            } else i--;
        }

        Collections.shuffle(listAnswer);
        _ABtn.setText(listAnswer.get(0));
        _BBtn.setText(listAnswer.get(1));
        _CBtn.setText(listAnswer.get(2));
        _DBtn.setText(listAnswer.get(3));

        chooseAnswer(_ABtn, _BBtn, _CBtn, _DBtn, slangWord.getWord());
        chooseAnswer(_BBtn, _ABtn, _CBtn, _DBtn, slangWord.getWord());
        chooseAnswer(_CBtn, _ABtn, _BBtn, _DBtn, slangWord.getWord());
        chooseAnswer(_DBtn, _ABtn, _BBtn, _CBtn, slangWord.getWord());

    }


    private void onClickQuizSlangWordRefreshBtn() {
        quizSlangWord();
    }

    private void onClickRandom1SlangWordBtn() {
        random1SlangWord();
    }

    private void random1SlangWord() {
        Random random = new Random();
        int index = random.nextInt(list.size());
        random1SlangWordLabel.setText(list.get(index).getWord());
        random1SlangWordLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 60));
        random1SlangWordMeaningLabel.setText(list.get(index).getMeaning());
        random1SlangWordMeaningLabel.setWrapText(true);
    }

    private void onClickResetBtn() throws FileNotFoundException {
        wordListInstance.reset();
        addSlangWordToTableView();
    }

    private void onClickDeleteBtn() {
        deleteSlangWord();
    }

    private void deleteSlangWord() {
        String slangWord = slangWordInput.getText().trim();
        String meaning = meaningInput.getText().trim();
        if (slangWord.isEmpty() || meaning.isEmpty()) {
            showDialog("Hãy nhập từ lóng");
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
                showDialog("Xóa từ thành công");
                return;
            }
            if (result.get() == cancel) {
                return;
            }
        }
        else
            showDialog("Không tìm thấy từ");
    }
    private void onClickEditBtn() {
        String slangWord = slangWordInput.getText().trim();
        String meaning = meaningInput.getText().trim();
        if (slangWord.isEmpty() || meaning.isEmpty()) {
            showDialog("Hãy nhập từ lóng và nghĩa");
            return;
        }
        if (wordListInstance.getList().containsKey(slangWord)) {
            if (!wordListInstance.checkMeaning(slangWord, meaning)) {
                if (wordListInstance.editSlangWord(slangWord, meaning))
                    showDialog("Sửa từ thành công");
            } else {
                showDialog("Vui lòng chọn nghĩa khác");
            }
        }
        else
            showDialog("Không tìm thấy từ");
    }

    private void showDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
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
                showDialog("Vui lòng chọn nghĩa khác");
            }
        }
        else {
            wordListInstance.setMeaningOverwrite(slangWord, meaning);
            showDialog("Thêm từ thành công");
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
            showDialog("Vui lòng nhập từ cần tìm");
            return;
        }
        if (wordListInstance.findSlangWord(word) != null) {
            for (String key : wordListInstance.findSlangWord(word).keySet()) {
                for (String meaning : wordListInstance.getList().get(key)) {
                    listSearch.add(new slangWord(key, meaning));
                }
            }
        }
        else {
            showDialog("Không tìm thấy từ");
        }
        findSlangWordTableView.setItems(listSearch);
        wordListInstance.addHistory(word.toUpperCase(Locale.ROOT), dtf.format(now));
        addHistoryToTableView();
    }

    private void onClickDefinitionSearchBtn() {
        setColumn();
        String definition = findInput.getText().trim();
        if (definition.equals("")) {
            showDialog("Vui lòng nhập nghĩa cần tìm");
            return;
        }
        listSearch.clear();
        HashMap<String, ArrayList<String>> list = wordListInstance.findFromDefinition(definition);
        if (list.size() == 0)
            showDialog("Không tìm thấy từ");
        else {
            for (String key : list.keySet()) {
                for (String meaning : list.get(key)) {
                    listSearch.add(new slangWord(key, meaning));
                }
            }
        }
        findSlangWordTableView.setItems(listSearch);
    }

    private void addSlangWordToTableView() {
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        meaningColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        list.clear();
        for (String key : wordListInstance.getList().keySet()) {
            for (String meaning : wordListInstance.getList().get(key)) {
                list.add(new slangWord(key, meaning));
            }
        }
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

    private ArrayList<String> randomWordMeaning(int size) {
        ArrayList<String> list = new ArrayList<>();
        int random = (int) (Math.random() * size);
        int random2 = (int) (Math.random() * wordListInstance.getList().get(wordListInstance.getList().keySet().toArray()[random]).size());
        String word = wordListInstance.getList().keySet().toArray()[random].toString();
        String meaning = wordListInstance.getList().get(wordListInstance.getList().keySet().toArray()[random]).get(random2);
        list.add(word);
        list.add(meaning);
        return list;
    }

    private void quizSlangWord() {
        isFresh = true;
        resetBtn(ABtn, BBtn, CBtn, DBtn);
        ArrayList<String> listAnswer = new ArrayList<>();
        int size = wordListInstance.getList().size();
        ArrayList<String> list = randomWordMeaning(size);
        String word = list.get(0);
        String meaning = list.get(1);
        quizSlangWordLabel.setText(word);
        quizSlangWordLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 60));
        listAnswer.add(meaning);

        for (int i = 0; i < 3; i++) {
            ArrayList<String> list2 = randomWordMeaning(size);
            String meaning2 = list2.get(1);
            if (!meaning2.equals(meaning))
                listAnswer.add(meaning2);
        }
        Collections.shuffle(listAnswer);
        ABtn.setText(listAnswer.get(0));
        BBtn.setText(listAnswer.get(1));
        CBtn.setText(listAnswer.get(2));
        DBtn.setText(listAnswer.get(3));

        chooseAnswer(ABtn, BBtn, CBtn, DBtn, meaning);
        chooseAnswer(BBtn, ABtn, CBtn, DBtn, meaning);
        chooseAnswer(CBtn, ABtn, BBtn, DBtn, meaning);
        chooseAnswer(DBtn, ABtn, BBtn, CBtn, meaning);
    }

    private void chooseAnswer(Button ABtn, Button BBtn, Button CBtn, Button DBtn, String answer) {
        ABtn.setOnAction(e -> {
            if (isFresh) {
                BBtn.setDisable(true);
                CBtn.setDisable(true);
                DBtn.setDisable(true);
                if (ABtn.getText().equals(answer)) {
                    setRightAnswerBtn(ABtn);
                }
                else {
                    // set button is answer
                    if (BBtn.getText().equals(answer)) {
                        setRightAnswerBtn(BBtn);
                    }
                    else if (CBtn.getText().equals(answer)) {
                        setRightAnswerBtn(CBtn);
                    }
                    else if (DBtn.getText().equals(answer)) {
                        setRightAnswerBtn(DBtn);
                    }
                    setWrongAnswerBtn(ABtn);
                }
                isFresh = false;
            }
        });
    }
    private void resetBtn(Button ABtn, Button BBtn, Button CBtn, Button DBtn) {
        ABtn.setTextFill(Color.BLACK);
        BBtn.setTextFill(Color.BLACK);
        CBtn.setTextFill(Color.BLACK);
        DBtn.setTextFill(Color.BLACK);
        setDisableBtn(ABtn, BBtn, CBtn, DBtn, false);
        setColorBtn(ABtn, BBtn, CBtn, DBtn);
    }

    private void setRightAnswerBtn(Button btn) {
        btn.setStyle("-fx-background-color: #4BD62F");
        btn.setDisable(false);
    }

    private void setWrongAnswerBtn(Button btn) {
        btn.setStyle("-fx-background-color: #ff0000");
        btn.setTextFill(Color.WHITE);
    }

    private void setDisableBtn(Button ABtn, Button BBtn, Button CBtn, Button DBtn, boolean disable) {
        ABtn.setDisable(disable);
        BBtn.setDisable(disable);
        CBtn.setDisable(disable);
        DBtn.setDisable(disable);
    }

    private void setColorBtn(Button ABtn, Button BBtn, Button CBtn, Button DBtn) {
        // set style for button default color
        ABtn.setStyle("-fx-background-color: #FFFFFF");
        BBtn.setStyle("-fx-background-color: #FFFFFF");
        CBtn.setStyle("-fx-background-color: #FFFFFF");
        DBtn.setStyle("-fx-background-color: #FFFFFF");
    }
}
