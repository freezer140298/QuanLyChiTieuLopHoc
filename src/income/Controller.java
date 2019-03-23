package income;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static login.Controller.conn;

class FundSelect {
    private int ID;
    private String fundname;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFundname() {
        return fundname;
    }

    public void setFundname(String fundname) {
        this.fundname = fundname;
    }

    @Override
    public String toString()
    {
        return fundname;
    }
}


public class Controller implements Initializable {

    private FundSelect currentFundSelect;

    @FXML
    private TableView<FundCollect> incomeTableView;

    @FXML
    private TableColumn<FundCollect, String> studentNameCol;

    @FXML
    private TableColumn<FundCollect,CheckBox> collectedCol;

    @FXML
    private ChoiceBox<FundSelect> fundChoiceBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fundChoiceBoxBuild();

        // Listener for choice box
        fundChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FundSelect>() {
            @Override
            public void changed(ObservableValue<? extends FundSelect> observable, FundSelect oldValue, FundSelect newValue) {
                currentFundSelect = fundChoiceBox.getValue();
                collectTableViewBuild();
            }
        });

        studentNameCol.setCellValueFactory(new PropertyValueFactory<FundCollect,String>("studentName"));
        collectedCol.setCellValueFactory(new PropertyValueFactory<FundCollect,CheckBox>("checked"));
        incomeTableView.setPlaceholder(new Label("Chọn quỹ cần thu"));
    }


    private void fundChoiceBoxBuild()
    {
        try
        {
            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery("SELECT ID,fundname FROM fund_list");

            ArrayList<FundSelect> lFundSelectChoiceBoxes = new ArrayList<FundSelect>();

            while(rs.next())
            {
                FundSelect fundSelect = new FundSelect();
                fundSelect.setID(rs.getInt(1));
                fundSelect.setFundname(rs.getString(2));
                lFundSelectChoiceBoxes.add(fundSelect);
            }

            fundChoiceBox.setItems(FXCollections.observableArrayList(lFundSelectChoiceBoxes));
            statement.close();
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void collectTableViewBuild()
    {
        List<FundCollect> lCollect = new ArrayList<FundCollect>();

        try
        {
            PreparedStatement statement = conn.prepareStatement("SELECT ID,fullname, CASE WHEN student.ID in (SELECT income.studentID FROM income WHERE income.fundID = ?) THEN 1 ELSE 0 END AS received" +
                                                                    " FROM student");

            statement.setInt(1,currentFundSelect.getID());

            ResultSet rs = statement.executeQuery();
            //con.close();

            while(rs.next())
            {
                FundCollect fundCollect = new FundCollect();
                fundCollect.setStudentID(rs.getInt(1));
                fundCollect.setStudentName(rs.getString(2));

                CheckBox checkBox = new CheckBox();

                if(rs.getInt(3) == 0)
                {
                    checkBox.setSelected(false);
                }
                else
                {
                    checkBox.setSelected(true);
                }

                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(newValue == true)
                        {
                            // INSERT to income table
                            try
                            {
                                PreparedStatement updateStatement = conn.prepareStatement("INSERT INTO income(fundID,studentID,date)" +
                                                                                                " VALUES(?,?,?)");
                                updateStatement.setInt(1,currentFundSelect.getID());
                                updateStatement.setInt(2,fundCollect.getStudentID());
                                updateStatement.setString(3, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                                updateStatement.execute();
                                updateStatement.close();
                                //con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            try
                            {
                                // Delete from income table
                                PreparedStatement updateStatement = conn.prepareStatement("DELETE FROM income WHERE fundID = ? AND studentID = ?");
                                updateStatement.setInt(1,currentFundSelect.getID());
                                updateStatement.setInt(2,fundCollect.getStudentID());

                                updateStatement.execute();
                                updateStatement.close();
                                //con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                fundCollect.setChecked(checkBox);

                lCollect.add(fundCollect);
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        incomeTableView.setItems(FXCollections.observableArrayList(lCollect));
    }
}
