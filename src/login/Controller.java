package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainApp.mainApp;

import java.io.IOException;
import java.sql.*;
import java.util.IllformedLocaleException;

import static login.MD5.getMD5;

public class Controller {

    public static Account account;

    public static Connection conn;

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField usernameText;

    @FXML
    private Button loginButton;

    @FXML
    void login(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");

        if(usernameText.getText() == "" || passwordText.getText() == "")
        {
            alert.setContentText("Tài khoản hoặc mật khẩu không được rỗng");
            alert.showAndWait();
        }

        try
        {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite::resource:database.db";
            conn = DriverManager.getConnection(url);

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM account WHERE username = ? AND password = ?");

            statement.setString(1,usernameText.getText());

            String hashedPassword = getMD5(passwordText.getText());
            statement.setString(2,hashedPassword);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next())
            {
                alert.setContentText("Đăng nhập thành công");
                account = new Account(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                alert.showAndWait();

                // Close log in scene

                Stage login = (Stage) loginButton.getScene().getWindow();
                login.close();

                // Start main app
                mainApp mainApp = new mainApp();
                mainApp.start(new Stage());

            }
            else
            {
                alert.setContentText("Sai tài khoản hoặc mật khẩu");
                alert.showAndWait();
            }

            statement.close();
            //con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllformedLocaleException e)
        {
            e.printStackTrace();
            switch (e.getMessage())
            {
                case "EMAILNOTVALID": {
                    alert.setContentText("Email không hợp lệ");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

