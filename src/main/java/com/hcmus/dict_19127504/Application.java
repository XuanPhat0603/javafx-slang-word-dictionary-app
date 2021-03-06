package com.hcmus.dict_19127504;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("slangword-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SlangWord Dictionary");
        stage.setScene(scene);
        stage.getIcons().add(new javafx.scene.image.Image(Application.class.getResourceAsStream("/icons/icon.png")));
        stage.show();
        // hadle close event
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}