package accountManage.changePasswordDialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import login.Account;
import login.Controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static login.MD5.getMD5;
import static login.Controller.conn;

public class changePasswordDialogController{
    @FXML
    private PasswordField oldPasswordText;

    @FXML
    private PasswordField newPasswordText1;

    @FXML
    private PasswordField newPasswordText2;

    @FXML
    private Button confirmButton;


    @FXML
    void changePassword(ActionEvent event) {
        Account account = Controller.account;

        try
        {
            if(!newPasswordText1.getText().equals(newPasswordText2.getText()))
            {
                throw new IllegalArgumentException("PasswordNotSame");
            }

            if(!getMD5(oldPasswordText.getText()).equals(account.getPassword()))
            {
                throw new IllegalArgumentException("PasswordNotCorrect");
            }

            PreparedStatement statement = conn.prepareStatement("UPDATE account " +
                    " SET password = ? " +
                    "WHERE ID = ? AND username = ?");
            statement.setString(1,getMD5(newPasswordText1.getText()));
            statement.setInt(2,account.getID());
            statement.setString(3,account.getUsername());

            statement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Đổi mật khẩu thành công");
            alert.showAndWait();

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Lỗi");
            if(e.getMessage().equals("PasswordNotSame"))
                error.setContentText("Mật khẩu không giống nhau");
            else
                error.setContentText("Mật khẩu không đúng");
            error.showAndWait();
        }
    }

}


