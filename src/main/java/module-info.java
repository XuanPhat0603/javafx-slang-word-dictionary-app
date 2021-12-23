module com.example.dict_19127504 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hcmus.dict_19127504 to javafx.fxml;
    exports com.hcmus.dict_19127504;
}