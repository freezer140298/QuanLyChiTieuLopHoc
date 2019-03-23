package login;

public class Account {
    private int ID;
    private String username;
    private String password;
    private String permission; // admin, manager, viewer

    public Account(int ID,String username,String password,String permission)
    {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.permission = permission;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}
