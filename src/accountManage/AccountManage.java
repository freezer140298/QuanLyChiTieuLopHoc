package accountManage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccountManage extends Application{
    public static void main(String[] Args)
    {
        Application.launch(Args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader accountManagerScene = new FXMLLoader(getClass().getResource("accountManageForm.fxml"));
        Parent addFundDialogRoot = accountManagerScene.load();
        Stage stage = new Stage();
        stage.setTitle("Quản lý tài khoản");
        stage.setScene(new Scene(addFundDialogRoot));
        stage.setResizable(false);
        stage.showAndWait();
    }
}
