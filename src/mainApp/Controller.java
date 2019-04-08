package mainApp;

import accountManage.AccountManage;
import fundManage.addFund.AddFundDialog;
import fundManage.TableViewFund;
import fundManage.editFund.EditFundDialog;
import income.Income;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import payManage.TableViewPay;
import payManage.addPay.AddPayDialog;
import payManage.editPay.EditPayDialog;
import studentManage.StudentManage;

import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.*;

import static login.Controller.account;
import static login.Controller.conn;

public class Controller implements Initializable {

    private ContextMenu fundContextMenu = new ContextMenu();

    public static TableViewFund tableViewFundSelected;

    private ContextMenu payContextMenu = new ContextMenu();

    public static TableViewPay tableViewPaySelected;

    @FXML
    private Label currentAvailableCash;

    @FXML
    private MenuItem accountManager;

    @FXML
    private MenuItem studentManager;

    @FXML
    private MenuItem incomeManager;

    @FXML
    private Label currentAccountText;

    @FXML
    private TableView<TableViewFund> fundTableView;

    @FXML
    private TableColumn<TableViewFund, String> fundNameCol;

    @FXML
    private TableColumn<TableViewFund, String> fundCashCol;

    @FXML
    private TableColumn<TableViewFund, Date> fundDateCol;

    @FXML
    private TableView<TableViewPay> payTableView;

    @FXML
    private TableColumn<TableViewPay, String> payNameCol;

    @FXML
    private TableColumn<TableViewPay, String> payCashCol;

    @FXML
    private TableColumn<TableViewPay, Date> payDateCol;


