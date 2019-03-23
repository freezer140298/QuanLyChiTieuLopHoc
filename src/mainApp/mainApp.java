package mainApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainApp = new FXMLLoader(getClass().getResource("mainForm.fxml"));
        Parent mainAppRoot = mainApp.load();
        Stage stage = new Stage();
        stage.setTitle("Quản lý chi tiêu lớp học");
        stage.setScene(new Scene(mainAppRoot));
        stage.setResizable(false);
        stage.show();
    }
}
