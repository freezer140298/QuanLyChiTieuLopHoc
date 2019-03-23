package fundManage;

public class Fund {
    private int ID;
    private String fundname;
    private int cash;
    private String date;

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

    public Fund(){};

    public Fund(int ID, String fundname, int cash, String date) {
        this.ID = ID;
        this.fundname = fundname;
        this.cash = cash;
        this.date = date;
    }
}
