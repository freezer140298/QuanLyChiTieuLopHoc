package accountManage.addAccountDialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static login.MD5.getMD5;
import static login.Controller.conn;

public class addAccountDialogController implements Initializable {

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText1;

    @FXML
    private PasswordField passwordText2;

    @FXML
    private Button confirmButton;

    @FXML
    private ChoiceBox<String> permissionChoiceBox;

    @FXML
    void addAccount(ActionEvent event) {
        try
        {
            if(usernameText.getText() == "" || passwordText1.getText() == "" || passwordText2.getText() == "")
                throw new IllegalArgumentException("TextFieldOrPasswordFieldNotEmpty");

            if(!passwordText1.getText().equals(passwordText1.getText()))
            {
                throw new IllegalArgumentException("PasswordNotSame");
            }

            PreparedStatement statement = conn.prepareStatement("INSERT INTO account(username,password,permission) " +
                                                                        " VALUES (?,?,?)");
            statement.setString(1,usernameText.getText());
            statement.setString(2,getMD5(passwordText1.getText()));
            statement.setString(3,permissionChoiceBox.getValue());

            statement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Thêm tài khoản thành công");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            if(e.getMessage().equals("[SQLITE_CONSTRAINT_UNIQUE]  A UNIQUE constraint failed (UNIQUE constraint failed: account.username)"))
            {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Lỗi");
                error.setContentText("Tài khoản đã tồn tại");
                error.showAndWait();
            }
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Lỗi");
            if(e.getMessage().equals("TextFieldOrPasswordFieldNotEmpty"))
            {
                error.setContentText("Tài khoản hoặc mật khẩu không được rỗng");
            }
            else
            {
                error.setContentText("Mật khẩu không giống nhau");
            }
            error.showAndWait();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> permission = FXCollections.observableArrayList("viewer","manager");

        permissionChoiceBox.setItems(permission);
        permissionChoiceBox.setValue("viewer");
    }
}
