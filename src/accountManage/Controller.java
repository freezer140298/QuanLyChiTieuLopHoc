package accountManage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


import static login.Controller.account;
import static login.Controller.conn;

public class Controller extends login.Controller implements Initializable {

    @FXML
    private Label permissionText;

    @FXML
    private TableView<TableViewAccount> tableView;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button addAccountButton;


    @FXML
    private TableColumn<TableViewAccount, String> usernameColumn;

    @FXML
    private TableColumn<TableViewAccount, String> accountPermissionColumn;

    @FXML
    private TableColumn<TableViewAccount, HBox> operateColumn;

    @FXML
    void changePassword(ActionEvent event) {
        try {
            // Call Change Password Dialog
            FXMLLoader changePasswordFormLoader = new FXMLLoader(getClass().getResource("changePasswordDialog/changePasswordDialogForm.fxml"));
            Parent root1 = changePasswordFormLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Đổi mật khẩu");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void addAccount(ActionEvent event){
        if(account.getPermission().equals("viewer"))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Tài khoản hiện tại không có quyền hạn này");
            alert.showAndWait();
            return;
        }

        try
        {
            FXMLLoader changePasswordFormLoader = new FXMLLoader(getClass().getResource("addAccountDialog/addAccountDialogForm.fxml"));
            Parent root1 = changePasswordFormLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Thêm tài khoản");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void buildTableView()
    {
        List<TableViewAccount> lAccount = new ArrayList<TableViewAccount>();
        ObservableList<TableViewAccount> data = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM account");

            ArrayList<String> permissionTextList = new ArrayList<String>();
            permissionTextList.add("viewer");
            permissionTextList.add("manager");

            while(rs.next())
            {
                TableViewAccount tableViewAccount = new TableViewAccount(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4));
                ChoiceBox<String> permissionChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(permissionTextList));
                tableViewAccount.setPermissionChoiceBox(permissionChoiceBox);
                permissionChoiceBox.setValue(tableViewAccount.getPermission());
                Button updateButton = new Button("Cập nhật");
                Button removeButton = new Button("Xoá");

                // Set on action for update account permission button
                updateButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Xác nhận");
                        confirm.setContentText("Xác nhận lại lựa chọn");

                        Optional<ButtonType> result = confirm.showAndWait();
                        if(result.get() == ButtonType.OK)
                        {
                            try {
                                PreparedStatement statement1 = conn.prepareStatement("UPDATE account SET permission = ? WHERE id = ? AND username = ?");
                                statement1.setString(1,permissionChoiceBox.getValue());
                                statement1.setInt(2,tableViewAccount.getID());
                                statement1.setString(3,tableViewAccount.getUsername());
                                statement1.execute();

                                Alert inform = new Alert(Alert.AlertType.INFORMATION);
                                inform.setTitle("Thông báo");
                                inform.setContentText("Đã thay đổi thành công");
                                inform.showAndWait();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                // Set on action for remove account button
                removeButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Xác nhận");
                        confirm.setContentText("Xác nhận lại lựa chọn");

                        Optional<ButtonType> result = confirm.showAndWait();
                        if(result.get() == ButtonType.OK)
                        {
                            try {
                                PreparedStatement statement1 = conn.prepareStatement("DELETE FROM account WHERE ID = ? AND username = ?");
                                statement1.setInt(1,tableViewAccount.getID());
                                statement1.setString(2,tableViewAccount.getUsername());
                                statement1.execute();

                                Alert inform = new Alert(Alert.AlertType.INFORMATION);
                                inform.setTitle("Thông báo");
                                inform.setContentText("Đã xoá thành công");
                                inform.showAndWait();
                                buildTableView();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


                HBox hBox = new HBox(updateButton,removeButton);
                tableViewAccount.setOperate(hBox);
                lAccount.add(tableViewAccount);
            }

            data = FXCollections.observableArrayList(lAccount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(data);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Display permission
        permissionText.setText(account.getPermission());

        // Table View
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        accountPermissionColumn.setCellValueFactory(new PropertyValueFactory<>("permissionChoiceBox"));
        operateColumn.setCellValueFactory(new PropertyValueFactory<>("operate"));

        if(permissionText.getText().equals("viewer"))
        {
            tableView.setPlaceholder(new Label("Tài khoản không có quyền hạn này"));
            return;
        }
        else
        {
            buildTableView();
        }
    }
}