    @FXML
    void fundContextMenu(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY)
        {
            fundContextMenu.setAutoHide(true);
            fundContextMenu.show(fundTableView,event.getScreenX(),event.getScreenY());
        }
        else
        {
            fundContextMenu.hide();
        }
    }

    @FXML
    void payContextMenu(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY)
        {
            payContextMenu.setAutoHide(true);
            payContextMenu.show(payTableView,event.getScreenX(),event.getScreenY());
        }
        else
        {
            payContextMenu.hide();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentAccountText.setText(account.getUsername());
        updateAvailableCash();

        // Initialization for fund context menu
        if(account.getPermission().equals("admin") || account.getPermission().equals("manager"))
        {
            MenuItem addFund = new MenuItem("Thêm");

            addFund.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        AddFundDialog addFundDialog = new AddFundDialog();
                        Stage addStage = new Stage();
                        addFundDialog.start(addStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fundTableViewBuild();
                    updateAvailableCash();
                }
            });


            MenuItem editFund = new MenuItem("Chỉnh sửa");

            editFund.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    tableViewFundSelected = fundTableView.getSelectionModel().getSelectedItem();
                    try
                    {
                        EditFundDialog editFundDialog = new EditFundDialog();
                        editFundDialog.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fundTableViewBuild();
                    updateAvailableCash();
                }
            });

            MenuItem deleteFund = new MenuItem("Xoá");

            deleteFund.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    tableViewFundSelected = fundTableView.getSelectionModel().getSelectedItem();

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận");
                    confirm.setContentText("Bạn có chắc chắn ?");
                    Optional<ButtonType> result = confirm.showAndWait();

                    if(result.get() == ButtonType.OK)
                    {
                        try
                        {
                            PreparedStatement deleteStatement1 = conn.prepareStatement("DELETE FROM fund_list WHERE ID = ?");
                            PreparedStatement deleteStatement2 = conn.prepareStatement("DELETE FROM income WHERE fundID = ?");

                            deleteStatement1.setInt(1, tableViewFundSelected.getID());
                            deleteStatement2.setInt(1, tableViewFundSelected.getID());

                            deleteStatement1.execute();
                            deleteStatement2.execute();

                            deleteStatement1.close();
                            deleteStatement2.close();

                            //con.close();

                            Alert inform = new Alert(Alert.AlertType.INFORMATION);
                            inform.setTitle("Thông báo");
                            inform.setContentText("Đã xoá thành công");
                            inform.showAndWait();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    fundTableViewBuild();
                    updateAvailableCash();
                }
            });

            fundContextMenu.getItems().addAll(addFund,editFund,deleteFund);
        }

        // Pay table view init
        fundNameCol.setCellValueFactory(new PropertyValueFactory<TableViewFund,String>("fundname"));
        fundDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        fundCashCol.setCellValueFactory(column-> {
            NumberFormat vi = NumberFormat.getInstance(new Locale("vi","VN"));
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(vi.format(column.getValue().getCash()));
            return property;
        });
        fundTableViewBuild();


        // Initialization for pay context menu
        if(account.getPermission().equals("admin") || account.getPermission().equals("manager"))
        {
            MenuItem addPay = new MenuItem("Thêm");

            addPay.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        AddPayDialog addPayDialog = new AddPayDialog();
                        Stage addStage = new Stage();
                        addPayDialog.start(addStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    payTableViewBuild();
                    updateAvailableCash();
                }
            });


            MenuItem editPay = new MenuItem("Chỉnh sửa");

            editPay.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    tableViewPaySelected = payTableView.getSelectionModel().getSelectedItem();
                    try
                    {
                        EditPayDialog editPayDialog = new EditPayDialog();
                        editPayDialog.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    payTableViewBuild();
                    updateAvailableCash();
                }
            });

            MenuItem deletePay = new MenuItem("Xoá");

            deletePay.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    tableViewPaySelected = payTableView.getSelectionModel().getSelectedItem();

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận");
                    confirm.setContentText("Bạn có chắc chắn ?");
                    Optional<ButtonType> result = confirm.showAndWait();

                    if(result.get() == ButtonType.OK)
                    {
                        try
                        {
                            PreparedStatement deleteStatement1 = conn.prepareStatement("DELETE FROM pay_list WHERE ID = ?");

                            deleteStatement1.setInt(1, tableViewPaySelected.getID());

                            deleteStatement1.execute();

                            deleteStatement1.close();

                            //con.close();

                            Alert inform = new Alert(Alert.AlertType.INFORMATION);
                            inform.setTitle("Thông báo");
                            inform.setContentText("Đã xoá thành công");
                            inform.showAndWait();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    payTableViewBuild();
                    updateAvailableCash();
                }
            });

            payContextMenu.getItems().addAll(addPay,editPay,deletePay);
        }

        // Pay table view init
        payNameCol.setCellValueFactory(new PropertyValueFactory<TableViewPay,String>("payname"));
        payDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        payCashCol.setCellValueFactory(column-> {
            NumberFormat vi = NumberFormat.getInstance(new Locale("vi","VN"));
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(vi.format(column.getValue().getCash()));
            return property;
        });
        payTableViewBuild();
        updateAvailableCash();

    }

    public void fundTableViewBuild()
    {
        List<TableViewFund> lFund = new ArrayList<TableViewFund>();
        ObservableList<TableViewFund> data = null;
        try
        {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM fund_list");
            while(rs.next())
            {
                TableViewFund tableViewFund = new TableViewFund();
                tableViewFund.setID(rs.getInt(1));
                tableViewFund.setFundname(rs.getString(2));
                tableViewFund.setCash(rs.getInt(3));
                tableViewFund.setDate(rs.getString(4));
                lFund.add(tableViewFund);
            }
            data = FXCollections.observableArrayList(lFund);
            statement.close();
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        fundTableView.setItems(data);
    }

    public void payTableViewBuild()
    {
        List<TableViewPay> lPay = new ArrayList<TableViewPay>();
        ObservableList<TableViewPay> data = null;
        try
        {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM pay_list");
            while(rs.next())
            {
                TableViewPay tableViewPay = new TableViewPay();
                tableViewPay.setID(rs.getInt(1));
                tableViewPay.setPayname(rs.getString(2));
                tableViewPay.setCash(rs.getInt(3));
                tableViewPay.setDate(rs.getString(4));
                lPay.add(tableViewPay);
            }
            data = FXCollections.observableArrayList(lPay);
            statement.close();
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        payTableView.setItems(data);
    }


    @FXML
    void startAccountManger(ActionEvent event) {
        AccountManage accountManage = new AccountManage();
        try {
            accountManage.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateAvailableCash();
    }

    @FXML
    void startIncomeManager(ActionEvent event) {
        if(account.getPermission().equals("admin") || account.getPermission().equals("manager"))
        {
            Income incomeManage = new Income();
            try {
                incomeManage.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Lỗi");
            error.setContentText("Tài khoản hiện tại không có quyền hạn này");
            error.showAndWait();
        }
        updateAvailableCash();
    }

    @FXML
    void startStudentManager(ActionEvent event) {
        if(account.getPermission().equals("admin") || account.getPermission().equals("manager"))
        {
            StudentManage studentManage = new StudentManage();
            try {
                studentManage.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Lỗi");
            error.setContentText("Tài khoản hiện tại không có quyền hạn này");
            error.showAndWait();
        }
        updateAvailableCash();
    }

    @FXML
    void closeApp(ActionEvent event) throws SQLException {
        conn.close();
        Platform.exit();
    }

    @FXML
    void showAbout(ActionEvent event) {
        Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutDialog.setTitle("Thông tin");
        aboutDialog.setHeaderText("Thông tin về chương trình");
        aboutDialog.setContentText("Thành viên nhóm:\nNguyễn Đình Duy \t 1620054\n" +
                                                    "Trương Huỳnh Đủ \t 1620041\n" +
                                                    "Lê Bá Phước Long \t 1620128");
        aboutDialog.show();
    }

    private int getAvailableCash()
    {
        try {
            Statement statement1 = conn.createStatement();

            ResultSet rs1 = statement1.executeQuery("SELECT SUM(cash)" +
                                         " FROM income JOIN fund_list ON income.fundID = fund_list.ID");

            Statement statement2 = conn.createStatement();
            ResultSet rs2 = statement2.executeQuery("SELECT SUM(cash) FROM pay_list");

            int incomeSum = rs1.getInt(1);
            int paySum = rs2.getInt(1);

            statement1.close();
            statement2.close();

            return incomeSum - paySum;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    void updateAvailableCash()
    {
        currentAvailableCash.setText(String.valueOf(getAvailableCash()));
    }

}
