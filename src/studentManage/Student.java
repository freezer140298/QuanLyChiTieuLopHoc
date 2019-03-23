package studentManage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student {
    private String fullname,email,phone,address;
    private int sex; // 0 : Male        1: Female

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private boolean emailValidate(final String hex)
    {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    private boolean phoneValidate(final String phone)
    {
        if(phone.length() == 9 || phone.length() == 10)
        {
            return true;
        }
        else
            return false;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(emailValidate(email))
        {
            this.email = email;
        }
        else
        {
            throw new IllegalArgumentException("EmailNotVaild");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if(phoneValidate(phone))
        {
            this.phone = phone;
        }
        else
        {
            throw new IllegalArgumentException("PhoneNotValid");
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
