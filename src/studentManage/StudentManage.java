package studentManage;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentManage extends Application{

    private ObservableList tableData;


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader studentManagerScene = new FXMLLoader(getClass().getResource("studentManageForm.fxml"));
        Parent addFundDialogRoot = studentManagerScene.load();
        Stage stage = new Stage();
        stage.setTitle("Quản lý học sinh");
        stage.setScene(new Scene(addFundDialogRoot));
        stage.setResizable(false);
        stage.showAndWait();
    }
}
