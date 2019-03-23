package studentManage;


public class TableViewStudent extends Student
{
    private int ID; // ID from DB
    private String sexString;

    public int getID()
    {
        return this.ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public String getSexString() {
        return sexString;
    }

    public void setSexString(int sex) {
        if(sex == 0)
        {
            this.sexString = "Nam";
        }
        else
        {
            this.sexString = "Nữ";
        }
    }

}