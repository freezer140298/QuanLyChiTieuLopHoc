package studentManage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static login.Controller.conn;

public class Controller implements Initializable {

    @FXML
    private TableView<TableViewStudent> tableView;

    @FXML
    private TableColumn<TableViewStudent, String> fullNameCol;

    @FXML
    private TableColumn<TableViewStudent, String> emailCol;

    @FXML
    private TableColumn<TableViewStudent, String> phoneCol;

    @FXML
    private TableColumn<TableViewStudent, String> genderCol;

    @FXML
    private TableColumn<TableViewStudent, String> addressCol;

    @FXML
    private TextField fullNameText;

    @FXML
    private TextField emailText;

    @FXML
    private TextField phoneText;

    @FXML
    private TextArea addressText;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button updateButton;

    @FXML
    private ToggleGroup sex;

    @FXML
    private RadioButton maleRadio;

    @FXML
    private RadioButton femaleRadio;

    // Object for display
    private TableViewStudent selectedStudent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("sexString"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        buildData();
    }


    private void buildData()
    {
        List<TableViewStudent> lStudent = new ArrayList<TableViewStudent>();
        ObservableList<TableViewStudent> data = null;
        try
        {
            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM student");

            // Fill data to Table view

            while(rs.next()) {
                TableViewStudent tableViewStudent = new TableViewStudent();
                tableViewStudent.setID(rs.getInt(1));
                tableViewStudent.setFullname(rs.getString(2));
                tableViewStudent.setEmail(rs.getString(3));
                tableViewStudent.setPhone(rs.getString(4));
                tableViewStudent.setSex(rs.getInt(5));
                tableViewStudent.setSexString(rs.getInt(5));
                tableViewStudent.setAddress(rs.getString(6));
                lStudent.add(tableViewStudent);
            }

            statement.close();
            //con.close();
            data = FXCollections.observableArrayList(lStudent);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(data);
    }

    @FXML
    void addStudent(ActionEvent event) {
        Student student = new Student();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");

        try
        {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO student(fullname,email,phone,sex,address) " +
                    "VALUES (?,?,?,?,?)");

            student.setFullname(fullNameText.getText());
            student.setEmail(emailText.getText());
            student.setPhone(phoneText.getText());
            student.setAddress(addressText.getText());

            // Get gender of student
            RadioButton sexRadioButton = (RadioButton) sex.getSelectedToggle();
            String toggleGroupValue;
            if(sexRadioButton.getText() != null)
            {
                toggleGroupValue = sexRadioButton.getText();
            }
            else
            {
                throw new NullPointerException("GenderNull");
            }
            if(toggleGroupValue.equals("Nam"))
            {
                student.setSex(0);
            }
            else
            {
                student.setSex(1);
            }

            statement.setString(1,student.getFullname());
            statement.setString(2,student.getEmail());
            statement.setString(3,student.getPhone());
            statement.setInt(4,student.getSex());
            statement.setString(5,student.getAddress());

            statement.execute();
            statement.close();
            //con.close();
            alert.setContentText("Đã thêm thành công");
            buildData();

        } catch (SQLException e) {
            alert.setContentText("Thất bại");
            e.printStackTrace();
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            alert.setContentText("Giới tính chưa được chọn");
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            showError(e,alert);
        }
        alert.showAndWait();
    }

    @FXML
    void selectStudent(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY)
        {
            selectedStudent = tableView.getSelectionModel().getSelectedItem();
            fullNameText.setText(selectedStudent.getFullname());
            emailText.setText(selectedStudent.getEmail());
            phoneText.setText(selectedStudent.getPhone());


            if(selectedStudent.getSex() == 0)
            {
                sex.selectToggle(maleRadio);
            }
            else
            {
                sex.selectToggle(femaleRadio);
            }

            addressText.setText(selectedStudent.getAddress());
        } else
        {
            selectedStudent = null;
            tableView.getSelectionModel().clearSelection();
            fullNameText.setText("");
            emailText.setText("");
            phoneText.setText("");
            sex.selectToggle(null);
            addressText.setText("");
        }
    }

    @FXML
    void removeStudent(ActionEvent event) {

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Xác nhận");
        confirmDialog.setContentText("Bạn có muốn xoá?");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if(result.get() == ButtonType.OK)
        {
            try
            {
                PreparedStatement statement1 = conn.prepareStatement("DELETE FROM student WHERE ID = ?");
                statement1.setInt(1,selectedStudent.getID());
                statement1.execute();
                statement1.close();

                PreparedStatement statement2 = conn.prepareStatement("DELETE FROM income WHERE studentID = ?");
                statement2.setInt(1,selectedStudent.getID());
                statement2.execute();
                statement2.close();

                //con.close();
                buildData();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Đã xoá thành công");
                alert.showAndWait();

            } catch (NullPointerException e)
            {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Chưa chọn để xoá");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void updateStudent(ActionEvent event) {
        TableViewStudent temp = selectedStudent;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        try
        {
            temp.setFullname(fullNameText.getText());
            temp.setEmail(emailText.getText());
            temp.setPhone(phoneText.getText());

            RadioButton sexRadioButton = (RadioButton) sex.getSelectedToggle();
            String toggleGroupValue = sexRadioButton.getText();
            if(toggleGroupValue.equals("Nam"))
            {
                temp.setSex(0);
            }
            else
            {
                temp.setSex(1);
            }

            temp.setAddress(addressText.getText());

            PreparedStatement statement = conn.prepareStatement("UPDATE student" +
                                                                    " SET fullname=? , email = ? , phone = ? , sex = ? ,address = ?" +
                                                                    " WHERE ID = ?");
            statement.setString(1,temp.getFullname());
            statement.setString(2,temp.getEmail());
            statement.setString(3,temp.getPhone());
            statement.setInt(4,temp.getSex());
            statement.setString(5,temp.getAddress());
            statement.setInt(6,temp.getID());

            statement.execute();
            statement.close();
            //con.close();

            buildData();

            alert.setContentText("Cập nhật thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            showError(e,alert);
        }
        alert.showAndWait();
    }

    private void showError(IllegalArgumentException e, Alert alert)
    {
        switch (e.getMessage())
        {
            case "EmailNotVaild":
            {
                alert.setContentText("E-mail không hợp lệ");
                break;
            }
            case "PhoneNotValid":
            {
                alert.setContentText("SĐT không hợp lệ");
                break;
            }
        }
    }
}
