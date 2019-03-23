package accountManage;

import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;


public class TableViewAccount extends login.Account{
    private ChoiceBox<String> permissionChoiceBox;

    private HBox operate;

    public TableViewAccount(int ID, String username, String password, String permission) {
        super(ID, username, password, permission);
    }

    public ChoiceBox<String> getPermissionChoiceBox() {
        return permissionChoiceBox;
    }

    public void setPermissionChoiceBox(ChoiceBox<String> permissionChoiceBox) {
        this.permissionChoiceBox = permissionChoiceBox;
    }

    public HBox getOperate() {
        return operate;
    }

    public void setOperate(HBox operate) {
        this.operate = operate;
    }
}

