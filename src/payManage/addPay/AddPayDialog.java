package payManage.addPay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddPayDialog extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader addFundDialog = new FXMLLoader(getClass().getResource("addPayDialog.fxml"));
        Parent addFundDialogRoot = addFundDialog.load();
        Stage stage = new Stage();
        stage.setTitle("Thêm quỹ mới");
        stage.setScene(new Scene(addFundDialogRoot));
        stage.setResizable(false);
        stage.showAndWait();
    }
}
