package income;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Income extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader incomeManagerScene = new FXMLLoader(getClass().getResource("incomeManage.fxml"));
        Parent addFundDialogRoot = incomeManagerScene.load();
        Stage stage = new Stage();
        stage.setTitle("Quản lý thu quỹ");
        stage.setScene(new Scene(addFundDialogRoot));
        stage.setResizable(false);
        stage.showAndWait();
    }
}
