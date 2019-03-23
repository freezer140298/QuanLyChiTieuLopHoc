package fundManage.editFund;

import fundManage.Fund;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import static mainApp.Controller.tableViewFundSelected;
import static  login.Controller.conn;

public class Controller implements Initializable {

    private final NumberFormat vi = NumberFormat.getInstance(new Locale("vi","VN"));

    private final StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }
        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };

    @FXML
    private TextField payNameText;

    @FXML
    private TextField cashText;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button cancelButton;

    @FXML
    private Button editButton;

    @FXML
    void editFund(ActionEvent event) {
        Fund fund = new Fund();
        fund.setFundname(payNameText.getText());
        fund.setCash(Integer.parseInt(cashText.getText().replaceAll("\\.","")));
        fund.setDate(datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        try
        {
            PreparedStatement statement = conn.prepareStatement("UPDATE fund_list" +
                                                                    " SET fundname = ? , cash = ? , date = ?" +
                                                                    "WHERE ID = ?");
            statement.setString(1,fund.getFundname());
            statement.setInt(2,fund.getCash());
            statement.setString(3,fund.getDate());
            statement.setInt(4, tableViewFundSelected.getID());

            statement.execute();
            //con.close();

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Thông báo");
            success.setContentText("Đã chỉnh sửa thành công");
            success.showAndWait();

            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void numberFormat(KeyEvent event) {
        // Format cash to xxx.xxx VND
        String tempStr = cashText.getText().replaceAll("\\.","");
        String formatted = vi.format(Integer.parseInt(tempStr));
        cashText.setText(formatted);
        cashText.requestFocus();
        cashText.end();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ld = LocalDate.parse(tableViewFundSelected.getDate(),dtf);
        datePicker.setValue(ld);
        datePicker.setConverter(converter);
        datePicker.setPromptText("dd/MM/yyyy");
        payNameText.setText(tableViewFundSelected.getFundname());
        cashText.setText(vi.format(tableViewFundSelected.getCash()));
    }
}
