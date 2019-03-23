package payManage;

public class Pay {
    private int ID;
    private String payname;
    private int cash;
    private String date;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPayname() {
        return payname;
    }

    public void setPayname(String payname) {
        this.payname = payname;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Pay(){};

    public Pay(int ID, String payname, int cash, String date) {
        this.ID = ID;
        this.payname = payname;
        this.cash = cash;
        this.date = date;
    }
}
